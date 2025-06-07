package com.ngocrong.bot.boss.fide;

import com.ngocrong.bot.Boss;
import com.ngocrong.consts.ItemName;
import com.ngocrong.event.Event;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.item.ItemOption;
import com.ngocrong.map.GinyuForce;
import com.ngocrong.server.SessionManager;
import com.ngocrong.skill.SkillName;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.mob.Mob;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class So4 extends Boss {

    private static Logger logger = Logger.getLogger(So4.class);
    private GinyuForce team;

    public So4(GinyuForce team) {
        super();
        this.team = team;
        this.distanceToAddToList = 1000;
        this.limit = 1000;
        this.name = "Số 4";
        setInfo(20000000, 1000000, 10000, 100, 5);
        this.willLeaveAtDeath = false;
        if (team.getType() == 0) {
            setInfo(20000000, 1000000, 10000, 100, 5);
            this.percentDame = 35;
        }
    }

    @Override
    public long injure(Player plAtt, Mob mob, long dameInput) {
        if (team.getType() == 0) {
            return Math.min(500000, dameInput);
        }
        return dameInput;

    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList<>();
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_DAM_DRAGON, (byte) 3).clone());
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_DAM_DEMON, (byte) 3).clone());
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_DAM_GALICK, (byte) 3).clone());
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_KAMEJOKO, (byte) 3).clone());
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_MASENKO, (byte) 3).clone());
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_ANTOMIC, (byte) 3).clone());
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill err");
        }
    }

    @Override
    public void killed(Object obj) {
        super.killed(obj);
        if (obj == null) {
            return;
        }
        Player killer = (Player) obj;
        if (killer.taskMain != null && killer.taskMain.id == 20 && killer.taskMain.index == 1
                && killer.zone.map.mapID > 70) {
            killer.updateTaskCount(1);
        }
    }

    @Override
    public void throwItem(Object obj) {
        if (obj == null) {
            return;
        }
        Player c = (Player) obj;

        if (team.getType() == 0) {
            for (int i = 0; i < 5; i++) {
                Item item = new Item(ItemName.THOI_VANG);
                item.setDefaultOptions();
                item.quantity = 1;
                ItemMap itemMap = new ItemMap(zone.autoIncrease++);
                itemMap.item = item;
                itemMap.playerID = -1;
                itemMap.x = (short) Utils.nextInt(50, zone.map.width - 50);
                itemMap.y = zone.map.collisionLand(itemMap.x, getY());
                zone.addItemMap(itemMap);
                zone.service.addItemMap(itemMap);
            }
        } else if (zone.map.mapID > 70) {
            Item item = new Item(ItemName.NGOC_RONG_5_SAO);
            item.setDefaultOptions();
            item.quantity = 1;
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.playerID = Math.abs(c.id);
            itemMap.x = getX();
            itemMap.y = zone.map.collisionLand(getX(), getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        } else {
            Item item = new Item(ItemName.THOI_VANG);
            item.setDefaultOptions();
            item.quantity = 1;
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            itemMap.item = item;
            itemMap.playerID = -1;
            itemMap.x = (short) Utils.nextInt(50, zone.map.width - 50);
            itemMap.y = zone.map.collisionLand(itemMap.x, getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);

        }
    }

    public boolean meCanMove() {
        return super.meCanMove() && typePk == 5;
    }

    @Override
    public void sendNotificationWhenAppear(String map) {
        // PlayerManager.chatVip(String.format("BOSS %s vừa xuất hiện tại %s",
        // this.name, map));
    }

    @Override
    public void sendNotificationWhenDead(String name) {
        // if (team != null && team.type == 0) {
        // PlayerManager.chatVip(String.format("%s: Đã đánh bại và nhận được cải trang
        // thành Số 4", name));
        // } else {
        SessionManager.chatVip(String.format("%s: Đã tiêu diệt được %s mọi người đều ngưỡng mộ.", name, this.name));
        // }
    }

    @Override
    public void startDie() {
        try {
            super.startDie();
        } finally {
            zone.leave(this);
            if (team.getType() == 0) {
                Utils.setTimeout(() -> {
                    this.wakeUpFromDead();
                    team.spawnSolo(this);
                    this.setTypePK((byte) 5);
                }, 30 * 60000L);
            } else {
                team.next(this);
            }
        }
    }

    @Override
    public void setDefaultLeg() {
        setLeg((short) 170);
    }

    @Override
    public void setDefaultBody() {
        setBody((short) 169);
    }

    @Override
    public void setDefaultHead() {
        setHead((short) 168);
    }

}
