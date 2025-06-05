package com.ngocrong.top;

import com.ngocrong.consts.Cmd;
import com.ngocrong.network.Message;
import com.ngocrong.user.Player;
import lombok.Getter;
import org.apache.log4j.Logger;

import com.ngocrong.network.FastDataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
public abstract class Top {

    public static final byte TYPE_NONE = 1;
    public static final byte TYPE_THACH_DAU = 0;
    public static final int TOP_POWER = 0;
    public static final int TOP_EXCHANGE = 1;
    public static final int TOP_DHVT_SIEU_HANG = 2;
    public static final int REWARD_TOP_DHVT_SIEU_HANG = 3;
    public static final int TOP_VQTD = 4;
    public static final int TOP_WHIS = 5;
    public static final int TOP_WHIS_Reward = 6;
    public boolean isToggle;
    private static Logger logger = Logger.getLogger(Top.class);
    private static ArrayList<Top> tops = new ArrayList<>();
    private static volatile boolean isRunning = true;
    private static Thread updateThread;
    private static long lastUpdateTime = System.currentTimeMillis();

    private int id;
    private String name;
    private byte type;
    protected byte limit;
    public ArrayList<TopInfo> elements;
    protected long lowestScore = -1;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public ArrayList<TopInfo> getElements() {
        return elements;
    }

    public static void initialize() {
        addTop(new TopPower(TOP_POWER, TYPE_NONE, "Sức mạnh", (byte) 100));
        addTop(new TopExchange(TOP_EXCHANGE, TYPE_NONE, "Đổi Vật phẩm", (byte) 100));
        addTop(new TopDHVTSieuHang(Top.TOP_DHVT_SIEU_HANG, TYPE_THACH_DAU, "Top DHVT Siêu Hạng", (byte) 100));
        addTop(new TopVQTD(Top.TOP_VQTD, TYPE_NONE, "Top Vòng Quay Thượng Đế", (byte) 50));
        addTop(new TopWhis(Top.TOP_WHIS, TYPE_NONE, "Top WHIS", (byte) 20));
        addTop(new TopWhisReward(Top.TOP_WHIS_Reward, TYPE_NONE, "Top WHIS Lần trước", (byte) 20));

        for (Top top : tops) {
            top.load();
        }
        startAutoUpdate();
    }

    private static void startAutoUpdate() {
        if (updateThread != null && updateThread.isAlive()) {
            return;
        }

        updateThread = new Thread(() -> {
            while (isRunning) {
                try {
                    // Tính thời gian đến lần update tiếp theo
                    Calendar now = Calendar.getInstance();
                    int minute = now.get(Calendar.MINUTE);
                    int nextUpdateMinute;

                    if (minute < 15) {
                        nextUpdateMinute = 15;
                    } else if (minute < 30) {
                        nextUpdateMinute = 30;
                    } else if (minute < 45) {
                        nextUpdateMinute = 45;
                    } else {
                        nextUpdateMinute = 60;
                    }

                    int sleepMinutes = nextUpdateMinute - minute;
                    int sleepMillis = sleepMinutes * 60 * 1000 - (now.get(Calendar.SECOND) * 1000 + now.get(Calendar.MILLISECOND));

                    Thread.sleep(sleepMillis);

                    logger.info("Starting scheduled top update...");
                    lastUpdateTime = System.currentTimeMillis();

                    synchronized (tops) {
                        for (Top top : tops) {
                            try {
                                // Clear data cũ
                                top.lock.writeLock().lock();
                                try {
                                    top.elements.clear();
                                    top.lowestScore = -1;
                                } finally {
                                    top.lock.writeLock().unlock();
                                }

                                // Load data mới
                                top.load();
                                top.update();
                                top.updateLowestScore();

                                logger.info("Updated top: " + top.getName());
                            } catch (Exception e) {
                                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                                System.err.println("Error at 129");
                                logger.error("Error updating top " + top.getName(), e);
                            }
                        }
                    }

                    System.gc();
                    logger.info("Scheduled top update completed");

                } catch (InterruptedException e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 128");
                    logger.warn("Top update thread interrupted", e);
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 127");
                    logger.error("Error in top update thread", e);
                }
            }
        }, "TopUpdateThread");

        updateThread.setDaemon(true);
        updateThread.start();
    }

    public static void addTop(Top top) {
        synchronized (tops) {
            tops.add(top);
        }
    }

    public static void updateTop(int id) {
        synchronized (tops) {
            for (Top top : tops) {
                if (top.id == id) {
                    top.load();
                }
            }
        }
    }

    public static Top getTop(int id) {
        synchronized (tops) {
            for (Top top : tops) {
                if (top.id == id) {
                    return top;
                }
            }
        }
        return null;
    }

    public static void stopAutoUpdate() {
        isRunning = false;
        if (updateThread != null) {
            updateThread.interrupt();
            try {
                updateThread.join(5000);
            } catch (InterruptedException e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                System.err.println("Error at 126");
                logger.error("Error stopping update thread", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public Top(int id, byte type, String name, byte limit) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.limit = limit;
        elements = new ArrayList<>();
    }

    private String getTimeUntilNextUpdate() {
        Calendar now = Calendar.getInstance();
        int minute = now.get(Calendar.MINUTE);
        int nextUpdateMinute;

        if (minute < 15) {
            nextUpdateMinute = 15;
        } else if (minute < 30) {
            nextUpdateMinute = 30;
        } else if (minute < 45) {
            nextUpdateMinute = 45;
        } else {
            nextUpdateMinute = 60;
        }

        int remainingMinutes = nextUpdateMinute - minute - 1;
        int remainingSeconds = 59 - now.get(Calendar.SECOND);

        return String.format("Thời gian cập nhật lại : %dp%ds", remainingMinutes, remainingSeconds);
    }

    public TopInfo getTopInfo(int playerID) {
        lock.readLock().lock();
        try {
            for (TopInfo top : elements) {
                if (top.playerID == playerID) {
                    return top;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    public void update() {
        lock.readLock().lock();
        try {
            elements.sort((o1, o2) -> isToggle
                    ? ((Long) o1.score).compareTo(((Long) o2.score))
                    : ((Long) o2.score).compareTo(((Long) o1.score)));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateLowestScore() {
        if (elements.size() < limit) {
            lowestScore = 0;
        } else {
            lock.readLock().lock();
            try {
                long lowest = -1;
                for (TopInfo top : elements) {
                    if (lowest == -1) {
                        lowest = top.score;
                    } else {
                        if (top.score < lowest) {
                            lowest = top.score;
                        }
                    }
                }
                lowestScore = lowest;
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    public void addTopInfo(TopInfo top) {
        lock.writeLock().lock();
        try {
            if (elements.size() >= limit) {
                elements.set(limit - 1, top);
            } else {
                elements.add(top);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public abstract void load();

    public void show(Player _player) {
        try {
            Message ms = new Message(Cmd.TOP);
            FastDataOutputStream ds = ms.writer();
            ds.writeByte(type);
            ds.writeUTF(name + "\n" + getTimeUntilNextUpdate());
            ds.writeByte(elements.size());
            int i = 1;
            for (TopInfo top : elements) {
                ds.writeInt(i++);
                ds.writeInt(top.playerID);
                ds.writeShort(top.head);
                ds.writeShort(top.body);
                ds.writeShort(top.leg);
                ds.writeUTF(top.name);
                ds.writeUTF(top.info);
                ds.writeUTF(top.info2);
            }
            ds.flush();
            _player.service.sendMessage(ms);
            ms.cleanup();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 125");
            logger.error("failed!", ex);
        }
    }
}
