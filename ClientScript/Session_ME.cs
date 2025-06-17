using System;
using System.Buffers;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading;
using UnityEngine;


public class Session_ME : ISession
{
    public class Sender
    {
        private readonly object lockObject = new object();
        public List<Message> sendingMessage;
        private readonly byte[] tempBuffer = new byte[4096]; // Buffer tạm cho việc đọc/ghi
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
                    // Làm ngắn thời gian ngủ để phục hồi nhanh sau lỗi
                    Thread.Sleep(10);
                }
            }
        }
    }

    private class MessageCollector
    {
        private readonly byte[] readBuffer = new byte[8192]; // Buffer lớn hơn cho việc đọc dữ liệu
        private readonly object lockObject = new object();
        private bool isRunning = true;

        public void Stop()
        {
            isRunning = false;
        }

        public void run()
        {
            try
            {
                int error = 0;
                while (isRunning && connected)
                {
                    Message message = readMessage();
                    if (message == null)
                    {
                        // Ngắt kết nối được phát hiện
                        error++;
                        if (error >= 5)
                        {
                            break;
                        }
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
                        Debug.LogError("Lỗi xử lý message: " + ex.ToString());
                    }

                    // Giảm thời gian ngủ để xử lý tin nhắn nhanh hơn
                    // nhưng vẫn giữ CPU không quá cao
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

                // Tối ưu vòng lặp XOR key bằng cách tránh tham chiếu lặp lại
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
                Debug.LogError("Lỗi xử lý getKey: " + ex.ToString());
            }
        }

        private Message readMessage2(sbyte cmd)
        {
            try
            {
                // Kiểm tra kết nối trước khi đọc
                if (dis == null || !dis.BaseStream.CanRead || !connected)
                {
                    Debug.Log("Connection is not available for reading");
                    return null;
                }

                int numBits = 28;
                int size = 0;

                // Đọc kích thước (3 byte) với timeout
                for (int i = 0; i < numBits; i += 8)
                {
                    try
                    {
                        int bitsToRead = Math.Min(8, numBits - i);
                        int value = readKey(dis.ReadSByte()) + 128;
                        size |= (value & ((1 << bitsToRead) - 1)) << i;
                    }
                    catch (IOException ex)
                    {
                        Debug.LogError("Error reading message size: " + ex.Message);
                        return null;
                    }
                }

                // Sử dụng ArrayPool để tránh GC
                sbyte[] pooledArray = ArrayPool<sbyte>.Shared.Rent(size);
                try
                {
                    // Đọc nội dung message với timeout
                    byte[] src;
                    try
                    {
                        src = dis.ReadBytes(size);
                    }
                    catch (IOException ex)
                    {
                        Debug.LogError("Error reading message content: " + ex.Message);
                        ArrayPool<sbyte>.Shared.Return(pooledArray);
                        return null;
                    }

                    if (src == null || src.Length != size)
                    {
                        Debug.LogError("Invalid message content length");
                        ArrayPool<sbyte>.Shared.Return(pooledArray);
                        return null;
                    }

                    Buffer.BlockCopy(src, 0, pooledArray, 0, size);

                    if (getKeyComplete)
                    {
                        // Giải mã từng byte
                        for (int i = 0; i < size; i++)
                        {
                            pooledArray[i] = readKey(pooledArray[i]);
                        }
                    }

                    recvByteCount += (numBits / 8) + 2 + size;
                    int num6 = recvByteCount + sendByteCount;
                    strRecvByteCount = num6 / 1024 + "." + num6 % 1024 / 102 + "Kb";

                    byte[] raw = new byte[size];
                    Buffer.BlockCopy(pooledArray, 0, raw, 0, size);
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
                        decoded = pooledArray;
                    }

                    // Tạo message mới với array từ pool
                    Message result = new Message(cmd, decoded, decoded.Length);
                    // Không trả lại array vào pool vì Message sẽ quản lý nó
                    return result;
                }
                catch (Exception ex)
                {
                    // Trả lại array vào pool nếu có lỗi
                    ArrayPool<sbyte>.Shared.Return(pooledArray);
                    Debug.LogError("Error processing message2: " + ex.ToString());
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
                // Kiểm tra kết nối trước khi đọc
                if (dis == null || !dis.BaseStream.CanRead || !connected)
                {
                    Debug.Log("Stream is not available for reading");
                    return null;
                }

                sbyte b;
                try
                {
                    b = dis.ReadSByte();
                    if (getKeyComplete)
                    {
                        b = readKey(b);
                    }
                }
                catch (IOException ex)
                {
                    Debug.LogError("Error reading command byte: " + ex.Message);
                    return null;
                }

                // Xử lý các loại message đặc biệt
                if (b == -32 || b == -66 || b == 11 || b == -67 || b == -74 || b == -87 || b == 66 || b == 120)
                {
                    return readMessage2(b);
                }

                int dataSize;
                try
                {
                    if (getKeyComplete)
                    {
                        // Sử dụng 3 byte (24-bit) thay vì 2 byte cho message thường
                        // Tăng từ 65KB lên 16MB capacity
                        sbyte b1 = dis.ReadSByte();
                        sbyte b2 = dis.ReadSByte();
                        sbyte b3 = dis.ReadSByte();

                        int byte1 = readKey(b1) & 0xFF;
                        int byte2 = readKey(b2) & 0xFF;
                        int byte3 = readKey(b3) & 0xFF;

                        dataSize = (byte1 << 16) | (byte2 << 8) | byte3;
                    }
                    else
                    {
                        // Không mã hóa cũng sử dụng 3 byte
                        sbyte b1 = dis.ReadSByte();
                        sbyte b2 = dis.ReadSByte();
                        sbyte b3 = dis.ReadSByte();

                        dataSize = ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
                    }
                }
                catch (IOException ex)
                {
                    Debug.LogError("Error reading message size: " + ex.Message);
                    return null;
                }

                // Kiểm tra kích thước hợp lệ
                if (dataSize < 0 || dataSize > 16777215) // 24-bit max value
                {
                    Debug.LogError($"Invalid message size: {dataSize}");
                    return null;
                }

                // Sử dụng ArrayPool để tránh GC
                sbyte[] pooledArray = ArrayPool<sbyte>.Shared.Rent(dataSize);
                try
                {
                    // Đọc nội dung message với timeout
                    byte[] src;
                    try
                    {
                        src = dis.ReadBytes(dataSize);
                    }
                    catch (IOException ex)
                    {
                        Debug.LogError("Error reading message content: " + ex.Message);
                        ArrayPool<sbyte>.Shared.Return(pooledArray);
                        return null;
                    }

                    if (src == null || src.Length != dataSize)
                    {
                        Debug.LogError("Invalid message content length");
                        ArrayPool<sbyte>.Shared.Return(pooledArray);
                        return null;
                    }

                    Buffer.BlockCopy(src, 0, pooledArray, 0, dataSize);

                    // Cập nhật byte count (3 byte cho size thay vì 2)
                    recvByteCount += 4 + dataSize; // 1 byte command + 3 byte size + data
                    int byteTotal = recvByteCount + sendByteCount;
                    strRecvByteCount = byteTotal / 1024 + "." + byteTotal % 1024 / 102 + "Kb";

                    if (getKeyComplete)
                    {
                        // Giải mã từng byte
                        for (int i = 0; i < dataSize; i++)
                        {
                            pooledArray[i] = readKey(pooledArray[i]);
                        }
                    }

                    byte[] raw = new byte[dataSize];
                    Buffer.BlockCopy(pooledArray, 0, raw, 0, dataSize);
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
                        decoded = pooledArray;
                    }

                    Message result = new Message(b, decoded, decoded.Length);
                    return result;
                }
                catch (Exception ex)
                {
                    // Trả lại array vào pool nếu có lỗi
                    ArrayPool<sbyte>.Shared.Return(pooledArray);
                    Debug.LogError("Error processing message: " + ex.ToString());
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

    protected static Session_ME instance = new Session_ME();

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

    public static int count;

    // Đổi sang ConcurrentQueue để an toàn luồng và hiệu quả hơn
    public static MyVector recieveMsg = new MyVector();
    private static readonly object recieveMsgLock = new object();

    // Thêm trạng thái cho session
    private bool isDisposed = false;

    // Tăng timeout cho kết nối
    private const int CONNECTION_TIMEOUT = 10000; // 10 giây

    public Session_ME()
    {
        Debug.Log("init Session_ME");
    }

    public void clearSendingMessage()
    {
        sender.sendingMessage.Clear();
    }

    [MethodImpl(MethodImplOptions.AggressiveInlining)]
    public static Session_ME gI()
    {
        if (instance == null)
        {
            instance = new Session_ME();
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
            if (isMainSession)
            {
                ServerListScreen.testConnect = -1;
            }
            this.host = host;
            this.port = port;
            getKeyComplete = false;
            sc = null;
            Debug.Log("connecting to " + host + ":" + port);

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
            Debug.LogError("Connect error: " + ex.ToString());
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

    public TcpClient ProxyTcpClient(string targetHost, int targetPort)
    {
        TcpClient result;
        //var acc = Account.myAccount;
        if (true)
        {
            try
            {
                TcpClient tcpClient = new TcpClient();
                // Đặt buffer cho socket
                tcpClient.ReceiveBufferSize = 8192;
                tcpClient.SendBufferSize = 8192;
                tcpClient.NoDelay = true; // Tắt Nagle algorithm để giảm độ trễ

                // Đặt timeout kết nối
                var connectTask = tcpClient.ConnectAsync(targetHost, targetPort);
                bool success = connectTask.Wait(CONNECTION_TIMEOUT);

                if (!success)
                {
                    throw new TimeoutException("Kết nối đến server quá thời gian chờ");
                }

                result = tcpClient;
            }
            catch (Exception ex)
            {
                Debug.LogError("ProxyTcpClient error: " + ex.ToString());
                throw;
            }
        }
        return result;
    }

    public void doConnect(string host, int port)
    {
        try
        {
            sc = ProxyTcpClient(host, port);
            dataStream = sc.GetStream();

            // Đặt timeout để tránh bị treo khi đọc/ghi
            dataStream.ReadTimeout = 30000; // 30 giây
            dataStream.WriteTimeout = 30000;

            dis = new BinaryReader(dataStream, new UTF8Encoding());
            dos = new BinaryWriter(dataStream, new UTF8Encoding());

            connected = true;

            // Tạo và bắt đầu thread gửi
            Thread senderThread = new Thread(sender.run);
            senderThread.IsBackground = true;
            senderThread.Start();

            // Tạo và bắt đầu thread nhận
            MessageCollector collector = new MessageCollector();
            collectorThread = new Thread(collector.run);
            collectorThread.IsBackground = true;
            collectorThread.Start();

            timeConnected = currentTimeMillis();
            connecting = false;

            // Gửi message yêu cầu key ngay sau khi kết nối
            doSendMessage(new Message(-27));
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
        count++;
        //   Debug.Log("SEND MSG: " + message.command);
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
                // Mã hóa và gửi command
                sbyte value = writeKey(m.command);
                dos.Write(value);
            }
            else
            {
                dos.Write(m.command);
            }
            if (data != null)
            {
                int dataLength = data.Length;
                if (getKeyComplete)
                {
                    // Mã hóa và gửi độ dài 24-bit (3 bytes)
                    int num1 = writeKey((sbyte)(dataLength >> 16));
                    dos.Write((sbyte)num1);
                    int num2 = writeKey((sbyte)(dataLength >> 8));
                    dos.Write((sbyte)num2);
                    int num3 = writeKey((sbyte)(dataLength & 0xFF));
                    dos.Write((sbyte)num3);
                }
                else
                {
                    // Gửi độ dài 24-bit (3 bytes) không mã hóa
                    dos.Write((byte)(dataLength >> 16));
                    dos.Write((byte)(dataLength >> 8));
                    dos.Write((byte)(dataLength & 0xFF));
                }
                if (getKeyComplete)
                {
                    // Gửi từng byte đã mã hóa
                    for (int i = 0; i < data.Length; i++)
                    {
                        sbyte value2 = writeKey(data[i]);
                        dos.Write(value2);
                    }
                }
                else
                {
                    // Gửi dữ liệu nguyên bản
                    for (int i = 0; i < data.Length; i++)
                    {
                        dos.Write(data[i]);
                    }
                }
                sendByteCount += 4 + data.Length; // 1 byte command + 3 bytes length + data
            }
            else
            {
                // Gửi độ dài 0 nếu không có dữ liệu (24-bit)
                if (getKeyComplete)
                {
                    int num4 = 0;
                    int num5 = writeKey((sbyte)(num4 >> 16));
                    dos.Write((sbyte)num5);
                    int num6 = writeKey((sbyte)(num4 >> 8));
                    dos.Write((sbyte)num6);
                    int num7 = writeKey((sbyte)(num4 & 0xFF));
                    dos.Write((sbyte)num7);
                }
                else
                {
                    dos.Write((byte)0);
                    dos.Write((byte)0);
                    dos.Write((byte)0);
                }
                sendByteCount += 4; // 1 byte command + 3 bytes length
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
        int processLimit = 10; // Giới hạn số lượng message xử lý mỗi frame để tránh lag
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
                // Đưa message trở lại hàng đợi để xử lý sau
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

            if (isMainSession)
            {
                ServerListScreen.testConnect = 0;
            }

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
        if (var == null)
            return null;

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