package com.ngocrong.server.icon;

import java.net.ServerSocket;
import java.net.Socket;

public class IconServer implements Runnable {
    private final int port;
    private ServerSocket server;
    private volatile boolean running;

    public IconServer(int port) {
        this.port = port;
    }

    public void start() {
        Thread t = new Thread(this, "IconServer");
        t.start();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);
            running = true;
            System.err.println("Active IconServer at port :" + port);
            while (running) {
                Socket socket = server.accept();
                new IconSession(socket);
            }
        } catch (Exception ignored) {
        }
    }

    public void stop() {
        running = false;
        try {
            if (server != null) server.close();
        } catch (Exception ignored) {
        }
    }
}
