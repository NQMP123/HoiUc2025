﻿using System;
using System.Buffers;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading;
using UnityEngine;
using ThreadPriority = System.Threading.ThreadPriority;

public class Session_ME2 : ISession
{
    public class Sender
    {
        private readonly object lockObject = new object();
        public List<Message> sendingMessage;
        private bool isRunning = true;

        public Sender()
        {
            sendingMessage = new List<Message>();
        }

        [MethodImpl(MethodImplOptions.AggressiveInlining)]
        public void AddMessage(Message message)
        {
            lock (lockObject)
            {
                sendingMessage.Add(message);
            }
        }

        public void Stop()
        {
            isRunning = false;
        }

        public void run()
        {
            while (isRunning && connected)
            {
                try
                {
                    if (getKeyComplete)
                    {
                        // Xử lý message hàng loạt để giảm lock
                        Message[] messagesToSend = null;
                        int count = 0;

                        lock (lockObject)
                        {
                            if (sendingMessage.Count > 0)
                            {
                                count = sendingMessage.Count;
                                messagesToSend = new Message[count];
                                sendingMessage.CopyTo(messagesToSend);
                                sendingMessage.Clear();
                            }
                        }

                        if (messagesToSend != null)
                        {
                            for (int i = 0; i < count; i++)
                            {
                                doSendMessage(messagesToSend[i]);
                            }
                        }
                    }
                    // Ngủ ngắn hơn để phản hồi nhanh hơn với tin nhắn mới
                    Thread.Sleep(1);
                }
                catch (Exception ex)
                {
                    Res.outz("error send message! " + ex.ToString());
                    // Ngủ ngắn sau lỗi để tránh sử dụng CPU cao
                    Thread.Sleep(10);
                }
            }
        }
    }

    private class MessageCollector
    {
        private readonly byte[] readBuffer = new byte[8192]; // Buffer lớn hơn
        private bool isRunning = true;

        public void Stop()
        {
            isRunning = false;
        }

        public void run()
        {
            try
            {
                while (isRunning && connected)
                {
                    Message message = readMessage();
                    if (message == null)
                    {
                        // Ngắt kết nối được phát hiện
                        break;
                    }
                    try
                    {
                        if (message.command == -27)
                        {
                            getKey(message);
                        }
                        else
                        {
                            onRecieveMsg(message);
                        }
                    }
                    catch (Exception ex)
                    {
                        Cout.println("LOI NHAN MESS THU 1: " + ex.ToString());
                    }

                    // Giảm thời gian ngủ để xử lý tin nhắn nhanh hơn
                    Thread.Sleep(1);
                }
            }
            catch (Exception ex3)
            {
                Debug.LogError("error read message: " + ex3.ToString());
            }

            if (!connected)
            {
                return;
            }

            if (messageHandler != null)
            {
                if (currentTimeMillis() - timeConnected > 500)
                {
                    messageHandler.onDisconnected(isMainSession);
                }
                else
                {
                    messageHandler.onConnectionFail(isMainSession);
                }
            }

            cleanNetwork();
        }

        private void getKey(Message message)
        {
            try
            {
                sbyte b = message.reader().readSByte();
                key = new sbyte[b];
                for (int i = 0; i < b; i++)
                {
                    key[i] = message.reader().readSByte();
                }

                // Tối ưu vòng lặp XOR key
                for (int j = 0; j < key.Length - 1; j++)
                {
                    key[j + 1] = (sbyte)(key[j + 1] ^ key[j]);
                }

                getKeyComplete = true;
                GameMidlet.IP2 = message.reader().readUTF();
                GameMidlet.PORT2 = message.reader().readInt();
                GameMidlet.isConnect2 = message.reader().readByte() != 0;

                if (isMainSession && GameMidlet.isConnect2)
                {
                    GameCanvas.connect2();
                }
            }
            catch (Exception ex)
            {
                Debug.LogWarning("Lỗi khi xử lý getKey: " + ex.ToString());
            }
        }

