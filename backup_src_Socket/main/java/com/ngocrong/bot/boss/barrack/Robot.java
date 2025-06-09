package com.ngocrong.bot.boss.barrack;

import com.ngocrong.bot.Boss;
import com.ngocrong.consts.ItemName;
import com.ngocrong.event.Event;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.model.RandomItem;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Robot extends Boss {

    private static final Logger logger = Logger.getLogger(Robot.class);

    public Robot(int id, String name) {
        super();
        this.id = id;
        this.name = name;
        this.limit = -1;
        this.isShow = false;
        this.distanceToAddToList = 10000;
        setInfo(500, 1000000, 400, 10, 5);
        info.percentMiss = 10;
        setTypePK((byte) 5);
        point = 3;
    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList();
            Skill skill = Skills.getSkill((byte) 1, (byte) 7).clone();
            skills.add(skill);
        } catch (CloneNotSupportedException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill err", ex);
        }
    }

    @Override
    public void sendNotificationWhenAppear(String map) {
    }

    @Override
    public void sendNotificationWhenDead(String name) {
    }

    @Override
    public void setDefaultHead() {
        setHead((short) 138);
    }

    @Override
    public void setDefaultBody() {
        setBody((short) 139);
    }

    @Override
    public void setDefaultLeg() {
        setLeg((short) 140);
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
//            int itemID = RandomItem.DRAGONBALL.next();
//            Item item = new Item(itemID);
//            item.setDefaultOptions();
//            item.quantity = 1;
//            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
//            itemMap.item = item;
//            itemMap.playerID = Math.abs(c.id);
//            itemMap.x = getX();
//            itemMap.y = zone.map.collisionLand(getX(), getY());
//            zone.addItemMap(itemMap);
//            zone.service.addItemMap(itemMap);
//
//            itemID = ItemName.MANH_VO_BONG_TAI;
//            item = new Item(itemID);
//            item.setDefaultOptions();
//            item.quantity = Utils.nextInt(90, 110);
//            itemMap = new ItemMap(zone.autoIncrease++);
//            itemMap.item = item;
//            itemMap.playerID = -1;
//            itemMap.x = getX();
//            itemMap.y = zone.map.collisionLand(getX(), getY());
//            zone.addItemMap(itemMap);
//            zone.service.addItemMap(itemMap);
//        }
    }
}
