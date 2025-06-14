package com.ngocrong.bot.boss.bill;

import com.ngocrong.bot.Boss;
import com.ngocrong.consts.ItemName;
import com.ngocrong.consts.MapName;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.lib.RandomCollection;
import com.ngocrong.model.RandomItem;
import com.ngocrong.server.SessionManager;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Berus extends Boss {

    private static final Logger logger = Logger.getLogger(Berus.class);

    public static final int[] MAPS = new int[]{MapName.THANH_PHO_PHIA_DONG, MapName.THANH_PHO_PHIA_NAM, MapName.DAO_BALE, MapName.CAO_NGUYEN,
        MapName.THANH_PHO_PHIA_BAC, MapName.NGON_NUI_PHIA_BAC, MapName.THANH_PHO_PHIA_BAC};

    public static int count;
    public static RandomCollection<Integer> ITEMS = new RandomCollection<>();
    public static RandomCollection<Integer> ITEM_SUPER = new RandomCollection<>();

    static {
        ITEMS.add(2, ItemName.AO_THAN_LINH);
        ITEMS.add(2, ItemName.AO_THAN_NAMEC);
        ITEMS.add(2, ItemName.AO_THAN_XAYDA);
        ITEMS.add(2, ItemName.QUAN_THAN_NAMEC);
        ITEMS.add(2, ItemName.QUAN_THAN_LINH);
        ITEMS.add(0.5, ItemName.QUAN_THAN_XAYDA);
        ITEMS.add(2, ItemName.GIAY_THAN_XAYDA);
        ITEMS.add(2, ItemName.GIAY_THAN_LINH);
        ITEMS.add(0.5, ItemName.GIAY_THAN_NAMEC);
        ITEMS.add(0.1, ItemName.GANG_THAN_LINH);
        ITEMS.add(0.1, ItemName.GANG_THAN_XAYDA);
        ITEMS.add(0.1, ItemName.GANG_THAN_NAMEC);
        ITEMS.add(0.1, ItemName.NHAN_THAN_LINH);

        ITEM_SUPER.add(0.1, ItemName.GANG_THAN_LINH);
        ITEM_SUPER.add(0.1, ItemName.GANG_THAN_XAYDA);
        ITEM_SUPER.add(0.1, ItemName.GANG_THAN_NAMEC);
        ITEM_SUPER.add(0.5, ItemName.QUAN_THAN_XAYDA);
        ITEM_SUPER.add(0.5, ItemName.GIAY_THAN_NAMEC);
    }

    public Berus() {
        super();
        this.limit = -1;
        this.name = "Berus";
        setInfo(2000000000L, 1000000, 500000, 10000, 10);
        count++;
        setDefaultPart();
        setTypePK((byte) 5);
        point = 5;
    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList<>();
            skills.add(Skills.getSkill((byte) 1, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 5, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 3, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 4, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 19, (byte) 7).clone());
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill err");
        }
    }

    @Override
    public void setDefaultLeg() {
        setLeg((short) 510);
    }

    @Override
    public void setDefaultBody() {
        setBody((short) 509);
    }

    @Override
    public void setDefaultHead() {
        setHead((short) 508);
    }

    @Override
    public void sendNotificationWhenAppear(String map) {
        SessionManager.chatVip(String.format("BOSS %s vừa xuất hiện tại %s", this.name, map));
        logger.debug(String.format("BOSS %s vừa xuất hiện tại %s khu vực %d", this.name, map, zone.zoneID));
    }

    @Override
    public void sendNotificationWhenDead(String name) {
        SessionManager.chatVip(String.format("%s: Đã tiêu diệt được %s mọi người đều ngưỡng mộ.", name, this.name));
    }

    @Override
    public void throwItem(Object obj) {
        if (obj == null) {
            return;
        }
        Player c = (Player) obj;
        int percent = Utils.nextInt(100);
        if (percent < 10) {
            Item item = new Item(Utils.nextInt(650, 662));
            item.setDefaultOptions();
            item.quantity = 1;
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.playerID = Math.abs(c.id);
            itemMap.x = getX();
            itemMap.y = zone.map.collisionLand(getX(), getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        } else if (percent < 20) {
            Item item = new Item(ItemName.SAO_PHA_LE_964);
            item.setDefaultOptions();
            item.quantity = 1;
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.playerID = Math.abs(c.id);
            itemMap.x = getX();
            itemMap.y = zone.map.collisionLand(getX(), getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        } else if (percent < 40) {
            Item item = new Item(ItemName.DA_XANH_LAM);
            item.setDefaultOptions();
            item.quantity = Utils.nextInt(5, 15);
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.playerID = Math.abs(c.id);
            itemMap.x = getX();
            itemMap.y = zone.map.collisionLand(getX(), getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        } else if (percent < 70) {
            Item item = new Item(RandomItem.FOOD.next());
            item.setDefaultOptions();
            item.quantity = Utils.nextInt(5, 15);
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.playerID = Math.abs(c.id);
            itemMap.x = getX();
            itemMap.y = zone.map.collisionLand(getX(), getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        } else {
            Item item = new Item(ItemName.NGOC_RONG_3_SAO);
            item.setDefaultOptions();
            item.quantity = 1;
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.playerID = Math.abs(c.id);
            itemMap.x = getX();
            itemMap.y = zone.map.collisionLand(getX(), getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        }
    }

    @Override
    public void startDie() {
        super.startDie();
        Utils.setTimeout(() -> {
            Boss boss = new Whis();
            boss.setLocation(MapName.HANH_TINH_BILL, -1);
        }, 10000);
    }
}
