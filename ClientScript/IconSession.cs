using System;
using System.Collections.Concurrent;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using UnityEngine;

public class IconSession : ISession
{
    private static IconSession instance = new IconSession();
    private TcpClient client;
    private NetworkStream stream;
    private IMessageHandler messageHandler;
    private Thread receiver;
    private Thread sender;
    private readonly ConcurrentQueue<Message> sendQueue = new ConcurrentQueue<Message>();
    private readonly AutoResetEvent sendEvent = new AutoResetEvent(false);
    private volatile bool running;
    private volatile bool isConnecting;
    private bool handshake;

    public IconSession()
    {
        onConnectionEstablished += OnConnected;
    }

    static void OnConnected()
    {
        try
        {
            IconSession.gI().sendInit(Char.myCharz().charID);
        }
        catch (Exception e)
        {
            Debug.LogException(e);
        }
    }

    public static IconSession gI()
    {
        return instance;
    }

    public bool isConnected()
    {
        return client != null && client.Connected && stream != null && !isConnecting;
    }

    public void setHandler(IMessageHandler handler)
    {
        messageHandler = handler;
    }

    public void connect(string host, int port)
    {
        if (isConnected() || isConnecting) return;
        Task.Run(async () =>
        {
            bool ok = await connectAsync(host, port);
            if (ok) onConnectionEstablished?.Invoke(); else onConnectionFailed?.Invoke();
        });
    }

    public async Task<bool> connectAsync(string host, int port, int timeoutMs = 5000)
    {
        isConnecting = true;
        TcpClient temp = new TcpClient();
        try
        {
            var connectTask = temp.ConnectAsync(host, port);
            var timeoutTask = Task.Delay(timeoutMs);
            var done = await Task.WhenAny(connectTask, timeoutTask);
            if (done == timeoutTask) throw new TimeoutException();
            await connectTask;
            stream = temp.GetStream();
            client = temp;
            running = true;
            receiver = new Thread(run) { IsBackground = true };
            receiver.Start();
            sender = new Thread(runSender) { IsBackground = true };
            sender.Start();
            isConnecting = false;
            return true;
        }
        catch
        {
            try { temp.Close(); } catch { }
            isConnecting = false;
            return false;
        }
    }

    private void run()
    {
        try
        {
            while (running && client != null && client.Connected)
            {
                Message msg = readMessage();
                if (msg != null)
                {
                    messageHandler?.onMessage(msg);
                    msg.cleanup();
                }
                else
                {
                    Thread.Sleep(10);
                }
            }
        }
        catch (Exception e)
        {
            Debug.LogError("Icon receiver error: " + e.Message);
        }
        finally
        {
            close();
        }
    }

    private void runSender()
    {
        try
        {
            while (running)
            {
                sendEvent.WaitOne();
                while (sendQueue.TryDequeue(out Message m))
                {
                    try
                    {
                        writeMessage(m);
                    }
                    catch (Exception e)
                    {
                        Debug.LogError("Icon send error: " + e.Message);
                    }
                    finally
                    {
                        m.cleanup();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Debug.LogError("Icon sender error: " + e.Message);
        }
    }

    private Message readMessage()
    {
        try
        {
            if (stream == null || !stream.CanRead) return null;
            if (!stream.DataAvailable) { Thread.Sleep(10); return null; }
            int cmd = stream.ReadByte();
            if (cmd < 0) return null;
            int b1 = stream.ReadByte();
            int b2 = stream.ReadByte();
            int b3 = stream.ReadByte();
            if (b1 < 0 || b2 < 0 || b3 < 0) return null;
            int size = (b1 << 16) | (b2 << 8) | b3;
            byte[] data = new byte[size];
            int read = 0;
            while (read < size)
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
            Debug.LogError("Icon read error: " + ex.Message);
            return null;
        }
    }

    public void sendMessage(Message message)
    {
        if (!isConnected()) return;
        sendQueue.Enqueue(message);
        sendEvent.Set();
    }

    private void writeMessage(Message message)
    {
        sbyte[] data = message.getData();
        stream.WriteByte((byte)message.command);
        int len = data != null ? data.Length : 0;
        stream.WriteByte((byte)(len >> 16));
        stream.WriteByte((byte)(len >> 8));
        stream.WriteByte((byte)len);
        if (len > 0)
        {
            byte[] raw = new byte[len];
            Buffer.BlockCopy(data, 0, raw, 0, len);
            stream.Write(raw, 0, raw.Length);
        }
        stream.Flush();
    }

    public void sendInit(int id)
    {
        if (handshake || !isConnected()) return;
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
        try { stream?.Close(); } catch { }
        try { client?.Close(); } catch { }
        stream = null;
        client = null;
        handshake = false;
    }

    public event Action onConnectionEstablished;
    public event Action onConnectionFailed;
}
