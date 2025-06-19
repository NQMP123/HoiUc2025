package com.ngocrong.server.icon;

import com.ngocrong.consts.Cmd;
import com.ngocrong.network.FastDataOutputStream;
import com.ngocrong.network.Message;
import com.ngocrong.server.SessionManager;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class IconSession implements Runnable {
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Thread thread;
    private Player player;

    public IconSession(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.thread = new Thread(this, "IconSession");
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                MessageData msg = readMessage();
                if (msg == null) {
                    break;
                }
                handleMessage(msg);
            }
        } catch (Exception ignored) {
        } finally {
            close();
        }
    }

    private static class MessageData {
        byte command;
        byte[] data;
        MessageData(byte cmd, byte[] data) { this.command = cmd; this.data = data; }
    }

    private MessageData readMessage() throws IOException {
        byte cmd;
        try {
            cmd = dis.readByte();
        } catch (IOException e) {
            return null;
        }
        int size = (dis.readByte() & 0xff) << 16;
        size |= (dis.readByte() & 0xff) << 8;
        size |= (dis.readByte() & 0xff);
        byte[] data = new byte[size];
        if (size > 0) dis.readFully(data);
        return new MessageData(cmd, data);
    }

    private void handleMessage(MessageData msgData) throws IOException {
        if (msgData.command == -100) {
            Message m = new Message(msgData.command, msgData.data);
            int id = m.reader().readInt();
            this.player = SessionManager.findChar(id);
            if (player != null) {
                IconSessionManager.addSession(player.name, this);
            }
            m.cleanup();
            return;
        }
        if (player == null) {
            return;
        }
        if (msgData.command == Cmd.REQUEST_ICON) {
            Message m = new Message(msgData.command, msgData.data);
            int id = m.reader().readInt();
            m.cleanup();
            sendIcon(id);
        }
    }

    private void sendIcon(int id) throws IOException {
        if (player == null) return;
        int zoom = player.getSession().getZoomLevel();
        byte[] ab = Utils.getFile(String.format("resources/image/%d/small/Small%d.png", zoom, id));
        if (ab == null) return;
        Message mss = new Message(Cmd.REQUEST_ICON);
        FastDataOutputStream ds = mss.writer();
        ds.writeInt(id);
        ds.writeInt(ab.length);
        ds.write(ab);
        ds.flush();
        sendMessage(mss);
        mss.cleanup();
    }

    public synchronized void sendMessage(Message msg) throws IOException {
        byte[] data = msg.getData();
        int size = data != null ? data.length : 0;
        dos.writeByte(msg.getCommand());
        dos.writeByte((size >> 16) & 0xff);
        dos.writeByte((size >> 8) & 0xff);
        dos.writeByte(size & 0xff);
        if (size > 0) {
            dos.write(data);
        }
        dos.flush();
    }

    public void close() {
        IconSessionManager.removeSession(this);
        try { socket.close(); } catch (Exception ignored) {}
    }
}
