package com.ngocrong.network;

import com.ngocrong.clan.ClanManager;
import com.ngocrong.data.DiscipleData;
import com.ngocrong.data.PlayerData;
import com.ngocrong.item.Amulet;
import com.ngocrong.item.ItemOption;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.server.*;
import com.ngocrong.bot.Disciple;
import com.ngocrong.clan.Clan;
import com.ngocrong.collection.Card;
import com.ngocrong.consts.Cmd;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemTime;
import com.ngocrong.model.Achievement;
import com.ngocrong.model.Friend;
import com.ngocrong.model.History;
import com.ngocrong.model.MagicTree;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.SkillBook;
import com.ngocrong.skill.Skills;
import com.ngocrong.skill.SpecialSkill;
import com.ngocrong.task.Task;
import com.ngocrong.user.Player;
import com.ngocrong.user.Info;
import com.ngocrong.user.User;
import com.ngocrong.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngocrong.NQMP.DHVT_SH.DHVT_SH_Service;
import com.ngocrong.NQMP.DHVT_SH.SuperRank;
import com.ngocrong.NQMP.UtilsNQMP;
import com.ngocrong.NQMP.Whis.RewardWhis;
import com.ngocrong.consts.ItemName;
import com.ngocrong.data.VongQuayThuongDeData;
import com.ngocrong.data.WhisData;
import com.ngocrong.event.OsinCheckInEvent;
import com.ngocrong.top.AutoReward.AutoReward;
import com.ngocrong.user.func.BaiSu;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;

public class Session implements ISession {

    private static final ScheduledExecutorService HEARTBEAT_SERVICE = Executors.newScheduledThreadPool(1);

    private static final Logger logger = Logger.getLogger(Session.class);
    private static final Lock lock = new ReentrantLock();
    private byte[] key;
    public Socket socket;
    public DataInputStream dis;
    public DataOutputStream dos;
    public int id;
    public IMessageHandler messageHandler;
    @Getter
    private IService service;
    protected boolean isConnected;
    private byte curR, curW;
    private final Sender sender;
    private Thread collectorThread;
    protected Thread sendThread;
    protected String version;
    protected byte zoomLevel;
    protected int width;
    protected int height;
    protected int device; // 0-PC, 1- APK, 2-IOS
    public User user;
    public Player _player;
    private boolean isSetClientInfo;
    public boolean isEnter = false;
    public String deviceInfo;
    List<Short> iconList = new ArrayList();
    private long[][] matrixChallenge;
    private boolean matrixVerified;
    public String ip;
    private ScheduledFuture<?> heartbeatTask;
    private volatile long lastReceiveTime;
    private volatile long lastSendTime;
    public boolean isConfirm = false;
    public long lastConfirm = System.currentTimeMillis(), lastCreateSession = System.currentTimeMillis();
    private static final int SOCKET_BUFFER_SIZE = 4096;
    private static final int PING_INTERVAL = 150000, TIMEOUT = 150000; // 30s
    private static final int SENDING_QUEUE_LIMIT = 2048;

    public Session(Socket socket, String ip, int id) throws IOException {
        this.socket = socket;
        this.id = id;
        this.ip = ip;
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        socket.setReceiveBufferSize(SOCKET_BUFFER_SIZE);
        socket.setSendBufferSize(SOCKET_BUFFER_SIZE);
        socket.setSoTimeout(PING_INTERVAL);
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        lastReceiveTime = System.currentTimeMillis();
        lastSendTime = lastReceiveTime;
        setHandler(new MessageHandler(this));
        messageHandler.onConnectOK();
        setService(new Service(this));
        sendThread = new Thread(sender = new Sender());
        collectorThread = new Thread(new MessageCollector());
        collectorThread.start();
        heartbeatTask = HEARTBEAT_SERVICE.scheduleAtFixedRate(new Heartbeat(), 5, 5, TimeUnit.SECONDS);
        Server.ips.put(ip, Server.ips.getOrDefault(ip, 0) + 1);
    }

    public void setClientType(Message mss) throws IOException {
        if (!this.isSetClientInfo) {
            this.zoomLevel = mss.reader().readByte();
            this.width = mss.reader().readInt();
            this.height = mss.reader().readInt();
            device = mss.reader().readByte();
            version = mss.reader().readUTF();
            if (zoomLevel < 1 || zoomLevel > 4 || mss.reader().available() > 0) {
                disconnect();
                return;
            }
            if (!version.equals(Server.VERSION)) {
                ((Service) this.service).dialogMessage("Vui lòng tải phiên bản mới tại HOIUCNGOCRONG.COM");
                return;
            }
            this.isSetClientInfo = true;
            Service sv = (Service) this.service;
            sv.setLinkListServer();
            sv.setResource();
            sv.sendResVersion();
            sv.sendValidDll();
            sendMatrixChallenge();
        }
    }

