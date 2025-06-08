package com.ngocrong.user;

import com.ngocrong.bot.BossManager;
import com.ngocrong.bot.MiniDisciple;
import com.ngocrong.bot.Disciple;
import com.ngocrong.bot.boss.*;
import com.ngocrong.bot.boss.karin.Karin;
import com.ngocrong.bot.boss.karin.Yajiro;
import com.ngocrong.calldragon.CallDragon1Star;
import com.ngocrong.clan.*;
import com.ngocrong.collection.Card;
import com.ngocrong.collection.CardTemplate;
import com.ngocrong.combine.*;
import com.ngocrong.consts.*;
import com.ngocrong.crackball.Reward;
import com.ngocrong.data.*;
import com.ngocrong.event.Event;
import com.ngocrong.item.*;
import com.ngocrong.lib.Menu;
import com.ngocrong.event.OsinCheckInEvent;
import com.ngocrong.map.*;
import com.ngocrong.map.expansion.blackdragon.MBlackDragonBall;
import com.ngocrong.map.expansion.blackdragon.ZBlackDragonBall;
import com.ngocrong.map.tzone.*;
import com.ngocrong.model.*;
import com.ngocrong.network.Session;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.server.*;
import com.ngocrong.shop.Consignment;
import com.ngocrong.skill.*;
import com.ngocrong.task.TaskText;
import com.ngocrong.util.Utils;
import com.ngocrong.calldragon.CallDragon;
import com.ngocrong.crackball.CrackBall;
import com.ngocrong.effect.AmbientEffect;
import com.ngocrong.effect.EffectChar;
import com.ngocrong.lib.KeyValue;
import com.ngocrong.lib.RandomCollection;
import com.ngocrong.lucky.Lucky;
import com.ngocrong.mob.Mob;
import com.ngocrong.mob.MobFactory;
import com.ngocrong.mob.MobType;
import com.ngocrong.network.Message;
import com.ngocrong.network.Service;
import com.ngocrong.shop.Shop;
import com.ngocrong.shop.Tab;
import com.ngocrong.task.Task;
import com.ngocrong.top.Top;
import com.ngocrong.top.TopInfo;
import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.ngocrong.NQMP.CMS.ItemCMS;
import com.ngocrong.NQMP.CMS.ItemCMS_Service;
import com.ngocrong.NQMP.ConSoMayMan;
import com.ngocrong.NQMP.DHVT23;
import com.ngocrong.NQMP.DHVT23_Service;
import com.ngocrong.NQMP.DHVT_SH.DHVT_SH_Service;
import com.ngocrong.NQMP.DHVT_SH.SuperRank;
import com.ngocrong.NQMP.DaNangCap.EventDaNangCap;
import com.ngocrong.NQMP.KeoBuaBao;
import com.ngocrong.NQMP.MainUpdate;
import com.ngocrong.NQMP.TamThangBa.Event1;
import com.ngocrong.NQMP.Tet2025.EventTet2025;
import com.ngocrong.NQMP.UtilsNQMP;
import com.ngocrong.NQMP.Whis.WhisInSingleMap;
import com.ngocrong.bot.Boss;
import com.ngocrong.bot.BotCold;
import com.ngocrong.bot.VirtualBot;
import com.ngocrong.bot.VirtualBot_SoSinh;
import com.ngocrong.bot.boss.BossDisciple.SuperBroly;
import com.ngocrong.calldragon.CallDragonNamek;
import com.ngocrong.mob._BigBoss.Hirudegarn;
import java.io.BufferedWriter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ngocrong.network.FastDataOutputStream;
import com.ngocrong.user.func.BaiSu;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import com.ngocrong.bot.boss.BossDisciple.Broly;

@Getter
@Setter
public class Player {

    public static final byte UPDATE_ONE_SECONDS = 0;
    public static final byte UPDATE_THIRTY_SECONDS = 1;
    public static final byte UPDATE_ONE_MINUTES = 2;
    public static final byte TAI_TAO = 3;
    public static final byte UPDATE_FIVE_SECONDS = 4;
    public static final byte UPDATE_HALF_SECONDS = 5;
    public static final byte UPDATE_TEN_SECONDS = 6;
    public static final int DISTANCE_EFFECT = 150;

    private static Logger logger = Logger.getLogger(Player.class);
    public static long delayToCallDragonNamek;
    public static long lastTimeCallDragon;
    // BYTE
    public byte classId;
    public byte gender;
    public byte pointPk;
    public byte teleport;
    public byte statusMe = 1;
    public byte ship;
    public byte typeTraining;
    public byte bag = -1;
    public byte typePk;
    public byte flag;
    private byte commandTransport;
    private String captcha;
    private int playerReportedID;
    private String playerReportedName;
    private byte numCheck;
    public int typePorata;
    private byte commandPK;
    public byte mucCuoc = -1;

    public byte lastPlayerSelect = -1;
    public byte lastServerSelect = -1;

    public byte roundDHVT23 = 9;
    public byte timesOfDHVT23 = 0;

    public short mapPhuHo = -1;
    public short percentPhuHo = 0;

    public boolean isGetChest;

    // SHORT
    private short head, headDefault;
    private short body;
    private short leg;
    private short wp;
    private short x, y, preX, preY;
    private short eff5buffhp, eff5buffmp;
    private short idAuraEff = -1;
    private short idEffSetItem = -1;
    private short countNumberOfSpecialSkillChanges;
    public short idNRNM = -1;
    public short idGo = -1;
    public short[] itemDrop = new short[3];

    // INT
    public int id;
    public int diamond;
    public int diamondLock;
    public int timeIsMoneky;
    public int hpPercent;
    public int betAmount;
    public int freezSeconds;
    public int seconds;
    public int clanID;
    public int mapEnter;
    public int idMount;
    public int dameDown;
    public int goldTrading;
    public int fusionType;
    private int currZoneId;
    public int capsule;
    public int timePlayed;
    public int numberCellBag, numberCellBox;
    public int deltaTime = 1000;
    public int testCharId = -9999;
    public int killCharId = -9999;
    public int goldBarUnpaid;
    private int tickMove;
    private int percentDamageBonus;
    private int phuX;
    private int paramOptLaze;
    private int paramOptTuSat;
    public int pointDhvtSieuhang;
    public int countDhvtSieuHang = 0;
    public int baiSu_id = -1;
    // Tet
    public int pointPhaoHoa;
    public int pointBanhChung;
    public int pointHopQua;
    public boolean isChangePoint = false;

    public int pointHoaSumVay;
    public int pointHoaSacMau;
    public int pointThoiVang;
    // VQTD
    public int numberVongQuay;
    public int rewardMoc;
    // LONG

    public long gold;
    public long lastRequestPean;
    public long lastAttack;
    private long lastUsePotion;
    private long lastTimeChatGlobal;
    private long timeAtSplitFusion;
    private long lastTimeRequestChangeZone;
    private long lastTimeMove;
    private long lastTick, currTick;
    public long lastUseRecoveryEnery;
    private long lastTimeUsePorata;
    private long lastTimeRevenge;
    private long lastTimeTrade;
    private long lastPickup;
    public long lastActionCMS;
    private long lastTimeUseGiftCode;
    public long lastLogin = System.currentTimeMillis();
    private long lastTimeThachDau;
    private long lastTimePickUpDragonBallNamec;
    public long limitDame = -1;
    public long lastEnterMap = System.currentTimeMillis();
    // BOOL
    public boolean isAttackWhis;
    private boolean isNewMember;
    private boolean isNhapThe;
    private boolean isLoggedOut;
    private boolean isMask;
    private boolean isFreeze, isSleep, isBlind, isProtected, isHuytSao, isHeld, isCritFirstHit;
    private boolean isBuaTriTue, isBuaManhMe, isBuaDaTrau, isBuaOaiHung, isBuaBatTu, isBuaDeoDai, isBuaThuHut,
            isBuaDeTu, isBuaTriTue3, isBuaTriTue4;
    private boolean isDragonBallNamek1, isDragonBallNamek2, isDragonBallNamek3, isDragonBallNamek4;
    private boolean isDead;
    private boolean isKhangTDHS;
    private boolean isHaveMount;
    private boolean isGoBack;
    private boolean isCuongNo, isBoHuyet, isGiapXen, isBoKhi, isAnDanh, isMayDo, isDuoiKhi, isPudding, isXucXich,
            isKemDau, isMiLy, isSushi,
            isCuongNo2, isBoHuyet2, isBoKhi2, isPhieuX2TNSM;

    private boolean isUocThienMenh1, isUocThienMenh2, isUocThienMenh4, isUocThienMenh5;
    private boolean isUocLocPhat1, isUocLocPhat2, isUocLocPhat3, isUocLocPhat7;
    private boolean isDauVang, isNuocTangCuong, isCaNoiGian, isQuaHongDao;
    private boolean isNoNeedToConfirm;
    private boolean isVoHinh;
    private boolean isUnaffectedCold;
    private boolean isChocolate, isStone;
    private boolean isSkillSpecial;
    private boolean isTrading;
    public boolean isCharge;
    public boolean isRecoveryEnergy;
    private boolean isMonkey;
    private boolean isHaveEquipTeleport;
    private boolean isHaveEquipSelfExplosion;
    private boolean isExploded;
    private boolean isAutoPlay;
    private boolean setThienXinHang, setKirin, setSongoku, setPicolo, setOcTieu, setPikkoroDaimao, setKakarot, setCaDic,
            setNappa, setThanLinh, caiTrangLaze, caiTrangTuSat, setHuyDiet;
    public boolean isCold;
    private boolean isMod;
    private boolean isHaveEquipInvisible, isHaveEquipTransformIntoChocolate, isHaveEquipTransformIntoStone,
            isHaveEquipMiNuong, isHaveEquipBulma, isHaveEquipXinbato, isHaveEquipBuiBui, isDoSaoPhaLe, optionLaze,
            optionTuSat;
    private boolean isInvisible;
    public boolean inFighting;
    private boolean isWaitToCallDragonNamek;
    private boolean isRewardTNSMDragonNamek;

    public boolean[] sachdacbiet = new boolean[8];

    // VP SK TET
    private boolean isXoai, isDudu, isMamTraiCay, isMangCau, isDuaXanh, isTraiSung;
    // ARRAY LIST
    public ArrayList<Skill> skills;
    public ArrayList<EffectChar> effects;
    public ArrayList<KeyValue> menus = new ArrayList<>();
    public Menu menu;
    public ArrayList<Friend> friends;
    public ArrayList<Friend> enemies;
    public ArrayList<ItemTime> itemTimes;
    public ArrayList<Integer> listAccessMap;
    public ArrayList<Amulet> amulets;
    public ArrayList<Item> boxCrackBall;
    public ArrayList<Achievement> achievements;
    public ArrayList<KeyValue> itemsTrading;
    public ArrayList<ItemCMS> itemsCMS = new ArrayList<>();
    public HistoryGoldBar hgb;
    public int viewTop = -1;

    public Item danhHieu() {
        try {
            return itemBody[12];
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<Card> cards;
    private ArrayList<AmbientEffect> ambientEffects;
    private ArrayList<KeyValue> listMapTransport;
    private ArrayList<MessageTime> messageTimes;

    // ARRAY ITEM
    public Item[] itemBody;
    public Item[] itemBag;
    public Item[] itemBox;

    // OBJECT
    public Task taskMain;
    public Service service;
    public String name;
    public Zone zone;
    public Clan clan;
    public Info info;
    public Disciple myDisciple;
    public Skill select;
    public Shop shop;
    public MagicTree magicTree;
    public Invite invite;
    public Combine combine;
    private Player trader;
    private Status status;
    public Hold hold;
    public Mob mobMe;
    private KeyValue currMap;
    private Item itemLoot;
    public SkillBook studying;
    private CrackBall crackBall;
    private Lucky lucky;
    private Session session;
    private PetFollow petFollow;
    private CallDragon callDragon;
    private SpecialSkill specialSkill;
    private PowerInfo accumulatedPoint;
    private MiniDisciple miniDisciple;
    private InputDialog inputDlg;
    public Timestamp resetTime;
    public PlayerService playerService;
    public SuperRank superrank;
    // LOCK
    public Lock lock = new ReentrantLock();
    public Lock lockAction = new ReentrantLock();

    // ARRAY
    public long[] lastUpdates = new long[100];
    public byte[] shortcut;

    public Player() {
        messageTimes = new ArrayList<>();
        listAccessMap = new ArrayList<>();
        invite = new Invite();
        ambientEffects = new ArrayList<>();
        long now = System.currentTimeMillis();
        this.lastTimeRequestChangeZone = now;
        Arrays.fill(lastUpdates, now);
        lastAttack = now;
    }

    public void initializedCollectionBook() {
        if (cards == null) {
            cards = new ArrayList<>();
        }
        if (cards.size() < Card.templates.size()) {
            for (CardTemplate cardT : Card.templates) {
                boolean isExist = false;
                for (Card card : cards) {
                    if (card.id == cardT.id) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    Card cardNew = new Card();
                    cardNew.id = cardT.id;
                    cardNew.level = 0;
                    cardNew.amount = 0;
                    cardNew.isUse = false;
                    cards.add(cardNew);
                }
            }
        }
        for (Card card : cards) {
            card.setTemplate();
        }
        setAuraEffect();
    }

    public void moveTo(int x, int y) {
        if (isDead()) {
            return;
        }
        if (isBlind() || isFreeze() || isSleep() || isCharge()) {
            return;
        }
        setX((short) x);
        setY((short) y);
        if (zone != null) {
            zone.service.move(this);
        }
    }

    // public void endEscort(int type) {
    // if (type == 1) {
    // if (escortedPerson instanceof DuongTang) {
    // addMerit(ItemName.VONG_KIM_CO, 100);
    // service.sendThongBao("Nhiệm vụ hoàn thành, bạn được 100 điểm công đức");
    // }
    // }
    // this.escortedPerson = null;
    // }
    public void setAuraEffect() {
        short id = -1;
        for (Card card : cards) {
            if (card.isUse) {
                if (card.id == 956) {
                    id = card.template.aura;
                    break;
                }
            }
        }
        if (itemBody[14] != null) {
            id = itemBody[14].template.part;
            System.err.println("12345");
        }
        idAuraEff = id;
    }

    public String getName() {
        String name = this.name;
        if (clan != null) {
            if (!Strings.isNullOrEmpty(clan.abbreviationName)) {
                name = String.format("[%s] %s", clan.abbreviationName, this.name);
            }
        }
        return name;
    }

    public void collectionBookACtion(Message ms) {
        try {
            byte action = ms.reader().readByte();
            int id = -1;
            if (ms.reader().available() > 0) {
                id = ms.reader().readShort();
            }
            if (action == 0) {
                service.viewCollectionBook();
            }
            if (action == 1) {
                Card c = getCollectionCard(id);
                if (c != null) {
                    if (c.level > 0) {
                        if (!c.isUse) {
                            long size = cards.stream().filter(a -> a.isUse).count();
                            if (size >= 3) {
                                return;
                            }
                        }
                        c.isUse = !c.isUse;
                        service.useCard(c.id, c.isUse);
                        info.setInfo();
                        service.loadPoint();
                        zone.service.playerLoadBody(this);
                        int eff = idAuraEff;
                        setAuraEffect();
                        if (eff != idAuraEff) {
                            zone.service.setIDAuraEff(this.id, this.idAuraEff);
                        }
                    }
                    return;
                }

            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 117");
            logger.error("collectionBookAction error!", ex);
        }
    }

    public boolean addAmbientEffect(AmbientEffect eff) {
        synchronized (ambientEffects) {
            int i = 0;
            for (AmbientEffect e : ambientEffects) {
                if (e.id == eff.id) {
                    ambientEffects.set(i, eff);
                    return false;
                }
                i++;
            }
            this.ambientEffects.add(eff);
        }
        return true;
    }

    public void updateAmbientEffect() {
        synchronized (ambientEffects) {
            long now = System.currentTimeMillis();
            List<AmbientEffect> list = new ArrayList<>();
            boolean isUpdate = false;
            for (AmbientEffect eff : ambientEffects) {
                if (now - eff.time >= eff.maintain) {
                    isUpdate = true;
                    list.add(eff);
                    if (eff.id == 24) {
                        zone.service.chat(this, "Nhẹ lại rồi");
                    }
                }
            }
            ambientEffects.removeAll(list);
            if (isUpdate) {
                info.setInfo();
                service.loadPoint();
            }
        }
    }

    public void clearAmbientEffect() {
        if (ambientEffects.size() > 0) {
            synchronized (ambientEffects) {
                ambientEffects.clear();
            }
            info.setInfo();
            service.loadPoint();
        }
    }

    public void removeAmbientEffect(AmbientEffect eff) {
        synchronized (ambientEffects) {
            ambientEffects.remove(eff);
        }
    }

    private Card getCollectionCard(int id) {
        for (Card c : cards) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }

    public void useCollectibleCard(Item item) {
        Card c = getCollectionCard(item.id);
        if (c != null) {
            removeItem(item.indexUI, 1);
            c.amount++;
            service.setCardExp(c.id, c.amount, c.template.max_amount);
            if (c.amount >= c.template.max_amount) {
                c.levelUp();
                if (c.isUse) {
                    info.setInfo();
                    service.loadPoint();
                    zone.service.playerLoadBody(this);
                }
                service.setCardLevel(c.id, c.level);
            }
            service.sendThongBao("Mảnh được cộng vào Sổ Sưu Tầm");
        }
    }

    public void initAchievement() {
        Server server = DragonBall.getInstance().getServer();
        int size = server.achievements.size();
        this.achievements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.achievements.add(new Achievement(i));
        }
    }

    public void updateAmulet() {
        if (amulets != null) {
            if (!amulets.isEmpty()) {
                ArrayList<Amulet> listRemove = new ArrayList<>();
                long now = System.currentTimeMillis();
                boolean isBuaTriTue = false;
                boolean isBuaManhMe = false;
                boolean isBuaDaTrau = false;
                boolean isBuaOaiHung = false;
                boolean isBuaBatTu = false;
                boolean isBuaDeoDai = false;
                boolean isBuaThuHut = false;
                boolean isBuaDeTu = false;
                boolean isBuaTriTue3 = false;
                boolean isBuaTriTue4 = false;
                for (Amulet amulet : amulets) {
                    if (amulet.expiredTime < now) {
                        listRemove.add(amulet);
                    } else {
                        switch (amulet.id) {
                            case 213:
                                isBuaTriTue = true;
                                break;

                            case 214:
                                isBuaManhMe = true;
                                break;

                            case 215:
                                isBuaDaTrau = true;
                                break;

                            case 216:
                                isBuaOaiHung = true;
                                break;

                            case 217:
                                isBuaBatTu = true;
                                break;

                            case 218:
                                isBuaDeoDai = true;
                                break;

                            case 219:
                                isBuaThuHut = true;
                                break;

                            case 522:
                                isBuaDeTu = true;
                                break;

                            case 671:
                                isBuaTriTue3 = true;
                                break;

                            case 672:
                                isBuaTriTue4 = true;
                                break;
                        }
                    }
                }
                setBuaBatTu(isBuaBatTu);
                setBuaDaTrau(isBuaDaTrau);
                setBuaDeTu(isBuaDeTu);
                setBuaManhMe(isBuaManhMe);
                setBuaDeoDai(isBuaDeoDai);
                setBuaOaiHung(isBuaOaiHung);
                setBuaThuHut(isBuaThuHut || this instanceof VirtualBot);
                setBuaTriTue(isBuaTriTue);
                setBuaTriTue3(isBuaTriTue3);
                setBuaTriTue4(isBuaTriTue4);
                if (listRemove.size() > 0) {
                    amulets.removeAll(listRemove);
                }
            }
        }
    }

    public Amulet getAmulet(int id) {
        for (Amulet amulet : amulets) {
            if (amulet.id == id) {
                return amulet;
            }
        }
        return null;
    }

    private void setListMap() {
        ArrayList<KeyValue> list = new ArrayList<>();
        int mapID = zone.map.mapID;
        if (currMap != null) {
            list.add(currMap);
        }
        if (mapID != MapName.NHA_GOHAN && mapID != MapName.NHA_MOORI && mapID != MapName.NHA_BROLY) {
            if (gender == 0) {
                if (checkCanEnter(MapName.NHA_GOHAN)) {
                    list.add(new KeyValue(MapName.NHA_GOHAN, "Về nhà", "Trái đất"));
                }
            }
            if (gender == 1) {
                if (checkCanEnter(MapName.NHA_MOORI)) {
                    list.add(new KeyValue(MapName.NHA_MOORI, "Về nhà", "Namếc"));
                }
            }
            if (gender == 2) {
                if (checkCanEnter(MapName.NHA_BROLY)) {
                    list.add(new KeyValue(MapName.NHA_BROLY, "Về nhà", "Xay da"));
                }
            }
        }
        if (mapID != MapName.RUNG_KARIN) {
            if (checkCanEnter(MapName.RUNG_KARIN)) {
                list.add(new KeyValue(MapName.RUNG_KARIN, "Rừng Karin", "Trái đất"));
            }
        }
        if (mapID != MapName.LANG_ARU) {
            if (checkCanEnter(MapName.LANG_ARU)) {
                list.add(new KeyValue(MapName.LANG_ARU, "Làng Aru", "Trái đất"));
            }
        }
        if (mapID != MapName.LANG_MORI) {
            if (checkCanEnter(MapName.LANG_MORI)) {
                list.add(new KeyValue(MapName.LANG_MORI, "Làng Mori", "Namếc"));
            }
        }
        if (mapID != MapName.LANG_KAKAROT) {
            if (checkCanEnter(MapName.LANG_KAKAROT)) {
                list.add(new KeyValue(MapName.LANG_KAKAROT, "Làng Kakarot", "Xay da"));
            }
        }

        if (mapID != MapName.DAO_KAME) {
            if (checkCanEnter(MapName.DAO_KAME)) {
                list.add(new KeyValue(MapName.DAO_KAME, "Đảo Kamê", "Trái đất"));
            }
        }
        if (mapID != MapName.VACH_NUI_DEN) {
            if (checkCanEnter(MapName.VACH_NUI_DEN)) {
                list.add(new KeyValue(MapName.VACH_NUI_DEN, "Vách núi đen", "Xay da"));
            }
        }
        if (mapID != MapName.DAO_GURU) {
            if (checkCanEnter(MapName.DAO_GURU)) {
                list.add(new KeyValue(MapName.DAO_GURU, "Đảo Guru", "Namếc"));
            }
        }
        if (mapID != MapName.TRAM_TAU_VU_TRU && mapID != MapName.TRAM_TAU_VU_TRU_2
                && mapID != MapName.TRAM_TAU_VU_TRU_3) {
            if (gender == 0) {
                if (checkCanEnter(MapName.TRAM_TAU_VU_TRU)) {
                    list.add(new KeyValue(MapName.TRAM_TAU_VU_TRU, "Trạm tàu vũ trụ", "Trái đất"));
                }
            }
            if (gender == 1) {
                if (checkCanEnter(MapName.TRAM_TAU_VU_TRU_2)) {
                    list.add(new KeyValue(MapName.TRAM_TAU_VU_TRU_2, "Trạm tàu vũ trụ", "Namếc"));
                }
            }
            if (gender == 2) {
                if (checkCanEnter(MapName.TRAM_TAU_VU_TRU_3)) {
                    list.add(new KeyValue(MapName.TRAM_TAU_VU_TRU_3, "Trạm tàu vũ trụ", "Xay da"));
                }
            }
        }
        if (mapID != MapName.RUNG_BAMBOO) {
            if (checkCanEnter(MapName.RUNG_BAMBOO)) {
                list.add(new KeyValue(MapName.RUNG_BAMBOO, "Rừng Bamboo", "Trái đất"));
            }
        }
        if (mapID != MapName.THANH_PHO_VEGETA) {
            if (checkCanEnter(MapName.THANH_PHO_VEGETA)) {
                list.add(new KeyValue(MapName.THANH_PHO_VEGETA, "Thành phố Vegeta", "Xay da"));
            }
        }
        if (mapID != MapName.NUI_KHI_DO) {
            if (checkCanEnter(MapName.NUI_KHI_DO)) {
                list.add(new KeyValue(MapName.NUI_KHI_DO, "Núi khỉ đỏ", "Fide"));
            }
        }
        if (mapID != MapName.SIEU_THI) {
            if (checkCanEnter(MapName.SIEU_THI)) {
                list.add(new KeyValue(MapName.SIEU_THI, "Siêu Thị", "Xay da"));
            }
        }
        listMapTransport = list;
        setCommandTransport((byte) 0);
    }

    public void setListAccessMap() {
        listAccessMap.clear();
        listAccessMap.add(21);
        listAccessMap.add(52);
        if (this.isBoss() || this.taskMain == null) {
            return;
        }
        if (gender == 0) {
            listAccessMap.add(21);

        }
        if (gender == 1) {
            listAccessMap.add(22);
        }
        if (gender == 2) {
            listAccessMap.add(23);
        }
        if (taskMain.id >= 1) {
            listAccessMap.add(0);
            listAccessMap.add(7);
            listAccessMap.add(14);
        }
        if (taskMain.id >= 2) {
            listAccessMap.add(1);
            listAccessMap.add(8);
            listAccessMap.add(15);
        }
        if (taskMain.id > 3 || (taskMain.id == 3 && taskMain.index >= 1)) {
            listAccessMap.add(42);
            listAccessMap.add(43);
            listAccessMap.add(44);
        }
        if (taskMain.id >= 6) {
            listAccessMap.add(2);
            listAccessMap.add(9);
            listAccessMap.add(16);

            listAccessMap.add(24);
            listAccessMap.add(25);
            listAccessMap.add(26);
        }
        if (taskMain.id >= 7) {
            listAccessMap.add(3);
            listAccessMap.add(11);
            listAccessMap.add(17);
        }
        if (taskMain.id >= 8) {
            listAccessMap.add(4);
            listAccessMap.add(12);
            listAccessMap.add(18);
        }
        if (taskMain.id > 8 || (taskMain.id == 8 && taskMain.index >= 3)) {
            listAccessMap.add(47);
        }
        if (taskMain.id > 9 || (taskMain.id == 9 && taskMain.index >= 2)) {
            listAccessMap.add(46);
            listAccessMap.add(45);
        }
        if (taskMain.id >= 11) {
            listAccessMap.add(5);
            listAccessMap.add(13);
            listAccessMap.add(20);
        }
        if (taskMain.id >= 13) {
            listAccessMap.add(27);
            listAccessMap.add(28);
            listAccessMap.add(29);

            listAccessMap.add(31);
            listAccessMap.add(32);
            listAccessMap.add(33);

            listAccessMap.add(35);
            listAccessMap.add(36);
            listAccessMap.add(37);
        }
        if (taskMain.id >= 15) {
            listAccessMap.add(30);
            listAccessMap.add(34);
            listAccessMap.add(38);
        }
        if (this.typeTraining >= 2) {
            listAccessMap.add(45);
        }
        if (taskMain.id >= 16) {
            listAccessMap.add(6);
            listAccessMap.add(10);
            listAccessMap.add(19);
        }
        if (taskMain.id >= 16) {
            listAccessMap.add(68);
            listAccessMap.add(69);
            listAccessMap.add(70);
            listAccessMap.add(71);
            listAccessMap.add(72);
            listAccessMap.add(64);
            listAccessMap.add(65);
            listAccessMap.add(84);
        }
        if (taskMain.id >= 17) {
            listAccessMap.add(63);
            listAccessMap.add(66);
            listAccessMap.add(67);
            listAccessMap.add(73);
            listAccessMap.add(74);
            listAccessMap.add(75);
            listAccessMap.add(76);
            listAccessMap.add(77);
        }
        if (taskMain.id >= 17) {
            listAccessMap.add(63);
            listAccessMap.add(66);
            listAccessMap.add(67);
            listAccessMap.add(73);
            listAccessMap.add(74);
            listAccessMap.add(75);
            listAccessMap.add(76);
            listAccessMap.add(77);
        }
        if (taskMain.id >= 19) {
            listAccessMap.add(79);
            listAccessMap.add(81);
            listAccessMap.add(82);
            listAccessMap.add(83);

        }
        if (taskMain.id >= 20) {
            listAccessMap.add(81);
            listAccessMap.add(82);
            listAccessMap.add(83);
            listAccessMap.add(80);
            listAccessMap.add(102);

        }
        if (taskMain.id >= 21) {
            listAccessMap.add(92);
            listAccessMap.add(93);
            listAccessMap.add(94);
            listAccessMap.add(95);
            listAccessMap.add(96);
        }
        if (taskMain.id >= 22) {
            listAccessMap.add(97);
            listAccessMap.add(98);
            listAccessMap.add(99);
            listAccessMap.add(100);
        }
        if (taskMain.id >= 24) {
            listAccessMap.add(103);
            listAccessMap.add(105);
            listAccessMap.add(106);
            listAccessMap.add(107);
            listAccessMap.add(108);
            listAccessMap.add(109);
            listAccessMap.add(110);
        }
        if (info.power < 1500000) {
            listAccessMap.add(111);
        }
    }

    public void setTypeTranning(byte typeTrainning) {
        this.typeTraining = typeTrainning;
        setListAccessMap();
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void expandBag(int numberCell) {
        this.numberCellBag += numberCell;
        Item[] items = new Item[this.numberCellBag];
        int index = 0;
        for (Item item : this.itemBag) {
            items[index] = item;
            index++;
        }
        this.itemBag = items;
        service.setItemBag();
        service.sendThongBao("Hành trang đã được mở rộng thêm " + numberCell + " ô.");
    }

    public void expandBox(int numberCell) {
        this.numberCellBox += numberCell;
        Item[] items = new Item[this.numberCellBox];
        int index = 0;
        for (Item item : this.itemBox) {
            items[index] = item;
            index++;
        }
        this.itemBox = items;
        service.setItemBox();
        service.sendThongBao("Rương đồ đã được mở rộng thêm " + numberCell + " ô.");
    }

    public void friendAction(Message ms) {
        try {
            byte action = ms.reader().readByte();
            int playerId = 0;
            if (ms.reader().available() > 0) {
                playerId = ms.reader().readInt();
            }
            if (playerId < 0) {
                return;
            }
            if (action == 1) {
                Player _player = SessionManager.findChar(playerId);
                if (_player != null && _player != this) {
                    Friend _check = this.friends.stream().filter(f -> f.name.equals(_player.name)).findAny()
                            .orElse(null);
                    if (_check != null) {
                        service.serverMessage2("Đã có trong danh sách bạn bè.");
                        return;
                    }
                    String text = null;
                    if (this.friends.size() >= 5) {
                        text = String.format("Bạn có muốn kết bạn với %s với phí 5 ngọc ?", _player.name);
                    } else {
                        text = String.format("Bạn có muốn kết bạn với %s ?", _player.name);
                    }
                    menus.clear();
                    menus.add(new KeyValue(10000, "Đồng ý", playerId));
                    menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                    service.openUIConfirm(NpcName.CON_MEO, text, getPetAvatar(), menus);
                }
            } else if (action == 0) {
                viewListFriend();
            } else if (action == 2) {
                removeFriend(playerId);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 116");
            logger.error("failed!", ex);
        }
    }

    public short getPetAvatar() {
        if (this.gender == 1) {
            return 536;
        }
        if (this.gender == 2) {
            return 537;
        }
        return 351;
    }

    public void addFriend(int playerId) {
        Player _player = SessionManager.findChar(playerId);
        if (_player != null && _player != this) {
            Friend _check = this.friends.stream().filter(f -> f.name.equals(_player.name)).findAny().orElse(null);
            if (_check != null) {
                return;
            }
            Friend friend = new Friend();
            friend.id = _player.id;
            friend.name = _player.name;
            friend.head = _player.head;
            friend.body = _player.body;
            friend.bag = _player.bag;
            friend.leg = _player.leg;
            friend.power = _player.info.power;
            this.friends.add(friend);
            service.serverMessage2("Kết bạn thành công.");
            chatPlayer(_player, String.format("%s vừa mời kết bạn với %s", this.name, _player.name));
        }
    }

    public void addEnemy(Player _player) {
        Friend _check = enemies.stream().filter(f -> f.name.equals(_player.name)).findAny().orElse(null);
        if (_check != null) {
            return;
        }
        Friend friend = new Friend();
        friend.id = _player.id;
        friend.name = _player.name;
        friend.head = _player.head;
        friend.body = _player.body;
        friend.bag = _player.bag;
        friend.leg = _player.leg;
        friend.power = _player.info.power;
        this.enemies.add(friend);
    }

    public void removeEnemy(Player _player) {
        enemies.removeIf(f -> f.id == _player.id);
    }

    public void throwItem(Item item, byte type) {
        if (info.power < 1500000) {
            service.sendThongBao("Bạn chưa đủ sức mạnh để vứt vật phẩm vui lòng thử lại sau");
            return;
        }
        if (item.id == 457) {
            service.sendThongBao("Không thể bỏ vật phẩm này");
            return;
        }
        Item[] items;
        if (type == 0) {
            items = itemBody;
        } else {
            items = itemBag;
        }
        item.lock.lock();
        try {
            int index = item.indexUI;
            if (items[index] == null) {
                return;
            }

            History history = new History(this.id, History.THROW_ITEM);
            history.setBefores(gold, diamond, diamondLock);
            history.setAfters(gold, diamond, diamondLock);
            history.addItem(item);
            history.setZone(zone);
            history.setExtras("Bỏ ra mất luôn");
            history.save();
            if (type == 0) {
                service.setItemBody();
                updateSkin();
                info.setInfo();
                service.loadPoint();
                zone.service.playerLoadBody(this);
                zone.service.updateBody((byte) 0, this);
            } else {
                if (item.id == 457) {
                    service.sendThongBao("Không thể bỏ vật phẩm này");
                    return;
                }
                removeItem(index, item.quantity);
            }
            if (items[index] == null) {
                sort(index, true);
            }
        } finally {
            item.lock.unlock();
        }
    }

    public void chatPlayer(Message ms) {
        try {
            int id = ms.reader().readInt();
            String text = ms.reader().readUTF();
            if (text == null || text.equals("")) {
                return;
            }
            if (text.length() > 100) {
                return;
            }
            text = text.replaceAll("\n", " ");
            Player _player = SessionManager.findChar(id);
            if (_player != null) {

                chatPlayer(_player, text);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 115");
            logger.error("failed!", ex);
        }
    }

    public void chatPlayer(Player _player, String text) {
        try {
            if (text != null && !text.equals("")) {
                if (text.length() > 100) {
                    return;
                }
                Message mss = new Message(Cmd.CHAT_THEGIOI_SERVER);
                FastDataOutputStream ds = mss.writer();
                ds.writeUTF(this.name);
                ds.writeUTF("|5|" + text);
                ds.writeInt(this.id);
                ds.writeShort(this.head);
                ds.writeShort(this.body);
                ds.writeShort(this.bag);
                ds.writeShort(this.leg);
                ds.writeByte(1);
                ds.flush();
                _player.service.sendMessage(mss);
                service.sendMessage(mss);
                mss.cleanup();
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 113");
            logger.error("failed!", ex);
        }
    }

    public void sendChatGlobalFromAdmin() {
        try {
            Message mss = new Message(Cmd.CHAT_THEGIOI_SERVER);
            FastDataOutputStream ds = mss.writer();
            ds.writeUTF("Admin");
            ds.writeUTF(String.format(
                    "|5|Pháo hoa đang được nổ tại Quảng trường Pháo Hoa với số lượng %d, sẽ phát nổ toàn server sau %d quả. Hãy nhanh chân di chuyển tới khu vực bắn pháo để nhận những phần quà giá trị.",
                    SessionManager.countPhaoHoa, 100 - SessionManager.countPhaoHoa));
            ds.writeInt(-1);
            ds.writeShort(861);
            ds.writeShort(862);
            ds.writeShort(102);
            ds.writeShort(863);
            ds.writeByte(0);
            ds.flush();
            SessionManager.sendMessage(mss);
            mss.cleanup();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 112");
            logger.error("failed!", ex);
        }
    }

    public void removeFriend(int id) {
        try {
            if (this.friends.size() == 0) {
                return;
            }
            int index = 0;
            for (Friend friend : this.friends) {
                if (friend.id == id) {
                    this.friends.remove(index);
                    Message ms = new Message(Cmd.FRIEND);
                    FastDataOutputStream ds = ms.writer();
                    ds.writeByte(2);
                    ds.writeInt(id);
                    ds.flush();
                    service.sendMessage(ms);
                    ms.cleanup();
                    service.sendThongBao("Đã xóa thành công " + friend.name + " ra khỏi danh sách bạn bè.");
                    return;
                }
                index++;
            }

        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 111");
            logger.error("failed!", ex);
        }
    }

    public void enemyAction(Message ms) {
        try {
            byte action = ms.reader().readByte();
            if (action == 1 || action == 2) {
                int playerID = ms.reader().readInt();
                Friend f = enemies.stream().filter(a -> a.id == playerID).findAny().orElse(null);
                if (f != null) {
                    if (action == 2) {
                        enemies.remove(f);
                    }
                    if (action == 1) {
                        Player _c = SessionManager.findChar(playerID);
                        if (_c != null && _c != this) {
                            if (_c.zone != null) {
                                TMap map = _c.zone.map;
                                int mapID = map.mapID;
                                if (_c.isAnDanh || !checkCanEnter(mapID) || map.isUnableToTeleport() || isDead
                                        || _c.isDead) {
                                    service.sendThongBao("Chưa thể đến lúc này, vui lòng thử lại sau ít phút");
                                } else {
                                    long now = System.currentTimeMillis();
                                    if (now - lastTimeRevenge < 300000) {
                                        revenge(_c);
                                    } else {
                                        menus.clear();
                                        menus.add(new KeyValue(555, "OK", playerID));
                                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                                        service.openUIConfirm(NpcName.CON_MEO,
                                                "Bạn muốn đến ngay chỗ hắn, phí là 1 ngọc và được tìm thoải mái trong 5 phút nhé",
                                                getPetAvatar(), menus);
                                    }
                                }
                            }
                        } else {
                            service.sendThongBao("Đang offline");
                        }
                    }
                }
            }
            if (action == 0 || action == 2) {
                service.enemyAction((byte) 0);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 110");
            logger.error("failed!", ex);
        }
    }

    public void revenge(Player c) {
        // if (escortedPerson != null) {
        // service.sendThongBao(String.format("Bạn đang hộ tống %s, không thể thực
        // hiện.", escortedPerson.name));
        // return;
        // }
        lastTimeRevenge = System.currentTimeMillis();
        subDiamond(1);
        this.x = calculateX(c.zone.map);
        this.y = c.zone.map.collisionLand(x, (short) 24);
        zone.leave(this);
        c.zone.enter(this);
        Utils.setTimeout(() -> {
            if (zone != null && c != null && !isDead && !c.isDead && zone == c.zone) {
                zone.service.chat(this, "Mau đền tội");
                c.service.sendThongBao("Có người đang đến tìm bạn để trả thù");
                setCommandPK(CMDPk.TRA_THU);
                c.setCommandPK(CMDPk.TRA_THU);
                testCharId = c.id;
                c.testCharId = this.id;
                setTypePK((byte) 3);
                c.setTypePK((byte) 3);
            }
        }, 3000);

    }

    public void viewListFriend() {
        try {
            Message ms = new Message(Cmd.FRIEND);
            FastDataOutputStream ds = ms.writer();
            ds.writeByte(0);
            ds.writeByte(this.friends.size());
            for (Friend friend : this.friends) {
                Player _player = SessionManager.findChar(friend.id);
                if (_player != null) {
                    friend.head = _player.head;
                    friend.body = _player.body;
                    friend.bag = _player.bag;
                    friend.leg = _player.leg;
                    friend.power = _player.info.power;
                    friend.isOnline = true;
                } else {
                    friend.isOnline = false;
                }
                ds.writeInt(friend.id);
                ds.writeShort(friend.head);
                ds.writeShort(friend.body);
                ds.writeShort(friend.leg);
                ds.writeByte(friend.bag);
                ds.writeUTF(friend.name);
                ds.writeBoolean(friend.isOnline);
                ds.writeUTF(Utils.formatNumber(friend.power));
            }
            ds.flush();
            service.sendMessage(ms);
            ms.cleanup();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 109");
            logger.error("failed!", ex);
        }
    }

    public void mapOffline() {
        if (this.isDead) {
            return;
        }
        int mapId = 0;
        short x = 0;
        short y = 0;
        if ((this.gender == 0 && zone.map.mapID == 0) || (this.gender == 1 && zone.map.mapID == 7)
                || (this.gender == 2 && zone.map.mapID == 14)) {
            switch (this.gender) {
                case 0:
                    mapId = 21;
                    x = 456;
                    y = 336;
                    break;

                case 1:
                    mapId = 22;
                    x = 168;
                    y = 336;
                    break;

                case 2:
                    mapId = 23;
                    x = 432;
                    y = 336;
                    break;
            }
        }
        if (zone.map.mapID == 21) {
            mapId = 0;
            x = 288;
            y = 432;
        }
        if (zone.map.mapID == 22) {
            mapId = 7;
            x = 384;
            y = 432;
        }
        if (zone.map.mapID == 23) {
            mapId = 14;
            x = 540;
            y = 408;
        }

        if (zone.map.mapID == 39) {
            mapId = 21;
            x = 100;
            y = 336;
        }
        if (zone.map.mapID == 40) {
            mapId = 22;
            x = 100;
            y = 336;
        }
        if (zone.map.mapID == 41) {
            mapId = 23;
            x = 100;
            y = 336;
        }
        TMap map = MapManager.getInstance().getMap(mapId);
        if ((itemLoot != null && itemLoot.isDragonBallNamec()) || idNRNM != -1) {
            if (map.mapID == 21 || map.mapID == 22 || map.mapID == 23 || map.mapID == 24 || map.mapID == 25
                    || map.mapID == 26) {
                service.serverMessage("Không thực hiện được");
                return;
            }
        }
        this.zone.leave(this);
        this.x = x;
        this.y = y;
        int zoneId = 0;

        if (!map.isMapSingle()) {
            zoneId = map.getZoneID();
            map.enterZone(this, zoneId);
        } else {
            enterMapSingle(map);
        }
    }

    public void requestChangeMap() {
        lock.lock();
        try {
            if (this.isDead) {
                return;
            }
            Waypoint way = zone.map.findWaypoint(this.x, this.y);
            if (way != null) {
                int mapID = way.next;
                boolean flag = false;
                if (zone.map.isBarrack() || zone.map.isTreasure()) {
                    List<Mob> list2 = zone.getListMob();
                    for (Mob mob : list2) {
                        if (!mob.isDead()) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        List<Player> list = zone.getListChar(Zone.TYPE_BOSS);
                        flag = !list.isEmpty();
                    }
                    TMap map = MapManager.getInstance().getMap(mapID);
                    if (flag) {
                        way = map.getWaypointByNextID(zone.map.mapID);
                        if (way != null) {
                            this.x = way.x;
                            this.y = way.y;
                        } else {
                            this.x = this.preX;
                            this.y = this.preY;
                        }
                        service.resetPoint();
                        service.sendThongBao("Chưa hạ hết đối thủ");
                    } else {
                        this.x = way.x;
                        this.y = way.y;
                        if (map.isBarrack() || map.isTreasure()) {
                            if (clan != null) {
                                if (map.isBarrack()) {
                                    zone.leave(this);
                                    clan.barrack.enterMap(mapID, this);
                                } else if (map.isTreasure()) {
                                    zone.leave(this);
                                    clan.treasure.enterMap(mapID, this);
                                }
                            } else {
                                goHome();
                            }
                        } else {
                            zone.leave(this);
                            int zoneId = map.getZoneID();
                            map.enterZone(this, zoneId);
                        }
                    }
                } else {
                    TMap map = MapManager.getInstance().getMap(mapID);
                    long now = System.currentTimeMillis();
                    int timeWaitWithDragonBallNamec = (int) ((now - lastTimePickUpDragonBallNamec) / 1000);
                    if ((itemLoot != null && itemLoot.isDragonBallNamec() || idNRNM != -1)) {
                        // map nha va tram tau vu tru
                        if (map.mapID == 21 || map.mapID == 22 || map.mapID == 23 || map.mapID == 24 || map.mapID == 25
                                || map.mapID == 26) {
                            service.serverMessage("Không thực hiện được");
                            return;
                        }
                        if (timeWaitWithDragonBallNamec < 60) {
                            this.x = this.preX;
                            this.y = this.preY;
                            service.resetPoint();
                            service.serverMessage("Chưa thể chuyển khu vực lúc này vui lòng chờ "
                                    + (60 - timeWaitWithDragonBallNamec) + "giây nữa");
                            return;
                        }
                    }

                    lastTimePickUpDragonBallNamec = System.currentTimeMillis();
                    if (checkCanEnter(mapID)) {
                        zone.leave(this);
                        this.x = way.x;
                        this.y = way.y;
                        if (map.isMapSingle()) {
                            enterMapSingle(map);
                        } else {
                            int zoneId = map.getZoneID();
                            map.enterZone(this, zoneId);
                        }
                    } else {
                        way = map.getWaypointByNextID(zone.map.mapID);
                        if (way != null) {
                            this.x = way.x;
                            this.y = way.y;
                        } else {
                            this.x = this.preX;
                            this.y = this.preY;
                        }
                        service.resetPoint();
                        service.sendThongBao("Bạn chưa thể đến khu vực này");
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean checkCanEnter(int mapID) {

        if (mapID >= 156 && mapID <= 158) {
            return info.power > 40_000_000_000L && this.session.user.getActivated() == 1;
        }
        if (mapID >= 168 && mapID <= 180) {
            return true;
        }
        if (mapID >= 105 && mapID <= 110) {
            return taskMain.id >= 26 || this instanceof BotCold;
        }
        if (taskMain.id >= 23) {
            return true;
        }
        if (info.power > 110000000000L) {
            return true;
        }

        for (int map : listAccessMap) {
            if (map == mapID || mapID == 52) {
                return true;
            }
        }
        TMap map = MapManager.getInstance().getMap(mapID);
        if (map.isMapSpecial()) {
            return true;
        }
        return false;
    }

    public void changeOnSkill(Message ms) {
        try {
            if (isDead) {
                return;
            }
            int len = ms.reader().available();
            for (int i = 0; i < len; i++) {
                byte b = ms.reader().readByte();
                this.shortcut[i] = b;
            }
            // service.CHANGE_ONSKILL(this.shortcut);
            // sendSkillShortCut();
            service.changeOnSkill(shortcut);
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 108");
            logger.error("failed!", ex);
        }
    }

    public Skill getSkill(int id) {
        for (Skill skill : skills) {
            if (skill.template.id == id) {
                return skill;
            }
        }
        return null;
    }

    public void setDefaultLeg() {
        if (this.gender == 0) {
            this.leg = 58;
        } else if (this.gender == 1) {
            this.leg = 60;
        } else if (this.gender == 2) {
            this.leg = 58;
        }
    }

    public void setDefaultBody() {
        if (this.gender == 0) {
            this.body = 57;
        } else if (this.gender == 1) {
            this.body = 59;
        } else if (this.gender == 2) {
            this.body = 57;
        }
    }

    public void setDefaultHead() {
        this.head = this.headDefault;
    }

    public void setDefaultPart() {
        setDefaultHead();
        setDefaultBody();
        setDefaultLeg();
    }

    public void updateSkin() {
        this.bag = -1;
        if (taskMain != null && taskMain.id == 3 && taskMain.index == 2) {
            this.bag = ClanImageName.DUA_BE_51;
        }
        updateBag();
        setDefaultPart();
        if (itemBody != null) {
            if (itemBody[0] != null) {
                this.body = itemBody[0].template.part;
            }
            if (itemBody[1] != null) {
                this.leg = itemBody[1].template.part;
            }
        }
        if (this.isStone) {
            this.head = 454;
            this.body = 455;
            this.leg = 456;
        } else if (this.isChocolate) {
            this.head = 412;
            this.body = 413;
            this.leg = 414;
        } else if (isNhapThe && !isMonkey) {
            this.isMask = true;
            if (itemBody != null && itemBody[5] != null && itemBody[5].template.part == -1 && itemBody[5].isNhapThe) {
                ItemTemplate template = itemBody[5].template;
                this.head = template.head;
                this.body = template.body;
                this.leg = template.leg;
            } else {
                if (typePorata == 0) {
                    if (gender == 1) {
                        this.head = 391;
                        this.body = 392;
                        this.leg = 393;
                    } else {
                        if (fusionType == 4) {
                            this.head = 380;
                            this.body = 381;
                            this.leg = 382;
                        } else if (fusionType == 6) {
                            this.head = 383;
                            this.body = 384;
                            this.leg = 385;
                        }
                    }
                } else if (typePorata == 1) {
                    switch (gender) {
                        case 0:
                            this.head = 870;
                            this.body = 871;
                            this.leg = 872;
                            break;

                        case 1:
                            this.head = 873;
                            this.body = 874;
                            this.leg = 875;
                            break;

                        case 2:
                            this.head = 867;
                            this.body = 868;
                            this.leg = 869;
                            break;
                    }
                }
            }
        } else if (!this.isMonkey) {
            if (itemBody != null && itemBody[5] != null && !itemBody[5].isNhapThe) {
                this.head = itemBody[5].template.part;
                if (this.head == -1) {
                    this.isMask = true;
                    ItemTemplate template = itemBody[5].template;
                    this.head = template.head;
                    this.body = template.body;
                    this.leg = template.leg;
                }
            } else {
                this.isMask = false;
            }
        } else {
            Skill skill = getSkill(13);
            if (skill != null) {
                this.isMask = true;
                this.body = 193;
                this.leg = 194;
                switch (skill.point) {
                    case 1:
                        this.head = 192;
                        break;
                    case 2:
                        this.head = 195;
                        break;
                    case 3:
                        this.head = 196;
                        break;
                    case 4:
                        this.head = 199;
                        break;
                    case 5:
                        this.head = 197;
                        break;
                    case 6:
                        this.head = 200;
                        break;
                    case 7:
                        this.head = 198;
                        break;
                }
            }
        }
    }

    public void setMount() {
        this.isHaveMount = false;
        this.idMount = -1;
        for (Item item : this.itemBody) {
            if (item != null) {
                if (item.template.type == 23 || item.template.type == 24) {
                    this.isHaveMount = true;
                    this.idMount = item.template.mountID;
                    return;
                }
            }
        }
    }

    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    private static final String[] NAMES = {
        // 2-3 ký tự
        "an", "ha", "ly", "my", "tu", "vy", "lan", "nam", "son", "tan", "thi", "thu", "van",
        // 3-4 ký tự
        "anh", "dao", "duc", "hai", "hoa", "hon", "huy", "kim", "lan", "linh", "mai", "minh",
        "nam", "nga", "nhu", "tam", "tan", "thu", "tuan", "tung", "viet", "xuan"
    };

    public static String generateCharacterName() {
        if (Utils.nextInt(10) <= 3) {
            return generateName();
        }
        if (Utils.nextInt(10) % 2 == 0) {
            return generateRandomString(Utils.nextInt(5, 10));
        }
        StringBuilder name;
        do {
            name = new StringBuilder();

            // Chọn 2 phần ngẫu nhiên để ghép thành tên
            name.append(NAMES[Utils.nextInt(NAMES.length)]);
            name.append(NAMES[Utils.nextInt(NAMES.length)]);

            // 30% cơ hội thêm số nếu tên vẫn còn ngắn
            if (name.length() < 8 && Utils.nextInt(100) < 30) {
                name.append(Utils.nextInt(99) + 1); // Thêm số từ 1-99
            }

        } while (name.length() < 5 || name.length() > 10);

        return name.toString();
    }

    private static final String[] SYLLABLES = {
        "anh", "binh", "chi", "dung", "giang", "hai", "hieu", "khoa",
        "lam", "long", "mai", "minh", "nam", "ngoc", "nhat", "phong",
        "quang", "son", "tien", "trung", "viet", "yen"
    };

    public static String generateName() {
        int nameLength = Utils.nextInt(6) + 5; // Độ dài tên từ 5 đến 10 ký tự
        StringBuilder name = new StringBuilder();

        while (name.length() < nameLength) {
            // Lấy ngẫu nhiên một âm tiết từ danh sách
            String syllable = SYLLABLES[Utils.nextInt(SYLLABLES.length)];
            name.append(syllable);

            // Nếu độ dài vượt quá, cắt bớt
            if (name.length() > nameLength) {
                name.setLength(nameLength);
            }
        }

        // Ngẫu nhiên thêm số ở cuối tên (50% khả năng)
        if (Utils.nextInt(10) % 2 == 0) {
            int randomNumber = Utils.nextInt(100); // Số từ 0 đến 99
            name.append(randomNumber);
        }

        return name.toString();
    }

    public void chatMap(Message ms) {
        try {
            String text = ms.reader().readUTF();
            if (!text.isEmpty()) {
                if (this.getSession().user.getRole() == 1) {

                    if (text.startsWith("xoabot")) {
                        synchronized (zone.getPlayers()) {
                            for (Player pl : new ArrayList<>(zone.getPlayers())) {
                                if (pl instanceof VirtualBot || pl instanceof BotCold) {
                                    pl.zone.leave(pl);
                                    pl.close();
                                }
                            }
                        }
                        return;
                    }

                    if (text.startsWith("vutall")) {
                        for (int i = itemBag.length - 1; i >= 0; i--) {
                            Item item = itemBag[i];
                            if (item != null) {
                                this.removeItem(item.indexUI, item.quantity);
                            }
                        }
                        return;
                    }

                    if (text.startsWith("vuthsd")) {
                        for (int i = itemBag.length - 1; i >= 0; i--) {
                            Item item = itemBag[i];
                            if (item != null && item.findOptions(93) != -1) {
                                this.removeItem(item.indexUI, item.quantity);
                            }
                        }
                        return;
                    }

                    if (text.startsWith("taydu")) {
                        TayDuManager taydu = new TayDuManager();
                        taydu.spawnTeam();
                        return;
                    }

                    if (text.startsWith("haitac")) {
                        HaiTacManager haitac = new HaiTacManager();
                        haitac.spawnTeam();
                        return;
                    }

                    if (text.startsWith("bc")) {
                        UtilsNQMP.createBotCold(1, this.zone);
                        return;
                    }
                    if (text.startsWith("botss ")) {
                        int quantity = Integer.parseInt(text.replace("botss ", ""));
                        for (int i = 0; i < quantity; i++) {
                            VirtualBot_SoSinh bot = new VirtualBot_SoSinh(generateCharacterName());
                            bot.setLocation(0, -1);
                        }
                        return;
                    }
                    if (text.startsWith("createbot")) {
                        int quantity = Integer.parseInt(text.replace("createbot ", ""));
                        UtilsNQMP.createBot(quantity, this.zone.map.mapID);
                        return;
                    }
                    if (text.startsWith("m ")) {
                        int mapid = Integer.parseInt(text.replace("m ", ""));
                        if (MapManager.getInstance().getMap(mapid) != null) {
                            this.zone.leave(this);
                            TMap map = MapManager.getInstance().getMap(mapid);
                            map.enterZone(this, 0);
                            this.x = 100;
                            this.y = 26;
                            zone.service.setPosition(this, (byte) 0);
                        } else {
                            service.sendThongBao("Map không tồn tại");
                        }
                        return;
                    } else if (text.startsWith("m")) {
                        int mapid = Integer.parseInt(text.replace("m", ""));
                        if (MapManager.getInstance().getMap(mapid) != null) {
                            this.zone.leave(this);
                            TMap map = MapManager.getInstance().getMap(mapid);
                            map.enterZone(this, 0);
                            this.x = 100;
                            this.y = 26;
                            zone.service.setPosition(this, (byte) 0);
                        } else {
                            service.sendThongBao("Map không tồn tại");
                        }
                        return;
                    }
                    if (text.equals("cmd")) {
                        inputDlg = new InputDialog(CMDTextBox.BUFF_ITEM, "Nhập Thông Tin Item Cần Buff",
                                new TextField("ID Item", TextField.INPUT_TYPE_ANY),
                                new TextField("Số Lượng", TextField.INPUT_TYPE_ANY));
                        inputDlg.setService(service);
                        inputDlg.show();
                        return;
                    }
                    if (text.equals("droprate")) {
                        inputDlg = new InputDialog(CMDTextBox.DROP_RATE, "Nhập tỉ lệ rơi đồ",
                                new TextField("Mob", TextField.INPUT_TYPE_NUMERIC),
                                new TextField("Boss", TextField.INPUT_TYPE_NUMERIC));
                        inputDlg.setService(service);
                        inputDlg.show();
                    }
                    if (text.equals("test")) {
                        sendChatGlobalFromAdmin();
                    }
                    if (text.equals("ccu")) {
                        service.dialogMessage(
                                "Tổng CCU: " + SessionManager.getCountPlayer() + " người chơi đang online");
                        return;
                    }

                }
                if (text.length() > 100) {
                    return;
                }
                if (myDisciple != null && !isNhapThe) {
                    String tmp = Utils.unaccent(text);
                    if (tmp.equalsIgnoreCase("di theo") || tmp.equalsIgnoreCase("follow")) {
                        petStatus((byte) 0);
                    }
                    if (tmp.equalsIgnoreCase("bao ve") || text.equalsIgnoreCase("protect")) {
                        petStatus((byte) 1);
                    }
                    if (tmp.equalsIgnoreCase("tan cong") || tmp.equalsIgnoreCase("attack")) {
                        petStatus((byte) 2);
                    }
                    if (tmp.equalsIgnoreCase("ve nha") || tmp.equalsIgnoreCase("go home")) {
                        petStatus((byte) 3);
                    }
                    String words = "ten con la ";
                    if (tmp.startsWith(words)) {
                        String name = text.substring(words.length()).trim();
                        if (StringUtils.isBlank(tmp)) {
                            return;
                        }
                        int length = name.length();
                        if (length >= 5 && length <= 15) {
                            int index = getIndexBagById(400);
                            if (index == -1) {
                                return;
                            }
                            removeItem(index, 1);
                            myDisciple.name = name;
                            zone.service.playerLoadAll(myDisciple);
                            service.chat(myDisciple, "Cảm ơn sư phụ, tên con từ nay sẽ là " + name);
                        } else {
                            service.sendThongBao("Tên không hợp lệ!");
                        }
                        return;
                    }

                }
                zone.service.chat(this, text);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 107");
            logger.error("failed!", ex);
        }

    }

    public void chatGlobal(Message ms) {
        if (this.session.user.getActivated() == 0) {
            service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
            return;
        }
        try {
            long now = System.currentTimeMillis();
            long time = now - lastTimeChatGlobal;
            if (time < 60000) {
                service.sendThongBao(String.format("Vu lòng thử lại sau %d giây", 60 - (time / 1000)));
                return;
            }
            lastTimeChatGlobal = now;
            String text = ms.reader().readUTF();
            if (!text.isEmpty()) {
                if (text.length() > 100) {
                    return;
                }
                text = text.replaceAll("\n", " ");
                if (gold < 200000000) {
                    service.sendThongBao("Bạn không đủ vàng để thực hiện");
                    return;
                }
                subGold(200000000);
                Message mss = new Message(Cmd.CHAT_THEGIOI_SERVER);
                FastDataOutputStream ds = mss.writer();
                ds.writeUTF(this.name);
                ds.writeUTF("|5|" + text);
                ds.writeInt(this.id);
                ds.writeShort(this.head);
                ds.writeShort(this.body);
                ds.writeShort(this.bag);
                ds.writeShort(this.leg);
                ds.writeByte(0);
                ds.flush();
                SessionManager.sendMessage(mss);
                mss.cleanup();
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 106");
            logger.error("failed!", ex);
        }

    }

    public boolean meCanMove() {
        return !isDead && !isBlind && !isFreeze && !isSleep && !isCharge && !isStone
                && !(hold != null && hold.detainee == this);
    }

    public void taskNext() {
        taskMain.index++;
        taskMain.count = 0;
        service.taskNext();
        taskMain.lastTask = System.currentTimeMillis();
        // String subName = taskMain.subNames[taskMain.index];
        // service.sendThongBao(subName);
    }

    public void updateTask(int taskID) {
        if (taskMain != null) {
            int power = taskMain.rewardPower;
            int potential = taskMain.rewardPotential;
            int gold = taskMain.rewardGold;
            int gem = taskMain.rewardGem;
            int gemLock = taskMain.rewardGemLock;
            if (power > 0) {
                addExp(Info.POWER, power, false, false);
                service.sendThongBao(String.format("Bạn được thưởng %d sức mạnh", power));
            }
            if (potential > 0) {
                addExp(Info.POTENTIAL, potential, false, false);
            }
            if (gold > 0) {
                addGold(gold);
            }
            if (gem > 0) {
                addDiamond(gem);
            }
            if (gemLock > 0) {
                addDiamondLock(gem);
            }
        }
        Task task = new Task();
        task.id = taskID;
        task.count = 0;
        task.index = 0;
        task.initTask(this.gender);
        taskMain = task;
        setListAccessMap();
        service.setTask();
        taskMain.lastTask = System.currentTimeMillis();
    }

    public void updateTaskCount(int add) {
        taskMain.count += add;
        switch (taskMain.id) {
            case 1:
                service.sendThongBao(String.format("Bạn đánh được %d/%d mộc nhân", taskMain.count, 5));
                break;

            case 2:
                service.sendThongBao(String.format("Bạn nhặt được %d/%d Đùi gà", taskMain.count, 10));
                break;
        }
        service.updateTaskCount();
        int countMax = taskMain.counts[taskMain.index];
        if (countMax > 0) {
            if (taskMain.count >= countMax) {
                taskNext();
            }
        } else {
            taskNext();
        }
        taskMain.lastTask = System.currentTimeMillis();
    }

    public void move(Message ms) {
        try {
            tickMove++;
            if (tickMove > 105) {
                numCheck++;
            }
            if (tickMove == 1) {
                lastTick = System.currentTimeMillis();
            }
            if (this.isRecoveryEnergy) {
                stopRecoveryEnery();
            }
            if (!meCanMove()) {
                return;
            }
            if (taskMain != null) {
                if (taskMain.id == 0 && taskMain.index == 0) {
                    String text = TaskText.TASK_0_0[gender];
                    service.openUISay(NpcName.CON_MEO, text, getPetAvatar());
                    taskNext();
                }
            }
            if (this.hold != null) {
                if (this.hold.holder == this) {
                    this.hold.close();
                } else {
                    return;
                }
            }
            byte type = ms.reader().readByte();// 0 duoi dat, 1 bay
            this.preX = this.x;
            this.preY = this.y;
            this.x = ms.reader().readShort();
            if (ms.reader().available() > 0) {
                this.y = ms.reader().readShort();
            }

            if (this.mobMe != null) {
                this.mobMe.x = this.x;
                this.mobMe.y = (short) (this.y - 40);
            }
            if (type == 0) {
                this.y = zone.map.collisionLand(this.x, this.y);
            } else {
                achievements.get(5).addCount(Utils.getDistance(this.preX, this.preY, this.x, this.y) / 10);// Khinh công
                // thành thạo
                if ((zone.map.tileTypeAtPixel(this.x, this.y)) == TMap.T_EMPTY) {
                    if (!this.isHaveMount) {
                        info.mp -= this.info.originalMP / 100 * (!isMonkey ? 1 : 2);
                        if (this.info.mp < 0) {
                            this.info.mp = 0;
                        }
                    }
                }
            }
            if (myDisciple != null && myDisciple.discipleStatus == 0) {
                myDisciple.move();
            }
            if (miniDisciple != null) {
                miniDisciple.move();
            }
            // if (escortedPerson != null) {
            // escortedPerson.move();
            // }
            this.zone.service.move(this);
            this.lastTimeMove = System.currentTimeMillis();
            if ((this.x <= 24 || this.x >= this.zone.map.width - 24 || this.y < 0
                    || this.y >= this.zone.map.height - 24) && zone.map.findWaypoint(x, y) == null) {
                this.x = this.preX;
                this.y = this.preY;
                zone.service.setPosition(this, (byte) 0);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 105");
            logger.error("failed!", ex);
        }
    }

    public void checkMove(Message ms) {
        try {
            long now = System.currentTimeMillis();
            int s = (int) (now - lastTick) / 1000;
            int seconds = ms.reader().readInt();
            int m = 100 / Math.max(1, info.speed / 2);
            if (tickMove != 100 || Math.abs(seconds - s) > 2 || s < m) {
                numCheck++;
            }
            tickMove = 0;
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 104");
            logger.error("check move", ex);
        }
    }

    public void requestPean() {
        if (isAutoPlay && !magicTree.isUpgrade) {
            boolean flag2 = false;
            for (int j = 0; j < this.itemBag.length; j++) {
                Item item = this.itemBag[j];
                if (item != null && item.template.type == 6) {
                    flag2 = true;
                    break;
                }
            }
            if (!flag2 && magicTree != null) {
                magicTree.harvest(this);
            }
        }
    }

    public void discipleInfo() {
        if (myDisciple != null) {
            service.petInfo((byte) 2);
        }
    }

    public void wakeUpFromDead() {
        if (!this.isDead) {
            return;
        }
        if (zone != null) {
            if (zone.map.isDauTruong()) {
                service.sendThongBao("Không thể thực hiện");
                return;
            }
        }
        if (this.diamond + this.diamondLock <= 0 && !zone.map.isBlackDragonBall()) {
            this.service.sendThongBao("Không đủ ngọc để thực hiện.");
            return;
        }
        if (this.gold < 200_000_000 && zone.map.isBlackDragonBall()) {
            this.service.sendThongBao("Hồi sinh trong đây cần 100tr vàng");
            return;
        }
        if (!zone.map.isBlackDragonBall()) {
            subDiamond(1);
        } else {
            subGold(100000000);
        }
        achievements.get(13).addCount(1);// thánh hồi sinh
        this.statusMe = 1;
        this.info.hp = this.info.hpFull;
        this.info.mp = this.info.mpFull;
        this.isDead = false;
        service.sendMessage(new Message(Cmd.ME_LIVE));
        if (zone != null) {
            zone.service.playerLoadLive(this);
        }
    }

    public void healFull() {
        this.info.hp = this.info.hpFull;
        this.info.mp = this.info.mpFull;
        this.isDead = false;
        service.sendMessage(new Message(Cmd.ME_LIVE));
        if (zone != null) {
            zone.service.playerLoadLive(this);
        }
    }

    public void getItem(Message ms) {
        try {
            byte type = ms.reader().readByte();
            byte index = ms.reader().readByte();
            switch (type) {

                case Item.BOX_BAG:
                    itemBoxToBag(index);
                    break;

                case Item.BAG_BODY:
                    itemBagToBody(index);
                    break;

                case Item.BAG_BOX:
                    itemBagToBox(index);
                    break;

                case Item.BODY_BAG:
                    itemBodyToBag(index);
                    break;

                case Item.BODY_BOX:
                    itemBodyToBox(index);
                    break;

                case Item.BOX_BODY:
                    // itemBoxBody(index);
                    break;

                case Item.BAG_PET:
                    itemBagToPet(index);
                    break;

                case Item.PET_BAG:
                    itemPetToBag(index);
                    break;
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 103");
            logger.error("failed!", ex);
        }
    }

    public void loadBody() {
        if (zone != null) {
            this.updateSkin();
            this.info.setInfo();
            service.loadPoint();
            zone.service.playerLoadBody(this);
            zone.service.updateBody((byte) 0, this);
        }
    }

    public void timeOutIsMonkey() {
        setMonkey(false);
        this.timeIsMoneky = 0;
        this.updateSkin();
        this.info.setInfo();
        service.loadPoint();
        if (zone != null) {
            zone.service.playerLoadBody(this);
            zone.service.updateBody((byte) 0, this);
        }
    }

    public void loadEffectSkillOnMob() {
        List<Mob> list2 = zone.getListMob();
        for (Mob mob : list2) {
            if (mob.status == 0) {
                continue;
            }
            if (mob.isBlind) {
                service.setEffect(null, mob.mobId, Skill.ADD_EFFECT, Skill.MONSTER, (byte) 40);
            }
            if (mob.isSleep) {
                service.setEffect(null, mob.mobId, Skill.ADD_EFFECT, Skill.MONSTER, (byte) 41);
            }
            if (mob.isChangeBody) {
                service.changeBodyMob(mob, (byte) 1);
            }
        }
    }

    public void loadEffectSkillPlayer(Player _player) {
        if (_player.isSleep) {
            service.setEffect(null, _player.id, Skill.ADD_EFFECT, Skill.CHARACTER, (byte) 41);
        }
        if (_player.isProtected) {
            service.setEffect(null, _player.id, Skill.ADD_EFFECT, Skill.CHARACTER, (byte) 33);
        }
        if (_player.isBlind) {
            service.setEffect(null, _player.id, Skill.ADD_EFFECT, Skill.CHARACTER, (byte) 40);
        }
        if (_player.isRecoveryEnergy) {
            service.skillNotFocus(_player.id, (short) _player.select.id, (byte) 1, null, null);
        }
        if (_player.isHeld && _player.hold.holder == _player) {
            if (_player.hold.detainee instanceof Mob) {
                Mob mob = (Mob) _player.hold.detainee;
                service.setEffect(_player.hold, mob.mobId, Skill.ADD_EFFECT, Skill.MONSTER, (byte) 32);
            } else {
                Player _c = (Player) _player.hold.detainee;
                service.setEffect(_player.hold, _c.id, Skill.ADD_EFFECT, Skill.CHARACTER, (byte) 32);
            }
        }
        if (_player.isCharge()) {
            switch (_player.select.template.id) {
                case SkillName.QUA_CAU_KENH_KHI:
                case SkillName.MAKANKOSAPPO:
                    service.skillNotFocus(_player.id, (short) _player.select.id, (byte) 4, null, null);
                    break;

                case SkillName.BIEN_HINH:
                    service.skillNotFocus(_player.id, (short) _player.select.id, (byte) 6, null, null);
                    break;

                case SkillName.TU_PHAT_NO:
                    service.skillNotFocus(_player.id, (short) _player.select.id, (byte) 7, null, null);
                    break;
            }
        }
    }

    public void loadEffectFreeze() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Mob> mobs = new ArrayList<>();
        if (zone != null) {
            List<Player> list = zone.getListChar(Zone.TYPE_ALL);
            for (Player _player : list) {
                if (_player != this) {
                    if (_player.isFreeze) {
                        players.add(_player);
                    }
                }
            }
            List<Mob> list2 = zone.getListMob();
            for (Mob mob : list2) {
                if (mob.isFreeze) {
                    mobs.add(mob);
                }
            }
        }
        service.skillNotFocus(this.id, (short) 42, (byte) 0, mobs, players);
    }

    public boolean addItemBag(Item item) {
        return addItem(item);
    }

    public void skillNotFocus(Message ms) {
        try {
            byte status = ms.reader().readByte();
            skillNotFocus(status);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 102");
        }
    }

    public void skillNotFocus(byte type) {
        if (!isRecoveryEnergy) {
            if (!meCanAttack()) {
                return;
            }
        } else {
            if (isDead) {
                return;
            }
        }
        long now = System.currentTimeMillis();
        if (type == 2) {
            if (this.isRecoveryEnergy) {
                if (this.info.hp >= this.info.hpFull && this.info.mp >= this.info.mpFull) {
                    stopRecoveryEnery();
                } else {
                    if (now - this.lastUpdates[TAI_TAO] >= 1000) {
                        this.lastUpdates[TAI_TAO] = now;
                        this.info.recovery(Info.ALL, this.select == null ? 10 : this.select.damage, true);
                        this.isCritFirstHit = true;
                    }
                }
                long percent = (long) this.info.hp * 100L / (long) this.info.hpFull;
                if (this.info.mp >= this.info.mpFull) {
                    if (percent >= 100) {
                        stopRecoveryEnery();
                    }
                }
                zone.service.chat(this, "Phục hồi năng lượng " + percent + "%");
            }
            return;
        }
        if (type == 3) {
            stopRecoveryEnery();
            return;
        }
        if (this.select.template.type != 3 && this.select.template.id != SkillName.QUA_CAU_KENH_KHI
                && this.select.template.id != SkillName.MAKANKOSAPPO) {
            return;
        }
        if (this.select.isCooldown()) {
            return;
        }
        if (this.isRecoveryEnergy) {
            stopRecoveryEnery();
        }
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Mob> mobs = new ArrayList<>();
        long manaUse = this.select.manaUse;
        if (this.select.template.manaUseType == 1) {
            manaUse = Utils.percentOf(this.info.mpFull, manaUse);
        }
        if (isBoss()) {
            manaUse = 0;
        }
        if (this.info.mp < manaUse) {
            service.sendThongBao("Không đủ KI đế sử dụng");
            return;
        }
        if (type == 1) {
            if (this.select.template.id == SkillName.TAI_TAO_NANG_LUONG) {
                startRecoveryEnery();
                if (achievements != null) {
                    achievements.get(14).addCount(1);// kỹ năng thành thạo
                }
            }
        }
        if (type == 0) {
            if (this.select.template.id == SkillName.THAI_DUONG_HA_SAN) {
                if (specialSkill != null) {
                    if (specialSkill.id == 13) {
                        manaUse -= Utils.percentOf(manaUse, specialSkill.param);
                    }
                }
                info.mp -= manaUse;
                if (achievements != null) {
                    achievements.get(14).addCount(1);// kỹ năng thành thạo
                }
                int seconds = (select.damage / 1000) * (setThienXinHang ? 2 : 1);
                int distance = Utils.getDistance(0, 0, select.dx, select.dy);
                List<Player> list = zone.getListChar(Zone.TYPE_ALL);
                for (Player _player : list) {
                    if (_player != null && _player != this && myDisciple != _player && !_player.isDead
                            && isMeCanAttackOtherPlayer(_player)) {
                        if (isDisciple()) {
                            Disciple disciple = (Disciple) this;
                            if (disciple.master == _player) {
                                continue;
                            }
                        }
                        if (_player.isKhangTDHS) {
                            zone.service.chat(_player, "Vô dụng thôi, HAHAHAHAHHA");
                            continue;
                        }
                        int d = Utils.getDistance(this.x, this.y, _player.x, _player.y);
                        if (d < distance) {
                            _player.isFreeze = true;
                            _player.freezSeconds = seconds - (seconds * info.options[175] / 100);
                            zone.service.chat(_player, "Chói mắt quá");
                            players.add(_player);
                        }
                    }
                }

                List<Mob> list2 = zone.getListMob();
                for (Mob mob : list2) {
                    int d = Utils.getDistance(this.x, this.y, mob.x, mob.y);
                    if (d < distance) {
                        mob.isFreeze = true;
                        mob.seconds = seconds;
                        mobs.add(mob);
                    }
                }
                if (!isHuman()) {
                    zone.service.chat(this, "Thái dương hạ san");
                }
            }
        }
        if (type == 4) {
            if (this.select.template.id == SkillName.QUA_CAU_KENH_KHI
                    || this.select.template.id == SkillName.MAKANKOSAPPO) {
                info.mp -= manaUse;
                this.seconds = 3000;
                setCharge(true);
                setSkillSpecial(true);
                Utils.setTimeout(() -> {
                    try {
                        setCharge(false);
                        Thread.sleep(1000L);
                        setSkillSpecial(false);
                        seconds = 0;
                    } catch (InterruptedException ex) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                        System.err.println("Error at 101");
                        logger.error("error");
                    }
                }, this.seconds);
            }
        }
        if (type == 6) {
            if (this.select.template.id == SkillName.BIEN_HINH) {
                setCharge(true);
                this.seconds = 3000;
                Utils.setTimeout(() -> {
                    try {
                        if (!this.isDead && zone != null) {
                            setMonkey(true);
                            int level = getSkill(13).point;
                            this.timeIsMoneky = (55 + (10 * level)) * (setCaDic ? 5 : 1);
                            this.updateSkin();
                            this.info.setInfo();
                            service.loadPoint();
                            info.recovery(Info.ALL, 50, true);
                            zone.service.playerLoadBody(this);
                            zone.service.updateBody((byte) 0, this);

                        }
                    } finally {
                        setCharge(false);
                        seconds = 0;
                    }
                }, this.seconds);
            }
        }
        if (type == 7) {
            if (this.select.template.id == SkillName.TU_PHAT_NO) {
                int distance = Utils.getDistance(0, 0, select.dx, select.dy);
                explode(distance);
            }
        }
        if (type == 8) {
            if (this.select.template.id == SkillName.DE_TRUNG) {
                info.mp -= manaUse;
                this.select.lastTimeUseThisSkill = now;
                int[] arr = {8, 11, 32, 25, 43, 49, 50};
                int level = getSkill(12).point;
                int templateId = arr[level - 1];
                long hp = this.info.hpFull * level;
                Mob mob = MobFactory.getMob(MobType.MOB);
                mob.setMobId(this.id);
                mob.setTemplateId((byte) templateId);
                mob.setX(this.x);
                mob.setY((short) (this.y - 40));
                // Mob mob = new Mob(this.id, (byte) templateId, this.x, (short) (this.y - 40));
                mob.hp = mob.maxHp = hp;
                mob.status = 4;
                mob.sys = 0;
                mob.isMobMe = true;
                mob.levelBoss = 0;
                mob.timeLive = (55 + (10 * level));
                mob.percentDamage = this.select.damage;
                this.mobMe = mob;
                zone.service.mobMeUpdate(this, null, -1, (byte) -1, (byte) 0);
            }
        }
        if (type == 9) {
            if (this.select.template.id == SkillName.KHIEN_NANG_LUONG) {
                info.mp -= manaUse;
                ItemTime item = new ItemTime(ItemTimeName.KHIEN_NANG_LUONG, 3784, 15 + ((select.point - 1) * 5), false);
                this.isProtected = true;
                addItemTime(item);
                zone.service.setEffect(null, this.id, Skill.ADD_EFFECT, Skill.CHARACTER, (byte) 33);
            }
        }
        if (type == 10) {
            if (this.select.template.id == SkillName.HUYT_SAO) {
                this.isCritFirstHit = true;
                int distance = Utils.getDistance(0, 0, select.dx, select.dy);
                info.mp -= manaUse;
                List<Player> list = zone.getListChar(Zone.TYPE_HUMAN, Zone.TYPE_PET);
                for (Player _player : list) {
                    if (_player != null && !_player.isDead && !_player.isBoss() && !_player.isMiniDisciple()) {
                        int d = Utils.getDistance(this.x, this.y, _player.x, _player.y);
                        if (d < distance) {
                            ItemTime item = new ItemTime(ItemTimeName.HUYT_SAO, 3781, 30, false);
                            _player.addItemTime(item);
                            _player.isHuytSao = true;
                            _player.hpPercent = this.select.damage;
                            _player.info.setInfo();
                            _player.info.recovery(Info.HP, 50, true);
                            _player.service.loadPoint();
                            zone.service.playerLoadBody(_player);
                            zone.service.setEffect(null, _player.id, Skill.ADD_EFFECT, Skill.CHARACTER, (byte) 39);
                            if (_player.hold != null) {
                                _player.hold.close();
                            }
                        }
                    }
                }
            }
        }
        select.lastTimeUseThisSkill = now;
        zone.service.skillNotFocus(this, type, mobs, players);
    }

    public void explode(int distance) {
        this.seconds = 3000;
        setCharge(true);
        Utils.setTimeout(() -> {
            try {
                if (!this.isDead && zone != null) {
                    long hpFull = info.hpFull;
                    List<Player> list = zone.getListChar(Zone.TYPE_ALL);
                    List<Player> filter = list.stream().filter(f -> {
                        if (f != null && f != this && isMeCanAttackOtherPlayer(f)
                                && Utils.getDistance(this.x, this.y, f.x, f.y) < distance) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                    try {
                        for (Player _player : filter) {
                            _player.lock.lock();
                            try {
                                if (!_player.isDead) {
                                    long dame = hpFull;
                                    dame += (dame * this.info.optionTuSat / 100);
                                    if (_player.isBoss()) {
                                        if (isMonkey) {
                                            dame /= 3;
                                        } else {
                                            dame /= 2;
                                        }
                                    }
                                    if (_player.limitDame > 0 && dame > _player.limitDame) {
                                        dame = _player.limitDame;
                                    }
                                    if (_player.isProtected) {
                                        if (dame >= _player.info.hp) {
                                            _player.setTimeForItemtime(0, 0);
                                            _player.service.sendThongBao("Khiên năng lượng đã vỡ");
                                        }
                                        dame = 1;
                                    }
                                    if (_player instanceof SuperBroly) {
                                        if (dame >= _player.info.hpFull / 10) {
                                            dame = _player.info.hpFull / 10;
                                        }
                                    }
                                    dame = injure(_player, null, dame);
                                    _player.info.hp -= dame;

                                    zone.service.attackPlayer(_player, dame, false, (byte) -1);
                                    if (_player.info.hp <= 0) {
                                        kill(_player);
                                        _player.killed(this);
                                        _player.startDie();
                                    }
                                }
                            } finally {
                                _player.lock.unlock();
                            }

                        }
                        List<Mob> list2 = zone.getListMob();
                        for (Mob mob : list2) {
                            mob.lock.lock();
                            try {
                                if (mob.isDead()) {
                                    continue;
                                }
                                int d = Utils.getDistance(this.x, this.y, mob.x, mob.y);
                                if (d < distance) {
                                    if (mob.templateId != 70) {
                                        mob.hp -= hpFull;
                                        zone.service.attackNpc(hpFull, false, mob, (byte) -1);
                                        if (mob.hp <= 0) {
                                            kill(mob);
                                            mob.startDie(hpFull, false, this);
                                        }
                                    }
                                }
                            } finally {
                                mob.lock.unlock();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startDie();

                }
            } finally {
                setCharge(false);
                seconds = 0;
            }
        }, this.seconds);
    }

    public long injure(Player plAtt, Mob mob, long dameInput) {
        if (plAtt instanceof Broly) {
            Broly broly = (Broly) plAtt;
            return broly.getDameAttack(this);
        }
        if (plAtt instanceof SuperBroly) {
            SuperBroly broly = (SuperBroly) plAtt;
            return broly.getDameAttack(this);
        }
        return dameInput;
    }
    
    public static short[] listTypeBody = new short[]{35, 36, 37};

    public void itemBagToPet(int index) {
        if (myDisciple == null) {
            return;
        }
        if (isDead) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        if (myDisciple.info.power < 1500000) {
            service.sendThongBao(String.format("Yêu cầu sức mạnh đệ tử %s trở lên", 1500000));
            return;
        }
        if (index < 0 || index > this.itemBag.length) {
            return;
        }
        Item item = this.itemBag[index];
        if (item != null) {
            if (item.template.isSuPhu()) {
                service.sendThongBao("Chỉ dành cho sự phụ");
                return;
            }
            if (myDisciple.gender != 3) {
                if (item.template.gender <= 2 && item.template.gender != myDisciple.gender) {
                    service.sendThongBao(Language.WE_CANT_USE_EQUIP);
                    return;
                }
            }
            if (item.require > myDisciple.info.power) {
                service.sendThongBao("Sức mạnh không đạt yêu cầu.");
                return;
            }
            byte type = item.template.type;
            if (type == 32) {
                type = 6;
            } else if (type == 23 || type == 24) {
                type = 7;
            } else if (type == 11) {
                type = 8;
            } else if (type == 36) {
                type = 9;
            } else if (type == Item.TYPE_NGOC_BOI) {
                type = 10;
            }
            if (type >= this.itemBody.length - 1) {
                return;
            }
            Item item2 = myDisciple.itemBody[type];
            myDisciple.itemBody[type] = item.clone();
            myDisciple.itemBody[type].indexUI = type;
            if (item2 != null) {
                Item clone = item2.clone();
                this.itemBag[index] = clone;
                this.itemBag[index].indexUI = index;
            } else {
                this.itemBag[index] = null;
                sort(index, false);
            }
            if (myDisciple != null) {
                myDisciple.info.setInfo();
            }
            if (isNhapThe) {
                info.setInfo();
            }
            myDisciple.updateSkin();
            service.setItemBag();
            service.petInfo((byte) 2);
            if (myDisciple.isMask()) {
                zone.service.updateBody((byte) 0, myDisciple);
            } else {
                zone.service.updateBody((byte) -1, myDisciple);
            }
            zone.service.playerLoadBody(myDisciple);
            myDisciple.update(item.template.type);
        }
    }

    public void itemPetToBag(int index) {
        if (myDisciple == null) {
            return;
        }
        if (isDead) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        if (index < 0 || index > myDisciple.itemBody.length) {
            return;
        }
        Item item = myDisciple.itemBody[index];
        if (item != null) {
            if (getSlotNullInBag() == 0) {
                service.serverMessage2(Language.ME_BAG_FULL);
                return;
            }
            for (int i = 0; i < this.itemBag.length; i++) {
                if (this.itemBag[i] == null) {
                    this.itemBag[i] = item.clone();
                    this.itemBag[i].indexUI = i;
                    myDisciple.itemBody[index] = null;
                    if (myDisciple != null) {
                        myDisciple.info.setInfo();
                    }
                    if (isNhapThe) {
                        info.setInfo();
                    }
                    myDisciple.updateSkin();
                    service.setItemBag();
                    service.petInfo((byte) 2);
                    if (myDisciple.isMask()) {
                        zone.service.updateBody((byte) 0, myDisciple);
                    } else {
                        zone.service.updateBody((byte) -1, myDisciple);
                    }
                    service.loadPoint();
                    zone.service.playerLoadBody(myDisciple);
                    myDisciple.update(item.template.type);
                    return;
                }
            }
        }
    }

    public void itemBodyToBag(int index) {
        if (isDead) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        if (index < 0 || index > this.itemBody.length) {
            return;
        }
        Item item = this.itemBody[index];
        if (item != null) {
            if (getSlotNullInBag() == 0) {
                service.serverMessage2(Language.ME_BAG_FULL);
                return;
            }
            for (int i = 0; i < this.itemBag.length; i++) {
                if (this.itemBag[i] == null) {
                    this.itemBag[i] = item.clone();
                    this.itemBag[i].indexUI = i;
                    this.itemBody[index] = null;
                    info.setInfo();
                    updateSkin();
                    service.setItemBag();
                    service.setItemBody();
                    if (this.isMask) {
                        zone.service.updateBody((byte) 0, this);
                    } else {
                        zone.service.updateBody((byte) -1, this);
                    }
                    service.loadPoint();
                    zone.service.playerLoadBody(this);
                    update(item.template.type);
                    if (item.template.type == Item.TYPE_PET_BAY) {
                        updatePetTheoSau(true);
                    }
                    if (item.template.type == Item.TYPE_PET_THEO_SAU) {
                        zone.leave(miniDisciple);
                        miniDisciple = null;
                        info.setInfo();
                        service.loadPoint();
                    }
                    if (item.template.type == Item.TYPE_HAO_QUANG) {
                        int eff = idAuraEff;
                        setAuraEffect();
                        if (eff != idAuraEff) {
                            zone.service.setIDAuraEff(this.id, this.idAuraEff);
                        }
                    }
                    // if (item.template.type == Item.TYPE_DANH_HIEU) {
                    // danhHieu = null;
                    // this.loadBody();
                    // }
                    return;
                }
            }
        }
    }

    public void itemBodyToBox(int index) {
        if (isDead) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        if (index < 0 || index > this.itemBody.length) {
            return;
        }
        Item item = this.itemBody[index];
        if (item != null) {
            if (getSlotNullInBox() == 0) {
                service.serverMessage2(Language.ME_BOX_FULL);
                return;
            }
            for (int i = 0; i < this.itemBox.length; i++) {
                if (this.itemBox[i] == null) {
                    this.itemBox[i] = item.clone();
                    this.itemBox[i].indexUI = i;
                    this.itemBody[index] = null;
                    info.setInfo();
                    updateSkin();
                    service.setItemBox();
                    service.setItemBody();
                    if (this.isMask) {
                        zone.service.updateBody((byte) 0, this);
                    } else {
                        zone.service.updateBody((byte) -1, this);
                    }
                    service.loadPoint();
                    zone.service.playerLoadBody(this);
                    update(item.template.type);
                    return;
                }
            }
        }
    }

    public boolean isFullBag(int count, String... notify) {
        if (this.getCountEmptyBag() < count) {
            this.service.sendThongBao(notify.length == 0 ? Language.ME_BAG_FULL : notify[0]);
            return true;
        }
        return false;
    }

    public void itemBagToBody(int index) {
        if (isDead) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        if (index < 0 || index > this.itemBag.length) {
            return;
        }
        Item item = this.itemBag[index];
        if (item != null) {
            if (item.template.isDeTu()) {
                service.sendThongBao("Chỉ dành cho đệ tử");
                return;
            }
            if (item.template.gender <= 2 && item.template.gender != this.gender) {
                service.sendThongBao(Language.WE_CANT_USE_EQUIP);
                return;
            }
            if (item.require > this.info.power) {
                service.sendThongBao("Sức mạnh không đạt yêu cầu.");
                return;
            }
            byte indexBody = item.template.type;
            if (indexBody == 32) {
                indexBody = 6;
            } else if (indexBody == 23 || indexBody == 24) {
                indexBody = 7;
            } else if (indexBody == 11) {
                indexBody = 8;
            } else if (indexBody == 37) {
                indexBody = 9;
            } else if (indexBody == Item.TYPE_PET_THEO_SAU) {
                indexBody = 10;
                setMiniDisciple(item);
            } else if (indexBody == Item.TYPE_PET_BAY) {
                indexBody = 11;
            } else if (indexBody == Item.TYPE_DANH_HIEU) {
                indexBody = 12;

            } else if (indexBody == Item.TYPE_NGOC_BOI) {
                indexBody = 13;
            } else if (indexBody == Item.TYPE_HAO_QUANG) {
                indexBody = 14;
            }
            if (indexBody >= this.itemBody.length) {
                return;
            }
            Item item2 = this.itemBody[indexBody];
            this.itemBody[indexBody] = item.clone();
            this.itemBody[indexBody].indexUI = indexBody;
            if (item2 != null) {
                Item clone = item2.clone();
                this.itemBag[index] = clone;
                this.itemBag[index].indexUI = index;
            } else {
                this.itemBag[index] = null;
                sort(index, false);
            }
            info.setInfo();
            updateSkin();
            service.setItemBag();
            service.setItemBody();
            if (this.isMask) {
                zone.service.updateBody((byte) 0, this);
            } else {
                zone.service.updateBody((byte) -1, this);
            }

            if (item.template.type == Item.TYPE_PET_BAY) {
                updatePetTheoSau(true);
            }
            if (item.template.type == Item.TYPE_PET_BAY) {
                this.loadBody();
            }
            if (item.template.type == Item.TYPE_HAO_QUANG) {
                int eff = idAuraEff;
                setAuraEffect();
                if (eff != idAuraEff) {
                    zone.service.setIDAuraEff(this.id, this.idAuraEff);
                }
                System.err.println("1234");
            }
            service.loadPoint();
            zone.service.playerLoadBody(this);
            update(item.template.type);
        }
    }

    public void sort() {
        // for (int i = 0; i < this.numberCellBag; i++) {
        // if (this.itemBag[i] == null) {
        // sort(i, false);
        // }
        // }
        // sort(0, true);
    }

    public void sort(int index, boolean isUpdate) {
        int index2 = -1;
        for (int i = index; i < this.numberCellBag; i++) {
            if (this.itemBag[i] != null) {
                index2 = i;
            }
        }
        if (index2 != -1) {
            this.itemBag[index] = this.itemBag[index2];
            this.itemBag[index].indexUI = index;
            this.itemBag[index2] = null;
        }
        if (isUpdate) {
            service.setItemBag();
        }
    }

    public void itemBoxToBody(int index) {
        if (index < 0 || index > this.itemBox.length) {
            return;
        }
        if (isDead) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        Item item = this.itemBox[index];
        if (item != null) {
            byte type = item.template.type;
            if (type >= this.itemBody.length) {
                return;
            }
            Item item2 = this.itemBody[type];
            this.itemBody[type] = item.clone();
            this.itemBody[type].indexUI = type;
            if (item2 != null) {
                Item clone = item2.clone();
                this.itemBox[index] = clone;
                this.itemBox[index].indexUI = index;
            } else {
                this.itemBox[index] = null;
            }
            info.setInfo();
            updateSkin();
            service.setItemBox();
            service.setItemBody();
            if (this.isMask) {
                zone.service.updateBody((byte) 0, this);
            } else {
                zone.service.updateBody((byte) -1, this);
            }
            service.loadPoint();
            zone.service.playerLoadBody(this);
            update(item.template.type);
        }
    }

    public void itemBagToBox(int index) {
        if (index < 0 || index > this.itemBag.length) {
            return;
        }
        if (isDead) {
            return;
        }
        if (!zone.map.isHome()) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        Item item = this.itemBag[index];
        if (item != null) {
            if (getSlotNullInBox() == 0) {
                service.serverMessage2(Language.ME_BOX_FULL);
                return;
            }
            int quantityMax = Server.getMaxQuantityItem();
            int quantityCanAdd = 0;
            int having = 0;
            if (item.template.type == Item.TYPE_DAUTHAN) {
                for (Item itm : itemBox) {
                    if (itm != null && itm.template.type == Item.TYPE_DAUTHAN) {
                        having += itm.quantity;
                    }
                }
            } else {
                having = getQuantityInBoxById(item.id);
            }

            quantityCanAdd = quantityMax - having;
            if (quantityCanAdd > item.quantity) {
                quantityCanAdd = item.quantity;
            }
            if (quantityCanAdd <= 0) {
                return;
            }
            boolean added = false;
            if (item.template.isUpToUp()) {
                int indexItem = getIndexBoxById(item.id);
                if (indexItem != -1) {
                    this.itemBox[indexItem].quantity += quantityCanAdd;
                    item.quantity -= quantityCanAdd;
                    // if (item.id == ItemName.TU_DONG_LUYEN_TAP) {
                    // this.itemBox[indexItem].options.get(0).param += item.options.get(0).param;
                    // this.itemBox[indexItem].quantity = 1;
                    // item.quantity = 0;
                    // }
                    for (ItemOption o : item.options) {
                        if (o.optionTemplate.id == 1 || o.optionTemplate.id == 31) {
                            for (ItemOption o2 : this.itemBox[indexItem].options) {
                                if (o.optionTemplate.id == o2.optionTemplate.id) {
                                    o2.param += o.param;
                                    this.itemBox[indexItem].quantity = 1;
                                    item.quantity = 0;
                                    break;
                                }
                            }
                        }
                    }
                    if (item.quantity <= 0) {
                        this.itemBag[index] = null;
                        sort(index, false);
                    }
                    added = true;
                }
            }

            if (!added) {
                for (int i = 0; i < this.itemBox.length; i++) {
                    if (this.itemBox[i] == null) {
                        this.itemBox[i] = item.clone();
                        this.itemBox[i].indexUI = i;
                        this.itemBox[i].quantity = quantityCanAdd;
                        item.quantity -= quantityCanAdd;
                        if (item.quantity <= 0) {
                            this.itemBag[index] = null;
                            sort(index, false);
                        }
                        break;
                    }
                }
            }
            service.setItemBag();
            service.setItemBox();
            if (item.template.type == 23 || item.template.type == 24) {
                setMount();
            }
        }
    }

    public int getQuantityInBoxById(int itemId) {
        int quantity = 0;
        for (Item itm : itemBox) {
            if (itm != null && itm.id == itemId) {
                quantity += itm.quantity;
            }
        }
        return quantity;
    }

    public int getQuantityInBagById(int itemId) {
        int quantity = 0;
        for (Item itm : itemBag) {
            if (itm != null && itm.id == itemId) {
                quantity += itm.quantity;
            }
        }
        return quantity;
    }

    public void itemBoxToBag(int index) {
        if (index < 0 || index > this.itemBox.length) {
            return;
        }
        if (isDead) {
            return;
        }
        if (!zone.map.isHome()) {
            return;
        }
        if (isTrading) {
            service.sendThongBao(Language.TRADE_FAIL2);
            return;
        }
        Item item = this.itemBox[index];
        if (item != null) {
            if (getSlotNullInBag() == 0) {
                service.serverMessage2(Language.ME_BOX_FULL);
                return;
            }
            int quantityCanAdd = 0;
            int having = 0;
            int quantityMax = Server.getMaxQuantityItem();
            if (item.template.type == Item.TYPE_DAUTHAN) {
                for (Item itm : itemBag) {
                    if (itm != null && itm.template.type == Item.TYPE_DAUTHAN) {
                        having += itm.quantity;
                    }
                }
            } else {
                having = getQuantityInBagById(item.id);
            }
            quantityCanAdd = quantityMax - having;
            if (quantityCanAdd > item.quantity) {
                quantityCanAdd = item.quantity;
            }
            if (quantityCanAdd <= 0) {
                return;
            }
            boolean added = false;
            if (item.template.isUpToUp()) {
                int indexItem = getIndexBagById(item.id);
                if (indexItem != -1) {
                    this.itemBag[indexItem].quantity += quantityCanAdd;
                    item.quantity -= quantityCanAdd;

                    for (ItemOption o : item.options) {
                        if (o.optionTemplate.id == 1 || o.optionTemplate.id == 31) {
                            for (ItemOption o2 : this.itemBag[indexItem].options) {
                                if (o.optionTemplate.id == o2.optionTemplate.id) {
                                    o2.param += o.param;
                                    this.itemBag[indexItem].quantity = 1;
                                    item.quantity = 0;
                                    break;
                                }
                            }
                        }
                    }
                    if (item.quantity <= 0) {
                        this.itemBox[index] = null;
                    }
                    added = true;
                }
            }
            if (!added) {
                for (int i = 0; i < this.itemBag.length; i++) {
                    if (this.itemBag[i] == null) {
                        this.itemBag[i] = item.clone();
                        this.itemBag[i].indexUI = i;
                        this.itemBag[i].quantity = quantityCanAdd;
                        item.quantity -= quantityCanAdd;
                        if (item.quantity <= 0) {
                            this.itemBox[index] = null;
                        }
                        break;
                    }
                }
            }
            if (taskMain != null && taskMain.id == 0 && taskMain.index == 3) {
                taskNext();
            }
            service.setItemBag();
            service.setItemBox();
            if (item.template.type == 23 || item.template.type == 24) {
                setMount();
            }
        }
    }

    public int getIndexBagById(int id) {

        for (int i = 0; i < this.itemBag.length; i++) {
            Item item = this.itemBag[i];
            if (item != null && item.id == id) {
                return i;
            }
        }
        return -1;
    }

    public Item getItemInBag(int id) {
        for (Item item : this.itemBag) {
            if (item != null && item.id == id) {
                return item;
            }
        }
        return null;
    }

    public int getIndexBoxById(int id) {
        for (int i = 0; i < this.itemBox.length; i++) {
            Item item = this.itemBox[i];
            if (item != null && item.id == id) {
                return i;
            }
        }
        return -1;
    }

    public void update(int type) {
        if (zone != null) {
            switch (type) {
                case 0:
                    zone.service.playerLoadAo(this);
                    break;

                case 1:
                    zone.service.playerLoadQuan(this);
                    break;

                case 5:
                    zone.service.playerLoadAll(this);
                    break;
            }
        }
    }

    public void menu(Message ms) {
        try {
            if (menus == null) {
                return;
            }
            int npcId = ms.reader().readUnsignedByte();
            int menuId = ms.reader().readUnsignedByte();
            int optionId = ms.reader().readUnsignedByte();
            if (menus.isEmpty() || menuId >= menus.size()) {
                return;
            }
            KeyValue<Integer, String> keyValue = menus.get(menuId);
            if (keyValue == null) {
                return;
            }
            menus.clear();
            Npc npc = zone.findNpcByID(npcId);
            if (npc != null) {
                confirmKeyValue(keyValue, npc);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 100");
            logger.error("failed!", ex);
        }
    }

    public boolean taskTalk(Npc npc) {
        if (taskMain.id == 0) {
            if (taskMain.index == 2) {
                service.openUISay(npc.templateId, TaskText.TASK_0_2, npc.avatar);
                taskNext();
                return true;
            } else if (taskMain.index == 5) {
                service.openUISay(npc.templateId, TaskText.TASK_0_5, npc.avatar);
                taskNext();
                int skillID = 0;
                if (gender == 1) {
                    skillID = 2;
                } else if (gender == 2) {
                    skillID = 4;
                }
                Skill skill;
                try {
                    skill = Skills.getSkill((byte) skillID, (byte) 1).clone();
                    skills.add(skill);
                    service.loadSkill();
                    shortcut[0] = (byte) skillID;
                    service.changeOnSkill(shortcut);
                    select = skill;
                } catch (CloneNotSupportedException ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 99");
                    logger.error("failed!", ex);
                }
                updateTask(1);
                return true;
            }
        } else if (taskMain.id == 1 && taskMain.index == 1) {
            String text = TaskText.TASK_1_1[gender];
            service.openUISay(npc.templateId, text, npc.avatar);
            updateTask(2);
            return true;
        } else if (taskMain.id == 2 && taskMain.index == 1) {
            int index = getIndexBagById(73);
            if (index != -1) {
                removeItem(index, itemBag[index].quantity);
            }
            String text = TaskText.TASK_2_1 + "\n" + TaskText.TASK_3_0[gender];
            service.openUISay(npc.templateId, text, npc.avatar);
            updateTask(3);
            return true;
        } else if (taskMain.id == 3 && taskMain.index == 2) {
            String text = TaskText.TASK_3_FINISH;
            service.openUISay(npc.templateId, text, npc.avatar);
            updateTask(6);
            updateSkin();
            zone.service.updateBag(this);
            zone.service.playerLoadWeapon(this);
            return true;
        } else if (taskMain.id == 4 && taskMain.index == 1) {
            String text = TaskText.TASK_4_FINISH + "\n" + TaskText.TASK_6_0;
            service.openUISay(npc.templateId, text, npc.avatar);
            updateTask(6);
            return true;
        } else if (taskMain.id == 6 && taskMain.index == 3) {
            String text = TaskText.TASK_6_FINISH + "\n" + TaskText.TASK_7_0;
            service.openUISay(npc.templateId, text, npc.avatar);
            int itemID = 67;
            if (gender == 1) {
                itemID = 80;
            } else if (gender == 2) {
                itemID = 88;
            }
            Item item = new Item(itemID);
            item.setDefaultOptions();
            item.quantity = 1;
            addItem(item);
            updateTask(7);
            return true;
        } else if (taskMain.id == 7) {
            if (taskMain.index == 2) {
                String text = TaskText.TASK_7_2;
                service.openUISay(npc.templateId, text, npc.avatar);
                taskNext();
                return true;
            } else if (taskMain.index == 3) {
                String text = TaskText.TASK_7_3 + "\n" + TaskText.TASK_8_0[gender];
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(8);
                return true;
            }
        } else if (taskMain.id == 8) {
            if (taskMain.index == 2) {
                String text = TaskText.TASK_7_2;
                service.openUISay(npc.templateId, text, npc.avatar);
                taskNext();
                setListAccessMap();
                return true;
            } else if (taskMain.index == 3) {
                String text = TaskText.TASK_7_3 + "\n" + TaskText.TASK_8_0[gender];
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(9);
                return true;
            }
        } else if (taskMain.id == 8) {
            if (taskMain.index == 2) {
                String text = TaskText.TASK_8_2;
                service.openUISay(npc.templateId, text, npc.avatar);
                taskNext();
                setListAccessMap();
                return true;
            }
        } else if (taskMain.id == 9) {
            if (taskMain.index == 0) {
                String text = TaskText.TASK_9_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                taskNext();
                return true;
            } else if (taskMain.index == 1) {
                String text = TaskText.TASK_9_1;
                service.openUISay(npc.templateId, text, npc.avatar);
                taskNext();
                ((KarinForest) zone).appearTaoPaiPai();
                setListAccessMap();
                return true;
            } else if (taskMain.index == 3) {
                String text = TaskText.TASK_9_3 + "\n" + TaskText.TASK_10_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(10);
                return true;
            }
        } else if (taskMain.id == 10) {
            if (taskMain.index == 2) {
                String text = TaskText.TASK_10_2;
                service.openUISay(npc.templateId, text, npc.avatar);
                Item item = new Item(19);
                item.setDefaultOptions();
                item.quantity = 1;
                addItem(item);
                taskNext();
                return true;
            }
            if (taskMain.index == 3) {
                updateTask(11);
                String text = TaskText.TASK_11_0[gender];
                service.openUISay(npc.templateId, text, npc.avatar);
                Item item = new Item(85);
                item.setDefaultOptions();
                item.quantity = 1;
                addItem(item);
                return true;
            }
        } else if (taskMain.id == 11) {
            if (taskMain.index == 1) {
                int index = getIndexBagById(85);
                if (index != -1) {
                    removeItem(index, itemBag[index].quantity);
                }
                String text = String.format(TaskText.TASK_11_1[gender], this.name) + "\n" + TaskText.TASK_12_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                int itemID = 94;
                if (gender == 1) {
                    itemID = 101;
                } else if (gender == 2) {
                    itemID = 108;
                }
                Item item = new Item(itemID);
                item.setDefaultOptions();
                item.quantity = 1;
                addItem(item);
                updateTask(12);
                return true;
            }
        } else if (taskMain.id == 12) {
            if (taskMain.index == 1) {
                if (clan != null && clan.getNumberMember() >= 1) {
                    String text = TaskText.TASK_12_FINISH + "\n" + TaskText.TASK_13_0[gender];
                    service.openUISay(npc.templateId, text, npc.avatar);
                    int itemID = 115;
                    if (gender == 1) {
                        itemID = 122;
                    } else if (gender == 2) {
                        itemID = 129;
                    }
                    Item item = new Item(itemID);
                    item.setDefaultOptions();
                    item.quantity = 1;
                    addItem(item);
                    updateTask(13);
                    return true;
                } else {
                    service.openUISay(npc.templateId, "Con hãy vào bang hội có 5 thành viên trở lên để cùng chiến đấu",
                            npc.avatar);
                }
            }
        } else if (taskMain.id == 13) {
            if (taskMain.index == 3) {
                String text = TaskText.TASK_13_FINISH + "\n" + TaskText.TASK_14_0[gender];
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(14);
                return true;

            }
        } else if (taskMain.id == 14) {
            if (taskMain.index == 2) {
                String text = TaskText.TASK_14_FINISH + "\n" + TaskText.TASK_15_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(15);
                return true;

            }
        } else if (taskMain.id == 15) {
            if (taskMain.index == 4) {
                String text = TaskText.TASK_15_FINISH + "\n" + TaskText.TASK_16_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(16);
                return true;

            }
        } else if (taskMain.id == 16) {
            if (taskMain.index == 3) {
                String text = TaskText.TASK_16_FINISH + "\n" + TaskText.TASK_17_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(17);
                return true;

            }
        }
        if (taskMain.id == 18) {
            if (taskMain.index == 5) {
                String text = TaskText.TASK_18_FINISH + "\n" + TaskText.TASK_19_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(19);
                return true;
            }
        } else if (taskMain.id == 19) {
            if (taskMain.index == 3) {
                String text = TaskText.TASK_19_FINISH + "\n" + TaskText.TASK_20_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(20);
                return true;
            }
        } else if (taskMain.id == 20) {
            if (taskMain.index == 6) {
                String text = TaskText.TASK_20_FINISH + "\n" + TaskText.TASK_21_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(21);
                return true;
            }
        } else if (taskMain.id == 21) {
            if (taskMain.index == 4) {
                String text = TaskText.TASK_21_FINISH + "\n" + TaskText.TASK_22_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(22);
                return true;
            }
        } else if (taskMain.id == 22) {
            if (taskMain.index == 0) {
                String text = "Cảm ơn bạn đã đến đây hỗ trợ, hãy đến điểm hẹn để gặp bọn Robot sát thủ nào";
                service.openUISay(npc.templateId, text, npc.avatar);
                taskNext();
                return true;
            }
            if (taskMain.index == 5) {
                String text = TaskText.TASK_22_FINISH + "\n" + TaskText.TASK_23_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(23);
                return true;
            }
        } else if (taskMain.id == 23) {
            if (taskMain.index == 4) {
                String text = TaskText.TASK_23_FINISH + "\n" + TaskText.TASK_24_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(24);
                return true;
            }
        } else if (taskMain.id == 24) {
            if (taskMain.index == 5) {
                String text = TaskText.TASK_23_FINISH + "\n" + TaskText.TASK_24_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(25);
                return true;
            }
        } else if (taskMain.id == 25) {
            if (taskMain.index == 5) {
                String text = TaskText.TASK_23_FINISH + "\n" + TaskText.TASK_24_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(26);
                return true;
            }
        } else if (taskMain.id == 26) {
            if (taskMain.index == 4) {
                String text = TaskText.TASK_23_FINISH + "\n" + TaskText.TASK_24_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                updateTask(27);
                return true;
            }
        } else if (taskMain.id == 27) {
            if (taskMain.index == 0) {
                taskNext();
                return true;
            }
            if (taskMain.index == 5) {
                updateTask(28);
                return true;
            }
        } else if (taskMain.id == 28) {
            if (taskMain.index == 5) {
                updateTask(29);
                return true;
            }
        }
        return false;
    }

    public void consignment(Message message) {
        try {
            int type = message.reader().readByte();
            if (type == 0) {
                int index = message.reader().readByte();
                int quantity = message.reader().readInt();
                int price = message.reader().readInt();
                Consignment.getInstance().addItem(this, index, quantity, price);
            } else if (type == 1) {
                Consignment.getInstance().getItem(this, message.reader().readInt());
            } else if (type == 2) {
                Consignment.getInstance().buyItem(this, message.reader().readInt());
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 98");
            logger.error("consignment", ex);
        }
    }

    private int transportToMap;
    private int maxTime;
    private long last;

    public void transport(int mapID, int type, int time) {
        if (type == 0) {
            if (getShip() == 1) {
                setTeleport((byte) 3);
            } else {
                setTeleport((byte) 1);
            }
            if (getShip() == 1) {
                info.recovery(Info.ALL, 100, true);
            }
            zone.service.addTeleport(getId(), getTeleport());
        }
        setMaxTime(time);
        setTransportToMap(mapID);
        setLast(System.currentTimeMillis());
        zone.leave(this);
        service.transport(getMaxTime(), type);
    }

    public void transportNow() {
        long now = System.currentTimeMillis();
        int seconds = (int) ((now - getLast()) / 1000);
        if (seconds < getMaxTime()) {
            if (getTotalGem() >= 1) {
                subDiamond(1);
            } else {
                return;
            }
        }
        TMap map = MapManager.getInstance().getMap(getTransportToMap());
        setX((short) 100);
        setY((short) 0);
        if (map.isTreasure()) {
            boolean isFlag = false;
            if (clan != null) {
                Treasure treasure = clan.treasure;
                if (treasure != null) {
                    treasure.enter(this);
                    isFlag = true;
                }
            }
            if (!isFlag) {
                goHome();
            }
        } else {
            int zoneId = map.getZoneID();
            map.enterZone(this, zoneId);
            setTeleport((byte) 0);
        }
        setY(map.collisionLand(getX(), getY()));
    }

    public void openUIMenu(Message ms) {
        try {
            menus.clear();
            this.shop = null;
            this.combine = null;
            if (this.isDead) {
                return;
            }
            if (isTrading) {
                return;
            }
            int npcId = ms.reader().readShort();
            int mapId = zone.map.mapID;
            Npc npc = zone.findNpcByID(npcId);
            if (npc != null || npcId == NpcName.LY_TIEU_NUONG) {
                if (taskMain != null && taskMain.tasks[taskMain.index] == npcId) {
                    if (taskTalk(npc)) {
                        setListAccessMap();
                        return;
                    }
                }
                switch (npcId) {

                    case NpcName.DAISHINKAN:
                        if (zone.map.mapID == 178 || zone.map.mapID == 179 || zone.map.mapID == 180) {
                            menus.add(new KeyValue(1163, "Shop hỗ trợ"));
                            menus.add(new KeyValue(1164, "Về Đảo Kame"));
                        } else {
                            menus.add(new KeyValue(1158, "Thiên Đường Mộng Mơ "));
                            menus.add(new KeyValue(1161, "Hành Tinh Bill"));
                            menus.add(new KeyValue(1156, "Di chuyển\ntới Map\nsự kiện"));

                        }
                        service.openUIConfirm(npc.templateId, "Ta có thể giúp gì cho ngươi? \n " // + "Bạn có thể thu
                                // thập x99 bình nước
                                // xanh lam,lục,đỏ và
                                // 500tr vàng để lấy 1
                                // phần quà sự kiện"
                                ,
                                 npc.avatar, menus);
                        break;
                    case NpcName.RUONG_DO_HE_THONG:
                    case NpcName.TORIBOT:
                        menus.add(new KeyValue(421, "Nhập Code tại đây"));
                        menus.add(new KeyValue(1165, "Nhận quà giftCode"));
                        service.openUIConfirm(npc.templateId, "Ta có thể giúp gì cho ngươi? ", npc.avatar, menus);
                        break;
                    case NpcName.BUNMA_TET:
                        if (this.session.user.getActivated() == 0) {
                            service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
                            return;
                        }
                        menus.add(new KeyValue(1110, "Shop\nĐặc Biệt"));
                        // menus.add(new KeyValue(1116, "Di chuyển\nĐến Đặc Biệt"));
                        // menus.add(new KeyValue(1134, "Di chuyển\nĐến Quảng Trường\nPháo Hoa"));
                        menus.add(new KeyValue(1138, "Hồi Skill Ngay\n(200tr Vàng)"));
                        // menus.add(new KeyValue(1145, "Đổi bình nước"));
                        // menus.add(new KeyValue(1139, "Đổi Hộp Quà 2025"));
                        // menus.add(new KeyValue(1140, "Vòng quay tết 2025"));
                        menus.add(new KeyValue(1152, "Đổi Bó hoa sắc màu"));
                        menus.add(new KeyValue(1153, "Đổi Bó hoa sum vầy"));
                        menus.add(new KeyValue(1155, "Đổi Hộp Mù Bé Ba"));

                        service.openUIConfirm(npc.templateId, "Ta có thể giúp gì cho ngươi? \n " // + "Bạn có thể thu
                                // thập x99 bình nước
                                // xanh lam,lục,đỏ và
                                // 500tr vàng để lấy 1
                                // phần quà sự kiện"
                                ,
                                 npc.avatar, menus);
                        break;
                    case NpcName.ONG_GOHAN:
                    case NpcName.ONG_PARAGUS:
                    case NpcName.ONG_MOORI:
                        menus.add(new KeyValue(6868, "Rút\nThỏi Vàng"));
//                        menus.add(new KeyValue(6869, "Nhận\nđệ tử"));
                        menus.add(new KeyValue(6871, "Nhập\nGiftcode"));
                        menus.add(new KeyValue(6870, "Nhận\nngọc xanh"));
                        service.openUIConfirm(npc.templateId, "Con muốn ta giúp gì ", npc.avatar, menus);
                        break;
                    case NpcName.RUONG_DO:

                        boolean isClan = false;
                        // if (zone.map.isClanTerritory()) {
                        // isClan = true;
                        // }
                        // if (isClan) {
                        //// service.setItemClan();
                        // } else {
                        // service.setItemBox();
                        // }
                        service.setItemBox();
                        service.showBox(isClan);
                        break;

                    case NpcName.DAU_THAN:
                        magicTree.openMenu(this);
                        break;

                    case NpcName.RONG_1_SAO:
                    case NpcName.RONG_2_SAO:
                    case NpcName.RONG_3_SAO:
                    case NpcName.RONG_4_SAO:
                    case NpcName.RONG_5_SAO:
                    case NpcName.RONG_6_SAO:
                    case NpcName.RONG_7_SAO: {
                        if (zone.map.isBlackDragonBall()) {
                            if (itemLoot != null) {
                                menus.add(new KeyValue(CMDMenu.BLACK_DRAGONBALL_PHU, "Phù hộ"));
                            }
                        }
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        service.openUIConfirm(npc.templateId, "Ta có thể giúp gì cho ngươi?", npc.avatar, menus);
                    }
                    break;

                    case NpcName.CUA_HANG_KY_GUI:
                        // khoidt
                        if (this.session.user.getActivated() != 1) {
                            service.sendThongBao("Bạn cần kích hoạt để sử dụng tính năng này");
                            return;
                        }
                        menus.add(new KeyValue(CMDMenu.CONSIGNMENT_GUIDE, "Hướng\ndẫn\nthêm"));
                        menus.add(new KeyValue(CMDMenu.CONSIGNMENT, "Mua bán\nKý gửi"));
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        service.openUIConfirm(npc.templateId,
                                "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cám ơn bạn đã ghé thăm.",
                                npc.avatar, menus);
                        break;

                    case NpcName.BUNMA:
                    case NpcName.DENDE:
                    case NpcName.APPULE:
                        if (mapId == MapName.LANG_MORI && idNRNM != -1) {
                            menus.add(new KeyValue(20008, "Gọi\nRồng Thần\nNamek"));
                            service.openUIConfirm(npc.templateId,
                                    "Hãy thu thập đủ 7 viên ngọc rồng Namek, ta sẽ giúp ngươi triệu hồi rồng thần",
                                    npc.avatar, menus);
                            break;
                        }
                        menus.add(new KeyValue(CMDMenu.STORE, "Cửa hàng"));
                        service.openUIConfirm(npc.templateId, "Cậu cần trang bị cứ đến chỗ tôi nhé", npc.avatar, menus);
                        break;

                    case NpcName.DR_BRIEF: {
                        String text = "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?";
                        if (mapId == MapName.TRAM_TAU_VU_TRU) {
                            menus.add(new KeyValue(CMDMenu.TELEPORT_NAMEC, "Đến\nNamếc"));
                            menus.add(new KeyValue(CMDMenu.TELEPORT_XAYDA, "Đến\nXayda"));
                            if (checkCanEnter(MapName.SIEU_THI)) {
                                menus.add(new KeyValue(CMDMenu.TELEPORT_SIEU_THI, "Siêu thị"));
                            }
                        }
                        if (mapId == MapName.SIEU_THI) {
                            switch (this.gender) {
                                case 0:
                                    menus.add(new KeyValue(CMDMenu.TELEPORT_TRAI_DAT, "Đến\nTrái đất"));
                                    break;

                                case 1:
                                    menus.add(new KeyValue(CMDMenu.TELEPORT_NAMEC, "Đến\nNamếc"));
                                    break;

                                case 2:
                                    menus.add(new KeyValue(CMDMenu.TELEPORT_XAYDA, "Đến\nXayda"));
                                    break;
                            }
                        }
                        if (mapId == MapName.LANH_DIA_BANG_HOI) {
                            if (clan != null) {
                                ClanMember clanMember = clan.getMember(this.id);
                                if (clanMember != null) {
                                    if (clanMember.role == 0) {
                                        menus.add(new KeyValue(CMDMenu.CLAN_FUNCTION, "Chức năng\nbang hội"));
                                    }
                                    menus.add(new KeyValue(CMDMenu.CLAN_TASK, "Nhiệm vụ\nBang"));
                                }
                            }
                            menus.add(new KeyValue(CMDMenu.TELEPORT_DAO_KAME, "Đảo Kame"));
                            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            text = "Tôi có thể giúp gì cho bang hội của bạn ?";
                        }
                        service.openUIConfirm(npc.templateId, text, npc.avatar, menus);
                    }
                    break;

                    case NpcName.CARGO:
                        menus.add(new KeyValue(CMDMenu.TELEPORT_TRAI_DAT, "Đến\nTrái đất"));
                        menus.add(new KeyValue(CMDMenu.TELEPORT_XAYDA, "Đến\nXayda"));
                        if (checkCanEnter(MapName.SIEU_THI)) {
                            menus.add(new KeyValue(CMDMenu.TELEPORT_SIEU_THI, "Siêu thị"));
                        }
                        service.openUIConfirm(npc.templateId,
                                "Tàu vũ trụ Namếc tuy cũ nhưng tốc độ không hề kém bất kỳ loại tàu nào khác. Cậu muốn đi đâu?",
                                npc.avatar, menus);
                        break;

                    case NpcName.CUI:
                        if (zone.map.mapID == MapName.TRAM_TAU_VU_TRU_3) {
                            menus.add(new KeyValue(CMDMenu.TELEPORT_TRAI_DAT, "Đến\nTrái đất"));
                            menus.add(new KeyValue(CMDMenu.TELEPORT_NAMEC, "Đến\nNamếc"));
                            if (checkCanEnter(MapName.SIEU_THI)) {
                                menus.add(new KeyValue(CMDMenu.TELEPORT_SIEU_THI, "Siêu thị"));
                            }
                            service.openUIConfirm(npc.templateId,
                                    "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                    npc.avatar, menus);
                        } else if (zone.map.mapID == MapName.THANH_PHO_VEGETA) {
                            if (checkCanEnter(MapName.RUNG_BANG)) {
                                menus.add(new KeyValue(CMDMenu.TELEPORT_COLD, "Đến Cold"));
                            }
                            if (checkCanEnter(MapName.THUNG_LUNG_NAPPA)) {
                                menus.add(new KeyValue(CMDMenu.TELEPORT_NAPPA, "Đến\nNappa"));
                            }
                            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            service.openUIConfirm(npc.templateId,
                                    "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó", npc.avatar,
                                    menus);
                        } else if (zone.map.mapID == MapName.THUNG_LUNG_NAPPA) {
                            menus.add(new KeyValue(CMDMenu.TELEPORT_VEGETA, "Đồng ý"));
                            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            service.openUIConfirm(npc.templateId, "Ngươi muốn về Thành Phố Vegeta", npc.avatar, menus);
                        }
                        break;

                    case NpcName.QUY_LAO_KAME:
                        menus.add(new KeyValue(1127, "Đổi mảnh\ntổng hợp"));
                        menus.add(new KeyValue(605, "Top 100\nSức mạnh"));
                        menus.add(new KeyValue(1210, "Úp Bông tai Cấp 2"));
                        if (clan != null) {
                            menus.add(new KeyValue(536, "Bang hội"));
                            menus.add(new KeyValue(606, "Kho báu\ndưới biển"));
                        }
                        //
                        menus.add(new KeyValue(535, "Nói chuyện"));
                        service.openUIConfirm(npc.templateId, "Cần ta giúp gì không?", npc.avatar, menus);
                        break;

                    case NpcName.TRUONG_LAO_GURU:
                        if (this.gender != 1) {
                            service.openUISay(npc.templateId,
                                    "Con hãy về hành tinh mình để học tập, ta chỉ dạy cho dân tộc Namếc", npc.avatar);
                            return;
                        }
                        master(npc);
                        break;

                    case NpcName.VUA_VEGETA:
                        if (this.gender != 2) {
                            service.openUISay(npc.templateId,
                                    "Ngươi hãy cút về hành tinh của ngươi, ta chỉ dạy cho người Xayda", npc.avatar);
                            return;
                        }
                        master(npc);
                        break;
                    case NpcName.URON:
                        shop = Shop.getShop(npc.templateId);
                        if (shop != null) {
                            shop.setNpc(npc);
                            service.viewShop(shop);
                        }
                        break;

                    case NpcName.PANCHY: {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Chào cưng, tôi là Panchy").append("\n");
                        sb.append("Tôi có thể giúp gì cho cưng?");
                        menus.add(new KeyValue(CMDMenu.CLAN_SHOP, "Cửa hàng"));
                        service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);
                    }
                    break;

                    case NpcName.BO_MONG:
                        // menus.add(new KeyValue(420, "Nhận Ngọc\nMiễn Phí"));
                        service.openUIConfirm(npc.templateId,
                                "Ngươi muốn có thêm thỏi vàng, có nhiều cách, nạp thẻ cào là nhanh nhất",
                                npc.avatar, menus);
                        break;

                    case NpcName.THAN_MEO_KARIN:
                        String text;
                        menus.add(new KeyValue(400, "Đăng ký\ntập\ntự động"));
                        // menus.add(new KeyValue(401, "Nhiệm vụ"));
                        if (this.typeTraining == 0) {
                            text = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta đã";
                            menus.add(new KeyValue(402, "Tập luyện\nvới\nThần Mèo"));
                            menus.add(new KeyValue(403, "Thách đấu\nThần Mèo"));
                        } else if (this.typeTraining == 1) {
                            text = "Từ bây giờ Yajirô sẽ tập luyện cùng ngươi. Yajirô đã từng lên đây tập luyện và bây giờ hắn mạnh hơn ta đấy";
                            menus.add(new KeyValue(404, "Tập luyện\nvới\nYajirô"));
                            menus.add(new KeyValue(405, "Thách đấu\nYajirô"));
                        } else {
                            menus.add(new KeyValue(402, "Tập luyện\nvới\nThần Mèo"));
                            menus.add(new KeyValue(404, "Tập luyện\nvới\nYajirô"));
                            text = "Con hãy bay theo cây Gậy Như Ý trên đỉnh tháp để đến Thần Điện gặp Thượng đế\nCon rất xứng đáng làm đồ đệ ông ấy";
                        }
                        service.openUIConfirm(npc.templateId, text, npc.avatar, menus);
                        break;

                    case NpcName.THUONG_DE:
                        // menus.add(new KeyValue(CMDMenu.CANCEL, "Chức năng\n tạm đóng"));
                        menus.add(new KeyValue(520, "Vòng quay\nMay mắn"));
                        menus.add(new KeyValue(CMDMenu.TELEPORT_HANH_TINH_KAIO, "Đến Kaio"));
                        service.openUIConfirm(npc.templateId, "Chơi vòng quay may mắn không con", npc.avatar, menus);
                        break;

                    case NpcName.THAN_VU_TRU:
                        menus.add(new KeyValue(CMDMenu.TELEPORT_THAN_DIEN, "Về\nthần điện"));
                        // menus.add(new KeyValue(CMDMenu.TELEPORT_THANH_DIA_KAIO, "Thánh địa Kaio"));
                        service.openUIConfirm(npc.templateId, "Con tìm ta có việc gì?", npc.avatar, menus);
                        break;

                    case NpcName.BA_HAT_MIT:
                        if (mapId == MapName.VACH_NUI_ARU_2 || mapId == MapName.VACH_NUI_MOORI_2
                                || mapId == MapName.VACH_NUI_KAKAROT || mapId == MapName.SIEU_THI) {
                            menus.add(new KeyValue(CMDMenu.STORE_BUA, "Cửa hàng\nBùa"));
                            menus.add(new KeyValue(CMDMenu.UPGRADE_ITEM, "Nâng cấp\nvật phẩm"));
                            // menus.add(new KeyValue(CMDMenu.NANG_CAP_2, "Nâng cấp\nvật phẩm 2"));
                            menus.add(new KeyValue(CMDMenu.NHAP_NGOC_RONG, "Nhập Ngọc\nRồng"));
                            Item item = getItemInBag(ItemName.BONG_TAI_PORATA_CAP_2);
                            if (item != null) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Mở chỉ số").append("\n");
                                sb.append("Bông tai").append("\n");
                                sb.append("Porate cấp 2");
                                menus.add(new KeyValue(CMDMenu.MO_CHI_SO_PORATA2, sb.toString()));
                            } else {
                                item = getItemInBag(ItemName.MANH_VO_BONG_TAI);
                                if (item != null) {
                                    menus.add(new KeyValue(CMDMenu.NANG_CAP_PORATA, "Nâng cấp\nBông tai\nPorata"));
                                }
                            }
                        }
                        if (mapId == MapName.DAO_KAME) {
                            menus.add(new KeyValue(CMDMenu.EP_SAO_TRANG_BI, "Ép sao\ntrang bị"));
                            menus.add(new KeyValue(CMDMenu.PHA_LE_HOA, "Pha lê\nhóa trang bị"));
                            // menus.add(new KeyValue(CMDMenu.DOI_DO_HUY_DIET, "Đổi Đồ\nHủy Diệt"));
                            menus.add(new KeyValue(CMDMenu.DOI_DO_KICH_HOAT, "Đổi Đồ\nKích Hoạt"));
                            menus.add(new KeyValue(1154, "Tách Vật phẩm SK"));
                            menus.add(new KeyValue(1157, "Nâng cấp\nTrang bị\nĐệ tử"));
                            // menus.add(new KeyValue(CMDMenu.GHEP_DA, "Ghép đá\nnâng cấp"));
                            // menus.add(new KeyValue(CMDMenu.TACH_DO_THAN_LINH, "Tách đồ\nthần linh"));
                            // menus.add(new KeyValue(CMDMenu.TACH_DO_KICH_HOAT, "Tách đồ\nkích hoạt"));
                        }
                        service.openUIConfirm(npc.templateId, "Ngươi tìm ta có việc gì ?", npc.avatar, menus);
                        break;

                    case NpcName.DUONG_TANG:

                        if (zone.map.mapID == MapName.LANG_ARU) {

                            menus.add(new KeyValue(CMDMenu.TELEPORT_NGU_HANH_SON, "Đồng ý"));
                            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        } else {
                            menus.add(new KeyValue(CMDMenu.TELEPORT_ARU, "Về\nLàng Aru"));
                        }
                        service.openUIConfirm(npc.templateId,
                                "A mi phò phò, thí chủ hãy giúp giải cứu đồ đệ của bần tăng đang bị phong ấn tại ngũ hành sơn.",
                                npc.avatar, menus);

                        // service.dialogMessage("Chức năng đang được phát triển.");
                        // {
                        // if (Event.isEvent()) {
                        // String text2 = "A mi phò phò, thí chủ hãy thu thập bùa 'giải khai phong ấn',
                        // mỗi chữ 10 cái.";
                        // if (zone.map.mapID == MapName.LANG_ARU) {
                        // Item item = itemBody[5];
                        // if (item != null && (item.id == ItemName.CAI_TRANG_TON_NGO_KHONG_TD ||
                        // item.id == ItemName.CAI_TRANG_TON_NGO_KHONG_NM || item.id ==
                        // ItemName.CAI_TRANG_TON_NGO_KHONG_XD)) {
                        // menus.add(new KeyValue(CMDMenu.THINH_KINH, "Đồng ý"));
                        // text2 = "A mi phò phò, Ngộ Không mau hộ tống sư phụ đi thỉnh kinh nào";
                        // } else {
                        // menus.add(new KeyValue(CMDMenu.TELEPORT_NGU_HANH_SON, "Đồng ý"));
                        // text2 = "A mi phò phò, thí chủ hãy giúp giải cứu đồ đệ của bần tăng đang bị
                        // phong ấn tại ngũ hành sơn.";
                        // }
                        // menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        // menus.add(new KeyValue(CMDMenu.DUONG_TANG_REWARD, "Nhận\nthưởng"));
                        // } else {
                        // if (zone.map.mapID == MapName.NGU_HANH_SON) {
                        // menus.add(new KeyValue(CMDMenu.GIAI_PHONG_AN, "Giải\nPhong ấn"));
                        // }
                        // menus.add(new KeyValue(CMDMenu.TELEPORT_ARU, "Về\nLàng Aru"));
                        // }
                        // service.openUIConfirm(npc.templateId, text2, npc.avatar, menus);
                        // } else {
                        // menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                        // service.openUIConfirm(npc.templateId, "Ami phò phò, ta đang đứng đây chờ đồ
                        // đệ", npc.avatar, menus);
                        // }
                        // }
                        break;

                    case NpcName.NGO_KHONG:
                        service.dialogMessage("Chức năng đang được phát triển.");
                        // if (Event.isEvent()) {
                        // menus.add(new KeyValue(CMDMenu.TANG_QUA_HONG_DAO, "Tặng quả\nHồng đào"));
                        // menus.add(new KeyValue(CMDMenu.TANG_QUA_HONG_DAO_CHIN, "Tặng quả\nHồng
                        // đào\nChín"));
                        // service.openUIConfirm(npc.templateId, "Chu mi nga", npc.avatar, menus);
                        // }
                        break;

                    case NpcName.RONG_OMEGA: {
                        String text3 = "Ta có thể làm gì giúp ngươi?";
                        menus.add(new KeyValue(CMDMenu.BLACK_DRAGONBALL_GUIDE, "Hướng\ndẫn\nthêm"));
                        if (clan != null) {
                            ClanMember mem = clan.getMember(this.id);
                            if (mem != null && mem.getNumberOfRewardsCanBeReceived() > 0) {
                                menus.add(new KeyValue(CMDMenu.BLACK_DRAGONBALL_REWARD, "Nhận\nthưởng"));
                            }
                        }
                        if (MapManager.getInstance().blackDragonBall != null) {
                            text3 = "Đường đến với ngọc rồng sao đen đã mở, ngươi có muốn tham gia không?";
                            menus.add(new KeyValue(CMDMenu.BLACK_DRAGONBALL_JOIN, "Tham gia"));
                        }
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        service.openUIConfirm(npc.templateId, text3, npc.avatar, menus);
                    }
                    break;
                    case NpcName.GHI_DANH: {
                        StringBuilder sb = new StringBuilder();
                        if (mapId == MapName.DAI_HOI_VO_THUAT || this.zone.map.isLang()) {
                            menus.add(new KeyValue(586, "Giải\nSiêu Hạng"));
                            menus.add(new KeyValue(581, "Đại Hội\nVõ Thuật\nLần thứ\n23"));
                        } else if (mapId == MapName.DAI_HOI_VO_THUAT_3) {
                            long gold = DHVT23_Service.getGold(timesOfDHVT23);
                            menus.add(new KeyValue(582, "Hướng\ndẫn\nthêm"));
                            if (!isGetChest) {
                                menus.add(new KeyValue(583, "Thi đấu\n" + Utils.formatNumber(gold) + "\nvàng"));
                                if (roundDHVT23 > 0 && !isGetChest) {

                                    menus.add(new KeyValue(584, "Nhận Thưởng\nRương Gỗ " + roundDHVT23));

                                }
                            }
                            menus.add(new KeyValue(585, "Về\nĐại Hội\nVõ Thuật"));
                            sb.append("Đại hội võ thuật lần thứ 23").append("\b");
                            sb.append("Diễn ra bất kể ngày đêm, ngày nghỉ, ngày lễ").append("\b");
                            sb.append("Phần thưởng vô cùng quý giá").append("\b");
                            sb.append("Nhanh chóng tham gia nào").append("\b");
                            if (isGetChest) {
                                sb.append("Bạn đã nhận rương hôm nay rồi").append("\b");
                            }
                        }
                        service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);

                    }
                    break;
                    case NpcName.TRONG_TAI:
                        if (zone != null && zone.map.mapID == MapName.DAI_HOI_VO_THUAT_2) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Đại hội võ thuật Siêu Hạng").append("\b");
                            sb.append("diễn ra 24/7 kể cả thứ 7 và chủ nhật").append("\b");
                            sb.append("Hãy thi đấu ngay để thể hiện đẳng cấp của mình nhé").append("\b");
                            menus.add(new KeyValue(1146, "Top 100\nCao Thủ"));
                            menus.add(new KeyValue(1147, "Hướng\ndẫn\nthêm"));
                            if (DHVT_SH_Service.gI().getTicket(this) >= 1) {
                                menus.add(new KeyValue(1148,
                                        String.format("Miễn phí\n(Còn %d vé)", DHVT_SH_Service.gI().getTicket(this))));
                            } else {
                                menus.add(new KeyValue(1148, "Thi đấu (200tr vàng)"));
                            }
                            menus.add(new KeyValue(1149, "Ưu tiên\nđấu ngay"));
                            menus.add(new KeyValue(585, "Về\nĐại Hội\nVõ Thuật"));
                            service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);
                        }
                        break;
                    case NpcName.LINH_CANH:
                        // if (true) {
                        // service.openUISay(npc.id, "Chức năng đã tạm đóng", npc.avatar);
                        // return;
                        // }
                        if (clan == null) {
                            service.openUISay(npc.id, "Chỉ tếp các bang hội, miễn tiếp khách vãng lai", npc.avatar);
                            return;
                        }
                        // if (clan.getNumberMember() < 5) {
                        // service.openUISay(npc.id, "Doanh trại chỉ dành cho bang hội có tối thiểu 5
                        // thành viên",
                        // npc.avatar);
                        // return;
                        // }
                        ClanMember clanMember = clan.getMember(this.id);
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime openTime = LocalDateTime.of(2025, 4, 6, 17, 0);

                        if (now.isBefore(openTime)) {
                            Duration duration = Duration.between(now, openTime);
                            long minutes = duration.toMinutes();
                            long seconds = duration.minusMinutes(minutes).getSeconds();

                            service.openUISay(
                                    npc.id,
                                    "Bạn có thể tham gia doanh trại sau " + minutes + " phút " + seconds
                                    + " giây nữa.\b"
                                    + "17h00 06/04/2025",
                                    npc.avatar);
                            return;
                        }
                        Barrack barrack = clan.barrack;
                        if (barrack != null && !barrack.running) {
                            boolean isNewDay = !DateUtils.isSameDay(new Date(), new Date(barrack.getOpenedAt()));
                            if (isNewDay) {
                                barrack = null;
                                clan.barrack = null;
                            }
                        }
                        String text2 = "";
                        if (barrack != null) {
                            if (!barrack.running) {
                                menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                                Date date = new Date(barrack.getOpenedAt());
                                DateFormat f = new SimpleDateFormat("HH:mm");
                                String t = f.format(date).replace(":", "h");
                                text2 = String.format(
                                        "Bang hội của ngươi ngày hôm nay đã vào 1 lần rồi (thành viên %s) lúc %s\nNên ngươi không thể vào được nữa.\nHãy chờ ngày mai để có thể vào miễn phí.",
                                        barrack.getOpenMemberName(), t);
                            } else {
                                text2 = String.format(
                                        "Bang hội của ngươi đang đánh trại độc nhãn\nThời gian còn lại là %d phút. Ngươi có muốn tham gia không?",
                                        barrack.getCountDown() / 60);
                                menus.add(new KeyValue(602, "Tham gia"));
                                menus.add(new KeyValue(CMDMenu.CANCEL, "Không"));
                            }
                        } else {
                            List<Player> mems = zone.getMemberSameClan(this);
                            boolean check = true;
                            for (Player _c : mems) {
                                if (_c == this) {
                                    continue;
                                }
                                int d = Utils.getDistance(this.x, this.y, _c.x, _c.y);
                                if (d < 300) {
                                    check = false;
                                    break;
                                }
                            }
                            if (false) {
                                text2 = "Ngươi phải có ít nhất 1 đồng đội cùng bang đứng gần mới có thể vào tuy nhiên ta khuyên ngươi nên đi cùng 3-4 người để khỏi chết.\nHahaha";
                                menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                            } else {
                                text2 = "Hôm nay bang hội của ngươi chưa vào doanh trại lần nào. Ngươi có muốn vào không?\nĐể vào ta khuyên ngươi nên có 3-4 người cùng bang đi cùng.";
                                menus.add(new KeyValue(602, "Vào\n(miễn phí)"));
                                menus.add(new KeyValue(CMDMenu.CANCEL, "Không"));
                            }
                        }
                        menus.add(new KeyValue(601, "Hướng\ndẫn\nthêm"));
                        service.openUIConfirm(npc.templateId, text2, npc.avatar, menus);
                        break;

                    case NpcName.DOC_NHAN:
                        if (clan != null) {
                            Barrack barrack2 = clan.barrack;
                            if (barrack2 != null && barrack2.running) {
                                StringBuilder sb = new StringBuilder();
                                if (barrack2.isWinner) {
                                    barrack2.win();
                                    sb.append("Ta chịu thua, nhưng các ngươi đừng có mong lấy được ngọc của ta")
                                            .append("\n");
                                    sb.append("ta đã giấu ngọc 4 sao và đống ngọc 7 sao trong doanh trại này...")
                                            .append("\n");
                                    sb.append("Các ngươi chỉ có 5 phút để tìm, đố các ngươi tìm ra hahaha");
                                } else {
                                    sb.append("Các ngươi không thể thắng được bọn ta, hahaha");
                                }
                                service.openUISay(npc.templateId, sb.toString(), npc.avatar);
                            }
                        }
                        break;

                    case NpcName.BUNMA_2:
                        menus.add(new KeyValue(544, "Kể truyện"));
                        menus.add(new KeyValue(543, "Cửa\nhàng"));
                        service.openUIConfirm(npc.templateId, "Cảm ơn bạn đã đến đây để giúp chúng tôi", npc.avatar,
                                menus);
                        break;

                    case NpcName.CA_LICH:
                        if (checkCanEnter(102)) {
                            menus.add(new KeyValue(541, "Kể truyện"));
                            if (zone.map.isFuture()) {
                                menus.add(new KeyValue(542, "Quay về\nQuá khứ"));
                            } else {
                                menus.add(new KeyValue(540, "Đi đến\nTương lai"));
                                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            }
                            service.openUIConfirm(npc.templateId, "Chào chú, cháu có thể giúp gì?", npc.avatar, menus);
                        }
                        break;

                    case NpcName.SANTA:
                        menus.add(new KeyValue(CMDMenu.STORE, "Cửa hàng"));
                        menus.add(new KeyValue(CMDMenu.TIEM_HOT_TOC, "Tiệm\nHớt tóc"));
                        service.openUIConfirm(npc.templateId,
                                "Xin chào,ta có một số vật phẩm đặc biệt cậu có muốn xem không?", npc.avatar, menus);
                        break;

                    case NpcName.QUOC_VUONG:
                        // if (info.power < 17999000000L) {
                        // return;
                        // }
                        menus.add(new KeyValue(500, "Bản thân"));
                        menus.add(new KeyValue(501, "Đệ tử"));
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        service.openUIConfirm(npc.templateId,
                                "Con muốn nâng giới hạn sức mạnh\ncho bản thân hay đệ tử?", npc.avatar, menus);

                        break;

                    case NpcName.OSIN: {
                        StringBuilder sb = new StringBuilder();
                        if (zone.map.mapID == 7 || zone.map.mapID == 14 || zone.map.mapID == 0) {
                            sb.append("Sự kiện: Điểm Danh Nhận Quà Tại NPC Ôsin\n");
                            sb.append("- Thời gian điểm danh: Sau bảo trì các ngày Thứ 2, 4, 6.\n");
                            sb.append("- Thời gian nhận quà: Vào các ngày Thứ 3, 5, 7.\n");
                            sb.append("- Hướng dẫn: Đến gặp NPC Ôsin tại 3 làng để 'Điểm danh'. Số người điểm danh càng đông, quà vào ngày hôm sau càng lớn.\n");
                            sb.append("- Các mốc phần thưởng:\n");
                            sb.append("  + Mốc 200 người: Ngọc Rồng ngẫu nhiên (5-7 sao).\n");
                            sb.append("  + Mốc 300 người: 2 viên Sao Pha Lê ngẫu nhiên.\n");
                            sb.append("  + Mốc 400 người: x2 Vệ tinh ngẫu nhiên.\n");
                            sb.append("  + Mốc 500 người: x2 TNSM trong ngày nhận quà.\n");
                            sb.append("  + Mốc 1000 người: 5 Thỏi vàng.\n");
                            sb.append("- Lưu ý quan trọng: Phải tham gia 'Điểm danh' mới có thể 'Nhận quà'.\n");
                            sb.append("- Số người đã điểm danh: " + OsinCheckInEvent.getTotalTodayCheckIns());
//                            if (OsinCheckInEvent.) {
                            menus.add(new KeyValue(CMDMenu.OSIN_CHECKIN, "Điểm danh"));
//                            } else if (OsinCheckInEvent.isRewardDay()) {
//                                menus.add(new KeyValue(CMDMenu.OSIN_REWARD, "Nhận quà"));
//                            }
                        } else if (zone.map.mapID == 127) {
                            sb.append("Tôi có thể phù hộ cho bạn với 2 thỏi vàng").append("\n");
                            sb.append("Sau khi phù hộ bạn sẽ được tăng 10% Chỉ số").append("\n");
                            sb.append("Ngoài ra tôi có thể đưa bạn ra khỏi đây");
                            menus.add(new KeyValue(1109, "Phù hộ"));
                            menus.add(new KeyValue(585, "Rời khỏi đây"));
                            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        } else if (zone.map.mapID == 52) {
                            if (MainUpdate.now.getHour() == 12) {
                                sb.append("Bây giờ tôi bí mật..").append("\n");
                                sb.append("đuổi theo 2 tên đồ tể..").append("\n");
                                sb.append("Quý vị nào muốn đi theo thì xin mời !");
                                menus.add(new KeyValue(455, "OK"));
                                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            } else if (MainUpdate.now.getHour() == 14) {
                                sb.append("Bây giờ tôi bí mật..").append("\n");
                                sb.append("đuổi theo 2 tên đồ tể..").append("\n");
                                sb.append("Quý vị nào muốn đi theo thì xin mời !");
                                menus.add(new KeyValue(457, "OK"));
                                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            } else {
                                sb.append("Vào lúc 12h tôi bí mật..").append("\n");
                                sb.append("đuổi theo 2 tên đồ tể..").append("\n");
                                sb.append("Quý vị nào muốn đi theo thì xin mời !");
                                menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            }
                        } else {
                            menus.add(new KeyValue(1133, "Phù Hộ"));

                            if (this.flag == 9) {
                                sb.append(
                                        "Đừng vội xem thường Babiđây, ngay đến cha hắn là thần ma đạo sĩ Bibiđây khi còn sống cũng phải sợ hắn đấy!\n"
                                        + "Ta có thể Phù hộ để ngươi có để đánh tới 10% HP với giá 2 thỏi vàng");
                                menus.add(new KeyValue(456, "Hướng\ndẫn\nthêm"));
                                int floor = getCurrentNumberFloorInBaseBabidi();
                                if ((accumulatedPoint != null && accumulatedPoint.isMaxPoint()
                                        && floor + 1 < BaseBabidi.MAPS.length)
                                        || this.getSession().user.getRole() == 1) {
                                    menus.add(new KeyValue(459, "Xuống\nTầngdưới"));
                                }
                                if (this.getSession().user.getRole() == 1) {
                                    menus.add(new KeyValue(459, "Xuống\nTầngdưới"));
                                }
                            } else {
                                service.addBigMessage(npc.avatar, "Ngươi hãy về phe của mình mà thể hiện", (byte) 0,
                                        null, null);
                                return;
                            }
                        }
                        service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);
                    }
                    break;

                    case NpcName.KIBIT:
                        if (zone.map.isBaseBabidi()) {
                            if (flag == 9) {
                                menus.add(new KeyValue(458, "Về nhà"));
                            } else {
                                service.addBigMessage(npc.avatar, "Ngươi hãy về phe của mình mà thể hiện", (byte) 0,
                                        null,
                                        null);
                                return;
                            }
                        }
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        service.openUIConfirm(npc.templateId, "Ta có thể giúp gì ngươi ?", npc.avatar, menus);
                        break;

                    case NpcName.BABIDAY: {
                        if (flag == 9) {
                            service.addBigMessage((short) 4388, "Ngươi hãy về phe của mình mà thể hiện", (byte) 0, null,
                                    null);
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Khi nhận buff bạn sẽ tăng lượng sát thương lên 10% vào người và quái trong khu ")
                                .append("\b");
                        sb.append("Bọn Kaiô do con nhóc Ôsin cầm đầu đã có mặt tại đây..Hãy chuẩn bị").append("\b");
                        sb.append("'Tiếp khách' nhé!\n"
                                + "Ta có thể Phù hộ để ngươi có để đánh tới 10% HP với giá 2 thỏi vàng");

                        menus.add(new KeyValue(1133, "Phù Hộ"));
                        menus.add(new KeyValue(456, "Hướng\ndẫn\nthêm"));
                        int floor = getCurrentNumberFloorInBaseBabidi();
                        if ((accumulatedPoint != null && accumulatedPoint.isMaxPoint()
                                && floor + 1 < BaseBabidi.MAPS.length) || this.getSession().user.getRole() == 1) {
                            menus.add(new KeyValue(459, "Xuống\nTầngdưới"));
                        }
                        if (this.getSession().user.getRole() == 1) {
                            menus.add(new KeyValue(459, "Xuống\nTầngdưới"));
                        }
                        menus.add(new KeyValue(458, "Về nhà"));
                        service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);
                    }
                    break;

                    case NpcName.MR_POPO:
                        service.dialogMessage("Chức năng đang được phát triển.");
                        break;
                    case NpcName.TAPION:
                        if (this.zone.map.mapID == 126 && mapPhuHo != 126) {
                            menus.add(new KeyValue(1108, "Phù Hộ"));
                        }
                        menus.add(new KeyValue(1104, this.zone.map.mapID == 126 ? "Rời khỏi đây" : "OK"));
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        service.openUIConfirm(npc.templateId,
                                zone.map.mapID != 126 ? "Ác quỷ truyền thuyết Hirudegarn\n"
                                        + "đã thoát khỏi phong ấn ngàn năm\n"
                                        + "Hãy giúp tôi chế ngự nó?"
                                        : "Bạn có thể phù hộ với giá 2 thỏi vàng\n"
                                        + "Khi phù hộ bạn có thể đánh được 2% hp và tăng tỉ lệ nhặt trứng bư"
                                        + "\nTôi sẽ đưa bạn về",
                                npc.avatar, menus);
                        break;
                    case NpcName.LY_TIEU_NUONG:
                        menus.add(new KeyValue(1100, "Con Số May Mắn"));
                        menus.add(new KeyValue(1118, "Kéo Búa Bao"));
                        service.openUIConfirm(54, "MINI Game giải trí  ", (short) 3049, menus);
                        break;
                    case NpcName.WHIS:
                        if (zone.map.mapID == MapName.HANH_TINH_BILL) {
                            menus.add(new KeyValue(1150, "Khiêu chiến\n Lv." + currentLevelBossWhis));
                            menus.add(new KeyValue(1151, "BXH"));
                            menus.add(new KeyValue(1167, "BXH Lần trước"));
                            menus.add(new KeyValue(1162, "Shop hỗ trợ"));
                            menus.add(new KeyValue(1166, "Phần thưởng\nkhiêu chiến"));

                            service.openUIConfirm(npc.templateId, "Khiêu chiến với ta sao ?", npc.avatar, menus);
                        } else {
                            service.dialogMessage("Chức năng đang được phát triển.");
                        }
                        break;
                    case NpcName.BILL:
                        menus.add(new KeyValue(CMDMenu.FOOD_SHOP_PUDDING, "Cửa hàng\nPudding"));
                        menus.add(new KeyValue(CMDMenu.FOOD_SHOP_MI_LY, "Cửa hàng\nMì Ly"));
                        menus.add(new KeyValue(CMDMenu.FOOD_SHOP_XUC_XICH, "Cửa hàng\nXúc xích"));
                        menus.add(new KeyValue(CMDMenu.FOOD_SHOP_KEM_DAU, "Cửa hàng\nKem dâu"));
                        menus.add(new KeyValue(CMDMenu.FOOD_SHOP_SU_SHI, "Cửa hàng\nSushi"));
                        service.openUIConfirm(npc.templateId, "Cửa hàng đồ ăn", npc.avatar, menus);
                        // service.dialogMessage("Chức năng đang được phát triển.");
                        break;
                    case NpcName.NOI_BANH:
                        // StringBuilder content = new StringBuilder();
                        // menus.add(new KeyValue(CMDMenu.NAU_BANH_CHUNG, "Đổi\nBánh Chưng"));
                        // menus.add(new KeyValue(CMDMenu.NAU_BANH_TET, "Đổi\nBánh Tét"));
                        // menus.add(new KeyValue(CMDMenu.BANH_CHUNG_TAN_NIEN, "Đổi\nBánh Chưng\nTân
                        // Niên"));
                        // menus.add(new KeyValue(CMDMenu.BANH_TET_XUAN_VUI, "Đổi\nBánh Tét\nXuân
                        // Vui"));
                        // menus.add(new KeyValue(CMDMenu.HOP_QUA_XUAN, "Đổi\nHộp Quà\nXuân"));
                        // menus.add(new KeyValue(CMDMenu.HOP_QUA_TET, "Đổi\nHộp Quà\nTết"));
                        // menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                        // content.append("Cần 99 thúng gạo nếp + 99 lá dong + 99 nắm cơm nếp + 99 thịt
                        // heo + 1 lửa nấu bánh + 500tr để đổi Bánh chung").append("\n");
                        // content.append("Cần 99 thúng đậu xanh + 99 lá dong + 99 nắm cơm nếp + 99 thịt
                        // heo + 1 lửa nấu bánh + 500tr để đổi Bánh tét").append("\n");
                        // content.append("Cần 99 gạo tám thơm + 99 lá bàng tươi + 99 hạt dưa hồng + 99
                        // thịt ba chỉ + 99 lửa hồng tết + 100tr để đổi Bánh chưng Tân
                        // Niên").append("\n");
                        // content.append("Cần 5 Bánh chưng Tân Niên + 1 bao lì xì may mắn để đổi Hộp
                        // Quà Xuân Phú Quý").append("\n");
                        // content.append("Cần 5 Bánh tét Xuân Vui + 1 bao lì xì may mắn để đổi Hộp Quà
                        // Tết An Khang");
                        // service.openUIConfirm(npc.templateId, content.toString(), npc.avatar, menus);

                        service.dialogMessage("Chức năng đang được phát triển.");
                        break;
                    default:
                        service.dialogMessage("Chức năng đang được phát triển.");
                        break;
                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 97");
            logger.error("failed!", ex);
        }
    }

    public boolean isMeCanAttackOtherPlayer(Player cAtt) {
        return cAtt != null && !cAtt.isMiniDisciple() && select != null && select.template.type != 2
                && (select.template.type != 4 || cAtt.statusMe == 14 || cAtt.statusMe == 5)
                && ((((int) cAtt.typePk == 3 && (int) this.typePk == 3)
                || ((int) this.typePk == 5 || (int) cAtt.typePk == 5
                || ((int) this.typePk == 1 && (int) cAtt.typePk == 1))
                || ((int) this.typePk == 4 && (int) cAtt.typePk == 4)
                || (this.testCharId >= 0 && this.testCharId == cAtt.id)
                || (this.killCharId >= 0 && this.killCharId == cAtt.id && !this.isLang())
                || (cAtt.killCharId >= 0 && cAtt.killCharId == this.id && !this.isLang())
                || ((int) this.flag == 8 && (int) cAtt.flag != 0)
                || ((int) this.flag != 0 && (int) cAtt.flag == 8)
                || ((int) this.flag != (int) cAtt.flag && (int) this.flag != 0 && (int) cAtt.flag != 0))
                && cAtt.statusMe != 14)
                && cAtt.statusMe != 5;
    }

    public boolean isLang() {
        return zone.map.mapID == 1 || zone.map.mapID == 27 || zone.map.mapID == 72 || zone.map.mapID == 10
                || zone.map.mapID == 17 || zone.map.mapID == 22 || zone.map.mapID == 32 || zone.map.mapID == 38
                || zone.map.mapID == 43 || zone.map.mapID == 48;
    }

    public void master(Npc npc) {
        if (taskMain != null) {
            if (taskMain.id == 13 && taskMain.index == 3) {
                String text = TaskText.TASK_13_FINISH + "\n" + TaskText.TASK_14_0[gender];
                service.openUISay(npc.templateId, text, npc.avatar);
                int itemID = 68;
                if (gender == 1) {
                    itemID = 81;
                } else if (gender == 2) {
                    itemID = 89;
                }
                Item item = new Item(itemID);
                item.setDefaultOptions();
                item.quantity = 1;
                addItem(item);
                updateTask(14);
                return;
            }
            if (taskMain.id == 14 && taskMain.index == 2) {
                String text = TaskText.TASK_14_FINISH + "\n" + TaskText.TASK_15_0;
                service.openUISay(npc.templateId, text, npc.avatar);
                int index = getIndexBagById(85);
                if (index != -1) {
                    removeItem(index, 1);
                }
                updateTask(15);
                return;
            }
        }
        if (studying != null) {
            long now = System.currentTimeMillis();
            if (now >= studying.studyTime) {
                Skill skill = Skills.getSkill((byte) studying.id, (byte) studying.level);
                Skill hSkill = null;
                int index = -1;
                int i = 0;
                for (Skill sk : this.skills) {
                    if (sk.template.id == skill.template.id) {
                        index = i;
                        hSkill = sk;
                        break;
                    }
                    i++;
                }
                try {
                    if (hSkill != null) {
                        if (hSkill.point + 1 == skill.point) {
                            this.skills.set(index, skill.clone());
                        } else {
                            service.openUISay(npc.id, "Con đã học kỹ năng này", npc.avatar);
                            studying = null;
                            return;
                        }
                    } else {
                        this.skills.add(skill.clone());
                    }
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 96");
                    logger.error("failed!", e);
                }
                service.loadSkill();
                service.openUISay(npc.templateId,
                        String.format(Language.LEARN_SUCCESS, skill.template.name, skill.point), npc.avatar);
                studying = null;
                return;
            }
        }
        menus.add(new KeyValue(530, "Nhiệm vụ"));
        String text = "Con muốn làm gì?";
        if (studying != null) {
            Skill skill = Skills.getSkill((byte) studying.id, (byte) studying.level);
            text = String.format(Language.STUDYING, skill.template.name,
                    Utils.getTimeAgo((int) ((studying.studyTime - System.currentTimeMillis()) / 1000)));
            menus.add(new KeyValue(532, "Học\nCấp tốc\n" + skill.price + " ngọc"));
        } else {
            menus.add(new KeyValue(531, "Học\nkỹ năng"));
        }
        service.openUIConfirm(npc.templateId, text, npc.avatar, menus);
    }

    public void confirmKeyValue(KeyValue<Integer, String> keyValue, Npc npc) {
        if (this.isDead) {
            return;
        }
        if (isTrading) {
            return;
        }
        int menuID = keyValue.key;
        String menuName = keyValue.value;
        switch (menuID) {
            case CMDMenu.TELE_NR_NAMEC_1_SAO:
                int indexItem = getIndexBagById(ItemName.GOI_10_RADA_DO_NGOC);
                if (indexItem < 0) {
                    return;
                } else {
                    teleportToNrNamec(this);
                }
                break;
            case CMDMenu.TELEPORT_TRAI_DAT:
                teleport(MapName.TRAM_TAU_VU_TRU);
                break;

            case CMDMenu.TELEPORT_NAMEC:
                teleport(MapName.TRAM_TAU_VU_TRU_2);
                break;

            case CMDMenu.TELEPORT_XAYDA:
                teleport(MapName.TRAM_TAU_VU_TRU_3);
                break;

            case CMDMenu.TELEPORT_SIEU_THI:
                teleport(MapName.SIEU_THI);
                break;

            case CMDMenu.TELEPORT_NAPPA:
                teleport(MapName.THUNG_LUNG_NAPPA);
                break;

            case CMDMenu.TELEPORT_COLD:
                teleport(MapName.RUNG_BANG);
                break;

            case CMDMenu.TELEPORT_VEGETA:
                teleport(MapName.THANH_PHO_VEGETA);
                break;

            case CMDMenu.TELEPORT_DAO_KAME:
                teleport(MapName.DAO_KAME);
                break;

            case CMDMenu.STORE:
                if (taskMain.id > 7) {
                    if (npc.templateId == NpcName.BUNMA && this.gender != 0) {
                        service.openUISay(npc.templateId, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất",
                                npc.avatar);
                        return;
                    }
                    if (npc.templateId == NpcName.DENDE && this.gender != 1) {
                        service.openUISay(npc.templateId, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Na mếc", npc.avatar);
                        return;
                    }
                    if (npc.templateId == NpcName.APPULE && this.gender != 2) {
                        service.openUISay(npc.templateId,
                                "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán cho người Xayda thôi",
                                npc.avatar);
                        return;
                    }
                    this.shop = Shop.getShop(npc.templateId);
                    if (shop != null) {
                        shop.setNpc(npc);
                        service.viewShop(shop);
                    }
                }
                break;

            case CMDMenu.TIEM_HOT_TOC:
                this.shop = Shop.getShop(-2);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;

            case CMDMenu.BUY_FROM_PLAYER:
                buyFromPlayer((KeyValue) keyValue);
                break;

            case CMDMenu.CLAN_SHOP: {
                shop = Shop.getShop(npc.templateId);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
            }
            break;
            case CMDMenu.GOLD_BAR_SHOP: {
                // khoidtif (this.session.user.getActivated() != 1) {
                // service.sendThongBao("Bạn cần kích hoạt tài khoản để sử dụng tính năng này");
                // return;
                // }
                shop = Shop.getShop(54);
                // shop = Shop.getShop(npc.templateId);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
            }
            break;
            case CMDMenu.FOOD_SHOP_PUDDING:
                if (this.session.user.getActivated() != 1) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để sử dụng tính năng này");
                    return;
                }
                shop = Shop.getShop(-663);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;
            case CMDMenu.FOOD_SHOP_MI_LY:
                if (this.session.user.getActivated() != 1) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để sử dụng tính năng này");
                    return;
                }
                shop = Shop.getShop(-666);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;
            case CMDMenu.FOOD_SHOP_XUC_XICH:
                if (this.session.user.getActivated() != 1) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để sử dụng tính năng này");
                    return;
                }
                shop = Shop.getShop(-664);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;
            case CMDMenu.FOOD_SHOP_KEM_DAU:
                if (this.session.user.getActivated() != 1) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để sử dụng tính năng này");
                    return;
                }
                shop = Shop.getShop(-665);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;
            case CMDMenu.FOOD_SHOP_SU_SHI:
                if (this.session.user.getActivated() != 1) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để sử dụng tính năng này");
                    return;
                }
                shop = Shop.getShop(-667);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;
            case CMDMenu.FOOD_SHOP: {
                if (this.session.user.getActivated() != 1) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để sử dụng tính năng này");
                    return;
                }
                shop = Shop.getShop(55);
                // shop = Shop.getShop(npc.templateId);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
            }
            break;
            case CMDMenu.UPGRADE_ITEM:
                combine = CombineFactory.getCombine(CombineType.NANG_CAP);
                combine.setNpc(npc);
                combine.setPlayer(this);
                combine.showTab();
                break;

            case CMDMenu.NHAP_DA:
                combine = CombineFactory.getCombine(CombineType.NHAP_DA);
                combine.setNpc(npc);
                combine.setPlayer(this);
                combine.showTab();
                break;

            case CMDMenu.NHAP_NGOC_RONG:
                this.combine = CombineFactory.getCombine(CombineType.NHAP_NGOC);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;

            case CMDMenu.EP_SAO_TRANG_BI:
                this.combine = CombineFactory.getCombine(CombineType.EP_PHA_LE);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;

            case CMDMenu.PHA_LE_HOA_BY_GEM:
                this.combine = CombineFactory.getCombine(CombineType.PHA_LE_HOA);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case CMDMenu.DOI_DO_HUY_DIET:
                this.combine = CombineFactory.getCombine(CombineType.HUY_DIET);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case CMDMenu.DOI_DO_KICH_HOAT:
                this.combine = CombineFactory.getCombine(CombineType.KICH_HOAT);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case CMDMenu.TACH_DO_KICH_HOAT:
                this.combine = CombineFactory.getCombine(CombineType.TACH_DO_KICH_HOAT);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case CMDMenu.NANG_CAP_2:
                this.combine = CombineFactory.getCombine(CombineType.NANG_CAP_2);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case CMDMenu.GHEP_DA:
                this.combine = CombineFactory.getCombine(CombineType.GHEP_DA);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case CMDMenu.TACH_DO_THAN_LINH:
                this.combine = CombineFactory.getCombine(CombineType.TACH_DO);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;

            case CMDMenu.NANG_CAP_PORATA: {
                this.combine = CombineFactory.getCombine(CombineType.NANG_PORATA);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
            }
            break;

            case CMDMenu.MO_CHI_SO_PORATA2: {
                this.combine = CombineFactory.getCombine(CombineType.NANG_OPTION_PORATA);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
            }
            break;

            case CMDMenu.CHUYEN_HOA_BANG_VANG:
            case CMDMenu.CHUYEN_HOA_BANG_NGOC:
                byte type = ChuyenHoa.BANG_VANG;
                if (menuID == CMDMenu.CHUYEN_HOA_BANG_VANG) {
                    type = ChuyenHoa.BANG_NGOC;
                }
                combine = CombineFactory.getCombine(CombineType.CHUYEN_HOA);
                combine.setNpc(npc);
                combine.setPlayer(this);
                ((ChuyenHoa) combine).setType(type);
                combine.showTab();
                break;

            case CMDMenu.TELEPORT_NGU_HANH_SON: {
                TMap map = MapManager.getInstance().getMap(MapName.NGU_HANH_SON_2);
                this.x = 60;
                this.y = 384;
                zone.leave(this);
                int zoneID = map.getZoneID();
                map.enterZone(this, zoneID);
            }
            break;

            case CMDMenu.TANG_QUA_HONG_DAO: {
                Event.exchange(0, this);
            }
            break;

            case CMDMenu.TANG_QUA_HONG_DAO_CHIN: {
                Event.exchange(1, this);
            }
            break;

            case CMDMenu.TELEPORT_ARU: {
                TMap map = MapManager.getInstance().getMap(MapName.LANG_ARU);
                this.x = 532;
                this.y = 432;
                zone.leave(this);
                int zoneID = map.getZoneID();
                map.enterZone(this, zoneID);
            }
            break;

            case CMDMenu.GIAI_PHONG_AN: {
                Event.exchange(2, this);
            }
            break;

            case CMDMenu.THINH_KINH: {
                Event.exchange(3, this);
            }
            break;

            case CMDMenu.REPORT: {
                int playerID = ((Integer) keyValue.elements[0]).intValue();
                Player _player = zone.findCharByID(playerID);
                if (_player != null) {
                    String text = String.format("Báo cáo %s", _player.name);
                    service.serverMessage2(String.format("Báo cáo %s", _player.name));
                    setCaptcha(RandomStringUtils.randomAlphanumeric(5).toLowerCase());
                    setPlayerReportedID(_player.id);
                    setPlayerReportedName(_player.name);
                    inputDlg = new InputDialog(CMDTextBox.REPORT, text,
                            new TextField("Vấn đề báo cáo", TextField.INPUT_TYPE_ANY),
                            new TextField("Nội dung cáo", TextField.INPUT_TYPE_ANY),
                            new TextField(String.format("Nhập mã: %s", getCaptcha()), TextField.INPUT_TYPE_ANY));
                    inputDlg.setService(service);
                    inputDlg.show();
                }
            }
            break;

            case CMDMenu.MUA_CAI_TRANG: {
                int playerID = ((Integer) keyValue.elements[0]).intValue();
                Player _player = zone.findCharByID(playerID);
                if (_player != null) {
                    Item item = _player.itemBody[5];
                    if (item != null) {
                        if (item.isCantSaleForPlay) {
                            return;
                        }
                        int gem = item.template.buyGem;
                        if (gem > 0) {
                            if (item.template.gender >= 3 || item.template.gender == this.gender) {
                                int km = 5;
                                int hoaHong = 20;
                                int giaSauKhiGiam = gem - ((gem * km) / 100);
                                int tienHoaHong = gem * hoaHong / 100;
                                menus.clear();
                                menus.add(new KeyValue(121, "Đồng ý", _player.id));
                                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                                String info = String.format(
                                        "Bạn có muốn mua %s từ %s không?\nGiá của hàng %d ngọc\nGiá tại đây là %d ngọc xanh\nBạn cũng có thể nhận được %d hồng ngọc khi người khác mua từ bạn (Cải trang của bạn sẽ không bị mất)",
                                        item.template.description, _player.name, gem, giaSauKhiGiam, tienHoaHong);
                                service.openUIConfirm(NpcName.CON_MEO, info, getPetAvatar(), menus);
                            }
                        }
                    } else {
                        service.serverMessage2("Có lỗi xảy ra");
                    }
                }
            }
            break;

            case CMDMenu.DUONG_TANG_REWARD: {
                StringBuilder sb3 = new StringBuilder();
                int p = getMerit(ItemName.VONG_KIM_CO);
                sb3.append(String.format("A mi phò phò, thí chủ đang có %d điểm công đức, xin hãy chọn 1 phần quà", p))
                        .append("\n");
                sb3.append("1.000 điểm cải trang thành Bát Giới (dành riêng đệ tử)").append("\n");
                sb3.append("2.000 điểm cải trang thành Tôn Ngộ Không (dành riêng đệ tử)");
                menus.add(new KeyValue(CMDMenu.DOI_1000_DIEM, "1.000"));
                menus.add(new KeyValue(CMDMenu.DOI_2000_DIEM, "2.000"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(npc.templateId,
                        sb3.toString(),
                        npc.avatar, menus);
            }
            break;

            case CMDMenu.DOI_1000_DIEM:
                Event.exchange(4, this);
                break;

            case CMDMenu.DOI_2000_DIEM:
                Event.exchange(5, this);
                break;

            case 300:
            case 301:
            case 302:
                int playerId2 = ((Integer) keyValue.elements[0]).intValue();
                Player _player2 = zone.findCharByID(playerId2);
                if (_player2 != null) {
                    if (testCharId != -9999) {
                        service.serverMessage2("Bạn đang thách đấu người khác");
                        return;
                    }
                    if (_player2.isDead) {
                        service.serverMessage2("Nhân vật này đã kiệt sức");
                        return;
                    }
                    if (_player2.testCharId != -9999) {
                        service.serverMessage2("Người này đang thách đấu người khác");
                        return;
                    }
                    int gold = 1000;
                    if (menuID == 301) {
                        gold = 10000;
                    }
                    if (menuID == 302) {
                        gold = 100000;
                    }
                    betAmount = gold;
                    _player2.invite.addCharInvite(Invite.THACH_DAU, this.id, 25000);
                    _player2.service.playerVsPlayer((byte) 3, this.id, gold,
                            String.format("%s (Sức mạnh: %s) Muốn thách đấu với bạn cược %s vàng?", this.name,
                                    Utils.formatNumber(info.getTotalPotential()), Utils.currencyFormat(gold)));
                } else {
                    this.service.sendThongBao("Người này không còn trong khu vực");
                }
                break;

            case 303: {
                int playerId3 = ((Integer) keyValue.elements[0]).intValue();
                Player _player3 = zone.findCharByID(playerId3);
                if (_player3 != null) {
                    BaiSu.accept(this, _player3);
                }
                break;
            }
            case 200:
                menus.add(new KeyValue(2000, "Bùa\nDùng\n1 giờ"));
                menus.add(new KeyValue(2001, "Bùa\nDùng\n8 giờ"));
                menus.add(new KeyValue(2002, "Bùa\nDùng\n1 tháng"));
                service.openUIConfirm(npc.templateId,
                        "Bùa của ta rất lợi hại. Mua xong có tác dụng ngay nhé, nhớ tranh thủ sử dụng, thoát game phí lắm. Mua càng nhiều thời gian giá càng rẻ!",
                        npc.avatar, menus);
                break;

            case 402:
                menus.add(new KeyValue(412, "Đồng ý\nluyện tập"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Không\nđồng ý"));
                service.openUIConfirm(npc.templateId, "Con có chắc muốn luyện tập với ta?", npc.avatar, menus);
                break;

            case 404:
                menus.add(new KeyValue(413, "Đồng ý\nluyện tập"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Không\nđồng ý"));
                service.openUIConfirm(npc.templateId, "Con có chắc muốn tập luyện với Yajirô?", npc.avatar, menus);
                break;

            case 403:
                menus.add(new KeyValue(410, "Đồng ý\ngiao đấu"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Không\nđồng ý"));
                service.openUIConfirm(npc.templateId,
                        "Con có chắc muốn thách đấu?\nNếu thắng ta sẽ được tập luyện với Yajirô, tăng 40 sức mạnh mỗi phút",
                        npc.avatar, menus);
                break;

            case 405:
                menus.add(new KeyValue(411, "Đồng ý\ngiao đấu"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Không\nđồng ý"));
                service.openUIConfirm(npc.templateId, "Con có chắc muốn thách đấu với Yajirô?", npc.avatar, menus);
                break;

            case 410:
                if (zone.getBoss(Karin.class) == null) {
                    Karin karin = new Karin(false);
                    karin.setLocation(zone);
                }
                break;

            case 411:
                Yajiro boss = ((KarinTower) zone).yajiro;
                if (boss != null) {
                    boss.revival(100);
                    boss.setTrainning(false);
                    boss.testCharId = this.id;
                    this.testCharId = boss.id;
                    boss.setTypePK((byte) 3);
                    setTypePK((byte) 3);
                    if (boss.zone == null) {
                        boss.setLocation(zone);
                    }
                }
                break;

            case 412:
                if (zone.getBoss(Karin.class) == null) {
                    Karin karin2 = new Karin(true);
                    karin2.setLocation(zone);
                }
                break;

            case 413:
                Yajiro boss2 = ((KarinTower) zone).yajiro;
                if (boss2 != null) {
                    boss2.revival(100);
                    boss2.setTrainning(true);
                    boss2.testCharId = this.id;
                    this.testCharId = boss2.id;
                    boss2.setTypePK((byte) 3);
                    setTypePK((byte) 3);
                    if (boss2.zone == null) {
                        boss2.setLocation(zone);
                    }
                }
                break;

            case 420:
                achievements.get(0).upadateCount(info.power);// Vệ binh hoàng gia
                achievements.get(1).upadateCount(info.power);// sức mạnh siêu cấp
                achievements.get(2).upadateCount(magicTree.level);// Nông dân chăm chỉ
                achievements.get(8).upadateCount(timePlayed / 60 / 60);// Hoạt động trăm chỉ
                achievements.get(11).upadateCount(0);// Lần đầu nạp ngọc
                service.achievement((byte) 0, (byte) -1);
                break;
            case 421:
                service.dialogMessage(
                        "Xin chào, Nhập giftcode tại trang chủ ngocrongbaby.com rồi quay lại đây nhận quà nhé");
                break;
            case 20230:
                inputDlg = new InputDialog(CMDTextBox.SELL_GOLD_BAR, "Nhập số lượng thỏi vàng muốn bán",
                        new TextField("Nhập Số lượng"));
                inputDlg.setService(service);
                inputDlg.show();
                break;
            case 6868:
                UserData user = null;
                try {
                    Optional<UserData> userData = GameRepository.getInstance().user.findById(this.session.user.getId());
                    if (userData.isPresent()) {
                        user = userData.get();
                    } else {
                        service.sendThongBao("Không tìm thấy thong tin user, vui lòng liên hệ admin để giải quyết");
                    }
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 95");
                    e.printStackTrace();
                }
                if (user != null) {
                    withdrawGoldBar(user);
                } else {
                    service.sendThongBao("Không tìm thấy thong tin user, vui lòng liên hệ admin để giải quyết");
                }
                break;
            case 6869:
                if (myDisciple == null) {
                    createDisciple(0);
                } else {
                    service.sendThongBao("Bạn đã có đệ tử rồi");
                    return;
                }
                break;
            case 6870:
                if (this.diamond < 100000) {
                    addDiamond(500000);
                    service.sendThongBao("Bạn đã nhận được 500k ngọc xanh");
                } else {
                    service.sendThongBao("Bạn vẫn còn ngọc xanh, hãy quay lại khi còn dưới 100k");
                    return;
                }
                break;
            case 6871: {
                inputDlg = new InputDialog(CMDTextBox.GIFT_CODE, "Nhập Giftcode muốn dùng",
                        new TextField("Nhập Giftcode"));
                inputDlg.setService(service);
                inputDlg.show();
                break;
            }
            case 455: {
                BaseBabidi baseBabidi = MapManager.getInstance().baseBabidi;
                if (baseBabidi != null) {
                    this.x = 200;
                    this.y = 0;
                    TMap map = MapManager.getInstance().getMap(BaseBabidi.MAPS[0]);
                    zone.leave(this);
                    int zoneId = map.getZoneID();
                    setTeleport((byte) (ship == 1 ? 3 : 1));
                    map.enterZone(this, zoneId);
                    setTeleport((byte) 0);
                    this.y = 336;
                    SpaceshipRoom z = (SpaceshipRoom) zone;
                    RandomCollection<Integer> rd = new RandomCollection<>();
                    rd.add(1 + Math.abs(z.teamMabu), 0);
                    rd.add(1 + Math.abs(z.teamKaiosin), 1);
                    byte flag = (byte) (rd.next() == 0 ? 9 : 10);
                    setFlag(flag);
                    accumulatedPoint = new PowerInfo("TL", 0, 10, 5);
                    service.setPowerInfo(accumulatedPoint);
                } else {
                }
            }
            break;
            case 456: {
                StringBuilder sb = new StringBuilder();
                sb.append("Tại khu vực này, ta đang dùng ma pháp phong ấn").append("\b");
                sb.append("Dù các ngươi có mạnh đến đâu").append("\b");
                sb.append("Cũng sẽ trở thành yếu đuối như nhau");
                sb.append("\n");
                sb.append("Chỉ có ta với nhóc Ôsin mới giải được ma pháp này").append("\b");
                sb.append("Khi đó sức mạnh của ngươi sẽ được").append("\b");
                sb.append("giải phóng theo điểm tích lũy (TL)");
                sb.append("\n");
                sb.append("Khi đủ điểm tích lũy bằng cách").append("\b");
                sb.append("hạ lẫn nhau hoặc hạ boss").append("\b");
                sb.append("ta sẽ đưa ngươi xuống tầng tiếp theo");
                service.openUISay(npc.templateId, sb.toString(), (short) 4388);
            }
            break;
            case 457: {
                TMap map = MapManager.getInstance().getMap(MapName.CONG_PHI_THUYEN_2);
                zone.leave(this);
                int zoneId = map.randomZoneID();
                map.enterZone(this, zoneId);
                this.x = 50;
                this.y = 0;
                this.zone.service.setPosition(this, (byte) 0);
                break;
            }
            case 458: {
                goHome();
            }

            break;

            case 459: {
                if (MapManager.getInstance().baseBabidi != null) {
                    if (accumulatedPoint != null && accumulatedPoint.isMaxPoint()) {
                        accumulatedPoint.setPoint(0);
                        MapManager.getInstance().baseBabidi.nextFloor(this);
                    }
                }
            }
            break;

            case 500: {
                String text = "Sức mạnh của con đã đạt mức tối đa";
                if (info.numberOpenLimitedPower + 1 >= PowerLimitMark.limitMark.size()) {
                    menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                } else {
                    PowerLimitMark limit = PowerLimitMark.limitMark.get(info.numberOpenLimitedPower + 1);
                    text = String.format(
                            "Ta sẽ truyền năng lượng giúp con mở giới hạn sức mạnh\ncủa bản thân lên %s.\nLưu ý: từ 40 tỉ trở lên sức mạnh của con sẽ tăng chậm đáng kể",
                            Utils.formatNumber(limit.power + 1));
                    // menus.add(new KeyValue(502, "Nâng\nGiới hạn\nSức mạnh"));
                    menus.add(new KeyValue(503, "Nâng giới hạn\nSức mạnh\n500Tr Vàng"));
                    menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                }
                service.openUIConfirm(npc.templateId, text, npc.avatar, menus);
                break;
            }

            case 501: {
                String text = "Con chưa có đệ tử";
                if (myDisciple == null) {
                    menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                } else if (myDisciple.info.numberOpenLimitedPower + 1 >= PowerLimitMark.limitMark.size()) {
                    text = "Sức mạnh của đệ tử đã đạt mức tối đa";
                    menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                } else {
                    PowerLimitMark limit = PowerLimitMark.limitMark.get(myDisciple.info.numberOpenLimitedPower + 1);
                    text = String.format(
                            "Ta sẽ truyền năng lượng giúp con mở giới hạn sức mạnh\ncủa đệ tử lên %s.\nLưu ý: từ 40 tỉ trở lên sức mạnh của đệ tử sẽ tăng chậm đáng kể",
                            Utils.formatNumber(limit.power + 1));
                    menus.add(new KeyValue(504, "Nâng ngay\ncho đệ tử\n500tr vàng"));
                    menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                }
                service.openUIConfirm(npc.templateId, text, npc.avatar, menus);
                break;
            }

            case 502: {
                ItemTime itm = getItemTime(6);
                if (itm != null) {
                    service.openUISay(npc.templateId, "Ta đang truyền năng lựng cho con, con hãy cố gắng chờ.",
                            npc.avatar);
                    return;
                }
                ItemTime item = new ItemTime(ItemTimeName.MO_GIOI_HAN_SUC_MANH, 3783, 60 * 60, true);
                addItemTime(item);
                break;
            }

            case 503: {
                if (this.gold < 500000000) {
                    service.sendThongBao(String.format("Bạn không đủ vàng, cần ít nhất 500tr vàng "));
                    return;
                }
                ItemTime itm = getItemTime(6);
                if (itm != null) {
                    synchronized (itemTimes) {
                        itemTimes.remove(itm);
                        itm.seconds = 0;
                        service.setItemTime(itm);
                    }
                }
                info.numberOpenLimitedPower++;
                subGold(500000000);
                PowerLimitMark limit = PowerLimitMark.limitMark.get(info.numberOpenLimitedPower);
                info.powerLimitMark = limit;
                service.sendThongBao(
                        String.format("Giớ hạn sức mạnh đã năng lên %s", Utils.formatNumber(limit.power + 1)));
            }
            break;

            case 504:
                if (myDisciple != null) {
                    if (this.gold < 500000000) {
                        service.sendThongBao(String.format("Bạn không đủ vàng"));
                        return;
                    }
                    myDisciple.info.numberOpenLimitedPower++;
                    subGold(500000000);
                    PowerLimitMark limit = PowerLimitMark.limitMark.get(myDisciple.info.numberOpenLimitedPower);
                    myDisciple.info.powerLimitMark = limit;
                    service.sendThongBao(String.format("Giớ hạn sức mạnh của đệ tử đã năng lên %s",
                            Utils.formatNumber(limit.power + 1)));
                }
                break;
            case 5200:
                menus.add(new KeyValue(5201, "Quay\n1 lần"));
                menus.add(new KeyValue(5205, "Quay\n5 lần"));
                menus.add(new KeyValue(52020, "Quay\n20 lần"));
                menus.add(new KeyValue(520100, "Quay\n100 lần"));
                service.openUIConfirm(npc.templateId,
                        String.format(
                                "Số lượt quay của bạn là: %d\nHãy tiếp tục quay để nhận nhiều phần thưởng giá trị",
                                numberVongQuay),
                        npc.avatar, menus);
                break;
            case 520:
                if (this.session.user.getActivated() == 0) {
                    service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
                    return;
                }
                menus.add(new KeyValue(5200, "Bắt đầu"));
                menus.add(new KeyValue(522, "BXH\nVòng Quay"));
                menus.add(new KeyValue(523, "Nhận\nThưởng Mốc"));
                // menus.add(new KeyValue(521, "Vòng quay\nĐặc biệt"));
                if (boxCrackBall.size() > 0) {
                    menus.add(new KeyValue(524, String.format("Rương phụ\nĐang có %d món", boxCrackBall.size())));
                    menus.add(new KeyValue(5241, "Xóa tất cả\nrương phụ"));
                    menus.add(new KeyValue(5242, "Nhận vàng\nXóa vật phẩm HSD"));
                }

                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(npc.templateId,
                        "Giá mỗi lượt quay là 1 thỏi vàng\nQuay càng nhiều, tỉ lệ trúng càng cao",
                        npc.avatar, menus);
                break;
            case 522:
                Top topVQTD = Top.getTop(Top.TOP_VQTD);
                topVQTD.show(this);
                break;
            case 523:
                menus.clear();
                if (rewardMoc < 100) {
                    menus.add(new KeyValue(CMDMenu.MOC_100_VQTD, "Phần thưởng\nmốc 100"));
                } else if (rewardMoc < 200) {
                    menus.add(new KeyValue(CMDMenu.MOC_200_VQTD, "Phần thưởng\nmốc 200"));
                } else if (rewardMoc < 500) {
                    menus.add(new KeyValue(CMDMenu.MOC_500_VQTD, "Phần thưởng\nmốc 500"));
                } else if (rewardMoc < 1000) {
                    menus.add(new KeyValue(CMDMenu.MOC_1000_VQTD, "Phần thưởng\nmốc 1000"));
                } else if (rewardMoc < 2000) {
                    menus.add(new KeyValue(CMDMenu.MOC_2000_VQTD, "Phần thưởng\nmốc 2000"));
                } else if (rewardMoc < 3000) {
                    menus.add(new KeyValue(CMDMenu.MOC_3000_VQTD, "Phần thưởng\nmốc 3000"));
                } else if (rewardMoc < 5000) {
                    menus.add(new KeyValue(CMDMenu.MOC_5000_VQTD, "Phần thưởng\nmốc 5000"));
                }
                // else if (rewardMoc < 7000) {
                // menus.add(new KeyValue(CMDMenu.MOC_7000_VQTD, "Phần thưởng\nmốc 7000"));
                // } else if (rewardMoc < 10000) {
                // menus.add(new KeyValue(CMDMenu.MOC_10000_VQTD, "Phần thưởng\nmốc 10000"));
                // } else if (rewardMoc < 13000) {
                // menus.add(new KeyValue(CMDMenu.MOC_13000_VQTD, "Phần thưởng\nmốc 13000"));
                // } else if (rewardMoc < 20000) {
                // menus.add(new KeyValue(CMDMenu.MOC_20000_VQTD, "Phần thưởng\nmốc 20000"));
                // } else if (rewardMoc < 25000) {
                // menus.add(new KeyValue(CMDMenu.MOC_25000_VQTD, "Phần thưởng\nmốc 25000"));
                // }
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(npc.templateId,
                        String.format("Số lượt quay của bạn là: %d\nHãy chọn mốc phần thưởng bạn muốn nhận?",
                                numberVongQuay),
                        npc.avatar, menus);
                break;
            case CMDMenu.MOC_100_VQTD:
                if (rewardMoc >= 100) {
                    service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
                    return;
                }
                getRewardMoc(100);
                break;
            case CMDMenu.MOC_200_VQTD:
                if (rewardMoc >= 200) {
                    service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
                    return;
                }
                getRewardMoc(200);
                break;
            case CMDMenu.MOC_500_VQTD:
                if (rewardMoc >= 500) {
                    service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
                    return;
                }
                getRewardMoc(500);
                break;
            case CMDMenu.MOC_1000_VQTD:
                if (rewardMoc >= 1000) {
                    service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
                    return;
                }
                getRewardMoc(1000);
                break;
            case CMDMenu.MOC_2000_VQTD:
                if (rewardMoc >= 2000) {
                    service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
                    return;
                }
                getRewardMoc(2000);
                break;
            case CMDMenu.MOC_3000_VQTD:
                if (rewardMoc >= 3000) {
                    service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
                    return;
                }
                getRewardMoc(3000);
                break;
            case CMDMenu.MOC_5000_VQTD:
                if (rewardMoc >= 5000) {
                    service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
                    return;
                }
                getRewardMoc(5000);
                break;
            // case CMDMenu.MOC_7000_VQTD:
            // if (rewardMoc >= 7000) {
            // service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
            // return;
            // }
            // getRewardMoc(7000);
            // break;
            // case CMDMenu.MOC_10000_VQTD:
            // if (rewardMoc >= 10000) {
            // service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
            // return;
            // }
            // getRewardMoc(10000);
            // break;
            // case CMDMenu.MOC_13000_VQTD:
            // if(rewardMoc >= 13000) {
            // service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
            // return;
            // }
            // getRewardMoc(13000);
            // break;
            // case CMDMenu.MOC_20000_VQTD:
            // if (rewardMoc >= 20000) {
            // service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
            // return;
            // }
            // getRewardMoc(20000);
            // break;
            // case CMDMenu.MOC_25000_VQTD:
            // if (rewardMoc >= 25000) {
            // service.serverMessage(" Bạn đã nhận thưởng mốc này rồi ");
            // return;
            // }
            // getRewardMoc(25000);
            // break;

            case CMDMenu.OPTION_LAZE_15:
                selectOptionCaiTrangTop5000(197);
                break;
            case CMDMenu.OPTION_TU_SAT_15:
                selectOptionCaiTrangTop5000(196);
                break;
            case CMDMenu.OPTION_SĐCM_10:
                selectOptionCaiTrangTop5000(5);
                break;
            case CMDMenu.MON_TLKH_TĐ:
                openDoTLKHRandom(0);
                break;
            case CMDMenu.MON_TLKH_NM:
                openDoTLKHRandom(1);
                break;
            case CMDMenu.MON_TLKH_XD:
                openDoTLKHRandom(2);
                break;
            case 5201: {
                if (boxCrackBall.size() >= 100) {
                    service.sendThongBao("Rương quá đầy, vui lòng thử lại sau");
                    return;
                }
                luckyRoundVQTD((byte) 1);
                break;
            }
            case 5205: {
                if (boxCrackBall.size() >= 100) {
                    service.sendThongBao("Rương quá đầy, vui lòng thử lại sau");
                    return;
                }
                if (boxCrackBall.size() > 95) {
                    service.serverMessage("Bạn cần nhận hết vật phẩm để có thể tiếp tục quay");
                    return;
                }
                luckyRoundVQTD((byte) 5);
                break;
            }
            case 52020: {
                if (boxCrackBall.size() >= 100) {
                    service.sendThongBao("Rương quá đầy, vui lòng thử lại sau");
                    return;
                }
                if (boxCrackBall.size() > 80) {
                    service.serverMessage("Bạn cần nhận hết vật phẩm để có thể tiếp tục quay");
                    return;
                }
                luckyRoundVQTD((byte) 20);
                break;
            }
            case 520100: {
                if (boxCrackBall.size() > 100) {
                    service.sendThongBao("Rương quá đầy, vui lòng thử lại sau");
                    return;
                }
                if (boxCrackBall.size() != 0) {
                    service.serverMessage("Bạn cần nhận hết vật phẩm để có thể quay 100 lần");
                    return;
                }
                luckyRoundVQTD((byte) 100);
                break;
            }
            case 524:
                viewBoxCrackBall();
                break;
            case 5241: {
                menus.clear();
                menus.add(new KeyValue(52410, "Đồng Ý"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(npc.templateId, "Bạn có muốn xóa hết vật phẩm ở rương phụ không ?", npc.avatar,
                        menus);
                break;
            }
            case 5242: {
                for (int i = boxCrackBall.size() - 1; i >= 0; i--) {
                    Item item = boxCrackBall.get(i);
                    if (item != null) {
                        if (item.template.id == 190) {
                            addItem(item);
                            deleteItemBoxCrackBall(i, false);
                        } else {
                            boolean flag = false;
                            for (ItemOption op : item.options) {
                                if (op != null && op.optionTemplate.id == 93) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (flag) {
                                deleteItemBoxCrackBall(i, false);
                            }
                        }
                    }
                }
                viewBoxCrackBall();
                break;
            }
            case 52410: {
                boxCrackBall.clear();
                service.serverMessage("Vật phẩm trong rương phụ đã được xóa !");
                break;
            }
            case 525: {
                if (boxCrackBall.size() > 100) {
                    service.sendThongBao("Rương quá đầy, vui lòng thử lại sau");
                    return;
                }
                CrackBall crackBall = new CrackBall();
                crackBall.setImgs(new int[]{419, 420, 421, 422, 423, 424, 425});
                crackBall.setPrice(1);
                crackBall.setTypePrice((byte) 0);
                crackBall.setIdTicket(821);
                crackBall.setType(CrackBall.VONG_QUAY_THUONG);
                crackBall.setPlayer(this);
                crackBall.setRandom();
                crackBall.show();
                this.crackBall = crackBall;
            }
            break;

            case 526: {
                int index = ((Integer) keyValue.elements[0]).intValue();
                deleteItemBoxCrackBall(index);
            }
            break;

            case 527: {
                isNoNeedToConfirm = true;
                int index = ((Integer) keyValue.elements[0]).intValue();
                deleteItemBoxCrackBall(index);
            }
            break;

            case 530:
                taskTalk(npc);
                break;

            case 531:
                shop = Shop.getShopSkill(this);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;

            case 535:
                master(npc);
                break;

            case 536: {
                StringBuilder sb = new StringBuilder();
                sb.append("Con muốn ta giúp gì nào?").append("\n");
                sb.append("Con muốn thực hiện một số chức năng bang hội à?").append("\n");
                sb.append("Để ta giúp con.");
                menus.add(new KeyValue(CMDMenu.VE_KHU_VUC_BANG, "Về khu\nvực bang"));
                menus.add(new KeyValue(CMDMenu.GIAI_TAN_BANG, "Giải tán\nBang hội"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);
            }
            break;

            case CMDMenu.GIAI_TAN_BANG:
                if (clan != null) {
                    if (clan.getNumberMember() == 1 || clan.getMember(clan.leaderID) == null) {
                        menus.add(new KeyValue(CMDMenu.DONG_Y_GIAI_TAN_BANG, "Đồng ý"));
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        service.openUIConfirm(npc.templateId, "Con có chắc muốn giải tán bang hội hay không?",
                                npc.avatar, menus);
                    } else {
                        menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                        service.openUIConfirm(npc.templateId,
                                "Ta chỉ giúp con giải tán bang hội khi trong bang chỉ còn mộ thành viên hoặc mất nhóm trưởng.",
                                npc.avatar, menus);
                    }
                }
                break;

            case CMDMenu.DONG_Y_GIAI_TAN_BANG:
                if (clan != null && (clan.getNumberMember() == 1 || clan.getMember(clan.leaderID) == null)) {
                    try {
                        ClanMember mem = this.clan.getMember(this.id);
                        if (mem != null) {
                            clan.deleteMember(mem);
                            Optional<ClanData> clanDataOpt = GameRepository.getInstance().clan.findById(this.clanID);
                            if (clanDataOpt.isPresent()) {
                                ClanData clanData = clanDataOpt.get();
                                GameRepository.getInstance().clan.delete(clanData);
                            }
                            zone.service.playerLoadAll(this);
                            this.clan = null;
                            clanID = -1;
                            updateBag();
                            service.clanInfo();
                            this.saveData();
                            this.service.serverMessage("Bạn đã rời bang hội thành công");
                        } else {
                            this.service.serverMessage("Bạn không đang trong bang hội nào");
                        }
                    } catch (Exception ignored) {
                        System.err.println("Error at 94");
                        logger.error("Có lỗi khi giải tán bang hội");
                    }
                }
                break;

            case CMDMenu.VE_KHU_VUC_BANG: {
                if (clan != null) {
                    if (clan.clanTerritory == null) {
                        TMap map = MapManager.getInstance().getMap(MapName.LANH_DIA_BANG_HOI);
                        clan.clanTerritory = new ClanTerritory(map, map.autoIncrease++);
                    }
                    zone.leave(this);
                    this.x = 159;
                    this.y = 432;
                    clan.clanTerritory.enter(this);
                }
            }
            break;

            case 540:
                transport(MapName.NHA_BUNMA, 0, 60);
                break;

            case 542:
                teleport(24);
                break;

            case CMDMenu.BLACK_DRAGONBALL_GUIDE: {
                StringBuilder sb = new StringBuilder();
                sb.append("Mỗi ngày từ 20h đến 21h các hành tinh có ngọc Rồng Sao Đen sẽ xảy ra 1 cuộc đại chiến")
                        .append("\b");
                sb.append(
                        "Người nào tìm thấy và giữ được Ngọc Rồng Sao Đen sẽ mang phần thưởng về cho bang của mình trong vòng 1 ngày")
                        .append("\n");
                sb.append(
                        "Lưu ý mỗi bang có thể chiếm hữu nhiều viên khác nhau nhưng nếu cùng loại cũng chỉ nhận được 1 lần phần thưởng đó.")
                        .append("\b");
                sb.append("Có 2 cách để thắng:").append("\b");
                sb.append("1) Giữ ngọc sao đen trên người hơn 5 phút liên tục").append("\b");
                sb.append("2) Sau 30 phút tham gia tàu sẽ đón về và đang giữ ngọc sao đen trên người").append("\n");
                sb.append("Các phần thưởng như sau").append("\b");
                sb.append("1 sao đen: +20% HP, KI và Sức đánh").append("\b");
                sb.append("2 sao đen: Mỗi giờ nhận ngẫu nhiên 1 CN/BH/BK/GX/AD/C1 hoặc CN2/BH2/BK2")
                        .append("\b");
                sb.append("3 sao đen: Mỗi giờ nhận 2 thỏi vàng").append("\b");
                sb.append("4 sao đen: +10% TNSM sư phụ và đệ tử cho cả ngày").append("\b");
                sb.append("Các phần thưởng mỗi giờ đến gặp ta để nhận nhé");
                service.openUISay(npc.templateId, sb.toString(), npc.avatar);
            }
            break;
            case CMDMenu.BLACK_DRAGONBALL_JOIN: {
                if (this.session.user.getActivated() == 0) {
                    service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
                    break;
                }
                ArrayList<KeyValue> list = new ArrayList<>();
                TMap map = MapManager.getInstance().getMap(MapName.HANH_TINH_M_2);
                if (!map.zones.isEmpty()) {
                    list.add(new KeyValue(MapName.HANH_TINH_M_2, "Hành tinh M-2", "Vũ trụ"));
                }
                map = MapManager.getInstance().getMap(MapName.HANH_TINH_POLARIS);
                if (!map.zones.isEmpty()) {
                    list.add(new KeyValue(MapName.HANH_TINH_POLARIS, "Hành tinh Polaris", "Vũ trụ"));
                }
                map = MapManager.getInstance().getMap(MapName.HANH_TINH_CRETACEOUS);
                if (!map.zones.isEmpty()) {
                    list.add(new KeyValue(MapName.HANH_TINH_CRETACEOUS, "Hành tinh Cretaceous", "Vũ trụ"));
                }
                map = MapManager.getInstance().getMap(MapName.HANH_TINH_MONMAASU);
                if (!map.zones.isEmpty()) {
                    list.add(new KeyValue(MapName.HANH_TINH_MONMAASU, "Hành tinh Monmaasu", "Vũ trụ"));
                }
                listMapTransport = list;
                setCommandTransport((byte) 1);
                service.mapTransport(list);
            }
            break;

            case CMDMenu.BLACK_DRAGONBALL_REWARD:
                if (clan != null) {
                    ClanMember mem = clan.getMember(this.id);
                    if (mem != null) {
                        long now = System.currentTimeMillis();
                        for (ClanReward r : mem.rewards) {
                            if (!r.isExpired()) {
                                if (!r.isCanBeReceivedDirectly()) {
                                    long t = now - r.getReceiveTime();
                                    long timeDelay = r.getTimeDelay();
                                    int star = r.getStar();
                                    if (t < timeDelay) {
                                        int timeRemaining = (int) ((timeDelay - t) / 1000);
                                        menus.add(new KeyValue(CMDMenu.CANCEL,
                                                String.format("%d sao\n%s", star, Utils.timeAgo(timeRemaining))));
                                    } else {
                                        int cmd = -1;
                                        if (star == 2) {
                                            cmd = CMDMenu.BLACK_DRAGONBALL_REWARD_5_STAR;
                                        } else if (star == 3) {
                                            cmd = CMDMenu.BLACK_DRAGONBALL_REWARD_6_STAR;
                                        }
                                        if (cmd != -1) {
                                            menus.add(new KeyValue(cmd, String.format("Nhận\nthưởng\n%d sao", star)));
                                        }
                                    }
                                }
                            }
                        }
                        service.openUIConfirm(npc.templateId,
                                "Ngươi đang có phần thưởng ngọc sao đen, có muốn nhận không?", npc.avatar, menus);
                    }
                }
                break;

            // case CMDMenu.BLACK_DRAGONBALL_REWARD_3_STAR: {
            // if (clan != null) {
            // ClanMember mem = clan.getMember(this.id);
            // if (mem != null) {
            // ClanReward r = mem.getClanReward(3);
            // if (r != null) {
            // long now = System.currentTimeMillis();
            // long t = now - r.getReceiveTime();
            // long timeDelay = r.getTimeDelay();
            // if (t >= timeDelay) {
            // Item item = new Item(ItemName.DAU_THAN_CAP_8);
            // item.quantity = 10;
            // item.setDefaultOptions();
            // if (addItem(item)) {
            // r.setReceiveTime(now);
            // service.sendThongBao(String.format("Bạn nhận được %d %s", item.quantity,
            // item.template.name));
            // } else {
            // service.sendThongBao(Language.ME_BAG_FULL);
            // }
            // }
            // }
            // }
            // }
            // }
            // break;
            //
            // case CMDMenu.BLACK_DRAGONBALL_REWARD_4_STAR: {
            // if (clan != null) {
            // ClanMember mem = clan.getMember(this.id);
            // if (mem != null) {
            // ClanReward r = mem.getClanReward(4);
            // if (r != null) {
            // long now = System.currentTimeMillis();
            // long t = now - r.getReceiveTime();
            // long timeDelay = r.getTimeDelay();
            // if (t >= timeDelay) {
            // Item item = new Item(RandomItem.BUA.next());
            // item.quantity = 3600000;
            // item.setDefaultOptions();
            // addItem(item);
            // r.setReceiveTime(now);
            // service.sendThongBao(String.format("Bạn nhận được %s", item.template.name));
            // }
            // }
            // }
            // }
            // }
            // break;
            case CMDMenu.BLACK_DRAGONBALL_REWARD_5_STAR: {
                // Nhận thưởng cho Ngọc Rồng Sao Đen 2 sao
                if (clan != null) {
                    ClanMember mem = clan.getMember(this.id);
                    if (mem != null) {
                        ClanReward r = mem.getClanReward(2);
                        if (r != null) {
                            long now = System.currentTimeMillis();
                            long t = now - r.getReceiveTime();
                            long timeDelay = r.getTimeDelay();
                            if (t >= timeDelay) {
                                int[] items = {ItemName.CUONG_NO, ItemName.BO_HUYET, ItemName.BO_KHI,
                                        ItemName.GIAP_XEN_BO_HUNG, ItemName.AN_DANH, ItemName.CAPSULE_1_SAO,
                                        ItemName.CUONG_NO_2, ItemName.BO_HUYET_2, ItemName.BO_KHI_2};
                                int itemID = items[Utils.nextInt(items.length)];
                                Item item = new Item(itemID);
                                item.quantity = 1;
                                item.setDefaultOptions();
                                if (addItem(item)) {
                                    r.setReceiveTime(now);
                                    service.sendThongBao(
                                            String.format("Bạn nhận được %d %s", item.quantity, item.template.name));
                                } else {
                                    service.sendThongBao(Language.ME_BAG_FULL);
                                }
                            }
                        }
                    }
                }
            }
            break;

            case CMDMenu.BLACK_DRAGONBALL_REWARD_6_STAR: {
                // Nhận thưởng cho Ngọc Rồng Sao Đen 3 sao
                if (clan != null) {
                    ClanMember mem = clan.getMember(this.id);
                    if (mem != null) {
                        ClanReward r = mem.getClanReward(3);
                        if (r != null) {
                            long now = System.currentTimeMillis();
                            long t = now - r.getReceiveTime();
                            long timeDelay = r.getTimeDelay();
                            if (t >= timeDelay) {
                                Item item = new Item(ItemName.THOI_VANG);
                                item.quantity = 2;
                                item.setDefaultOptions();
                                if (addItem(item)) {
                                    r.setReceiveTime(now);
                                    service.sendThongBao(
                                            String.format("Bạn nhận được %d %s", item.quantity, item.template.name));
                                } else {
                                    service.sendThongBao(Language.ME_BAG_FULL);
                                }

                            }
                        }
                    }
                }
            }
            break;

            case CMDMenu.BLACK_DRAGONBALL_REWARD_7_STAR: {
                if (clan != null) {
                    ClanMember mem = clan.getMember(this.id);
                    if (mem != null) {
                        ClanReward r = mem.getClanReward(7);
                        if (r != null) {
                            long now = System.currentTimeMillis();
                            long t = now - r.getReceiveTime();
                            long timeDelay = r.getTimeDelay();
                            if (t >= timeDelay) {
                                int itemID = ItemName.THOI_VANG;
                                Item item = new Item(itemID);
                                item.quantity = 2;
                                item.setDefaultOptions();
                                if (addItem(item)) {
                                    r.setReceiveTime(now);
                                    service.sendThongBao(
                                            String.format("Bạn nhận được %d %s", item.quantity, item.template.name));
                                } else {
                                    service.sendThongBao(Language.ME_BAG_FULL);
                                }
                            }
                        }
                    }
                }
            }
            break;

            case 543:
                shop = Shop.getShop(npc.templateId);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;

            case 541:
                service.openUISay(npc.id,
                        "20 năm trước bọn Android sát thủ đã đánh bại nhóm bảo vệ trái đất của Sôngoku và Cađíc, Pôcôlô ...\nRiêng Sôngoku vì bệnh tim đã chết trước đó nên không thể tham gia trận đánh...\nTừ đó đến nay bọn chúng tàn phá Trái đất không hề thương tiếc\nCháu và mẹ may mắn sống sót nhờ lần trốn tại tầng hầm công ty Capsule...\nCháu tuy cũng là siêu xayda nhưng cũng không thể làm gì được bọn Android sát thủ...\nChỉ có Sôngoku mới có thể đánh bại bọn chúng\nmẹ cháu đã chế tạo thành công cỗ máy thời gian\nVà cháu quay về quá khứ để cứu Sôngoku...\nBệnh của Gôku ở quá khứ là nan y, nhưng với trình độ y học tương lại chỉ cần uống thuốc là khỏi...\nHãy đi theo cháu đến tương lai giúp nhóm của Gôku dánh bại bọn Android sát thủ\nKhi nào chú cần giúp đỡ của cháu hãy đến đây nhé",
                        npc.avatar);
                break;

            case 544:
                service.openUISay(npc.id,
                        "20 năm trước bọn Android sát thủ đã đánh bại nhóm bảo vệ trái đất của Sôngoku và Cađíc, Pôcôlô ...\nRiêng Sôngoku vì bệnh tim đã chết trước đó nên không thể tham gia trận đánh...\nTừ đó đến nay bọn chúng tàn phá Trái đất không hề thương tiếc\nCa Lích và tôi may mắn sống sót nhờ lần trốn tại tầng hầm công ty Capsule...\nCa Lích tuy cũng là siêu xayda nhưng cũng không thể làm gì được bọn Android sát thủ...\nChỉ có Sôngoku mới có thể đánh bại bọn chúng\ntôi đã chế tạo thành công cỗ máy thời gian\nVà Ca Lích quay về quá khứ để cứu Sôngoku...\nBệnh của Gôku ở quá khứ là nan y, nhưng với trình độ y học tương lại chỉ cần uống thuốc là khỏi...\nHãy giúp chúng tôi bảo vệ người dân ở đây và giúp nhóm của Gôku dánh bại bọn Android sát thủ\nKhi nào bạn cần giúp đỡ của tôi hãy đến đây nhé",
                        npc.avatar);
                break;

            case 555: {
                if (getTotalGem() < 1) {
                    service.sendThongBao("bạn không đủ 1 ngọc để thực hiện");
                    return;
                }
                int playerID = ((Integer) keyValue.elements[0]).intValue();
                Player _c = SessionManager.findChar(playerID);
                if (_c != null) {
                    TMap map = _c.zone.map;
                    int mapID = map.mapID;
                    if (_c.isAnDanh || !checkCanEnter(mapID) || map.isUnableToTeleport() || isDead || _c.isDead) {
                        service.sendThongBao("Chưa thể đến lúc này, vui lòng thử lại sau ít phút");
                    } else {
                        revenge(_c);
                    }
                } else {
                    service.sendThongBao("Đang offline");
                }
            }
            break;

            case CMDMenu.CONSIGNMENT: {
                Consignment.getInstance().showShop(this);
            }
            break;

            case CMDMenu.CONSIGNMENT_GUIDE: {
                StringBuilder sb = new StringBuilder();
                sb.append("Của hàng chuyên nhận ký gửi mua bán vật phầm").append("\b");
                sb.append("Chỉ với phí kí là 500tr").append("\b");
                sb.append("Giá trị ký gửi không giới hạn").append("\b");
                sb.append("Một người bán, vạn người mua, mại dô, mại dô");
                service.openUISay(npc.templateId, sb.toString(), npc.avatar);
            }
            break;

            case CMDMenu.BLACK_DRAGONBALL_PHU:
                menus.add(new KeyValue(CMDMenu.BLACK_DRAGONBALL_PHU_X3, "X3\n5 thỏi vàng"));
                menus.add(new KeyValue(CMDMenu.BLACK_DRAGONBALL_PHU_X5, "X5\n10 thỏi vàng"));
                menus.add(new KeyValue(CMDMenu.BLACK_DRAGONBALL_PHU_X7, "X7\n15 thỏi vàng"));
                service.openUIConfirm(npc.templateId,
                        "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", npc.avatar, menus);
                break;

            case CMDMenu.BLACK_DRAGONBALL_PHU_X3:
                if (zone.map.isBlackDragonBall()) {
                    if (itemLoot != null) {
                        if (getQuantityThoiVang() < 3) {
                            service.sendThongBao("Bạn không đủ thỏi vàng để thực hiện,cần có 3 thỏi vàng");
                            return;
                        }
                        if (phuX >= 3) {
                            service.sendThongBao("Không thể phù cùng loại hoặc thấp hơn");
                            return;
                        }
                        subThoiVang(3);
                        setPhuX(3);
                        info.setInfo();
                        service.loadPoint();
                        info.recovery(Info.ALL, 100, true);
                    }
                }
                break;

            case CMDMenu.BLACK_DRAGONBALL_PHU_X5:
                if (zone.map.isBlackDragonBall()) {
                    if (itemLoot != null) {
                        if (getQuantityThoiVang() < 5) {
                            service.sendThongBao("Bạn không đủ thỏi vàng để thực hiện,cần có 5 thỏi vàng");
                            return;
                        }
                        if (phuX >= 5) {
                            service.sendThongBao("Không thể phù cùng loại hoặc thấp hơn");
                            return;
                        }
                        subThoiVang(5);
                        setPhuX(5);
                        info.setInfo();
                        service.loadPoint();
                        info.recovery(Info.ALL, 100, true);
                    }
                }
                break;

            case CMDMenu.BLACK_DRAGONBALL_PHU_X7:
                if (zone.map.isBlackDragonBall()) {
                    if (itemLoot != null) {
                        if (getQuantityThoiVang() < 10) {
                            service.sendThongBao("Bạn không đủ thỏi vàng để thực hiện,cần có 10 thỏi vàng");
                            return;
                        }
                        if (phuX >= 7) {
                            service.sendThongBao("Không thể phù cùng loại hoặc thấp hơn");
                            return;
                        }
                        subThoiVang(10);
                        setPhuX(7);
                        info.setInfo();
                        service.loadPoint();
                        info.recovery(Info.ALL, 100, true);
                    }
                }
                break;

            case 580: {
                StringBuilder sb = new StringBuilder();
                sb.append("Lịch thi đấu trong ngày").append("\b");
                sb.append("Giải nhi đồng: 8,14,18h").append("\b");
                sb.append("Giải siêu cấp 1: 9,13,19h").append("\b");
                sb.append("Giải siêu cấp 2: 10,15,20h").append("\b");
                sb.append("Giải siêu cấp 3: 11,16,21h").append("\b");
                sb.append("Giải ngoại hạng: 12,17,22,23h").append("\n");
                sb.append("Giải thưởng khi thắng mỗi vòng").append("\b");
                sb.append("Giải nhi đồng: 2 ngọc").append("\b");
                sb.append("Giải siêu cấp 1: 4 ngọc").append("\b");
                sb.append("Giải siêu cấp 2: 6 ngọc").append("\b");
                sb.append("Giải siêu cấp 3: 8 ngọc").append("\b");
                sb.append("Giải ngoại hạng: 10.000 vàng").append("\b");
                sb.append("Vô địch: 5 viên đá nâng cấp").append("\n");
                sb.append("Lệ phí đăng ký các giải đấu").append("\b");
                sb.append("Giải nhi đồng: 2 ngọc").append("\b");
                sb.append("Giải siêu cấp 1: 4 ngọc").append("\b");
                sb.append("Giải siêu cấp 2: 6 ngọc").append("\b");
                sb.append("Giải siêu cấp 3: 8 ngọc").append("\b");
                sb.append("Giải ngoại hạng: 10.000 vàng").append("\n");
                sb.append("Vui lòng đến đúng giờ để đăng ký thi đấu");
                service.openUISay(npc.templateId, sb.toString(), npc.avatar);
            }
            break;

            case 581: {
                zone.leave(this);
                TMap map = MapManager.getInstance().getMap(MapName.DAI_HOI_VO_THUAT_3);
                int zoneId = map.getZoneID();
                map.enterZone(this, zoneId);
            }
            break;

            case 582: {
                StringBuilder sb = new StringBuilder();
                sb.append("Đại hội quy tụ nhiều cao thủ như là Jacky Chun, Thiên Xin Hăng, Tàu Bảy Bảy...")
                        .append("\b");
                sb.append("Phần thưởng là 1 rương gỗ chứa nhiều vật phẩm giá trị").append("\b");
                sb.append("Khi hạ được đối thủ, phần thưởng sẽ nâng lên 1 cấp").append("\b");
                sb.append("Rương càng cao cấp, vật phẩm trong đó càng giá trị hơn").append("\n");
                sb.append("Mỗi ngày bạn chỉ được nhận 1 phần thưởng").append("\b");
                sb.append("Bạn hãy cố gắng hết sức mình để").append("\b");
                sb.append("nhận phần thưởng xứng đáng nhất nhé");
                service.openUISay(npc.templateId, sb.toString(), npc.avatar);
            }
            break;

            case 583:
                if (this.session.user.getActivated() == 0) {
                    service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
                    return;
                }
                DHVT23 dhvt = new DHVT23(this, roundDHVT23);
                DHVT23_Service.addDHVT(dhvt);
                break;
            case 584:
                if (isGetChest) {
                    this.service.sendThongBao("Bạn đã nhận rương hôm nay rồi");
                    return;
                }
                isGetChest = true;

                Item chest = new Item(ItemName.RUONG_GO);
                chest.options.add(new ItemOption(72, roundDHVT23));
                this.addItemBag(chest);
                this.service.sendThongBao("Bạn nhận được Rương gỗ cấp " + roundDHVT23);
                break;
            case 585: {
                zone.leave(this);
                TMap map = MapManager.getInstance().getMap(MapName.DAI_HOI_VO_THUAT);
                int zoneId = map.getZoneID();
                this.y -= 24;
                map.enterZone(this, zoneId);
            }
            break;
            case 586: {
                zone.leave(this);
                TMap map = MapManager.getInstance().getMap(MapName.DAI_HOI_VO_THUAT_2);
                int zoneId = map.getZoneID();
                this.y -= 24;
                map.enterZone(this, zoneId);
            }
            break;
            case CMDMenu.DANG_KY_DAI_HOI_VO_THUAT: {

            }
            break;

            case CMDMenu.HUY_DANG_KY_DAI_HOI_VO_THUAT: {

            }
            break;

            case CMDMenu.XAC_NHAN_HUY_DANG_KY_DAI_HOI_VO_THUAT: {
            }
            break;

            case CMDMenu.XAC_NHAN_DANG_KY_DAI_HOI_VO_THUAT: {
            }
            break;
            case CMDMenu.GIAI_SIEU_HANG_HUONG_DAN: {
                StringBuilder sb = new StringBuilder();
                sb.append("- Cơ cấu giải thưởng như sau ( chốt và trao giải nhẫu nhiên từ 20-23h mỗi ngày)")
                        .append("\b");
                sb.append("Top 1 : thưởng 200 thỏi vàng").append("\b");
                sb.append("Top 2 -10 : thưởng 50 thỏi vàng").append("\b");
                sb.append("Top 11-50 :  thưởng 10 thỏi vàng").append("\n");
                sb.append("- Mỗi ngày các bạn được thi đấu miễn phí 3 lần").append("\b");
                sb.append("- Sau đó phải trả 200tr vàng để đấu tiếp ").append("\b");
                sb.append("- Mặc định sẽ phải thi đấu với đối thủ có hạng cao hơn mình ( sẽ không thể \n"
                        + "thi đấu với đối thủ có hạng thấp hơn mình )").append("\b");
                sb.append("- Sau khi hạ được đối thủ có hạng cao hơn mình thì bạn sẽ lên được hạng \n"
                        + "của đối thủ mà bạn đã hạ gục được");
                service.openUISay(npc.templateId, sb.toString(), npc.avatar);
            }
            break;
            case CMDMenu.GIAI_SIEU_HANG_THI_DAU: {
                TMap map = MapManager.getInstance().getMap(MapName.DAU_TRUONG);
                ArenaSieuHang arenaSieuHang = new ArenaSieuHang(map, this.zone.zoneID);
                if (this == null) {
                    return;
                }

                this.setX((short) 334);
                this.setY((short) 274);
                this.zone.leave(this);
                arenaSieuHang.setCurrFightingPlayer(this);
                arenaSieuHang.enter(this);
                zone.service.setPosition(this, (byte) 0);
            }
            break;
            case CMDMenu.GIAI_SIEU_HANG_TOP_100: {
                Top dhvtSieuHang = Top.getTop(Top.TOP_DHVT_SIEU_HANG);
                assert dhvtSieuHang != null;
                dhvtSieuHang.show(this);
                break;
            }
            case CMDMenu.BXH_NHAN_THUONG_DHVT_SIEU_HANG:
                Top topTuiQua = Top.getTop(Top.REWARD_TOP_DHVT_SIEU_HANG);
                assert topTuiQua != null;
                topTuiQua.show(this);
                break;
            case CMDMenu.NHAN_THUONG_DHVT_SIEU_HANG: {
                Top topHopQua = Top.getTop(Top.REWARD_TOP_DHVT_SIEU_HANG);
                if (topHopQua != null) {
                    getRewardDHVTSieuHang(topHopQua.getElements());
                } else {
                    service.serverMessage("Có lỗi trong quá trình nhận thuởng, hãy thông báo cho admin để được hỗ trợ");
                }
                break;
            }
            case 600: {
                ItemTemplate item = (ItemTemplate) keyValue.elements[0];
                SkillBook book = Skills.getSkillBook(item.id);
                if (book != null) {
                    Skill skill = getSkill(book.id);
                    int sLevel = 0;
                    if (skill != null) {
                        sLevel = skill.point;
                        if (skill.point >= book.level) {
                            return;
                        }
                    }
                    if (info.potential < item.powerRequire) {
                        return;
                    }
                    skill = Skills.getSkill((byte) book.id, (byte) book.level);
                    if (book.level > sLevel + 1) {
                        return;
                    }
                    if (skill != null) {
                        if (skill.powerRequire > Integer.MAX_VALUE) {
                            info.potential -= skill.powerRequire;
                            service.loadPoint();
                        } else {
                            addExp(Info.POTENTIAL, -skill.powerRequire, false, false);
                        }
                        SkillBook b = new SkillBook(book.id, book.level, System.currentTimeMillis() + book.studyTime);
                        studying = b;
                        service.openUISay(npc.templateId, String.format(Language.LEARN_SKILL_SUCCESS,
                                skill.template.name, book.level, Utils.getTimeAgo((int) (book.studyTime / 1000))),
                                npc.avatar);
                    }
                }
            }
            break;

            case 601: {
                StringBuilder sb = new StringBuilder();
                sb.append("1) Trại độc nhãn là nơi các ngươi không nên vào vì những tướng tá rất mạnh. Hahaha")
                        .append("\b");
                sb.append("2) Trong trại độc nhãn, mỗi vị tướng đều giữ ngọc rồng từ 4 sao đến 7 sao, tùy lúc")
                        .append("\n");
                sb.append(
                        "3) Nếu ngươi thích chết thì cứ việc vào. Nhưng ta chỉ cho vào mỗi ngày một lần thôi, để ngươi khỏi phải chết nhiều, hahaha.")
                        .append("\b");
                sb.append(
                        "4) Các vị tướng trong doanh trại rất mạnh nhé, các ngươi không đơn giản có thể đánh bại họ bằng cách bình thường như đánh quái được đâu")
                        .append("\n");
                ;
                sb.append(
                        "5) Muốn vào, ngươi phải đi cùng một người đồng đội cùng bang (phải đứng gần ngươi). Nhưng ta khuyên là nên đi 3-4 người cùng.")
                        .append("\b");
                sb.append(
                        "6) Mỗi lần vào, ngươi chỉ có 30 phút để đánh. Sau 30 phút mà ngươi vẫn không thắng, ta sẽ cho máy bay trở ngươi về nhà.");
                service.openUISay(npc.id, sb.toString(), npc.avatar);
            }
            break;

            case 602:
                if (clan == null) {
                    return;
                }
                if (info.originalDamage < 300) {
                    service.sendThongBao("Phải nâng sức đánh gốc lớn hơn 300");
                    return;
                }
                boolean isOpen = false;
                if (clan.barrack == null) {
                    isOpen = true;
                    clan.barrack = new Barrack(this.name);
                    MapManager.getInstance().addObj(clan.barrack);
                } else {
                    if (!clan.barrack.running) {
                        return;
                    }
                }
                if (isOpen) {
                    List<Player> mems = zone.getMemberSameClan(this);
                    long now = System.currentTimeMillis();
                    for (Player _c : mems) {
                        if (_c.info.originalDamage < 300) {
                            continue;
                        }
                        ClanMember mem = clan.getMember(_c.id);
                        if (mem != null) {
                            if (mem.getNumberOfDaysJoinClan() < 2) {
                                continue;
                            }
                            _c.zone.leave(_c);
                            clan.barrack.enter(_c);
                        }
                    }
                } else {
                    zone.leave(this);
                    clan.barrack.enter(this);
                }
                break;

            case 603:
                if (clan == null) {
                    return;
                }
                int memberID = ((Integer) keyValue.elements[0]).intValue();
                ClanMember remoter = clan.getMember(this.id);
                ClanMember clanMember = clan.getMember(memberID);
                if (remoter == null || clanMember == null) {
                    service.sendThongBao("Có lỗi xảy ra");
                    return;
                }
                if (remoter.role == 0) {
                    clanMember.role = 0;
                    clan.leaderID = clanMember.playerID;
                    clan.leaderName = clanMember.name;
                    clan.clanUpdate((byte) 2, clanMember, -1);
                    remoter.role = 2;
                    clan.clanUpdate((byte) 2, remoter, -1);
                    ClanMessage message = new ClanMessage();
                    message.type = 0;
                    message.color = 1;
                    message.playerId = remoter.playerID;
                    message.playerName = remoter.name;
                    message.role = remoter.role;
                    message.isNewMessage = true;
                    message.chat = "Đã chuyển chủ bang cho " + clanMember.name;
                    clan.addMessage(message);
                    clan.clanInfo();
                }
                break;

            case 604: {
                if (clan != null) {
                    ClanMember clanMember2 = clan.getMember(this.id);
                    if (clanMember2.role == 0) {
                        return;
                    }
                    if (!clan.deleteMember(clanMember2)) {
                        return;
                    }
                    ClanMessage message = new ClanMessage();
                    message.type = 0;
                    message.playerId = this.id;
                    message.playerName = this.name;
                    message.role = clanMember2.role;
                    message.isNewMessage = true;
                    message.chat = "Đã rời bang";
                    message.color = 1;
                    clan.addMessage(message);
                    zone.service.playerLoadAll(this);
                    this.clan = null;
                    clanID = -1;
                    updateBag();
                    service.clanInfo();
                }
            }
            break;

            case 605:
                Top top = Top.getTop(Top.TOP_POWER);
                top.update();
                top.show(this);
                break;
            case 1210:
                if (this.session.user.getActivated() == 0) {
                    service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
                    return;
                }
                if (this.info.power < 40000000000L) {
                    service.serverMessage("Bạn cần sức mạnh tối thiểu 40 tỷ để vào map này");
                    break;
                } else {
                    teleport(156);
                }
                break;
            case 606: {
                if (true) {
                    service.openUISay(npc.id, "Chức năng đã tạm đóng", npc.avatar);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                menus.add(new KeyValue(607, "Top\nBang hội"));
                menus.add(new KeyValue(608, "Thành tích\nbang"));
                if (clan.treasure == null) {
                    sb.append("Đây là bản đồ kho báu hải tặc tí hon").append("\n");
                    sb.append("Các con cứ yên tâm lên đường").append("\n");
                    sb.append("Ở đây ta lo").append("\n");
                    sb.append("Nhớ chọn cấp độ vừa sức với mình nhé");
                    menus.add(new KeyValue(609, "Chọn\ncấp độ"));
                } else {
                    sb.append(String.format("Bang hội của con đang ở hang kho báu cấp %d", clan.treasure.getLevel()))
                            .append("\n");
                    sb.append("con có muốn đi cùng họ không?");
                    menus.add(new KeyValue(1210, "Đồng ý"));
                }
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);
            }
            break;

            case 609: {
                if (clan == null) {
                    service.sendThongBao("Hãy tham gia bang hội để tham gia phó bản");
                    return;
                }
                long power = info.power;
                if (info.power < power) {
                    service.sendThongBao(String.format("Yêu cầu %s sức mạnh trở lên", Utils.formatNumber(power)));
                    return;
                }
                if (clan.lastTimeEnterTreasure == -1) {
                    clan.remainingTimesCanEnterTreasure = 5;
                } else {
                    Date d = new Date(clan.lastTimeEnterTreasure);
                    if (!DateUtils.isSameDay(d, new Date())) {
                        clan.remainingTimesCanEnterTreasure = 5;
                    }
                }
                if (clan.remainingTimesCanEnterTreasure <= 0) {
                    service.sendThongBao("Bang của bạn đã hết lượt tham gia");
                    return;
                }
                Item item = getItemInBag(ItemName.BAN_DO_KHO_BAU);
                if (item == null) {
                    service.sendThongBao("Không tìm thấy bản đồ kho báu");
                    return;
                }
                ClanMember mem = clan.getMember(this.id);
                int days = mem.getNumberOfDaysJoinClan();
                if (days < 2) {
                    service.openUISay(npc.id,
                            "Chỉ những thành viên gia nhập bang hội tối thiểu 2 ngày mới có thể tham gia", npc.avatar);
                    return;
                }

                inputDlg = new InputDialog(CMDTextBox.TREASURE, "Hãy chọn cấp độ hang kho báu từ 1-110",
                        new TextField("Cấp độ", TextField.INPUT_TYPE_NUMERIC));
                inputDlg.setService(service);
                inputDlg.show();
            }
            break;

            case 610: {
                if (this.clan != null) {
                    Treasure treasure = clan.treasure;
                    long power = info.power;
                    if (info.power < power) {
                        service.sendThongBao(String.format("Yêu cầu %s sức mạnh trở lên", Utils.formatNumber(power)));
                        return;
                    }
                    ClanMember mem = clan.getMember(this.id);
                    int days = mem.getNumberOfDaysJoinClan();
                    if (days < 2) {
                        service.openUISay(npc.id,
                                "Chỉ những thành viên gia nhập bang hội tối thiểu 2 ngày mới có thể tham gia",
                                npc.avatar);
                        return;
                    }
                    if (treasure == null) {
                        if (clan.remainingTimesCanEnterTreasure <= 0) {
                            return;
                        }
                        clan.lastTimeEnterTreasure = System.currentTimeMillis();
                        clan.remainingTimesCanEnterTreasure--;
                        Item item = getItemInBag(ItemName.BAN_DO_KHO_BAU);
                        if (item == null) {
                            service.sendThongBao("Không tìm thấy bản đồ kho báu");
                            return;
                        }
                        int level = ((Integer) keyValue.elements[0]).intValue();
                        treasure = new Treasure((short) level, clan);
                        MapManager.getInstance().addObj(treasure);
                        clan.treasure = treasure;
                        removeItem(item.indexUI, 1);
                    } else if (treasure.isWinner()) {
                        service.sendThongBao("Động kho báu đang trong quá trình sập, không thể tham gia");
                        return;
                    }
                    if (treasure != null) {
                        transport(MapName.DONG_HAI_TAC, 1, 4);
                    }
                }
            }
            break;

            case 1100:
                menus.clear();
                menus.add(new KeyValue(1100, "Cập Nhật"));
                menus.add(new KeyValue(1101, "Chọn 1 số\n1 thỏi vàng"));
                menus.add(new KeyValue(1102, "Chọn 10 số\n10 thỏi vàng"));
                menus.add(new KeyValue(1103, "Chọn 100 số\n100 thỏi vàng"));
                menus.add(new KeyValue(1105, "Hướng Dẫn Thêm"));
                service.openUIConfirm(54, ConSoMayMan.showInfo(), (short) 3049, menus);
                break;
            case 1101:
                Item item = getItemInBag(ItemName.THOI_VANG);
                if (item == null) {
                    service.sendThongBao("Bạn không đủ thỏi vàng");
                    return;
                }
                inputDlg = new InputDialog(CMDTextBox.CSMM_1_SO, "Chọn 1 số", new TextField("Nhập số"));
                inputDlg.setService(service);
                inputDlg.show();
                break;
            case 1102:
                item = getItemInBag(ItemName.THOI_VANG);
                if (item == null || item.quantity < 9) {
                    service.sendThongBao("Bạn không đủ thỏi vàng");
                    return;
                }
                inputDlg = new InputDialog(CMDTextBox.CSMM_10_SO, "Chọn 10 số", new TextField("Nhập số"));
                inputDlg.setService(service);
                inputDlg.show();
                break;
            case 1103:
                item = getItemInBag(ItemName.THOI_VANG);
                if (item == null || item.quantity < 80) {
                    service.sendThongBao("Bạn không đủ thỏi vàng");
                    return;
                }
                inputDlg = new InputDialog(CMDTextBox.CSMM_100_SO, "Chọn 100 số", new TextField("Nhập số"));
                inputDlg.setService(service);
                inputDlg.show();
                break;
            case 1104:
                int next = zone.map.mapID != 126 ? 126 : 19;
                if (next == 126 && LocalDateTime.now().getHour() != 22) {
                    service.sendThongBao("Sự kiện chỉ hoạt động trong khung giờ 22h");
                    return;
                }
                if (!Hirudegarn.init && next == 126) {
                    Hirudegarn.init = true;
                    List<Zone> zones = MapManager.getInstance().getMap(126).zones;
                    for (Zone z : zones) {
                        Hirudegarn.addMob(z);
                        logger.info("Init Hirudegarn : 126 - " + z.zoneID);
                    }
                }
                zone.leave(this);
                TMap map = MapManager.getInstance().getMap(next);
                int zoneId = map.randomZoneID();
                map.enterZone(this, zoneId);
                break;
            case 1105:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(54, ConSoMayMan.details, (short) 3049, menus);
                break;
            case 1106:
                ConSoMayMan.Add(this, 4, 0);
                break;
            case 1107:
                ConSoMayMan.Add(this, 3, 0);
                break;
            case 1108: {
                item = getItemInBag(ItemName.THOI_VANG);
                if (item == null || item.quantity < 2) {
                    service.sendThongBao("Bạn không đủ thỏi vàng");
                    return;
                }
                removeItem(item.indexUI, 2);
                service.sendMessage(new Message(Cmd.ME_LIVE));
                mapPhuHo = 126;
                percentPhuHo = 2;
                pointThoiVang += 2;
                isChangePoint = true;
                break;
            }
            case 1109: {
                item = getItemInBag(ItemName.THOI_VANG);
                if (item == null || item.quantity < 2) {
                    service.sendThongBao("Bạn không đủ thỏi vàng");
                    return;
                }
                removeItem(item.indexUI, 2);
                service.sendMessage(new Message(Cmd.ME_LIVE));
                mapPhuHo = 127;
                percentPhuHo = 10;
                pointThoiVang += 2;
                isChangePoint = true;
                this.loadBody();
                break;
            }
            case 1110:
                this.shop = Shop.getShop(npc.templateId);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;

            case 1116: {
                zone.leave(this);
                TMap map2 = MapManager.getInstance().getMap(168);
                int zoneId2 = map2.getZoneID();
                this.y -= 24;
                map2.enterZone(this, zoneId2);
            }
            break;
            case 1117: {
                item = getItemInBag(ItemName.THOI_VANG);
                if (item == null || item.quantity < 2) {
                    service.sendThongBao("Bạn không đủ thỏi vàng");
                    return;
                }
                removeItem(item.indexUI, 2);
                service.sendMessage(new Message(Cmd.ME_LIVE));
                mapPhuHo = 113;
                percentPhuHo = 10;
                pointThoiVang += 2;
                this.loadBody();
                isChangePoint = true;
                break;
            }
            case 1118:
                KeoBuaBao.start(this);
                break;
            case 1119:
                mucCuoc = 5;
                service.sendThongBao("Đã đổi mức cược là 5 thỏi vàng");
                KeoBuaBao.start(this);
                break;
            case 1120:
                mucCuoc = 10;
                service.sendThongBao("Đã đổi mức cược là 10 thỏi vàng");
                KeoBuaBao.start(this);
                break;
            case 1121:
                mucCuoc = 50;
                service.sendThongBao("Đã đổi mức cược là 50 thỏi vàng");
                KeoBuaBao.start(this);
                break;
            case 1122:
                mucCuoc = 100;
                service.sendThongBao("Đã đổi mức cược là 100 thỏi vàng");
                KeoBuaBao.start(this);
                break;
            case 1123:
                KeoBuaBao.StartGame(this.mucCuoc, this, KeoBuaBao.KEO);
                break;
            case 1124:
                KeoBuaBao.StartGame(this.mucCuoc, this, KeoBuaBao.BUA);
                break;
            case 1125:
                KeoBuaBao.StartGame(this.mucCuoc, this, KeoBuaBao.BAO);
                break;
            case 1126:
                KeoBuaBao.showMucCuoc(this);
                break;
            case 1127:
                menus.clear();
                menus.add(new KeyValue(1128, "Bó hoa sắc màu"));
                menus.add(new KeyValue(1129, "Bó hoa sum vầy"));
                menus.add(new KeyValue(1130, "Hộp quà tổng hợp"));
                menus.add(new KeyValue(1131, "Nước ma thuật"));
                service.openUIConfirm(npc.templateId, "Con muốn đổi phần thưởng nào ?\n"
                        + "Dùng 10 mảnh tổng hợp đổi được Bó hoa sắc màu\n"
                        + "Dùng 20 mảnh tổng hợp đổi được Bó hoa sum vầy\n"
                        + "Dùng 30 mảnh tổng hợp đổi được Hộp quà tổng hợp\n"
                        + "Dùng 50 mảnh tổng hợp đổi được Nước ma thuật", npc.avatar, menus);

                break;
            case 1128: {
                int index = getIndexBagById(2156);
                item = null;
                if (index != -1) {
                    item = itemBag[index];
                }
                if (item == null) {
                    service.sendThongBao("Bạn không có vật phẩm để đổi");
                    return;
                }
                if (item.quantity < 10) {
                    service.sendThongBao("Bạn không có đủ vật phẩm");
                    return;
                }
                Item item2 = new Item(2215);
                item2.quantity = 1;
                item2.setDefaultOptions();
                if (this.addItem(item2)) {
                    removeItem(item.indexUI, 10);
                    service.sendThongBao("Bạn nhận được " + item2.template.name);
                } else {
                    service.sendThongBao("Hành trang không đủ ô trống");
                }
            }
            break;
            case 1129: {
                int index = getIndexBagById(2156);
                item = null;
                if (index != -1) {
                    item = itemBag[index];
                }
                if (item == null) {
                    service.sendThongBao("Bạn không có vật phẩm để đổi");
                    return;
                }
                if (item.quantity < 20) {
                    service.sendThongBao("Bạn không có đủ vật phẩm");
                    return;
                }
                Item item2 = new Item(2216);
                item2.quantity = 1;
                item2.setDefaultOptions();
                if (this.addItem(item2)) {
                    removeItem(item.indexUI, 20);
                    service.sendThongBao("Bạn nhận được " + item2.template.name);

                } else {
                    service.sendThongBao("Hành trang không đủ ô trống");
                }
            }
            break;
            case 1130: {
                int index = getIndexBagById(2156);
                item = null;
                if (index != -1) {
                    item = itemBag[index];
                }
                if (item == null) {
                    service.sendThongBao("Bạn không có vật phẩm để đổi");
                    return;
                }
                if (item.quantity < 30) {
                    service.sendThongBao("Bạn không có đủ vật phẩm");
                    return;
                }
                Item item2 = new Item(2238);
                item2.quantity = 1;
                item2.setDefaultOptions();
                if (this.addItem(item2)) {
                    removeItem(item.indexUI, 30);
                    service.sendThongBao("Bạn nhận được " + item2.template.name);
                } else {
                    service.sendThongBao("Hành trang không đủ ô trống");
                }
            }
            break;
            case 1131: {
                int index = getIndexBagById(2156);
                item = null;
                if (index != -1) {
                    item = itemBag[index];
                }
                if (item == null) {
                    service.sendThongBao("Bạn không có vật phẩm để đổi");
                    return;
                }
                if (item.quantity < 50) {
                    service.sendThongBao("Bạn không có đủ vật phẩm");
                    return;
                }
                Item item2 = new Item(2243);
                item2.quantity = 1;
                item2.setDefaultOptions();
                if (this.addItem(item2)) {
                    removeItem(item.indexUI, 50);
                    service.sendThongBao("Bạn nhận được " + item2.template.name);
                } else {
                    service.sendThongBao("Hành trang không đủ ô trống");
                }
            }
            break;
            case 1132: {
                int index = getIndexBagById(2156);
                item = null;
                if (index != -1) {
                    item = itemBag[index];
                }
                if (item == null) {
                    service.sendThongBao("Bạn không có vật phẩm để đổi");
                    return;
                }
                if (item.quantity < 20) {
                    service.sendThongBao("Bạn không có đủ vật phẩm");
                    return;
                }
                Item item2 = new Item(2147);
                item2.quantity = 1;
                item2.setDefaultOptions();
                if (this.addItem(item2)) {
                    removeItem(item.indexUI, 20);
                    service.sendThongBao("Bạn nhận được " + item2.template.name);
                } else {
                    service.sendThongBao("Hành trang không đủ ô trống");
                }
            }
            break;
            case 1133:
                item = getItemInBag(ItemName.THOI_VANG);
                if (item == null || item.quantity < 2) {
                    service.sendThongBao("Bạn không đủ thỏi vàng");
                    return;
                }
                removeItem(item.indexUI, 2);
                service.sendMessage(new Message(Cmd.ME_LIVE));
                mapPhuHo = 114;
                percentPhuHo = 10;
                pointThoiVang += 2;
                this.loadBody();
                isChangePoint = true;
                break;
            case 1134:
                if (this.getSession().user.getActivated() == 0) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để có thể vào map này");
                    return;
                }
                TMap map2 = MapManager.getInstance().getMap(175);
                if (map2 != null) {
                    int zoneId2 = map2.randomZoneID();
                    Zone zone2 = map2.getZoneByID(zoneId2);
                    if (zone2 != null) {
                        zone.leave(this);
                        this.x = 1018;
                        this.y = 264;
                        map2.enterZone(this, zoneId2);
                    }
                }
                break;
            case 1135:
            case 1136:
            case 1137:
                this.getDoTanThu(menuID - 1135);
                break;
            case 1138:
                if (this.gold < 200_000_000) {
                    service.sendThongBao("Bạn không có đủ 200tr vàng");
                    return;
                }
                this.subGold(200_000_000);
                for (Skill skill : this.getSkills()) {
                    skill.lastTimeUseThisSkill = System.currentTimeMillis() - skill.coolDown;
                }
                service.updateCoolDown(this.getSkills());
                service.sendMessage(new Message(Cmd.ME_LIVE));
                if (zone != null) {
                    zone.service.playerLoadLive(this);
                }
                service.sendThongBao("Hồi skill thành công");
                break;
            case 1139:
                EventTet2025.DoiHopQua(this);
                break;
            case 1140:
                menus.clear();
                menus.add(new KeyValue(1141, "Quay 1 lượt"));
                menus.add(new KeyValue(1142, "Quay 10 lượt"));
                menus.add(new KeyValue(1143, "Quay 25 lượt"));
                menus.add(new KeyValue(1144, "Quay 50 lượt"));
                service.openUIConfirm(npc.templateId, "Vòng quay tết 2025 "
                        + "\n Giá quay là 1 thỏi vàng 1 lượt\n"
                        + "Bạn có thể nhận được các vật phẩm may mắn khi quay", npc.avatar, menus);
                break;
            case 1141:
                com.ngocrong.NQMP.Tet2025.VongQuayTet.instance.start(this, 1);
                break;
            case 1142:
                com.ngocrong.NQMP.Tet2025.VongQuayTet.instance.start(this, 10);
                break;
            case 1143:
                com.ngocrong.NQMP.Tet2025.VongQuayTet.instance.start(this, 25);
                break;
            case 1144:
                com.ngocrong.NQMP.Tet2025.VongQuayTet.instance.start(this, 50);
                break;
            case 1145:
                EventDaNangCap.Exchange(this);
                break;
            case 1146:
                DHVT_SH_Service.gI().viewTop(this, (byte) 0);
                break;
            case 1147:
                DHVT_SH_Service.gI().sendInfo(this);
                break;
            case 1148:
                DHVT_SH_Service.gI().viewTop(this, (byte) 1);
                break;
            case 1149:
                DHVT_SH_Service.gI().viewTop(this, (byte) 1);
                break;
            case 1150:
                if (zone.map.mapID == MapName.HANH_TINH_BILL) {
                    for (Player pl : zone.players) {
                        if (pl.isBoss()) {
                            service.serverMessage("Bạn cần phải hạ gục tôi để có thể tiếp tục khiêu chiến ");
                            return;
                        }
                    }
                }
                Boss whis = new WhisInSingleMap(this);
                if (currentLevelBossWhis > 50) {
                    whis.setInfo(180000000L + ((currentLevelBossWhis - 50) * Utils.percentOf(250000000L, 10)), 1000000,
                            250000 + ((currentLevelBossWhis - 50) * Utils.percentOf(250000, 10)),
                            10 * currentLevelBossWhis, 5 * currentLevelBossWhis);
                } else if (currentLevelBossWhis > 0) {
                    whis.setInfo(3000000L * currentLevelBossWhis, 1000000 * currentLevelBossWhis,
                            5000 * currentLevelBossWhis, 10 * currentLevelBossWhis, 5 * currentLevelBossWhis);
                } else {
                    whis.name = "Whis Level " + currentLevelBossWhis;
                    whis.setInfo(3000000L, 10000, 5000, 10, 5);
                }
                whis.setLocation(this.zone);
                break;
            case 1151:
                Top topWhis = Top.getTop(Top.TOP_WHIS);
                assert topWhis != null;
                topWhis.update();
                topWhis.show(this);
                break;
            case 1152:
                Event1.ChangeItem(this, (byte) 1);
                break;
            case 1153:
                Event1.ChangeItem(this, (byte) 2);
                break;
            case 1154:
                this.combine = CombineFactory.getCombine(CombineType.Tach_Vat_Pham);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case 1155:
                Event1.ChangeItem(this, (byte) 3);
                break;
            case 1156: {
                if (this.getSession().user.getActivated() == 0) {
                    service.sendThongBao("Bạn cần kích hoạt tài khoản để có thể vào map này");
                    return;
                }
                var map3 = MapManager.getInstance().getMap(177);
                if (map3 != null) {
                    int zoneId2 = map3.randomZoneID();
                    Zone zone2 = map3.getZoneByID(zoneId2);
                    if (zone2 != null) {
                        zone.leave(this);
                        this.x = 1018;
                        this.y = 264;
                        map3.enterZone(this, zoneId2);
                    }
                }
                break;
            }
            case 1157:
                this.combine = CombineFactory.getCombine(CombineType.Nang_Item_De_Tu);
                combine.setNpc(npc);
                combine.setPlayer(this);
                this.combine.showTab();
                break;
            case 1158:
                joinMap(178, false);
                this.zone.service.setPosition(this, (byte) 0, 115, 480);
                break;
            case 1159:
                joinMap(179, false);
                this.zone.service.setPosition(this, (byte) 0, 90, 600);
                break;
            case 1160:
                joinMap(180, false);
                this.zone.service.setPosition(this, (byte) 0, 150, 840);
                break;
            case 1161:
                joinMap(154, false);
                break;
            case 1162:
                shop = Shop.getShop(-668);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;
            case 1163:
                shop = Shop.getShop(-669);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;
            case 1164:
                joinMap(5, false);
                break;
            case 1165:
                ItemCMS_Service.ShowCMSItem(this, npc);
                break;
            case 1166:
                service.dialogMessage(
                        "Top 1 : x7 đá Huyết Ma Thiên Tử cấp 5 ( sử dụng 10 viên ) nhận Huyết Ma Thiên Tử Cấp 5 : 5% HP,KI,SĐ vĩnh viễn\n"
                        + "Top 2 : x3 đá Huyết Ma Thiên Tử cấp 5 ( sử dụng 10 viên ) nhận Huyết Ma Thiên Tử cấp 5 : 5% HP,KI,SĐ vĩnh viễn\n"
                        + "Top 3,4,5,6 : x5 đá Huyết Ma Thiên Tử cấp 3 : ( sử dụng 10 viên ) nhận Huyết Ma Thiên Tử cấp 3 : 3% HP,KI,SĐ vĩnh viễn\n"
                        + "Top 7-16 : x3 đá Huyết Ma Thiên Tử cấp 2 : ( sử dụng 10 viên ) nhận Huyết Ma Thiên Tử cấp 2 : 2% HP,KI,SĐ vĩnh viễn\n"
                        + "Ngày nào chốt thưởng ngày đấy, chốt top vào lúc 00h00 hàng ngày");
                break;
            case 1167:
                Top topWhisRw = Top.getTop(Top.TOP_WHIS_Reward);
                assert topWhisRw != null;
                topWhisRw.update();
                topWhisRw.show(this);
                break;
            case 1168:
                int selectt = (int) keyValue.elements[0];
                item = getItemInBag(2243);
                if (item == null) {
                    service.sendThongBao("Bạn không đủ Nước ma thuật");
                    return;
                }
                if (selectt == 4 && isFullBag(3)) {
                    return;
                }
                if (selectt == 5 && isFullBag(1)) {
                    return;
                }
                removeItem(item.indexUI, 1);

                Item itemAdd = null;
                switch (selectt) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        byte itemTimeId = (byte) (ItemTimeName.DA_MA_THUAT_SELECT1 + selectt);
                        setItemTime(itemTimeId, 419 + selectt, true, 30 * 60);
                        System.err.println("add : " + itemTimeId + "icon:" + (419 + selectt));
                        break;
                    case 4:
                        itemAdd = new Item(1021);
                        itemAdd.quantity = 2;
                        addItem(itemAdd);

                        itemAdd = new Item(1022);
                        itemAdd.quantity = 2;
                        addItem(itemAdd);

                        itemAdd = new Item(1023);
                        itemAdd.quantity = 2;
                        addItem(itemAdd);
                        service.sendThongBao("Bạn nhận được x2 Item cấp 2 mỗi loại");
                        break;
                    case 5:
                        itemAdd = new Item(1994);
                        itemAdd.quantity = 1;
                        addItem(itemAdd);
                        service.sendThongBao("Bạn nhận được Vé đổi nội tại");
                        break;
                }
                break;
            case 2810:

                menus.clear();
                menus.add(new KeyValue(2811, "Dùng 99 Ốc\nđổi \nvật phẩm"));
                menus.add(new KeyValue(2812, "Dùng 99 Sò\nđổi \nvật phẩm"));
                menus.add(new KeyValue(2813, "Dùng 99 Cua\nđổi \nvật phẩm"));
                menus.add(new KeyValue(2814, "Dùng 99 Sao biển\nđổi \nvật phẩm"));
                menus.add(new KeyValue(2815, "BXH\nđổi \nvật phẩm"));
                service.openUIConfirm(npc.templateId, "Con muốn đổi phần thưởng nào ?", npc.avatar, menus);
                break;
            case 2811:
                exchangeItemEvent(ItemName.VO_OC);
                break;
            case 2812:
                exchangeItemEvent(ItemName.VO_SO);
                break;
            case 2813:
                exchangeItemEvent(ItemName.CON_CUA);
                break;
            case 2814:
                exchangeItemEvent(ItemName.SAO_BIEN);
                break;
            case 2815:
                Top topExchange = Top.getTop(Top.TOP_EXCHANGE);
                topExchange.update();
                topExchange.show(this);
                break;
            case CMDMenu.CLAN_FUNCTION:
                if (clan != null) {
                    menus.add(new KeyValue(CMDMenu.RENAME_CLAN, "Đổi tên\ntên bang\nviết tắt"));
                    menus.add(new KeyValue(CMDMenu.RANDOM_CLAN_NAME, "Chọn\nngẫu nhiên\ntên bang\nviết tắt"));
                    if (clan.level < Clan.LEVEL_MAX) {
                        menus.add(new KeyValue(CMDMenu.CLAN_UPGRADE, "Nâng cấp\nBang hội"));
                    }
                    menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                    service.openUIConfirm(npc.templateId, "Tôi có thể giúp gì cho bang hội của bạn ?", npc.avatar,
                            menus);
                }
                break;

            case CMDMenu.CLAN_UPGRADE: {
                if (clan != null) {
                    if (clan.level < Clan.LEVEL_MAX) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(String.format(
                                "Cần %s capsule bang [đang có %s capsule bang] để nâng cấp bang hội lên cấp %d",
                                Utils.currencyFormat(clan.level * 10000), Utils.currencyFormat(clan.clanPoint),
                                clan.level + 1)).append("\n");
                        sb.append("+ 1 tối đa số lượng thành viên").append("\n");
                        sb.append("+ 1 ô trống tối đa rương bang.").append("\n");
                        sb.append(String.format("+Mở bán bùa bang cấp %d", clan.level + 1));
                        menus.add(new KeyValue(CMDMenu.CLAN_UPGRADE_CONFIRM, "Đồng ý"));
                        menus.add(new KeyValue(CMDMenu.CANCEL, "từ chối"));
                        service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, menus);
                    }
                }
            }
            break;

            case CMDMenu.CLAN_UPGRADE_CONFIRM: {
                if (clan != null) {
                    if (clan.level < Clan.LEVEL_MAX) {
                        ClanMember clanMember2 = clan.getMember(this.id);
                        if (clanMember2 != null && clanMember2.isLeader()) {
                            int clanPoint = clan.clanPoint;
                            int pointNeedUpgrade = clan.level * 10000;
                            if (clanPoint < pointNeedUpgrade) {
                                service.sendThongBao(String.format("Không đủ capsule bang. cần %s capsule bang nữa.",
                                        Utils.currencyFormat(pointNeedUpgrade - clanPoint)));
                                return;
                            }
                            clan.clanPoint -= pointNeedUpgrade;
                            clan.level += 1;
                            if (clan.maxMember < 20) {
                                clan.maxMember += 1;
                            }
                            clan.clanInfo();
                            service.sendThongBao(String.format("Đã nâng cấp bang hội lên cấp %d", clan.level));
                        }
                    }
                }
            }
            break;

            case CMDMenu.RENAME_CLAN: {
                if (clan != null) {
                    long now = System.currentTimeMillis();
                    long time = now - clan.lastTimeRename;
                    if (time < 60000) {
                        service.sendThongBao(
                                String.format("Vui lòng đợi %s nữa", Utils.timeAgo((int) ((60000 - time) / 1000))));
                        return;
                    }
                    inputDlg = new InputDialog(CMDTextBox.RENAME_CLAN, "Nhập tên viết tắt bang hội",
                            new TextField("Tên viết tắt từ 2 đến 4 ký tự", TextField.INPUT_TYPE_ANY));
                    inputDlg.setService(service);
                    inputDlg.show();
                }
            }
            break;

            case CMDMenu.RANDOM_CLAN_NAME:
                if (clan != null) {
                    int length = Utils.nextInt(2, 4);
                    String name = RandomStringUtils.randomAlphanumeric(length);
                    renameClan(name);
                }
                break;

            case 779:
                menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                StringBuilder sb = new StringBuilder();
                sb.append("Mỗi lượt chơi có 6 giải thưởng");
                sb.append("\nĐược chọn tối đa 10 lần mỗi giải");
                sb.append("\nThời gian 1 lượt chọn là 5 phút");
                sb.append(
                        "\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn của từng giải và trao thưởng.");
                sb.append(
                        "\nLưu ý: Nếu tham gia trò chơi bằng Ngọc xanh hoặc Hồng ngọc thì người thắng sẽ nhận thưởng là hồng ngọc.");
                service.openUIConfirm(NpcName.LY_TIEU_NUONG, sb.toString(), (short) 3049, menus);
                break;

            case 780:
                lucky = Lucky.getLucky(Lucky.LUCKY_GOLD);
                if (lucky != null) {
                    lucky.show(this);
                }
                break;

            case 781:
                lucky = Lucky.getLucky(Lucky.LUCKY_GOLDBAR);
                if (lucky != null) {
                    lucky.show(this);
                }
                break;

            case 782:
                lucky = Lucky.getLucky(Lucky.LUCKY_GEMLOCK);
                if (lucky != null) {
                    lucky.show(this);
                }
                break;

            case 783:
                lucky = Lucky.getLucky(Lucky.LUCKY_GEM);
                if (lucky != null) {
                    lucky.show(this);
                }
                break;

            case 784:
                if (lucky != null) {
                    Server server = DragonBall.getInstance().getServer();
                    if (server.isMaintained) {
                        service.sendThongBao("Máy chủ đang bảo trì, không thể thực hiện");
                        return;
                    }
                    lucky.join(this, (byte) Lucky.LUCKY_NORMAL);
                }
                break;

            case 785:
                if (lucky != null) {
                    Server server = DragonBall.getInstance().getServer();
                    if (server.isMaintained) {
                        service.sendThongBao("Máy chủ đang bảo trì, không thể thực hiện");
                        return;
                    }
                    lucky.join(this, (byte) Lucky.LUCKY_VIP);
                }
                break;

            case 2000:
            case 2001:
            case 2002:
                this.shop = Shop.getShop(menuID);
                if (shop != null) {
                    shop.setNpc(npc);
                    service.viewShop(shop);
                }
                break;

            case CMDMenu.COMBINE:
                if (this.combine != null) {
                    if (this.combine instanceof PhaLeHoa) {
                        ((PhaLeHoa) this.combine).count = (int) keyValue.elements[0];
                    }
                    if (this.combine instanceof NangItemDeTu) {
                        ((NangItemDeTu) this.combine).count = (int) keyValue.elements[0];
                    }
                    if (this.combine instanceof DoiDoKichHoat) {
                        ((DoiDoKichHoat) this.combine).type = (int) keyValue.elements[0];
                    }
                    this.combine.combine();
                }
                break;

            case CMDMenu.PHA_LE_HOA:
                menus.add(new KeyValue(CMDMenu.PHA_LE_HOA_BY_GEM, "Bằng vàng"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(npc.templateId, "Ngươi muốn pha lê hóa trang bị bằng cách nào?", npc.avatar,
                        menus);
                break;

            // case 1002:
            // menus.add(new KeyValue(207, "Chuyển hóa\nDùng vàng"));
            // menus.add(new KeyValue(208, "Chuyển hóa\nDùng ngọc"));
            // service.openUIConfirm(npc.templateId,
            // "Ta sẽ biến trang bị mới cao cấp hơn của ngươi thành trang bị có cấp độ và
            // sao pha lê của trang bị cũ",
            // npc.avatar, menus);
            // break;
            case 1111:
                service.specialSkill((byte) 1);
                break;

            case 1112: {
                short numberOfOpenings = this.countNumberOfSpecialSkillChanges;
                if (numberOfOpenings > 8) {
                    numberOfOpenings = 8;
                }
                long price = (long) (10000000L * (Math.pow(2, numberOfOpenings)));
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Bạn có muốn mở Nội Tại");
                sb2.append("\n");
                sb2.append(String.format("với giá %s vàng", Utils.formatNumber(price)));
                menus.add(new KeyValue(1114, "Mở\nNội Tại"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(NpcName.CON_MEO, sb2.toString(), getPetAvatar(), menus);
            }
            break;

            case 1113: {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Bạn có muốn mở Nội Tại");
                sb2.append("\n");
                sb2.append("với giá 100 ngọc và");
                sb2.append("\n");
                sb2.append("tái lập giá vàng quay lại ban đầu không?");
                menus.add(new KeyValue(1115, "Mở VIP"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(NpcName.CON_MEO, sb2.toString(), getPetAvatar(), menus);
            }
            break;

            case 1114: {
                long require = 10000000000L;
                if (info.power < require) {
                    service.sendThongBao(String.format("Cần %s sức mạnh để mở", Utils.formatNumber(require)));
                    return;
                }
                short numberOfOpenings = this.countNumberOfSpecialSkillChanges;
                if (numberOfOpenings > 8) {
                    numberOfOpenings = 8;
                }
                long price = (long) (10000000 * (Math.pow(2, numberOfOpenings)));
                if (price > getGold()) {
                    service.sendThongBao(String.format("Bạn không đủ vàng, còn thiếu %s vàng nữa",
                            Utils.formatNumber(price - getGold())));
                    return;
                }
                addGold(-price);
                this.countNumberOfSpecialSkillChanges++;
                List<SpecialSkillTemplate> list = SpecialSkill.getListSpecialSkill(this.gender);
                int rd = Utils.nextInt(list.size());
                SpecialSkillTemplate tm = list.get(rd);
                int param = Utils.nextInt(tm.min, tm.max);
                specialSkill = new SpecialSkill(tm.id, param);
                specialSkill.setTemplate();
                service.specialSkill((byte) 0);
                service.sendThongBao(String.format("Bạn nhận được Nội tại: %s", specialSkill.getInfo()));
            }
            break;

            case 1115: {
                long require = 10000000000L;
                if (info.power < require) {
                    service.sendThongBao(String.format("Cần %s sức mạnh để mở", Utils.formatNumber(require)));
                    return;
                }
                int price = 100;
                if (price > getTotalGem()) {
                    service.sendThongBao(String.format("Bạn không đủ ngọc, còn thiếu %s ngọc nữa",
                            Utils.formatNumber(price - getTotalGem())));
                    return;
                }
                subDiamond(price);
                this.countNumberOfSpecialSkillChanges = 0;
                List<SpecialSkillTemplate> list = SpecialSkill.getListSpecialSkill(this.gender);
                int rd = Utils.nextInt(list.size());
                SpecialSkillTemplate tm = list.get(rd);
                int param = Utils.nextInt(tm.min, tm.max);
                specialSkill = new SpecialSkill(tm.id, param);
                specialSkill.setTemplate();
                service.specialSkill((byte) 0);
                service.sendThongBao(String.format("Bạn nhận được Nội tại: %s", specialSkill.getInfo()));
            }
            break;

            case 3500:
                magicTree.harvest(this);
                break;

            case 3501:
                menus.add(new KeyValue(3503, "Đồng ý"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(NpcName.CON_MEO, "Bạn có chắc muốn nâng cây đậu?", getPetAvatar(), menus);
                break;

            case 3502:
                magicTree.fastest(this);
                break;

            case 3503:
                magicTree.upgrade(this);
                break;

            case 3504:
                magicTree.quickUpgrade(this);
                break;

            case 3505:
                magicTree.cancelUpgrade(this);
                break;

            case 7878: {
                teleport(155);
            }
            break;

            case 7879: {
                teleport(0);
            }
            break;

            case 10000:
                int playerId = ((Integer) keyValue.elements[0]).intValue();
                if (this.friends.size() >= 5) {
                    if (getTotalGem() >= 5) {
                        subDiamond(5);
                    } else {
                        service.sendThongBao("Không đủ ngọc để thực hiện.");
                        return;
                    }
                }
                addFriend(playerId);
                break;

            case 20000:
                if (callDragon != null) {
                    callDragon.more();
                }
                break;

            case 20001:
                if (callDragon != null) {
                    callDragon.accept();
                }
                break;

            case 20002:
                if (callDragon != null) {
                    callDragon.deny();
                }
                break;

            case 20004:
                if (callDragon != null) {
                    callDragon.setSelect(keyValue);
                    callDragon.confirm();
                    return;
                }
                break;

            case 20006: {
                long now2 = System.currentTimeMillis();
                long time = now2 - lastTimeCallDragon;
                if (time < 600000) {
                    int seconds = (int) ((600000 - time) / 1000);
                    service.sendThongBao(String.format("Vui lòng đợi %s nữa", Utils.timeAgo(seconds)));
                    return;
                }
                int indexDragonBall1Star = -1;
                int indexDragonBall2Star = -1;
                int indexDragonBall3Star = -1;
                int indexDragonBall4Star = -1;
                int indexDragonBall5Star = -1;
                int indexDragonBall6Star = -1;
                int indexDragonBall7Star = -1;
                for (Item itm : itemBag) {
                    if (itm != null) {
                        if (itm.id == ItemName.NGOC_RONG_1_SAO) {
                            indexDragonBall1Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_2_SAO) {
                            indexDragonBall2Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_3_SAO) {
                            indexDragonBall3Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_4_SAO) {
                            indexDragonBall4Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_5_SAO) {
                            indexDragonBall5Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_6_SAO) {
                            indexDragonBall6Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_7_SAO) {
                            indexDragonBall7Star = itm.indexUI;
                        }
                    }
                }
                if (indexDragonBall1Star != -1 && indexDragonBall2Star != -1 && indexDragonBall3Star != -1
                        && indexDragonBall4Star != -1 && indexDragonBall5Star != -1 && indexDragonBall6Star != -1
                        && indexDragonBall7Star != -1) {
                    lastTimeCallDragon = now2;
                    removeItem(indexDragonBall1Star, 1);
                    removeItem(indexDragonBall2Star, 1);
                    removeItem(indexDragonBall3Star, 1);
                    removeItem(indexDragonBall4Star, 1);
                    removeItem(indexDragonBall5Star, 1);
                    removeItem(indexDragonBall6Star, 1);
                    removeItem(indexDragonBall7Star, 1);
                    callDragon = new CallDragon1Star(this, this.x, this.y);
                    callDragon.appearDragon();
                    callDragon.show();
                    SessionManager.serverMessage(
                            String.format("%s vừa gọi rồng thần tại %s khu %d", this.name, zone.map.name, zone.zoneID));
                }
            }
            break;
            case 20008: {
                if (this.clan == null) {
                    service.openUISay(NpcName.CON_MEO, "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai",
                            getPetAvatar());
                    return;
                }
                if (idNRNM != 353) {
                    service.openUISay(NpcName.CON_MEO, "Bạn phải có viên ngọc rồng Namếc 1 sao", getPetAvatar());
                    return;
                }
                byte numChar = 0;
                // for (Player pl : zone.getPlayers()) {
                // if (pl.clan != null && pl.clan.id == clan.id && pl.id != this.id) {
                // if (pl.idNRNM != -1) {
                // numChar++;
                // }
                // }
                // }
                // if (numChar < 6) {
                // service.serverMessage("Bạn hãy tập hợp đủ 7 viên ngọc rồng namek đi");
                // return;
                // }
                if (zone.map.mapID == 7 && idNRNM != -1) {
                    if (idNRNM == 353) {
                        // if (!isWaitToCallDragonNamek()) {
                        // isWaitToCallDragonNamek = true;
                        // delayToCallDragonNamek = System.currentTimeMillis();
                        // }
                        // if (!isWaitToCallDragonNamek()) {
                        // service.serverMessage("Bạn hãy tập hợp đủ 7 viên ngọc rồng namek");
                        // return;
                        // }
                        int delay = (int) ((System.currentTimeMillis() - delayToCallDragonNamek) / 1000);
                        // delay 60
                        if (delay < 10) {
                            service.serverMessage("Bạn cần chờ " + (10 - delay) + " giây nữa để gọi rồng thần");
                            return;
                        } else {
                            callDragon = new CallDragonNamek(this, this.x, this.y);
                            callDragon.appearDragon();
                            callDragon.show();
                            doneDragonNamec();
                            Server server = DragonBall.getInstance().getServer();
                            server.initNgocRongNamec((byte) 1);
                            SessionManager.serverMessage(
                                    String.format("Bang hội %s vừa gọi rồng vừa gọi rồng thần Namek tại %s khu %d",
                                            this.clan.name, zone.map.name, zone.zoneID));
                        }
                    } else {
                        service.openUISay(NpcName.CON_MEO, "Bạn phải có viên ngọc rồng Namếc 1 sao", getPetAvatar());
                    }
                }
            }
            break;
            case CMDMenu.TELEPORT_HANH_TINH_KAIO: {
                teleport(MapName.HANH_TINH_KAIO);
                break;
            }

            case CMDMenu.TELEPORT_THAN_DIEN: {
                teleport(MapName.THAN_DIEN);
                break;
            }

            case CMDMenu.TELEPORT_THANH_DIA_KAIO: {
                teleport(MapName.THANH_DIA_KAIO);
                break;
            }

            case CMDMenu.TELEPORT_HANH_TINH_BILL: {
                teleport(MapName.HANH_TINH_BILL);
                break;
            }
            case CMDMenu.NAU_BANH_CHUNG:
                nauBanh(0);
                break;
            case CMDMenu.NAU_BANH_TET:
                nauBanh(1);
                break;
            case CMDMenu.BANH_CHUNG_TAN_NIEN:
                nauBanhNew(0);
                break;
            case CMDMenu.BANH_TET_XUAN_VUI:
                nauBanhNew(1);
                break;
            case CMDMenu.HOP_QUA_XUAN:
                hopquatet(0);
                break;
            case CMDMenu.HOP_QUA_TET:
                hopquatet(1);
                break;
            case CMDMenu.OSIN_CHECKIN:
                OsinCheckInEvent.checkIn(this);
                break;
//            case CMDMenu.OSIN_REWARD:
//                OsinCheckInEvent.receiveReward(this);
//                break;
            case CMDMenu.UOC_RONG_THIEN_MENH_1:
                uocRongThienMenh(1);
                break;
            case CMDMenu.UOC_RONG_THIEN_MENH_2:
                uocRongThienMenh(2);
                break;
            case CMDMenu.UOC_RONG_THIEN_MENH_3:
                uocRongThienMenh(3);
                break;
            case CMDMenu.UOC_RONG_THIEN_MENH_4:
                uocRongThienMenh(4);
                break;
            case CMDMenu.UOC_RONG_THIEN_MENH_5:
                uocRongThienMenh(5);
                break;
            case CMDMenu.UOC_RONG_THIEN_MENH_6:
                uocRongThienMenh(6);
                break;
            case CMDMenu.UOC_RONG_LOC_PHAT_1:
                uocRongLocPhat(1);
                break;
            case CMDMenu.UOC_RONG_LOC_PHAT_2:
                uocRongLocPhat(2);
                break;
            case CMDMenu.UOC_RONG_LOC_PHAT_3:
                uocRongLocPhat(3);
                break;
            case CMDMenu.UOC_RONG_LOC_PHAT_4:
                uocRongLocPhat(4);
                break;
            case CMDMenu.UOC_RONG_LOC_PHAT_5:
                uocRongLocPhat(5);
                break;
            case CMDMenu.UOC_RONG_LOC_PHAT_6:
                List<SpecialSkillTemplate> list = SpecialSkill.getListSpecialSkill(this.gender);
                menus.clear();
                for (SpecialSkillTemplate s : list) {
                    menus.add(new KeyValue(
                            CMDMenu.CHANGE_NOI_TAI,
                            s.info.replace("#", String.valueOf(s.max) + "%"),
                            s.id));
                }
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn nội tại bạn mong muốn", getPetAvatar(), menus);
                break;
            case CMDMenu.UOC_RONG_LOC_PHAT_7:
                uocRongLocPhat(7);
                break;
            case CMDMenu.CHANGE_NOI_TAI:
                item = getItemInBag(1994);
                if (item == null) {
                    service.sendThongBao("Bạn không đủ Vé đổi nội tại");
                    return;
                }
                removeItem(item.indexUI, 1);
                List<SpecialSkillTemplate> listNoiTai = SpecialSkill.getListSpecialSkill(this.gender);
                int skillId = ((Integer) keyValue.elements[0]).intValue();
                SpecialSkillTemplate special = SpecialSkill.getSpecialSkillById(skillId);
                specialSkill = new SpecialSkill(special.id, special.max);
                specialSkill.setTemplate();
                service.specialSkill((byte) 0);
                this.service.sendThongBao("Bạn vừa nhận được nội tại " + keyValue.value);
                break;

            case CMDMenu.OPEN_SET_CUOI_TD_KH:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_TD_KH + 1, "Set Thiên Xin Hăng"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_TD_KH + 2, "Set Kirin"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_TD_KH + 3, "Set Songoku"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case CMDMenu.OPEN_SET_CUOI_NM_KH:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_NM_KH + 1, "Set Picolo"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_NM_KH + 2, "Set Ốc tiêu"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_NM_KH + 3, "Set Pikkoro Daimao"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case CMDMenu.OPEN_SET_CUOI_XD_KH:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_XD_KH + 1, "Set Nappa"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_XD_KH + 2, "Set Cadic"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_XD_KH + 3, "Set Kakarot"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case CMDMenu.OPEN_1_SKH_NM: {
                int index1 = getIndexBagById(2303);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSKHRandom(1);
                break;
            }
            case CMDMenu.OPEN_1_SKH_XD: {
                int index1 = getIndexBagById(2303);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSKHRandom(2);
                break;
            }
            case CMDMenu.OPEN_1_SKH_TD: {
                int index1 = getIndexBagById(2303);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSKHRandom(0);
                break;
            }
            case CMDMenu.OPEN_1_TL_TD: {
                int index1 = getIndexBagById(2304);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openTLRandom(0);
                break;
            }
            case CMDMenu.OPEN_1_TL_NM: {
                int index1 = getIndexBagById(2304);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openTLRandom(1);
                break;
            }
            case CMDMenu.OPEN_1_TL_XD: {
                int index1 = getIndexBagById(2304);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openTLRandom(2);
                break;
            }
            case CMDMenu.OPEN_1_CUOI_TD: {
                int index1 = getIndexBagById(2302);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openDoCuoiKHRandom(0);
            }
            break;
            case CMDMenu.OPEN_1_CUOI_NM: {
                int index1 = getIndexBagById(2302);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openDoCuoiKHRandom(1);
            }
            break;
            case CMDMenu.OPEN_1_CUOI_XD: {
                int index1 = getIndexBagById(2302);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openDoCuoiKHRandom(2);
                break;
            }
            case CMDMenu.OPEN_1_HD_TD: {
                int index1 = getIndexBagById(2301);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openDoHDRandom(0, -1);
                break;
            }
            case CMDMenu.OPEN_1_HD_NM: {
                int index1 = getIndexBagById(2301);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openDoHDRandom(1, -1);
                break;
            }
            case CMDMenu.OPEN_1_HD_XD: {
                int index1 = getIndexBagById(2301);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openDoHDRandom(2, -1);
                break;
            }
            case CMDMenu.OPEN_SET_HD_TD: {
                int index1 = getIndexBagById(2300);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                this.openSetHD(0, false, -1);
            }
            break;
            case CMDMenu.OPEN_SET_HD_NM: {
                int index1 = getIndexBagById(2300);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                this.openSetHD(1, false, -1);
            }
            break;
            case CMDMenu.OPEN_SET_HD_XD: {
                int index1 = getIndexBagById(2300);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                this.openSetHD(2, false, -1);
            }
            break;
            case CMDMenu.OPEN_SET_TL_TD_KH:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_TD_KH + 1, "Set Thiên Xin Hăng"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_TD_KH + 2, "Set Kirin"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_TD_KH + 3, "Set Songoku"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set thần linh bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case CMDMenu.OPEN_SET_TL_NM_KH:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_NM_KH + 1, "Set Picolo"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_NM_KH + 2, "Set Ốc tiêu"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_NM_KH + 3, "Set Pikkoro Daimao"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set thần linh bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case CMDMenu.OPEN_SET_TL_XD_KH:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_XD_KH + 1, "Set Nappa"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_XD_KH + 2, "Set Cadic"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_XD_KH + 3, "Set Kakarot"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set thần linh bạn mong muốn ?", getPetAvatar(), menus);
                break;

            case CMDMenu.OPEN_SET_CUOI_TD_KH + 1: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(0, true, Item.THIENXINHANG);
            }
            break;
            case CMDMenu.OPEN_SET_CUOI_TD_KH + 2: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(0, true, Item.KIRIN);
            }
            break;
            case CMDMenu.OPEN_SET_CUOI_TD_KH + 3: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(0, true, Item.SONGOKU);
            }
            break;

            case CMDMenu.OPEN_SET_CUOI_NM_KH + 1: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(1, true, Item.PICOLO);
            }
            break;
            case CMDMenu.OPEN_SET_CUOI_NM_KH + 2: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(1, true, Item.OCTIEU);
            }
            break;
            case CMDMenu.OPEN_SET_CUOI_NM_KH + 3: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(1, true, Item.DAIMAO);
            }
            break;

            case CMDMenu.OPEN_SET_CUOI_XD_KH + 1: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(2, true, Item.NAPPA);
            }
            break;
            case CMDMenu.OPEN_SET_CUOI_XD_KH + 2: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(2, true, Item.CADIC);
            }
            break;
            case CMDMenu.OPEN_SET_CUOI_XD_KH + 3: {
                int index1 = getIndexBagById(ItemName.RUONG_DO_CUOI_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetDoCuoi(2, true, Item.KAKAROT);
            }
            break;

            case CMDMenu.OPEN_SET_TL_TD_KH + 1: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(0, true, Item.THIENXINHANG);
            }
            break;
            case CMDMenu.OPEN_SET_TL_TD_KH + 2: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(0, true, Item.KIRIN);
            }
            break;
            case CMDMenu.OPEN_SET_TL_TD_KH + 3: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(0, true, Item.SONGOKU);
            }
            break;
            case CMDMenu.OPEN_SET_TL_NM_KH + 1: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(1, true, Item.PICOLO);
            }
            break;
            case CMDMenu.OPEN_SET_TL_NM_KH + 2: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(1, true, Item.OCTIEU);
            }
            break;
            case CMDMenu.OPEN_SET_TL_NM_KH + 3: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(1, true, Item.DAIMAO);
            }
            break;
            case CMDMenu.OPEN_SET_TL_XD_KH + 1: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(2, true, Item.NAPPA);
            }
            break;
            case CMDMenu.OPEN_SET_TL_XD_KH + 2: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(2, true, Item.CADIC);
            }
            break;
            case CMDMenu.OPEN_SET_TL_XD_KH + 3: {
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH_KICH_HOAT);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(2, true, Item.KAKAROT);
            }
            break;
            case CMDMenu.OPEN_SET_TL_TD:
                int index1 = getIndexBagById(ItemName.RUONG_THAN_LINH);
                Item rtl = null;
                if (index1 != -1) {
                    rtl = itemBag[index1];
                }
                if (rtl != null && rtl.quantity < 1) {
                    return;
                }
                removeItem(index1, 1);
                openSetTL(0, false, -1);
                break;
            case CMDMenu.OPEN_SET_TL_NM:
                int index2 = getIndexBagById(ItemName.RUONG_THAN_LINH);
                Item rtl2 = null;
                if (index2 != -1) {
                    rtl2 = itemBag[index2];
                }
                if (rtl2 != null && rtl2.quantity < 1) {
                    return;
                }
                removeItem(index2, 1);
                openSetTL(1, false, -1);
                break;
            case CMDMenu.OPEN_SET_TL_XD:
                int index3 = getIndexBagById(ItemName.RUONG_THAN_LINH);
                Item rtl3 = null;
                if (index3 != -1) {
                    rtl3 = itemBag[index3];
                }
                if (rtl3 != null && rtl3.quantity < 1) {
                    return;
                }
                removeItem(index3, 1);
                openSetTL(2, false, -1);
                break;
        }
    }

    public void hopquatet(int type) {
        Item item = null;
        Item banhchung = getItemInBag(ItemName.BANH_CHUNG_TAN_NIEN);
        Item banhtet = getItemInBag(ItemName.BANH_TET_XUAN_VUI);
        Item lixi = getItemInBag(ItemName.LI_XI_MAY_MAN);
        if (lixi == null || (banhchung == null && type == 0) || (banhtet == null && type == 1)) {
            this.service.sendThongBao("Không tìm thấy đủ nguyên liệu");
            return;
        }
        if ((type == 0 && banhchung.quantity < 5) || (type == 1 && banhtet.quantity < 5)) {
            this.service.sendThongBao("Bạn không đủ nguyên liệu");
            return;
        }
        removeItem(lixi.indexUI, 1);
        if (type == 0) {
            removeItem(banhchung.indexUI, 5);
            item = new Item(ItemName.HOP_QUA_XUAN);
            item.quantity = 1;
        }
        if (type == 1) {
            removeItem(banhtet.indexUI, 5);
            item = new Item(ItemName.HOP_QUA_TET_AN_KHANG);
            item.quantity = 1;
        }
        addItem(item);
        this.service.sendThongBao("Bạn vừa nhận được " + item.template.name);
    }

    public void nauBanhNew(int type) {
        Item item = null;
        Item gaotam = getItemInBag(ItemName.GAO_TAM_THOM);
        Item labang = getItemInBag(ItemName.LA_BANG_TUOI);
        Item hatdua = getItemInBag(ItemName.HAT_DUA_HONG);
        Item thitbachi = getItemInBag(ItemName.THIT_BA_CHI);
        Item luahong = getItemInBag(ItemName.LUA_HONG_TET);
        Item hoamai = getItemInBag(ItemName.HOA_MAI_VANG);
        if ((gaotam == null || labang == null || hatdua == null || luahong == null) || (type == 0 && thitbachi == null)
                || (type == 1 && hoamai == null)) {
            this.service.sendThongBao("Không tìm thấy đủ nguyên liệu");
            return;
        }
        if (gaotam.quantity < 99 || labang.quantity < 99 || hatdua.quantity < 99 || luahong.quantity < 99
                || (type == 0 && thitbachi.quantity < 99) || (type == 1 && hoamai.quantity < 99)) {
            this.service.sendThongBao("Bạn không đủ nguyên liệu");
            return;
        }
        if (this.gold < 100000000) {
            this.service.sendThongBao("Không đủ vàng để thực hiện");
            return;
        }
        removeItem(gaotam.indexUI, 99);
        removeItem(labang.indexUI, 99);
        removeItem(hatdua.indexUI, 99);
        removeItem(luahong.indexUI, 99);
        subGold(100000000);
        if (type == 0) {
            removeItem(thitbachi.indexUI, 99);
            item = new Item(ItemName.BANH_CHUNG_TAN_NIEN);
            item.isLock = false;
            item.quantity = 1;
        } else if (type == 1) {
            removeItem(hoamai.indexUI, 99);
            item = new Item(ItemName.BANH_TET_XUAN_VUI);
            item.isLock = false;
            item.quantity = 1;
        }
        addItem(item);
        this.service.sendThongBao("Bạn vừa nhận được " + item.template.name);
    }

    public void nauBanh(int type) {
        Item item = null;
        Item thungnep = getItemInBag(ItemName.THUNG_NEP);
        Item dauxanh = getItemInBag(ItemName.THUNG_DAU_XANH);
        Item ladong = getItemInBag(ItemName.LA_DONG);
        Item thitheo = getItemInBag(ItemName.THIT_HEO);
        Item luanaubanh = getItemInBag(ItemName.LUA_NAU_BANH);
        if ((ladong == null || thitheo == null || luanaubanh == null) || (type == 0 && thungnep == null)
                || (type == 1 && dauxanh == null)) {
            this.service.sendThongBao("Không tìm thấy đủ nguyên liệu");
            return;
        }
        if (thungnep.quantity < 99 || ladong.quantity < 99 || thitheo.quantity < 99 || luanaubanh.quantity < 1
                || (type == 0 && thungnep.quantity < 99) || (type == 1 && dauxanh.quantity < 99)) {
            this.service.sendThongBao("Nguyên liệu không đủ");
            return;
        }
        if (this.gold < 500000000) {
            this.service.sendThongBao("Không đủ vàng để thực hiện");
            return;
        }
        removeItem(ladong.indexUI, 99);
        removeItem(thitheo.indexUI, 99);
        removeItem(luanaubanh.indexUI, 1);
        subGold(500000000);
        if (type == 0) {
            removeItem(thungnep.indexUI, 99);
            item = new Item(ItemName.BANH_CHUNG);
            item.isLock = false;
            item.quantity = 1;
        } else if (type == 1) {
            removeItem(dauxanh.indexUI, 99);
            item = new Item(ItemName.BANH_TET);
            item.isLock = false;
            item.quantity = 1;
        }
        addItem(item);
        this.service.sendThongBao("Bạn vừa nhận được " + item.template.name);
    }

    public void confirmTextBox(Message ms) {
        if (inputDlg != null) {
            if (inputDlg.input(ms)) {
                switch (inputDlg.getType()) {
                    case CMDTextBox.GIFT_CODE:
                        useGiftCode();
                        break;
                    case CMDTextBox.REPORT:
                        report();
                        break;
                    case CMDTextBox.TREASURE:
                        treasure();
                        break;
                    case CMDTextBox.RENAME_CLAN:
                        renameClan(inputDlg.getText());
                        break;
                    case CMDTextBox.SELL_GOLD_BAR:
                        sellGoldBar(inputDlg.getLong());
                        break;
                    case CMDTextBox.CSMM_1_SO:
                        AddCSMM(0);
                        break;
                    case CMDTextBox.CSMM_10_SO:
                        AddCSMM(1);
                        break;
                    case CMDTextBox.CSMM_100_SO:
                        AddCSMM(2);
                        break;
                    case CMDTextBox.BUFF_ITEM:
                        buffItem();
                        break;
                    case CMDTextBox.DROP_RATE:
                        setDropRate(inputDlg.getInt(), inputDlg.getInt());
                        break;
                    default:
                        service.sendThongBao("Tính năng đang bảo trì.");
                        break;
                }
            } else {
                service.sendThongBao("Có lỗi xảy ra!");
            }
            inputDlg = null;
        }
    }

    public void setDropRate(int mob, int boss) {
        DropRateService.update(mob, boss);
        this.service.sendThongBao("Đã thiết lập tỉ lệ rơi đồ");
    }

    public void renameClan(String name) {
        if (clan == null) {
            return;
        }
        ClanMember clanMember = clan.getMember(this.id);
        if (clanMember == null || !clanMember.isLeader()) {
            return;
        }
        long now = System.currentTimeMillis();
        long time = now - clan.lastTimeRename;
        if (time < 60000) {
            service.sendThongBao(String.format("Vui lòng đợi %s nữa", Utils.timeAgo((int) ((60000 - time) / 1000))));
            return;
        }
        int length = name.length();
        if (length < 2 || length > 4 || !CharMatcher.javaLetterOrDigit().matchesAllOf(name)) {
            service.dialogMessage("Chỉ chấp nhận các ký tự a-z, 0-9 và chiều dài từ 2 đến 4 ký tự");
            return;
        }
        name = name.toUpperCase();
        clan.abbreviationName = name;
        clan.lastTimeRename = now;
        service.sendThongBao(String.format("[%s] OK", name));
    }

    public void AddCSMM(int type) {
        try {
            int num = inputDlg.getInt();
            ConSoMayMan.Add(this, type, num);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 93");
            service.sendThongBao("Có lỗi xảy ra!");
        }
    }

    public void treasure() {
        try {
            int level = inputDlg.getInt();
            if (level <= 0 || level > 110) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Con có chắc muốn đến").append("\n");
            sb.append(String.format("hang kho báu cấp độ %d ?", level));
            menus.add(new KeyValue(610, "Đồng ý", level));
            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
            service.openUIConfirm(NpcName.QUY_LAO_KAME, sb.toString(), head, menus);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 92");
            service.sendThongBao("Có lỗi xảy ra!");
        }
    }

    public void report() {
        String title = inputDlg.getText();
        String content = inputDlg.getText();
        String captcha = inputDlg.getText();
        if (Strings.isNullOrEmpty(title) || Strings.isNullOrEmpty(content) || Strings.isNullOrEmpty(captcha)) {
            return;
        }
        if (getCaptcha().equals(captcha)) {
            Report report = new Report();
            report.setTitle(title);
            report.setContent(content);
            report.setReporterID(this.id);
            report.setReporterName(this.name);
            report.setPlayerReportedID(playerReportedID);
            report.setPlayerReportedName(playerReportedName);
            report.save();
            service.dialogMessage("Báo cáo thành công!");
        } else {
            service.dialogMessage("Nhập mã không hợp lệ!");
        }

    }

    public void buffItem() {
        String iditem = inputDlg.getText();
        String quantity = inputDlg.getText();
        if (Strings.isNullOrEmpty(iditem) || Strings.isNullOrEmpty(quantity)) {
            return;
        }
        Item item = new Item(Integer.parseInt(iditem));
        item.quantity = Integer.parseInt(quantity);
        item.setDefaultOptions();
        addItem(item);
        if (item.template.isUpToUp) {
            this.service.sendThongBao("Bạn vừa nhận được " + item.quantity + " " + item.template.name);
        } else {
            this.service.sendThongBao("Bạn vừa nhận được " + item.template.name);
        }
        if (item.template.id == ItemName.THOI_VANG) {
            saveHistory(item.quantity, String.format("%s vừa nhận %d thỏi vàng từ lệnh CMD", this.name, item.quantity));
        }
    }

    public void useGiftCode() {
        lockAction.lock();
        try {
            long now = System.currentTimeMillis();
            if (now - lastTimeUseGiftCode < 5000) {
                service.dialogMessage("Thao tác quá nhanh");
                return;
            }
            lastTimeUseGiftCode = now;
            Server server = DragonBall.getInstance().getServer();
            Config config = server.getConfig();
            String code = inputDlg.getText().toLowerCase();
            int lent = code.length();
            if (code.isEmpty() || lent < 5 || lent > 30) {
                service.dialogMessage("Mã quà tặng có chiều dài từ 5 đến 30 ký tự");
                return;
            }
            List<GiftCodeData> codeDataList = GameRepository.getInstance().giftCode.findByCode(code);
            if (codeDataList.isEmpty()) {
                service.dialogMessage("Mã quà tặng không tồn tại");
                return;
            }
            GiftCodeData giftCodeData = codeDataList.get(0);
            long createTime = giftCodeData.createTime.getTime();
            long expiryTime = giftCodeData.expiryTime.getTime();
            if (createTime > now || now > expiryTime) {
                service.dialogMessage("Mã quà tặng không tồn tại hoặc đã hết hạn");
                return;
            }
            if (!GameRepository.getInstance().giftCodeHistory.findByGiftCodeIdAndPlayerId(giftCodeData.id, this.id)
                    .isEmpty()) {
                service.dialogMessage("Mỗi người chỉ được sử dụng 1 lần");
                return;
            }
            JSONArray arrItem = new JSONArray(giftCodeData.items);
            int size = arrItem.length();
            if (size > getSlotNullInBag()) {
                service.dialogMessage("Bạn không đủ chỗ trống trong hành trang");
                return;
            }
            GiftHistoryData historyData = new GiftHistoryData();
            historyData.createTime = new Timestamp(now);
            historyData.giftCodeId = giftCodeData.id;
            historyData.playerId = this.id;
            GameRepository.getInstance().giftCodeHistory.save(historyData);
            StringBuilder sb = new StringBuilder();
            sb.append("Chúc mừng, bạn đã được tặng").append("\b");
            for (int i = 0; i < size; i++) {
                JSONObject itemObj = (JSONObject) arrItem.get(i);
                Item newItem = new Item(itemObj.getInt("id"));
                newItem.quantity = itemObj.getInt("quantity");
                newItem.setDefaultOptions();
                if (itemObj.has("expire")) {
                    int expire = itemObj.getInt("expire");
                    if (expire > 0) {
                        newItem.addItemOption(new ItemOption(93, itemObj.getInt("expire")));
                    }
                }
                if (itemObj.has("options")) {
                    JSONArray arrOption = itemObj.getJSONArray("options");
                    newItem.addItemOptions(arrOption);
                }
                addItem(newItem);
                sb.append(String.format("- x%s %s", Utils.currencyFormat(newItem.quantity), newItem.template.name))
                        .append("\b");
            }
            if (giftCodeData.gold > 0) {
                addGold(gold);
                sb.append(String.format("- %s vàng", Utils.currencyFormat(gold))).append("\b");
            }
            if (giftCodeData.diamond > 0) {
                addDiamond(giftCodeData.diamond);
                sb.append(String.format("- %s ngọc xanh", Utils.currencyFormat(giftCodeData.diamond))).append("\b");
            }
            if (giftCodeData.diamondLock > 0) {
                addDiamondLock(giftCodeData.diamondLock);
                sb.append(String.format("- %s hồng ngọc", Utils.currencyFormat(giftCodeData.diamondLock))).append("\b");
            }
            String text = sb.toString();
            String[] arr = text.split("\\\b");
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                sb2.append(arr[i]);
                if (i % 10 == 0 && i != 0 && i != arr.length - 1) {
                    sb2.append("\n");
                } else {
                    sb2.append("\b");
                }
            }
            service.openUISay(NpcName.CON_MEO, sb2.toString(), getPetAvatar());
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 91");
            logger.error("failed!", ex);
        } finally {
            lockAction.unlock();
        }
    }

    public void deleteItemBoxCrackBall(int index, boolean isShow) {
        boxCrackBall.remove(index);
        if (isShow) {
            viewBoxCrackBall();
        }
    }

    public void deleteItemBoxCrackBall(int index) {
        boxCrackBall.remove(index);
        viewBoxCrackBall();
    }

    public void viewBoxCrackBall() {
        Shop shop = new Shop();
        shop.setTypeShop(4);
        Tab tab = new Tab();
        tab.setTabName("Vật\n phẩm");
        tab.setType(1);
        for (Item item : boxCrackBall) {
            ItemTemplate template = new ItemTemplate();
            template.id = (short) item.id;
            template.isNew = item.template.isNew;
            template.isPreview = item.template.isPreview;
            template.head = item.template.head;
            template.body = item.template.body;
            template.leg = item.template.leg;
            template.part = item.template.part;
            template.reason = "LUCKY DRAGON BALL";
            template.options = new ArrayList<>();
            if (item.quantity >= 1000) {
                template.options.add(new ItemOption(171, item.quantity / 1000));
            } else if (item.quantity > 1) {
                template.options.add(new ItemOption(31, item.quantity));
            }
            for (ItemOption option : item.options) {
                template.options.add(option);
            }
            tab.addItem(template);
        }
        shop.addTab(tab);
        this.shop = shop;
        service.viewShop(shop);
    }

    public void luckyRoundVQTD(byte numbers) {
        CrackBall crackBall = new CrackBall();
        crackBall.setPrice(1);
        crackBall.setTypePrice((byte) 0);
        crackBall.setIdTicket(821);
        crackBall.setType(CrackBall.VONG_QUAY_THUONG);
        crackBall.setPlayer(this);
        crackBall.setRandom();
        this.crackBall = crackBall;
        if (crackBall == null) {
            return;
        }
        if (numbers > 0 && numbers <= 100) {
            crackBall.setQuantity(numbers);
            crackBall.resultVQTD();
        }
    }

    public void luckyRound(Message ms) {
        try {
            if (crackBall == null) {
                return;
            }
            if (boxCrackBall.size() > 100) {
                service.sendThongBao("Rương quá đầy, vui lòng thử lại sau");
                return;
            }
            int typePrice = ms.reader().readByte();
            byte quantity = 0;
            if (ms.reader().available() > 0) {
                quantity = ms.reader().readByte();
            }
            if (typePrice == crackBall.getTypePrice()) {
                crackBall.show();
            } else {
                if (quantity > 0 && quantity <= 7) {
                    crackBall.setQuantity(quantity);
                    crackBall.result();
                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 90");
            logger.error("failed!", ex);
        }
    }

    public void buyFromPlayer(KeyValue keyValue) {
        lockAction.lock();
        try {
            if (isTrading) {
                return;
            }
            int playerId = ((Integer) keyValue.elements[0]).intValue();
            Player _player = zone.findCharByID(playerId);
            if (_player != null) {
                Item item = _player.itemBody[5];
                if (item != null) {
                    if (item.isCantSaleForPlay) {
                        return;
                    }
                    int gem = item.template.buyGem;
                    if (gem > 0) {
                        if (getSlotNullInBag() == 0) {
                            service.sendThongBao(Language.ME_BAG_FULL);
                            return;
                        }
                        if ((item.template.gender < 3 && item.template.gender == this.gender)
                                || item.template.gender >= 3) {
                            int km = 5;
                            int hoaHong = 20;
                            int giaSauKhiGiam = gem - ((gem * km) / 100);
                            int tienHoaHong = gem * hoaHong / 100;
                            if (giaSauKhiGiam > this.diamond) {
                                service.sendThongBao("Không đủ ngọc xanh");
                                return;
                            }
                            History history1 = new History(this.id, History.BUY_ITEM);
                            History history2 = new History(_player.id, History.BUY_ITEM);
                            history1.setBefores(this.gold, this.diamond, this.diamondLock);
                            history2.setBefores(_player.gold, _player.diamond, _player.diamondLock);
                            addDiamond(-giaSauKhiGiam);
                            _player.addDiamondLock(tienHoaHong);
                            _player.service.sendThongBao(this.name + " đã mua cải trang của bạn. Bạn nhận được "
                                    + tienHoaHong + " ngọc hồng");
                            Item item2 = item.clone();

                            history1.setAfters(this.gold, this.diamond, this.diamondLock);
                            history2.setAfters(_player.gold, _player.diamond, _player.diamondLock);
                            history1.addItem(item);
                            history2.addItem(item);
                            history1.setZone(zone);
                            history2.setZone(zone);
                            history1.setSeller(_player.id, tienHoaHong);
                            history2.setBuyer(this.id, giaSauKhiGiam);
                            history1.save();
                            history2.save();
                            addItem(item2);
                            service.sendThongBao(
                                    "Mua thành công " + item2.template.description + " từ " + _player.name);
                        }
                    }
                }
            } else {
                service.serverMessage2("Người chơi này không còn trong khu vực");
            }
        } finally {
            lockAction.unlock();
        }
    }

    public void combine(Message ms) {
        try {
            if (this.combine == null) {
                return;
            }
            if (isDead) {
                return;
            }
            if (isTrading) {
                return;
            }
            byte action = ms.reader().readByte();
            if (action == 1) {
                ArrayList<Byte> items = new ArrayList<>();
                byte size = ms.reader().readByte();
                for (int i = 0; i < size; i++) {
                    byte index = ms.reader().readByte();
                    if (index < 0 || index >= this.numberCellBag) {
                        continue;
                    }
                    if (this.itemBag[index] != null) {
                        if (!items.contains(index)) {
                            items.add(index);
                        }
                    }
                }
                this.combine.setItemCombine(items);
                this.combine.confirm();
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 89");
            logger.error("failed!", ex);
        }
    }

    public void confirmMenu(Message ms) {
        try {
            int npcId = ms.reader().readShort();
            byte select = ms.reader().readByte();
            if (menus == null) {
                return;
            }
            if (select < 0 || select >= menus.size() || menus.size() == 0) {
                return;
            }
            KeyValue<Integer, String> keyValue = menus.get(select);
            if (keyValue == null) {
                return;
            }
            menus.clear();
            if (npcId == NpcName.CON_MEO || npcId == NpcName.RONG_THIENG || npcId == NpcName.LY_TIEU_NUONG) {
                confirmKeyValue(keyValue, null);
            } else {
                Npc npc = zone.findNpcByID(npcId);
                if (npc != null) {
                    confirmKeyValue(keyValue, npc);
                    return;
                }

            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 88");
            logger.error("failed!", ex);
        }
    }

    public void clearMap() {
        try {
            if (this.typePk == 3) {
                Player _player = zone.findCharByID(this.testCharId);
                clearPk();
                resultPk((byte) 3);
                _player.clearPk();
                _player.resultPk((byte) 2);
            }
            if (isTrading) {
                trader.service.sendThongBao(Language.TRADE_FAIL);
                service.sendThongBao(Language.TRADE_FAIL);
                trader.clearTrade();
                clearTrade();
            }
            if (service != null) {
                service.clearMap();
            }
            if (menus != null) {
                this.menus.clear();
            }
            this.crackBall = null;
            this.shop = null;
            this.zone = null;
            this.combine = null;
            this.lucky = null;
            if (callDragon != null) {
                callDragon.close();
            }
            clearAmbientEffect();
        } catch (Exception e) {
        }
    }

    public void buyItem(Message ms) {
        try {
            if (isDead) {
                return;
            }
            if (isTrading) {
                return;
            }
            byte type = ms.reader().readByte();
            short itemID = ms.reader().readShort();
            int quantity = ms.reader().readInt();
            if (quantity <= 0) {
                return;
            }
            buyItem(type, itemID, quantity);
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 88");
            logger.error("failed!", ex);
        }
    }

    long lastChangeFlag = 0;

    public void changeFlag(Message ms) {
        try {
            Server server = DragonBall.getInstance().getServer();
            byte action = ms.reader().readByte();
            byte flagType = 0;
            if (action != 0) {
                flagType = ms.reader().readByte();
            }
            if (action == 0) {
                viewListFlag();
            } else if (action == 1) {
                if (zone.map.isBaseBabidi()) {
                    service.sendThongBao("Không thể thực hiện");
                    return;
                }
                if (zone.map.isBlackDragonBall()) {
                    service.sendThongBao("Không thể thực hiện");
                    return;
                }
                if (System.currentTimeMillis() - lastChangeFlag <= 60000) {
                    service.sendThongBao("Chưa thể đổi cờ lúc này,chờ "
                            + (((60000 + lastChangeFlag) - System.currentTimeMillis()) / 1000) + " giây nữa");
                    return;
                }
                if (flagType >= 0 && flagType < server.flags.size()) {
                    if (this.flag != flagType) {
                        setFlag(flagType);
                        lastChangeFlag = System.currentTimeMillis();
                    }
                }
            } else if (action == 2) {
                if (flagType >= 0 && flagType < server.flags.size()) {
                    ItemTemplate item = server.flags.get(flagType);
                    Message mss = new Message(Cmd.CHANGE_FLAG);
                    FastDataOutputStream ds = mss.writer();
                    ds.writeByte(2);
                    ds.writeByte(flagType);
                    ds.writeShort(item.iconID);
                    ds.flush();
                    service.sendMessage(mss);
                    mss.cleanup();
                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 87");
            logger.error("failed!", ex);
        }
    }

    public void viewListFlag() {
        try {
            Server server = DragonBall.getInstance().getServer();
            Message ms = new Message(Cmd.CHANGE_FLAG);
            FastDataOutputStream ds = ms.writer();
            ds.writeByte(0);
            ds.writeByte(server.flags.size());
            for (ItemTemplate itemTemplate : server.flags) {
                ds.writeShort(itemTemplate.id);
                ds.writeByte(itemTemplate.options.size());
                for (ItemOption option : itemTemplate.options) {
                    ds.writeShort(option.optionTemplate.id);
                    ds.writeInt(option.param);
                }
            }
            ds.flush();
            service.sendMessage(ms);
            ms.cleanup();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 86");
            logger.error("failed!", ex);
        }
    }

    public void clearPk() {
        this.testCharId = -9999;
        setTypePK((byte) 0);
    }

    public void resultPk(byte type) {
        switch (getCommandPK()) {
            case CMDPk.THACH_DAU: {
                if (type == 0) {
                    int gold = this.betAmount * 2;
                    gold -= gold / 10;
                    addGold(gold);
                    service.sendThongBao("Đối thủ đã kiệt sức, bạn thắng được " + gold + " vàng");
                }
                if (type == 1) {
                    service.sendThongBao("Bạn đã thua vì kiệt sức");
                }
                if (type == 2) {
                    int gold = this.betAmount * 2;
                    gold -= gold / 10;
                    addGold(gold);
                    service.sendThongBao("Đối thủ đã bỏ chạy, bạn thắng được " + gold + " vàng");
                }
                if (type == 3) {
                    service.sendThongBao("Bạn đã thua vì bỏ chạy");
                }
            }
            break;

            case CMDPk.TRA_THU: {
                if (type == 3) {
                    service.sendThongBao("Bạn đã bị xử thua");
                }
            }
            break;

            case CMDPk.DAI_HOI_VO_THUAT:
                // Arena arena = (Arena) zone;
                // arena.checkResult();
                break;
        }
        setCommandPK(CMDPk.NORMAL);
        if (type == 0 || type == 2) {
            if (achievements != null) {
                achievements.get(3).addCount(1);// trăm trận trăm tháng
            }
        }
    }

    public void giaoDich(Message ms) {
        if (this.session.user.getActivated() == 0) {
            service.sendThongBao("Tài khoản cần kích hoạt để có thể giao dịch");
            return;
        }
        try {
            Server server = DragonBall.getInstance().getServer();
            if (server.isMaintained) {
                service.sendThongBao("Không thể thực hiện khi máy chủ sắp bảo trì");
                return;
            }
            if (isDead) {
                return;
            }
            if (info.level < 7) {
                service.sendThongBao("Phải đạt cấp độ vệ binh trở lên mới có thể giao dịch");
                return;
            }
            Player _player = null;
            byte action = ms.reader().readByte();
            if (action == 0 || action == 1) {
                int playerId = ms.reader().readInt();
                _player = zone.findCharByID(playerId);
                if (_player != null) {
                    if (this.session.user.getActivated() == 0) {
                        service.sendThongBao("Tài khoản cần kích hoạt để có thể giao dịch");
                        return;
                    }
                    if (_player.session.user.getActivated() == 0) {
                        service.sendThongBao("Đối phương cần kích hoạt để có thể giao dịch");
                        return;
                    }
                    if (isTrading) {
                        service.sendThongBao("Bạn đang giao dịch với người khác");
                        return;
                    }
                    if (_player.info.level < 7) {
                        return;
                    }
                    if (_player.isTrading) {
                        service.sendThongBao("Người này đang giao dịch với người khác");
                        return;
                    }
                    if (_player.isDead) {
                        service.sendThongBao("Người này đã kiệt sức");
                        return;
                    }
                    if (action == 0) {
                        if (_player.invite.findCharInvite(Invite.GIAO_DICH, this.id) != null) {
                            service.sendThongBao("Vui lòng thử lại sau ít phút");
                            return;
                        }
                        _player.invite.addCharInvite(Invite.GIAO_DICH, this.id, 20000);
                        _player.service.giaoDich(this, (byte) 0, -1);
                    }
                    if (action == 1) {
                        if (invite.findCharInvite(Invite.GIAO_DICH, _player.id) != null) {
                            long now = System.currentTimeMillis();
                            this.isTrading = true;
                            this.goldTrading = 0;
                            this.status = Status.GIAO_DICH;
                            this.itemsTrading = new ArrayList<>();
                            this.trader = _player;
                            lastTimeTrade = now;

                            _player.isTrading = true;
                            _player.goldTrading = 0;
                            _player.status = Status.GIAO_DICH;
                            _player.itemsTrading = new ArrayList<>();
                            _player.trader = this;
                            _player.lastTimeTrade = now;
                            service.giaoDich(_player, (byte) 1, -1);
                            _player.service.giaoDich(this, (byte) 1, -1);
                        }
                    }
                } else {
                    service.sendThongBao("Người này không còn trong khu vực.");
                }
            }
            if (isTrading) {
                if (action == 2) {
                    if (this.status == Status.GIAO_DICH) {
                        byte index = ms.reader().readByte();
                        int num = ms.reader().readInt();
                        if (index == -1) {
                            if (num < 0 || num > this.gold || num > 500000000) {
                                return;
                            }
                            goldTrading = num;
                        } else {
                            if (index < 0 || index > this.numberCellBag) {
                                return;
                            }
                            Item item = this.itemBag[index];
                            if (item != null) {

                                if (item.isLock() || item.quantity < num) {
                                    service.giaoDich(this, (byte) 2, index);
                                    return;
                                }

                                if (num <= 0) {
                                    num = 1;
                                }
                                item = item.clone();
                                item.quantity = num;
                                if (!Utils.checkExistKey(index, itemsTrading)) {
                                    itemsTrading.add(new KeyValue(index, num, item.id));
                                }
                            }
                        }
                    }
                }
                if (action == 5 && this.status == Status.GIAO_DICH) {
                    this.status = Status.KHOA_GIAO_DICH;
                    trader.service.giaoDich(this, (byte) 6, -1);
                }
                if (action == 7 && this.status == Status.KHOA_GIAO_DICH) {
                    Config config = server.getConfig();
                    this.status = Status.DONG_Y_GIAO_DICH;
                    if (trader.status == Status.DONG_Y_GIAO_DICH) {
                        Gson g = new Gson();
                        HistoryTradeData historyTrader = new HistoryTradeData();
                        historyTrader.setPlayerName1(this.name);
                        historyTrader.setPlayerName2(trader.name);
                        historyTrader.setItemBefore1(UtilsNQMP.getStringTrade(itemBag));
                        historyTrader.setItemBefore2(UtilsNQMP.getStringTrade(trader.itemBag));
                        boolean error = false;
                        if (this.goldTrading > this.gold || trader.goldTrading > trader.gold) {
                            error = true;
                        }
                        if (!error) {
                            for (KeyValue<Byte, Integer> keyValue : itemsTrading) {
                                Item item = this.itemBag[keyValue.key];
                                if (item != null) {
                                    int quantity = keyValue.value;
                                    int id = ((Integer) keyValue.elements[0]).intValue();
                                    if (item.id != id || quantity > item.quantity) {
                                        error = true;
                                        break;
                                    }
                                    Item itm = trader.getItemInBag(item.id);
                                    if (itm != null && itm.quantity + quantity > config.getMaxQuantity()) {
                                        error = true;
                                        break;
                                    }
                                } else {
                                    error = true;
                                    break;
                                }
                            }

                        }
                        if (!error) {
                            for (KeyValue<Byte, Integer> keyValue : trader.itemsTrading) {
                                Item item = trader.itemBag[keyValue.key];
                                if (item != null) {
                                    int quantity = keyValue.value;
                                    int id = ((Integer) keyValue.elements[0]).intValue();
                                    if (item.id != id || quantity > item.quantity) {
                                        error = true;
                                        break;
                                    }

                                    Item itm = getItemInBag(item.id);
                                    if (itm != null && itm.quantity + quantity > config.getMaxQuantity()) {
                                        error = true;
                                        break;
                                    }
                                } else {
                                    error = true;
                                    break;
                                }
                            }
                        }
                        if (error) {
                            trader.service.sendThongBao(Language.TRADE_FAIL);
                            service.sendThongBao(Language.TRADE_FAIL);
                            trader.clearTrade();
                            clearTrade();
                        } else {
                            if (getSlotNullInBag() < trader.itemsTrading.size()) {
                                service.sendThongBao(Language.ME_BAG_FULL);
                                trader.service.sendThongBao(Language.PLAYER_BAG_FULL);
                                trader.clearTrade();
                                clearTrade();
                                return;
                            }
                            if (trader.getSlotNullInBag() < itemsTrading.size()) {
                                trader.service.sendThongBao(Language.ME_BAG_FULL);
                                service.sendThongBao(Language.PLAYER_BAG_FULL);
                                trader.clearTrade();
                                clearTrade();
                                return;
                            }

                            if (trader.goldTrading > 0) {
                                addGold(trader.goldTrading);
                            }

                            if (goldTrading > 0) {
                                addGold(-goldTrading);
                            }

                            if (goldTrading > 0) {
                                trader.addGold(goldTrading);
                            }

                            if (trader.goldTrading > 0) {
                                trader.addGold(-trader.goldTrading);
                            }
                            List<Item> itemTrader1 = new ArrayList<>();
                            List<Item> itemTrader2 = new ArrayList<>();

                            for (KeyValue<Byte, Integer> keyValue : itemsTrading) {
                                int index = keyValue.key;
                                Item item = this.itemBag[index];
                                if (item != null) {
                                    int quantity = keyValue.value;
                                    int id = ((Integer) keyValue.elements[0]).intValue();
                                    if (item.id == id || quantity <= item.quantity) {
                                        item = item.clone();
                                        item.quantity = quantity;
                                        itemTrader1.add(item);
                                        trader.addItem(item);
                                        removeItem(index, quantity);
                                    }

                                    if (item.template.id == ItemName.THOI_VANG) {
                                        saveHistory(item.quantity, String.format("%s vừa giao dịch %d thỏi vàng cho %s",
                                                this.name, item.quantity, trader.name));
                                    }

                                    if (item.template.id == ItemName.THOI_VANG) {
                                        saveHistory(-(item.quantity),
                                                String.format("%s vừa giao dịch %d thỏi vàng cho %s", this.name,
                                                        item.quantity, trader.name));

                                    }
                                }
                            }
                            for (KeyValue<Byte, Integer> keyValue : trader.itemsTrading) {
                                int index = keyValue.key;
                                Item item = trader.itemBag[index];
                                if (item != null) {
                                    int quantity = keyValue.value;
                                    int id = ((Integer) keyValue.elements[0]).intValue();
                                    if (item.id == id || quantity <= item.quantity) {
                                        item = item.clone();
                                        item.quantity = quantity;
                                        itemTrader2.add(item);
                                        addItem(item);
                                        trader.removeItem(index, quantity);
                                    }
                                }
                            }
                            trader.service.sendThongBao(Language.TRADE_SUCCESS);
                            service.sendThongBao(Language.TRADE_SUCCESS);
                            historyTrader.setItemAfter1(UtilsNQMP.getStringTrade(itemBag));
                            historyTrader.setItemAfter2(UtilsNQMP.getStringTrade(trader.itemBag));
                            historyTrader.setItemTrade1(UtilsNQMP.getStringTrade(itemTrader1));
                            historyTrader.setItemTrade2(UtilsNQMP.getStringTrade(itemTrader2));
                            historyTrader.setCreateTime(new Timestamp(System.currentTimeMillis()));
                            try {
                                GameRepository.getInstance().historyTradeRepository.save(historyTrader);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                this.saveData();
                                trader.saveData();
                            } catch (Exception ignored) {
                                System.err.println("Error at 85");
                                logger.error("save trade fail");
                            }
                            trader.sort();
                            sort();
                            trader.clearTrade();
                            clearTrade();
                        }
                    } else {
                        service.sendThongBao(Language.TRADE_WAIT);
                    }
                }
                if (action == 3 && this.status != Status.DONG_Y_GIAO_DICH) {
                    trader.service.sendThongBao(Language.TRADE_FAIL);
                    service.sendThongBao(Language.TRADE_FAIL);
                    trader.clearTrade();
                    clearTrade();
                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 84");
            logger.error("failed!", ex);
        }
    }

    public void clearTrade() {
        if (!(this.status == Status.DONG_Y_GIAO_DICH)) {
            this.service.giaoDich(null, (byte) 7, -1);
        }
        this.isTrading = false;
        this.goldTrading = 0;
        this.itemsTrading = null;
        this.trader = null;
        this.status = Status.NORMAL;

    }

    public void setTypePK(byte typePk) {
        this.typePk = typePk;
        if (zone != null) {
            zone.service.playerSetTypePk(this);
        }
    }

    public void playerVsPlayer(Message ms) {
        try {
            if (this.isDead) {
                return;
            }
            if (zone.map.isBaseBabidi()) {
                service.serverMessage2("Không thể thực hiện");
                return;
            }
            byte action = ms.reader().readByte();
            byte type = ms.reader().readByte();
            int playerId = ms.reader().readInt();
            if (type == 3 || type == 4) {
                Player _player = zone.findCharByID(playerId);
                if (_player != null) {
                    if (testCharId != -9999) {
                        service.serverMessage2("Bạn đang thách đấu người khác");
                        return;
                    }
                    if (_player.isDead) {
                        service.serverMessage2("Nhân vật này đã kiệt sức");
                        return;
                    }
                    if (_player.testCharId != -9999) {
                        service.serverMessage2("Người này đang thách đấu người khác");
                        return;
                    }
                    if (action == 0) {
                        if (_player.invite.findCharInvite(Invite.THACH_DAU, this.id) != null) {
                            service.serverMessage2("Vui lòng thử lại sau ít phút");
                            return;
                        }
                        menus.clear();
                        menus.add(new KeyValue(300, "1000 vàng", playerId));
                        menus.add(new KeyValue(301, "10000 vàng", playerId));
                        menus.add(new KeyValue(302, "100000 vàng", playerId));
                        service.openUIConfirm(NpcName.CON_MEO,
                                String.format("%s (Sức mạnh: %s)\n Bạn muốn cược bao nhiêu vàng?",
                                        _player.name, Utils.formatNumber(_player.info.power)),
                                getPetAvatar(), menus);
                    }
                    if (action == 1) {
                        if (invite.findCharInvite(Invite.THACH_DAU, playerId) != null) {
                            setCommandPK(CMDPk.THACH_DAU);
                            _player.setCommandPK(CMDPk.THACH_DAU);
                            this.testCharId = playerId;
                            _player.testCharId = this.id;
                            setTypePK((byte) 3);
                            _player.setTypePK((byte) 3);
                            this.betAmount = _player.betAmount;
                            addGold(-betAmount);
                            _player.addGold(-betAmount);
                        }
                    }
                } else {
                    service.serverMessage2("Người này không còn trong khu vực.");
                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 83");
            logger.error("failed!", ex);
        }
    }

    public void viewInfo(Message ms) {
        try {
            int id = ms.reader().readInt();
            Player _player = zone.findCharByID(id);
            if (_player != null && _player != this) {
                service.viewInfoPlayer(_player);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 82");
            logger.error("failed!", ex);
        }
    }

    public void playerMenu(Message ms) {
        try {
            int id = ms.reader().readInt();
            Player _player = zone.findCharByID(id);
            if (_player == null) {
                return;
            }
            if (_player.id > 0) {
                if (_player != null && _player != this) {
                    menu = new Menu(_player.id, "Chức năng", "", Menu.MENU);
                    service.addMenuPlayer(menu);
                }
            }

        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 81");
            logger.error("failed!", ex);
        }
    }

    public void gotoPlayer(Message ms) {
        try {
            // if (escortedPerson != null) {
            // service.sendThongBao(String.format("Bạn đang hộ tống %s, không thể thực
            // hiện.", escortedPerson.name));
            // return;
            // }
            int playerID = ms.reader().readInt();
            if (isHaveEquipTeleport) {
                Player player = SessionManager.findChar(playerID);
                if (player != null) {
                    if (player.isAnDanh) {
                        service.sendThongBao("Không tìm thấy vị trí người này");
                        return;
                    }
                    Zone z = player.zone;
                    if (z != null) {
                        if (z.getNumPlayer() >= z.getMaxPlayer()) {
                            service.sendThongBao("Khu vực đã đầy");
                            return;
                        }
                        if (checkCanEnter(z.map.mapID)) {
                            TMap map = z.map;
                            if (map.isUnableToTeleport()) {
                                service.sendThongBao("Bạn không thể tới đây");
                                return;
                            }
                            zone.service.addTeleport(this.id, (byte) 2);
                            zone.leave(this);
                            this.x = player.x;
                            this.y = player.y;
                            this.teleport = 2;
                            z.enter(this);
                            this.teleport = 0;
                        } else {
                            service.sendThongBao("Bạn chưa thể đến nơi này");
                        }
                    }
                } else {
                    service.sendThongBao("Người này không online");
                }
            } else {
                service.sendThongBao("Cần trang bị có khả năng dịch chuyển");
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 80");
            logger.error("failed!", ex);
        }
    }

    public void playerMenuAction(Message ms) {
        try {
            if (isDead) {
                return;
            }
            short select = ms.reader().readShort();
            if (menu != null) {
                int id = menu.getPlayerId();
                Player _player = zone.findCharByID(id);
                if (_player != null && _player != this) {
                    menus.clear();
                    if (this.info.power >= 15000000 && _player.itemBody[5] != null) {
                        Item item = _player.itemBody[5];
                        if (!item.isCantSaleForPlay) {
                            int gem = item.template.buyGem;
                            if (gem > 0) {
                                if ((item.template.gender < 3 && item.template.gender == this.gender)
                                        || item.template.gender >= 3) {
                                    menus.add(new KeyValue(CMDMenu.MUA_CAI_TRANG, "Cải trang " + gem + " ngọc xanh",
                                            _player.id));
                                }
                            }
                        }
                    }
                    menus.add(new KeyValue(CMDMenu.REPORT, "Báo cáo", _player.id));
                    service.openUIConfirm(5, "Hãy chọn điều muốn thực hiện", getPetAvatar(), menus);
                } else {
                    service.serverMessage2("Người này không còn trong khu vực");
                }
                menu = null;
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 79");
            logger.error("failed!", ex);
        }
    }

    public int getCurrentNumberFloorInBaseBabidi() {
        for (int i = 0; i < BaseBabidi.MAPS.length; i++) {
            if (BaseBabidi.MAPS[i] == zone.map.mapID) {
                return i;
            }
        }
        return -1;
    }

    public void addAccumulatedPoint(int point) {
        if (accumulatedPoint != null) {
            accumulatedPoint.addPoint(point);
            service.setPowerInfo(accumulatedPoint);
            int floor = getCurrentNumberFloorInBaseBabidi();
            if (accumulatedPoint.isMaxPoint() && floor + 1 < BaseBabidi.MAPS.length) {
                showMenuDownToNextFloor();
            }
        }
    }

    public void showMenuDownToNextFloor() {
        menus.clear();
        menus.add(new KeyValue(459, "OK"));
        String say = "Mau đi với ta xuống tầng tiếp theo";
        byte npcTemplateID = 5;
        short avatar = (short) ((flag == 9) ? 4390 : 4388);
        service.openUIConfirm(npcTemplateID, say, avatar, menus);
    }

    public void buyItem(int type, int itemID, int quantity) {
        if (this.shop == null) {
            return;
        }
        lockAction.lock();
        try {
            if (quantity < 0) {
                return;
            }
            int max_quantity = Server.getMaxQuantityItem();
            if (quantity > max_quantity) {
                service.sendThongBao("Chỉ có thể mua hoặc chứa tối đa " + max_quantity);
                return;
            }
            int typeShop = shop.getTypeShop();
            if (typeShop == 1) {
                List<ItemTemplate> list = shop.getListItem(this);
                ItemTemplate item = null;
                for (ItemTemplate itm : list) {
                    if (itm.id == itemID) {
                        item = itm;
                        break;
                    }
                }
                if (item == null) {
                    return;
                }
                if (item.require > info.power) {
                    service.sendThongBao("Sức mạnh không đủ yêu cầu");
                    return;
                }
                SkillBook book = Skills.getSkillBook(item.id);
                if (book != null) {
                    // Calculate total power requirement for all books
                    long totalPowerRequire = (long) item.powerRequire * quantity;
                    if (info.potential < totalPowerRequire) {
                        service.sendThongBao(String.format(Language.LEARN_SKILL_FAIL_1,
                                Utils.formatNumber(totalPowerRequire - info.potential)));
                        return;
                    }

                    Skill skill = getSkill(book.id);
                    int sLevel = 0;
                    if (skill != null) {
                        sLevel = skill.point;
                        if (skill.point >= book.level) {
                            return;
                        }
                    }

                    skill = Skills.getSkill((byte) book.id, (byte) book.level);
                    if (book.level > sLevel + quantity) {
                        service.sendThongBao(
                                String.format(Language.LEARN_SKILL_FAIL_2, skill.template.name, sLevel + 1));
                        return;
                    }

                    if (skill != null) {
                        menus.clear();
                        menus.add(new KeyValue(600, "Đồng ý x" + quantity, new Object[]{item, quantity}));
                        menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                        Npc npc = shop.getNpc();
                        service.openUIConfirm(npc.templateId,
                                String.format(Language.LEARN_SKILL, skill.template.name,
                                        Utils.currencyFormat(totalPowerRequire),
                                        Utils.getTimeAgo((int) (book.studyTime * quantity / 1000))),
                                npc.avatar, menus);
                    }
                }
            } else if (typeShop == 4) {
                boolean flagMap = this.zone != null && this.zone.map != null
                        && (this.zone.map.mapID == 45 || this.zone.map.mapID == 21 + this.gender);
                if (!flagMap) {
                    return;
                }
                if (this.zone.map.mapID == 45) {
                    if (itemID >= boxCrackBall.size()) {
                        return;
                    }
                    Item item = boxCrackBall.get(itemID);
                    if (type == 0) {// nhận
                        if (addItem(item)) {
                            if (item.quantity > 1) {
                                service.serverMessage2(
                                        String.format("Bạn nhận được %d %s", item.quantity, item.template.name));
                            } else {
                                service.serverMessage2(String.format("Bạn nhận được %s", item.template.name));
                            }
                            deleteItemBoxCrackBall(itemID);
                        } else {
                            service.serverMessage2(
                                    "Hành trang đã đầy, cần 1 ô trống trong hành trang để nhận vật phẩm");
                        }
                    }
                    if (type == 1) {// xóa
                        if (isNoNeedToConfirm) {
                            deleteItemBoxCrackBall(itemID);
                        } else {
                            menus.clear();
                            menus.add(new KeyValue(526, "Đồng ý", itemID));
                            menus.add(new KeyValue(527, "Xóa\nkhông cần\nhỏi lại", itemID));
                            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                            service.openUIConfirm(NpcName.CON_MEO,
                                    String.format("Bạn có chắc muốn xóa (mất luôn) vật phẩm %s ?", item.template.name),
                                    getPetAvatar(), menus);
                        }
                    }
                    if (type == 2) {// nhận tất
                        ArrayList<Item> list = new ArrayList<>();
                        for (Item itm : boxCrackBall) {
                            if (addItem(itm)) {
                                list.add(itm);
                                if (itm.quantity > 1) {
                                    service.serverMessage2(
                                            String.format("Bạn nhận được %d %s", itm.quantity, itm.template.name));
                                } else {
                                    service.serverMessage2(String.format("Bạn nhận được %s", itm.template.name));
                                }
                            }
                        }
                        boxCrackBall.removeAll(list);
                        viewBoxCrackBall();
                    }
                } else if (this.zone.map.mapID == 21 + this.gender) {
                    if (type == 0) {// nhận
                        ItemCMS_Service.getItem(this, itemID);
                    }
                    if (type == 1) {// xóa
                        ItemCMS_Service.deleteItem(this, itemID);
                    }
                    if (type == 2) {// nhận tất
                        ItemCMS_Service.getAll(this);
                    }
                }
            } else {
                List<ItemTemplate> list = shop.getListItem(this);
                ItemTemplate item = null;
                for (ItemTemplate itm : list) {
                    if (itm.id == itemID) {
                        item = itm;
                        break;
                    }
                }
                if (item == null) {
                    return;
                }

                if (shop.getNpcId() == -2) {
                    Server server = DragonBall.getInstance().getServer();
                    this.headDefault = server.iTemplates.get(itemID).part;
                    if (!this.isMask) {
                        this.head = this.headDefault;
                        service.setItemBody();
                        zone.service.playerLoadBody(this);
                    }
                    service.serverMessage2("Hớt tóc thành công.");
                    return;
                }

                // Calculate total costs based on quantity
                long buyGold = (long) item.buyGold * quantity;
                long buyGem = (long) item.buyGem * quantity;
                long buySpecial = (long) (item.buySpec) * (long) (quantity);

                if (type == 0 && item.buyGold == 0) {
                    return;
                }
                if (type == 1 && item.buyGem == 0) {
                    return;
                }
                if (type == 5 && item.buySpec == 0) {
                    return;
                }
                System.err.println(String.format("%s vừa mua %s với số lượng %d", this.name, item.name, quantity));
                int t = 1;
                long date = 3600000L;
                int npc = shop.getNpcId();
                // if (npc == 2001) {
                // t = 4;
                // g = 28800000L;
                // } else if (npc == 2002) {
                // t = 100;
                // g = 2592000000L;
                // }
                buyGold *= t;
                buyGem *= t;

                if (buyGold < 0 || buyGem < 0 || buySpecial < 0) {
                    return;
                }

                // Check special currency requirements
                if (item.iconSpec == 7223) {
                    if (clan == null) {
                        return;
                    }
                    ClanMember clanMember = clan.getMember(this.id);
                    if (clanMember == null) {
                        return;
                    }
                    if (clanMember.currClanPoint < buySpecial) {
                        service.serverMessage2("Bạn không có đủ capsule cá nhân");
                        return;
                    }
                }
                if (item.iconSpec == 4028) {
                    if (buySpecial > Integer.MAX_VALUE) {
                        service.sendThongBao("Số lượng quá nhiều");
                        return;
                    }
                    int index = getIndexBagById(ItemName.THOI_VANG);
                    Item tv = null;
                    if (index != -1) {
                        tv = itemBag[index];
                    }
                    if (tv == null) {
                        service.sendThongBao("Bạn không có thỏi vàng");
                        return;
                    }
                    if (tv.quantity < buySpecial) {
                        service.sendThongBao("Bạn không có đủ thỏi vàng");
                        return;
                    }
                    pointThoiVang += buySpecial;
                    isChangePoint = true;
                    removeItem(tv.indexUI, (int) buySpecial);
                }
                if (item.iconSpec == 930) {
                    if (this.gold < buySpecial) {
                        service.sendThongBao("Bạn không có đủ vàng");
                        return;
                    }
                    subGold(buySpecial);
                }

                // Handle food items with special requirements
                int itemFoodToBuy = 0;
                if (item.iconSpec >= 6324 && item.iconSpec <= 6328) {
                    buyGold = 500000000;
                    switch (item.iconSpec) {
                        case 6324:
                            itemFoodToBuy = ItemName.BANH_PUDDING;
                            break;
                        case 6325:
                            itemFoodToBuy = ItemName.XUC_XICH;
                            break;
                        case 6326:
                            itemFoodToBuy = ItemName.KEM_DAU;
                            break;
                        case 6327:
                            itemFoodToBuy = ItemName.MI_LY;
                            break;
                        case 6328:
                            itemFoodToBuy = ItemName.SUSHI;
                            break;
                    }

                    int index = getIndexBagById(itemFoodToBuy);
                    Item food = null;
                    if (index != -1) {
                        food = itemBag[index];
                    }
                    if (food == null) {
                        service.sendThongBao("Bạn không có đủ thức ăn");
                        return;
                    }
                    if (food.quantity < buySpecial) {
                        service.sendThongBao("Bạn không có đủ " + food.template.name);
                        return;
                    }
                    if (getGold() < buyGold) {
                        service.serverMessage2(
                                "Bạn không đủ tiền. Còn thiếu " + Utils.formatNumber(buyGold - getGold()));
                        return;
                    }

                    Item itemTL = null;
                    int indexItemTL = getIndexBagById(shopBillByGender(item.id));
                    if (indexItemTL != -1) {
                        itemTL = itemBag[indexItemTL];
                    }
                    if (itemTL == null) {
                        service.sendThongBao("Bạn không có đồ thần linh tương ứng");
                        return;
                    }
                    removeItem(food.indexUI, (int) buySpecial);
                    removeItem(itemTL.indexUI, quantity);
                }

                // Handle bag/box expansion items
                if (itemID == 517 || itemID == 518) {
                    if (item.require > this.info.power) {
                        service.serverMessage2("Bạn không đủ sức mạnh để thực hiện.");
                        return;
                    }
                    for (int i = 0; i < quantity; i++) {
                        if (itemID == 517) {
                            if (numberCellBag >= 100) {
                                service.serverMessage2("Hành trang chỉ có thể mở rộng tối đa 100 ô.");
                                return;
                            }
                            expandBag(1);
                        }
                        if (itemID == 518) {
                            if (numberCellBox >= 100) {
                                service.serverMessage2("Rương đồ chỉ có thể mở rộng tối đa 100 ô.");
                                return;
                            }
                            expandBox(1);
                        }
                    }

                    History history = new History(this.id, History.BUY_ITEM);
                    history.setBefores(this.gold, this.diamond, this.diamondLock);
                    this.gold -= buyGold;
                    subDiamond((int) buyGem);
                    history.setAfters(this.gold, this.diamond, this.diamondLock);
                    history.setExtras("item_id" + itemID);
                    history.setZone(zone);
                    history.save();
                    service.buy();
                    return;
                }
                if (itemID == 453) {
                    if (this.ship == 1) {
                        service.serverMessage2("Bạn đã có Chiến thuyền Tennis.");
                        return;
                    }
                    this.ship = 1;
                    this.gold -= buyGold;
                    subDiamond((int) buyGem);
                    service.buy();
                    service.serverMessage(String.format("Mua thành công %s", item.name));
                    return;
                }
                // Handle amulets
                if (item.type == 13) {
                    Amulet amulet = getAmulet(itemID);
                    if (amulet != null) {
                        amulet.expiredTime = System.currentTimeMillis() + (date * 650);
                    } else {
                        amulet = new Amulet();
                        amulet.id = item.id;
                        amulet.expiredTime = System.currentTimeMillis() + (date * 650);
                        addAmulet(amulet);
                    }
                    service.viewShop(shop);
                    History history = new History(this.id, History.BUY_ITEM);
                    history.setBefores(this.gold, this.diamond, this.diamondLock);
                    this.gold -= buyGold;
                    subDiamond((int) buyGem);
                    history.setAfters(this.gold, this.diamond, this.diamondLock);
                    history.setExtras("item_id" + itemID);
                    history.setZone(zone);
                    history.save();
                    service.buy();
                    service.sendThongBao(String.format("Mua thành công %s", item.name));
                    return;
                }

                // Handle special quantity multipliers
                int adjustedQuantity = quantity;
                int finalItemID = itemID;

                if (itemID == 193 || itemID == 361) {
                    adjustedQuantity *= 10;
                } else if (itemID == 293) {
                    finalItemID = 13; // Convert to corresponding item IDs (13,60-65)
                    adjustedQuantity *= 30;
                } else if (itemID >= 294 && itemID <= 299) {
                    finalItemID = itemID - 234; // Convert to corresponding item IDs (13,60-65)
                    adjustedQuantity *= 30;
                } else if (itemID == 596) {
                    finalItemID = 352;
                    adjustedQuantity *= 30;
                } else if (itemID == 597) {
                    finalItemID = 523;
                    adjustedQuantity *= 30;
                } else if (itemID == 934) {
                    itemID = 934;
                    item.options.clear();
                    adjustedQuantity *= 99;
                }
                // Create and setup new item
                Item item2 = new Item(finalItemID);
                boolean flag = false;

                // Process item options
                for (ItemOption o : item.options) {
                    ItemOption option = new ItemOption(o.optionTemplate.id, o.param);
                    if (o.optionTemplate.id == 1 || o.optionTemplate.id == 31
                            || o.optionTemplate.id == 11 || o.optionTemplate.id == 12
                            || o.optionTemplate.id == 13) {
                        option.param *= adjustedQuantity;
                        flag = true;
                    }
                    if (item2.isDoHD() && o.id != 21) {
                        option.param += option.param * (Utils.nextInt(16)) / 100;
                    }
                    item2.addItemOption(option);
                }
                if (item2.template.id == 934) {
                    item2.options.clear();
                    item2.setDefaultOptions();
                }
                // Add HD gear specific options
                if (item2.isDoHD()) {
                    int[] optionBonus = new int[]{77, 103, 50};
                    item2.addItemOption(
                            new ItemOption(optionBonus[Utils.nextInt(optionBonus.length)], Utils.nextInt(1, 5)));
                    item2.addItemOption(new ItemOption(30, 0));
                }

                // Handle non-stackable items with quantity
                if ((!item.isUpToUp() && quantity > 1)) {
                    for (int i = 0; i < quantity; i++) {
                        if (addItem(item2)) {
                            History history = new History(this.id, History.BUY_ITEM);
                            history.setBefores(this.gold, this.diamond, this.diamondLock);
                            this.gold -= buyGold / quantity;
                            subDiamond((int) buyGem / quantity);
                            if (item.iconSpec == 7223 && clan != null) {
                                ClanMember clanMember = clan.getMember(this.id);
                                if (clanMember != null) {
                                    clanMember.currClanPoint -= buySpecial / quantity;
                                    clan.clanInfo();
                                }
                            }
                            history.setAfters(this.gold, this.diamond, this.diamondLock);
                            history.addItem(item2);
                            history.setZone(zone);
                            history.save();
                        } else {
                            service.serverMessage2(Language.ME_BAG_FULL);
                            break;
                        }
                    }
                    service.sendThongBao(String.format("Mua thành công %s", item.name));
                    service.buy();
                } else {
                    // Handle stackable items
                    if (!flag) {
                        item2.quantity = adjustedQuantity;
                    } else {
                        item2.quantity = 1;
                    }

                    if (addItem(item2)) {
                        History history = new History(this.id, History.BUY_ITEM);
                        history.setBefores(this.gold, this.diamond, this.diamondLock);
                        this.gold -= buyGold;
                        subDiamond((int) buyGem);
                        if (item.iconSpec == 7223 && clan != null) {
                            ClanMember clanMember = clan.getMember(this.id);
                            if (clanMember != null) {
                                clanMember.currClanPoint -= buySpecial;
                                clan.clanInfo();
                            }
                        }
                        history.setAfters(this.gold, this.diamond, this.diamondLock);
                        history.addItem(item2);
                        history.setZone(zone);
                        history.save();
                        service.sendThongBao(String.format("Mua thành công %s", item.name));
                        service.buy();
                    } else {
                        service.serverMessage2(Language.ME_BAG_FULL);
                    }
                }
            }
        } finally {
            lockAction.unlock();
        }
    }

    public void addAmulet(Amulet amulet) {
        this.amulets.add(amulet);
    }

    public int getSlotNullInBag() {
        int number = 0;
        for (Item item : this.itemBag) {
            if (item == null) {
                number++;
            }
        }
        return number;
    }

    public boolean isBagFull() {
        for (Item item : this.itemBag) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    public int getSlotNullInBox() {
        int number = 0;
        for (Item item : this.itemBox) {
            if (item == null) {
                number++;
            }
        }
        return number;
    }

    public void sellGoldBar(long number) {
        if (number < 0) {
            service.sendThongBao("Vui lòng nhập đúng số lượng");
            return;
        }
        if (number > Integer.MAX_VALUE) {
            return;
        }
        if (isTrading) {
            return;
        }
        Item item = null;
        int index = this.getIndexBagById(ItemName.THOI_VANG);
        if (index < 0) {
            service.sendThongBao("Không tìm thấy thỏi vàng trong hành trang");
            return;
        }
        if (index < this.itemBag.length) {
            item = this.itemBag[index];
        }
        if (item == null) {
            return;
        }
        if (number > item.quantity) {
            service.sendThongBao("Số lượng không đủ");
            return;
        }
        removeItem(index, (int) number);
        long gold = item.template.resalePrice;
        gold *= number;
        addGold(gold);
        service.setItemBag();
        this.saveData();
        service.sendThongBao(String.format("Đã bán %s thu được %s vàng", name, Utils.currencyFormat(gold)));
        pointThoiVang += (int) number;
        isChangePoint = true;
    }

    public void saleItem(Message ms) {
        try {
            if (isTrading) {
                return;
            }
            if (shop == null) {
                service.sendThongBao("Không thể thực hiện");
                return;
            }
            byte action = ms.reader().readByte();
            byte type = ms.reader().readByte();
            short index = ms.reader().readShort();
            Item item = null;
            if (index < 0) {
                return;
            }
            if (type == 0) {
                if (index < this.itemBody.length) {
                    item = this.itemBody[index];
                }
            } else {
                if (index < this.itemBag.length) {
                    item = this.itemBag[index];
                }
            }
            if (item == null) {
                return;
            }

            String name = item.template.name;
            int quantity = item.quantity;
            long gold = item.template.resalePrice;
            // if (item.isCantSale || gold == -1) {
            // service.sendThongBao("Không thể bán vật phẩm này");
            // return;
            // }
            if (item.id == 457) {
                menus.clear();
                menus.add(new KeyValue(20230, "Bán theo số lượng"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(NpcName.CON_MEO, "Bạn có muốn bán nhiều không", getPetAvatar(), menus);
                return;
                // quantity = 1;
            }
            if (item.id != 457) {
                gold = 1;
            }
            gold *= quantity;
            if (action == 0) {
                Message mss = new Message(Cmd.ITEM_SALE);
                FastDataOutputStream ds = mss.writer();
                ds.writeByte(type);
                ds.writeShort(index);
                if (quantity > 1) {
                    ds.writeUTF(String.format("Bạn có muốn bán %dx %s với giá %s vàng?", quantity, name,
                            Utils.currencyFormat(gold)));
                } else {
                    ds.writeUTF(
                            String.format("Bạn có muốn bán %s với giá là %s vàng?", name, Utils.currencyFormat(gold)));
                }
                ds.flush();
                service.sendMessage(mss);
                mss.cleanup();
            }
            if (action == 1) {
                History history = new History(this.id, History.SELL_ITEM);
                history.setBefores(this.gold, this.diamond, this.diamondLock);
                addGold(gold);
                if (type == 0) {
                    this.itemBody[index] = null;
                    info.setInfo();
                    updateSkin();
                    service.setItemBody();
                    if (this.isMask) {
                        zone.service.updateBody((byte) 0, this);
                    } else {
                        zone.service.updateBody((byte) -1, this);
                    }
                    service.loadPoint();
                    zone.service.playerLoadBody(this);
                    update(item.template.type);
                    achievements.get(10).addCount(1);// trùm nhặt ve trai
                }
                if (type == 1) {
                    removeItem(index, quantity);
                    if (this.itemBag[index] == null) {
                        sort(index, true);
                    }
                    achievements.get(10).addCount(1);// trùm nhặt ve trai
                }

                history.setAfters(this.gold, this.diamond, this.diamondLock);
                history.addItem(item);
                history.setZone(zone);
                history.save();
                service.sendThongBao(String.format("Đã bán %s thu được %s vàng", name, Utils.currencyFormat(gold)));
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);

            System.err.println("Error at 78");
            logger.error("failed!", ex);
        }
    }

    public void removeItem(int index, int quantity) {
        Item item = this.itemBag[index];
        if (item != null) {
            if (item.template.id == 457) {
                writeLog(item, (byte) 1);
            }
            int quant = item.quantity;
            quant -= quantity;
            if (quant <= 0) {
                this.itemBag[index] = null;
                quant = 0;
            } else {
                item.quantity = quant;
            }
            service.updateBag(index, quant);
        }
    }

    public int getCountEmptyBag() {
        int size = 0;
        for (Item item : this.itemBag) {
            if (item == null || item.template == null) {
                size++;
            }

        }
        return size;
    }

    public void removeItemBox(int index, int quantity) {
        Item item = this.itemBox[index];
        if (item != null) {
            int quant = item.quantity;
            quant -= quantity;
            if (quant <= 0) {
                this.itemBox[index] = null;
                // sort(index);
                quant = 0;
            } else {
                this.itemBox[index].quantity = quant;
            }
            service.updateBox(index, quant);
        }
    }

    public int getIndexItemBoxByType(byte type) {
        int index = -1;
        for (int i = 0; i < this.itemBox.length; i++) {
            if (itemBox[i] != null && itemBox[i].template.type == type) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void writeLog(Item item, byte type) {
        try {
            // Lấy thời gian hiện tại để thêm vào log
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            // Lấy stack trace
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            // Tạo StringBuilder để build nội dung log
            StringBuilder logContent = new StringBuilder();
            // Thêm thông tin tên nhân vật và số lượng item
            logContent.append(type == 0 ? "    Add" : "    Remove").append("\n");
            logContent.append("Time :" + timestamp).append("\n");
            logContent.append("Name: ").append(this.name).append("\n");
            logContent.append("Quantity: ").append(item.quantity).append("\n");

            // Format và thêm stack trace
            for (int i = 2; i < stackTrace.length - 2; i++) {
                StackTraceElement element = stackTrace[i];
                logContent.append(String.format("  at %s.%s(%s:%d)\n",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getFileName(),
                        element.getLineNumber()));
            }
            logContent.append("=====================================\n\n");

            // Ghi vào file
            try (FileWriter fw = new FileWriter("logthoivang.txt", true); BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(logContent.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addItem(Item item) {
        if (item.template.id == 457) {
            writeLog(item, (byte) 0);
        }
        if (item.template.type == Item.TYPE_GOLD) {
            addGold(item.quantity);
            return true;
        }
        if (item.template.type == Item.TYPE_DIAMOND) {
            addDiamond(item.quantity);
            return true;
        }
        if (item.template.type == Item.TYPE_DIAMOND_LOCK) {
            addDiamondLock(item.quantity);
            return true;
        }
        if (item.template.type == Item.TYPE_AMULET) {
            Amulet amulet = getAmulet(item.id);
            if (amulet != null) {
                amulet.expiredTime += item.quantity;
            } else {
                amulet = new Amulet();
                amulet.id = item.id;
                amulet.expiredTime = System.currentTimeMillis() + item.quantity;
                addAmulet(amulet);
            }
            return true;
        }
        if (item.template.isUpToUp()) {
            int maxQuantity = Server.getMaxQuantityItem();
            int index = getIndexBagById(item.id);
            if (index != -1) {
                Item item2 = this.itemBag[index];
                boolean flag = false;
                for (ItemOption o : item2.options) {
                    if (o.optionTemplate.id == 1 || o.optionTemplate.id == 31 || o.optionTemplate.id == 11
                            || o.optionTemplate.id == 12 || o.optionTemplate.id == 13) {
                        for (ItemOption o2 : item.options) {
                            if (o.optionTemplate.id == o2.optionTemplate.id) {
                                o.param += o2.param;
                                service.setItemBag();
                                flag = true;
                            }
                        }
                    }
                }
                if (flag) {
                    return true;
                }
                if (item2.quantity + item.quantity > maxQuantity) {
                    return false;
                }
                if (item2.quantity == 0) {
                    item2.quantity = 1;
                }
                if (item.quantity == 0) {
                    item.quantity = 1;
                }
                item2.quantity += item.quantity;

                if (item.template.type == Item.TYPE_DAUTHAN) {
                    service.setItemBag();
                } else {
                    service.updateBag(index, item2.quantity);

                }
                return true;
            }
        }
        for (int i = 0; i < itemBag.length; i++) {
            if (itemBag[i] == null) {
                itemBag[i] = item;
                item.indexUI = i;
                service.setItemBag();
                return true;
            }
        }
        return false;
    }

    public void addMerit(int itemID, int point) {
        Item item = getItemInBag(itemID);
        if (item != null) {
            ItemOption o = item.getItemOption(11);
            if (o != null) {
                o.param += point;
                service.refreshItem((byte) 1, item);
            }
        }
    }

    public int getMerit(int itemID) {
        Item item = getItemInBag(itemID);
        if (item != null) {
            ItemOption o = item.getItemOption(11);
            if (o != null) {
                return o.param;
            }
        }
        return 0;
    }

    public void addNumberOfUse(int itemID, int point) {
        Item item = getItemInBag(itemID);
        if (item != null) {
            ItemOption o = item.getItemOption(12);
            if (o != null) {
                o.param += point;
                service.refreshItem((byte) 1, item);
            }
        }
    }

    public int getNumberOfUse(int itemID) {
        Item item = getItemInBag(itemID);
        if (item != null) {
            ItemOption o = item.getItemOption(12);
            if (o != null) {
                return o.param;
            }
        }
        return 0;
    }

    public void addYoukaiHaveFallen(int itemID, int point) {
        Item item = getItemInBag(itemID);
        if (item != null) {
            ItemOption o = item.getItemOption(13);
            if (o != null) {
                o.param += point;
                service.refreshItem((byte) 1, item);
            }
        }
    }

    public int getYoukaiHaveFallen(int itemID) {
        Item item = getItemInBag(itemID);
        if (item != null) {
            ItemOption o = item.getItemOption(13);
            if (o != null) {
                return o.param;
            }
        }
        return 0;
    }

    public boolean addItemToBox(Item item) {
        if (item.template.isUpToUp()) {
            int maxQuantity = Server.getMaxQuantityItem();
            int index = getIndexBoxById(item.id);
            if (index != -1) {
                Item item2 = this.itemBox[index];
                if (item.id == 521) {
                    item2.options.get(0).param += item.options.get(0).param;
                    service.setItemBox();
                } else {
                    if (item2.quantity >= maxQuantity) {
                        return false;
                    }
                    item2.quantity += item.quantity;
                    if (item2.quantity > maxQuantity) {
                        item2.quantity = maxQuantity;
                    }
                    if (item.template.type == Item.TYPE_DAUTHAN) {
                        service.setItemBox();
                    } else {
                        service.updateBox(index, item2.quantity);
                    }
                }
                return true;
            }
        }
        for (int i = 0; i < itemBox.length; i++) {
            if (itemBox[i] == null) {
                itemBox[i] = item;
                item.indexUI = i;
                service.setItemBox();
                return true;
            }
        }
        return false;
    }

    public void subDiamond(int diamond) {
        if (this.diamondLock < diamond) {
            diamond -= this.diamondLock;
            this.diamondLock = 0;
            this.diamond -= diamond;
        } else {
            this.diamondLock -= diamond;
        }
        service.loadInfo();
    }

    public void subGold(long gold) {
        this.gold -= gold;
        service.loadInfo();
    }

    public boolean subThoiVang(int thoivang) {
        Item item = this.getItemInBag(457);
        if (item == null || item.quantity < thoivang) {
            return false;
        }
        this.removeItem(item.indexUI, thoivang);
        return true;
    }

    public void throwItem(Message ms) {
        if (isDead) {
            return;
        }
        if (isTrading) {
            return;
        }
    }

    public void pickItem(ItemMap itemMap, int distance) {
        itemMap.lock.lock();
        try {
            if (!itemMap.isPickedUp) {
                if (distance < 100) {
                    if (itemMap.playerID != -1) {
                        if (itemMap.playerID != this.id && (itemMap.item.template.type == Item.TYPE_NHIEMVU
                                || System.currentTimeMillis() - itemMap.throwTime < 20000)) {
                            if (itemMap.item.template.id != 568) {
                                service.sendThongBao("Vật phẩm của người khác");
                            } else {
                                service.sendThongBao("Vui lòng chờ "
                                        + (20 - ((System.currentTimeMillis() - itemMap.throwTime) / 1000)) + "s nữa");
                            }

                            return;
                        }
                    }

                    String text = "";
                    Item item = itemMap.item;
                    if (item.id == ItemName.NGOC_RONG_1_SAO_DEN || item.id == ItemName.NGOC_RONG_2_SAO_DEN
                            || item.id == ItemName.NGOC_RONG_3_SAO_DEN || item.id == ItemName.NGOC_RONG_4_SAO_DEN
                            || item.id == ItemName.NGOC_RONG_5_SAO_DEN || item.id == ItemName.NGOC_RONG_6_SAO_DEN
                            || item.id == ItemName.NGOC_RONG_7_SAO_DEN) {
                        if (zone.map.isBlackDragonBall()) {
                            MBlackDragonBall m = MapManager.getInstance().blackDragonBall;
                            if (zone.isDoneNRD) {
                                service.sendThongBao("Cuộc chiến đã kết thúc , hãy quay lại vào ngày mai");
                                return;
                            }
                            if (m != null) {
                                if (zone.isDoneNRD) {
                                    service.sendThongBao("Cuộc chiến đã kết thúc , hãy quay lại vào ngày mai");
                                    return;
                                }
                                if (itemMap.countDown > 0) {
                                    service.sendThongBao(String.format("Chưa thể nhặt lúc này, hãy đợi %d giây nữa",
                                            itemMap.countDown));
                                    return;
                                }
                            }

                        }
                    }
                    if (item.isDragonBallNamec()) {
                        Server server = DragonBall.getInstance().getServer();
                        if (idNRNM == -1) {
                            idNRNM = item.template.id;
                            server.mapNrNamec[item.template.id - 353] = this.zone.map.mapID;
                            server.nameNrNamec[item.template.id - 353] = this.zone.map.name;
                            server.zoneNrNamec[item.template.id - 353] = (byte) this.zone.zoneID;
                            server.pNrNamec[item.template.id - 353] = this.name;
                            server.idpNrNamec[item.template.id - 353] = (int) this.id;
                            this.lastTimePickUpDragonBallNamec = System.currentTimeMillis();
                            this.setTypePK((byte) 5);
                        } else {
                            service.serverMessage("Bạn đã mang ngọc rồng trên người");
                            return;
                        }
                    }
                    if (item.template.id == 362) {
                        service.serverMessage("Không thể nhặt vật phẩm này");
                        return;
                    }
                    itemMap.isPickedUp = true;
                    if (item.quantity == 1) {
                        text = String.format("Bạn nhặt được %s", item.template.name);
                    }
                    if (item.id == ItemName.DUI_GA && taskMain.id == 2 && taskMain.index == 0) {
                        updateTaskCount(1);
                    }
                    if (item.id == ItemName.NGOC_RONG_1_SAO_DEN || item.id == ItemName.NGOC_RONG_2_SAO_DEN
                            || item.id == ItemName.NGOC_RONG_3_SAO_DEN || item.id == ItemName.NGOC_RONG_4_SAO_DEN
                            || item.id == ItemName.NGOC_RONG_5_SAO_DEN || item.id == ItemName.NGOC_RONG_6_SAO_DEN
                            || item.id == ItemName.NGOC_RONG_7_SAO_DEN
                            || item.id == ItemName.NGOC_RONG_NAMEK_1_SAO || item.id == ItemName.NGOC_RONG_NAMEK_2_SAO
                            || item.id == ItemName.NGOC_RONG_NAMEK_3_SAO || item.id == ItemName.NGOC_RONG_NAMEK_4_SAO
                            || item.id == ItemName.NGOC_RONG_NAMEK_5_SAO || item.id == ItemName.NGOC_RONG_NAMEK_6_SAO
                            || item.id == ItemName.NGOC_RONG_NAMEK_7_SAO) {
                        if (this.session.user.getActivated() == 0) {
                            service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
                            itemMap.isPickedUp = false;
                            return;
                        }
                        itemLoot = item;
                        updateBag();
                        if (zone.map.isBlackDragonBall()) {
                            ZBlackDragonBall z = (ZBlackDragonBall) zone;
                            z.itemBlackDragonBall.countDown = 300;
                            z.setPlayerHolding(this);
                        }
                    } else if (item.id == ItemName.DUA_BE) {
                        itemMap.isPickedUp = false;
                        if (taskMain.id == 3 && taskMain.index == 1) {
                            text = "Wow, một đứa bé dễ thương";
                            taskNext();
                            updateSkin();
                            zone.service.updateBag(this);
                            zone.service.playerLoadWeapon(this);
                        } else {
                            return;
                        }
                    } else if (item.id == ItemName.DUI_GA_NUONG) {
                        info.recovery(Info.ALL, 100, true);
                        service.sendThongBao(String.format("Bạn vừa ăn %s", item.template.name));
                    } else if (item.id == ItemName.CA_CHUA || item.id == ItemName.SOCOLA) {
                        info.recovery(Info.HP, 100, true);
                        service.sendThongBao(String.format("Bạn vừa ăn %s", item.template.name));
                    } else if (item.id == ItemName.CA_ROT) {
                        info.recovery(Info.MP, 100, true);
                        service.sendThongBao(String.format("Bạn vừa ăn %s", item.template.name));
                    } else {
                        if (item.id == ItemName.TRUYEN_TRANH && taskMain.id == 14 && taskMain.index == 1) {
                            taskNext();
                            text = "Bạn nhặt được Truyện tranh tập 2";
                        }
                        switch (item.template.type) {
                            case 9:
                                this.gold += item.quantity;
                                text = "";
                                break;

                            case 10:
                                achievements.get(15).addCount(1);// Trùm nhặt ngọc
                                this.diamond += item.quantity;
                                text = "";
                                break;

                            case 34:
                                achievements.get(15).addCount(1);// Trùm nhặt ngọc
                                this.diamondLock += item.quantity;
                                text = "";
                                break;

                            default:
                                try {
                                    if (getSlotNullInBag() == 0 && !(this instanceof VirtualBot)) {
                                        itemMap.isPickedUp = false;
                                        service.sendThongBao(Language.ME_BAG_FULL);
                                        return;
                                    }
                                } catch (Exception e) {
                                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                                    System.err.println("Error at 77");
                                }
                                if (item.template.type <= 4 && itemMap.playerID == this.id) {
                                    if (itemMap.isThrowFromMob) {
                                        if (Math.abs(itemMap.mobLevel - info.level) < 4) {
                                            if (Utils.nextInt(10) == 0) {
                                                int r = Utils.nextInt(15);
                                                int hole = 1;
                                                if (r == 0) {
                                                    hole = 3;
                                                } else if (r < 5) {
                                                    hole = 2;
                                                }
                                                item.addItemOption(new ItemOption(107, hole));
                                            }
                                        }
                                        /*
                                         * if (itemMap.killerIsHuman) {
                                         * boolean isFlag = false;
                                         * int mapID = zone.map.mapID;
                                         * if ((mapID == 1 || mapID == 2 || mapID == 3 || mapID == 8 || mapID == 9
                                         * || mapID == 11 || mapID == 15 || mapID == 16 || mapID == 17)
                                         * && zone.zoneID < 10) {
                                         * isFlag = true;
                                         * }
                                         * if (isNewMember && !isFlag) {
                                         * if (Utils.nextInt(100 + (info.level * 30)) == 0) {
                                         * int index = Utils.nextInt(3);
                                         * int[] o = Mob.OPTIONS[gender][index];
                                         * item.addItemOption(new ItemOption(o[0], 0));
                                         * item.addItemOption(new ItemOption(o[1], 0));
                                         * item.addItemOption(new ItemOption(30, 0));
                                         * }
                                         * }
                                         * }
                                         */
                                    }
                                }
                                addItem(item);
                                History history = new History(this.id, History.PICK_ITEM);
                                history.setBefores(gold, diamond, diamondLock);
                                history.setAfters(gold, diamond, diamondLock);
                                history.addItem(item);
                                history.setZone(zone);
                                history.save();
                                break;
                        }
                    }
                    zone.service.playerPickItem(itemMap, this, text);
                    zone.removeItemMap(itemMap, false);
                }
            }
        } finally {
            itemMap.lock.unlock();
        }
    }

    public void dropItemSpe() {
        if (itemLoot != null) {
            if (itemLoot.isDragonBallNamec()) {
                setTypePK((byte) 0);
                lastTimePickUpDragonBallNamec = 0;
                if (itemLoot.isDragonBallNamec()) {
                    if (delayToCallDragonNamek > 0 && delayToCallDragonNamek < System.currentTimeMillis()) {
                        delayToCallDragonNamek = System.currentTimeMillis();
                    }
                }
                idNRNM = -1;
                ItemMap itemMap = new ItemMap(zone.autoIncrease++);
                Item item = new Item(itemLoot.template.id);
                itemMap.throwTime = System.currentTimeMillis();
                itemMap.isPickedUp = false;
                itemMap.item = item;
                itemMap.playerID = -1;
                switch (this.zone.map.mapID) {
                    case 8:
                        itemMap.x = (short) 553;
                        itemMap.y = (short) 288;
                        break;
                    case 9:
                        itemMap.x = (short) 634;
                        itemMap.y = (short) 432;
                        break;
                    case 10:
                        itemMap.x = (short) 711;
                        itemMap.y = (short) 288;
                        break;
                    case 11:
                        itemMap.x = (short) 1078;
                        itemMap.y = (short) 336;
                        break;
                    case 12:
                        itemMap.x = (short) 1300;
                        itemMap.y = (short) 288;
                        break;
                    case 13:
                        itemMap.x = (short) 323;
                        itemMap.y = (short) 432;
                        break;
                    case 31:
                        itemMap.x = (short) 606;
                        itemMap.y = (short) 312;
                        break;
                    case 32:
                        itemMap.x = (short) 650;
                        itemMap.y = (short) 360;
                        break;
                    case 33:
                        itemMap.x = (short) 1325;
                        itemMap.y = (short) 360;
                        break;
                    case 43:
                        itemMap.x = (short) 315;
                        itemMap.y = (short) 432;
                        break;
                    case 34:
                    case 7:
                        itemMap.x = (short) 643;
                        itemMap.y = (short) 432;
                        break;
                    default:
                        itemMap.x = getX();
                        itemMap.y = getY();
                        break;
                }
                itemMap.isDragonBallNamec = true;
                zone.addItemMap(itemMap);
                zone.service.addItemMap(itemMap);
            }
            itemLoot = null;
            updateBag();
            // if (getPhuX() > 0) {
            // setPhuX(0);
            // info.setInfo();
            // service.loadPoint();
            // }
        }
    }

    public void pickItem(Message ms) {
        try {
            if (isDead) {
                return;
            }
            if (isTrading) {
                return;
            }
            int itemMapID = ms.reader().readShort();
            ItemMap itemMap = zone.findItemMapByID(itemMapID);
            long now = System.currentTimeMillis();
            if (now - lastPickup >= 500) {
                lastPickup = now;
                if (itemMap != null) {
                    int distance = Utils.getDistance(this.x, this.y, itemMap.x, itemMap.y);
                    pickItem(itemMap, distance);
                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 76");
            logger.error("failed!", ex);
        }
    }

    public Skill getSkillByID(int id) {
        for (Skill skill : skills) {
            if (skill.template.id == id) {
                return skill;
            }
        }
        return null;
    }

    public void useItem(Message ms) {
        lockAction.lock();
        try {
            if (isDead) {
                return;
            }
            if (isTrading) {
                service.sendThongBao(Language.TRADE_FAIL2);
                return;
            }
            byte type = ms.reader().readByte();
            byte where = ms.reader().readByte();
            byte index = ms.reader().readByte();
            short templateID = -1;
            if (index == -1) {
                templateID = ms.reader().readShort();
            }
            useItem(type, where, index, templateID);
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 75");
            logger.error("failed!", ex);
        } finally {
            lockAction.unlock();
        }
    }

    public void transformIntoChocolate(int dameDown, int time) {
        isChocolate = true;
        this.dameDown = dameDown;
        ItemTime item = new ItemTime(ItemTimeName.SOCOLA, 4127, time, false);
        addItemTime(item);
        updateSkin();
        if (dameDown == 0) {
            info.setInfo();
            service.loadPoint();
            zone.service.playerLoadBody(this);
        }
        zone.service.updateBody((byte) 0, this);
    }

    public void transformIntoStone(int time) {
        isStone = true;
        ItemTime item = new ItemTime(ItemTimeName.STONE, 4392, time, false);
        addItemTime(item);
        updateSkin();
        if (zone != null) {
            zone.service.updateBody((byte) 0, this);
        }
        service.setEffect(null, this.id, Skill.ADD_EFFECT, Skill.CHARACTER, (byte) 42);
    }

    public boolean doUsePotion() {
        if (itemBag == null) {
            return false;
        }
        for (int i = 0; i < itemBag.length; i++) {
            if (itemBag[i] != null && itemBag[i].template.type == Item.TYPE_DAUTHAN) {
                useItem((byte) 0, (byte) 1, (byte) -1, itemBag[i].template.id);
                return true;
            }
        }
        return false;
    }

    public boolean useItemBonus(Item item) {
        if (item == null) {
            return false;
        }
        switch (item.template.id) {
            case 2262:
                setItemTime(ItemTimeName.BANH_CUPCAKE, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);

                return true;
            case 2263:
                setItemTime(ItemTimeName.BANH_DONUT, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);

                return true;
            case 2264:
                setItemTime(ItemTimeName.BANH_KEM_CHOCOLATE, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);

                return true;
            case 2265:
                setItemTime(ItemTimeName.BANH_KEM_NHO, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);

                return true;
            case 2266:
                setItemTime(ItemTimeName.BANH_TRAI_CAY, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);

                return true;
            case 2267:
                setItemTime(ItemTimeName.KEO_MUT_XOAN, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);

                return true;
            case 2270:
                setItemTime(ItemTimeName.NUOC_MAY_MAN, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);

                return true;
            case 2274:
                setItemTime(ItemTimeName.DAU_THAN_KY, item.template.iconID, true, 10 * 60);
                this.removeItem(item.indexUI, 1);
                return true;
            case 2278:
            case 2279:
            case 2280:
            case 2281:
            case 2282:
            case 2283:
            case 2284:
            case 2285:
                setItemTime((byte) (70 + (item.template.id - 2278)), item.template.iconID, true, 10 * 60);
                info.setInfo();
                service.loadPoint();
                this.removeItem(item.indexUI, 1);
                return true;
        }
        return false;
    }

    public void useItem(byte type, byte where, byte index, short templateID) {
        Item item = null;
        if (templateID != -1) {
            index = (byte) getIndexBagById(templateID);
        }
        if (index != -1) {
            if (where == 0) {
                item = this.itemBody[index];
            } else if (where == 1) {
                item = this.itemBag[index];
            }
        }
        if (item != null) {
            if (type == 0) {
                if (Event1.useItem(this, item)) {
                    return;
                }

                if (useItemBonus(item)) {
                    info.setInfo();
                    service.loadPoint();
                    return;
                }
                if (item.template.gender < 3 && this.gender != item.template.gender) {
                    service.sendThongBao("Vật phẩm không phù hợp với hành tinh của bạn.");
                    return;
                }
                if (item.require > this.info.power) {
                    service.sendThongBao("Sức mạnh không đủ để sử dụng.");
                    return;
                }
                if (item.template.type == Item.TYPE_SACH) {
                    service.useItem(type, where, index, String.format("Bạn có muôn dùng\n%s?", item.template.name));
                } else if (item.template.type == Item.TYPE_DAUTHAN) {
                    long now = System.currentTimeMillis();
                    if (now - lastUsePotion >= 10000) {
                        lastUsePotion = now;
                        ItemOption option = item.options.get(0);
                        int recovery = 0;
                        if (option.id == 48) {
                            recovery += option.param;
                        }
                        if (option.id == 2) {
                            recovery += option.param * 1000;
                        }
                        info.recovery(Info.ALL, recovery);
                        zone.service.playerLoadHP(this, (byte) 1);
                        if (myDisciple != null && !myDisciple.isDead()) {
                            myDisciple.info.recovery(Info.ALL, recovery);
                            int p = (int) (item.template.level * 10);
                            myDisciple.info.updateStamina(myDisciple.info.maxStamina * p / 100);
                            zone.service.playerLoadHP(myDisciple, (byte) 1);
                            service.chat(myDisciple, "Cảm ơn sư phụ");
                        }
                        removeItem(item.indexUI, 1);
                    }
                } else if (item.template.type == 27 || item.template.type == 25) {
                    typeOrder(item);
                } else if (item.template.type == 29) {
                    typeItemTime(item);
                } else if (item.template.type == Item.TYPE_BALO) {
                    typeClan(item);
                } else if (item.template.type == 22) {
                    typeItemMap(item, 0, type, where, index);
                } else if (item.template.type == 33) {
                    useCollectibleCard(item);
                } else if (item.template.type == Item.TYPE_NGOCRONG) {
                    typeCallDragon(item);
                }
            }
            if (type == 1) {

                service.useItem(type, where, index,
                        String.format("Bạn có chắc muốn hủy bỏ (mất luôn)\n%s ?", item.template.name));
            }
            if (type == 2) {

                if (where == 0) {
                    History history = new History(this.id, History.THROW_ITEM);
                    history.setBefores(gold, diamond, diamondLock);
                    history.setAfters(gold, diamond, diamondLock);
                    history.addItem(item);
                    history.setZone(zone);
                    history.setExtras("Bỏ ra mất luôn");
                    history.save();
                    itemBody[index] = null;
                    service.setItemBody();
                    updateSkin();
                    info.setInfo();
                    service.loadPoint();
                    zone.service.playerLoadBody(this);
                    zone.service.updateBody((byte) 0, this);
                } else {
                    removeItem(index, item.quantity);
                }

            }
            if (type == 3) {
                if (item.template.type == 22) {
                    typeItemMap(item, 1, type, where, index);
                }
                if (item.template.type == Item.TYPE_SACH) {
                    SkillBook book = Skills.getSkillBook(item.id);
                    if (book == null) {
                        return;
                    }
                    int skill_id = book.id;
                    int level = book.level;
                    int indexSkill = -1;
                    Skill skillOld = null;
                    int i = 0;
                    for (Skill skill : this.skills) {
                        if (skill.template.id == skill_id) {
                            skillOld = skill;
                            indexSkill = i;
                            break;
                        }
                        i++;
                    }
                    if (skillOld == null) {
                        if (level == 1) {
                            Skill skill = Skills.getSkill(this.gender, skill_id, level);
                            if (skill != null) {
                                learnSkill(skill, 0, -1, item);
                            }
                        } else {
                            service.sendThongBao("Bạn hãy học kỹ năng này trước.");
                        }
                    } else {
                        if (skillOld.point + 1 == level) {
                            Skill skillNew = null;
                            for (Skill skill : skillOld.template.skills) {
                                if (skill.point == level) {
                                    skillNew = skill;
                                    break;
                                }
                            }
                            if (skillNew != null) {
                                learnSkill(skillNew, 1, indexSkill, item);
                            }
                        } else {
                            int plevel = (skillOld.point + 1);
                            if (plevel <= skillOld.template.skills.size()) {
                                service.sendThongBao(
                                        String.format(Language.LEARN_SKILL_FAIL_2, skillOld.template.name, plevel));
                            } else {
                                service.sendThongBao(skillOld.template.name + " đã đạt cấp tối đa");
                            }
                        }
                    }
                }
            }
            if (itemBag[index] == null && where == 1) {
                sort(index, true);// sắp xếp
            }
        }
    }

    private void typeCallDragon(Item item) {
        switch (item.id) {

            case ItemName.NGOC_RONG_1_SAO:
                if (!zone.map.isLang()) {
                    service.sendThongBao("Hãy về làng để thực hiện");
                    return;
                }
                int indexDragonBall1Star = -1;
                int indexDragonBall2Star = -1;
                int indexDragonBall3Star = -1;
                int indexDragonBall4Star = -1;
                int indexDragonBall5Star = -1;
                int indexDragonBall6Star = -1;
                int indexDragonBall7Star = -1;
                for (Item itm : itemBag) {
                    if (itm != null) {
                        if (itm.id == ItemName.NGOC_RONG_1_SAO) {
                            indexDragonBall1Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_2_SAO) {
                            indexDragonBall2Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_3_SAO) {
                            indexDragonBall3Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_4_SAO) {
                            indexDragonBall4Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_5_SAO) {
                            indexDragonBall5Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_6_SAO) {
                            indexDragonBall6Star = itm.indexUI;
                        }
                        if (itm.id == ItemName.NGOC_RONG_7_SAO) {
                            indexDragonBall7Star = itm.indexUI;
                        }
                    }
                }
                if (indexDragonBall1Star != -1 && indexDragonBall2Star != -1 && indexDragonBall3Star != -1
                        && indexDragonBall4Star != -1 && indexDragonBall5Star != -1 && indexDragonBall6Star != -1
                        && indexDragonBall7Star != -1) {
                    menus.clear();
                    // menus.add(new KeyValue(20005, "Hướng\ndẫn thêm\n(mới)"));
                    menus.add(new KeyValue(20006, "Gọi\nRồng Thần\n1 Sao"));
                    service.openUIConfirm(NpcName.CON_MEO, "Bạn muốn gọi rồng thần ?", getPetAvatar(), menus);
                } else {
                    service.sendThongBao("Hãy tập hợp đủ 7 viên Ngọc Rồng");
                }
                break;
        }
    }

    private void typeItemMap(Item item, int action, byte type, byte where, byte index) {
        if (action == 0) {
            service.useItem(type, where, index, String.format("Bạn có muôn dùng\n%s ?", item.template.name));
        } else if (action == 1) {
            if (zone.getListSatellite().size() >= 3) {
                service.sendThongBao("Khu vực chỉ có thể đặt tối đa 3 vệ tinh");
                return;
            }
            removeItem(item.indexUI, 1);
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.x = this.x;
            itemMap.y = zone.map.collisionLand(itemMap.x, this.y);
            itemMap.playerID = -2;
            itemMap.owner = this;
            itemMap.r = 250;
            itemMap.item = item;
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        }
    }

    public void SachDacBiet() {
        try {
            boolean[] result = new boolean[ItemTimeName.SACH_DAC_BIET.length];
            synchronized (itemTimes) {
                for (ItemTime item : itemTimes) {
                    try {
                        for (int i = 0; i < result.length; i++) {
                            try {
                                if (item != null && item.id == ItemTimeName.SACH_DAC_BIET[i]) {
                                    result[i] = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                UtilsNQMP.logError(e);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        UtilsNQMP.logError(e);
                    }
                }
            }
            sachdacbiet = result;
        } catch (Exception e) {
            e.printStackTrace();
            UtilsNQMP.logError(e);
        }
    }

    public void setStatusItemTime() {
        synchronized (itemTimes) {
            for (ItemTime item : itemTimes) {
                switch (item.id) {
                    case ItemTimeName.CUONG_NO:
                        setCuongNo(true);
                        break;

                    case ItemTimeName.BO_HUYET:
                        setBoHuyet(true);
                        break;

                    case ItemTimeName.BO_KHI:
                        setBoKhi(true);
                        break;

                    case ItemTimeName.GIAP_XEN_BO_HUNG:
                        setGiapXen(true);
                        break;

                    case ItemTimeName.AN_DANH:
                        setAnDanh(true);
                        break;
                    case ItemTimeName.CUONG_NO_2:
                        setCuongNo2(true);
                        break;
                    case ItemTimeName.BO_HUYET_2:
                        setBoHuyet2(true);
                        break;
                    case ItemTimeName.BO_KHI_2:
                        setBoKhi2(true);
                        break;
                    case ItemTimeName.MAY_DO_CAPSULE_KI_BI:
                        setMayDo(true);
                        break;

                    case ItemTimeName.DUOI_KHI:
                        setDuoiKhi(true);
                        break;

                    case ItemTimeName.BANH_PUDDING:
                        setPudding(true);
                        break;

                    case ItemTimeName.XUC_XICH:
                        setXucXich(true);
                        break;

                    case ItemTimeName.KEM_DAU:
                        setKemDau(true);
                        break;

                    case ItemTimeName.MI_LY:
                        setMiLy(true);
                        break;

                    case ItemTimeName.SUSHI:
                        setSushi(true);
                        break;
                    case ItemTimeName.PHIEU_X2_TNSM:
                        setPhieuX2TNSM(true);
                        break;
                    case ItemTimeName.DU_DU:
                        setDudu(true);
                        break;
                    case ItemTimeName.XOAI_CHIN:
                        setXoai(true);
                        break;
                    case ItemTimeName.MANG_CAU:
                        setMangCau(true);
                        break;
                    case ItemTimeName.MAM_TRAI_CAY:
                        setMamTraiCay(true);
                        break;
                    case ItemTimeName.TRAI_SUNG:
                        setTraiSung(true);
                        break;
                    case ItemTimeName.DUA_XANH:
                        setDuaXanh(true);
                        break;
                    case ItemTimeName.UOC_THIEN_MENH_1:
                        setUocThienMenh1(true);
                        break;
                    case ItemTimeName.UOC_THIEN_MENH_2:
                        setUocThienMenh2(true);
                        break;
                    case ItemTimeName.UOC_THIEN_MENH_4:
                        setUocThienMenh4(true);
                        break;
                    case ItemTimeName.UOC_THIEN_MENH_5:
                        setUocThienMenh5(true);
                        break;
                    case ItemTimeName.UOC_LOC_PHAT_1:
                        setUocLocPhat1(true);
                        break;
                    case ItemTimeName.UOC_LOC_PHAT_2:
                        setUocLocPhat2(true);
                        break;
                    case ItemTimeName.UOC_LOC_PHAT_3:
                        setUocLocPhat3(true);
                        break;
                    case ItemTimeName.UOC_LOC_PHAT_7:
                        setUocLocPhat7(true);
                        break;
                    case ItemTimeName.CA_NOI_GIAN:
                        setCaNoiGian(true);
                        break;
                    case ItemTimeName.NUOC_TANG_CUONG:
                        setNuocTangCuong(true);
                        break;
                    case ItemTimeName.HAT_DAU_VANG:
                        setDauVang(true);
                        break;
                    case ItemTimeName.QUA_HONG_DAO:
                        setQuaHongDao(true);
                        break;
                }
            }
        }
        SachDacBiet();
    }

    public void typeClan(Item item) {
        switch (item.id) {
            case ItemName.LUOI_HAI_THAN_CHET:
            case ItemName.CANH_DOI_DRACULA:
            case ItemName.BONG_TUYET:
            case ItemName.LONG_DEN_CO_VY:
            case ItemName.LONG_DEN_CON_TAU:
            case ItemName.LONG_DEN_CON_GA:
            case ItemName.LONG_DEN_CON_BUOM:
            case ItemName.LONG_DEN_DOREMON:
            case ItemName.NON_THIEN_THAN:
            case ItemName.MA_TROI:
            case ItemName.HON_MA_GOKU:
            case ItemName.HON_MA_CA_DIC:
            case ItemName.HON_MA_POCOLO:
            case ItemName.CAY_THONG:
            case ItemName.TUI_QUA:
            case ItemName.CAY_TRUC:
            case ItemName.KIEM_Z:
            case ItemName.CO_CO_DONG_983:
            case ItemName.TRAI_BONG_966:
            case ItemName.CUP_VANG_982:
            case ItemName.VO_OC_994:
            case ItemName.CAY_KEM_995:
            case ItemName.CA_HEO_996:
            case ItemName.CON_DIEU_997:
            case ItemName.DIEU_RONG_998:
            case ItemName.MEO_MUN_999:
            case ItemName.XIEN_CA_1000:
            case ItemName.PHONG_LON_1001:
            case ItemName.VAN_LUOT_SONG_1007:
                updateBag();
                break;
        }
    }

    public void updateBag() {
        if (itemLoot != null) {
            switch (itemLoot.id) {
                case ItemName.NGOC_RONG_1_SAO_DEN:
                    this.bag = ClanImageName.NGOC_RONG_1_SAO_DEN_61;
                    break;
                case ItemName.NGOC_RONG_2_SAO_DEN:
                    this.bag = ClanImageName.NGOC_RONG_2_SAO_DEN_62;
                    break;
                case ItemName.NGOC_RONG_3_SAO_DEN:
                    this.bag = ClanImageName.NGOC_RONG_3_SAO_DEN_63;
                    break;
                case ItemName.NGOC_RONG_4_SAO_DEN:
                    this.bag = ClanImageName.NGOC_RONG_4_SAO_DEN_64;
                    break;
                case ItemName.NGOC_RONG_5_SAO_DEN:
                    this.bag = ClanImageName.NGOC_RONG_5_SAO_DEN_65;
                    break;
                case ItemName.NGOC_RONG_6_SAO_DEN:
                    this.bag = ClanImageName.NGOC_RONG_6_SAO_DEN_66;
                    break;
                case ItemName.NGOC_RONG_7_SAO_DEN:
                    this.bag = ClanImageName.NGOC_RONG_7_SAO_DEN_80;
                    ;
                    break;
                case ItemName.NGOC_RONG_NAMEK_1_SAO:
                    this.bag = ClanImageName.NGOC_RONG_NAMEK_1_SAO_52;
                    break;
                case ItemName.NGOC_RONG_NAMEK_2_SAO:
                    this.bag = ClanImageName.NGOC_RONG_NAMEK_2_SAO_53;
                    break;
                case ItemName.NGOC_RONG_NAMEK_3_SAO:
                    this.bag = ClanImageName.NGOC_RONG_NAMEK_3_SAO_54;
                    break;
                case ItemName.NGOC_RONG_NAMEK_4_SAO:
                    this.bag = ClanImageName.NGOC_RONG_NAMEK_4_SAO_55;
                    break;
                case ItemName.NGOC_RONG_NAMEK_5_SAO:
                    this.bag = ClanImageName.NGOC_RONG_NAMEK_5_SAO_56;
                    break;
                case ItemName.NGOC_RONG_NAMEK_6_SAO:
                    this.bag = ClanImageName.NGOC_RONG_NAMEK_6_SAO_57;
                    break;
                case ItemName.NGOC_RONG_NAMEK_7_SAO:
                    this.bag = ClanImageName.NGOC_RONG_NAMEK_7_SAO_58;
                    break;
            }
        } else if (itemBody != null && itemBody.length > 8 && itemBody[8] != null) {
            if (itemBody[8].template.part != -1) {
                this.bag = (byte) itemBody[8].template.part;
            } else {
                switch (itemBody[8].template.id) {
                    case ItemName.LUOI_HAI_THAN_CHET:
                        this.bag = ClanImageName.LUOI_HAI_THAN_CHET_72;
                        break;
                    case ItemName.CANH_DOI_DRACULA:
                        this.bag = ClanImageName.CANH_DOI_DRACULA_73;
                        break;
                    case ItemName.BONG_TUYET:
                        this.bag = ClanImageName.BONG_TUYET_74;
                        break;
                    case ItemName.LONG_DEN_CO_VY:
                        this.bag = ClanImageName.LONG_DEN_CO_VY_37;
                        break;
                    case ItemName.LONG_DEN_CON_TAU:
                        this.bag = ClanImageName.LONG_DEN_CON_TAU_38;
                        break;
                    case ItemName.LONG_DEN_CON_GA:
                        this.bag = ClanImageName.LONG_DEN_CON_GA_39;
                        break;
                    case ItemName.LONG_DEN_CON_BUOM:
                        this.bag = ClanImageName.LONG_DEN_CON_BUOM_40;
                        break;
                    case ItemName.LONG_DEN_DOREMON:
                        this.bag = ClanImageName.LONG_DEN_DOREMON_41;
                        break;
                    case ItemName.NON_THIEN_THAN:
                        this.bag = ClanImageName.NON_THIEN_THAN_42;
                        break;
                    case ItemName.MA_TROI:
                        this.bag = ClanImageName.MA_TROI_43;
                        break;
                    case ItemName.HON_MA_GOKU:
                        this.bag = ClanImageName.HON_MA_GOKU_44;
                        break;
                    case ItemName.HON_MA_CA_DIC:
                        this.bag = ClanImageName.HON_MA_CA_DIC_45;
                        break;
                    case ItemName.HON_MA_POCOLO:
                        this.bag = ClanImageName.HON_MA_POCOLO_46;
                        break;
                    case ItemName.CAY_THONG:
                        this.bag = ClanImageName.CAY_THONG_47;
                        break;
                    case ItemName.TUI_QUA:
                        this.bag = ClanImageName.TUI_QUA_48;
                        break;
                    case ItemName.CAY_TRUC:
                        this.bag = ClanImageName.CAY_TRUC_49;
                        break;
                    case ItemName.KIEM_Z:
                        this.bag = ClanImageName.KIEM_Z_50;
                        break;
                    case ItemName.TRAI_BONG_966:
                        this.bag = ClanImageName.TRAI_BONG_77;
                        break;
                    case ItemName.CUP_VANG_982:
                        this.bag = ClanImageName.CUP_VANG_78;
                        break;
                    case ItemName.CO_CO_DONG_983:
                        this.bag = ClanImageName.CO_CO_DONG_79;
                        break;
                    case ItemName.VO_OC_994:
                        this.bag = ClanImageName.VO_OC_81;
                        break;
                    case ItemName.CAY_KEM_995:
                        this.bag = ClanImageName.CAY_KEM_82;
                        break;
                    case ItemName.CA_HEO_996:
                        this.bag = ClanImageName.CA_HEO_83;
                        break;
                    case ItemName.CON_DIEU_997:
                        this.bag = ClanImageName.CON_DIEU_84;
                        break;
                    case ItemName.DIEU_RONG_998:
                        this.bag = ClanImageName.DIEU_RONG_85;
                        break;
                    case ItemName.MEO_MUN_999:
                        this.bag = ClanImageName.MEO_MUN_86;
                        break;
                    case ItemName.XIEN_CA_1000:
                        this.bag = ClanImageName.XIEN_CA_87;
                        break;
                    case ItemName.PHONG_LON_1001:
                        this.bag = ClanImageName.PHONG_LON_88;
                        break;
                    case ItemName.VAN_LUOT_SONG_1007:
                        this.bag = ClanImageName.VAN_LUOT_SONG_89;
                        break;
                    case ItemName.CANH_THIEN_THAN:
                        this.bag = ClanImageName.CANH_THIEN_THAN_97;
                        break;
                    case ItemName.HAO_QUANG_RUC_RO:
                        this.bag = ClanImageName.HAO_QUANG_RUC_RO_102;
                        break;
                    case ItemName.BONG_BONG_THIEN_THAN:
                        this.bag = ClanImageName.BONG_BONG_THIEN_THAN_103;
                        break;
                    case ItemName.KIEM_THAN_ANH_SANG:
                        this.bag = ClanImageName.KIEM_THAN_ANH_SANG_104;
                        break;
                    case ItemName.MEO_SAO_HOA:
                        this.bag = ClanImageName.MEO_SAO_HOA_104;
                        break;
                    case ItemName.GOKU_TI_HON:
                        this.bag = ClanImageName.GOKU_TI_HON_105;
                        break;
                    case ItemName.THO_MONG_MO:
                        this.bag = ClanImageName.THO_MONG_MO_106;
                        break;
                    case 2228:
                        this.bag = 107;
                        break;
                }
            }

        } else {
            if (clan != null) {
                this.bag = clan.imgID;
            } else {
                this.bag = -1;
            }
        }
        info.setInfo();
        service.loadPoint();
        if (zone != null) {
            zone.service.playerLoadBody(this);
            zone.service.updateBag(this);
        }
    }

    public void typeItemTime(Item item) {
        if (item.id == ItemName.DUOI_KHI && gender == 2) {
            return;
        }
        if (isHaveFood() && item.template.isFood()) {
            service.sendThongBao("Thức ăn vẫn còn tác dụng");
            return;
        }
        ItemTime itemTime = null;
        boolean isUpdate = false;
        switch (item.id) {
            case ItemName.CUONG_NO:
                if (this.isCuongNo2) {
                    service.serverMessage("Bạn đang sử dụng vật phẩm cùng loại");
                    return;
                }
                itemTime = new ItemTime(ItemTimeName.CUONG_NO, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.BO_HUYET:
                if (this.isBoHuyet2) {
                    service.serverMessage("Bạn đang sử dụng vật phẩm cùng loại");
                    return;
                }
                itemTime = new ItemTime(ItemTimeName.BO_HUYET, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.BO_KHI:
                if (this.isBoKhi2) {
                    service.serverMessage("Bạn đang sử dụng vật phẩm cùng loại");
                    return;
                }
                itemTime = new ItemTime(ItemTimeName.BO_KHI, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.GIAP_XEN_BO_HUNG:
                itemTime = new ItemTime(ItemTimeName.GIAP_XEN_BO_HUNG, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.AN_DANH:
                itemTime = new ItemTime(ItemTimeName.AN_DANH, item.template.iconID, 10 * 60, true);
                break;
            case ItemName.CUONG_NO_2:
                if (this.isCuongNo()) {
                    service.serverMessage("Bạn đang sử dụng vật phẩm cùng loại");
                    return;
                }
                itemTime = new ItemTime(ItemTimeName.CUONG_NO_2, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.BO_HUYET_2:
                if (this.isBoHuyet()) {
                    service.serverMessage("Bạn đang sử dụng vật phẩm cùng loại");
                    return;
                }
                itemTime = new ItemTime(ItemTimeName.BO_HUYET_2, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.BO_KHI_2:
                if (this.isBoKhi()) {
                    service.serverMessage("Bạn đang sử dụng vật phẩm cùng loại");
                    return;
                }
                itemTime = new ItemTime(ItemTimeName.BO_KHI_2, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.MAY_DO_CAPSULE_KI_BI:
                itemTime = new ItemTime(ItemTimeName.MAY_DO_CAPSULE_KI_BI, item.template.iconID, 30 * 60, true);
                break;

            case ItemName.DUOI_KHI:
                itemTime = new ItemTime(ItemTimeName.DUOI_KHI, item.template.iconID,
                        (gender == 0 ? (30 * 60) : (2 * 60 * 60)), true);
                break;

            case ItemName.BANH_PUDDING:
                itemTime = new ItemTime(ItemTimeName.BANH_PUDDING, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.XUC_XICH:
                itemTime = new ItemTime(ItemTimeName.XUC_XICH, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.KEM_DAU:
                itemTime = new ItemTime(ItemTimeName.KEM_DAU, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.MI_LY:
                itemTime = new ItemTime(ItemTimeName.MI_LY, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;

            case ItemName.SUSHI:
                itemTime = new ItemTime(ItemTimeName.SUSHI, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.PHIEU_X2_TNSM:
                itemTime = new ItemTime(ItemTimeName.PHIEU_X2_TNSM, item.template.iconID, 20 * 60, true);
                isUpdate = true;
                break;
            case ItemName.DU_DU:
                itemTime = new ItemTime(ItemTimeName.DU_DU, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.XOAI_CHIN:
                itemTime = new ItemTime(ItemTimeName.XOAI_CHIN, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.MANG_CAU:
                itemTime = new ItemTime(ItemTimeName.MANG_CAU, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.MAM_TRAI_CAY:
                itemTime = new ItemTime(ItemTimeName.MAM_TRAI_CAY, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.DUA_XANH:
                itemTime = new ItemTime(ItemTimeName.DUA_XANH, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.TRAI_SUNG:
                itemTime = new ItemTime(ItemTimeName.TRAI_SUNG, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.HAT_DAU_VANG:
                itemTime = new ItemTime(ItemTimeName.HAT_DAU_VANG, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.NUOC_TANG_CUONG:
                itemTime = new ItemTime(ItemTimeName.NUOC_TANG_CUONG, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.CA_NOI_GIAN:
                itemTime = new ItemTime(ItemTimeName.CA_NOI_GIAN, item.template.iconID, 10 * 60, true);
                isUpdate = true;
                break;
            case ItemName.QUA_HONG_DAO:
                itemTime = new ItemTime(ItemTimeName.QUA_HONG_DAO, item.template.iconID, 30 * 60, true);
                isUpdate = true;
                break;
            case ItemName.SACH_DAC_BIET:
            case ItemName.SACH_DAC_BIET + 1:
            case ItemName.SACH_DAC_BIET + 2:
            case ItemName.SACH_DAC_BIET + 3:
            case ItemName.SACH_DAC_BIET + 4:
            case ItemName.SACH_DAC_BIET + 5:
            case ItemName.SACH_DAC_BIET + 6:
            case ItemName.SACH_DAC_BIET + 7: {
                int index = item.id - ItemName.SACH_DAC_BIET;
                itemTime = new ItemTime(ItemTimeName.SACH_DAC_BIET[index], item.template.iconID, 10 * 60, true);
                isUpdate = true;
            }
            break;
        }
        if (itemTime != null) {
            removeItem(item.indexUI, 1);
            addItemTime(itemTime);
            setStatusItemTime();
            if (isUpdate) {
                if (item.template.isFood() && myDisciple != null) {
                    myDisciple.info.setInfo();
                }
                info.setInfo();
                // if (item.id == ItemName.BO_HUYET) {
                // info.recovery(Info.HP, 50, true);
                // }
                // if (item.id == ItemName.BO_KHI) {
                // info.recovery(Info.MP, 50, true);
                // }
                service.loadPoint();
                zone.service.playerLoadBody(this);
            }
        }

    }

    public void useAutoPlay(Item item) {
        ItemTime itemTime2 = getItemTime(12);
        if (itemTime2 == null) {
            int minutes = item.options.get(0).param;
            int seconds = minutes * 60;
            item.options.get(0).param = 0;
            isAutoPlay = true;
            service.refreshItem((byte) 1, item);
            ItemTime itemTime1 = new ItemTime(ItemTimeName.TU_DONG_LUYEN_TAP, item.template.iconID, seconds, true);
            addItemTime(itemTime1);
            service.sendThongBao("Tự động đánh quái đã bật");
            service.autoPlay(true);
        } else {
            int seconds = itemTime2.seconds;
            int minutes = seconds / 60;
            item.options.get(0).param += minutes;
            service.refreshItem((byte) 1, item);
            setTimeForItemtime(12, 0);
        }
    }

    public void setMiniDisciple(Item item) {
        if (this.zone == null) {
            return;
        }
        if (miniDisciple == null) {
            miniDisciple = new MiniDisciple(item, this);
            zone.enter(miniDisciple);
            miniDisciple.move();
        } else {
            zone.leave(miniDisciple);
            Item itm = miniDisciple.item;
            miniDisciple = null;
            if (itm != item) {
                setMiniDisciple(item);
                return;
            }
        }
        info.setInfo();
        service.loadPoint();
    }

    public void openMabuEgg() {
        if (myDisciple != null) {
            deleteDisciple();
        }
        createDisciple(1, 3);
        this.service.petInfo((byte) 2);
    }

    public void openUubEgg() {
        if (myDisciple != null) {
            deleteDisciple();
        }
        createDisciple(2, 3);
        this.service.petInfo((byte) 2);
    }

    public void createDisciple(int type, int gender) {
        try {
            if (myDisciple == null) {
                lastAttack = System.currentTimeMillis();
                Disciple disciple = new Disciple();
                disciple.typeDisciple = (byte) type;
                disciple.id = -this.id;
                disciple.name = "Đệ tử";
                if (type != 2) {
                    disciple.petBonus = 10;
                } else {
                    disciple.petBonus = 20;
                }
                disciple.itemBody = new Item[13];
                if (gender >= 0) {
                    disciple.gender = disciple.classId = (byte) gender;
                } else {
                    disciple.gender = disciple.classId = (byte) Utils.nextInt(3);
                }
                if (type == 0) {
                    disciple.gender = disciple.classId = this.gender;
                }
                if (disciple.gender == 3) {
                    disciple.classId = this.gender;
                }
                disciple.info = new Info(disciple);
                disciple.info.setChar(disciple);
                disciple.info.setPowerLimited();
                disciple.info.applyCharLevelPercent();
                disciple.info.setStamina();
                if (type == 1) {
                    disciple.info.power = 1500000L;
                } else if (type == 2) {
                    disciple.info.power = 15000000L;
                } else {
                    disciple.info.power = 2000L;
                }
                disciple.skills = new ArrayList<>();
                disciple.skillOpened = 0;
                disciple.learnSkill();
                disciple.discipleStatus = 0;
                disciple.info.setInfo();
                disciple.info.recovery(Info.ALL, 100, false);
                disciple.service = new Service(disciple);
                disciple.setDefaultPart();
                myDisciple = disciple;
                disciple.saveData();
                disciple.setMaster(this);
                disciple.followMaster();
                service.petInfo((byte) 1);
                zone.enter(myDisciple);
                service.chat(myDisciple, "Sư phụ hãy nhận con làm đệ tử");
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 74");
            logger.error("ko tao duoc de tu!", ex);
            this.session.disconnect();
        }
    }

    public void typeOrder(Item item) {
        if (item.template.id >= ItemName.MANH_AO_THIEN_SU_TD && item.template.id <= ItemName.MANH_NHAN_THIEN_SU_XD) {
            useMTS(item);
            return;
        }
        if (EventTet2025.useItem(this, item)) {
            removeItem(item.indexUI, 1);

            return;
        }
        switch (item.id) {
            case 2300:
                if (getSlotNullInBag() < 7) {
                    service.serverMessage("Bạn cần ít nhất 6 ô trống trong hành trang ");
                    return;
                }
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_HD_TD, "Set Hủy Diệt Trái Đất"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_HD_NM, "Set Hủy Diệt Namec"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_HD_XD, "Set Hủy Diệt Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set hủy diệt bạn mong muốn ?", getPetAvatar(), menus);

                break;
            case 2301:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_1_HD_TD, "1 món Hủy Diệt Trái Đất"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_HD_NM, "1 món Hủy Diệt Namec"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_HD_XD, "1 món Hủy Diệt Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set hủy diệt bạn mong muốn ?", getPetAvatar(), menus);

                break;
            case 2302:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_1_CUOI_TD, "1 món Trái Đất"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_CUOI_NM, "1 món Namec"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_CUOI_XD, "1 món Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn hành tinh bạn mong muốn ?", getPetAvatar(), menus);

                break;
            case 2303:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_1_SKH_TD, "1 món Trái Đất"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_SKH_NM, "1 món Namec"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_SKH_XD, "1 món Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn hành tinh bạn mong muốn ?", getPetAvatar(), menus);

                break;
            case 2304:
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_1_TL_TD, "1 món Trái Đất"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_TL_NM, "1 món Namec"));
                menus.add(new KeyValue(CMDMenu.OPEN_1_TL_XD, "1 món Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn hành tinh bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case 2305:
                break;
            case 2297:
            case 2298:
            case 2299:
                int index_change = item.id - 2295;
                if (this.myDisciple == null) {
                    this.service.sendThongBao("Bạn chưa có đệ tử");
                    return;
                }
                if (this.myDisciple.skillOpened < index_change) {
                    this.service.sendThongBao("Đệ tử của bạn chưa mở khóa kĩ năng này");
                    return;
                }
                removeItem(item.indexUI, 1);
                this.myDisciple.changeSkillIndex(index_change);
                break;
            case 2296:
                if (clan != null) {
                    if (clan.maxMember < 20) {
                        ClanMember clanMember2 = clan.getMember(this.id);
                        if (clanMember2 != null && clanMember2.isLeader()) {
                            removeItem(item.indexUI, 1);
                            clan.maxMember++;
                            clan.clanInfo();
                            service.sendThongBao("Bang hội của bạn đã tăng thêm 1 thành viên");

                            return;
                        }
                        service.sendThongBao("Bạn không phải là bang chủ");
                        return;
                    }
                    service.sendThongBao("Bang hội của bạn đã đạt tối đa thành viên");
                    return;
                }
                service.sendThongBao("Bạn không có bang hội");
                break;
            case 2290:
            case 2291:
            case 2292: {
                if (isBagFull()) {
                    service.sendThongBao(Language.ME_BAG_FULL);
                    return;
                }
                if (item.quantity < 10) {
                    service.sendThongBao("Cần tối thiểu x10 để đổi Huyết ma thiên tử");
                    return;
                }
                removeItem(item.indexUI, 10);
                Item itm = new Item(item.id + 3);
                itm.quantity = 1;
                itm.setDefaultOptions();
                addItem(itm);
                service.sendThongBao("Bạn nhận được " + itm.template.name);
            }
            break;
            case 2176:
                this.SKIPNV(item);
                break;
            case 2175:
                this.HopQuaTanThu(item);
                break;
            case ItemName.THOI_VANG:
                inputDlg = new InputDialog(CMDTextBox.SELL_GOLD_BAR, "Nhập số lượng thỏi vàng muốn dùng",
                        new TextField("Nhập Số lượng , 1 thỏi vàng sẽ được 500tr vàng"));
                inputDlg.setService(service);
                inputDlg.show();
                break;
            case ItemName.DA_ANH_SANG: {
                if (isBagFull()) {
                    service.sendThongBao(Language.ME_BAG_FULL);
                    return;
                }
                if (item.quantity < 99) {
                    service.sendThongBao("Cần tối thiểu x99 để đổi Nhẫn ánh sáng");
                    return;
                }
                removeItem(item.indexUI, 99);
                Item itm = new Item(ItemName.NHAN_ANH_SANG_CAP_1);
                itm.quantity = 1;
                List<ItemOption> optionList = new ArrayList<>();
                for (int[] options : NangNhanAnhSang.OPTION) {
                    optionList.add(new ItemOption(options[0], options[1]));
                }
                Collections.shuffle(optionList);
                itm.addItemOption(optionList.get(0));
                itm.addItemOption(optionList.get(1));
                if (addItem(itm)) {
                    service.sendThongBao(String.format("Bạn nhận được %s", itm.template.name));
                }
                break;
            }

            case ItemName.GOI_10_RADA_DO_NGOC:
                if (this.zone.map.isMappNgucTu()) {
                    service.serverMessage("Không thể thực hiện");
                    return;
                }
                if (idNRNM != -1) {
                    service.serverMessage("Không thể thực hiện");
                    return;
                }
                idGo = (short) Utils.nextInt(0, 6);
                removeItem(item.indexUI, 1);
                String sb = "";
                sb += "\n1 Sao (" + getDis(this, 0, (short) 353) + ")";
                sb += "\n2 Sao (" + getDis(this, 1, (short) 354) + ")";
                sb += "\n3 Sao (" + getDis(this, 2, (short) 355) + ")";
                sb += "\n4 Sao (" + getDis(this, 3, (short) 356) + ")";
                sb += "\n5 Sao (" + getDis(this, 4, (short) 357) + ")";
                sb += "\n6 Sao (" + getDis(this, 5, (short) 358) + ")";
                sb += "\n7 Sao (" + getDis(this, 6, (short) 359) + ")";
                menus.clear();
                menus.add(new KeyValue(CMDMenu.TELE_NR_NAMEC_1_SAO, "Đến ngay\n Viên " + (idGo + 1) + " Sao"));
                service.openUIConfirm(NpcName.CON_MEO, sb, getPetAvatar(), menus);
                break;
            case 2197: {
                if (this.myDisciple != null) {
                    if (!this.myDisciple.isEmptyBody()) {
                        this.service.serverMessage("Hãy tháo hết trang bị của đệ tử xuống để sử dụng vật phẩm này");
                        return;
                    }
                }
                if (this.myDisciple == null) {
                    this.service.serverMessage("Bạn cần phải có đệ tử trước tiên");
                    return;
                }
                if (this.myDisciple.typeDisciple != 1 && this.myDisciple.typeDisciple != 2) {
                    this.service.dialogMessage("Bạn cần phải có đệ tử Mabu trước tiên");
                    return;
                }
                int indexTrung = getIndexBagById(2197);
                if (indexTrung != -1) {
                    removeItem(indexTrung, 1);
                } else {
                    service.serverMessage("Bạn không có trứng trong hành trang");
                    return;
                }
                this.openUubEgg();
                break;
            }
            case ItemName.QUA_TRUNG:
                if (this.myDisciple != null) {
                    if (!this.myDisciple.isEmptyBody()) {
                        this.service.serverMessage("Hãy tháo hết trang bị của đệ tử xuống để sử dụng vật phẩm này");
                        return;
                    }
                }
                if (this.myDisciple == null) {
                    this.service.serverMessage("Bạn cần phải có đệ tử trước tiên");
                    return;
                }
                int indexTrung = getIndexBagById(ItemName.QUA_TRUNG);
                if (indexTrung != -1) {
                    removeItem(indexTrung, 1);
                } else {
                    service.serverMessage("Bạn không có trứng trong hành trang");
                    return;
                }
                this.openMabuEgg();
                break;
            case ItemName.TU_DONG_LUYEN_TAP:
                useAutoPlay(item);
                break;
            case ItemName.NHAN_THOI_KHONG_SAI_LECH_992: {
                zone.leave(this);
                TMap map = MapManager.getInstance().getMap(160);
                int zoneId = map.getZoneID();
                map.enterZone(this, zoneId);
                this.zone.service.setPosition(this, (byte) 0, (short) 60, (short) 168);

                if (taskMain != null && taskMain.id == 29 && taskMain.index == 2) {
                    updateTaskCount(1);
                }
            }
            break;
            case ItemName.NHO_TIM:
                info.stamina = info.maxStamina;
                service.setStamina();
                removeItem(item.indexUI, 1);
                break;

            case ItemName.NHO_XANH:
                info.stamina += (short) (info.maxStamina / 5);
                service.setStamina();
                removeItem(item.indexUI, 1);
                break;

            case ItemName.NANG_CAP_KY_NANG_1_DE_TU:
                if (myDisciple != null && myDisciple.skillOpened >= 1) {
                    try {
                        Skill skill = myDisciple.skills.get(0);
                        if (skill.point == 7) {
                            service.sendThongBao("Kỹ năng cấp 1 đã đạt cấp tối đa");
                            return;
                        }
                        Skill skillNew = Skills.getSkill(skill.template.id, (byte) (skill.point + 1)).clone();
                        skillNew.lastTimeUseThisSkill = skill.lastTimeUseThisSkill;
                        skillNew.coolDown = skill.coolDown;
                        myDisciple.skills.set(0, skillNew);
                        removeItem(item.indexUI, 1);
                        service.chat(myDisciple, "Cảm ơn sư phụ");
                    } catch (CloneNotSupportedException ex) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                        System.err.println("Error at 73");
                        logger.error("failed!", ex);
                    }
                }
                break;

            case ItemName.NANG_CAP_KY_NANG_2_DE_TU:
                if (myDisciple != null && myDisciple.skillOpened >= 2) {
                    try {
                        Skill skill = myDisciple.skills.get(1);
                        if (skill.point == 7) {
                            service.sendThongBao("Kỹ năng cấp 2 đã đạt cấp tối đa");
                            return;
                        }
                        Skill skillNew = Skills.getSkill(skill.template.id, (byte) (skill.point + 1)).clone();
                        skillNew.lastTimeUseThisSkill = skill.lastTimeUseThisSkill;
                        skillNew.coolDown = skill.coolDown;
                        myDisciple.skills.set(1, skillNew);
                        removeItem(item.indexUI, 1);
                        service.chat(myDisciple, "Cảm ơn sư phụ");
                    } catch (CloneNotSupportedException ex) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                        System.err.println("Error at 72");
                        logger.error("failed!", ex);
                    }
                }
                break;

            case ItemName.NANG_CAP_KY_NANG_3_DE_TU:
                if (myDisciple != null && myDisciple.skillOpened >= 3) {
                    try {
                        Skill skill = myDisciple.skills.get(2);
                        if (skill.point == 7) {
                            service.sendThongBao("Kỹ năng cấp 3 đã đạt cấp tối đa");
                            return;
                        }
                        Skill skillNew = Skills.getSkill(skill.template.id, (byte) (skill.point + 1)).clone();
                        skillNew.lastTimeUseThisSkill = skill.lastTimeUseThisSkill;
                        skillNew.coolDown = skill.coolDown;
                        myDisciple.skills.set(2, skillNew);
                        removeItem(item.indexUI, 1);
                        service.chat(myDisciple, "Cảm ơn sư phụ");
                    } catch (CloneNotSupportedException ex) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                        System.err.println("Error at 71");
                        logger.error("failed!", ex);
                    }
                }
                break;

            case ItemName.NANG_CAP_KY_NANG_4_DE_TU:
                if (myDisciple != null && myDisciple.skillOpened >= 4) {
                    try {
                        Skill skill = myDisciple.skills.get(3);
                        if (skill.point == 7) {
                            service.sendThongBao("Kỹ năng cấp 4 đã đạt cấp tối đa");
                            return;
                        }
                        Skill skillNew = Skills.getSkill(skill.template.id, (byte) (skill.point + 1)).clone();
                        skillNew.lastTimeUseThisSkill = skill.lastTimeUseThisSkill;
                        skillNew.coolDown = skill.coolDown;
                        myDisciple.skills.set(3, skillNew);
                        removeItem(item.indexUI, 1);
                        service.chat(myDisciple, "Cảm ơn sư phụ");
                    } catch (CloneNotSupportedException ex) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                        System.err.println("Error at 70");
                        logger.error("failed!", ex);
                    }
                }
                break;

            case ItemName.DOI_DE_TU:
                if (myDisciple == null || isNhapThe || myDisciple.isDead()) {
                    service.sendThongBao("Không thể thực hiện");
                    return;
                }
                changeDisciple();
                removeItem(item.indexUI, 1);

                break;

            case ItemName.BONG_TAI_PORATA:
                if (myDisciple != null) {
                    if (zone.map.isDauTruong() || myDisciple.isDead()) {
                        service.sendThongBao("Không thể thực hiện");
                        return;
                    }
                    long now = System.currentTimeMillis();
                    long time = now - lastTimeUsePorata;
                    if (time < 10000) {
                        int seconds = (int) ((10000 - time) / 1000);
                        service.sendThongBao(String.format("Vui lòng đợi %s nữa", Utils.timeAgo(seconds)));
                        return;
                    }
                    if (!this.isNhapThe) {
                        typePorata = 0;
                        fusion((byte) 6);
                    } else {
                        if (this.fusionType == 6 && typePorata == 0) {
                            fusion((byte) 1);
                        } else {
                            service.sendThongBao("Không thể thực hiện");
                        }
                    }
                } else {
                    service.sendThongBao("Bạn cần phải có đệ tử");
                }
                break;

            case ItemName.BONG_TAI_PORATA_CAP_2:
                if (myDisciple != null) {
                    if (zone.map.isDauTruong() || myDisciple.isDead()) {
                        service.sendThongBao("Không thể thực hiện");
                        return;
                    }
                    long now = System.currentTimeMillis();
                    long time = now - lastTimeUsePorata;
                    if (time < 3000) {
                        int seconds = (int) ((3000 - time) / 1000);
                        service.sendThongBao(String.format("Vui lòng đợi %s nữa", Utils.timeAgo(seconds)));
                        return;
                    }
                    if (!this.isNhapThe) {
                        typePorata = 1;
                        fusion((byte) 6);
                    } else {
                        if (this.fusionType == 6 && typePorata == 1) {
                            fusion((byte) 1);
                        } else {
                            service.sendThongBao("Không thể thực hiện");
                        }
                    }
                } else {
                    service.sendThongBao("Bạn cần phải có đệ tử");
                }
                break;

            case ItemName.GOI_10_VIEN_CAPSULE:
            case ItemName.VIEN_CAPSULE_DAC_BIET:
                // if (escortedPerson != null) {
                // service.sendThongBao(String.format("Bạn đang hộ tống %s, không thể thực
                // hiện.", escortedPerson.name));
                // return;
                // }
                if ((itemLoot != null && itemLoot.isDragonBallNamec())) {
                    service.serverMessage("Không thể sử dụng capsule khi đang mang Ngọc Rồng Namek!");
                    return;
                }
                if (zone.map.isBlackDragonBall()) {
                    if (itemLoot != null) {
                        service.sendThongBao("Không thể thực hiện");
                        return;
                    }
                }
                if (isKhongChe()) {
                    service.sendThongBao("Không thể thực hiện");
                    return;
                }
                setListMap();
                capsule = item.indexUI;
                service.mapTransport(listMapTransport);
                break;
            case ItemName.VIEN_CAPSULE_KI_BI: {
                if (getSlotNullInBag() == 0) {
                    service.sendThongBao(Language.ME_BAG_FULL);
                    return;
                }
                int[] rewardCskb = {ItemName.CUONG_NO, ItemName.BO_HUYET, ItemName.BO_KHI, ItemName.AN_DANH};
                Item itm = null;
                int rd = Utils.nextInt(100);
                if (rd >= 70) {
                    itm = new Item(rewardCskb[Utils.nextInt(rewardCskb.length)]);
                    itm.quantity = 1;
                } else {
                    itm = new Item(ItemName.VANG);
                    itm.quantity = Utils.nextInt(1, 1000) * 1000;
                }
                itm.setDefaultOptions();
                addItem(itm);
                removeItem(item.indexUI, 1);
                service.combine((byte) 6, null, item.template.iconID, itm.template.iconID);
            }
            break;

            case ItemName.CAPSULE_SQUID_GAME: {
                if (getSlotNullInBag() == 0) {
                    service.sendThongBao(Language.ME_BAG_FULL);
                    return;
                }
                RandomCollection<Integer> rd = RandomItem.SQUIDGAME;
                int itemID = rd.next();
                Item itm = new Item(itemID);
                itm.setDefaultOptions();
                int[] arr = {-1, 7, 7, 7, 7, 15, 15, 15, 30, 30};
                int expire = arr[Utils.nextInt(arr.length)];
                if (expire != -1) {
                    itm.addItemOption(new ItemOption(93, expire));
                }
                itm.quantity = 1;
                addItem(itm);
                removeItem(item.indexUI, 1);
                service.combine((byte) 6, null, item.template.iconID, itm.template.iconID);
            }
            break;
            // case ItemName.BUA_DOI_SKILL_DE_TU:
            // this.myDisciple.changeSkill();
            // removeItem(item.indexUI, 1);
            // break;
            case ItemName.CAPSULE_THOI_TRANG:
                if (isBagFull()) {
                    service.sendThongBao(Language.ME_BAG_FULL);
                    return;
                }
                Item ct = new Item(ItemName.CAI_TRANG_GOHAN_BU);
                ct.addItemOption(new ItemOption(50, new Random().nextInt(41) + 10));
                ct.addItemOption(new ItemOption(77, new Random().nextInt(41) + 10));
                ct.addItemOption(new ItemOption(103, new Random().nextInt(41) + 10));
                ct.addItemOption(new ItemOption(14, new Random().nextInt(20) + 1));
                if (Utils.nextInt(3) == 0) {
                    ct.addItemOption(new ItemOption(5, new Random().nextInt(21) + 10));
                } else if (Utils.nextInt(3) == 0) {
                    ct.addItemOption(new ItemOption(196, new Random().nextInt(31) + 10));
                } else {
                    ct.addItemOption(new ItemOption(197, new Random().nextInt(31) + 10));
                }
                if (Utils.nextInt(10) != 0) {
                    ct.addItemOption(new ItemOption(93, new Random().nextInt(5) + 3));
                }
                this.addItem(ct);
                removeItem(item.indexUI, 1);
                break;
            case ItemName.RUONG_CAI_TRANG_THO_DAU_BAC:
                if (isBagFull()) {
                    service.sendThongBao(Language.ME_BAG_FULL);
                    return;
                }
                Item ctTDB = new Item(ItemName.CAI_TRANG_THO_DAU_BAC);
                ctTDB.addItemOption(new ItemOption(101, new Random().nextInt(191) + 10));
                if (Utils.nextInt(10) != 0) {
                    ctTDB.addItemOption(new ItemOption(93, 1));
                }
                this.addItem(ctTDB);
                removeItem(item.indexUI, 1);
                break;
            case ItemName.LINH_BAO_VE_TAM_GIAC:
            case ItemName.LINH_BAO_VE_TRON:
            case ItemName.LINH_BAO_VE_VUONG:
            case ItemName.BUP_BE:
            case ItemName.HO_MAP_TRANG_943:
            case ItemName.HO_MAP_VANG_942:
            case ItemName.HO_MAP_XANH_944:
            case ItemName.SAO_LA_967:
            case ItemName.MA_PHONG_BA:
            case ItemName.THAN_CHET_CUTE:
            case ItemName.BI_NGO_NHI_NHANH:
            case ItemName.CUA_DO_1008:
            // case ItemName.PET_KE_XAM_LANG:
            case ItemName.RONG_HONG:
            case ItemName.RONG_VANG:
                setMiniDisciple(item);
                break;
            case ItemName.HOP_QUA_THAN_LINH:
                if (isBagFull()) {
                    service.sendThongBao(Language.ME_BAG_FULL);
                    return;
                }
                menus.clear();
                menus.add(new KeyValue(CMDMenu.MON_TLKH_NM, "Namec"));
                menus.add(new KeyValue(CMDMenu.MON_TLKH_TĐ, "Trái Đất"));
                menus.add(new KeyValue(CMDMenu.MON_TLKH_XD, "Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO,
                        "Bạn sẽ nhận được ngẫu nhiên 1 món Thần Linh Kích Hoạt\nHãy chọn hành tinh bạn muốn ?",
                        getPetAvatar(), menus);
                break;
            case ItemName.BANH_CHUNG:
                // if (isBagFull()) {
                // service.sendThongBao(Language.ME_BAG_FULL);
                // return;
                // }
                // Item randomItem = null;
                // int[] items = {ItemName.DUA_XANH, ItemName.DU_DU, ItemName.MANG_CAU,
                // ItemName.XOAI_CHIN, ItemName.TRAI_SUNG,
                // ItemName.MAM_TRAI_CAY, ItemName.NGOC_RONG_4_SAO, ItemName.THIEN_LONG_TU_THAN,
                // ItemName.CHIEN_THAN_LUC_QUANG, ItemName.CHIEN_THAN_KIM_HOANG};
                // int iditem = items[Utils.nextInt(items.length)];
                // if (iditem == ItemName.NGOC_RONG_4_SAO) {
                // int index = Utils.nextInt(0, 3);
                // iditem = ItemName.NGOC_RONG_4_SAO + index;
                // }
                // randomItem = new Item(iditem);
                // randomItem.quantity = 1;
                // randomItem.setDefaultOptions();
                // if (iditem == ItemName.THIEN_LONG_TU_THAN || iditem ==
                // ItemName.CHIEN_THAN_LUC_QUANG || iditem == ItemName.CHIEN_THAN_KIM_HOANG) {
                // randomItem.addItemOption(new ItemOption(93, 1));
                // }
                // removeItem(item.indexUI, 1);
                // pointBanhChung += 10;
                // isChangePoint = true;
                // addItem(randomItem);
                // this.service.sendThongBao("Bạn vừa nhận được " + randomItem.template.name);
                break;
            case ItemName.BANH_TET:
                // if (isBagFull()) {
                // service.sendThongBao(Language.ME_BAG_FULL);
                // return;
                // }
                // Item itmRandom = null;
                // int[] itemsBanhTet = {ItemName.NGOC_RONG_LOC_PHAT_1_SAO,
                // ItemName.NGOC_RONG_LOC_PHAT_2_SAO, ItemName.NGOC_RONG_LOC_PHAT_3_SAO,
                // ItemName.NGOC_RONG_LOC_PHAT_4_SAO,
                // ItemName.NGOC_RONG_LOC_PHAT_5_SAO, ItemName.NGOC_RONG_LOC_PHAT_6_SAO,
                // ItemName.NGOC_RONG_LOC_PHAT_7_SAO, ItemName.GOKU_TI_HON,
                // ItemName.MEO_SAO_HOA, ItemName.HO_MAP_VANG_942, ItemName.HO_MAP_XANH_944};
                // int iditm = itemsBanhTet[Utils.nextInt(itemsBanhTet.length)];
                // itmRandom = new Item(iditm);
                // itmRandom.quantity = 1;
                // itmRandom.setDefaultOptions();
                // removeItem(item.indexUI, 1);
                // addItem(itmRandom);
                // pointBanhChung += 10;
                // isChangePoint = true;
                // this.service.sendThongBao("Bạn vừa nhận được " + itmRandom.template.name);
                break;
            case ItemName.HOP_QUA_XUAN:
                // if (isBagFull()) {
                // service.sendThongBao(Language.ME_BAG_FULL);
                // return;
                // }
                // Item quaxuan = null;
                // int[] quaxuans = {ItemName.NGOC_SEN_HONG, ItemName.NGOC_SEN_VANG,
                // ItemName.GOKU_ROSE_TET, ItemName.CUONG_NO_2, ItemName.BO_HUYET_2,
                // ItemName.BO_KHI_2};
                // int idxuan = quaxuans[Utils.nextInt(quaxuans.length)];
                // quaxuan = new Item(idxuan);
                // quaxuan.quantity = 1;
                // switch (idxuan) {
                // case ItemName.NGOC_SEN_HONG:
                // quaxuan.isLock = true;
                // quaxuan.addItemOption(new ItemOption(50, Utils.nextInt(2, 5)));
                // quaxuan.addItemOption(new ItemOption(77, Utils.nextInt(2, 5)));
                // quaxuan.addItemOption(new ItemOption(103, Utils.nextInt(2, 5)));
                // quaxuan.addItemOption(new ItemOption(5, Utils.nextInt(1, 4)));
                // quaxuan.addItemOption(new ItemOption(93, 1));
                // break;
                // case ItemName.NGOC_SEN_VANG:
                // quaxuan.isLock = true;
                // quaxuan.addItemOption(new ItemOption(50, Utils.nextInt(2, 5)));
                // quaxuan.addItemOption(new ItemOption(77, Utils.nextInt(2, 5)));
                // quaxuan.addItemOption(new ItemOption(103, Utils.nextInt(2, 5)));
                // quaxuan.addItemOption(new ItemOption(Utils.nextInt(196, 197),
                // Utils.nextInt(1, 4)));
                // quaxuan.addItemOption(new ItemOption(93, 1));
                // break;
                // case ItemName.GOKU_ROSE_TET:
                // int rand = Utils.nextInt(0, 2);
                // quaxuan.isLock = true;
                // quaxuan.addItemOption(new ItemOption(50, 25));
                // quaxuan.addItemOption(new ItemOption(77, 25));
                // quaxuan.addItemOption(new ItemOption(103, 25));
                // quaxuan.addItemOption(new ItemOption(93, Utils.nextInt(1, 3)));
                // if (rand == 0) {
                // quaxuan.addItemOption(new ItemOption(95, 10));
                // quaxuan.addItemOption(new ItemOption(96, 10));
                // } else if (rand == 1) {
                // quaxuan.addItemOption(new ItemOption(95, 10));
                // quaxuan.addItemOption(new ItemOption(96, 10));
                // quaxuan.addItemOption(new ItemOption(101, 20));
                // } else {
                // quaxuan.addItemOption(new ItemOption(94, 10));
                // }
                // break;
                // default:
                // break;
                // }
                // pointHopQua += 10;
                // isChangePoint = true;
                // removeItem(item.indexUI, 1);
                // addItem(quaxuan);
                // this.service.sendThongBao("Bạn vừa nhận được " + quaxuan.template.name);
                break;
            case ItemName.HOP_QUA_TET_AN_KHANG:
                // if (isBagFull()) {
                // service.sendThongBao(Language.ME_BAG_FULL);
                // return;
                // }
                //
                // Item quatet = null;
                // int[] quatets = {ItemName.NGOC_THAN_LONG, ItemName.NGOC_THAN_CONG,
                // ItemName.NUOC_TANG_CUONG, ItemName.CA_NOI_GIAN, ItemName.HAT_DAU_VANG};
                // int idtet = quatets[Utils.nextInt(quatets.length)];
                // quatet = new Item(idtet);
                // quatet.quantity = 1;
                // quatet.setDefaultOptions();
                // if (idtet == ItemName.NGOC_THAN_CONG) {
                // quatet.isLock = true;
                // int rd = Utils.nextInt(100);
                // quatet.addItemOption(new ItemOption(50, Utils.nextInt(1, 4)));
                // quatet.addItemOption(new ItemOption(77, Utils.nextInt(1, 4)));
                // quatet.addItemOption(new ItemOption(103, Utils.nextInt(1, 4)));
                // if (rd < 95) {
                // quatet.addItemOption(new ItemOption(93, Utils.nextInt(1, 5)));
                // }
                // }
                // if (idtet == ItemName.NGOC_THAN_LONG) {
                // quatet.isLock = true;
                // int rd = Utils.nextInt(100);
                // quatet.addItemOption(new ItemOption(50, Utils.nextInt(2, 6)));
                // quatet.addItemOption(new ItemOption(77, Utils.nextInt(2, 6)));
                // quatet.addItemOption(new ItemOption(103, Utils.nextInt(2, 6)));
                // if (rd < 95) {
                // quatet.addItemOption(new ItemOption(93, Utils.nextInt(1, 5)));
                // }
                // }
                // pointHopQua += 10;
                // isChangePoint = true;
                // removeItem(item.indexUI, 1);
                // addItem(quatet);
                // this.service.sendThongBao("Bạn vừa nhận được " + quatet.template.name);
                break;
            case ItemName.RUONG_GO: {
                MartialCongress martialCongress = MapManager.getInstance().martialCongress;
                if (martialCongress.isOpenRuongGo(this.id)) {
                    service.serverMessage("Mỗi ngày bạn chỉ có thể sử dụng 1 rương gỗ");
                    return;
                }
                if (getSlotNullInBag() < 13) {
                    service.serverMessage("Bạn cần 13 ô trống để mở ");
                    return;
                }
                if (item.options.size() < 0) {
                    return;
                }
                boolean isOpen = true;
                int level = 0;
                for (ItemOption io : item.options) {
                    if (io.id == 227) {
                        isOpen = false;
                    } else if (io.id == 72) {
                        level = io.param;
                    }
                }
                martialCongress.savePlayerOpenRuongGoDHVT23(this.id);
                // if (!isOpen) {
                // service.serverMessage("Bạn chỉ có thể mở sau thời gian bảo trì");
                // return;
                // }
                menus.clear();
                Item it;
                StringBuilder sbnew = new StringBuilder();
                sbnew.append("Bạn đã nhận được\n");
                switch (level) {
                    case 1: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                        service.openUIConfirm(NpcName.CON_MEO, String.valueOf(sbnew), getPetAvatar(), menus);
                    }
                    break;
                    case 2: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.quantity = 3;
                        it.setDefaultOptions();
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 5;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                    }
                    break;
                    case 3: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                    }
                    break;
                    case 4: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.NGOC_RONG_4_7_SAO.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_4.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                    }
                    break;
                    case 5: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.NGOC_RONG_4_7_SAO.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_4.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 5;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                    }
                    break;
                    case 6: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.GINYUFORCE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_4.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    case 7: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.GINYUFORCE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_4.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    case 8: {
                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.GINYUFORCE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(3, 3);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_4.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 20;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    case 9: {

                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 5;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(4, 4);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_9.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    case 10: {

                        int itemId = RandomItem.DA_NANG_CAP.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.SAO_PHA_LE.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_2.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(4, 4);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(4, 4);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_9.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 5;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    case 11: {

                        int itemId = RandomItem.ITEM_CAP_2.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.NGOC_RONG_2_SAO;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(3, 5);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(3, 5);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_9.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    case 12: {

                        int itemId = RandomItem.ITEM_CAP_2.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 5;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.NGOC_RONG_2_SAO;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(3, 5);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(3, 5);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_9.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.MANH_DOI_TRUONG_VANG_956;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.MANH_DOC_NHAN;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    case 13: {
                        int itemId = RandomItem.ITEM_CAP_2.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.ITEM_CAP_1.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 7;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.NGOC_RONG_2_SAO;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.NGOC_RONG_3_SAO;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(3, 5);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.DO_CUOI.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.addRandomOption(3, 5);
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_9.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 10;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.MANH_DOI_TRUONG_VANG_956;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 2;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = ItemName.MANH_DOC_NHAN;
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 3;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");

                        itemId = RandomItem.RUONG_GO_LEVEL_13.next();
                        it = new Item(itemId);
                        it.setDefaultOptions();
                        it.quantity = 1;
                        addItem(it);
                        sbnew.append("|1|" + it.quantity + " " + it.template.name + "\n");
                    }
                    break;
                    default:
                        service.serverMessage("Có lỗi xảy ra");
                        return;
                }
                menus.add(new KeyValue(CMDMenu.CANCEL, "OK"));
                service.openUIConfirm(NpcName.CON_MEO, String.valueOf(sbnew), getPetAvatar(), menus);
                removeItem(item.indexUI, 1);
            }
            break;
            case ItemName.NGOC_RONG_THIEN_MENH_1_SAO:
                int indexThienMenh1s = getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_1_SAO);
                int indexThienMenh2s = getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_2_SAO);
                int indexThienMenh3s = getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_3_SAO);
                int indexThienMenh4s = getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_4_SAO);
                int indexThienMenh5s = getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_5_SAO);
                int indexThienMenh6s = getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_6_SAO);
                int indexThienMenh7s = getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_7_SAO);
                if (indexThienMenh1s == -1 || indexThienMenh2s == -1 || indexThienMenh3s == -1 || indexThienMenh4s == -1
                        || indexThienMenh5s == -1
                        || indexThienMenh6s == -1 || indexThienMenh7s == -1) {
                    this.service.sendThongBao("Thu thập đủ 7 viên ngọc rồng Thiên Mệnh để thực hiện điều ước");
                    return;
                }
                menus.clear();
                menus.add(new KeyValue(CMDMenu.UOC_RONG_THIEN_MENH_1, "Tăng\n100% TNSM\nSP + Đệ"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_THIEN_MENH_2, "Tăng\n15%\nSức Đánh"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_THIEN_MENH_3, "Ngẫu nhiên\n1-3\nĐá Bảo Vệ"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_THIEN_MENH_4, "Tăng\n15%\nHP,KI"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_THIEN_MENH_5, "Tăng\n10% HP,KI,SĐ\nĐệ Tử"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_THIEN_MENH_6, "x1 Mảnh Đội Trưởng Vàng"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO,
                        "Hãy chọn điều ước bạn mong muốn\nMỗi điều ước sẽ có tác dụng trong 20 phút", getPetAvatar(),
                        menus);
                break;
            case ItemName.NGOC_RONG_LOC_PHAT_1_SAO:
                int indexLocPhat1s = getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_1_SAO);
                int indexLocPhat2s = getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_2_SAO);
                int indexLocPhat3s = getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_3_SAO);
                int indexLocPhat4s = getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_4_SAO);
                int indexLocPhat5s = getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_5_SAO);
                int indexLocPhat6s = getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_6_SAO);
                int indexLocPhat7s = getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_7_SAO);
                if (indexLocPhat1s == -1 || indexLocPhat2s == -1 || indexLocPhat3s == -1 || indexLocPhat4s == -1
                        || indexLocPhat5s == -1
                        || indexLocPhat6s == -1 || indexLocPhat7s == -1) {
                    this.service.sendThongBao("Thu thập đủ 7 viên ngọc rồng Lộc Phát để thực hiện điều ước");
                    return;
                }
                menus.clear();
                menus.add(new KeyValue(CMDMenu.UOC_RONG_LOC_PHAT_1, "Tăng\n100% TNSM\nSư phụ"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_LOC_PHAT_2, "Tăng\n10% HP,KI\nSư phụ"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_LOC_PHAT_3, "Tăng\n10% Sức đánh\nSư phụ"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_LOC_PHAT_4, "Đổi Kỹ\nNăng 3, 4\nĐệ tử"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_LOC_PHAT_5, "Đổi Kỹ\nNăng 2\nĐệ tử"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_LOC_PHAT_6, "Đổi Max\nnội tại\ntùy chọn"));
                menus.add(new KeyValue(CMDMenu.UOC_RONG_LOC_PHAT_7, "Tăng\n5% HP,KI,SĐ\nĐệ tử"));
                service.openUIConfirm(NpcName.CON_MEO,
                        "Hãy chọn điều ước bạn mong muốn\nMỗi điều ước sẽ có tác dụng trong 10 phút", getPetAvatar(),
                        menus);
                break;
            case 1994:
                List<SpecialSkillTemplate> list = SpecialSkill.getListSpecialSkill(this.gender);
                menus.clear();
                for (SpecialSkillTemplate s : list) {
                    menus.add(new KeyValue(
                            CMDMenu.CHANGE_NOI_TAI,
                            s.info.replace("#", String.valueOf(s.max) + "%"),
                            s.id));
                }
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn nội tại bạn mong muốn", getPetAvatar(), menus);
                break;
            case 2243:
                menus.clear();
                menus.add(new KeyValue(1168, "Tăng\n15% HP,KI 30p", 0));
                menus.add(new KeyValue(1168, "Tăng\n15% SD 30p", 1));
                menus.add(new KeyValue(1168, "Tăng\n10% chỉ số đệ tử 30p", 2));
                menus.add(new KeyValue(1168, "x2 TNSM trong 30p", 3));
                menus.add(new KeyValue(1168, "x2 Item cấp 2 mỗi loại", 4));
                menus.add(new KeyValue(1168, "Vé đổi nội tại max cs", 5));

                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn điều ước bạn mong muốn", getPetAvatar(), menus);
                break;
            case ItemName.RUONG_DO_CUOI_KICH_HOAT:
                if (getSlotNullInBag() < 7) {
                    service.serverMessage("Bạn cần ít nhất 6 ô trống trong hành trang ");
                    return;
                }
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_TD_KH, "Set Trái Đất"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_NM_KH, "Set Namec"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_CUOI_XD_KH, "Set Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn hành tinh bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case ItemName.RUONG_THAN_LINH_KICH_HOAT:
                if (getSlotNullInBag() < 7) {
                    service.serverMessage("Bạn cần ít nhất 6 ô trống trong hành trang ");
                    return;
                }
                menus.clear();
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_TD_KH, "Set Thần Linh Trái Đất"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_NM_KH, "Set Thần Linh Namec"));
                menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_XD_KH, "Set Thần Linh Xayda"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
                service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set thần linh bạn mong muốn ?", getPetAvatar(), menus);
                break;
            case ItemName.RUONG_THAN_LINH:
                openRTL(item);
                break;

            case ItemName.PHAO_TET:
                // if (isBagFull()) {
                // service.sendThongBao(Language.ME_BAG_FULL);
                // return;
                // }
                // if (this.zone.map.mapID != 175) {
                // service.sendThongBao("Bạn phải ở trong map Quảng Trường Pháo Hoa");
                // return;
                // }
                // Item itphaohoa = null;
                // int[] itphoahoas = {ItemName.QUA_HONG_DAO, ItemName.CAI_TRANG_CADIC,
                // ItemName.CAI_TRANG_GOHAN, ItemName.CAI_TRANG_POCOLO,
                // ItemName.DANH_HIEU_BE_NGOAN,
                // ItemName.DANH_HIEU_PHONG_BA, ItemName.CUONG_NO_2, ItemName.BO_HUYET_2,
                // ItemName.BO_KHI_2};
                // int itid = itphoahoas[Utils.nextInt(itphoahoas.length)];
                // itphaohoa = new Item(itid);
                // itphaohoa.quantity = 1;
                // itphaohoa.setDefaultOptions();
                // switch (itid) {
                // case ItemName.CAI_TRANG_CADIC:
                // case ItemName.CAI_TRANG_GOHAN:
                // case ItemName.CAI_TRANG_POCOLO:
                // itphaohoa.addItemOption(new ItemOption(116, 1));
                // itphaohoa.addItemOption(new ItemOption(93, 1));
                // break;
                // case ItemName.DANH_HIEU_BE_NGOAN:
                // itphaohoa.addItemOption(new ItemOption(50, 4));
                // itphaohoa.addItemOption(new ItemOption(77, 4));
                // itphaohoa.addItemOption(new ItemOption(103, 4));
                // itphaohoa.addItemOption(new ItemOption(101, 15));
                // itphaohoa.addItemOption(new ItemOption(93, Utils.nextInt(1, 5)));
                // break;
                // case ItemName.DANH_HIEU_PHONG_BA:
                // itphaohoa.addItemOption(new ItemOption(50, 5));
                // itphaohoa.addItemOption(new ItemOption(77, 5));
                // itphaohoa.addItemOption(new ItemOption(103, 5));
                // itphaohoa.addItemOption(new ItemOption(93, Utils.nextInt(1, 5)));
                // break;
                // }
                // this.service.phaohoaZone();
                // removeItem(item.indexUI, 1);
                // SessionManager.countPhaoHoa++;
                // addItem(itphaohoa);
                // pointPhaoHoa += 10;
                // isChangePoint = true;
                // this.service.sendThongBao("Bạn vừa nhận được " + itphaohoa.template.name);
                // if (SessionManager.countPhaoHoa % 20 == 0) {
                // SessionManager.addBigMessage(String.format(
                // "Pháo tết đang được nổ tại map Quảng Trường Pháo Hoa \n"
                // + "Số lượng pháo hiện tại là : %d \n, sẽ nổ sau 100 quả"
                // + "Hãy nhanh chân tới khu vực nổ pháo để có thể nhận được các phần quà hấp
                // dẫn nhé !!!!!",
                // SessionManager.countPhaoHoa));
                // sendChatGlobalFromAdmin();
                // }
                // if (SessionManager.countPhaoHoa >= 100) {
                // dropThoiVang();
                // this.service.phaohoaFull();
                // SessionManager.countPhaoHoa = 0;
                // }
                break;
        }
    }

    public void openRTL(Item item) {
        if (item == null || item.template.id != 1996) {
            return;
        }
        if (getSlotNullInBag() < 7) {
            service.serverMessage("Bạn cần ít nhất 6 ô trống trong hành trang ");
            return;
        }
        menus.clear();
        menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_TD, "Set Thần Linh Trái Đất"));
        menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_NM, "Set Thần Linh Namec"));
        menus.add(new KeyValue(CMDMenu.OPEN_SET_TL_XD, "Set Thần Linh Xayda"));
        menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
        service.openUIConfirm(NpcName.CON_MEO, "Hãy chọn set thần linh bạn mong muốn ?", getPetAvatar(), menus);
    }

    public void dropThoiVang() {
        TMap map = MapManager.getInstance().getMap(175);
        if (map != null) {
            for (Zone zoneDrop : map.zones) {
                int soluong = 50;
                for (int i = 0; i < soluong; i++) {
                    Item item = new Item(ItemName.THOI_VANG);
                    item.setDefaultOptions();
                    item.quantity = 1;
                    ItemMap itemMap = new ItemMap(zone.autoIncrease++);
                    itemMap.item = item;
                    itemMap.playerID = -1;
                    itemMap.x = (short) ((map.width / soluong) * i);
                    itemMap.y = zoneDrop.map.collisionLand(itemMap.x, getY());
                    zoneDrop.addItemMap(itemMap);
                    zoneDrop.service.addItemMap(itemMap);
                }
            }
        }
    }

    public void setFlag(byte flag) {
        this.flag = flag;
        if (isHuman()) {
            if (myDisciple != null) {
                myDisciple.setFlag(flag);
            }
        }
        if (zone != null) {
            zone.service.flag(this);
        }
    }

    public void openBox(short icon1, short icon2) {
        service.combine((byte) 6, null, icon1, icon2);
    }

    public void mapTransport(Message ms) {
        // if (escortedPerson != null) {
        // service.sendThongBao(String.format("Bạn đang hộ tống %s, không thể thực
        // hiện.", escortedPerson.name));
        // return;
        // }
        if (listMapTransport == null) {
            return;
        }
        if (this.idNRNM != -1) {
            service.sendThongBao("Không thể thực hiện");
            return;
        }
        if (zone.map.isBlackDragonBall()) {
            if (itemLoot != null) {
                service.sendThongBao("Không thể thực hiện");
                return;
            }
        }

        lock.lock();
        try {
            int index = ms.reader().readByte();
            if (index < 0 || index >= listMapTransport.size()) {
                return;
            }
            int cmd = getCommandTransport();
            if (cmd == 0) {
                Item item = itemBag[capsule];
                if (item != null && (item.id == 193 || item.id == 194)) {
                    if (item.id == 193) {
                        removeItem(capsule, 1);
                    }
                } else {
                    return;
                }
                KeyValue<Integer, String> keyValue = listMapTransport.get(index);
                int mapID = keyValue.key;
                TMap curr = zone.map;
                Zone z = zone;
                String planet = "";
                switch (curr.planet) {
                    case 0:
                        planet = "Trái đất";
                        break;

                    case 1:
                        planet = "Namêc";
                        break;

                    case 2:
                        planet = "Xay da";
                        break;
                }
                if (curr.isCold()) {
                    planet = "Cold";
                } else if (curr.isFuture()) {
                    planet = "Tương lai";
                } else if (curr.isNappa()) {
                    planet = "Fide";
                }
                isGoBack = currMap == keyValue;
                TMap map = zone.map;
                if (!map.isCantGoBack()) {
                    currMap = new KeyValue(curr.mapID, "Về chỗ cũ: " + curr.name, planet);
                } else {
                    currMap = null;
                }
                teleport(mapID);
                currZoneId = z.zoneID;
                listMapTransport = null;
            } else if (cmd == 1) {
                if (MapManager.getInstance().blackDragonBall == null) {
                    service.serverMessage2("Đã kết thúc");
                    return;
                }
                KeyValue<Integer, String> keyValue = listMapTransport.get(index);
                int mapID = keyValue.key;
                TMap map = MapManager.getInstance().getMap(mapID);
                short x = calculateX(map);
                zone.leave(this);
                this.x = x;
                this.y = map.collisionLand(x, (short) 24);
                int zoneID = map.getZoneID();
                map.enterZone(this, zoneID);
            }

        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 69");
            logger.error("failed!", ex);
        } finally {
            lock.unlock();
        }
    }

    public void createDisciple(int type, byte bonus) {
        try {
            if (myDisciple == null) {
                lastAttack = System.currentTimeMillis();
                Disciple disciple = new Disciple();
                disciple.typeDisciple = (byte) type;
                disciple.id = -this.id;
                disciple.name = "Đệ tử";
                disciple.itemBody = new Item[13];
                disciple.gender = disciple.classId = (byte) Utils.nextInt(3);
                if (type != 0) {
                    disciple.gender = disciple.classId = this.gender;
                }
                disciple.info = new Info(disciple);
                disciple.info.setChar(disciple);
                disciple.info.setPowerLimited();
                disciple.info.applyCharLevelPercent();
                disciple.info.setStamina();
                disciple.skills = new ArrayList<>();
                disciple.skillOpened = 0;
                disciple.learnSkill();
                disciple.discipleStatus = 0;
                disciple.info.setInfo();
                disciple.petBonus = bonus;
                disciple.info.recovery(Info.ALL, 100, false);
                disciple.service = new Service(disciple);

                disciple.setDefaultPart();
                myDisciple = disciple;
                disciple.saveData();
                disciple.setMaster(this);
                disciple.followMaster();
                service.petInfo((byte) 1);
                zone.enter(myDisciple);
                service.chat(myDisciple, "Sư phụ hãy nhận con làm đệ tử");
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 68");
            logger.error("failed!", ex);
        }
    }

    public void createDisciple(int type) {
        try {
            if (myDisciple == null) {
                lastAttack = System.currentTimeMillis();
                Disciple disciple = new Disciple();
                disciple.typeDisciple = (byte) type;
                disciple.id = -this.id;
                disciple.name = "Đệ tử";
                disciple.itemBody = new Item[13];
                disciple.gender = disciple.classId = (byte) Utils.nextInt(3);
                if (type != 0) {
                    disciple.gender = disciple.classId = this.gender;
                }
                disciple.petBonus = (byte) Utils.nextInt(5);
                disciple.info = new Info(disciple);
                disciple.info.setChar(disciple);
                disciple.info.setPowerLimited();
                disciple.info.applyCharLevelPercent();
                disciple.info.setStamina();
                disciple.skills = new ArrayList<>();
                disciple.skillOpened = 0;
                disciple.learnSkill();
                disciple.discipleStatus = 0;
                disciple.info.setInfo();
                disciple.info.recovery(Info.ALL, 100, false);
                disciple.service = new Service(disciple);
                disciple.setDefaultPart();
                myDisciple = disciple;
                disciple.saveData();
                disciple.setMaster(this);
                disciple.followMaster();
                service.petInfo((byte) 1);
                zone.enter(myDisciple);
                service.chat(myDisciple, "Sư phụ hãy nhận con làm đệ tử");
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 67");
            logger.error("failed!", ex);
        }
    }

    public void changeDisciple() {
        if (myDisciple != null) {
            byte oldbonus = myDisciple.petBonus;
            deleteDisciple();
            createDisciple(0, oldbonus);
        }
    }

    public void deleteDisciple() {
        try {
            if (myDisciple != null) {
                if (myDisciple.zone != null) {
                    myDisciple.zone.leave(myDisciple);
                }
                GameRepository.getInstance().disciple.deleteById(myDisciple.id);
                myDisciple = null;
            }
            service.petInfo((byte) 0);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 66");
            logger.error("failed!", e);
        }
    }

    public void learnSkill(Skill skill, int type, int index, Item item) {
        try {
            if (type == 0) {
                skills.add(skill.clone());
            }
            if (type == 1) {
                skills.set(index, skill.clone());
            }
        } catch (CloneNotSupportedException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 65");
            logger.error("failed!", ex);

        }
        info.recovery(Info.ALL, 100, false);
        service.loadSkill();
        int indexItem = item.indexUI;
        removeItem(indexItem, 1);
        service.updateBag(indexItem, 0);
        service.sendThongBao("Bạn học thành công " + skill.template.name + " cấp " + skill.point);
    }

    public void requestChangeZone(Message ms) {
        lock.lock();
        try {
            if (this.isDead) {
                return;
            }

            if (this.zone != null) {
                try {
                    byte index = ms.reader().readByte();
                    TMap map = this.zone.map;
                    if (map.isCantChangeZone()) {
                        return;
                    }
                    int zoneID = -1;
                    if (index == -1) {
                        if (!isAutoPlay()) {
                            return;
                        }
                        zoneID = (byte) map.randomZoneID();
                    } else {
                        int size = map.zones.size();
                        if (index < 0 || index >= size) {
                            return;
                        }
                        Zone z = map.zones.get(index);
                        zoneID = z.zoneID;
                    }
                    Zone z = map.getZoneByID(zoneID);
                    if (z == null) {
                        return;
                    }
                    long delay = 12;
                    if (sachdacbiet[7] || exitsItemTime(ItemTimeName.BANH_TRAI_CAY)) {
                        delay = 5;
                    }
                    long now = System.currentTimeMillis();
                    int seconds = (int) ((now - lastTimeRequestChangeZone) / 1000);
                    int timeWaitWithDragonBallNamec = (int) ((now - lastTimePickUpDragonBallNamec) / 1000);

                    if (map.isCantChangeZone() && this.getSession().user.getRole() != 1) {
                        service.dialogMessage("Không thể chuyển khu vực.");
                        return;
                    }

                    if ((itemLoot != null && itemLoot.isDragonBallNamec()) && timeWaitWithDragonBallNamec < 60) {
                        service.addBigMessage(getPetAvatar(), String
                                .format("Chưa thể chuyển khu vực lúc này vui lòng chờ %d giây nữa",
                                        60 - timeWaitWithDragonBallNamec),
                                (byte) 0, null, null);
                        return;
                    }
                    if (seconds < delay && this.getSession().user.getRole() != 1) {
                        service.addBigMessage(getPetAvatar(), String
                                .format("Chưa thể chuyển khu vực lúc này vui lòng chờ %d giây nữa", delay - seconds),
                                (byte) 0, null, null);
                        return;
                    }
                    if (!z.getBossInZone().isEmpty()) {
                        if (this.session.user.getActivated() == 0) {
                            service.addBigMessage(getPetAvatar(),
                                    "Khu vực có BOSS , cần kích hoạt tài khoản để vào khu này",
                                    (byte) 0, null, null);
                            return;
                        }
                    }
                    if (!MainUpdate.CanEnterZoneSupportMisson(this, z)) {
                        service.addBigMessage(getPetAvatar(),
                                "Khu vực có BOSS và đang trong khung giờ hổ trợ (17h00-19h59) , bạn không thể vào lúc này",
                                (byte) 0, null, null);
                        return;
                    }
                    lastTimeRequestChangeZone = now;// fix đổi khu
                    lastTimePickUpDragonBallNamec = now;
                    if (seconds < delay && this.getSession().user.getRole() != 1) {
                        service.addBigMessage(getPetAvatar(), String
                                .format("Chưa thể chuyển khu vực lúc này vui lòng chờ %d giây nữa", delay - seconds),
                                (byte) 0, null, null);
                        return;
                    }
                    lastTimeRequestChangeZone = now;// fix đổi khu
                    if (z.getNumPlayer() < z.getMaxPlayer() || this.getSession().user.getRole() == 1) {
                        zone.leave(this);
                        z.enter(this);
                    } else {
                        service.serverMessage2("Khu vực đã đầy.");
                    }
                } catch (IOException ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 64");
                    logger.error("failed!", ex);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void selectSkill(Message ms) {
        try {
            short skillId = ms.reader().readShort();
            selectSkill(skillId);
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 63");
            logger.error("failed!", ex);
        }
    }

    public void selectSkill(short id) {
        if (this.isCharge() || this.isSkillSpecial()) {
            return;
        }
        for (Skill skill : this.skills) {
            if (skill.template.id == id) {
                this.select = skill;
            }
        }
    }

    public boolean isCancelTroi() {
        return isFreeze || isSleep || isStone || isBlind;
    }

    public boolean isKhongChe() {
        return isFreeze || isSleep || isHeld || isStone || isBlind;
    }

    public boolean meCanAttack() {
        return !isDead && !isFreeze && !isSleep && !isHeld && select != null && !isStone;
    }

    public void attackNpc(Message ms) {
        try {
            if (!meCanAttack()) {
                return;
            }
            if (this.isRecoveryEnergy) {
                stopRecoveryEnery();
            }
            if (ms.reader().available() > 0) {
                int id = ms.reader().readInt();

                if (id == 0) {
                    // mob me
                    id = ms.reader().readInt();
                    Player _player = zone.findCharByID(id);
                    if (_player != null && isMeCanAttackOtherPlayer(_player)) {
                        if (_player.mobMe != null) {
                            Mob mob = _player.mobMe;
                            zone.attackNpc(this, mob, true);
                            if (mob.hp <= 0) {
                                zone.service.mobMeUpdate(_player, null, -1, (byte) -1, (byte) 6);
                                _player.mobMe = null;
                            }
                        }
                    }
                } else {
                    Mob mob = zone.findMobByID(id);
                    int tempId = ms.reader().readInt();
                    if (mob == null) {
                        mob = zone.findMobByTemplateID(tempId, false);
                    }
                    if (mob != null) {
                        if (mob.status == 4) {
                            if (this.hold != null) {
                                this.hold.close();
                            }
                            zone.attackNpc(this, mob, false);
                            // if (tempId == 70) {
                            // zone.service.attackHiduregarn(mob);
                            // if (mob.hp <= 0) {
                            // delete(mob);
                            // service.setMapInfo();
                            // }
                            // }

                        }
                    }

                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 62");
            logger.error("failed!", ex);
        }
    }

    public void delete(Mob mob) {
        try {

            mob.setX((short) -1000);
            mob.setY((short) -1000);
            Message ms = new Message(Cmd.BIG_BOSS);
            FastDataOutputStream ds = ms.writer();
            ds.writeByte(7);
            ds.flush();
            zone.service.sendMessage(ms, null);
            ms.cleanup();
            zone.removeMob(mob);

        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 61");
            logger.debug("attack err" + ex.toString());
        }
    }

    public void finishLoadMap() {
        // if (activated) {
        if (taskMain != null && taskMain.id == 4 && taskMain.index == 0) {
            taskNext();
        }

        // }
        int mapID = -1;

        if (zone != null) {
            mapID = zone.map.mapID;
        }

        if (mapID == 39 || mapID == 40 || mapID == 41) {
            String petName = new String[]{"Puaru", "Piano", "Icarus"}[gender];
            service.openUISay((short) NpcName.CON_MEO, String.format(
                    "Chào mừng bạn đến với thế giới Ngọc Rồng!\nMình là %s sẽ đồng hành cùng bạn ở thế giới này\nĐể di chuyển, hãy chạm 1 lần vào nơi muốn đến",
                    petName), (short) getPetAvatar());
        }
        if (taskMain != null) {
            Task task = taskMain;
            if (task.id == 0 && task.index == 1 && task.mapTasks[1] == mapID) {
                String text = TaskText.TASK_0_1[gender];
                service.openUISay(NpcName.CON_MEO, text, getPetAvatar());
                taskNext();
            }
            if (mapID == 47) {
                if (task.id == 8 && task.index == 3) {
                    updateTask(9);
                }
            }
            if (mapID == 46) {
                if (task.id == 9 && task.index == 2) {
                    taskNext();
                }
            }
            if (task.id == 11 && task.index == 0) {
                int map = (new int[]{5, 13, 20})[gender];
                if (mapID == map) {
                    taskNext();
                }
            }
            if (mapID == 93) {
                if (task.id == 22 && task.index == 1) {
                    taskNext();
                }
            }
            if (mapID == 97) {
                if (task.id == 24 && task.index == 0) {
                    taskNext();
                }
            }
            if (mapID == 104) {
                if (task.id == 23 && task.index == 0) {
                    taskNext();
                }
            }
            if (mapID == 100) {
                if (task.id == 25 && task.index == 0) {
                    taskNext();
                }
            }
            if (mapID == 103) {
                if (task.id == 26 && task.index == 1) {
                    taskNext();
                }
            }
            if (mapID == MapName.CUA_AI_1) {
                if (task.id == 27 && task.index == 0) {
                    taskNext();
                }
            }
            if (mapID == MapName.CONG_PHI_THUYEN_2) {
                if (task.id == 28 && task.index == 0) {
                    taskNext();
                }
            }
        }
        loadEffectFreeze();
        loadEffectSkillOnMob();
        if (this.mobMe != null) {
            service.mobMeUpdate(this, null, -1, (byte) -1, (byte) 0);
        }
        if (this.petFollow != null) {
            service.petFollow(this, (byte) 1);
        }
        service.updateBag(this);
        List<Player> list = zone.getListChar(Zone.TYPE_ALL);
        for (Player _c : list) {
            if (_c != this) {
                service.playerAdd(_c);
                if (_c.petFollow != null) {
                    service.petFollow(_c, (byte) 1);
                }
                loadEffectSkillPlayer(_c);
                if (_c.mobMe != null) {
                    service.mobMeUpdate(_c, null, -1, (byte) -1, (byte) 0);
                }
            }
        }
        if (isAutoPlay) {
            service.autoPlay(true);
        }
    }

    public boolean useSkill(Object obj) {
        long now = System.currentTimeMillis();
        lastAttack = now;
        Skill skill = this.select;
        long manaUse = skill.manaUse;
        if (this.select.template.manaUseType == 1) {
            manaUse = Utils.percentOf(this.info.mpFull, manaUse);
        }
        if (isBoss()) {
            manaUse = 0;
        }
        if (skill.template.id == SkillName.TRI_THUONG) {
            if (obj instanceof Player) {
                if (achievements != null) {
                    achievements.get(14).addCount(1);// kỹ năng thành thạo
                }
                skill.lastTimeUseThisSkill = now;
                Player _player = (Player) obj;
                int distance = Utils.getDistance(0, 0, skill.dx, skill.dy);
                List<Player> list = zone.getListChar(Zone.TYPE_HUMAN, Zone.TYPE_PET);
                for (Player _c : list) {
                    if (_c != this) {
                        int distance2 = Utils.getDistance(_c.x, _c.y, _player.x, _player.y);
                        if (distance2 < distance) {
                            zone.service.chat(_c, String.format("Cảm ơn %s đã cứu mình", this.name));
                            _c.revival(skill.damage);
                        }
                    }
                }
                this.info.recovery(Info.HP, skill.damage, true);
                info.mp -= manaUse;
            }
            return false;
        }
        if (skill.template.id == SkillName.KAIOKEN) {
            long percent = this.info.hp * 100 / this.info.hpFull;
            if (percent <= 10) {
                return false;
            }
            this.info.recovery(Info.HP, -10, true);
        }
        if (skill.template.id == SkillName.TROI) {
            if (obj instanceof Mob) {
                Mob mob = (Mob) obj;
                if (mob.hold != null) {
                    return false;
                }
            } else {
                Player _c = (Player) obj;
                if (_c.hold != null) {
                    if (_c.hold.holder.equals(_c)) {
                        _c.hold.close();
                    }

                }
            }
            skill.lastTimeUseThisSkill = now;
            Hold hold = new Hold(this.zone, this, obj, skill.damage);
            hold.start();
            if (specialSkill != null) {
                if (specialSkill.id == 7) {
                    setPercentDamageBonus(specialSkill.param);
                }
            }
            this.isCritFirstHit = true;
            info.mp -= manaUse;
            return false;
        }
        return true;
    }

    public void revival(int percent) {
        if (this.isDead) {
            this.statusMe = 1;
            this.isDead = false;
            service.sendMessage(new Message(Cmd.ME_LIVE));
            zone.service.playerLoadLive(this);
        }
        this.info.recovery(Info.ALL, percent, true);
    }

    public void attackPlayer(Message ms) {
        try {
            if (!meCanAttack()) {
                return;
            }
            if (this.isRecoveryEnergy) {
                stopRecoveryEnery();
            }
            if (ms.reader().available() > 0) {
                int id = ms.reader().readInt();
                if (id == this.id) {
                    return;
                }
                Player _player = zone.findCharByID(id);
                if (_player != null && _player != this
                        && (isMeCanAttackOtherPlayer(_player) || this.select.template.id == SkillName.TRI_THUONG)) {
                    if (this.hold != null) {
                        this.hold.close();
                    }
                    zone.attackPlayer(this, _player);
                }

            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 60");
            logger.error("failed!", ex);
        }
    }

    public boolean isCanAddTimeItemTime(int id) {
        return id >= 7 && id != 14;
    }

    public void addItemTime(ItemTime item) {
        if (item == null) {
            return;
        }
        synchronized (this.itemTimes) {
            for (ItemTime itm : this.itemTimes) {
                if (itm.id == item.id) {
                    if (item.id == 11 || isCanAddTimeItemTime(item.id)) {
                        itm.seconds += item.seconds;
                        if (itm.seconds > 3600 * 60) {
                            itm.seconds = 3600 * 60;
                        }
                    } else {
                        itm.seconds = item.seconds;
                    }
                    service.setItemTime(itm);
                    return;
                }
            }
            this.itemTimes.add(item);
            service.setItemTime(item);
        }
    }

    public ItemTime getItemTime(int id) {
        for (ItemTime itm : this.itemTimes) {
            if (itm.id == id) {
                return itm;
            }
        }
        return null;
    }

    public void setTimeForItemtime(int id, int seconds) {
        synchronized (this.itemTimes) {
            for (ItemTime item : this.itemTimes) {
                if (item.id == id) {
                    item.seconds = seconds;
                    service.setItemTime(item);
                    break;
                }
            }
        }
    }

    public void startRecoveryEnery() {
        this.isRecoveryEnergy = true;
        this.lastUseRecoveryEnery = System.currentTimeMillis();
    }

    public void stopRecoveryEnery() {
        this.isRecoveryEnergy = false;
        if (zone != null && !(this instanceof Broly) && !(this instanceof SuperBroly)) {
            zone.service.skillNotFocus(this, (byte) 3, null, null);
        }
    }

    public void updatePetTheoSau(boolean isUpdate) {
        if (isUpdate) {
            if (this.itemBody != null && this.itemBody[11] != null) {
                this.service.sendPetFollow(this, (short) (this.itemBody[11].template.iconID - 1));
            } else {
                this.service.sendPetFollow(this, (short) 0);
            }
        }
    }

    public void updateEveryOneSeconds() {
        // auto update nv
        try {
            if (taskMain != null) {
                if (taskMain.id == 7 && taskMain.index == 0) {
                    if (info.power >= 16000) {
                        taskNext();
                    }
                }
                if (taskMain.id == 8 && taskMain.index == 0) {
                    if (info.power >= 40000) {
                        taskNext();
                    }
                }
                if (taskMain.id == 14 && taskMain.index == 0) {
                    if (info.power >= 200000) {
                        taskNext();
                    }
                }
                if (taskMain.id == 15 && taskMain.index == 0) {
                    if (info.power >= 500000) {
                        taskNext();
                    }
                }
                if (taskMain.id == 20 && taskMain.index == 0) {
                    if (info.power >= 600000000) {
                        taskNext();
                    }
                }
                if (taskMain.id == 21 && taskMain.index == 0) {
                    if (info.power >= 2000000000) {
                        taskNext();
                    }
                }
            }
            if (this.taskMain != null && this.taskMain.id == 12 && this.taskMain.index == 0) {
                if (this.clan != null) {
                    this.taskNext();
                }
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 59");
            e.printStackTrace();
        }
        try {
            long now = System.currentTimeMillis();
            if (specialSkill != null) {
                boolean isUpdate = false;
                for (Skill skill : skills) {
                    if (skill.isCooldown()) {
                        if ((specialSkill.id == 4 && skill.template.id == SkillName.TU_PHAT_NO)
                                || (specialSkill.id == 6 && skill.template.id == SkillName.HUYT_SAO)
                                || (specialSkill.id == 14 && skill.template.id == SkillName.QUA_CAU_KENH_KHI)
                                || (specialSkill.id == 23 && skill.template.id == SkillName.TRI_THUONG)
                                || (specialSkill.id == 24 && skill.template.id == SkillName.MAKANKOSAPPO)
                                || (specialSkill.id == 25 && skill.template.id == SkillName.DE_TRUNG)
                                || (specialSkill.id == 28 && skill.template.id == SkillName.KHIEN_NANG_LUONG)
                                || (specialSkill.id == 13 && skill.template.id == SkillName.THAI_DUONG_HA_SAN)) {
                            int p = (int) ((now - skill.lastTimeUseThisSkill) * 100 / skill.coolDown);
                            if (p + specialSkill.param >= 100) {
                                skill.lastTimeUseThisSkill = now - skill.coolDown;
                                isUpdate = true;
                            }
                            break;
                        }
                    }
                }
                if (isUpdate) {
                    service.updateCoolDown(skills);
                }
            }
            if (!isDead) {
                if (isHuman()) {
                    // if (zone != null && zone.map.checkBlock(this.x, this.y)) {
                    // info.recovery(Info.ALL, -10, true);
                    // if (info.hp <= 0) {
                    // startDie();
                    // }
                    // }
                }
                if (info.options[162] > 0) {
                    List<Player> list = zone.getListChar(Zone.TYPE_HUMAN, Zone.TYPE_PET);
                    if (list.size() > 1) {
                        for (Player _c : list) {
                            if (_c.isDead) {
                                continue;
                            }
                            int d = Utils.getDistance(this.x, this.y, _c.x, _c.y);
                            if (d < DISTANCE_EFFECT) {
                                _c.info.recovery(Info.MP, info.options[162], true);
                            }
                        }
                    }
                }
            }
            if (callDragon != null) {
                if (now - callDragon.time >= 300000) {
                    callDragon.close();
                }
            }
            try {
                this.timePlayed++;
                if (isMonkey) {
                    if (this.timeIsMoneky > 0) {
                        this.timeIsMoneky--;
                    }
                    if (this.timeIsMoneky == 0) {
                        this.timeOutIsMonkey();
                    }
                }
                if (invite != null) {
                    invite.update();
                }
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                System.err.println("Error at 58");
                e.printStackTrace();
                logger.error("update every one second - block 1");
            }
            try {
                boolean isFlag = false;
                if (this.zone != null && this.zone.map != null) {
                    if (this.x < 24) {
                        this.x = 24;
                        isFlag = true;
                    }
                    if (this.x > zone.map.width - 24) {
                        this.x = (short) (zone.map.width - 24);
                        isFlag = true;
                    }
                    if (this.y < 0) {
                        this.y = 24;
                        isFlag = true;
                    }
                    if (this.y > zone.map.height - 24) {
                        this.y = zone.map.collisionLand(x, (short) 24);
                        isFlag = true;
                    }
                    if (isFlag) {
                        if (zone.service != null) {
                            zone.service.setPosition(this, (byte) 0);
                        }
                    }
                }
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                System.err.println("Error at 57");
                e.printStackTrace();
                logger.error("update every one second - block 2");
            }
            try {
                try {
                    updateMessageTime();
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 56");
                    e.printStackTrace();
                }
                try {
                    updateAmulet();
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 55");
                    e.printStackTrace();
                }
                try {
                    updateTimeLiveMobMe();
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 54");
                    e.printStackTrace();
                }

                if (this.freezSeconds > 0) {
                    this.freezSeconds--;
                    if (this.freezSeconds == 0) {
                        this.isFreeze = false;
                    }
                }
                try {
                    updateItemTime();
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 53");
                    e.printStackTrace();
                }

            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                System.err.println("Error at 52");
                e.printStackTrace();
                logger.error("update every one second - block 3");
            }
            if (this.zone != null && this.zone.map != null && mapPhuHo != -1) {
                if (mapPhuHo == 113) {
                    if (this.zone.map.mapID != 113 && this.zone.map.mapID != MapName.DAU_TRUONG) {
                        mapPhuHo = percentPhuHo = -1;
                        service.sendThongBao("Bạn vừa mất phù hộ");
                        loadBody();
                    }
                } else if (this.mapPhuHo == 114) {
                    if (!this.zone.map.isBaseBabidi()) {
                        mapPhuHo = percentPhuHo = -1;
                        service.sendThongBao("Bạn vừa mất phù hộ");
                        loadBody();
                    }
                } else if (this.zone.map.mapID != mapPhuHo) {
                    mapPhuHo = percentPhuHo = -1;
                    service.sendThongBao("Bạn vừa mất phù hộ");
                    loadBody();
                }
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            e.printStackTrace();
            System.err.println("Error at 51");
            logger.error("updateEveryOneSeconds error", e.getCause());
        }
    }

    public MessageTime getMessageTime(byte id) {
        synchronized (messageTimes) {
            for (MessageTime ms : messageTimes) {
                if (ms.getId() == id) {
                    return ms;
                }
            }
        }
        return null;
    }

    public void setTimeForMessageTime(byte id, short time) {
        MessageTime ms = getMessageTime(id);
        if (ms != null) {
            ms.setTime(time);
            service.messageTime(ms);
        }
    }

    public void addMessageTime(MessageTime ms) {
        if (messageTimes != null) {
            synchronized (messageTimes) {
                MessageTime mss = getMessageTime(ms.getId());
                if (mss != null) {
                    mss.setId(ms.getId());
                    mss.setText(ms.getText());
                    mss.setTime(ms.getTime());
                } else {
                    messageTimes.add(ms);
                }
                service.messageTime(ms);
            }
        }
    }

    public void updateMessageTime() {
        if (messageTimes != null) {
            synchronized (messageTimes) {
                ArrayList<MessageTime> removes = new ArrayList<>();
                for (MessageTime ms : messageTimes) {
                    ms.update();
                    if (ms.getTime() <= 0) {
                        removes.add(ms);
                    }
                }
                messageTimes.removeAll(removes);
            }
        }

    }

    public void updateTimeLiveMobMe() {
        if (this.mobMe != null) {
            if (this.mobMe.timeLive > 0) {
                if (!isSetPikkoroDaimao()) {
                    this.mobMe.timeLive--;
                }

            }
            if (this.mobMe.timeLive == 0) {
                zone.service.mobMeUpdate(this, null, -1, (byte) -1, (byte) 7);
                this.mobMe = null;
            }
        }
    }

    public void updateEveryHalfSeconds() {
        try {
            long now = System.currentTimeMillis();
            if (isTrading) {
                if (now - lastTimeTrade >= 300000L) {
                    clearTrade();
                }
            }
            if (numCheck >= 5) {
                isMod = true;
            }
            updateAmbientEffect();
            if (isRecoveryEnergy) {
                if (now - lastUseRecoveryEnery >= 10000) {
                    stopRecoveryEnery();
                }
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            e.printStackTrace();

            System.err.println("Error at 50");
            logger.error("updateEveryHalfSeconds error", e);
        }
    }

    public void updateEveryFiveSeconds() {
        try {
            if (!isDead) {
                try {
                    if (zone == null || zone.service == null) {
                        if (zone == null) {
                            logger.debug("zone is null");
                        }
                        if (zone.service == null) {
                            logger.debug("service is null");
                        }
                    }
                    List<Player> list = zone.getListChar(Zone.TYPE_HUMAN, Zone.TYPE_PET);
                    if (list.size() > 1) {
                        String[] chats2 = {"Tránh ra đi Xinbatô ơi", "Phân tâm quá", "Nực quá", "Bực bội quá",
                            "Im đi ông Xinbatô ơi"};
                        if (info.options[8] > 0) {
                            info.recovery(Info.ALL, info.options[8], true);
                        }
                        for (Player _c : list) {
                            if (_c.isDead) {
                                continue;
                            }

                            int d = Utils.getDistance(this.x, this.y, _c.x, _c.y);
                            if (d < DISTANCE_EFFECT) {
                                boolean isUpdate = false;
                                if (_c != this) {
                                    if (info.options[8] > 0) {
                                        _c.info.hp -= Utils.percentOf(_c.info.hpFull, info.options[8]);
                                        _c.info.mp -= Utils.percentOf(_c.info.mpFull, info.options[8]);
                                        if (_c.info.hp <= 0) {
                                            _c.info.hp = 1;
                                        }
                                        if (_c.info.mp <= 0) {
                                            _c.info.mp = 1;
                                        }
                                        _c.service.loadPoint();
                                        zone.service.playerLoadBody(_c);
                                    }
                                    if (isHaveEquipXinbato) {
                                        if (!_c.isAnDanh) {
                                            AmbientEffect am = new AmbientEffect(111, info.options[111], 5000);
                                            if (_c.addAmbientEffect(am)) {
                                                isUpdate = true;
                                            }
                                            zone.service.chat(_c, chats2[Utils.nextInt(chats2.length)]);
                                        }
                                    } else if (isHaveEquipBuiBui) {
                                        AmbientEffect am = new AmbientEffect(24, -95, 5000);
                                        if (_c.addAmbientEffect(am)) {
                                            isUpdate = true;
                                        }
                                        zone.service.chat(_c, "Nặng quá");
                                    } else if (info.options[162] > 0) {
                                        zone.service.chat(_c, "Cute");
                                    }
                                }
                                if (isHaveEquipBulma || isHaveEquipMiNuong) {
                                    AmbientEffect am = new AmbientEffect(117, info.options[117], 5000);
                                    if (_c.addAmbientEffect(am)) {
                                        isUpdate = true;
                                    }
                                    String chat;
                                    if (isHaveEquipMiNuong) {
                                        chat = "Bắn tim...biu biu";
                                    } else {
                                        chat = "Wow, Sexy quá";
                                    }
                                    if (_c != this) {
                                        zone.service.chat(_c, chat);
                                    }
                                }
                                if (isUpdate) {
                                    _c.info.setInfo();
                                    _c.service.loadPoint();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    e.printStackTrace();
                    System.err.println("Error at 49");
                    logger.error("update every five seconds - block 1");
                }
                try {
                    if (isHaveEquipInvisible) {
                        isInvisible = true;
                        zone.service.playerLoadAll(this);
                        long delay = 1000;
                        Utils.setTimeout(() -> {
                            if (zone != null) {
                                isInvisible = false;
                                zone.service.playerLoadAll(this);
                            }
                        }, delay);
                    }
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    e.printStackTrace();

                    System.err.println("Error at 48");
                    logger.error("update every five seconds - block 2");
                }
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            e.printStackTrace();

            System.err.println("Error at 47");
            logger.error("updateEveryFiveSeconds error", e.getCause());
        }
    }

    public void updateEveryThirtySeconds() {
        try {
            if (!isDead) {
                info.recovery(Info.HP, info.options[80], true);
                info.recovery(Info.HP, info.options[27]);
                info.recovery(Info.MP, info.options[81], true);
                info.recovery(Info.MP, info.options[28]);
                if (isHaveEquipTransformIntoChocolate || isHaveEquipTransformIntoStone) {
                    List<Player> list = zone.getListChar(Zone.TYPE_HUMAN, Zone.TYPE_PET);
                    if (list.size() > 1) {
                        if (isHaveEquipTransformIntoStone) {
                            zone.service.chat(this, "Phẹt");
                        }
                        for (Player _c : list) {
                            if (_c.isDead || _c == this) {
                                continue;
                            }
                            int d = Utils.getDistance(this.x, this.y, _c.x, _c.y);
                            if (d < DISTANCE_EFFECT) {
                                if (isHaveEquipTransformIntoChocolate) {
                                    _c.transformIntoChocolate(0, 5);
                                }
                                if (isHaveEquipTransformIntoStone) {
                                    int time = 5;
                                    // if (this instanceof Drabura) {
                                    // time = 20;
                                    // _c.info.recovery(Info.HP, -_c.info.hp / 2);
                                    // _c.info.recovery(Info.MP, -_c.info.mp / 2);
                                    // }
                                    _c.transformIntoStone(time);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 46");
            logger.error("updateEveryThirtySeconds error", e);
        }
    }

    int tick = 0;

    public void updateEveryOneMinutes() {
        try {
            if (this.getSession() != null && this.getSession().user != null) {
                tick++;
                if (tick >= 5) {
                    this.saveData();
                    tick = 0;
                }
            }
            if (!isDead) {
                boolean isMacGiapLuyenTap = false;
                if (itemBody != null) {
                    for (Item item : this.itemBody) {
                        if (item != null) {
                            if (item.template.type == 32) {
                                ItemOption option = null;
                                for (ItemOption o : item.options) {
                                    if (o.optionTemplate.id == 9) {
                                        option = o;
                                        break;
                                    }
                                }
                                if (option != null) {
                                    option.param++;
                                    switch (item.id) {
                                        case 529:
                                        case 534:
                                            if (option.param > 100) {
                                                option.param = 100;
                                            }
                                            break;

                                        case 530:
                                        case 535:
                                            if (option.param > 1000) {
                                                option.param = 1000;
                                            }
                                            break;

                                        case 531:
                                        case 536:
                                        case 2268:
                                            if (option.param > 10000) {
                                                option.param = 10000;
                                            }
                                            break;

                                    }
                                    service.refreshItem((byte) 0, item);
                                    isMacGiapLuyenTap = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!isMacGiapLuyenTap) {
                    if (itemBag != null) {
                        for (Item item : this.itemBag) {
                            if (item != null) {
                                if (item.template.type == 32) {
                                    ItemOption option = null;
                                    for (ItemOption o : item.options) {
                                        if (o.optionTemplate.id == 9) {
                                            option = o;
                                            break;
                                        }
                                    }
                                    if (option != null) {
                                        option.param--;
                                        if (option.param < 0) {
                                            option.param = 0;
                                        }
                                        service.refreshItem((byte) 1, item);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 45");
            logger.error("updateEveryOneMinutes error", e);
        }
    }

    public void update() {

        long now = System.currentTimeMillis();
        try {
            if (now - lastUpdates[UPDATE_HALF_SECONDS] >= 500) {// update 500ms
                lastUpdates[UPDATE_HALF_SECONDS] = now;
                updateEveryHalfSeconds();
            }
            if (now - lastUpdates[UPDATE_ONE_SECONDS] >= 1000) {// update 1s
                lastUpdates[UPDATE_ONE_SECONDS] = now;
                updateEveryOneSeconds();
            }
            if (now - lastUpdates[UPDATE_FIVE_SECONDS] >= 5000) {// update 60s
                lastUpdates[UPDATE_FIVE_SECONDS] = now;
                updateEveryFiveSeconds();
            }
            if (now - lastUpdates[UPDATE_THIRTY_SECONDS] >= 30000) {// update 30s
                lastUpdates[UPDATE_THIRTY_SECONDS] = now;
                updateEveryThirtySeconds();
            }
            if (now - lastUpdates[UPDATE_ONE_MINUTES] >= 60000) {// update 60s
                lastUpdates[UPDATE_ONE_MINUTES] = now;
                updateEveryOneMinutes();
            }
            if (now - lastUpdates[UPDATE_TEN_SECONDS] >= 10000) {// update 10s
                lastUpdates[UPDATE_TEN_SECONDS] = now;
                updateEveryTenSeconds();
            }
            updateResetDay(now);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            e.printStackTrace();
            System.err.println("Error at 44");
            logger.error("update error player - " + this.name + " -\n" + e.toString());
        }
    }

    private void updateResetDay(long now) {
        if (this instanceof VirtualBot || this instanceof BotCold) {
            return;
        }
        if (!isHuman()) {
            return;
        }
        if (resetTime == null) {
            resetTime = new Timestamp(now);
            return;
        }
        int day = Utils.getCountDay(resetTime);
        if (day <= 0) {
            return;
        }
        resetTime = new Timestamp(now);
        if (info.activePoint > 0) {
            info.activePoint--;
            service.updateActivePoint();
        }
        boolean isUpdate = false;
        for (int i = 0; i < itemBag.length; i++) {
            if (itemBag[i] != null) {
                for (ItemOption option : itemBag[i].options) {
                    if (option.optionTemplate.id == 93) {
                        option.param -= day;
                        if (option.param <= 0) {
                            itemBag[i] = null;
                        }
                        isUpdate = true;
                        break;
                    }
                }
            }
        }
        if (isUpdate) {
            service.setItemBag();
        }
        isUpdate = false;
        for (int i = 0; i < itemBox.length; i++) {
            if (itemBox[i] != null) {
                for (ItemOption option : itemBox[i].options) {
                    if (option.optionTemplate.id == 93) {
                        option.param -= day;
                        if (option.param <= 0) {
                            itemBox[i] = null;
                        }
                        isUpdate = true;
                        break;
                    }
                }
            }
        }
        if (isUpdate) {
            service.setItemBox();
        }
        isUpdate = false;
        for (int i = 0; i < itemBody.length; i++) {
            if (itemBody[i] != null) {
                for (ItemOption option : itemBody[i].options) {
                    if (option.optionTemplate.id == 93) {
                        option.param -= day;
                        if (option.param <= 0) {
                            itemBody[i] = null;
                        }
                        isUpdate = true;
                        break;
                    }
                }
            }
        }
        if (isUpdate) {
            updateSkin();
            info.setInfo();
            service.setItemBody();
            service.loadPoint();
        }
        if (myDisciple != null) {
            for (int i = 0; i < myDisciple.itemBody.length; i++) {
                if (myDisciple.itemBody[i] != null) {
                    for (ItemOption option : myDisciple.itemBody[i].options) {
                        if (option.optionTemplate.id == 93) {
                            option.param -= day;
                            if (option.param <= 0) {
                                myDisciple.itemBody[i] = null;
                            }
                            break;
                        }
                    }
                }
            }
        }

    }

    public void updateEveryTenSeconds() {
        try {
            if (!isDead) {
                String[] chats = {"Thúi quá", "Biến đi", "Mùi gì hôi quá", "Hôi quá, tránh xa ta ra",
                    "Trời ơi, đồ ở dơ"};
                if (info.options[109] > 0) {
                    List<Player> list = zone.getListChar(Zone.TYPE_HUMAN, Zone.TYPE_PET);
                    if (list.size() > 1) {
                        for (Player _c : list) {
                            if (_c.isDead || _c == this || _c.isAnDanh) {
                                continue;
                            }
                            int d = Utils.getDistance(this.x, this.y, _c.x, _c.y);
                            if (d < DISTANCE_EFFECT) {
                                _c.info.hp -= Utils.percentOf(_c.info.hpFull, info.options[109]);
                                if (_c.info.hp <= 0) {
                                    _c.info.hp = 1;
                                }
                                zone.service.chat(_c, chats[Utils.nextInt(chats.length)]);
                                _c.service.loadPoint();
                                zone.service.playerLoadBody(_c);
                            }

                        }
                    }
                }
            }
            if (isChangePoint && false) {
                if (this instanceof VirtualBot || this.getSession() == null) {
                    return;
                }
                Optional<SuKienTetData> resp = GameRepository.getInstance().eventTet.findFirstByName(this.id);
                if (resp.isPresent()) {
                } else {
                    try {
                        SuKienTetData data = new SuKienTetData();
                        data.setPlayerId(id);
                        data.setPoint(getRewardEventTet());
                        data.setCreateDate(Instant.now());
                        data.setModifyDate(Instant.now());
                        GameRepository.getInstance().eventTet.save(data);
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);
                        System.err.println("Error at 43");
                        logger.error("Save error EventTet " + this.name);
                    }
                }
                try {
                    GameRepository.getInstance().eventTet.setReward(this.id, this.getRewardEventTet());
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    System.err.println("Error at 42");
                    e.printStackTrace();
                }
                isChangePoint = false;

            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 41");
            logger.error("updateEveryTenSeconds error", e);
        }
    }

    public void setItemTime(byte itemTimeId, int iconId, boolean isSave, int second) {
        ItemTime itemTime = new ItemTime(itemTimeId, iconId, second, isSave);
        addItemTime(itemTime);
        info.setInfo();
        service.loadPoint();
        if (zone != null) {
            zone.service.playerLoadBody(this);
        }
    }

    public boolean exitsItemTime(byte itemTimeId) {
        if (itemTimes == null) {
            return false;
        }

        synchronized (this.itemTimes) {
            return this.itemTimes.stream()
                    .anyMatch(item -> item.id == itemTimeId && item.seconds > 0);
        }
    }

    public int lastSizeItemTime;

    public void updateItemTime() {
        String excep = "";
        try {
            if (itemTimes != null) {
                if (itemTimes.size() != lastSizeItemTime) {
                    lastSizeItemTime = itemTimes.size();
                    info.setInfo();
                    service.loadPoint();
                }
                synchronized (this.itemTimes) {
                    ArrayList<ItemTime> listRemove = new ArrayList<>();
                    for (ItemTime item : this.itemTimes) {
                        if (item == null) {
                            continue;
                        }
                        try {
                            excep = "item.update";
                            item.update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (item.seconds <= 0) {
                            excep = "item.id = " + item.id;
                            switch (item.id) {
                                case ItemTimeName.KHIEN_NANG_LUONG:
                                    this.isProtected = false;
                                    if (zone != null) {
                                        zone.service.setEffect(null, this.id, Skill.REMOVE_EFFECT, Skill.CHARACTER,
                                                (byte) 33);
                                    }
                                    break;

                                case ItemTimeName.HUYT_SAO:
                                    this.isHuytSao = false;
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.setEffect(null, this.id, Skill.REMOVE_EFFECT, Skill.CHARACTER,
                                                (byte) 39);
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;

                                case ItemTimeName.HOP_THE:
                                    fusion((byte) 1);
                                    timeAtSplitFusion = System.currentTimeMillis();
                                    break;

                                case ItemTimeName.THOI_MIEN:
                                    this.isSleep = false;
                                    if (zone != null) {
                                        zone.service.setEffect(null, this.id, Skill.REMOVE_EFFECT, Skill.CHARACTER,
                                                (byte) 41);
                                    }
                                    break;

                                case ItemTimeName.DICH_CHUYEN_TUC_THOI:
                                    this.isBlind = false;
                                    if (zone != null) {
                                        zone.service.setEffect(null, this.id, Skill.REMOVE_EFFECT, Skill.CHARACTER,
                                                (byte) 40);
                                    }
                                    break;

                                case ItemTimeName.SOCOLA:
                                    this.isChocolate = false;
                                    updateSkin();
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                        zone.service.updateBody((byte) 0, this);
                                    }
                                    break;

                                case ItemTimeName.MO_GIOI_HAN_SUC_MANH:
                                    info.numberOpenLimitedPower++;
                                    PowerLimitMark limit = PowerLimitMark.limitMark.get(info.numberOpenLimitedPower);
                                    info.powerLimitMark = limit;
                                    service.sendThongBao(String.format("Giới hạn sức mạnh đã năng lên %s",
                                            Utils.formatNumber(limit.power + 1)));
                                    break;

                                case ItemTimeName.CUONG_NO:
                                    setCuongNo(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;

                                case ItemTimeName.BO_HUYET:
                                    setBoHuyet(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;

                                case ItemTimeName.BO_KHI:
                                    setBoKhi(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.CUONG_NO_2:
                                    setCuongNo2(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;

                                case ItemTimeName.BO_HUYET_2:
                                    setBoHuyet2(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;

                                case ItemTimeName.BO_KHI_2:
                                    setBoKhi2(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.GIAP_XEN_BO_HUNG:
                                    setGiapXen(false);
                                    break;

                                case ItemTimeName.AN_DANH:
                                    setAnDanh(false);
                                    break;

                                case ItemTimeName.TU_DONG_LUYEN_TAP:
                                    isAutoPlay = false;
                                    service.sendThongBao("Tự động đánh quái đã tắt");
                                    service.autoPlay(false);
                                    break;

                                case ItemTimeName.MAY_DO_CAPSULE_KI_BI:
                                    setMayDo(false);
                                    break;

                                case ItemTimeName.STONE:
                                    this.isStone = false;
                                    updateSkin();
                                    service.setEffect(null, this.id, Skill.REMOVE_EFFECT, Skill.CHARACTER, (byte) 42);
                                    if (zone != null) {
                                        zone.service.updateBody((byte) 0, this);
                                    }
                                    break;

                                case ItemTimeName.DUOI_KHI:
                                    setDuoiKhi(false);
                                    break;

                                case ItemTimeName.BANH_PUDDING:
                                    setPudding(false);
                                    break;

                                case ItemTimeName.XUC_XICH:
                                    setXucXich(false);
                                    break;

                                case ItemTimeName.KEM_DAU:
                                    setKemDau(false);
                                    break;

                                case ItemTimeName.MI_LY:
                                    setMiLy(false);
                                    break;

                                case ItemTimeName.SUSHI:
                                    setSushi(false);
                                    break;
                                case ItemTimeName.PHIEU_X2_TNSM:
                                    setPhieuX2TNSM(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.DU_DU:
                                    setDudu(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.DUA_XANH:
                                    setDuaXanh(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.MAM_TRAI_CAY:
                                    setMamTraiCay(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.MANG_CAU:
                                    setMangCau(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.XOAI_CHIN:
                                    setXoai(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.TRAI_SUNG:
                                    setTraiSung(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_THIEN_MENH_1:
                                    setUocThienMenh1(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_THIEN_MENH_2:
                                    setUocThienMenh2(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_THIEN_MENH_4:
                                    setUocThienMenh4(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_THIEN_MENH_5:
                                    setUocThienMenh5(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_LOC_PHAT_1:
                                    setUocLocPhat1(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_LOC_PHAT_2:
                                    setUocLocPhat2(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_LOC_PHAT_3:
                                    setUocLocPhat3(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.UOC_LOC_PHAT_7:
                                    setUocLocPhat7(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.HAT_DAU_VANG:
                                    setDauVang(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.CA_NOI_GIAN:
                                    setCaNoiGian(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.NUOC_TANG_CUONG:
                                    setNuocTangCuong(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                                case ItemTimeName.QUA_HONG_DAO:
                                    setQuaHongDao(false);
                                    info.setInfo();
                                    service.loadPoint();
                                    if (zone != null) {
                                        zone.service.playerLoadBody(this);
                                    }
                                    break;
                            }
                            listRemove.add(item);
                        }
                    }
                    this.itemTimes.removeAll(listRemove);
                }
            }
            excep = "sachdacbiet";
            SachDacBiet();

        } catch (Exception e) {
            UtilsNQMP.logError(e);
            e.printStackTrace();
            System.err.println("Error at updateitemTime  - player : " + this.name + "at error : " + excep);
        }
    }

    public boolean isHaveFood() {
        return isPudding() || isXucXich() || isKemDau() || isMiLy() || isSushi();
    }

    public void addExp(byte type, long exp, boolean canX2, boolean isAddForMember) {
        if (info.power >= info.powerLimitMark.power) {
            return;
        }

        if (info.power + exp >= info.powerLimitMark.power) {
            exp = info.powerLimitMark.power - info.power;
        }
        long exp2 = 0;
        if (canX2) {
            Server server = DragonBall.getInstance().getServer();
            Config config = server.getConfig();
            exp *= config.getExp();
            if (isDuoiKhi) {
                exp *= 2;
            }
            int mul = 1;
            if (isBuaTriTue4) {
                if (isBuaTriTue) {
                    mul = 6;
                } else {
                    mul = 4;
                }
            } else if (isBuaTriTue3) {
                if (isBuaTriTue) {
                    mul = 5;
                } else {
                    mul = 3;
                }
            } else if (isBuaTriTue) {
                mul = 2;
            }
            exp *= mul;
        }
        if (exp <= 0) {
            return;
        }
        if (isDisciple()) {
            Disciple disciple = (Disciple) this;
            exp2 = exp;
            if (disciple.master != null) {
                if (disciple.master.isRewardTNSMDragonNamek) {
                    exp += Utils.percentOf(exp, 10);
                }
                if (disciple.master.isPhieuX2TNSM) {
                    exp += Utils.percentOf(exp, 20);
                }
                if (disciple.master.sachdacbiet[0]) {
                    exp += Utils.percentOf(exp, 100);
                }
                if (disciple.master.sachdacbiet[1]) {
                    exp += Utils.percentOf(exp, 20);
                }
                if (disciple.master.isXoai) {
                    exp += Utils.percentOf(exp, 20);
                }
                if (disciple.master.isTraiSung) {
                    exp += Utils.percentOf(exp, 30);
                }
                if (disciple.master.exitsItemTime(ItemTimeName.KEO_DEO)) {
                    exp += Utils.percentOf(exp, 30);
                }
                if (disciple.master.exitsItemTime(ItemTimeName.DA_MA_THUAT_SELECT4)) {
                    exp += Utils.percentOf(exp, 100);
                }
                if (disciple.master.exitsItemTime(ItemTimeName.DAU_THAN_KY)) {
                    exp += Utils.percentOf(exp, 100);
                }
            }
            if (disciple.master.isUocThienMenh1) {
                exp *= 2;
            }
            if (disciple.master.isQuaHongDao) {
                exp *= 2;
            }
        } else {
            if (exitsItemTime(ItemTimeName.DA_MA_THUAT_SELECT4)) {
                exp += Utils.percentOf(exp, 100);
            }
            if (exitsItemTime(ItemTimeName.KEM_TRAI_CAY)) {
                exp += Utils.percentOf(exp, 100);
            }
            if (exitsItemTime(ItemTimeName.NUOC_MA_THUAT)) {
                exp += Utils.percentOf(exp, 100);
            }
            if (exitsItemTime(ItemTimeName.NUOC_GIAI_KHAT)) {
                exp += Utils.percentOf(exp, 20);
            }
            if (exitsItemTime(ItemTimeName.BANH_SU_KEM)) {
                exp += Utils.percentOf(exp, 30);
            }
            if (exitsItemTime(ItemTimeName.BANH_KEM_NHO)) {
                exp += Utils.percentOf(exp, 15);
            }
            if (isRewardTNSMDragonNamek) {
                exp += Utils.percentOf(exp, 10);
            }
            if (sachdacbiet[0]) {
                exp += Utils.percentOf(exp, 100);
            }
            if (sachdacbiet[1]) {
                exp += Utils.percentOf(exp, 20);
            }
            if (isPhieuX2TNSM) {
                exp += Utils.percentOf(exp, 20);
            }
            if (isMamTraiCay) {
                exp += Utils.percentOf(exp, 20);
            }
            if (isUocThienMenh1) {
                exp *= 2;
            }
            if (isUocLocPhat1) {
                exp *= 2;
            }
            if (baiSu_id != -1) {
                Player _baisu = SessionManager.findChar(baiSu_id);
                if (_baisu != null && _baisu.zone == this.zone
                        && info.power < 100_000_000_000L
                        && _baisu.info.power < 100_000_000_000L) {
                    exp *= 2;
                }
            }
        }
        exp /= 5;
        exp = Zone.callEXP(this, exp);
        if (zone.map.isMapDeTu() || zone.map.isMapPorata2()) {
            return;
        }
        if (isDisciple()) {
            Disciple disciple = (Disciple) this;
            disciple.master.addExp(type, exp2 / 2, false, isAddForMember);
        } else if (isAddForMember) {
            if (clan != null) {
                exp = exp * 90 / 100;
                List<Player> list = zone.getMemberSameClan(this);
                if (list.size() - 1 > 0) {
                    clan.powerPoint += exp;
                    for (Player _c : list) {
                        int d = Math.abs(info.level - _c.info.level);
                        exp -= exp * (d * 5L) / 100;
                        exp /= 10;
                        _c.addExp(Info.POTENTIAL, exp, false, false);
                    }
                }
            }
        }
        long prePower = info.power;
        info.addPowerOrPotential(type, exp);
        Top topPower = Top.getTop(Top.TOP_POWER);
        if (topPower != null) {
            if (info.power > topPower.getLowestScore()) {
                TopInfo in = topPower.getTopInfo(this.id);
                if (in != null) {
                    in.score = info.power;
                    in.head = this.head;
                    in.body = this.body;
                    in.leg = this.leg;
                    in.info = String.format("Sức mạnh: %s", Utils.currencyFormat(info.power));
                } else {
                    in = new TopInfo();
                    in.playerID = this.id;
                    in.name = this.name;
                    in.score = info.power;
                    in.head = this.head;
                    in.body = this.body;
                    in.leg = this.leg;
                    in.info = String.format("Sức mạnh: %s", Utils.currencyFormat(info.power));
                    in.info2 = "";
                    topPower.addTopInfo(in);
                }
                topPower.updateLowestScore();
            }
        }
        if (!isDisciple()) {
            if (prePower < 1500000 && info.power >= 1500000) {
                setListAccessMap();
            }
        }
        if (taskMain != null) {
            if (taskMain.id == 7 && taskMain.index == 0) {
                if (info.power >= 16000) {
                    taskNext();
                }
            }
            if (taskMain.id == 8 && taskMain.index == 0) {
                if (info.power >= 40000) {
                    taskNext();
                }
            }
            if (taskMain.id == 14 && taskMain.index == 0) {
                if (info.power >= 200000) {
                    taskNext();
                }
            }
            if (taskMain.id == 15 && taskMain.index == 0) {
                if (info.power >= 500000) {
                    taskNext();
                }
            }
            if (taskMain.id == 20 && taskMain.index == 0) {
                if (info.power >= 600000000) {
                    taskNext();
                }
            }
            if (taskMain.id == 21 && taskMain.index == 0) {
                if (info.power >= 2000000000) {
                    taskNext();
                }
            }
        }
    }

    public void achievement(Message ms) {
        try {
            if (zone.map.mapID != 47 && zone.map.mapID != 84) {
                return;
            }
            byte index = ms.reader().readByte();
            if (index < 0 || index >= achievements.size()) {
                return;
            }
            Achievement achive = this.achievements.get(index);
            if (achive.isFinish() && !achive.isRewarded()) {
                int reward = achive.getReward();
                achive.setIsRewarded(true);
                addDiamondLock(reward);
                service.achievement((byte) 1, index);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 40");
            logger.error("failed!", ex);
        }
    }

    public void petStatus(Message ms) {
        try {
            byte status = ms.reader().readByte();
            petStatus(status);
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 39");
            logger.error("failed!", ex);
        }
    }

    public void petStatus(byte status) {
        if (myDisciple != null) {
            myDisciple.discipleStatus = status;
            if (isNhapThe) {
                service.sendThongBao("Không thể thực hiện");
                return;
            }
            switch (status) {
                case 0:
                    service.chat(myDisciple, "Ok con theo sư phụ");
                    break;

                case 1:
                    service.chat(myDisciple, "Ok con sẽ bảo vệ sư phụ");
                    break;

                case 2:
                    service.chat(myDisciple, "Ok sư phụ để con lo cho");
                    break;

                case 3:
                    if (myDisciple.zone != null) {
                        service.chat(myDisciple, "Ok con về, bibi sư phụ");
                        if (!myDisciple.isDead()) {
                            myDisciple.clearEffect();
                            if (myDisciple.isMonkey()) {
                                myDisciple.timeOutIsMonkey();
                            }
                            Utils.setTimeout(() -> {
                                if (myDisciple.zone != null) {
                                    zone.leave(myDisciple);
                                }
                            }, 2000);
                        }
                    }
                    break;

                case 4:
                    myDisciple.discipleStatus = 3;
                    if (zone.map.isDauTruong() || isDead() || myDisciple.isDead()) {
                        service.sendThongBao("Không thể thực hiện");
                        return;
                    }
                    long now = System.currentTimeMillis();
                    if (now - timeAtSplitFusion >= 600000) {
                        typePorata = 0;
                        fusion((byte) 4);
                    } else {
                        long timeAgo = 600000 - (now - timeAtSplitFusion);
                        service.sendThongBao(
                                String.format("Chỉ có thể thực hiện sau %s", Utils.timeAgo((int) (timeAgo / 1000))));
                    }
                    break;

                case 5:
                    service.sendThongBao("Không thể thực hiện");
                    break;
            }
            if (myDisciple != null && myDisciple.isDead()) {
                service.chat(myDisciple, "Sư phụ ơi cho con đậu thần");
            }
        }
    }

    public void fusion(byte type) {
        if (type != 5) {
            this.fusionType = type;
        }
        if (type == 4 || type == 6) {// hợp thể
            if (!isNhapThe) {
                myDisciple.clearEffect();
                if (myDisciple.isMonkey()) {
                    myDisciple.timeOutIsMonkey();
                }
                if (zone != null) {
                    zone.leave(myDisciple);
                }
                myDisciple.discipleStatus = 3;
                this.isNhapThe = true;
                if (type == 4) {
                    ItemTime item = new ItemTime(ItemTimeName.HOP_THE, gender == 1 ? 3901 : 3790, 600, true);
                    addItemTime(item);
                }
            }

        } else if (type == 1) {// tách hợp thể
            if (isNhapThe) {
                this.isNhapThe = false;
                myDisciple.discipleStatus = 1;
                if (zone != null && !zone.map.isMapSingle()) {
                    myDisciple.followMaster();
                    zone.enter(myDisciple);
                }
            }
        }
        lastTimeUsePorata = System.currentTimeMillis();
        updateSkin();
        if (myDisciple != null) {
            myDisciple.info.setInfo();
        }
        info.setInfo();
        service.loadPoint();
        zone.service.playerLoadBody(this);
        zone.service.updateBody((byte) 0, this);
        if (isNhapThe) {
            info.recovery(Info.ALL, 100, true);
        }
        zone.service.fusion(this, type);
    }

    public void createClan(Message ms) {
        try {
            if (taskMain.id < 12) {
                return;
            }
            byte action = ms.reader().readByte();
            if (action == 2) {
                if (this.clan != null) {
                    return;
                }
                int imgID = ms.reader().readUnsignedByte();
                String name = ms.reader().readUTF();
                if (name.isEmpty()) {
                    service.sendThongBao("Tên không được bỏ trống");
                    return;
                }
                if (name.length() > 30) {
                    service.sendThongBao("Tên bang hội tối đa 30 ký tự");
                    return;
                }
                Server server = DragonBall.getInstance().getServer();
                ClanImage clanImage = server.getClanImageByID(imgID);
                if (clanImage != null && clanImage.isSale) {
                    int gold = clanImage.gold;
                    int gem = clanImage.gem;
                    if (gold > 0) {
                        if (gold > getGold()) {
                            service.sendThongBao("Bạn không đủ vàng để thực hiện.");
                            return;
                        }
                    }
                    if (gem > 0) {
                        if (gem > getTotalGem()) {
                            service.sendThongBao("Bạn không đủ ngọc để thực hiện.");
                            return;
                        }
                    }
                    if (gold > 0) {
                        addGold(-gold);
                    }
                    if (gem > 0) {
                        subDiamond(gem);
                    }
                    ClanData clanData = new ClanData(name, clanImage.id, this);
                    GameRepository.getInstance().clan.save(clanData);
                    ClanMemberData clanMemberData = new ClanMemberData(clanData.id, this, (byte) 0);
                    GameRepository.getInstance().clanMember.save(clanMemberData);
                    Clan clan = new Clan(clanData);
                    ClanManager.getInstance().addClan(clan);
                    clan.addMember(new ClanMember(clanMemberData));
                    this.clanID = clan.id;
                    this.clan = clan;
                    updateBag();
                    service.clanInfo();
                } else {
                    service.sendThongBao("Có lỗi xảy ra");
                }
            }
            if (action == 4) {
                if (clan == null) {
                    return;
                }
                int imgID = ms.reader().readUnsignedByte();
                String text = ms.reader().readUTF();
                int role = clan.getMember(this.id).role;
                if (!text.isEmpty()) {
                    if (role == 0 || role == 1) {
                        clan.slogan = text;
                    }
                } else {
                    if (role == 0) {
                        if (imgID == clan.imgID) {
                            return;
                        }
                        Server server = DragonBall.getInstance().getServer();
                        ClanImage clanImage = server.getClanImageByID(imgID);
                        if (clanImage != null && clanImage.isSale) {
                            int gold = clanImage.gold;
                            int gem = clanImage.gem;
                            if (gold > 0) {
                                if (gold > getGold()) {
                                    service.sendThongBao("Bạn không đủ vàng để thực hiện.");
                                    return;
                                }
                            }
                            if (gem > 0) {
                                if (gem > getTotalGem()) {
                                    service.sendThongBao("Bạn không đủ ngọc để thực hiện.");
                                    return;
                                }
                            }
                            if (gold > 0) {
                                addGold(-gold);
                            }
                            if (gem > 0) {
                                subDiamond(gem);
                            }
                            clan.imgID = (byte) imgID;
                            clan.updateBag();
                        }
                    }
                }
            }
            service.createClanInfo(action);
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 38");
            logger.error("failed!", ex);
        }
    }

    public void searchClan(Message ms) {
        try {
            if (taskMain == null || taskMain.id < 12) {
                return;
            }
            service.search(ClanManager.getInstance().search(ms.reader().readUTF()));
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 37");
            logger.error("failed!", ex);
        }

    }

    public void joinClan(Message ms) {
        try {
            if (taskMain.id < 12) {
                return;
            }
            if (this.clan == null) {
                return;
            }
            if (clan.getMember(this.id).role != 0) {
                return;
            }
            int id = ms.reader().readInt();
            byte action = ms.reader().readByte();
            ClanMessage message = this.clan.getMessage(id);
            if (message == null) {
                return;
            }
            if (action == 0) {
                if (clan.getNumberMember() >= clan.maxMember) {
                    service.sendThongBao("Thành viên đã đạt tối đa");
                    return;
                }
                if (message.type != 2) {
                    return;
                }
                String name = message.playerName;
                int playerId = message.playerId;
                Player _player = SessionManager.findChar(playerId);
                if (_player == null) {
                    service.sendThongBao("Người chơi này đang offline");
                    return;
                }
                if (_player.clan != null) {
                    service.sendThongBao("Người chơi này đã ở bang hội khác");
                    return;
                }

                ClanMemberData clanMemberData = new ClanMemberData(clan.id, _player, (byte) 2);
                GameRepository.getInstance().clanMember.save(clanMemberData);

                _player.clanID = clan.id;
                _player.clan = clan;
                ClanMember clanMember = new ClanMember(clanMemberData);
                clan.addMember(clanMember);
                clan.clanUpdate((byte) 0, clanMember, -1);

                message.role = clan.getMember(this.id).role;
                message.type = 0;
                message.chat = "Chấp nhận " + name + " vào bang";
                message.color = 1;
                message.playerId = this.id;
                message.playerName = this.name;
                message.isNewMessage = false;
                clan.addMessage(message);
                service.clanInfo();
                _player.service.clanInfo();
                zone.service.playerLoadAll(_player);
                _player.updateBag();
                _player.service.sendThongBao("Bạn đã tham gia bang hội " + clan.name);

            }
            if (action == 1) {
                message.role = clan.getMember(this.id).role;
                message.type = 0;
                message.chat = "Từ chối " + message.playerName + " vào bang";
                message.color = 1;
                message.playerId = this.id;
                message.playerName = this.name;
                message.isNewMessage = false;
                clan.addMessage(message);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 36");
            logger.error("failed!", ex);
        }
    }

    public void clanDonate(Message ms) {
        try {
            if (taskMain.id < 12) {
                return;
            }
            if (clan == null) {
                return;
            }
            int id = ms.reader().readInt();
            ClanMessage message = clan.getMessage(id);
            if (message != null) {
                if (message.receive < message.maxCap) {
                    int index = getIndexItemBoxByType((byte) 6);
                    if (index != -1) {
                        Item item = this.itemBox[index];
                        if (item == null) {
                            return;
                        }
                        item = new Item(item.id);
                        item.setDefaultOptions();
                        item.quantity = 1;
                        ClanMember clanMember = clan.getMember(this.id);
                        clanMember.donate++;
                        achievements.get(9).addCount(1);// hỗ trợ đồng đội
                        Player _player = SessionManager.findChar(message.playerId);
                        if (_player == null) {
                            clanMember = clan.getMember(message.playerId);
                            if (clanMember != null) {
                                clanMember.addItem(item, this.name);
                            }
                        } else {
                            _player.addItem(item);
                            _player.service
                                    .sendThongBao(String.format("Bạn nhận được %s từ %s", item.template.name, name));
                        }
                        removeItemBox(index, 1);
                        message.receive++;
                        message.isNewMessage = false;
                        clan.addMessage(message);
                    } else {
                        service.sendThongBao("Bạn đã hết đậu thần, hãy thu hoạch thêm");
                    }
                }
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 35");
            logger.error("failed!", ex);
        }
    }

    public void clanRemote(Message ms) {
        try {
            if (taskMain.id < 12) {
                return;
            }
            if (clan == null) {
                return;
            }
            int id = ms.reader().readInt();
            byte type = ms.reader().readByte();
            ClanMember remoter = clan.getMember(this.id);
            ClanMember clanMember = clan.getMember(id);
            if (remoter == null || clanMember == null) {
                return;
            }
            if (type == -1) {
                // kick
                if (clanMember.role > remoter.role && clan.deleteMember(clanMember)) {
                    ClanMessage message = new ClanMessage();
                    message.type = 0;
                    message.color = 1;
                    message.playerId = remoter.playerID;
                    message.playerName = remoter.name;
                    message.role = remoter.role;
                    message.isNewMessage = true;
                    message.chat = "Đã đuổi " + clanMember.name + " ra khỏi bang";
                    clan.addMessage(message);
                    Player _c = SessionManager.findChar(id);
                    if (_c != null) {
                        _c.clan = null;
                        _c.clanID = -1;
                        _c.updateBag();// _c.bag = -1;//_c.zone.service.updateBag(_c);
                        _c.service.clanInfo();
                        zone.service.playerLoadAll(_c);
                    } else {
                        GameRepository.getInstance().player.setClanId(id, -1);
                    }
                }
            }
            if (type == 0) {
                // phong chủ bang
                if (remoter.role == 0) {
                    menus.clear();
                    menus.add(new KeyValue(603, "Đồng ý", clanMember.playerID));
                    menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                    service.openUIConfirm(NpcName.CON_MEO,
                            String.format("Bạn có đồng ý nhường chức bang chủ cho %s?", clanMember.name),
                            getPetAvatar(), menus);
                }
            }
            if (type == 1) {
                // phong phó bang
                if (clanMember.role == 2 && remoter.role <= 1) {
                    clanMember.role = 1;
                    clan.clanUpdate((byte) 2, clanMember, -1);
                    ClanMessage message = new ClanMessage();
                    message.type = 0;
                    message.color = 1;
                    message.playerId = remoter.playerID;
                    message.playerName = remoter.name;
                    message.role = remoter.role;
                    message.isNewMessage = true;
                    message.chat = "Đã phong phó bang cho " + clanMember.name;
                    clan.addMessage(message);
                }
            }
            if (type == 2) {
                // giáng chức
                if (remoter.role == 0) {
                    clanMember.role = 2;
                    clan.clanUpdate((byte) 2, clanMember, -1);
                    ClanMessage message = new ClanMessage();
                    message.type = 0;
                    message.color = 1;
                    message.playerId = remoter.playerID;
                    message.playerName = remoter.name;
                    message.role = remoter.role;
                    message.isNewMessage = true;
                    message.chat = "Đã giáng chức " + clanMember.name;
                    clan.addMessage(message);
                }
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 34");
            logger.error("failed!", ex);
        }
    }

    public void leaveClan() {
        if (taskMain.id < 12) {
            return;
        }
        Clan clan = this.clan;
        if (clan != null) {
            ClanMember clanMember = clan.getMember(this.id);
            if (clanMember.role == 0) {
                return;
            }
            menus.clear();
            menus.add(new KeyValue(604, "OK"));
            menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
            service.openUIConfirm(NpcName.CON_MEO, "Bạn có chắc chắn muốn rời bang hội không?", getPetAvatar(), menus);
        }
    }

    public void clanMessage(Message ms) {
        try {
            if (taskMain.id < 12) {
                return;
            }
            byte type = ms.reader().readByte();
            if (type == 0) {
                if (this.clan == null) {
                    return;
                }
                ClanMember mem = clan.getMember(this.id);
                if (mem == null) {
                    return;
                }
                String text = ms.reader().readUTF();
                ClanMessage message = new ClanMessage();
                message.type = 0;
                message.playerId = this.id;
                message.playerName = this.name;
                message.role = mem.role;
                message.color = 0;
                message.chat = text;
                message.isNewMessage = true;
                clan.addMessage(message);
            }
            if (type == 1) {
                if (clan == null) {
                    return;
                }
                ClanMember mem = clan.getMember(this.id);
                if (mem == null) {
                    return;
                }
                long now = System.currentTimeMillis();
                int seconds = (int) ((now - lastRequestPean) / 1000);
                if (seconds >= 300) {
                    this.lastRequestPean = now;
                    ClanMessage message = clan.getMessage(this.id, (byte) 1);
                    if (message != null) {
                        message.isNewMessage = true;
                        if (message.receive >= message.maxCap) {
                            message.receive = 0;
                        }
                        message.role = mem.role;
                        message.time = (int) (System.currentTimeMillis() / 1000);
                        clan.addMessage(message);
                    } else {
                        message = new ClanMessage();
                        message.type = 1;
                        message.receive = 0;
                        message.maxCap = 5;
                        message.playerId = this.id;
                        message.playerName = this.name;
                        message.role = mem.role;
                        message.isNewMessage = true;
                        clan.addMessage(message);
                    }
                } else {
                    service.sendThongBao("Vui lòng xin đậu sau " + Utils.timeAgo(300 - seconds));
                }
            }
            if (type == 2) {
                if (this.clan != null) {
                    return;
                }
                if (this.taskMain.id < 12) {
                    return;
                }
                int clanID = ms.reader().readInt();
                Clan clan = ClanManager.getInstance().findClanById(clanID);
                if (clan != null) {
                    if (clan.getNumberMember() >= clan.maxMember) {
                        service.sendThongBao("Thành viên đã đạt tối đa");
                        return;
                    }
                    ClanMessage message = new ClanMessage();
                    message.type = 2;
                    message.playerId = this.id;
                    message.playerName = this.name;
                    message.role = 2;
                    message.setFashion(head, body, leg);
                    message.power = info.power;
                    message.isNewMessage = true;
                    clan.addMessage(message);
                }
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 33");
            logger.error("failed!", ex);
        }
    }

    public void clanInvite(Message ms) {
        try {
            byte action = ms.reader().readByte();
            if (action == 0) {
                if (clan == null) {
                    return;
                }
                int playerID = ms.reader().readInt();
                if (playerID < 0) {
                    return;
                }
                Player _player = zone.findCharByID(playerID);
                if (_player != null) {
                    if (_player.taskMain.id < 12) {
                        service.sendThongBao("Người chơi chưa thể vào bang lúc này");
                        return;
                    }
                    if (clan.getNumberMember() >= clan.maxMember) {
                        service.sendThongBao("Thành viên đã đạt tối đa");
                        return;
                    }
                    if (_player.clan != null) {
                        service.sendThongBao("Người chơi đã tham gia bang hội khác");
                        return;
                    }
                    if (clan.getMember(this.id).role == 2) {
                        return;
                    }
                    if (_player.invite.findCharInvite(Invite.INVITE_CLAN, this.clanID) != null) {
                        service.sendThongBao("Không thể mời vào bang hội liên tục");
                        return;
                    }
                    _player.invite.addCharInvite(Invite.INVITE_CLAN, this.clanID, 15);
                    _player.service.clanInvite(String.format("%s mời bạn gia nhập bang %s, bạn có muốn gia nhập không?",
                            this.name, this.clan.name), this.clanID, this.id);
                }
            } else if (action == 1) {
                if (clan != null) {
                    service.sendThongBao("Bạn đã tham gia bang hội khác");
                    return;
                }
                int clanID = ms.reader().readInt();
                if (invite != null) {
                    Clan clan = ClanManager.getInstance().findClanById(clanID);
                    if (clan.getNumberMember() >= clan.maxMember) {
                        service.sendThongBao("Thành viên đã đạt tối đa");
                        return;
                    }
                    ClanMemberData clanMemberData = new ClanMemberData(clan.id, this, (byte) 2);
                    GameRepository.getInstance().clanMember.save(clanMemberData);
                    ClanMember clanMember = new ClanMember(clanMemberData);
                    clan.addMember(clanMember);
                    clan.clanUpdate((byte) 0, clanMember, -1);
                    this.clanID = clanID;
                    this.clan = clan;
                    service.clanInfo();
                    updateBag();
                    service.sendThongBao(String.format("Bạn đã gia nhập bang %s thành công", clan.name));
                    zone.service.playerLoadAll(this);
                }
            } else if (action == 3) {
                int playerID = ms.reader().readInt();
                if (playerID < 0) {
                    return;
                }
                Player _player = zone.findCharByID(playerID);
                if (_player != null) {
                    BaiSu.Action(this, _player);
                }
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 31");
            logger.error("failed!", ex);
        }
    }

    public void viewClanMember(Message ms) {
        try {
            if (taskMain.id < 12) {
                return;
            }
            Clan clan = ClanManager.getInstance().findClanById(ms.reader().readInt());
            if (clan != null) {
                service.clanMember(clan);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 30");
            logger.error("failed!", ex);
        }
    }

    public void clearEffect() {
        setTimeForItemtime(0, 0);
        setTimeForItemtime(1, 0);
        setTimeForItemtime(3, 0);
        setTimeForItemtime(4, 0);
        setTimeForItemtime(5, 0);
        setTimeForItemtime(14, 0);
        if (zone != null) {
            zone.service.setEffect(null, this.id, Skill.REMOVE_ALL_EFFECT, Skill.CHARACTER, (short) -1);
        }
        updateItemTime();
    }

    public boolean isBoss() {
        return false;
    }

    public boolean isHuman() {
        return true;
    }

    public boolean isBot() {
        if (this instanceof VirtualBot || this instanceof BotCold) {
            return true;
        }
        return false;
    }

    public boolean isDisciple() {
        return false;
    }

    public boolean isMiniDisciple() {
        return false;
    }

    public boolean isEscort() {
        return false;
    }

    public void kill(Object victim) {
        if (victim instanceof Player) {
            Player v = (Player) victim;
            if (!isAnDanh && isHuman() && v.isHuman()) {
                v.addEnemy(this);
            }
            if (isHuman() && v.isHuman()) {
                if (testCharId == v.id && v.testCharId == this.id && betAmount == 0) {
                    removeEnemy(v);
                }
                if (zone.map.isBaseBabidi() && this.flag != v.flag) {
                    addAccumulatedPoint(5);
                    v.addAccumulatedPoint(-5);
                }
            }
        } else {
            Mob mob = (Mob) victim;
            if (achievements != null) {
                if (mob.templateId == MobName.MOC_NHAN) {
                    achievements.get(7).addCount(1);// tập luyện bài bản
                }
                if (mob.type == 4) {
                    achievements.get(6).addCount(1);// thợ săn thiện xạ
                }
                if (mob.levelBoss != 0) {
                    achievements.get(12).addCount(1);// đánh bại siêu quái
                }
            }
            if (taskMain != null) {
                Task task = taskMain;
                switch (task.id) {
                    case 1:
                        if (task.index == 0) {
                            if (mob.templateId == MobName.MOC_NHAN) {
                                updateTaskCount(1);
                            }
                        }
                        break;

                    case 6:// nhiệm vụ
                        if (task.index <= 2) {
                            int[][] mobTask = {{MobName.KHUNG_LONG_ME, MobName.LON_LOI_ME, MobName.QUY_DAT_ME},
                            {MobName.LON_LOI_ME, MobName.QUY_DAT_ME, MobName.KHUNG_LONG_ME},
                            {MobName.QUY_DAT_ME, MobName.KHUNG_LONG_ME, MobName.LON_LOI_ME}};
                            int mobId = mobTask[gender][task.index];
                            if (mob.templateId == mobId) {
                                updateTaskCount(1);
                            }
                        }
                        break;

                    case 7:// nhiệm vụ giải cứu
                        if (task.index == 1) {
                            int mobId = (new int[]{MobName.THAN_LAN_BAY, MobName.PHI_LONG, MobName.QUY_BAY})[gender];
                            if (mob.templateId == mobId) {
                                updateTaskCount(1);
                            }
                        }
                        break;

                    case 13: {// nhiem vu danh heo
                        int[] mobs = {MobName.HEO_RUNG, MobName.HEO_DA_XANH, MobName.HEO_XAYDA};
                        if (task.index < 3 && mob.templateId == mobs[task.index]) {
                            updateTaskCount(1);
                        }
                        break;
                    }

                    case 15: {// nhiem vu bulon
                        int[] mobs = {MobName.BULON, MobName.UKULELE, MobName.QUY_MAP};
                        if (task.index > 0 && task.index < 4 && mob.templateId == mobs[task.index - 1]) {
                            updateTaskCount(1);
                        }
                        break;
                    }
                    case 16: {// nhiem vu danh tabourine, drum, akkuman
                        int[] mobs = {MobName.TAMBOURINE, MobName.DRUM, MobName.AKKUMAN};
                        if (task.index < 3 && mob.templateId == mobs[task.index]) {
                            updateTaskCount(1);
                        }
                        break;
                    }
                    case 18: {
                        int[] mobs = {MobName.NAPPA, MobName.SOLDIER, MobName.APPULE, MobName.RASPBERRY,
                            MobName.THAN_LAN_XANH};
                        if (task.index < 5 && mob.templateId == mobs[task.index]) {
                            updateTaskCount(1);
                        }
                        break;
                    }
                    case 22: {
                        if (task.index == 4 && mob.templateId == MobName.XEN_CON_CAP_3) {
                            updateTaskCount(1);
                        }
                        break;
                    }
                    case 24: {
                        if (task.index == 4 && mob.templateId == MobName.XEN_CON_CAP__5) {
                            updateTaskCount(1);
                        }
                        break;
                    }
                    case 25: {
                        if (task.index == 4 && mob.templateId == MobName.XEN_CON_CAP__8) {
                            updateTaskCount(1);
                        }
                        break;
                    }
                    case 29: {
                        if (task.index == 3 && mob.templateId == 80) {
                            updateTaskCount(1);
                        }
                        if (task.index == 4 && mob.templateId == 81) {
                            updateTaskCount(1);
                        }
                        break;
                    }
                }
            }
        }
    }

    public int checkEffectOfSatellite(int id) {
        int number = 0;
        if (zone != null) {
            List<ItemMap> list = zone.getListSatellite();
            for (ItemMap item : list) {
                if (item.item.id == id) {
                    int d = Utils.getDistance(this.x, this.y, item.x, item.y);
                    if (d < item.r) {
                        if (item.owner.id == this.id || (item.owner.clan != null && item.owner.clan == this.clan)) {
                            number++;
                        }
                    }
                }
            }
        }
        return number;
    }

    public void killed(Object killer) {
        throwItem(killer);
    }

    public void throwItem(Object obj) {
        if (isHuman()) {
            int gold = info.level * 1000;
            if (gold > this.gold) {
                gold = (int) this.gold;
            }
            if (gold == 0) {
                return;
            }
            int itemID = Utils.getItemGoldByQuantity(gold);
            addGold(-gold);
            Item item = new Item(itemID);
            item.setDefaultOptions();
            item.quantity = gold;
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.x = this.x;
            itemMap.y = zone.map.collisionLand(x, y);
            itemMap.playerID = this.id;
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        }
    }

    public void startDie() {
        try {
            if (itemLoot != null) {
                dropItemSpe();
                if (zone instanceof ZBlackDragonBall) {
                    ZBlackDragonBall z = (ZBlackDragonBall) zone;
                    z.itemBlackDragonBall.isPickedUp = false;
                    z.service.addItemMap(z.itemBlackDragonBall);
                    z.itemBlackDragonBall.countDown = 3;
                    z.setPlayerHolding(null);
                }

            }
            if (this.typePk == 3) {
                Player playerPk = zone.findCharByID(this.testCharId);
                clearPk();
                playerPk.clearPk();
                resultPk((byte) 1);
                playerPk.resultPk((byte) 0);
            }
            if (this.mobMe != null) {
                this.mobMe.timeLive = 0;
            }
            if (isTrading) {
                trader.service.sendThongBao(Language.TRADE_FAIL);
                service.sendThongBao(Language.TRADE_FAIL);
                trader.clearTrade();
                clearTrade();
            }
            if (this.isMonkey) {
                timeOutIsMonkey();
            }
            if (this.hold != null) {
                this.hold.close();
            }
            if (isRecoveryEnergy) {
                stopRecoveryEnery();
            }
            if (getPhuX() > 0) {
                setPhuX(0);
                info.setInfo();
                service.loadPoint();
            }
            if (this.mapPhuHo != -1) {
                this.mapPhuHo = this.percentPhuHo = -1;
                service.sendThongBao("Bạn vừa mất phù hộ");
                info.setInfo();
                service.loadPoint();
            }
            isSkillSpecial = false;
            isCharge = false;
            this.seconds = 0;
            this.isFreeze = false;
            this.isCritFirstHit = false;
            this.statusMe = 5;
            this.info.hp = 0;
            this.isDead = true;

            this.y = zone.map.collisionLand(x, y);
            clearEffect();
            Message ms = new Message(Cmd.ME_DIE);
            FastDataOutputStream ds = ms.writer();
            ds.writeByte(this.typePk);
            ds.writeShort(this.x);
            ds.writeShort(this.y);
            ds.writeLong(this.info.power);
            service.sendMessage(ms);
            ms.cleanup();

            ms = new Message(Cmd.PLAYER_DIE);
            ds = ms.writer();
            ds.writeInt(this.id);
            ds.writeByte(this.typePk);
            ds.writeShort(this.x);
            ds.writeShort(this.y);
            zone.service.sendMessage(ms, this);
            ms.cleanup();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 29");
            logger.error("failed!", ex);
        }
    }

    public void goHome() {

        lock.lock();
        try {
            int mapID = 0;
            switch (this.gender) {
                case 0:
                    mapID = 21;
                    break;

                case 1:
                    mapID = 22;
                    break;

                case 2:
                    mapID = 23;
                    break;
            }
            if (zone.map.mapID == 127) {
                mapID = 52;
            }
            teleport(mapID);
        } finally {
            lock.unlock();

        }
    }

    public void returnTownFromDead() {
        lock.lock();
        try {
            if (!this.isDead) {
                return;
            }
            int mapID = 0;
            switch (this.gender) {
                case 0:
                    mapID = 21;
                    break;

                case 1:
                    mapID = 22;
                    break;

                case 2:
                    mapID = 23;
                    break;
            }
            if (zone.map.isBaseBabidi()) {
                mapID = BaseBabidi.MAPS[0];
                info.hp = info.hpFull;
                info.mp = info.mpFull;
                setTeleport((byte) ((ship == 1) ? 3 : 1));
                service.addTeleport(this.id, this.teleport);
                TMap map = MapManager.getInstance().getMap(mapID);
                this.x = calculateX(map);
                this.y = 0;
                int zoneID = map.getZoneID();
                zone.leave(this);
                map.enterZone(this, zoneID);
                this.y = map.collisionLand(this.x, this.y);
                setTeleport((byte) 0);
            } else {
                info.hp = 1;
                info.mp = 1;
                teleport(mapID);
            }
            isDead = false;
            statusMe = 1;
            service.sendMessage(new Message(Cmd.ME_LIVE));
            service.loadInfo();
        } finally {
            lock.unlock();

        }
    }

    public long getTotalGem() {
        return (long) getDiamond() + (long) getDiamondLock();
    }

    public short calculateX(TMap map) {
        double g = (double) this.x / (double) zone.map.width;
        short x = (short) (g * map.width);
        return x;
    }

    public short calculateY(TMap map) {
        short y = this.y;
        while (!((map.tileTypeAtPixel(this.x, y) & TMap.T_TOP) == TMap.T_TOP)) {
            y += 24;
            if (y >= map.width) {
                return 0;
            }
        }
        return y;
    }

    public short calculateY(TMap map, short cx, short cy) {
        short y = cy;
        while (!(map.tileTypeAtPixel(cx, y) == 2)) {
            y += 24;
            if (y >= map.width) {
                return 0;
            }
        }
        return y;
    }

    public void teleport(int mapId) {
        TMap map = MapManager.getInstance().getMap(mapId);
        if (map.isBarrack()) {
            if (clan == null || clan.barrack == null) {
                return;
            }
            if (!clan.barrack.running) {
                service.serverMessage2("Trại độc nhãn đã kết thúc");
                return;
            }
        }
        byte ship = getShip();
        if (ship == 1) {
            setTeleport((byte) 3);
        } else {
            setTeleport((byte) 1);
        }
        if (ship == 1) {
            info.recovery(Info.ALL, 100, true);
        }
        zone.service.addTeleport(this.id, this.teleport);
        switch (mapId) {
            case 68:
                this.x = (short) 100;
                break;
            case 19:
                this.x = (short) (map.width - 100);
                break;
            default:
                this.x = calculateX(map);
                break;
        }
        this.y = 0;
        zone.leave(this);
        if (map.isMapSingle()) {
            enterMapSingle(map);
        } else if (map.isBarrack()) {
            clan.barrack.enterMap(mapId, this);
        } else {
            int zoneId;
            if (isGoBack()) {
                zoneId = currZoneId;
                isGoBack = false;
            } else {
                zoneId = map.getZoneID();
            }
            map.enterZone(this, zoneId);
        }
        this.y = zone.map.collisionLand(this.x, this.y);
        setTeleport((byte) 0);
    }

    public void teleport_home(int mapId) {
        TMap map = MapManager.getInstance().getMap(mapId);
        if (map.isBarrack()) {
            if (clan == null || clan.barrack == null) {
                return;
            }
            if (!clan.barrack.running) {
                service.serverMessage2("Trại độc nhãn đã kết thúc");
                return;
            }
        }
        zone.leave(this);
        enterMapSingle(map);
        this.y = zone.map.collisionLand(this.x, this.y);
    }

    public void enterMapSingle(TMap map) {
        int zoneID = map.autoIncrease++;
        Zone z = null;
        if (map.mapID == MapName.NHA_GOHAN || map.mapID == MapName.NHA_MOORI || map.mapID == MapName.NHA_BROLY) {
            z = new Home(map, zoneID);
        } else if (map.mapID == MapName.RUNG_KARIN) {
            z = new KarinForest(map, zoneID, this);
        } // else if (map.mapID == MapName.DONG_NAM_KARIN) {
        // z = new SoutheastKarin(map, zoneID);
        // }
        else if (map.mapID == MapName.THAP_KARIN) {
            z = new KarinTower(map, zoneID, typeTraining);
        } else {
            z = new MapSingle(map, zoneID);
        }
        map.addZone(z);
        z.enter(this);
    }

    public void getMagicTree(Message ms) {
        try {
            byte action = ms.reader().readByte();
            if (action == 2) {
                magicTree.update();
                service.magicTree((byte) 0, magicTree);
            }
            if (action == 1) {
                magicTree.openMenu(this);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 28");
            logger.error("faild!", ex);
        }
    }

    public void closeTrade() {
        if (isTrading) {
            trader.service.sendThongBao(Language.TRADE_FAIL);
            service.sendThongBao(Language.TRADE_FAIL);
            trader.clearTrade();
            clearTrade();
        }
    }

    public long lastSaveData = 0l;

    public void saveData() {
        if (this instanceof VirtualBot || this instanceof BotCold) {
            return;
        }
        if (this.isBoss()) {
            return;
        }
        lastSaveData = System.currentTimeMillis();
        try {
            SuperRank.saveSuperRank(this);
            GameRepository.getInstance().eventTet.setReward(this.id, this.getRewardEventTet());
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 27");
            e.printStackTrace();
        }
        try {
            if (isLoggedOut) {
                System.err.println("return SaveData..............\n");
                return;

            }
            if (myDisciple != null) {
                myDisciple.saveData();
            }
            Gson g = new Gson();
            int thoivang = 0;
            ArrayList<Item> bags = new ArrayList<>();
            for (Item item : this.itemBag) {
                if (item != null) {
                    bags.add(item);
                    if (item.template.id == 457) {
                        thoivang += item.quantity;
                    }
                }

            }

            ArrayList<Item> bodys = new ArrayList<>();
            for (Item item : this.itemBody) {
                if (item != null) {
                    bodys.add(item);
                    if (item.template.id == 457) {
                        thoivang += item.quantity;
                    }
                }
            }

            ArrayList<Item> boxs = new ArrayList<>();
            for (Item item : this.itemBox) {
                if (item != null) {
                    boxs.add(item);
                    if (item.template.id == 457) {
                        thoivang += item.quantity;
                    }
                }
            }

            ArrayList<Integer> maps = new ArrayList<>();
            int mapId;
            if (zone == null) {
                mapId = transportToMap;
            } else {
                mapId = zone.map.mapID;
            }

            int x = this.x;
            int y = this.y;
            if (zone != null) {
                TMap map = zone.map;
                if (this.isDead || (map.isCantOffline())) {
                    if (map.isTreasure() || map.isClanTerritory()) {
                        mapId = MapName.DAO_KAME;
                        x = 1000;
                        y = 408;
                    } else if (map.isDauTruong()) {
                        mapId = MapName.DAI_HOI_VO_THUAT;
                    } else {
                        switch (this.gender) {
                            case 0:
                                mapId = MapName.NHA_GOHAN;
                                x = 456;
                                y = 336;
                                break;

                            case 1:
                                mapId = MapName.NHA_MOORI;
                                x = 168;
                                y = 336;
                                break;

                            case 2:
                                mapId = MapName.NHA_BROLY;
                                x = 432;
                                y = 336;
                                break;
                        }
                    }
                    if (isDead) {
                        this.info.hp = 1;
                        this.info.mp = 1;
                    }
                }
            }
            maps.add(mapId);
            maps.add(x);
            maps.add(y);
            ArrayList<ItemTime> items = new ArrayList<>();
            for (ItemTime item : itemTimes) {
                if (!item.isSave) {
                    continue;
                }
                items.add(item);
            }
            JSONArray skills = new JSONArray();
            for (Skill skill : this.skills) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("id", skill.template.id);
                    obj.put("level", skill.point);
                    obj.put("last_time_use", skill.lastTimeUseThisSkill);
                    skills.put(obj);
                } catch (JSONException ex) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                    System.err.println("Error at 26");
                    logger.error("failed!", ex);
                }
            }
            String study = null;
            if (this.studying != null) {
                JSONObject obj = new JSONObject();
                obj.put("id", studying.id);
                obj.put("level", studying.level);
                obj.put("studying_time", studying.studyTime);
                study = obj.toString();
            }

            String dataDHVT23 = String.format("[%d,%d,%s,%d]", roundDHVT23, timesOfDHVT23, isGetChest ? "1" : "0",
                    countDhvtSieuHang);

            JSONObject datadrop = new JSONObject();
            datadrop.put(String.valueOf(ItemName.MANH_VO_BONG_TAI), itemDrop[0]);
            datadrop.put(String.valueOf(ItemName.MANH_HON_BONG_TAI), itemDrop[1]);
            datadrop.put(String.valueOf(ItemName.NGOC_RONG_LOC_PHAT_7_SAO), itemDrop[2]);
            GameRepository.getInstance().player.saveData(this.id, g.toJson(this.taskMain), this.gold, this.diamond,
                    this.diamondLock, g.toJson(bags), g.toJson(bodys), g.toJson(boxs),
                    g.toJson(maps), skills.toString(), g.toJson(this.info), this.clanID, g.toJson(this.shortcut),
                    this.numberCellBag, this.numberCellBox, g.toJson(this.friends), g.toJson(this.enemies),
                    this.headDefault, this.ship, g.toJson(this.magicTree), g.toJson(items), this.fusionType,
                    g.toJson(this.amulets), this.typeTraining, g.toJson(this.achievements), this.timePlayed, study,
                    g.toJson(this.boxCrackBall), this.timeAtSplitFusion, (int) this.head, (int) this.body,
                    (int) this.leg, typePorata, g.toJson(this.cards), g.toJson(this.specialSkill),
                    this.countNumberOfSpecialSkillChanges, resetTime, dataDHVT23, thoivang, datadrop.toString());
            logger.debug("saveDataPlayer " + this.name + " success");
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 25");
            e.printStackTrace();
            logger.debug("Error at saveDataPlayer -> " + this.name, e);
        }
    }

    public void addGold(long gold) {
        this.gold += gold;
        service.addGold(gold);
    }

    public void addDiamond(int diamond) {
        this.diamond += diamond;
        service.loadInfo();
    }

    public void addDiamondLock(int diamondLock) {
        this.diamondLock += diamondLock;
        service.loadInfo();
    }

    public boolean checkBalanceEnough(int balance, byte type) {
        if (type == 0) {
            if (balance > getGold()) {
                service.sendThongBao(String.format("Bạn không đủ vàng, còn thiếu %s vàng nữa",
                        Utils.formatNumber(balance - getGold())));
                return false;
            }
        }
        if (type == 1) {
            if (balance > getDiamond()) {
                service.sendThongBao(String.format("Bạn không đủ ngọc xanh, còn thiếu %s ngọc xanh nữa",
                        Utils.formatNumber(balance - getDiamond())));
                return false;
            }
        }
        if (type == 2) {
            if (balance > getDiamondLock()) {
                service.sendThongBao(String.format("Bạn không đủ hồng ngọc, còn thiếu %s hồng ngọc nữa",
                        Utils.formatNumber(balance - getDiamond())));
                return false;
            }
        }
        if (type == 3) {
            if (balance > getTotalGem()) {
                service.sendThongBao(String.format("Bạn không đủ ngọc, còn thiếu %s ngọc nữa",
                        Utils.formatNumber(balance - getTotalGem())));
                return false;
            }
        }
        return true;
    }

    public void close() {
        accumulatedPoint = null;
        achievements = null;
        ambientEffects = null;
        amulets = null;
        boxCrackBall = null;
        callDragon = null;
        cards = null;
        clan = null;
        combine = null;
        crackBall = null;
        currMap = null;
        effects = null;
        enemies = null;
        friends = null;
        info = null;
        invite = null;
        itemBag = null;
        itemBody = null;
        itemBox = null;
        itemTimes = null;
        itemsTrading = null;
        listAccessMap = null;
        lock = null;
        lockAction = null;
        lucky = null;
        magicTree = null;
        menus = null;
        menu = null;
        mobMe = null;
        myDisciple = null;
        name = null;
        petFollow = null;
        service = null;
        session = null;
        shop = null;
        shortcut = null;
        skills = null;
        specialSkill = null;
        status = null;
        studying = null;
        taskMain = null;
        trader = null;
        // zone = null;
    }

    public void enter() {
        Server server = DragonBall.getInstance().getServer();
        GameRepository.getInstance().player.setOnline(this.id, (byte) server.getConfig().getServerID(),
                new Timestamp(System.currentTimeMillis()));
        updateSkin();
        setMount();
        updatePetTheoSau(false);
        info.setInfo();
        service.sendDataBG();
        service.setTileSet();
        service.setTask();
        service.loadAll();
        service.updateActivePoint();
        service.setMaxStamina();
        service.setStamina();
        service.loadPoint();
        service.specialSkill((byte) 0);
        if (shortcut != null) {
            service.changeOnSkill(shortcut);
        }
        service.updateCoolDown(skills);
        if (amulets == null) {
            amulets = new ArrayList<>();
        }
        if (itemTimes == null) {
            itemTimes = new ArrayList<>();
        }
        if (this.myDisciple == null) {
            service.petInfo((byte) 0);
        } else {
            Disciple deTu = this.myDisciple;
            deTu.setMaster(this);
            deTu.service = new Service(deTu);
            deTu.followMaster();
            deTu.updateSkin();
            service.petInfo((byte) 1);
        }
        for (int m : Barrack.MAPS) {
            if (m == mapEnter) {
                switch (gender) {
                    case 0:
                        mapEnter = 21;
                        x = 456;
                        y = 336;
                        break;

                    case 1:
                        mapEnter = 22;
                        x = 168;
                        y = 336;
                        break;

                    case 2:
                        mapEnter = 23;
                        x = 432;
                        y = 336;
                        break;
                }
                break;
            }
        }
        boolean isFusion = false;
        for (ItemTime item : itemTimes) {
            if (item.id == 2) {
                isFusion = true;
            }
            service.setItemTime(item);
            if (item.id == 12) {
                isAutoPlay = true;
            }
        }
        if (fusionType == 4 && !isFusion) {
            isNhapThe = true;
            ItemTime itemTime = new ItemTime(ItemTimeName.HOP_THE, gender == 1 ? 3901 : 3790, 10, true);
            addItemTime(itemTime);
        }
        TMap map = MapManager.getInstance().getMap(mapEnter);
        if (!map.isMapSingle()) {
            int zoneId = map.getZoneID();
            map.enterZone(this, zoneId);
        } else {
            enterMapSingle(map);
        }
        if (this.isMask) {
            zone.service.updateBody((byte) 0, this);
        }
        service.gameInfo();
        String subName = taskMain.subNames[taskMain.index];
        service.sendThongBao(subName);

        if (achievements == null) {
            initAchievement();
        } else {
            for (Achievement achive : achievements) {
                achive.initTemplate();
            }
        }
        if (clan != null) {
            ClanMember clanMember = clan.getMember(id);
            if (clanMember == null) {
                clan = null;
                clanID = -1;
            } else {
                clanMember.receiveItem(this);
                service.clanInfo();
            }
        }
        int mapID = zone.map.mapID;
        if (!(mapID == 39 || mapID == 40 || mapID == 41)) {
            Notification notification = Notification.getInstance();
            if (notification != null && !notification.equals("")) {
                service.openUISay(5, notification.getText(), notification.getAvatar());
            }
        }
        Utils.setTimeout(() -> {
            this.sort();
            updatePetTheoSau(true);
        }, 1000);
        setListAccessMap();
    }

    public void thachDauDHVTSieuHang(int id) {
        long now = System.currentTimeMillis();
        long delay = now - lastTimeThachDau;
        if (delay < 3000) {
            service.serverMessage("Thời gian giãn cách giữa mỗi lần thách đấu là 3s");
            return;
        }
        if (id == this.id) {
            service.serverMessage("Bạn không thể thách đấu chính mình");
            return;
        }
        Server server = DragonBall.getInstance().getServer();
        if (server.isStopDhvtSieuHang) {
            service.serverMessage("Đang trong thời gian tổng kết thứ hạng, không thể thách đấu.");
            return;
        }
        if (server.isFightingDhvtSieuHang != null) {
            if (server.isFightingDhvtSieuHang.stream().anyMatch((s) -> s == id)) {
                service.serverMessage("Đối thủ đang thi đấu, hãy quay trở lại sau");
                return;
            }
            ;
        }
        if (server.isFightingDhvtSieuHang != null) {
            if (server.isFightingDhvtSieuHang.stream().anyMatch((s) -> s == this.id)) {
                service.serverMessage("Bạn đang bị thách đấu nên không thể khiêu chiến người khác");
                return;
            }
            ;
        }
        if (this.session.user.getActivated() == 0) {
            service.sendThongBao("Bạn cần kích hoạt thành viên để sử dụng tính năng này");
            return;
        }
        // id = id doi thu
        PlayerData data = null;
        Optional<PlayerData> dataPlayer = GameRepository.getInstance().player.findById(id);
        DHVTSieuHangData dhvtSieuHangData = null;
        if (dataPlayer.isPresent()) {
            data = dataPlayer.get();
        } else {
            this.service.serverMessage("Có lỗi xảy ra");
            return;
        }
        Optional<DHVTSieuHangData> rs = GameRepository.getInstance().dhvtSieuHangRepository.findFirstByPlayerId(id);
        if (rs.isPresent()) {
            dhvtSieuHangData = rs.get();
        }
        if (dhvtSieuHangData == null || dhvtSieuHangData.playerId < 0) {
            this.service.serverMessage("Có lỗi xảy ra");
            return;
        }
        int pointTarget = dhvtSieuHangData.getPoint();
        if (this.pointDhvtSieuhang < pointTarget) {
            this.service.serverMessage("Bạn không thể thách đấu người có hạng thấp hơn mình");
            return;
        }

        if (pointDhvtSieuhang < 10 || pointTarget < 10) {

            if (Math.abs(pointTarget - this.pointDhvtSieuhang) > 1) {
                this.service.serverMessage("Bạn không thể thách đấu người cao hơn 1 hạng so với bản thân");
                return;
            }
        } else {
            if (Math.abs(pointTarget - this.pointDhvtSieuhang) > 10) {
                this.service.serverMessage("Bạn không thể thách đấu người cao hơn 10 hạng so với bản thân");
                return;
            }
        }
        server.isFightingDhvtSieuHang.add(id); // id clone boos
        server.isFightingDhvtSieuHang.add(this.id); // id nguoi thach dau
        try {
            int clonePlayerId = id;
            this.inFighting = true;
            TMap map = MapManager.getInstance().getMap(MapName.DAU_TRUONG);
            ArenaSieuHang arenaSieuHang = new ArenaSieuHang(map, this.zone.zoneID);
            if (this == null) {
                return;
            }
            this.setX((short) 334);
            this.setY((short) 274);
            this.zone.leave(this);
            arenaSieuHang.setData(data);
            arenaSieuHang.setCurrFightingPlayer(this);
            arenaSieuHang.enter(this);
            arenaSieuHang.setClonePlayerId(clonePlayerId);
            arenaSieuHang.setBossPointDhvt(dhvtSieuHangData.getPoint() > 0 ? dhvtSieuHangData.getPoint() : 0);
            zone.service.setPosition(this, (byte) 0);
        } catch (Exception ignored) {
            System.err.println("Error at 24");
            ignored.printStackTrace();
        }
    }

    public void getRewardDHVTSieuHang(List<TopInfo> top) {
        Server server = DragonBall.getInstance().getServer();
        if (server.isStopDhvtSieuHang) {
            service.serverMessage("Đang trong thời gian cập nhật BXH, hãy trở lại sau ");
            return;
        }
        if (server.isRewardDhvtSieuHang != null) {
            if (server.isRewardDhvtSieuHang.stream().anyMatch((s) -> s == this.id)) {
                service.serverMessage("Bạn đã nhận phần thưởng rồi");
                return;
            }
            ;
        }
        TopInfo ranking = null;
        for (TopInfo pl : top) {
            if (pl.playerID == this.id) {
                ranking = pl;
                break;
            }
        }
        if (ranking == null || ranking.rank < 1 || ranking.rank > 50) {
            this.service.serverMessage("Bạn không nằm trong danh sách trao giải");
            return;
        }
        if (this.getSlotNullInBag() < 5) {
            service.serverMessage2(Language.ME_BAG_FULL);
            return;
        }
        Optional<DhvtSieuHangReward> eventData;
        try {
            eventData = GameRepository.getInstance().dhvtSieuHangRewardRepository.findFirstByName(ranking.playerID);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 23");
            e.printStackTrace();
            this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
            return;
        }
        DhvtSieuHangReward ev = null;
        if (eventData.isPresent()) {
            ev = eventData.get();
        }
        if (ev != null && ev.reward != 0) {
            this.service.serverMessage("Bạn đã nhận phần thưởng này rồi");
            return;
        }
        try {
            GameRepository.getInstance().dhvtSieuHangRewardRepository.setReward(ranking.playerID, 1);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 22");
            this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
            e.printStackTrace();
            return;
        }
        Item thoiVang = new Item(ItemName.THOI_VANG);
        if (ranking.rank == 1) {
            thoiVang.quantity = 200;
        } else if (ranking.rank >= 2 && ranking.rank <= 10) {
            thoiVang.quantity = 50;
        } else if (ranking.rank >= 11 && ranking.rank <= 50) {
            thoiVang.quantity = 10;
        }
        addItem(thoiVang);
        server.isRewardDhvtSieuHang.add(this.id);
        this.service.serverMessage(
                "Bạn đã nhận được " + thoiVang.quantity + " " + thoiVang.template.name + " phần thưởng ");
    }

    public void logout() {
        if (isLoggedOut) {
            return;
        }
        try {
            // com.ngocrong.NQMP.UtilsNQMP.logError(this.name + "logout");
            try {
                closeTrade();
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                System.err.println("Error at 21");
                e.printStackTrace();
            }
            try {
                saveData();
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                System.err.println("Error at 20");
                e.printStackTrace();
            }
            if (getItemLoot() != null && getItemLoot().isDragonBallNamec()) {
                dropItemSpe();
            }
            if (zone != null) {
                zone.leave(this);
            }
            // History history = new History(id, History.LOGOUT);
            // history.setBefores(gold, diamond, diamondLock);
            // history.setAfters(gold, diamond, diamondLock);
            // for (Item item : itemBag) {
            // if (item != null) {
            // history.addItem(item);
            // }
            // }
            // for (Item item : itemBody) {
            // if (item != null) {
            // history.addItem(item);
            // }
            // }
            // for (Item item : itemBox) {
            // if (item != null) {
            // history.addItem(item);
            // }
            // }
            // history.setExtras(session.ip);
            // history.save();
            GameRepository.getInstance().player.setOffline(this.id, (byte) 0,
                    new Timestamp(System.currentTimeMillis()));
        } finally {
            isLoggedOut = true;
        }
    }

    public void specialSkill(Message mss) {
        try {
            byte index = mss.reader().readByte();
            if (index == 0) {
                menus.clear();
                StringBuilder sb = new StringBuilder();
                sb.append("Nội tại là một kỹ năng bị động hỗ trợ đặc biệt");
                sb.append("\n");
                sb.append("Bạn có muốn mở hoặc thay đổi nội tại không?");
                menus.add(new KeyValue(1111, "Xem\ntất cả\nNội Tại"));
                menus.add(new KeyValue(1112, "Mở\nNội Tại"));
                menus.add(new KeyValue(1113, "Mở VIP"));
                menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
                service.openUIConfirm(NpcName.CON_MEO, sb.toString(), getPetAvatar(), menus);
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 19");
            logger.debug("special skill error", ex);
        }
    }

    public void exchangeItemEvent(int idItem) {
        int index = getIndexBagById(idItem);
        Item item = null;
        if (index != -1) {
            item = itemBag[index];
        }
        if (item == null) {
            service.sendThongBao("Bạn không có vật phẩm để đổi");
            return;
        }
        if (item.quantity < 99) {
            service.sendThongBao("Bạn không có đủ vật phẩm");
            return;
        }
        removeItem(item.indexUI, 99);
        switch (idItem) {
            case ItemName.VO_OC:
                int MAX_GOLD = 1000000000;
                int MIN_GOLD = 100000000;
                int gold = Utils.nextInt(MAX_GOLD - MIN_GOLD + 1) + MIN_GOLD;
                addGold(gold);
                service.sendThongBao("Bạn đã nhận được " + Utils.currencyFormat(gold) + " vàng");
                break;
            case ItemName.VO_SO:
                int[] spl = {ItemName.SAO_PHA_LE_DAME_TO_HP, ItemName.SAO_PHA_LE_DAME_TO_MP, ItemName.SAO_PHA_LE_PST,
                    ItemName.SAO_PHA_LE_TNSM,
                    ItemName.SAO_PHA_LE_VANG, ItemName.SAO_PHA_LE_XG_CAN_CHIET, ItemName.SAO_PHA_LE_XG_CHUONG};
                for (int i = 0; i < 3; i++) {
                    Item saophale = new Item(spl[new Random().nextInt(spl.length - 1)]);
                    saophale.quantity = 1;
                    saophale.setDefaultOptions();
                    addItem(saophale);
                    service.sendThongBao("Bạn đã nhận được " + saophale.template.name);
                }
                break;
            case ItemName.CON_CUA:
                int[] nrList = {ItemName.NGOC_RONG_1_SAO, ItemName.NGOC_RONG_2_SAO, ItemName.NGOC_RONG_3_SAO,
                    ItemName.NGOC_RONG_4_SAO,
                    ItemName.NGOC_RONG_5_SAO, ItemName.NGOC_RONG_6_SAO, ItemName.NGOC_RONG_7_SAO};
                Item nr = new Item(nrList[new Random().nextInt(nrList.length - 1)]);
                nr.quantity = 1;
                nr.setDefaultOptions();
                addItem(nr);
                service.sendThongBao("Bạn đã nhận được " + nr.template.name);
                break;
            case ItemName.SAO_BIEN:

                Item itemCt;
                if (Utils.nextInt(3) == 0) {
                    itemCt = new Item(ItemName.CAI_TRANG_FIDE_VANG);
                    itemCt.addItemOption(new ItemOption(50, 30));
                } else if (Utils.nextInt(3) == 0) {
                    itemCt = new Item(ItemName.CAI_TRANG_HATCHIYACK);
                    itemCt.addItemOption(new ItemOption(77, 30));
                } else {
                    itemCt = new Item(ItemName.CAI_TRANG_BLACK_GOKU_SSJ_WHITE);
                    itemCt.addItemOption(new ItemOption(103, 30));
                }
                if (itemCt != null) {
                    addItem(itemCt);
                    service.sendThongBao("Bạn đã nhận được " + itemCt.template.name);
                }
                break;
            default:
                service.sendThongBao("Vật phẩm này không thể dùng để đổi");
                break;
        }
        try {
            EventOpen ev = new EventOpen();
            ev.setPlayerName(this.name);
            ev.setPoint(1);
            ev.setCreateDate(Instant.now());
            ev.setModifyDate(Instant.now());
            GameRepository.getInstance().eventOpenRepository.save(ev);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 18");
            logger.info("Save error " + this.name);
        }

    }

    public void sendMessage(Message ms) {

        if (session != null) {
            this.service.sendMessage(ms);
        }
    }

    public void withdrawGoldBar(UserData user) {
        if (this.isBagFull()) {
            service.sendThongBao("Hành trang cần ít nhất 1 ô trống");
            return;
        }
        int goldBar = user.getGoldBar();
        if (goldBar > 0) {
            user.setGoldBar(0);
            try {
                GameRepository.getInstance().user.save(user);
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                System.err.println("Error at 17");
                e.printStackTrace();
                service.sendThongBao("Có lỗi khi cập nhật thông tin");
                return;
            }
            Item item;
            int index = this.getIndexBagById(ItemName.THOI_VANG);
            if (index != -1) {
                item = this.itemBag[index];
                item.quantity += goldBar;
                service.setItemBag();
            } else {
                item = new Item(457);
                item.quantity = goldBar;
                addItem(item);
            }
            this.session.user.setGoldBar(0);
            service.sendThongBao("Bạn đã nhận được " + goldBar + " thỏi vàng, hãy kiểm tra hành trang");
        } else {
            service.sendThongBao("Bạn không có thỏi vàng để rút");
        }
    }

    public int shopBillByGender(int idDHD) {
        int idDoTL = 0;
        switch (idDHD) {
            case ItemName.AO_HUY_DIET_TD:
                idDoTL = ItemName.AO_THAN_LINH;
                break;
            case ItemName.AO_HUY_DIET_XD:
                idDoTL = ItemName.AO_THAN_XAYDA;
                break;

            case ItemName.AO_HUY_DIET_NM:
                idDoTL = ItemName.AO_THAN_NAMEC;
                break;

            case ItemName.QUAN_HUY_DIET_TD:
                idDoTL = ItemName.QUAN_THAN_LINH;
                break;

            case ItemName.QUAN_HUY_DIET_XD:
                idDoTL = ItemName.QUAN_THAN_XAYDA;
                break;

            case ItemName.QUAN_HUY_DIET_NM:
                idDoTL = ItemName.QUAN_THAN_NAMEC;
                break;

            case ItemName.GIAY_HUY_DIET_TD:
                idDoTL = ItemName.GIAY_THAN_LINH;
                break;

            case ItemName.GIAY_HUY_DIET_XD:
                idDoTL = ItemName.GIAY_THAN_XAYDA;
                break;

            case ItemName.GIAY_HUY_DIET_NM:
                idDoTL = ItemName.GIAY_THAN_NAMEC;
                break;

            case ItemName.GANG_HUY_DIET_TD:
                idDoTL = ItemName.GANG_THAN_LINH;
                break;

            case ItemName.GANG_HUY_DIET_XD:
                idDoTL = ItemName.GANG_THAN_XAYDA;
                break;

            case ItemName.GANG_HUY_DIET_NM:
                idDoTL = ItemName.GANG_THAN_NAMEC;
                break;

            case ItemName.NHAN_HUY_DIET:
                idDoTL = ItemName.NHAN_THAN_LINH;
                break;

        }
        return idDoTL;
    }

    public void doneDragonNamec() {
        Server server = DragonBall.getInstance().getServer();
        for (int i = 0; i < 7; i++) {
            Player p = SessionManager.findChar(server.idpNrNamec[i]);
            if (p != null) {
                p.idNRNM = -1;
                server.pNrNamec[i] = "";
                server.idpNrNamec[i] = -1;
                p.itemLoot = null;
                p.updateBag();
                p.setTypePK((byte) 0);
            }
        }
    }

    public boolean canCallDragonNamec(Player p) {
        Server server = DragonBall.getInstance().getServer();
        byte count = (byte) 0;
        if (server.isSameMapNrNamec() && server.isSameZoneNrNamec()) {
            if (p.clan != null) {
                for (int i = 0; i < server.idpNrNamec.length; i++) {
                    for (int j = 0; j < p.clan.members.size(); j++) {
                        if (server.idpNrNamec[i] == p.clan.members.get(j).id) {
                            count++;
                        }
                    }
                }
                if (count == (byte) 7) {
                    return true;
                }
            }
        }
        return false;
    }

    public void teleportToNrNamec(Player p) {
        Server server = DragonBall.getInstance().getServer();

        int idMAP = server.mapNrNamec[p.idGo];
        int idZone = server.zoneNrNamec[p.idGo];
        TMap map = MapManager.getInstance().getMap(idMAP);
        Zone z = null;
        if (map != null) {
            z = map.getZoneByID(idZone);
        }
        boolean isTeleported = false;
        List<ItemMap> list = z.getItems();
        if (z != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                ItemMap it = list.get(i);
                if (it != null && it.isDragonBallNamec) {
                    isTeleported = true;
                    short x = calculateX(map);
                    zone.leave(this);
                    this.x = x;
                    this.y = map.collisionLand(x, (short) 24);
                    map.enterZone(this, idZone);
                    break;
                }
            }
        }
        if (!isTeleported) {
            List<Player> listPlayer = z.getPlayers();
            if (z != null && !listPlayer.isEmpty()) {
                for (int i = 0; i < listPlayer.size(); i++) {
                    Player _p = listPlayer.get(i);
                    if (_p != null && _p.itemLoot != null && _p.itemLoot.isDragonBallNamec()) {
                        zone.leave(this);
                        this.x = _p.getX();
                        this.y = _p.getY();
                        map.enterZone(this, idZone);
                        break;
                    }
                }
            }
        }
    }

    public String getDis(Player pl, int id, short temp) {
        Server server = DragonBall.getInstance().getServer();
        try {
            int idMAP = server.mapNrNamec[id];
            int idZone = server.zoneNrNamec[id];
            Integer[] sttMap = {8, 9, 11, 12, 13, 31, 32, 33, 34, 43};
            TMap map = MapManager.getInstance().getMap(idMAP);
            Zone z = null;
            if (map != null) {
                z = map.getZoneByID(idZone);
            }
            List<ItemMap> list = z.getItems();
            if (z != null && !list.isEmpty()) {
                ItemMap it = z.findItemMapByItemID(temp);
                if (it != null) {
                    return "Map: " + z.map.name + ", Khu: " + z.zoneID;
                }
            }
            List<Player> listPlayer = z.getPlayers();
            if (z != null && !listPlayer.isEmpty()) {
                Player player = z.findItemLoot(temp);
                if (player != null) {
                    return "Map: " + z.map.name + ", Khu: " + z.zoneID + "[" + pl.getName() + "]";
                }
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 16");
            e.printStackTrace();
        }
        return "?";
    }

    public void getRewardMoc(int point) {
        // Kiểm tra số ô trống trong túi đồ
        if (getSlotNullInBag() < 7) {
            this.service.serverMessage("Bạn cần ít nhất 7 ô trống để nhận thưởng");
            return;
        }

        Item item = null;

        switch (point) {
            case 100:
                if (numberVongQuay < 100) {
                    this.service.serverMessage(
                            "Bạn còn thiếu " + (100 - numberVongQuay) + " lượt để nhận thưởng mốc này\n");
                    return;
                }

                try {
                    GameRepository.getInstance().eventVQTD.setReward(this.id, 100);
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                    logger.error("nhan thuong loi " + this.name, e);
                    return;
                }

                rewardMoc = 100;

                // Cải trang Ác Thần Huỷ Diệt 24% HP,KI,SĐ vĩnh viễn
                item = new Item(2255); // Ác thần hủy diệt
                item.quantity = 1;
                item.options.add(new ItemOption(77, 24)); // 24% HP
                item.options.add(new ItemOption(103, 24)); // 24% KI
                item.options.add(new ItemOption(50, 24)); // 24% SĐ
                addItem(item);

                // x3 item cuồng nộ
                item = new Item(384);
                item.quantity = 3;
                addItem(item);

                // x3 item bổ huyết
                item = new Item(381);
                item.quantity = 3;
                addItem(item);

                // x3 item giáp xên
                item = new Item(382);
                item.quantity = 3;
                addItem(item);

                // x3 item bổ khí
                item = new Item(383);
                item.quantity = 3;
                addItem(item);

                this.service
                        .serverMessage("Bạn vừa nhận được phần thưởng từ mốc 100, hãy kiểm tra hành trang của mình");
                break;

            case 200:
                if (numberVongQuay < 200) {
                    this.service.serverMessage(
                            "Bạn còn thiếu " + (200 - numberVongQuay) + " lượt để nhận thưởng mốc này\n");
                    return;
                }

                try {
                    GameRepository.getInstance().eventVQTD.setReward(this.id, 200);
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                    logger.error("nhan thuong loi " + this.name, e);
                    return;
                }

                rewardMoc = 200;

                // Thú cưỡi xe ngựa lộng lẫy 10% HP,KI,SĐ hsd 3 ngày
                item = new Item(2257); // Xe ngựa lộng lẫy
                item.quantity = 1;
                item.options.add(new ItemOption(77, 10)); // 10% HP
                item.options.add(new ItemOption(103, 10)); // 10% KI
                item.options.add(new ItemOption(50, 10)); // 10% SĐ
                item.options.add(new ItemOption(93, 3)); // HSD 3 ngày
                addItem(item);

                // x2 Nước Ma Thuật
                item = new Item(2243); // Nước Ma Thuật
                item.quantity = 2;
                addItem(item);

                this.service
                        .serverMessage("Bạn vừa nhận được phần thưởng từ mốc 200, hãy kiểm tra hành trang của mình");
                break;

            case 500:
                if (numberVongQuay < 500) {
                    this.service.serverMessage(
                            "Bạn còn thiếu " + (500 - numberVongQuay) + " lượt để nhận thưởng mốc này\n");
                    return;
                }

                try {
                    GameRepository.getInstance().eventVQTD.setReward(this.id, 500);
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                    logger.error("nhan thuong loi " + this.name, e);
                    return;
                }

                rewardMoc = 500;

                // Đeo Lưng Trượng Huyết Nguyệt 12% HP,KI,SĐ 15% chí mạng
                item = new Item(2245); // Trượng Huyết Nguyệt
                item.quantity = 1;
                item.options.add(new ItemOption(77, 12)); // 12% HP
                item.options.add(new ItemOption(103, 12)); // 12% KI
                item.options.add(new ItemOption(50, 12)); // 12% SĐ
                item.options.add(new ItemOption(14, 15)); // 15% chí mạng
                addItem(item);

                // x4 Nước Ma Thuật
                item = new Item(2243); // Nước Ma Thuật
                item.quantity = 4;
                addItem(item);

                // Cải trang Bill Bé Nhỏ 30% HP,KI,SĐ 15% Sát thương laze,Tự Sát, SĐCM 3 ngày
                item = new Item(2244); // Bill Bé Nhỏ
                item.quantity = 1;
                item.options.add(new ItemOption(77, 30)); // 30% HP
                item.options.add(new ItemOption(103, 30)); // 30% KI
                item.options.add(new ItemOption(50, 30)); // 30% SĐ
                item.options.add(new ItemOption(197, 15)); // 15% Sát thương laze
                item.options.add(new ItemOption(196, 15)); // 15% Sát thương tự sát
                item.options.add(new ItemOption(5, 15)); // 15% SĐCM
                item.options.add(new ItemOption(93, 3)); // HSD 3 ngày
                addItem(item);

                this.service
                        .serverMessage("Bạn vừa nhận được phần thưởng từ mốc 500, hãy kiểm tra hành trang của mình");
                break;

            case 1000:
                if (numberVongQuay < 1000) {
                    this.service.serverMessage(
                            "Bạn còn thiếu " + (1000 - numberVongQuay) + " lượt để nhận thưởng mốc này\n");
                    return;
                }

                try {
                    GameRepository.getInstance().eventVQTD.setReward(this.id, 1000);
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                    logger.error("nhan thuong loi " + this.name, e);
                    return;
                }

                rewardMoc = 1000;

                // Danh hiệu Bất Bại 5% HP,KI,SĐ
                item = new Item(2260); // Danh hiệu Bất Bại
                item.quantity = 1;
                item.options.add(new ItemOption(77, 5)); // 5% HP
                item.options.add(new ItemOption(103, 5)); // 5% KI
                item.options.add(new ItemOption(50, 5)); // 5% SĐ
                addItem(item);

                // Ngọc Bội 5% HP,KI,SĐ
                item = new Item(2151); // Ngọc Bội
                item.quantity = 1;
                item.options.add(new ItemOption(77, 5)); // 5% HP
                item.options.add(new ItemOption(103, 5)); // 5% KI
                item.options.add(new ItemOption(50, 5)); // 5% SĐ
                addItem(item);

                // x5 Nước Ma Thuật
                item = new Item(2243); // Nước Ma Thuật
                item.quantity = 5;
                addItem(item);

                this.service
                        .serverMessage("Bạn vừa nhận được phần thưởng từ mốc 1000, hãy kiểm tra hành trang của mình");
                break;

            case 2000:
                if (numberVongQuay < 2000) {
                    this.service.serverMessage(
                            "Bạn còn thiếu " + (2000 - numberVongQuay) + " lượt để nhận thưởng mốc này\n");
                    return;
                }

                try {
                    GameRepository.getInstance().eventVQTD.setReward(this.id, 2000);
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                    logger.error("nhan thuong loi " + this.name, e);
                    return;
                }

                rewardMoc = 2000;

                // Thú cưỡi Cá chép thần kỳ 8% HP,KI,SĐ
                item = new Item(2256); // Cá Chép Thần Kỳ
                item.quantity = 1;
                item.options.add(new ItemOption(77, 8)); // 8% HP
                item.options.add(new ItemOption(103, 8)); // 8% KI
                item.options.add(new ItemOption(50, 8)); // 8% SĐ
                addItem(item);

                // Cải trang Saiyan Cuồng Nộ : 30% HP,KI,SĐ, 15% giáp
                item = new Item(2253); // Saiyan Cuồng Nộ
                item.quantity = 1;
                item.options.add(new ItemOption(77, 30)); // 30% HP
                item.options.add(new ItemOption(103, 30)); // 30% KI
                item.options.add(new ItemOption(50, 30)); // 30% SĐ
                item.options.add(new ItemOption(94, 15)); // 15% giáp
                addItem(item);

                this.service
                        .serverMessage("Bạn vừa nhận được phần thưởng từ mốc 2000, hãy kiểm tra hành trang của mình");
                break;

            case 3000:
                if (numberVongQuay < 3000) {
                    this.service.serverMessage(
                            "Bạn còn thiếu " + (3000 - numberVongQuay) + " lượt để nhận thưởng mốc này\n");
                    return;
                }

                try {
                    GameRepository.getInstance().eventVQTD.setReward(this.id, 3000);
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                    logger.error("nhan thuong loi " + this.name, e);
                    return;
                }

                rewardMoc = 3000;

                // Cải trang Thần Lửa Huỷ Diệt 30% HP,KI,SĐ, 15% Laze, Tự Sát, SĐCM
                item = new Item(2254); // Thần lửa hủy diệt
                item.quantity = 1;
                item.options.add(new ItemOption(77, 35)); // 30% HP
                item.options.add(new ItemOption(103, 35)); // 30% KI
                item.options.add(new ItemOption(50, 35)); // 30% SĐ
                item.options.add(new ItemOption(197, 20)); // 15% Sát thương laze
                item.options.add(new ItemOption(196, 20)); // 15% Sát thương tự sát
                item.options.add(new ItemOption(5, 20)); // 15% SĐCM
                addItem(item);

                // x10 nước ma thuật
                item = new Item(2243); // Nước Ma Thuật
                item.quantity = 10;
                addItem(item);

                this.service
                        .serverMessage("Bạn vừa nhận được phần thưởng từ mốc 3000, hãy kiểm tra hành trang của mình");
                break;

            case 5000:
                if (numberVongQuay < 5000) {
                    this.service.serverMessage(
                            "Bạn còn thiếu " + (5000 - numberVongQuay) + " lượt để nhận thưởng mốc này\n");
                    return;
                }

                try {
                    GameRepository.getInstance().eventVQTD.setReward(this.id, 5000);
                } catch (Exception e) {
                    com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                    logger.error("nhan thuong loi " + this.name, e);
                    return;
                }

                rewardMoc = 5000;

                // Linh thú 15% HP,KI,SĐ 3% Giáp, SĐCM, Laze, Sát thương tự sát
                item = new Item(2258); // Tiểu miêu linh
                item.quantity = 1;
                item.options.add(new ItemOption(77, 15)); // 15% HP
                item.options.add(new ItemOption(103, 15)); // 15% KI
                item.options.add(new ItemOption(50, 15)); // 15% SĐ
                item.options.add(new ItemOption(94, 3)); // 3% Giáp
                item.options.add(new ItemOption(5, 3)); // 3% SĐCM
                item.options.add(new ItemOption(197, 3)); // 3% Sát thương laze
                item.options.add(new ItemOption(196, 3)); // 3% Sát thương tự sát
                addItem(item);

                // x30 item cấp 2 (giả sử là vật phẩm tiêu thụ cấp 2)
                item = new Item(1021); // Giả sử ID vật phẩm tiêu thụ cấp 2 là 385
                item.quantity = 30;
                addItem(item);

                item = new Item(1022); // Giả sử ID vật phẩm tiêu thụ cấp 2 là 385
                item.quantity = 30;
                addItem(item);

                item = new Item(1023); // Giả sử ID vật phẩm tiêu thụ cấp 2 là 385
                item.quantity = 30;
                addItem(item);

                // x30 nước ma thuật
                item = new Item(2243); // Nước Ma Thuật
                item.quantity = 30;
                addItem(item);

                this.service
                        .serverMessage("Bạn vừa nhận được phần thưởng từ mốc 5000, hãy kiểm tra hành trang của mình");
                break;

            default:
                this.service.serverMessage("Bạn chưa đủ lượt quay để nhận thưởng");
                break;
        }

        this.saveData();
    }

    public void openDoHDRandom(int gender, int setkh) {
        Item item = null;
        int isDoTL = 0;
        int[] DO_TL_TD = {ItemName.AO_HUY_DIET_TD, ItemName.QUAN_HUY_DIET_TD, ItemName.GANG_HUY_DIET_TD,
            ItemName.GIAY_HUY_DIET_TD, ItemName.NHAN_HUY_DIET};
        int[] DO_TL_XD = {ItemName.AO_HUY_DIET_XD, ItemName.QUAN_HUY_DIET_XD, ItemName.GANG_HUY_DIET_XD,
            ItemName.GIAY_HUY_DIET_XD, ItemName.NHAN_HUY_DIET};
        int[] DO_TL_NM = {ItemName.AO_HUY_DIET_NM, ItemName.QUAN_HUY_DIET_NM, ItemName.GANG_HUY_DIET_NM,
            ItemName.GIAY_HUY_DIET_NM, ItemName.NHAN_HUY_DIET};

        switch (gender) {
            case 0:
                isDoTL = DO_TL_TD[Utils.nextInt(DO_TL_TD.length)];
                break;
            case 1:
                isDoTL = DO_TL_NM[Utils.nextInt(DO_TL_NM.length)];
                break;
            case 2:
                isDoTL = DO_TL_XD[Utils.nextInt(DO_TL_XD.length)];
                break;
            default:
                this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                break;
        }
        item = new Item(isDoTL);
        item.setDefaultOptions();
        if (setkh != -1) {
            item.addOptionSKH(setkh);
            item.addItemOption(new ItemOption(30, 0));
        }
        this.addItem(item);
        this.service.serverMessage("Bạn vừa nhận được " + item.template.name);

    }

    public void openTLRandom(int gender) {
        Item item = null;
        int[][][] options = {{{127, 139}, {128, 140}, {129, 141}},
        {{130, 195}, {131, 143}, {132, 144}}, {{133, 136}, {134, 137}, {135, 138}}};
        int[] DO_TL_TD = {555, 556, 562, 563, 561};
        int[] DO_TL_XD = {559, 560, 566, 567, 561};
        int[] DO_TL_NM = {557, 558, 564, 565, 561};
        int isDoTL = 0;
        switch (gender) {
            case 0:
                isDoTL = DO_TL_TD[Utils.nextInt(DO_TL_TD.length)];
                break;
            case 1:
                isDoTL = DO_TL_NM[Utils.nextInt(DO_TL_NM.length)];
                break;
            case 2:
                isDoTL = DO_TL_XD[Utils.nextInt(DO_TL_XD.length)];
                break;
            default:
                this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                break;
        }
        item = new Item(isDoTL);
        item.setDefaultOptions();
        int typeSKH = Utils.nextInt(100);
        if (typeSKH <= 33) {
            typeSKH = 0;
        } else if (typeSKH <= 66) {
            typeSKH = 1;
        } else {
            typeSKH = 2;
        }
        item.addItemOption(
                new ItemOption(options[gender][typeSKH][0], 0));
        item.addItemOption(
                new ItemOption(options[gender][typeSKH][1], 0));
        item.addItemOption(
                new ItemOption(30, 0));
        addItem(item);
        this.service.serverMessage("Bạn vừa nhận được " + item.template.name);

    }

    public void openSKHRandom(int gender) {
        Item item = null;
        int[][][] options = {{{127, 139}, {128, 140}, {129, 141}},
        {{130, 195}, {131, 143}, {132, 144}}, {{133, 136}, {134, 137}, {135, 138}}};
        int[] DO_TL_TD = {0, 6, 21, 27, 12};
        int[] DO_TL_NM = {1, 7, 22, 28, 12};
        int[] DO_TL_XD = {2, 8, 23, 29, 12};
        int isDoTL = 0;
        switch (gender) {
            case 0:
                isDoTL = DO_TL_TD[Utils.nextInt(DO_TL_TD.length)];
                break;
            case 1:
                isDoTL = DO_TL_NM[Utils.nextInt(DO_TL_NM.length)];
                break;
            case 2:
                isDoTL = DO_TL_XD[Utils.nextInt(DO_TL_XD.length)];
                break;
            default:
                this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                break;
        }
        int typeSKH = Utils.nextInt(100);
        if (typeSKH <= 33) {
            typeSKH = 0;
        } else if (typeSKH <= 66) {
            typeSKH = 1;
        } else {
            typeSKH = 2;
        }
        item = new Item(isDoTL);

        item.setDefaultOptions();

        item.addItemOption(
                new ItemOption(options[gender][typeSKH][0], 0));
        item.addItemOption(
                new ItemOption(options[gender][typeSKH][1], 0));
        item.addItemOption(
                new ItemOption(30, 0));
        addItem(item);

        this.service.serverMessage(
                "Bạn vừa nhận được " + item.template.name);
    }

    public void openDoTLKHRandom(int gender) {
        Item item = null;
        int[][][] options = {{{127, 139}, {128, 140}, {129, 141}},
        {{130, 195}, {131, 143}, {132, 144}}, {{133, 136}, {134, 137}, {135, 138}}};
        int[] DO_TL_TD = {555, 556, 562, 563, 561};
        int[] DO_TL_XD = {559, 560, 566, 567, 561};
        int[] DO_TL_NM = {557, 558, 564, 565, 561};
        int isDoTL = 0;
        switch (gender) {
            case 0:
                isDoTL = DO_TL_TD[Utils.nextInt(DO_TL_TD.length)];
                break;
            case 1:
                isDoTL = DO_TL_NM[Utils.nextInt(DO_TL_NM.length)];
                break;
            case 2:
                isDoTL = DO_TL_XD[Utils.nextInt(DO_TL_XD.length)];
                break;
            default:
                this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                break;
        }
        int typeSKH = Utils.nextInt(100);
        if (typeSKH <= 33) {
            typeSKH = 0;
        } else if (typeSKH <= 66) {
            typeSKH = 1;
        } else {
            typeSKH = 2;
        }
        item = new Item(isDoTL);
        item.setDefaultOptions();
        item.addItemOption(new ItemOption(options[gender][typeSKH][0], 0));
        item.addItemOption(new ItemOption(options[gender][typeSKH][1], 0));
        item.addItemOption(new ItemOption(30, 0));
        addItem(item);
        removeItem(getItemInBag(ItemName.HOP_QUA_THAN_LINH).indexUI, 1);
        this.service.serverMessage("Bạn vừa nhận được " + item.template.name);
    }

    public void openDoCuoiKHRandom(int gender) {
        Item item = null;
        int[][][] options = {{{127, 139}, {128, 140}, {129, 141}},
        {{130, 195}, {131, 143}, {132, 144}}, {{133, 136}, {134, 137}, {135, 138}}};
        int[] DO_TL_TD = {233, 245, 257, 269, 281};
        int[] DO_TL_XD = {241, 253, 265, 277, 281};
        int[] DO_TL_NM = {237, 249, 261, 273, 281};
        int isDoTL = 0;
        switch (gender) {
            case 0:
                isDoTL = DO_TL_TD[Utils.nextInt(DO_TL_TD.length)];
                break;
            case 1:
                isDoTL = DO_TL_NM[Utils.nextInt(DO_TL_NM.length)];
                break;
            case 2:
                isDoTL = DO_TL_XD[Utils.nextInt(DO_TL_XD.length)];
                break;
            default:
                this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
                break;
        }
        int typeSKH = Utils.nextInt(100);
        if (typeSKH <= 33) {
            typeSKH = 0;
        } else if (typeSKH <= 66) {
            typeSKH = 1;
        } else {
            typeSKH = 2;
        }
        item = new Item(isDoTL);

        item.setDefaultOptions();

        item.addItemOption(
                new ItemOption(options[gender][typeSKH][0], 0));
        item.addItemOption(
                new ItemOption(options[gender][typeSKH][1], 0));
        item.addItemOption(
                new ItemOption(30, 0));
        addItem(item);

        this.service.serverMessage(
                "Bạn vừa nhận được " + item.template.name);
    }

    public void openSetHD(int gender, boolean isSetKH, int idSKH) {
        if (getSlotNullInBag() < 5) {
            service.serverMessage("Hành trang đã đầy, bạn cần ít nhất 5 ô trống");
            return;
        }
        Item it;
        switch (gender) {
            case 0:
                it = new Item(ItemName.AO_HUY_DIET_TD);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                it = new Item(ItemName.QUAN_HUY_DIET_TD);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GANG_HUY_DIET_TD);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GIAY_HUY_DIET_TD);
                it.isLock = false;
                it.setDefaultOptions();
                addItem(it);
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                break;
            case 1:
                it = new Item(ItemName.AO_HUY_DIET_NM);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.QUAN_HUY_DIET_NM);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GANG_HUY_DIET_NM);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GIAY_HUY_DIET_NM);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                break;
            case 2:
                it = new Item(ItemName.AO_HUY_DIET_XD);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                it = new Item(ItemName.QUAN_HUY_DIET_XD);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GANG_HUY_DIET_XD);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                it = new Item(ItemName.GIAY_HUY_DIET_XD);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                break;
            default:
                break;
        }
        it = new Item(ItemName.NHAN_HUY_DIET);
        it.isLock = false;
        it.setDefaultOptions();
        if (isSetKH && idSKH > 0) {
            it.addOptionSKH(idSKH);
            it.addItemOption(new ItemOption(30, 0));
        }
        addItem(it);
        service.serverMessage("Bạn đã nhận được set thần linh tương ứng, hãy kiểm tra lại hành trang");

    }

    public void openSetTL(int gender, boolean isSetKH, int idSKH) {
        if (getSlotNullInBag() < 5) {
            service.serverMessage("Hành trang đã đầy, bạn cần ít nhất 5 ô trống");
            return;
        }
        Item it;
        switch (gender) {
            case 0:
                it = new Item(ItemName.AO_THAN_LINH);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                it = new Item(ItemName.QUAN_THAN_LINH);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GANG_THAN_LINH);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GIAY_THAN_LINH);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                addItem(it);
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                break;
            case 1:
                it = new Item(ItemName.AO_THAN_NAMEC);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.QUAN_THAN_NAMEC);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                it.quantity = 1;
                addItem(it);

                it = new Item(ItemName.GANG_THAN_NAMEC);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GIAY_THAN_NAMEC);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                break;
            case 2:
                it = new Item(ItemName.AO_THAN_XAYDA);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.QUAN_THAN_XAYDA);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GANG_THAN_XAYDA);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);

                it = new Item(ItemName.GIAY_THAN_XAYDA);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                break;
            default:
                break;
        }
        it = new Item(ItemName.NHAN_THAN_LINH);
        it.isLock = false;
        ;
        it.setDefaultOptions();
        it.quantity = 1;
        if (isSetKH && idSKH > 0) {
            it.addOptionSKH(idSKH);
            it.addItemOption(new ItemOption(30, 0));
        }
        addItem(it);
        service.serverMessage("Bạn đã nhận được set thần linh tương ứng, hãy kiểm tra lại hành trang");
    }

    public void selectOptionCaiTrangTop5000(int optionId) {
        Item item = null;
        try {
            GameRepository.getInstance().eventVQTD.setReward(this.id, 5000);
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 7");
            this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để được hỗ trợ");
            logger.error("nhan thuong loi " + this.name);
            e.printStackTrace();
            return;
        }
        rewardMoc = 5000;
        item = new Item(ItemName.CAI_TRANG_SUPER_VEGETA);
        item.setDefaultOptions();
        if (optionId == 5) {
            item.addItemOption(new ItemOption(optionId, 10));
        } else {
            item.addItemOption(new ItemOption(optionId, 15));
        }
        item.quantity = 1;
        addItem(item);
        this.service.serverMessage("Bạn vừa nhận được phần thưởng từ mốc 5000, hãy kiểm tra hành trang của mình");
    }

    public int getQuantityThoiVang() {
        Item item = null;
        int index = this.getIndexBagById(ItemName.THOI_VANG);
        if (index < 0) {
            service.serverMessage("Không tìm thấy thỏi vàng trong hành trang");
            return 0;
        }
        if (index < this.itemBag.length) {
            item = this.itemBag[index];
        }
        return item.quantity;
    }

    public void getRewardTopVQTD(List<TopInfo> top) {
        // TopInfo ranking = null;
        // for (TopInfo pl : top) {
        // if (pl.playerID == this.id) {
        // ranking = pl;
        // break;
        // }
        // }
        // if (ranking == null || ranking.rank < 1 || ranking.rank > 50) {
        // this.service.serverMessage("Bạn không nằm trong danh sách trao giải");
        // return;
        // }
        // if (this.getSlotNullInBag() < 3) {
        // service.serverMessage2(Language.ME_BAG_FULL);
        // return;
        // }
        // Optional<RewardVQTDData> rewardVQTDDataOptional;
        // try {
        // rewardVQTDDataOptional =
        // GameRepository.getInstance().rewardVQTDRepository.findFirstByName(ranking.playerID);
        // } catch (Exception e) { com.ngocrong.NQMP.UtilsNQMP.logError(e);
        // e.printStackTrace();
        // this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để
        // được hỗ trợ");
        // return;
        // }
        // RewardVQTDData rewardVQTDData = null;
        // if (rewardVQTDDataOptional.isPresent()) {
        // rewardVQTDData = rewardVQTDDataOptional.get();
        // } else {
        // this.service.serverMessage("Bạn không nằm trong danh sách trao giải");
        // return;
        // }
        // if (rewardVQTDData != null && rewardVQTDData.reward != 0) {
        // this.service.serverMessage("Bạn đã nhận phần thưởng này rồi");
        // return;
        // }
        // try {
        // GameRepository.getInstance().rewardVQTDRepository.setReward(ranking.playerID,
        // 1);
        // } catch (Exception e) { com.ngocrong.NQMP.UtilsNQMP.logError(e);
        // this.service.serverMessage("Có lỗi khi nhận quà, vui lòng liên hệ admin để
        // được hỗ trợ");
        // logger.error("nhan thuong loi " + ranking.name);
        // e.printStackTrace();
        // return;
        // }
        //
        // Item caiTrang = new Item(ItemName.CAI_TRANG_TOP_VQTD);
        // Item danhHieu = new Item(ItemName.DANH_HIEU_DE_TU_THUONG_DE);
        // Item thuCuoi = new Item(ItemName.RUA_BAY);
        // Item buaDien = new Item(ItemName.DEO_LUNG_BUA_DIEN);
        // caiTrang.quantity = 1;
        // danhHieu.quantity = 1;
        // thuCuoi.quantity = 1;
        // Item dbv = new Item(ItemName.DA_BAO_VE_987);
        //
        // if (ranking.rank == 1) {
        // danhHieu.options.add(new ItemOption(50, 7));
        // danhHieu.options.add(new ItemOption(77, 7));
        // danhHieu.options.add(new ItemOption(103, 7));
        // danhHieu.options.add(new ItemOption(14, 7));
        // addItem(danhHieu);
        //
        // caiTrang.options.add(new ItemOption(50, 45));
        // caiTrang.options.add(new ItemOption(77, 45));
        // caiTrang.options.add(new ItemOption(103, 45));
        // caiTrang.options.add(new ItemOption(14, 15));
        // addItem(caiTrang);
        //
        // thuCuoi.options.add(new ItemOption(50, 6));
        // thuCuoi.options.add(new ItemOption(77, 6));
        // thuCuoi.options.add(new ItemOption(103, 6));
        // thuCuoi.options.add(new ItemOption(5, 2));
        // addItem(thuCuoi);
        //
        // dbv.quantity = 99;
        // addItem(dbv);
        // } else if (ranking.rank == 2) {
        // danhHieu.options.add(new ItemOption(50, 6));
        // danhHieu.options.add(new ItemOption(77, 6));
        // danhHieu.options.add(new ItemOption(103, 6));
        // danhHieu.options.add(new ItemOption(14, 6));
        // addItem(danhHieu);
        //
        // caiTrang.options.add(new ItemOption(50, 45));
        // caiTrang.options.add(new ItemOption(77, 45));
        // caiTrang.options.add(new ItemOption(103, 45));
        // caiTrang.options.add(new ItemOption(14, 15));
        // addItem(caiTrang);
        //
        // thuCuoi.options.add(new ItemOption(50, 6));
        // thuCuoi.options.add(new ItemOption(77, 6));
        // thuCuoi.options.add(new ItemOption(103, 6));
        // thuCuoi.options.add(new ItemOption(5, 2));
        // addItem(thuCuoi);
        //
        // dbv.quantity = 50;
        // addItem(dbv);
        // } else if (ranking.rank == 3) {
        // danhHieu.options.add(new ItemOption(50, 6));
        // danhHieu.options.add(new ItemOption(77, 6));
        // danhHieu.options.add(new ItemOption(103, 6));
        // danhHieu.options.add(new ItemOption(14, 6));
        // addItem(danhHieu);
        //
        // caiTrang.options.add(new ItemOption(50, 45));
        // caiTrang.options.add(new ItemOption(77, 45));
        // caiTrang.options.add(new ItemOption(103, 45));
        // caiTrang.options.add(new ItemOption(14, 15));
        // addItem(caiTrang);
        //
        // thuCuoi.options.add(new ItemOption(50, 6));
        // thuCuoi.options.add(new ItemOption(77, 6));
        // thuCuoi.options.add(new ItemOption(103, 6));
        // thuCuoi.options.add(new ItemOption(5, 2));
        // addItem(thuCuoi);
        //
        // dbv.quantity = 30;
        // addItem(dbv);
        // } else if (ranking.rank >= 4 && ranking.rank <= 10) {
        // caiTrang.options.add(new ItemOption(50, 40));
        // caiTrang.options.add(new ItemOption(77, 40));
        // caiTrang.options.add(new ItemOption(103, 40));
        // caiTrang.options.add(new ItemOption(14, 15));
        // addItem(caiTrang);
        //
        // buaDien.options.add(new ItemOption(50, 18));
        // buaDien.options.add(new ItemOption(77, 18));
        // buaDien.options.add(new ItemOption(103, 18));
        // buaDien.options.add(new ItemOption(14, 18));
        // addItem(buaDien);
        //
        // } else if (11 <= ranking.rank && ranking.rank <= 20) {
        // buaDien.options.add(new ItemOption(50, 18));
        // buaDien.options.add(new ItemOption(77, 18));
        // buaDien.options.add(new ItemOption(103, 18));
        // buaDien.options.add(new ItemOption(14, 18));
        // buaDien.options.add(new ItemOption(93, 90));
        // addItem(buaDien);
        //
        // thuCuoi.options.add(new ItemOption(50, 6));
        // thuCuoi.options.add(new ItemOption(77, 6));
        // thuCuoi.options.add(new ItemOption(103, 6));
        // thuCuoi.options.add(new ItemOption(5, 2));
        // thuCuoi.options.add(new ItemOption(93, 90));
        // addItem(thuCuoi);
        //
        // } else if (21 <= ranking.rank && ranking.rank <= 50) {
        // buaDien.options.add(new ItemOption(50, 18));
        // buaDien.options.add(new ItemOption(77, 18));
        // buaDien.options.add(new ItemOption(103, 18));
        // buaDien.options.add(new ItemOption(14, 18));
        // buaDien.options.add(new ItemOption(93, 60));
        // addItem(buaDien);
        //
        // thuCuoi.options.add(new ItemOption(50, 6));
        // thuCuoi.options.add(new ItemOption(77, 6));
        // thuCuoi.options.add(new ItemOption(103, 6));
        // thuCuoi.options.add(new ItemOption(5, 2));
        // thuCuoi.options.add(new ItemOption(93, 60));
        // addItem(thuCuoi);
        // }
        // this.service.serverMessage("Bạn đã nhận được phần thưởng từ BXH, hãy kiểm tra
        // hành trang của mình");
        // this.saveData();
    }

    // public boolean FindDanhhieu(int tempId) {
    // for (Item item : this.danhHieu) {
    // if (item != null && item.template.id == tempId) {
    // return true;
    // }
    // }
    // return false;
    // }
    public void AddDanhHieu(Item iddanhhieu) {
        loadBody();
    }

    public static int[] GetImgDanhHieu(Item item) {
        if (item == null) {
            return new int[]{-1, 0};
        }
        switch (item.template.id) {
            case ItemName.DANH_HIEU_THAN_HO_VE: {
                return new int[]{31259, 6};
            }
            case ItemName.DANH_HIEU_THAN_LONG: {
                return new int[]{31260, 6};
            }
            case ItemName.DANH_HIEU_BE_NGOAN: {
                return new int[]{31262, 6};
            }
            case ItemName.DANH_HIEU_PHONG_BA: {
                return new int[]{31264, 6};
            }
            case ItemName.DANH_HIEU_FAN_CUNG: {
                return new int[]{15510, 6};
            }
            case ItemName.DANH_HIEU_TOP_1_DAI_GIA: {
                return new int[]{32227, 6};
            }
            case ItemName.DANH_HIEU_TOP_2_DAI_GIA: {
                return new int[]{32229, 6};
            }
            case ItemName.DANH_HIEU_TOP_3_DAI_GIA: {
                return new int[]{32231, 6};
            }
            case ItemName.DANH_HIEU_TOP_1_NHIEM_VU: {
                return new int[]{32233, 6};
            }
            case ItemName.DANH_HIEU_TOP_2_NHIEM_VU: {
                return new int[]{32235, 6};
            }
            case ItemName.DANH_HIEU_TOP_3_NHIEM_VU: {
                return new int[]{32237, 6};
            }
            case ItemName.DANH_HIEU_TOP_1_SUC_MANH: {
                return new int[]{32239, 6};
            }
            case ItemName.DANH_HIEU_TOP_2_SUC_MANH: {
                return new int[]{32241, 6};
            }
            case ItemName.DANH_HIEU_TOP_3_SUC_MANH: {
                return new int[]{32243, 6};
            }
            case ItemName.DANH_HIEU_TOP_1_VONG_QUAY: {
                return new int[]{32245, 6};
            }
            case ItemName.DANH_HIEU_TOP_2_VONG_QUAY: {
                return new int[]{32247, 6};
            }
            case ItemName.DANH_HIEU_TOP_3_VONG_QUAY: {
                return new int[]{32249, 6};
            }
            case 2198: {
                return new int[]{16661, 6};
            }
            case 2260: {
                return new int[]{9138, 6};
            }
        }
        return new int[]{-1, 0};
    }

    public void uocRongThienMenh(int type) {
        if (this.isBagFull() && (type == 3 || type == 6)) {
            this.service.serverMessage(Language.ME_BAG_FULL);
            return;
        }
        ItemTime item = null;
        Item it = null;
        switch (type) {
            case 1:
                isUocThienMenh1 = true;
                item = new ItemTime(ItemTimeName.UOC_THIEN_MENH_1, 15256, 1200, true);
                break;
            case 2:
                isUocThienMenh2 = true;
                item = new ItemTime(ItemTimeName.UOC_THIEN_MENH_2, 15257, 1200, true);
                break;
            case 3:
                it = new Item(ItemName.DA_BAO_VE_987);
                it.quantity = Utils.nextInt(1, 3);
                break;
            case 4:
                isUocThienMenh4 = true;
                item = new ItemTime(ItemTimeName.UOC_THIEN_MENH_4, 15259, 1200, true);
                break;
            case 5:
                if (this.myDisciple == null) {
                    this.service.sendThongBao("Bạn chưa có đệ tử");
                    return;
                }
                isUocThienMenh5 = true;
                item = new ItemTime(ItemTimeName.UOC_THIEN_MENH_5, 15260, 1200, true);
                break;
            case 6:
                it = new Item(ItemName.MANH_DOI_TRUONG_VANG_956);
                it.quantity = 1;
                break;
            default:
                this.service.sendThongBao("Có lỗi xảy ra, vui lòng thử lại sau");
        }
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_1_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_2_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_3_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_4_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_5_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_6_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_THIEN_MENH_7_SAO), 1);
        if (type == 3 || type == 6) {
            addItem(it);
            this.service.sendThongBao("Bạn vừa nhận được x " + it.quantity + " " + it.template.name);
        } else {
            addItemTime(item);
            if (myDisciple != null) {
                myDisciple.info.setInfo();
            }
            info.setInfo();
            service.loadPoint();
            this.service.sendThongBao("Điều ước của ngươi đã thành hiện thực.");
        }
    }

    public void uocRongLocPhat(int type) {
        ItemTime item = null;
        switch (type) {
            case 1:
                isUocLocPhat1 = true;
                item = new ItemTime(ItemTimeName.UOC_LOC_PHAT_1, 8579, 600, true);
                break;
            case 2:
                isUocLocPhat2 = true;
                item = new ItemTime(ItemTimeName.UOC_LOC_PHAT_2, 8580, 600, true);
                break;
            case 3:
                isUocLocPhat3 = true;
                item = new ItemTime(ItemTimeName.UOC_LOC_PHAT_3, 8581, 600, true);
                break;
            case 4:
                if (this.myDisciple == null) {
                    this.service.sendThongBao("Bạn chưa có đệ tử");
                    return;
                }
                if (this.myDisciple.skillOpened < 3) {
                    this.service.sendThongBao("Đệ tử của bạn chưa mở khóa kĩ năng này");
                    return;
                }
                this.myDisciple.changeSkillIndex(3);
                if (this.myDisciple.skillOpened < 4) {
                    this.service.sendThongBao("Đệ tử của bạn chưa mở khóa kĩ năng này");
                    return;
                }
                this.myDisciple.changeSkillIndex(4);
                break;
            case 5:
                if (this.myDisciple == null) {
                    this.service.sendThongBao("Bạn chưa có đệ tử");
                    return;
                }
                if (this.myDisciple.skillOpened < 2) {
                    this.service.sendThongBao("Đệ tử của bạn chưa mở khóa kĩ năng này");
                    return;
                }
                this.myDisciple.changeSkillIndex(2);
                break;
            case 6:
                break;
            case 7:
                if (this.myDisciple == null) {
                    this.service.sendThongBao("Bạn chưa có đệ tử");
                    return;
                }
                isUocLocPhat7 = true;
                item = new ItemTime(ItemTimeName.UOC_LOC_PHAT_7, 8585, 600, true);
                break;
            default:
                this.service.sendThongBao("Có lỗi xảy ra, vui lòng thử lại sau");
        }
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_1_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_2_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_3_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_4_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_5_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_6_SAO), 1);
        this.removeItem(getIndexBagById(ItemName.NGOC_RONG_LOC_PHAT_7_SAO), 1);
        if (type != 6) {
            addItemTime(item);
            if (myDisciple != null) {
                myDisciple.info.setInfo();
            }

            info.setInfo();
            service.loadPoint();
            this.service.sendThongBao("Điều ước của ngươi đã thành hiện thực.");
        }
    }

    public void sendSkillShortCut() {
        Message msg;
        try {
            msg = this.service.messageSubCommand((byte) 61);
            msg.writer().writeUTF("KSkill");
            msg.writer().writeInt(shortcut.length);
            msg.writer().write(shortcut);
            this.service.sendMessage(msg);
            msg.cleanup();
            msg = this.service.messageSubCommand((byte) 61);
            msg.writer().writeUTF("OSkill");
            msg.writer().writeInt(shortcut.length);
            msg.writer().write(shortcut);
            this.service.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 6");
            e.printStackTrace();
        }
    }

    public String getRewardEventTet() {
        return String.format("[%d,%d,%d]", pointHoaSumVay, pointHoaSacMau, pointThoiVang);
    }

    public void useMTS(Item item) {
        if (item == null) {
            return;
        }
        if (item.template.id < 2046 || item.template.id > 2060) {
            service.sendThongBao("Vật phẩm không hợp lệ");
            return;
        }
        if (item.quantity < 10) {
            service.sendThongBao("Cần có x10 Mảnh thiên sứ");
            return;
        }
        Item itemCreate = new Item(item.template.id + 114);
        itemCreate.setDefaultOptions();
        int[] optionBonus = new int[]{77, 103, 50, item.template.gender == 2 ? 94 : 5};
        itemCreate.addItemOption(new ItemOption(optionBonus[Utils.nextInt(optionBonus.length)], Utils.nextInt(1, 5)));
        itemCreate.addItemOption(new ItemOption(30, 0));
        itemCreate.quantity = 1;
        if (addItem(itemCreate)) {
            this.removeItem(item.indexUI, 10);
            service.sendThongBao("Bạn nhận được " + itemCreate.template.name);
        } else {
            service.sendThongBao("Hành trang không đủ ô trống");
        }

    }

    public void HopQuaTanThu(Item item) {
        if (item == null || item.template.id != 2175) {
            return;
        }
        menus.clear();
        menus.add(new KeyValue(1135, "Set Tân Thủ Trái Đất"));
        menus.add(new KeyValue(1136, "Set Tân Thủ Namec"));
        menus.add(new KeyValue(1137, "Set Tân Thủ Xayda"));
        menus.add(new KeyValue(CMDMenu.CANCEL, "Đóng"));
        service.openUIConfirm(NpcName.CON_MEO, "Sau khi chọn hành tinh\n"
                + "Bạn sẽ nhận được 1 set đồ tân thủ 4s TNSM tương ứng với hành tinh đó", getPetAvatar(), menus);

    }

    public void getDoTanThu(int gender) {
        int[][] items = new int[][]{
            {0, 6, 21, 27, 12},
            {1, 7, 22, 28, 12},
            {2, 8, 23, 29, 12}
        };
        if (getCountEmptyBag() < 5) {
            service.sendThongBao("Hành trang không đủ ô trống , cần 5 ô trống");
            return;
        }
        Item itemFind = this.getItemInBag(2175);
        if (itemFind == null) {
            service.sendThongBao("Không tìm thấy hộp quà tân thủ");
            return;
        }
        removeItem(itemFind.indexUI, 1);
        for (int i = 0; i < items[gender].length; i++) {
            Item item = new Item(items[gender][i]);
            item.setDefaultOptions();
            item.options.add(new ItemOption(102, 4));
            item.options.add(new ItemOption(107, 4));
            item.options.add(new ItemOption(101, 20));
            item.options.add(new ItemOption(30, 0));
            this.addItem(item);
            service.sendThongBao("Bạn nhận được " + item.template.name);
        }
    }

    public void SKIPNV(Item item) {
        if (item == null || item.template.id != 2176) {
            service.sendThongBao("Có lỗi xãy ra");
            return;
        }
        if (this.taskMain != null) {
            if (this.taskMain.id >= 16) {
                service.sendThongBao("Bạn không thể bỏ qua nhiệm vụ này");
                return;
            }
            int next = taskMain.id + 1;
            if (next == 4 || next == 5) {
                next = 6;
            }
            this.removeItem(item.indexUI, 1);
            this.updateTask(next);
            service.sendThongBao("Bạn đã bỏ qua nhiệm vụ thành công");
        }
    }

    public void saveHistory(int numberGold, String note) {
        hgb = new HistoryGoldBar();
        hgb.setUserName(this.session.user.getUsername());
        hgb.setPlayerName(this.name);
        hgb.setGoldBefore(this.getQuantityThoiVang());
        hgb.setGoldAfter(this.getQuantityThoiVang() + numberGold);
        hgb.setNumberGold(numberGold);
        hgb.setCreateDate(Instant.now());
        hgb.setNote(note);
        GameRepository.getInstance().historyGoldBar.save(hgb);
    }

    public void openSetDoCuoi(int gender, boolean isSetKH, int idSKH) {
        if (getSlotNullInBag() < 5) {
            service.serverMessage("Hành trang đã đầy, bạn cần ít nhất 5 ô trống");
            return;
        }
        Item it;
        switch (gender) {
            case 0:
                it = new Item(ItemName.AO_JEAN_CALIC);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);

                it = new Item(ItemName.QUAN_JEAN_CALIC);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);

                it = new Item(ItemName.GANG_JEAN_CALIC);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);

                it = new Item(ItemName.GIAY_JEAN_CALIC);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);
                break;
            case 1:
                it = new Item(ItemName.AO_VANG_ZEALOT);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);

                it = new Item(ItemName.QUAN_VANG_ZEALOT);
                it.isLock = false;
                it.setDefaultOptions();
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                it.quantity = 1;
                addItem(it);

                it = new Item(ItemName.GANG_VANG_ZEALOT);
                it.isLock = false;
                ;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);
                it = new Item(ItemName.GIAY_VANG_ZEALOT);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);
                break;
            case 2:
                it = new Item(ItemName.AO_LUONG_LONG);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);

                it = new Item(ItemName.QUAN_LUONG_LONG);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);

                it = new Item(ItemName.GANG_LUONG_LONG);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }

                addItem(it);

                it = new Item(ItemName.GIAY_LUONG_LONG);
                it.isLock = false;
                it.setDefaultOptions();
                it.quantity = 1;
                if (isSetKH && idSKH > 0) {
                    it.addOptionSKH(idSKH);
                    it.addItemOption(new ItemOption(30, 0));
                }
                addItem(it);
                break;
            default:
                break;
        }
        it = new Item(ItemName.RADA_CAP_12);
        it.isLock = false;
        ;
        it.setDefaultOptions();
        it.quantity = 1;
        if (isSetKH && idSKH > 0) {
            it.addOptionSKH(idSKH);
            it.addItemOption(new ItemOption(30, 0));
        }
        addItem(it);
        service.serverMessage("Bạn đã nhận được set đồ cuối tương ứng, hãy kiểm tra lại hành trang");
    }

    public int currentLevelBossWhis;

    public void joinMap(int mapid) {
        if (!checkCanEnter(mapid)) {
            return;
        }
        var map3 = MapManager.getInstance().getMap(mapid);
        if (map3 != null) {
            int zoneId2 = map3.randomZoneID();
            Zone zone2 = map3.getZoneByID(zoneId2);
            if (zone2 != null) {
                zone.leave(this);
                this.x = 1018;
                this.y = 264;
                if (map3.isMapSingle()) {
                    enterMapSingle(map3);
                } else {
                    int zoneId = map3.getZoneID();
                    map3.enterZone(this, zoneId);
                }
            }
        }
    }

    public void joinMap(int mapid, boolean checkMTV) {
        if (this.getSession().user.getActivated() == 0 && checkMTV) {
            service.sendThongBao("Bạn cần kích hoạt tài khoản để có thể vào map này");
            return;
        }
        var map3 = MapManager.getInstance().getMap(mapid);
        if (map3 != null) {
            int zoneId2 = map3.randomZoneID();
            Zone zone2 = map3.getZoneByID(zoneId2);
            if (zone2 != null) {
                zone.leave(this);
                this.x = 1018;
                this.y = 264;
                if (map3.isMapSingle()) {
                    enterMapSingle(map3);
                } else {
                    int zoneId = map3.getZoneID();
                    map3.enterZone(this, zoneId);
                }
            }
        }
    }

    public void updateLevelWhis() {
        if (this.currentLevelBossWhis > 0) {
            Optional<WhisData> whisData = GameRepository.getInstance().whisDataRepository.findByPlayerId(this.id);
            if (whisData.isPresent()) {
                WhisData data = whisData.get();
                if (currentLevelBossWhis > data.getCurrentLevel()) {
                    data.setCurrentLevel(currentLevelBossWhis);
                    GameRepository.getInstance().whisDataRepository.save(data);
                }
            } else {
                WhisData data = new WhisData();
                data.setPlayerId(id);
                data.setCurrentLevel(currentLevelBossWhis);
                data.setCreateDate(Instant.now());
                GameRepository.getInstance().whisDataRepository.save(data);
            }
            Top topWhis = Top.getTop(Top.TOP_WHIS);
            assert topWhis != null;
            topWhis.load();
        }
    }

}
