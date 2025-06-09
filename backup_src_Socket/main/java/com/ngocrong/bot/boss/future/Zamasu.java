//package com.ngocrong.bot.boss.future;
//
//import com.ngocrong.bot.Boss;
//import com.ngocrong.consts.ItemName;
//import com.ngocrong.consts.MapName;
//import com.ngocrong.item.Item;
//import com.ngocrong.item.ItemMap;
//import com.ngocrong.item.ItemOption;
//import com.ngocrong.map.tzone.Zone;
//import com.ngocrong.server.SessionManager;
//import com.ngocrong.skill.SkillName;
//import com.ngocrong.skill.Skills;
//import com.ngocrong.user.Player;
//import com.ngocrong.user.Info;
//import com.ngocrong.util.Utils;
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//
//public class Zamasu extends Boss {
//    private final static Logger logger = Logger.getLogger(Zamasu.class);
//
//    public Zamasu() {
//        super();
//        this.limit = 1000;
//        this.name = "Zamasu";
//        setInfo(200000000000L, 1000000, 5000000, 2000000, 50);
//        info.options[201] = 50;
//        info.options[202] = 45;
//        point = 5;
//        setDefaultPart();
//        setTypePK((byte) 5);
//    }
//
//    @Override
//    public void setInfo(long hp, long mp, long dame, int def, int crit) {
//        info.originalHP = hp;
//        info.originalMP = mp;
//        info.originalDamage = dame;
//        info.originalDefense = def;
//        info.originalCritical = crit;
//        info.setInfo();
//        info.recovery(Info.ALL, 100, false);
//        info.percentMiss = 10;
//    }
//
//    @Override
//    public void initSkill() {
//        try {
//            skills = new ArrayList<>();
//            skills.add(Skills.getSkill((byte) SkillName.CHIEU_DAM_GALICK, (byte) 7).clone());
//            skills.add(Skills.getSkill((byte) SkillName.CHIEU_KAMEJOKO, (byte) 7).clone());
//            skills.add(Skills.getSkill((byte) SkillName.TAI_TAO_NANG_LUONG, (byte) 7).clone());
//            skills.add(Skills.getSkill((byte) SkillName.THAI_DUONG_HA_SAN, (byte) 7).clone());
//            //skills.add(Skills.getSkill((byte) SkillName.THOI_MIEN, (byte) 7).clone());
//            skills.add(Skills.getSkill((byte) SkillName.KHIEN_NANG_LUONG, (byte) 7).clone());
//        } catch (Exception ex) { com.ngocrong.NQMP.UtilsNQMP.logError(ex);
//        }
//    }
//
//    @Override
//    public void killed(Object obj) {
//        super.killed(obj);
//        if (obj == null) {
//            return;
//        }
//        Player killer = (Player) obj;
//        if (killer.taskMain != null && killer.taskMain.id == 39 && killer.taskMain.index == 5) {
//            killer.updateTaskCount(1);
//        }
//    }
//
//    @Override
//    public void sendNotificationWhenAppear(String map) {
//        SessionManager.chatVip(String.format("BOSS %s vừa xuất hiện tại %s", this.name, map));
//        logger.debug(String.format("BOSS %s vừa xuất hiện tại %s khu vực %d", this.name, map, zone.zoneID));
//
//    }
//
//    @Override
//    public void sendNotificationWhenDead(String name) {
//        SessionManager.chatVip(String.format("%s: Đã tiêu diệt được %s mọi người đều ngưỡng mộ.", name, this.name));
//    }
//
//    @Override
//    public void setDefaultLeg() {
//        setLeg((short) 905);
//    }
//
//    @Override
//    public void setDefaultBody() {
//        setBody((short) 904);
//    }
//
//    @Override
//    public void setDefaultHead() {
//        setHead((short) 903);
//    }
//
//    @Override
//    public void startDie() {
//        Zone z = zone;
//        super.startDie();
//        int[] mapIDs = new int[]{MapName.THANH_PHO_1, MapName.THANH_PHO_2, MapName.THANH_PHO_3};
//        Utils.setTimeout(() -> {
//            BlackWhite blackWhite = new BlackWhite(false);
//            blackWhite.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
//        }, 600000);
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        if (info.options[201] == 0) {
//            info.options[201] = 50;
//        }
//        if (info.options[202] == 0) {
//            info.options[202] = 45;
//        }
//    }
//
//    @Override
//    public void throwItem(Object obj) {
//        if (obj == null) {
//            return;
//        }
//        Player c = (Player) obj;
//        int percent = Utils.nextInt(100);
//        if (percent < 30) {
//            Item item = new Item(ItemName.QUA_TRUNG_UUB);
//            item.setDefaultOptions();
//            item.quantity = 1;
//            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//            itemMap.item = item;
//            itemMap.playerID = Math.abs(c.id);
//            itemMap.x = getX();
//            itemMap.y = zone.map.collisionLand(getX(), getY());
//            zone.addItemMap(itemMap);
//            zone.service.addItemMap(itemMap);
//        } else{
//            int[] items = new int[]{ItemName.NHAN_THAN_LINH, ItemName.GANG_THAN_XAYDA, ItemName.QUAN_THAN_XAYDA, ItemName.GANG_THAN_LINH, ItemName.GIAY_THAN_NAMEC};
//            Item item = new Item(items[Utils.nextInt(items.length)]);
//            item.setDefaultOptions();
//            for (ItemOption o : item.options) {
//                int p = Utils.nextInt(0, 15);
//                o.param += o.param * p / 100;
//            }
//            int percent_star = Utils.nextInt(10);
//            if (percent_star < 5) {
//                item.addItemOption(new ItemOption(107, 6));
//            } else {
//                item.addItemOption(new ItemOption(107, Utils.nextInt(1, 5)));
//            }
//            item.quantity = 1;
//            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//            itemMap.item = item;
//            itemMap.playerID = Math.abs(c.id);
//            itemMap.x = getX();
//            itemMap.y = zone.map.collisionLand(getX(), getY());
//            zone.addItemMap(itemMap);
//            zone.service.addItemMap(itemMap);
//        }
//    }
//}