    public void getImageSource(Message ms) {

        try {
            byte action = ms.reader().readByte();
            if (action == 1) {
                Service sv = (Service) service;
                String folder = "resources/data/" + zoomLevel;
                ArrayList<String> datas = new ArrayList<>();
                File file = new File(folder);
                addPath(datas, file);

                java.util.List<Message> batch = new java.util.ArrayList<>();
                batch.add(sv.buildSizeMessage(datas.size()));
                for (String path : datas) {
                    batch.add(sv.buildDownloadMessage(path));

                    if (batch.size() >= 50) {
                        sv.sendBatchMessages(batch);
                        batch.clear();
                    }
                }
                batch.add(sv.buildDownloadOkMessage());
                if (!batch.isEmpty()) {
                    sv.sendBatchMessages(batch);
                }
                sv.setLinkListServer();
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("failed!", ex);
        }
    }

    public static void addPath(ArrayList<String> paths, File file) {
        if (file.isFile()) {
            paths.add(file.getPath());
        } else {
            for (File f : file.listFiles()) {
                addPath(paths, f);
            }
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void setHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void setService(IService service) {
        this.service = service;
    }

    @Override
    public void sendMessage(Message message) {
        sender.addMessage(message);
        lastSendTime = System.currentTimeMillis();
    }

    private static boolean isSpecialMessage(int command) {
        return command == Cmd.BACKGROUND_TEMPLATE || command == Cmd.GET_EFFDATA || command == Cmd.REQUEST_NPCTEMPLATE || command == Cmd.REQUEST_ICON || command == Cmd.GET_IMAGE_SOURCE || command == Cmd.UPDATE_DATA || command == Cmd.GET_IMG_BY_NAME || command == 120;
    }

    protected synchronized void doSendMessage(Message m) throws IOException {
        if (m == null) {
            return;
        }

        byte[] data = m.getData();
        if (data != null) {
            data = java.util.Base64.getEncoder().encode(data);
        }

        byte b = m.getCommand();

        // Gửi command byte
        if (isConnected) {
            dos.writeByte(writeKey(b));
        } else {
            dos.writeByte(b);
        }

        if (data != null) {
            int size = data.length;

            if (isConnected) {
                if (isSpecialMessage(b)) {
                    // Message đặc biệt sử dụng 28-bit như cũ
                    int numBits = 28;
                    for (int i = 0; i < numBits; i += 8) {
                        int bitsToSend = Math.min(8, numBits - i);
                        byte value = (byte) ((size >> i & ((1 << bitsToSend) - 1)) - 128);
                        dos.writeByte(writeKey(value));
                    }
                } else {
                    // Message thường sử dụng 3 byte (24-bit) thay vì 2 byte
                    // Tăng từ 65KB lên 16MB capacity
                    byte byte1 = (byte) ((size >> 16) & 0xFF);
                    byte byte2 = (byte) ((size >> 8) & 0xFF);
                    byte byte3 = (byte) (size & 0xFF);

                    dos.writeByte(writeKey(byte1));
                    dos.writeByte(writeKey(byte2));
                    dos.writeByte(writeKey(byte3));
                }
            } else {
                // Không mã hóa cũng sử dụng 3 byte
                dos.writeByte((size >> 16) & 0xFF);
                dos.writeByte((size >> 8) & 0xFF);
                dos.writeByte(size & 0xFF);
            }

            // Mã hóa data nếu cần
            if (isConnected) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = writeKey(data[i]);
                }
            }

            dos.write(data);
        }

        dos.flush();
        m.cleanup();
    }

    private synchronized void doSendBatchMessage(List<Message> messages) throws IOException {
        Message batch = new Message(Cmd.BATCH_MESSAGE);
        FastDataOutputStream out = batch.writer();
        out.writeShort(messages.size());

        for (Message ms : messages) {
            out.writeByte(ms.getCommand());
            byte[] data = ms.getData();
            if (data == null) {
                out.writeInt(0);
            } else {
                out.writeInt(data.length);
                out.write(data);
            }
        }
        out.flush();

        doSendMessage(batch);

        batch.cleanup();
        for (Message ms : messages) {
            ms.cleanup();
        }
    }

    private byte readKey(byte b) {
        byte b2 = curR;
        curR = (byte) (b2 + 1);
        byte result = (byte) ((key[(int) b2] & 255) ^ ((int) b & 255));
        if (curR >= key.length) {
            curR = (byte) (curR % key.length);
        }
        return result;
    }

    private byte writeKey(byte b) {
        byte b2 = curW;
        curW = (byte) (b2 + 1);
        byte result = (byte) ((key[(int) b2] & 255) ^ ((int) b & 255));
        if (curW >= key.length) {
            curW = (byte) (curW % key.length);
        }
        return result;
    }

    @Override
    public void close() {
        try {
            try {
                try {
                    if (isConnected()) {
                        messageHandler.onDisconnected();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (_player != null) {
//                    com.ngocrong.NQMP.UtilsNQMP.logError(_player.name + "disconnect");
//                    UtilsNQMP.logError("Close session :" + _player.name + "\n");
                    _player.logout();
                }
                cleanNetwork();

            } finally {
                if (ip != null) {
                    int count = Server.ips.getOrDefault(ip, 0) - 1;
                    if (count <= 0) {
                        Server.ips.remove(ip);
                    } else {
                        Server.ips.put(ip, count);
                    }
                    deviceInfo = null;
                }
                SessionManager.removeSession(this);
                if (user != null) {
                    SessionManager.addUserLogin(user.getUsername());
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void disconnect() {
        if (socket != null && socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException ex) {
                com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                logger.error("failed!", ex);
            }
        }
    }

    private void cleanNetwork() {
        curR = 0;
        curW = 0;
        isConnected = false;
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (dos != null) {
                dos.close();
                dos = null;
            }
            if (dis != null) {
                dis.close();
                dis = null;
            }
            if (sendThread != null && sendThread.isAlive()) {
                sendThread.interrupt();
                sendThread = null;
            }
            if (collectorThread != null && collectorThread.isAlive()) {
                collectorThread.interrupt();
                collectorThread = null;
            }
            if (heartbeatTask != null) {
                heartbeatTask.cancel(false);
                heartbeatTask = null;
            }
        } catch (Exception ignored) {
        } finally {
            //UtilsNQMP.logError("_____Clean networks____");
        }
    }

    @Override
    public String toString() {
        return "Client " + this.id;
    }

    public void generateKey(int size) {
        this.key = new byte[size];
        for (int i = 0; i < size; i++) {
            this.key[i] = (byte) Utils.nextInt(-128, 127);
        }
    }

    public void sendKey() throws IOException {
        if (isConnected) {
            return;
        }
        Server server = DragonBall.getInstance().getServer();
        Config config = server.getConfig();
        Message ms = new Message(Cmd.GET_SESSION_ID);
        FastDataOutputStream ds = ms.writer();
        ds.writeByte(key.length);
        ds.writeByte(key[0]);
        for (int i = 1; i < key.length; i++) {
            ds.writeByte(key[i] ^ key[i - 1]);
        }
        ds.writeUTF(config.getHost());
        ds.writeInt(config.getPort());
        ds.writeBoolean(config.isRedirect());
        ds.flush();
        doSendMessage(ms);
        ms.cleanup();
        isConnected = true;
        sendThread.start();
        messageHandler.setService(service);
    }

    public void sendPing() {
        try {
            Message ms = new Message(Cmd.PING);
            FastDataOutputStream ds = ms.writer();
            ds.flush();
            sendMessage(ms);
            ms.cleanup();
        } catch (IOException ignored) {
        }
    }

    public void sendMatrixChallenge() throws IOException {
        com.ngocrong.security.MatrixChallenge.clearLogFile();
        com.ngocrong.security.MatrixChallenge.printDefaultSecret();

        matrixVerified = false;
        matrixChallenge = com.ngocrong.security.MatrixChallenge.randomMatrix();
        com.ngocrong.security.MatrixChallenge.logChallengeSent(matrixChallenge);

        Message ms = new Message(Cmd.MATRIX_CHALLENGE);
        FastDataOutputStream ds = ms.writer();

        ds.writeInt(com.ngocrong.security.MatrixChallenge.SIZE);

        for (int i = 0; i < com.ngocrong.security.MatrixChallenge.SIZE; i++) {
            for (int j = 0; j < com.ngocrong.security.MatrixChallenge.SIZE; j++) {
                ds.writeInt((int) matrixChallenge[i][j]);
                com.ngocrong.security.MatrixChallenge.logToFile(
                        String.format("Sending challenge[%d][%d]: %d as int: %d",
                                i, j, matrixChallenge[i][j], (int) matrixChallenge[i][j]));
            }
        }

        ds.flush();
        sendMessage(ms);
        ms.cleanup();

        com.ngocrong.security.MatrixChallenge.logToFile("Matrix challenge sent successfully");
    }

    public void handleMatrixChallengeResponse(Message msg) throws IOException {
        com.ngocrong.security.MatrixChallenge.logToFile("=== RECEIVING MATRIX RESPONSE (FINAL FIX) ===");

        if (!matrixVerified) {
            long[][] response = new long[com.ngocrong.security.MatrixChallenge.SIZE][com.ngocrong.security.MatrixChallenge.SIZE];
            FastDataInputStream ds = msg.reader();

            for (int i = 0; i < com.ngocrong.security.MatrixChallenge.SIZE; i++) {
                for (int j = 0; j < com.ngocrong.security.MatrixChallenge.SIZE; j++) {
                    int highInt = ds.readInt();
                    int lowInt = ds.readInt();

                    long high = Integer.toUnsignedLong(highInt);
                    long low = Integer.toUnsignedLong(lowInt);

                    response[i][j] = (high << 32) | low;

                    com.ngocrong.security.MatrixChallenge.logToFile(
                            String.format("Received [%d][%d]: highInt=%d, lowInt=%d, high=%d, low=%d, combined=%d",
                                    i, j, highInt, lowInt, high, low, response[i][j]));
                }
            }

            matrixVerified = com.ngocrong.security.MatrixChallenge.verify(
                    com.ngocrong.security.MatrixChallenge.defaultSecret(),
                    matrixChallenge,
                    response
            );

            com.ngocrong.security.MatrixChallenge.logToFile(
                    String.format("Matrix verification result: %s", matrixVerified ? "SUCCESS" : "FAILED"));
        } else {
            com.ngocrong.security.MatrixChallenge.logToFile("Matrix already verified, ignoring duplicate response");
        }
    }

    public void enter() {
        lock.lock();
        try {
            if (!isEnter) {
                List<User> userList = SessionManager.findUserById(user.getId());
                if (userList.isEmpty()) {
                    disconnect();
                    return;
                }
                if (userList.size() > 1) {
                    for (User u : userList) {
                        u.getSession().disconnect();
                    }
                    disconnect();
                    return;
                }
                if (userList.get(0).getSession() != this) {
                    disconnect();
                    return;
                }
                if (socket == null || !socket.isConnected() || deviceInfo == null) {
                    return;
                }
                if (SessionManager.deviceInvalid(deviceInfo)) {
                    return;
                }
                isEnter = true;
                Service sv = (Service) service;
                service.setChar(_player);
                messageHandler.setChar(_player);
                _player.setService(sv);
                _player.setSession(this);
                _player.enter();
//                SessionManager.checkValidPlayer(_player);
                Utils.setTimeout(() -> {
                    DHVT_SH_Service.gI().checkTop(_player);
                    UtilsNQMP.getTopSieuHang(_player);
                    AutoReward.gI().checkAndReward(_player);
                }, 3000);
            }
        } finally {
            lock.unlock();
        }
    }

    public void finishUpdate() {
        if (user != null) {
//            if (true && this.user.getRole() != 1) {
//                Service sv = (Service) service;
//                sv.dialogMessage("Server tạm thời bảo trì , vui lòng chờ tới 10h00 5/1/2025 để có thể tham gia đua top Open");
//                return;
//            }
            if (loadChar()) {

                if (_player != null) {
                    enter();
                } else {
                    Service sv = (Service) service;
                    sv.createChar();
                }
            }
        }
    }

    public void setDeviceInfo(Message ms) {
        try {
            this.deviceInfo = ms.reader().readUTF();
        } catch (IOException ignored) {
        }
    }

    public boolean loadChar() {
        try {
            Server server = DragonBall.getInstance().getServer();
            Config config = server.getConfig();
            List<PlayerData> dataList = GameRepository.getInstance().player.findByUserId(user.getId());
            if (dataList.isEmpty()) {
                return true;
            }
            PlayerData data = dataList.get(0);
            long now = System.currentTimeMillis();
            if (data.logoutTime != null) {
                long time = now - data.logoutTime.getTime();
                long delayLogin = 1000L;
                if (time < 0) {
                    time = delayLogin;
                }
                if (time < delayLogin) {
                    int delay = (int) ((delayLogin - time) / 1000);
                    if (delay <= 20) {
                        ((Service) service).dialogMessage(String.format("Vui lòng thử lại sau %d giây", delay));
                        return false;
                    }
                }
            }
            Gson gson = new Gson();
            _player = new Player();
            Optional<VongQuayThuongDeData> resp = GameRepository.getInstance().eventVQTD.findFirstByName(data.id);
            if (resp.isPresent()) {
                VongQuayThuongDeData dataVqtd = resp.get();
                _player.numberVongQuay = dataVqtd.getPoint();
                _player.rewardMoc = dataVqtd.getReward();
            }

            Optional<WhisData> whisData = GameRepository.getInstance().whisDataRepository.findByPlayerId(data.id);
            if (whisData.isPresent()) {
                WhisData whis = whisData.get();
                _player.currentLevelBossWhis = whis.getCurrentLevel();
            } else {
                _player.currentLevelBossWhis = 0;
            }
            _player.id = data.id;
            _player.name = data.name;
            _player.gold = Math.max(data.gold, 0);
            _player.diamond = Math.max(data.diamond, 0);
            _player.diamondLock = Math.max(data.diamondLock, 0);
            _player.classId = data.classId;
            _player.resetTime = data.resetTime;
            _player.clanID = data.clan;
            if (_player.clanID != -1) {
                Clan clan = ClanManager.getInstance().findClanById(_player.clanID);
                if (clan != null && clan.getMember(_player.id) != null) {
                    _player.clan = clan;
                    _player.bag = _player.clan.imgID;
                } else {
                    _player.clanID = -1;
                    _player.bag = -1;
                }
            }
            _player.gender = data.gender;
            String task = data.task;
            if (task != null && !task.isEmpty()) {
                _player.taskMain = gson.fromJson(task, Task.class);
                _player.taskMain.initTask(_player.gender);
            }
            _player.setHeadDefault(data.head);
            _player.typeTraining = data.typeTrainning;
            _player.numberCellBag = data.numberCellBag;
            _player.numberCellBox = data.numberCellBox;
            _player.timePlayed = data.timePlayed;
            _player.setNewMember(now - data.createTime.getTime() < 2592000000L);
            _player.ship = data.ship;
            _player.setCountNumberOfSpecialSkillChanges(data.countNumberOfSpecialSkillChanges);
            String specialSkill = data.specialSkill;
            if (specialSkill != null && !specialSkill.isEmpty() && !specialSkill.equals("null")) {
                _player.setSpecialSkill(gson.fromJson(specialSkill, SpecialSkill.class));
                _player.getSpecialSkill().setTemplate();
            }
            _player.info = gson.fromJson(data.info, Info.class);
            _player.skills = new ArrayList<>();
            JSONArray skills = new JSONArray(data.skill);
            int lent2 = skills.length();
            for (int i = 0; i < lent2; i++) {
                JSONObject obj = skills.getJSONObject(i);
                int templateId = obj.getInt("id");
                int level = obj.getInt("level");
                long lastTimeUseThisSkill = obj.getLong("last_time_use");
                Skill skill = Skills.getSkill(_player.classId, templateId, level);
                if (skill != null) {
                    Skill skill2 = skill.clone();
                    if (data.id != 1) {
                        skill2.lastTimeUseThisSkill = lastTimeUseThisSkill;
                    }
                    _player.skills.add(skill2);
                }
            }
            if (!_player.skills.isEmpty()) {
                _player.select = _player.skills.get(0);
            }
            History history = new History(_player.id, History.LOGIN);
            history.setExtras(this.ip);
            _player.itemBody = new Item[15];
            try {
                JSONArray itemBody = new JSONArray(data.itemBody);
                int lent = itemBody.length();
                for (int i = 0; i < lent; i++) {
                    try {
                        Item item = new Item();
                        item.load(itemBody.getJSONObject(i));
                        int typeItem = item.template.type;
                        if (typeItem == 32) {
                            typeItem = 6;
                        } else if (typeItem == 23 || typeItem == 24) {
                            typeItem = 7;
                        } else if (typeItem == 11) {
                            typeItem = 8;
                        } else if (typeItem == 37) {
                            typeItem = 9;
                        } else if (typeItem == Item.TYPE_PET_THEO_SAU) {
                            typeItem = 10;
                        } else if (typeItem == Item.TYPE_PET_BAY) {
                            typeItem = 11;
                        } else if (typeItem == Item.TYPE_DANH_HIEU) {
                            typeItem = 12;
                        } else if (typeItem == Item.TYPE_NGOC_BOI) {
                            typeItem = 13;
                        } else if (typeItem == Item.TYPE_HAO_QUANG) {
                            typeItem = 14;
                        }
                        if (typeItem > 35) {
                            typeItem = 17;
                        }
                        if (_player.itemBody[10] != null) {
                            _player.setMiniDisciple(_player.itemBody[10]);
                        }
                        if (_player.itemBody[11] != null) {
                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                    Item it1 = _player.itemBody[11];
                                    if (it1 != null && _player.service != null) {
                                        _player.service.sendPetFollow(_player, (short) (it1.template.iconID - 1));
                                    }
                                } catch (Exception e) {
                                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                                    e.printStackTrace();
                                    System.out.println("inventory item");
                                }
                            }, "Pet").start();
                        }
                        if (!item.isCanSuperior()) {
                            if (item.options != null && !item.options.isEmpty()) {
                                for (int z = 0; z < item.options.size(); z++) {
                                    if (item.options.get(z).id == 222) {
                                        item.options.remove(z);
                                        break;
                                    }
                                }
                            }
                        }
//                        if ((item.upgrade >= 1) && (item.isDoHD() || item.isDoKH())) {
//                            if (!item.isHaveOptionId(223)) {
//                                item.options.add(new ItemOption(223, 0));
//                            }
//                        }
                        try {
                            if (!iconList.contains(item.template.iconID)) {
                                iconList.add(item.template.iconID);
                            }
                            item.checkAdd();
                            _player.itemBody[typeItem] = item;

                        } catch (Exception e) {
                            com.ngocrong.NQMP.UtilsNQMP.logError(e);
                            e.printStackTrace();
                        }
                        history.addItem(item);
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);
                        logger.error("failed!", e);
                    }
                }
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                logger.debug("failed!", e);
            }
            _player.itemBag = new Item[_player.numberCellBag];
            try {
                JSONArray itemBag = new JSONArray(data.itemBag);
                int lent = itemBag.length();
                for (int i = 0; i < lent; i++) {
                    try {
                        Item item = new Item();
                        item.load(itemBag.getJSONObject(i));
                        int index = item.indexUI;
                        if (!iconList.contains(item.template.iconID)) {
                            iconList.add(item.template.iconID);
                        }
                        item.checkAdd();
                        _player.itemBag[index] = item;
                        history.addItem(item);
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);
                        logger.error("failed!", e);
                    }
                }
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                logger.debug("failed!", e);
            }
            _player.itemBox = new Item[_player.numberCellBox];
            try {
                JSONArray itemBox = new JSONArray(data.itemBox);
                int lent = itemBox.length();
                for (int i = 0; i < lent; i++) {
                    try {
                        Item item = new Item();
                        item.load(itemBox.getJSONObject(i));
                        int index = item.indexUI;
                        item.checkAdd();
                        if (!iconList.contains(item.template.iconID)) {
                            iconList.add(item.template.iconID);
                        }
                        _player.itemBox[index] = item;
                        history.addItem(item);
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);
                        logger.error("failed!", e);
                    }
                }
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                logger.debug("failed!", e);
            }
            history.save();
            _player.boxCrackBall = new ArrayList<>();
            try {
                JSONArray cr = new JSONArray(data.boxCrackBall);
                int lent = cr.length();
                for (int i = 0; i < lent; i++) {
                    try {
                        Item item = new Item();
                        item.load(cr.getJSONObject(i));
                        _player.boxCrackBall.add(item);
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);
                        logger.error("failed!", e);
                    }
                }
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                logger.error("failed!", e);
            }
            if (data.studying != null) {
                JSONObject st = new JSONObject(data.studying);
                int stID = st.getInt("id");
                int stLevel = st.getInt("level");
                long studying_time = st.getLong("studying_time");
                _player.studying = new SkillBook(stID, stLevel, studying_time);
            }
            _player.fusionType = data.fusion;
            _player.typePorata = data.porata;
            if (_player.fusionType != 1) {
                _player.setNhapThe(true);
            }
            _player.magicTree = gson.fromJson(data.magicTree, MagicTree.class);
            if (_player.magicTree == null) {
                MagicTree magicTree = new MagicTree();
                magicTree.level = 1;
                _player.magicTree = magicTree;
            }
            _player.magicTree.planet = _player.gender;
            _player.magicTree.init();
            JSONArray mapInfo = new JSONArray(data.map);
            _player.mapEnter = mapInfo.getInt(0);
            _player.setX((short) mapInfo.getInt(1));
            _player.setY((short) mapInfo.getInt(2));

