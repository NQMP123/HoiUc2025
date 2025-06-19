package com.ngocrong.server.icon;

import com.ngocrong.user.Player;

import java.util.concurrent.ConcurrentHashMap;

public class IconSessionManager {
    private static final ConcurrentHashMap<String, IconSession> sessions = new ConcurrentHashMap<>();

    static void addSession(String playerName, IconSession session) {
        sessions.put(playerName, session);
    }

    static void removeSession(IconSession session) {
        sessions.values().removeIf(v -> v == session);
    }

    public static IconSession getSession(Player player) {
        return player != null ? sessions.get(player.name) : null;
    }
}
