package com.ngocrong.server;

import com.ngocrong.network.nio.NioSession;
import com.ngocrong.server.SessionManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Basic NIO based server implementation using Selector.
 */
public class NioServer extends Server {
    private static final Logger logger = Logger.getLogger(NioServer.class);
    private Selector selector;
    private ServerSocketChannel serverChannel;

    @Override
    protected void start() {
        logger.debug("Start NIO server port=" + config.getPort());
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(config.getPort()), 10000);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            id = 0;
            start = true;
            Thread auto = new Thread(new AutoSaveData());
            Thread eventUpdate = new Thread(new MainUpdate());
            activeCommandLine();
            autoUpdateCCU();
            eventUpdate.start();
            auto.start();
            BossManager.bornBoss();
            var mapManager = MapManager.getInstance();
            mapManager.openBlackDragonBall();
            mapManager.openBaseBabidi();
            Thread threadMapManager = new Thread(mapManager);
            threadMapManager.start();
            logger.debug("Start server Success!");
            while (start) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (!key.isValid()) continue;
                    if (key.isAcceptable()) {
                        acceptClient();
                    } else if (key.isReadable()) {
                        readClient(key);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("failed!", e);
        } finally {
            close();
        }
    }

    private void acceptClient() throws IOException {
        SocketChannel channel = serverChannel.accept();
        if (channel != null) {
            channel.configureBlocking(false);
            InetSocketAddress socketAddress = (InetSocketAddress) channel.getRemoteAddress();
            String ip = socketAddress.getAddress().getHostAddress();
            if (ips.getOrDefault(ip, 0) < COUNT_SESSION_ON_IP) {
                NioSession session = new NioSession(channel);
                channel.register(selector, SelectionKey.OP_READ, session);
                SessionManager.addSession(session);
            } else {
                channel.close();
            }
        }
    }

    private void readClient(SelectionKey key) {
        NioSession session = (NioSession) key.attachment();
        try {
            session.read();
        } catch (IOException e) {
            session.disconnect();
            key.cancel();
        }
    }

    @Override
    protected void close() {
        super.close();
        try {
            if (serverChannel != null) serverChannel.close();
            if (selector != null) selector.close();
        } catch (IOException ignored) {
        }
    }
}

