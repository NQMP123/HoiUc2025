package com.ngocrong.network.nio;

import com.ngocrong.network.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Simple NIO based session using non blocking SocketChannel.
 */
public class NioSession implements ISession {
    private final SocketChannel channel;
    private IMessageHandler messageHandler;
    private IService service;
    private final ByteBuffer readBuffer = ByteBuffer.allocate(4096);

    public NioSession(SocketChannel channel) throws IOException {
        this.channel = channel;
        this.channel.configureBlocking(false);
        this.channel.socket().setTcpNoDelay(true);
        this.channel.socket().setKeepAlive(true);
    }

    public SocketChannel getChannel() {
        return channel;
    }

    /**
     * Read available data from the channel and dispatch messages to the handler.
     */
    public void read() throws IOException {
        int bytes = channel.read(readBuffer);
        if (bytes == -1) {
            disconnect();
            return;
        }
        if (bytes > 0) {
            readBuffer.flip();
            while (readBuffer.remaining() >= 3) {
                byte cmd = readBuffer.get();
                int size = readBuffer.getShort() & 0xffff;
                if (readBuffer.remaining() < size) {
                    readBuffer.position(readBuffer.position() - 3);
                    break;
                }
                byte[] data = new byte[size];
                readBuffer.get(data);
                if (messageHandler != null) {
                    Message m = new Message(cmd, data);
                    messageHandler.onMessage(m);
                }
            }
            readBuffer.compact();
        }
    }

    /**
     * Write a message to the channel.
     */
    public void write(Message message) throws IOException {
        byte[] data = message.getData();
        ByteBuffer buffer = ByteBuffer.allocate((data != null ? data.length : 0) + 3);
        buffer.put(message.getCommand());
        buffer.putShort((short) (data != null ? data.length : 0));
        if (data != null) {
            buffer.put(data);
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public void setHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            write(message);
        } catch (IOException e) {
            disconnect();
        }
    }

    @Override
    public void setService(IService service) {
        this.service = service;
        if (messageHandler != null) {
            messageHandler.setService(service);
        }
    }

    @Override
    public void close() {
        disconnect();
    }

    @Override
    public void disconnect() {
        try {
            channel.close();
        } catch (IOException ignored) {
        }
    }
}

