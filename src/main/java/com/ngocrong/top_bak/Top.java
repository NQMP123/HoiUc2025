//package com.ngocrong.top;
//
//import com.ngocrong.consts.Cmd;
//import com.ngocrong.network.Message;
//import com.ngocrong.user.Player;
//import lombok.Getter;
//import org.apache.log4j.Logger;
//
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.concurrent.locks.ReadWriteLock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
//@Getter
//public abstract class Top {
//
//    public static final byte TYPE_NONE = 1;
//    public static final byte TYPE_THACH_DAU = 0;
//    public static final int TOP_POWER = 0;
//    public static final int TOP_RECHARGE = 1;
//    public static final int TOP_COIN = 2;
//    public static final int TOP_GOLD = 3;
//    public static final int TOP_MONEY = 4;
//    public static final int TOP_EVENT = 5;
//    public static final int TOP_TASK = 6;
//
//    private static final Logger logger = Logger.getLogger(Top.class);
//    private static final ArrayList<Top> tops = new ArrayList<>();
//    public static ArrayList<Top> topSurvival;
//
//    public static void initialize() {
//        addTop(new TopPower(TOP_POWER, TYPE_NONE, "Sức mạnh", (byte) 100));
//        addTop(new TopRecharge(TOP_RECHARGE, TYPE_NONE, "Nạp thẻ", (byte) 100));
//        addTop(new TopCoin(TOP_COIN, TYPE_NONE, "Vàng", (byte) 100));
//        addTop(new TopGold(TOP_GOLD, TYPE_NONE, "Thỏi vàng", (byte) 100));
//        addTop(new TopMoney(TOP_MONEY, TYPE_NONE, "Tổng vàng", (byte) 100));
//        addTop(new TopEvent(TOP_EVENT, TYPE_NONE, "Sự kiện", (byte) 100));
//        addTop(new TopTask(TOP_TASK, TYPE_NONE, "Nhiệm vụ", (byte) 100));
//        for (Top top : tops) {
//            top.load();
//        }
//        topSurvival = new ArrayList<>();
//    }
//
//    public static void addTop(Top top) {
//        synchronized (tops) {
//            tops.add(top);
//        }
//    }
//
//    public static Top getTop(int id) {
//        synchronized (tops) {
//            for (Top top : tops) {
//                if (top.id == id) {
//                    return top;
//                }
//            }
//        }
//        return null;
//    }
//
//    private final int id;
//    private final String name;
//    private final byte type;
//    protected byte limit;
//    public ArrayList<TopInfo> elements;
//    protected long lowestScore = -1;
//    public final ReadWriteLock lock = new ReentrantReadWriteLock();
//
//    public Top(int id, byte type, String name, byte limit) {
//        this.id = id;
//        this.type = type;
//        this.name = name;
//        this.limit = limit;
//        elements = new ArrayList<>();
//        //load();
//    }
//
//    public TopInfo getTopInfo(int playerID) {
//        lock.readLock().lock();
//        try {
//            for (TopInfo top : elements) {
//                if (top.playerID == playerID) {
//                    return top;
//                }
//            }
//        } finally {
//            lock.readLock().unlock();
//        }
//        return null;
//    }
//
//    public void update() {
//        lock.readLock().lock();
//        try {
//            elements.sort(new Comparator<TopInfo>() {
//                @Override
//                public int compare(TopInfo o1, TopInfo o2) {
//                    return ((Long) o2.score).compareTo(((Long) o1.score));
//                }
//            });
//        } finally {
//            lock.readLock().unlock();
//        }
//    }
//
//    public void updateLowestScore() {
//        if (elements.size() < limit) {
//            lowestScore = 0;
//        } else {
//            lock.readLock().lock();
//            try {
//                long lowest = -1;
//                for (TopInfo top : elements) {
//                    if (lowest == -1) {
//                        lowest = top.score;
//                    } else {
//                        if (top.score < lowest) {
//                            lowest = top.score;
//                        }
//                    }
//                }
//                lowestScore = lowest;
//            } finally {
//                lock.readLock().unlock();
//            }
//        }
//    }
//
//    public void addTopInfo(TopInfo top) {
//        lock.writeLock().lock();
//        try {
//            if (elements.size() >= limit) {
//                elements.set(limit - 1, top);
//            } else {
//                elements.add(top);
//            }
//        } finally {
//            lock.writeLock().unlock();
//        }
//    }
//
//    public abstract void load();
//
//    public void show(Player _player) {
//        try {
//            Message ms = new Message(Cmd.TOP);
//            FastDataOutputStream ds = ms.writer();
//            ds.writeByte(type);
//            ds.writeUTF(name);
//            int size = Math.min(elements.size(), 100);
//            ds.writeShort(size);
//            for (int i = 0; i < elements.size(); i++) {
//                TopInfo top = elements.get(i);
//                ds.writeInt(i + 1);
//                ds.writeInt(top.playerID);
//                ds.writeShort(top.head);
//                ds.writeShort(top.body);
//                ds.writeShort(top.leg);
//                ds.writeUTF(top.name);
//                ds.writeUTF(top.info);
//                ds.writeUTF(top.info2);
//                ds.writeBoolean(_player.ranks.contains(0));
//            }
//            ds.flush();
//            _player.service.sendMessage(ms);
//            ms.cleanup();
//        } catch (IOException ex) { com.ngocrong.NQMP.UtilsNQMP.logError(ex);
//            logger.error("failed!", ex);
//        }
//
//    }
//
//    public void update(Object object) {
//
//    }
//}
