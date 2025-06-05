package com.ngocrong.bot.boss.barrack;

import com.ngocrong.bot.Boss;
import com.ngocrong.consts.ItemName;
import com.ngocrong.event.Event;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.map.tzone.ZTreasure;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.model.RandomItem;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class GeneralBlue extends Boss {

    private static Logger logger = Logger.getLogger(GeneralBlue.class);

    public GeneralBlue() {
        super();
        this.limit = -1;
        this.isShow = false;
        this.name = "Trung úy Xanh lơ";
        setInfo(500, 1000000, 400, 10, 5);
        info.percentMiss = 20;
        point = 3;
    }

    public void setLocation(Zone zone) {
        setX((short) 843);
        setY((short) 384);
        zone.enter(this);
    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList();
            Skill skill;
            skill = Skills.getSkill((byte) 0, (byte) 7).clone();
            skills.add(skill);
            skill = Skills.getSkill((byte) 1, (byte) 7).clone();
            skills.add(skill);
            skill = Skills.getSkill((byte) 6, (byte) 3).clone();
            skills.add(skill);
        } catch (CloneNotSupportedException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill error", ex);
        }
    }

    @Override
    public void updateEveryOneSeconds() {
        super.updateEveryOneSeconds();
        if (this.typePk == 0 && listTarget.size() > 0) {
            setTypePK((byte) 5);
        }
    }

    @Override
    public void setDefaultHead() {
        setHead((short) 135);
    }

    @Override
    public void setDefaultBody() {
        setBody((short) 136);
    }

    @Override
    public void setDefaultLeg() {
        setLeg((short) 137);
    }

    @Override
    public void throwItem(Object obj) {
        if (obj == null) {
            return;
        }
        Player c = (Player) obj;
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
//        if (obj != null) {
//            Player c = (Player) obj;
//            if (zone.map.isBarrack()) {
//                int itemID = RandomItem.DRAGONBALL.next();
//                Item item = new Item(itemID);
//                item.setDefaultOptions();
//                item.quantity = 1;
//                ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//                itemMap.item = item;
//                itemMap.playerID = Math.abs(c.id);
//                itemMap.x = getX();
//                itemMap.y = zone.map.collisionLand(getX(), getY());
//                zone.addItemMap(itemMap);
//                zone.service.addItemMap(itemMap);

        /*itemID = ItemName.BAN_DO_KHO_BAU;
                item = new Item(itemID);
                item.setDefaultOptions();
                item.quantity = 1;
                itemMap = new ItemMap(zone.autoIncrease++);
                itemMap.item = item;
                itemMap.playerID = -1;
                itemMap.x = getX();
                itemMap.y = zone.map.collisionLand(getX(), getY());
                zone.addItemMap(itemMap);
                zone.service.addItemMap(itemMap);*/
//                itemID = ItemName.MANH_VO_BONG_TAI;
//                item = new Item(itemID);
//                item.setDefaultOptions();
//                item.quantity = Utils.nextInt(45, 55);
//                itemMap = new ItemMap(zone.autoIncrease++);
//                itemMap.item = item;
//                itemMap.playerID = -1;
//                itemMap.x = getX();
//                itemMap.y = zone.map.collisionLand(getX(), getY());
//                zone.addItemMap(itemMap);
//                zone.service.addItemMap(itemMap);
//            }
//            if (zone.map.isTreasure()) {
//                int[] arr = {2, 3, 4, 5, 6, 7};
//                ZTreasure z = (ZTreasure) zone;
//                int level = z.getTreasure().getLevel();
//                int gold = 300 * level;
//                if (gold > 30000) {
//                    gold = 30000;
//                }
//                int itemID = Utils.getItemGoldByQuantity(gold);
//                int loop = arr[level / 20];
//                for (int i = 0; i < loop; i++) {
//                    Item item = new Item(itemID);
//                    item.setDefaultOptions();
//                    item.quantity = gold;
//                    ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//                    itemMap.item = item;
//                    itemMap.playerID = Math.abs(c.id);
//                    itemMap.x = (short) (getX() + Utils.nextInt(-loop * 10, loop * 10));
//                    itemMap.y = zone.map.collisionLand(getX(), getY());
//                    zone.addItemMap(itemMap);
//                    zone.service.addItemMap(itemMap);
//                }
        /*if (level == 110) {
                    Item item = new Item(ItemName.SAO_PHA_LE_DAC_BIET);
                    item.createOptionSaoPhaLeDacBiet();
                    item.quantity = 1;
                    ItemMap itemMap = new ItemMap(zone.autoIncrease++);
                    itemMap.item = item;
                    itemMap.playerID = Math.abs(c.id);
                    itemMap.x = getX();
                    itemMap.y = zone.map.collisionLand(getX(), getY());
                    zone.addItemMap(itemMap);
                    zone.service.addItemMap(itemMap);
                }*/
 /*if (Utils.nextInt(100) == 0 && level == 110) {
                    itemID = RandomItem.GANG_THAN.next();
                    Item item = new Item(itemID);
                    item.setDefaultOptions();
                    if (Utils.nextInt(10) == 0) {
                        int p = Utils.nextInt(1, 15);
                        for (ItemOption o : item.options) {
                            o.param += o.param * p / 100;
                        }
                    }
                    item.quantity = 1;
                    ItemMap itemMap = new ItemMap(zone.autoIncrease++);
                    itemMap.item = item;
                    itemMap.playerID = Math.abs(c.id);
                    itemMap.x = getX();
                    itemMap.y = zone.map.collisionLand(getX(), getY());
                    zone.addItemMap(itemMap);
                    zone.service.addItemMap(itemMap);
                }*/
//                Item item = new Item(ItemName.MANH_VO_BONG_TAI);
//                item.setDefaultOptions();
//                item.quantity = Utils.nextInt(90, 110);
//                ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//                itemMap.item = item;
//                itemMap.playerID = Math.abs(c.id);
//                itemMap.x = getX();
//                itemMap.y = zone.map.collisionLand(getX(), getY());
//                zone.addItemMap(itemMap);
//                zone.service.addItemMap(itemMap);
//            }
//        }
    }

    @Override
    public void sendNotificationWhenAppear(String map) {
    }

    @Override
    public void sendNotificationWhenDead(String name) {
    }
}
