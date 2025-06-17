package com.ngocrong.server.voice;

import com.ngocrong.network.Message;
import com.ngocrong.network.VoiceMessageService;
import com.ngocrong.server.SessionManager;
import com.ngocrong.user.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VoiceSession implements Runnable {
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Thread thread;
    private Player player;

    public VoiceSession(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.thread = new Thread(this, "VoiceSession");
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                Message msg = readMessage();
                if (msg == null) break;
                try {
                    handleMessage(msg);
                } finally {
                    msg.cleanup();
                }
            }
        } catch (Exception ignored) {
        } finally {
            close();
        }
    }

    private void handleMessage(Message msg) throws IOException {
        if (msg.getCommand() == -100) { // handshake
            String name = msg.reader().readUTF();
            this.player = SessionManager._findPlayer(name);
            if (player != null) {
                VoiceSessionManager.addSession(player.name, this);
            }
            return;
        }
        if (player == null) {
            return;
        }
        if (msg.getCommand() == -58) { // voice chat message
            byte[] data = msg.getData();
            if (data.length == 0) return;
            byte type = data[0];
            Message copy = new Message(msg.getCommand(), data);
            switch (type) {
                case 0:
                    VoiceMessageService.gI().processWorldChatVoiceMessage(player, copy);
                    break;
                case 1:
                    VoiceMessageService.gI().processPrivateChatVoiceMessage(player, copy);
                    break;
                case 2:
                    VoiceMessageService.gI().processMapChatVoiceMessage(player, copy);
                    break;
            }
        }
    }

    private Message readMessage() throws IOException {
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
        dis.readFully(data);
        return new Message(cmd, data);
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
        VoiceSessionManager.removeSession(this);
        try {
            socket.close();
        } catch (Exception ignored) {}
    }
}