        private Message readMessage2(sbyte cmd)
        {
            try
            {
                int num = readKey(dis.ReadSByte()) + 128;
                int num2 = readKey(dis.ReadSByte()) + 128;
                int num3 = readKey(dis.ReadSByte()) + 128;
                int num4 = (num3 * 256 + num2) * 256 + num;
                Cout.LogError("SIZE = " + num4);

                //if (num4 <= 0 || num4 > 1000000) // Kiểm tra kích thước hợp lý
                //{
                //    Debug.LogError("Invalid message size in readMessage2: " + num4);
                //    return null;
                //}

                // Sử dụng ArrayPool để tránh GC
                sbyte[] array = ArrayPool<sbyte>.Shared.Rent(num4);

                try
                {
                    // Đọc dữ liệu vào array
                    byte[] src = dis.ReadBytes(num4);
                    Buffer.BlockCopy(src, 0, array, 0, num4);

                    recvByteCount += 5 + num4;
                    int num6 = recvByteCount + sendByteCount;
                    strRecvByteCount = num6 / 1024 + "." + num6 % 1024 / 102 + "Kb";

                    if (getKeyComplete)
                    {
                        for (int i = 0; i < num4; i++)
                        {
                            array[i] = readKey(array[i]);
                        }
                    }

                    byte[] raw = new byte[num4];
                    Buffer.BlockCopy(array, 0, raw, 0, num4);
                    sbyte[] decoded;
                    try
                    {
                        string str = Encoding.ASCII.GetString(raw);
                        byte[] d = Convert.FromBase64String(str);
                        decoded = new sbyte[d.Length];
                        Buffer.BlockCopy(d, 0, decoded, 0, d.Length);
                    }
                    catch (Exception)
                    {
                        decoded = array;
                    }

                    // Sử dụng constructor 3 tham số
                    return new Message(cmd, decoded, decoded.Length);
                }
                catch (Exception ex)
                {
                    ArrayPool<sbyte>.Shared.Return(array);
                    Debug.LogError("Exception in readMessage2 data processing: " + ex.ToString());
                    return null;
                }
            }
            catch (Exception ex)
            {
                Debug.LogError("Error in readMessage2: " + ex.ToString());
                return null;
            }
        }

        private Message readMessage()
        {
            try
            {
                if (dis == null || !dis.BaseStream.CanRead)
                {
                    Debug.Log("Stream is not available for reading");
                    return null;
                }

                sbyte b = dis.ReadSByte();
                if (getKeyComplete)
                {
                    b = readKey(b);
                }

                if (b == -32 || b == -66 || b == 11 || b == -67 || b == -74 || b == -87)
                {
                    return readMessage2(b);
                }

                int num;
                if (getKeyComplete)
                {
                    sbyte b2 = dis.ReadSByte();
                    sbyte b3 = dis.ReadSByte();
                    num = ((readKey(b2) & 0xFF) << 8) | (readKey(b3) & 0xFF);
                }
                else
                {
                    sbyte b4 = dis.ReadSByte();
                    sbyte b5 = dis.ReadSByte();
                    num = (b4 & 0xFF) << 8 | (b5 & 0xFF);
                }

                //if (num < 0 || num > 1000000) // Kiểm tra kích thước hợp lý
                //{
                //    Debug.LogError("Invalid message size in readMessage: " + num);
                //    return null;
                //}

                // Sử dụng ArrayPool để tránh GC
                sbyte[] array = ArrayPool<sbyte>.Shared.Rent(num);

                try
                {
                    // Đọc dữ liệu vào array
                    byte[] src = dis.ReadBytes(num);
                    Buffer.BlockCopy(src, 0, array, 0, num);

                    recvByteCount += 5 + num;
                    int num4 = recvByteCount + sendByteCount;
                    strRecvByteCount = num4 / 1024 + "." + num4 % 1024 / 102 + "Kb";

                    if (getKeyComplete)
                    {
                        for (int i = 0; i < num; i++)
                        {
                            array[i] = readKey(array[i]);
                        }
                    }

                    byte[] raw = new byte[num];
                    Buffer.BlockCopy(array, 0, raw, 0, num);
                    sbyte[] decoded;
                    try
                    {
                        string str = Encoding.ASCII.GetString(raw);
                        byte[] d = Convert.FromBase64String(str);
                        decoded = new sbyte[d.Length];
                        Buffer.BlockCopy(d, 0, decoded, 0, d.Length);
                    }
                    catch (Exception)
                    {
                        decoded = array;
                    }

                    // Sử dụng constructor 3 tham số
                    return new Message(b, decoded, decoded.Length);
                }
                catch (Exception ex)
                {
                    ArrayPool<sbyte>.Shared.Return(array);
                    Debug.LogError("Exception in readMessage data processing: " + ex.ToString());
                    return null;
                }
            }
            catch (Exception ex)
            {
                Debug.LogError("Error in readMessage: " + ex.ToString());
                return null;
            }
        }
    }

