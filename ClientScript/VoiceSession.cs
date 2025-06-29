﻿using System;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Concurrent;
using UnityEngine;
using System.Collections.Generic;

public class VoiceSession : ISession
{
    private static VoiceSession instance = new VoiceSession();
    private TcpClient client;
    private NetworkStream stream;
    private IMessageHandler messageHandler;
    private Thread receiver;
    private Thread sender;
    private readonly ConcurrentQueue<Message> sendQueue = new ConcurrentQueue<Message>();
    private readonly AutoResetEvent sendEvent = new AutoResetEvent(false);
    private volatile bool running;
    private bool handshake;
    private volatile bool isConnecting;

    public VoiceSession()
    {
        onConnectionEstablished += OnVoiceConnected;
        onConnectionFailed += OnVoiceConnectionFailed;
    }

    static void OnVoiceConnected()
    {
        try
        {
            Debug.Log("Voice chat connected!");
            VoiceSession.gI().sendInit(Char.myCharz().charID);
        }
        catch (Exception e)
        {
            Debug.LogException(e);
        }
    }

    static void OnVoiceConnectionFailed()
    {
        Debug.Log("Voice chat connection failed!");
    }

    public static VoiceSession gI()
    {
        return instance;
    }

    public bool isConnected()
    {
        return client != null && client.Connected && !isConnecting && stream != null;
    }

    public void setHandler(IMessageHandler handler)
    {
        messageHandler = handler;
    }

    // Async version - không block main thread
    public async Task<bool> connectAsync(string host, int port, int timeoutMs = 5000)
    {
        if (isConnected() || isConnecting) return false;

        isConnecting = true;
        TcpClient tempClient = null;
        NetworkStream tempStream = null;

        try
        {
            tempClient = new TcpClient();

            // Async connect với timeout
            var connectTask = tempClient.ConnectAsync(host, port);
            var timeoutTask = System.Threading.Tasks.Task.Delay(timeoutMs);

            var completedTask = await System.Threading.Tasks.Task.WhenAny(connectTask, timeoutTask);

            if (completedTask == timeoutTask)
            {
                tempClient?.Close();
                throw new TimeoutException("Connection timeout");
            }

            // Kiểm tra nếu connect task có exception
            await connectTask; // Rethrow exception nếu có

            if (!tempClient.Connected)
            {
                throw new Exception("Connection failed - not connected");
            }

            // Thiết lập stream
            tempStream = tempClient.GetStream();
            if (tempStream == null)
            {
                throw new Exception("Failed to get network stream");
            }

            // Chỉ gán vào instance variables khi mọi thứ đã setup thành công
            client = tempClient;
            stream = tempStream;
            running = true;
            messages.Clear();
            // Start threads
            receiver = new Thread(run);
            receiver.IsBackground = true;
            receiver.Start();

            sender = new Thread(runSender);
            sender.IsBackground = true;
            sender.Start();

            isConnecting = false;
            Debug.LogError("Connect Success");
            return true;
        }
        catch (Exception ex)
        {
            Debug.LogError($"Connection failed: {ex.Message}");

            // Cleanup temporary objects
            try { tempStream?.Close(); } catch { }
            try { tempClient?.Close(); } catch { }

            // Reset state
            isConnecting = false;
            return false;
        }
    }

    // Sync version chạy trong background thread
    public void connect(string host, int port)
    {
        Debug.LogError("connect voiceServer : " + host + ":" + port);
        if (isConnected() || isConnecting) return;

        // Chạy connect trong background thread
        System.Threading.Tasks.Task.Run(async () =>
        {
            bool success = await connectAsync(host, port);
            if (success)
            {
                Debug.LogError("connect success");
                onConnectionEstablished?.Invoke();
            }
            else
            {
                Debug.LogError("connect failed");
                onConnectionFailed?.Invoke();
            }
        });
    }

    // Events để thông báo kết quả connection
    public event Action onConnectionEstablished;
    public event Action onConnectionFailed;
    private List<Message> messages = new List<Message>();
    private void run()
    {
        try
        {
            while (client != null && client.Connected && running)
            {
                Message msg = readMessage();
                if (msg != null)
                {
                    //messageHandler?.onMessage(msg);//not run on main thread
                    messages.Add(msg);
                }
                else
                {
                    // Nếu không đọc được message, có thể connection bị đóng
                    Thread.Sleep(10);
                }
            }
        }
        catch (Exception ex)
        {
            Debug.LogError($"Receiver error: {ex.Message}");
        }
        finally
        {
            close();
        }
    }

