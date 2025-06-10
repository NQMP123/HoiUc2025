//package com.ngocrong.bot.boss;
//
//import com.ngocrong.bot.Boss;
//import com.ngocrong.consts.ItemName;
//import com.ngocrong.item.Item;
//import com.ngocrong.item.ItemMap;
//import com.ngocrong.item.ItemOption;
//import com.ngocrong.map.tzone.Zone;
//import com.ngocrong.skill.Skills;
//import com.ngocrong.user.Player;
//import com.ngocrong.util.Utils;
//
//import java.util.ArrayList;
//
//public class ThoDaiCa extends Boss {
//
//    public ThoDaiCa() {
//        super();
//        this.distanceToAddToList = 100;
//        this.limit = 500;
//        name = "Thỏ đại ca";
//        setInfo(Utils.nextInt(400000000, 500000000), 100000, 1000, 100, 20);
//        setDefaultPart();
//        this.waitingTimeToLeave = 5000;
//        this.sayTheLastWordBeforeDie = "Mau giao hết vàng bạc châu báu cho ta...";
//        setTypePK((byte) 5);
//        point = 0;
//        setBienCarot(true);
//    }
//
//    @Override
//    public void initSkill() {
//        try {
//            skills = new ArrayList<>();
//            skills.add(Skills.getSkill((byte) 1, (byte) 7).clone());
//            skills.add(Skills.getSkill((byte) 5, (byte) 7).clone());
//            skills.add(Skills.getSkill((byte) 3, (byte) 7).clone());
//            skills.add(Skills.getSkill((byte) 4, (byte) 7).clone());
//        } catch (Exception ignored) {
//        }
//    }
//
//    @Override
//    public void sendNotificationWhenAppear(String map) {
//
//    }
//
//    @Override
//    public void sendNotificationWhenDead(String name) {
//
//    }
////
////    @Override
////    public long formatDamageInjure(Object attacker, long dame) {
////        if (attacker instanceof Player) {
////            Player player = (Player) attacker;
////            if (player.isCarot()) {
////                return 1;
////            }
////        }
////        return Math.min(dame, info.hpFull / 100);
////    }
//
//    @Override
//    public void setDefaultLeg() {
//        setLeg((short) 405);
//    }
//
//
//    @Override
//    public void setDefaultBody() {
//        setBody((short) 404);
//    }
//
//
//    @Override
//    public void setDefaultHead() {
//        setHead((short) 403);
//    }
//
//    @Override
//    public void startDie() {
//        Zone z = zone;
//        super.startDie();
//        Utils.setTimeout(() -> {
//            Boss boss = new ThoDaiCa();
//            boss.setLocation(z);
//        }, 60000);
//    }
//
//    @Override
//    public void throwItem(Object obj) {
//        if (obj == null) {
//            return;
//        }
//        Player c = (Player) obj;
//        if (Utils.nextInt(100) < 10) {
//            Item item = new Item(ItemName.CAI_TRANG_THO_DAI_CA);
//            item.setDefaultOptions();
//            item.addItemOption(new ItemOption(93, Utils.nextInt(1, 3)));
//            item.addItemOption(new ItemOption(212, 0));
//            item.quantity = 1;
//            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//            itemMap.item = item;
//            itemMap.playerID = Math.abs(c.id);
//            itemMap.x = getX();
//            itemMap.y = zone.map.collisionLand(getX(), getY());
//            zone.addItemMap(itemMap);
//            zone.service.addItemMap(itemMap);
//        }
//        Item item = new Item(ItemName.TRUNG_GA_EVENT_TRUNG_THU_2023);
//        item.setDefaultOptions();
//        item.quantity = Utils.nextInt(2, 5);
//        ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//        itemMap.item = item;
//        itemMap.playerID = Math.abs(c.id);
//        itemMap.x = getX();
//        itemMap.y = zone.map.collisionLand(getX(), getY());
//        zone.addItemMap(itemMap);
//        zone.service.addItemMap(itemMap);
//    }
//}