    protected static Session_ME2 instance = new Session_ME2();

    private static NetworkStream dataStream;

    private static BinaryReader dis;

    private static BinaryWriter dos;

    public static IMessageHandler messageHandler;

    public static bool isMainSession = true;

    private static TcpClient sc;

    public static bool connected;

    public static bool connecting;

    private static Sender sender = new Sender();

    public static Thread initThread;

    public static Thread collectorThread;

    public static Thread sendThread;

    public static int sendByteCount;

    public static int recvByteCount;

    private static bool getKeyComplete;

    public static sbyte[] key = null;

    private static sbyte curR;

    private static sbyte curW;

    private static int timeConnected;

    private long lastTimeConn;

    public static string strRecvByteCount = string.Empty;

    public static bool isCancel;

    private string host;

    private int port;

    // Sử dụng ConcurrentQueue thay vì MyVector để an toàn thread
    public static MyVector recieveMsg = new MyVector();
    private static readonly object recieveMsgLock = new object();

    // Thêm trạng thái cho session
    private bool isDisposed = false;

    // Tăng timeout cho kết nối
    private const int CONNECTION_TIMEOUT = 10000; // 10 giây

    public Session_ME2()
    {
        Debug.Log("init Session_ME2");
    }

    public void clearSendingMessage()
    {
        sender.sendingMessage.Clear();
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public static Session_ME2 gI()
    {
        if (instance == null)
        {
            instance = new Session_ME2();
        }
        return instance;
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public bool isConnected()
    {
        return connected && sc != null && sc.Connected && dataStream != null;
    }

    public void setHandler(IMessageHandler msgHandler)
    {
        messageHandler = msgHandler;
    }

    public void connect(string host, int port)
    {
        if (!connected && !connecting)
        {
            this.host = host;
            this.port = port;
            getKeyComplete = false;
            sc = null;
            Debug.Log("connecting...!");
            Debug.Log("host: " + host);
            Debug.Log("port: " + port);
            initThread = new Thread(NetworkInit);
            initThread.IsBackground = true; // Đảm bảo thread sẽ đóng khi ứng dụng tắt
            initThread.Start();
        }
    }

    private void NetworkInit()
    {
        isCancel = false;
        connecting = true;
        Thread.CurrentThread.Priority = System.Threading.ThreadPriority.Highest;
        connected = false; // Đặt false cho đến khi kết nối thành công

        try
        {
            // Đặt timeout cho kết nối
            var connectTask = System.Threading.Tasks.Task.Run(() => doConnect(host, port));
            bool success = connectTask.Wait(CONNECTION_TIMEOUT);

            if (!success)
            {
                throw new TimeoutException("Kết nối đến server quá thời gian chờ");
            }

            if (connected)
            {
                messageHandler.onConnectOK(isMainSession);
            }
        }
        catch (Exception ex)
        {
            Debug.LogError("NetworkInit error: " + ex.ToString());
            if (messageHandler != null)
            {
                close();
                messageHandler.onConnectionFail(isMainSession);
            }
        }
        finally
        {
            connecting = false;
        }
    }

    public void doConnect(string host, int port)
    {
        try
        {
            sc = new TcpClient();
            sc.ReceiveBufferSize = 8192;
            sc.SendBufferSize = 8192;
            sc.NoDelay = true; // Tắt Nagle algorithm để giảm độ trễ

            sc.Connect(host, port);
            dataStream = sc.GetStream();

            // Đặt timeout để tránh bị treo khi đọc/ghi
            dataStream.ReadTimeout = 30000; // 30 giây
            dataStream.WriteTimeout = 30000;

            dis = new BinaryReader(dataStream, new UTF8Encoding());
            dos = new BinaryWriter(dataStream, new UTF8Encoding());

            connected = true;

            // Tạo và bắt đầu thread gửi
            sendThread = new Thread(sender.run);
            sendThread.IsBackground = true;
            sendThread.Priority = ThreadPriority.AboveNormal;
            sendThread.Start();

            // Tạo và bắt đầu thread nhận
            MessageCollector collector = new MessageCollector();
            collectorThread = new Thread(collector.run);
            collectorThread.IsBackground = true;
            collectorThread.Priority = ThreadPriority.AboveNormal;
            collectorThread.Start();

            timeConnected = currentTimeMillis();
            connecting = false;

            // Gửi message yêu cầu key ngay sau khi kết nối
            doSendMessage(new Message(-27));
            Cout.LogError("Connected to " + host + ":" + port);
        }
        catch (Exception ex)
        {
            Debug.LogError("doConnect error: " + ex.ToString());
            cleanNetwork();
            throw;
        }
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public void sendMessage(Message message)
    {
        Res.outz("SEND MSG: " + message.command);
        sender.AddMessage(message);
    }

    private static void doSendMessage(Message m)
    {
        sbyte[] data = m.getData();
        if (data != null)
        {
            byte[] raw = new byte[data.Length];
            Buffer.BlockCopy(data, 0, raw, 0, data.Length);
            string encoded = Convert.ToBase64String(raw);
            byte[] encBytes = Encoding.ASCII.GetBytes(encoded);
            sbyte[] encSBytes = new sbyte[encBytes.Length];
            Buffer.BlockCopy(encBytes, 0, encSBytes, 0, encBytes.Length);
            data = encSBytes;
        }
        try
        {
            if (getKeyComplete)
            {
                sbyte value = writeKey(m.command);
                dos.Write(value);
            }
            else
            {
                dos.Write(m.command);
            }

            if (data != null)
            {
                int num = data.Length;
                if (getKeyComplete)
                {
                    int num2 = writeKey((sbyte)(num >> 8));
                    dos.Write((sbyte)num2);
                    int num3 = writeKey((sbyte)(num & 0xFF));
                    dos.Write((sbyte)num3);
                }
                else
                {
                    dos.Write((ushort)num);
                }

                if (getKeyComplete)
                {
                    for (int i = 0; i < data.Length; i++)
                    {
                        sbyte value2 = writeKey(data[i]);
                        dos.Write(value2);
                    }
                }
                else
                {
                    // Sử dụng BlockCopy để chuyển đổi
                    byte[] byteData = new byte[data.Length];
                    Buffer.BlockCopy(data, 0, byteData, 0, data.Length);
                    dos.Write(byteData);
                }

                sendByteCount += 5 + data.Length;
            }
            else
            {
                if (getKeyComplete)
                {
                    int num4 = 0;
                    int num5 = writeKey((sbyte)(num4 >> 8));
                    dos.Write((sbyte)num5);
                    int num6 = writeKey((sbyte)(num4 & 0xFF));
                    dos.Write((sbyte)num6);
                }
                else
                {
                    dos.Write((ushort)0);
                }
                sendByteCount += 5;
            }

            // Đảm bảo dữ liệu được gửi ngay lập tức
            dos.Flush();
        }
        catch (Exception ex)
        {
            Debug.LogError("doSendMessage error: " + ex.ToString());

            // Nếu xảy ra lỗi khi gửi, đánh dấu mất kết nối
            connected = false;
        }
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public static sbyte readKey(sbyte b)
    {
        sbyte result = (sbyte)((key[curR] & 0xFF) ^ (b & 0xFF));
        curR++;
        if (curR >= key.Length)
        {
            curR = (sbyte)(curR % (sbyte)key.Length);
        }
        return result;
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public static sbyte writeKey(sbyte b)
    {
        sbyte result = (sbyte)((key[curW] & 0xFF) ^ (b & 0xFF));
        curW++;
        if (curW >= key.Length)
        {
            curW = (sbyte)(curW % (sbyte)key.Length);
        }
        return result;
    }

    public static void onRecieveMsg(Message msg)
    {
        if (Thread.CurrentThread.Name == Main.mainThreadName)
        {
            messageHandler.onMessage(msg);
        }
        else
        {
            lock (recieveMsgLock)
            {
                recieveMsg.addElement(msg);
            }
        }
    }

    public static void enqueueMessage(Message msg)
    {
        lock (recieveMsgLock)
        {
            recieveMsg.addElement(msg);
        }
    }

    public static void update()
    {
        int processLimit = 10; // Giới hạn số lượng message xử lý mỗi frame
        int processCount = 0;

        while (processCount < processLimit)
        {
            Message message = null;

            lock (recieveMsgLock)
            {
                if (recieveMsg.size() > 0)
                {
                    message = (Message)recieveMsg.elementAt(0);
                    recieveMsg.removeElementAt(0);
                }
            }

            if (message == null)
            {
                break;
            }

            if (Controller.isStopReadMessage)
            {
                // Đưa message trở lại hàng đợi
                lock (recieveMsgLock)
                {
                    recieveMsg.insertElementAt(message, 0);
                }
                break;
            }

            try
            {
                messageHandler.onMessage(message);
            }
            catch (Exception ex)
            {
                Debug.LogError("Error processing message: " + ex.ToString());
            }

            processCount++;
        }
    }

    public void close()
    {
        if (!isDisposed)
        {
            cleanNetwork();
            isDisposed = true;
        }
    }

    private static void cleanNetwork()
    {
        try
        {
            key = null;
            curR = 0;
            curW = 0;

            // Dừng các thread trước khi đóng kết nối
            connected = false;
            connecting = false;

            // Giải phóng tài nguyên theo thứ tự an toàn
            if (dis != null)
            {
                try { dis.Close(); } catch { }
                dis = null;
            }

            if (dos != null)
            {
                try { dos.Close(); } catch { }
                dos = null;
            }

            if (dataStream != null)
            {
                try { dataStream.Close(); } catch { }
                dataStream = null;
            }

            if (sc != null)
            {
                try { sc.Close(); } catch { }
                sc = null;
            }

            // Dừng các thread
            if (sender != null)
            {
                sender.Stop();
            }

            sendThread = null;
            collectorThread = null;

            // Đảm bảo GC thu dọn các tài nguyên
            mSystem.gcc();
        }
        catch (Exception ex)
        {
            Debug.LogError("cleanNetwork error: " + ex.ToString());
        }
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public static int currentTimeMillis()
    {
        return Environment.TickCount;
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public static byte convertSbyteToByte(sbyte var)
    {
        return var > 0 ? (byte)var : (byte)(var + 256);
    }

    public static byte[] convertSbyteToByte(sbyte[] var)
    {
        byte[] array = new byte[var.Length];

        // Sử dụng Buffer.BlockCopy cho hiệu suất cao
        Buffer.BlockCopy(var, 0, array, 0, var.Length);

        // Sửa các byte âm
        for (int i = 0; i < var.Length; i++)
        {
            if (var[i] < 0)
            {
                array[i] = (byte)(var[i] + 256);
            }
        }

        return array;
    }
}