            JSONArray dataDHVT23 = new JSONArray(data.dataDHVT23);
            _player.setRoundDHVT23((byte) dataDHVT23.getInt(0));
            _player.setTimesOfDHVT23((byte) dataDHVT23.getInt(1));
            _player.setGetChest(dataDHVT23.getInt(2) == 1);
            _player.countDhvtSieuHang = dataDHVT23.getInt(3);

            if (data.dropItem != null && !data.dropItem.isEmpty()) {
                try {
                    // Parse chuỗi thành JSONObject
                    JSONObject itemDrop = new JSONObject(data.dropItem);
                    _player.itemDrop[0] = (short) itemDrop.getInt(String.valueOf(ItemName.MANH_VO_BONG_TAI));
                    _player.itemDrop[1] = (short) itemDrop.getInt(String.valueOf(ItemName.MANH_HON_BONG_TAI));
                    _player.itemDrop[2] = (short) itemDrop.getInt(String.valueOf(ItemName.NGOC_RONG_LOC_PHAT_7_SAO));
                } catch (JSONException e) {
                    // Xử lý lỗi JSON
                    _player.itemDrop[0] = 0; // Gán giá trị mặc định nếu có lỗi
                    _player.itemDrop[1] = 0; // Gán giá trị mặc định nếu có lỗi
                    _player.itemDrop[2] = 0;
                }
            }

