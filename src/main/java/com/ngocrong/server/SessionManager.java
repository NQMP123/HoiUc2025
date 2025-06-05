package com.ngocrong.server;

import com.ngocrong.network.Message;
import com.ngocrong.network.Session;
import com.ngocrong.user.Player;
import com.ngocrong.user.User;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SessionManager {

    private static final Logger logger = Logger.getLogger(SessionManager.class);

    public static ArrayList<Session> sessions = new ArrayList<>();
    public static ReadWriteLock lock = new ReentrantReadWriteLock();
    public static byte countPhaoHoa = 0;
    public static Lock lockUserLogin = new ReentrantLock();
    public static HashMap<String, Long> userLogins = new HashMap<>();

    public static void addUserLogin(String username) {
        lockUserLogin.lock();
        try {
            userLogins.put(username, System.currentTimeMillis());
        } finally {
            lockUserLogin.unlock();
        }
    }

    public static long getTimeUserLogin(String username) {
        lockUserLogin.lock();
        try {
            long time = userLogins.getOrDefault(username, 0L);
            return time;
        } finally {
            lockUserLogin.unlock();
        }
    }

    public static void addSession(Session session) {
        lock.writeLock().lock();
        try {
            sessions.add(session);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void removeSession(Session session) {
        lock.writeLock().lock();
        try {
            sessions.remove(session);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static long getCountPlayer() {
        lock.readLock().lock();
        try {
            return sessions.stream().filter(ss -> ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null).count();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static List<User> findUser(String name) {
        List<User> userList = new ArrayList<>();
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                if (ss.socket != null && !ss.socket.isClosed() && ss.user != null && ss.user.getUsername().toLowerCase().equals(name.toLowerCase())) {
                    userList.add(ss.user);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return userList;
    }
    private static final Object PLAYER_LOCK = new Object();

    public static List<Player> findPlayer(String name) {
        List<Player> userList = new ArrayList<>();
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                if (ss.socket != null && !ss.socket.isClosed() && ss.user != null) {
                    if (ss._player != null && ss._player.name.equals(name)) {
                        userList.add(ss._player);
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return userList;
    }

    public static void checkValidPlayer(Player player) {
        if (player == null) {
            return;
        }
        synchronized (PLAYER_LOCK) {
            var players = findPlayer(player.name);

            if (players.size() > 1) {
                for (var pl : players) {
                    try {
                        pl.getSession().close();
                        if (pl.zone != null) {
                            pl.zone.leave(pl);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean deviceInvalid(String device) {
        lock.readLock().lock();
        try {
            int num = 0;
            for (Session ss : sessions) {
                if (ss.socket != null && !ss.socket.isClosed() && ss.deviceInfo != null && ss.deviceInfo.equals(device)) {
                    num++;
                }
            }
            return num > Server.COUNT_SESSION_ON_IP;
        } finally {
            lock.readLock().unlock();
        }
    }

    public static List<User> findUserById(int id) {
        List<User> userList = new ArrayList<>();
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                if (ss.socket != null && !ss.socket.isClosed() && ss.user != null && ss.user.getId() == id) {
                    userList.add(ss.user);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return userList;
    }

    public static Player findChar(int id) {
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null && ss._player.id == id) {
                    return ss._player;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    public static void sendMessage(Message ms) {
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                try {
                    if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null) {
                        ss.sendMessage(ms);
                    }
                } catch (Exception ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 142");
                    logger.error("failed!", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void chatVip(String text) {
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                try {
                    if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null) {
                        ss._player.service.chatVip(text);
                    }
                } catch (Exception ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 141");
                    logger.error("failed!", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public static List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>();
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                try {
                    if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null) {
                        list.add(ss._player);
                    }
                } catch (Exception ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 140");
                    logger.error("failed!", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return list;
    }

    public static void serverMessage(String text) {
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                try {
                    if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null) {
                        ss._player.service.sendThongBao(text);
                    }
                } catch (Exception ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 139");
                    logger.error("failed!", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void addBigMessage(String text) {
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                try {
                    if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null) {
                        ss._player.service.addBigMessage(ss._player.getPetAvatar(), text, (byte) 0, null, null);
                    }
                } catch (Exception ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 138");
                    logger.error("failed!", ex);
                }

            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void saveData() {

        System.err.println("Save Toàn Bộ Player");
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                try {
                    if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null) {
                        if (System.currentTimeMillis() - ss._player.lastSaveData >= 15 * 60000) {
                            ss._player.saveData();
                        }
                    }
                } catch (Exception ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 137");
                    logger.error("failed!", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }

    }

    public static void close() {
        lock.readLock().lock();
        try {
            for (Session ss : sessions) {
                try {
                    if (ss.isEnter && ss.socket != null && !ss.socket.isClosed() && ss._player != null) {
                        ss.close();
                    }
                } catch (Exception ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    logger.error("failed!", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