    public void update()
    {
        while (messages.Count > 0)
        {
            try
            {
                var msgFirst = messages[0];
                messages.RemoveAt(0);
                messageHandler?.onMessage(msgFirst);
            }
            catch
            {
            }
        }
    }

    private void runSender()
    {
        try
        {
            while (running)
            {
                sendEvent.WaitOne();
                while (sendQueue.TryDequeue(out Message msg))
                {
                    try
                    {
                        if (client != null && client.Connected && stream != null)
                        {
                            writeMessage(msg);
                        }
                    }
                    catch (Exception ex)
                    {
                        Debug.LogError($"Send error: {ex.Message}");
                    }
                    finally
                    {
                        msg.cleanup();
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Debug.LogError($"Sender error: {ex.Message}");
        }
    }

    private Message readMessage()
    {
        try
        {
            if (stream == null || !stream.CanRead)
                return null;

            // Thêm timeout cho read operations
            if (!stream.DataAvailable)
            {
                Thread.Sleep(10); // Tránh CPU spinning
                return null;
            }

            int cmd = stream.ReadByte();
            if (cmd < 0) return null;

            int b1 = stream.ReadByte();
            int b2 = stream.ReadByte();
            int b3 = stream.ReadByte();

            if (b1 < 0 || b2 < 0 || b3 < 0) return null;

            int size = (b1 << 16) | (b2 << 8) | b3;

            // Giới hạn kích thước message để tránh memory issues
            if (size > 1024 * 1024) // 1MB limit
            {
                throw new InvalidDataException("Message too large");
            }

            byte[] data = new byte[size];
            int read = 0;
            while (read < size && running)
            {
                int r = stream.Read(data, read, size - read);
                if (r <= 0) return null;
                read += r;
            }

            sbyte[] sdata = new sbyte[size];
            Buffer.BlockCopy(data, 0, sdata, 0, size);
            return new Message((sbyte)cmd, sdata);
        }
        catch (Exception ex)
        {
            Debug.LogError($"Read message error: {ex.Message}");
            return null;
        }
    }

    public void sendMessage(Message message)
    {
        if (!isConnected())
        {
            Debug.LogWarning("Cannot send message - not connected");
            return;
        }

        Debug.LogError("Send message voice: " + message.command);
        sendQueue.Enqueue(message);
        sendEvent.Set();
    }

    private void writeMessage(Message message)
    {
        try
        {
            if (stream == null || !stream.CanWrite)
            {
                throw new InvalidOperationException("Stream is not available for writing");
            }

            sbyte[] data = message.getData();
            stream.WriteByte((byte)message.command);
            int len = data != null ? data.Length : 0;
            stream.WriteByte((byte)(len >> 16));
            stream.WriteByte((byte)(len >> 8));
            stream.WriteByte((byte)(len));
            if (len > 0)
            {
                byte[] raw = new byte[len];
                Buffer.BlockCopy(data, 0, raw, 0, len);
                stream.Write(raw, 0, raw.Length);
            }
            stream.Flush();
        }
        catch (Exception ex)
        {
            Debug.LogError($"Write message error: {ex.Message}");
            throw;
        }
    }

    public void sendInit(int id)
    {
        if (handshake || !isConnected())
        {
            Debug.LogWarning($"Cannot send init - handshake: {handshake}, connected: {isConnected()}");
            return;
        }

        Message msg = new Message(-100);
        msg.writer().writeInt(id);
        sendMessage(msg);
        msg.cleanup();
        handshake = true;
    }

    public void close()
    {
        running = false;
        isConnecting = false;

        try { sendEvent.Set(); } catch { }

        // Đợi threads kết thúc
        try
        {
            receiver?.Join(1000); // Đợi tối đa 1 giây
            sender?.Join(1000);
        }
        catch { }

        try { stream?.Close(); } catch { }
        try { client?.Close(); } catch { }

        // Reset state
        handshake = false;
        stream = null;
        client = null;
    }
}