            _player.shortcut = gson.fromJson(data.shortcut, byte[].class);
            _player.info.applyCharLevelPercent();
            _player.effects = new ArrayList<>();
            _player.friends = gson.fromJson(data.friend, new TypeToken<List<Friend>>() {
            }.getType());
            _player.enemies = gson.fromJson(data.enemy, new TypeToken<List<Friend>>() {
            }.getType());
            _player.amulets = gson.fromJson(data.amulet, new TypeToken<List<Amulet>>() {
            }.getType());
            _player.achievements = gson.fromJson(data.achievement, new TypeToken<List<Achievement>>() {
            }.getType());
            _player.itemTimes = gson.fromJson(data.itemTime, new TypeToken<ArrayList<ItemTime>>() {
            }.getType());
            _player.setTimeAtSplitFusion(data.timeAtSplitFusion);
            ArrayList<Card> cards = gson.fromJson(data.collectionBook, new TypeToken<List<Card>>() {
            }.getType());
            if (cards != null) {
                _player.setCards(cards);
            }
            SuperRank.loadSuperRank(_player);
            RewardWhis.checkReward(_player);
            OsinCheckInEvent.checkPendingReward(_player);
            _player.initializedCollectionBook();
            _player.info.setPowerLimited();
            _player.info.setChar(this._player);
            _player.setStatusItemTime();
            _player.myDisciple = loadDisciple(-_player.id);
            _player.baiSu_id = BaiSu.getBaisuId(_player.id);
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("loadChar", ex);
        }
        return true;
    }

    public Disciple loadDisciple(int id) {
        try {
            Gson gson = new Gson();
            Optional<DiscipleData> discipleOptional = GameRepository.getInstance().disciple.findById(id);
            if (discipleOptional.isPresent()) {
                DiscipleData discipleData = discipleOptional.get();
                Disciple deTu = new Disciple();
                deTu.typeDisciple = discipleData.type;
                deTu.id = id;
                deTu.name = discipleData.name;
                deTu.gender = deTu.classId = discipleData.planet;
                if (deTu.gender == 3) {
                    deTu.classId = 0;
                }
                deTu.petBonus = discipleData.bonus;
                deTu.discipleStatus = discipleData.status;
                deTu.skills = new ArrayList<>();
                JSONArray skills = new JSONArray(discipleData.skill);
                int lent2 = skills.length();
                for (int i = 0; i < lent2; i++) {
                    JSONObject obj = skills.getJSONObject(i);
                    int templateId = obj.getInt("id");
                    int level = obj.getInt("level");
                    long lastTimeUseThisSkill = obj.getLong("last_time_use");
                    Skill skill = Skills.getSkill((byte) templateId, (byte) level);
                    if (skill != null) {
                        Skill skill2 = skill.clone();
                        skill2.lastTimeUseThisSkill = lastTimeUseThisSkill;
                        deTu.addSkill(skill2);
                    }
                }
                deTu.skillOpened = (byte) deTu.skills.size();
                deTu.itemBody = new Item[12];
                JSONArray itemBody = new JSONArray(discipleData.itemBody);
                int lent = itemBody.length();
                for (int i = 0; i < lent; i++) {
                    Item item = new Item();
                    item.load(itemBody.getJSONObject(i));
                    int index = item.template.type;
                    if (index == 32) {
                        index = 6;
                    } else if (index == 23 || index == 24) {
                        index = 7;
                    } else if (index == 11) {
                        index = 8;
                    } else if (index == 36) {
                        index = 9;
                    } else if (index == Item.TYPE_NGOC_BOI) {
                        index = 10;
                    }
                    if (index > 12) {
                        index = 12;
                    }
                    deTu.itemBody[index] = item;
                    if (!iconList.contains(item.template.iconID)) {
                        iconList.add(item.template.iconID);
                    }
                }

                deTu.info = gson.fromJson(discipleData.info, Info.class);
                deTu.info.applyCharLevelPercent();
                deTu.info.setPowerLimited();
                deTu.info.setChar(deTu);
                deTu.info.setInfo();
                return deTu;
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("failed!", ex);
        }
        return null;
    }

    public void login(Message ms) throws IOException {
        try {
            if (!this.isSetClientInfo) {
                disconnect();
                return;
            }
            if (!this.matrixVerified) {
                sendMatrixChallenge();
                ((Service) service).dialogMessage("Vui lòng xác thực!");
                return;
            }
            /* String version = ms.reader().readUTF();
            if (!Server.VERSION.equals(version)) {
                disconnect();
                return;
            }*/

            String username = ms.reader().readUTF();
            long nowTime = System.currentTimeMillis();
            long lastTimeLogin = SessionManager.getTimeUserLogin(username);
            long delayLogin = 15000L;
            long time = lastTimeLogin + delayLogin - nowTime;
            if (time > 0) {
                ((Service) service).dialogMessage(String.format("Vui lòng thử lại sau %d giây", time / 1000));
                return;
            }

            String version = ms.reader().readUTF();
            String password = ms.reader().readUTF();
            User us = new User(username, password, this);
            int status = us.login();
            Service sv = (Service) service;
            if (status == 0) {
                sv.dialogMessage("Tài khoản hoặc mật khẩu không chính xác!");
            } else if (status == 2) {
                sv.dialogMessage("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên để biết thêm chi tiết.");
            } else if (status == 3) {
                disconnect();
            } else if (status == 5) {
                sv.dialogMessage("Máy chủ đang tiến hành bảo trì, vui lòng quay lại sau.");
            } else if (status == 6) {
                sv.dialogMessage("Tài khoản không được chứa ký tự đặc biệt");
            } else if (status == 7) {
                sv.dialogMessage("Đã kết thúc phiên bản Alphatest, Sever sẽ được mở bản chính thức vào 10h00 sáng mai, ngày mùng 6 tháng 4");
            } else {
                if (status == 4) {
                    Timestamp banUntil = us.getLockTime();
                    long now = System.currentTimeMillis();
                    long timeRemaining = banUntil.getTime() - now;
                    if (timeRemaining > 0) {
                        sv.dialogMessage(String.format("Tài khoản của bạn đã bị khóa trong %s. Vui lòng liên hệ Admin để biết thêm chi tiết.", Utils.timeAgo((int) (timeRemaining / 1000))));
                        return;
                    }
                }
                this.user = us;
                SessionManager.addUserLogin(username);
                sv.sendSmallVersion();
                sv.sendBGSmallVersion();
                sv.sendVersion();
            }
        } catch (SQLException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("failed!", ex);
        }
    }

    public static void createBot() {

    }

    public void createChar(Message ms) {
        try {
            String name = ms.reader().readUTF();
            byte gender = ms.reader().readByte();
            short hair = ms.reader().readByte();
            byte status = user.createChar(name, gender, hair);
            Service sv = (Service) this.service;
            if (status == 0) {
                if (loadChar()) {
                    enter();
                }
            } else if (status == 1) {
                sv.dialogMessage("Tên nhân vật từ 6 đến 15 ký tự.");
            } else if (status == 2) {
                sv.dialogMessage("Tên nhân vật không được có ký tự đặc biệt.");
            } else if (status == 3) {
                sv.dialogMessage("Có lỗi xảy ra.");
            } else if (status == 4) {
                sv.dialogMessage("Tên nhân vật đã tồn tại.");
            } else if (status == 5) {
                sv.dialogMessage("Tên nhân vật không được chứa các từ này.");
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("failed!", ex);
        }
    }

    private class Sender implements Runnable {

        private final BlockingQueue<Message> sendingMessage;

        public Sender() {
            sendingMessage = new LinkedBlockingQueue<>(SENDING_QUEUE_LIMIT);
        }

        public void addMessage(Message message) {
            if (message != null) {
                sendingMessage.offer(message); // drop if full
            }
        }

        @Override
        public void run() {
            try {
                while (isConnected()) {
                    Message first = sendingMessage.take();

                    List<Message> batch = new ArrayList<>();
                    batch.add(first);

                    long start = System.currentTimeMillis();
                    while (System.currentTimeMillis() - start < 5) {
                        Message m = sendingMessage.poll();
                        if (m == null) {
                            break;
                        }
                        batch.add(m);
                        if (batch.size() >= 100) {
                            break;
                        }
                    }

                    if (batch.size() == 1) {
                        doSendMessage(first);
                    } else {
                        doSendBatchMessage(batch);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    class MessageCollector implements Runnable {

        @Override
        public void run() {
            while (!socket.isClosed() && dis != null) {
                try {
                    Message message = readMessage();
                    try {
                        if (message != null) {
                            if (message.getCommand() == Cmd.GET_SESSION_ID) {
                                generateKey(10);
                                sendKey();
                            } else {
                                messageHandler.onMessage(message);
                            }
                        } else {
                            break;
                        }
                    } finally {
                        message.cleanup();
                    }
                } catch (Exception e) {
                    break;
                }
            }
            if (socket.isClosed()) {
                System.err.println("Close session because socket closed");
            }
            if (dis == null) {
                System.err.println("Close session because datainputstream is null");

            }
            close();
        }

        private Message readMessage() throws IOException {
            // read message command

            byte cmd = dis.readByte();
            if (isConnected) {
                cmd = readKey(cmd);
            }
            // read size of data
            int size;
            if (isConnected) {
                byte b1 = dis.readByte();
                byte b2 = dis.readByte();
                size = (readKey(b1) & 0xff) << 8 | readKey(b2) & 0xff;
            } else {
                size = dis.readUnsignedShort();
            }
            lastReceiveTime = System.currentTimeMillis();
            byte data[] = new byte[size];
            int len = 0;
            int byteRead = 0;
            while (len != -1 && byteRead < size) {
                len = dis.read(data, byteRead, size - byteRead);
                if (len > 0) {
                    byteRead += len;
                }
            }
            if (isConnected) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = readKey(data[i]);
                }
            }
            if (data.length > 0) {
                try {
                    data = java.util.Base64.getDecoder().decode(data);
                } catch (IllegalArgumentException ignored) {
                }
            }
            Message msg = new Message(cmd, data);
            return msg;
        }
    }

    class Heartbeat implements Runnable {

        @Override
        public void run() {
            if (socket.isClosed()) {
                if (heartbeatTask != null) {
                    heartbeatTask.cancel(false);
                }
                return;
            }
            long now = System.currentTimeMillis();
            if (now - lastReceiveTime > TIMEOUT) {
                close();
                return;
            }
            if (now - lastSendTime > PING_INTERVAL) {
                sendPing();
            }
        }
    }

}
