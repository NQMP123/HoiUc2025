using System;
using System.IO;
using System.Net.Sockets;
using System.Threading;

public class VoiceSession : ISession
{
    private static VoiceSession instance = new VoiceSession();
    private TcpClient client;
    private NetworkStream stream;
    private IMessageHandler messageHandler;
    private Thread receiver;
    private bool handshake;

    public static VoiceSession gI()
    {
        return instance;
    }

    public bool isConnected()
    {
        return client != null && client.Connected;
    }

    public void setHandler(IMessageHandler handler)
    {
        messageHandler = handler;
    }

    public void connect(string host, int port)
    {
        if (isConnected()) return;
        client = new TcpClient();
        client.Connect(host, port);
        stream = client.GetStream();
        receiver = new Thread(run);
        receiver.IsBackground = true;
        receiver.Start();
    }

    private void run()
    {
        try
        {
            while (client.Connected)
            {
                Message msg = readMessage();
                if (msg != null)
                {
                    messageHandler?.onMessage(msg);
                }
                else break;
            }
        }
        catch { }
        close();
    }

    private Message readMessage()
    {
        try
        {
            int cmd = stream.ReadByte();
            if (cmd < 0) return null;
            int b1 = stream.ReadByte();
            int b2 = stream.ReadByte();
            int b3 = stream.ReadByte();
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
        catch
        {
            return null;
        }
    }

    public void sendMessage(Message message)
    {
        if (!isConnected()) return;
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

    public void sendInit(string name)
    {
        if (handshake) return;
        Message msg = new Message(-100);
        msg.writer().writeUTF(name);
        sendMessage(msg);
        msg.cleanup();
        handshake = true;
    }

    public void close()
    {
        try { client?.Close(); } catch { }
    }
}
