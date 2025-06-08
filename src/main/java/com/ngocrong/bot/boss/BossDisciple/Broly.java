package com.ngocrong.bot.boss.BossDisciple;

import com.ngocrong.bot.Boss;
import com.ngocrong.map.MapManager;
import com.ngocrong.map.TMap;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.mob.Mob;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Broly extends Boss {

    private static final Logger logger = Logger.getLogger(Broly.class);

    public int level;
    public Skill[] TTNL = new Skill[7];

    public Broly() {
        super();
        this.distanceToAddToList = 100;
        this.limit = 500;
        this.level = level;
        setInfo(Utils.nextInt(100000, 500000), 100000, 10, 100, 20);
        this.name = "Broly " + Utils.nextInt(100);
        setDefaultPart();
        this.waitingTimeToLeave = 5000;
        this.sayTheLastWordBeforeDie = "Các ngươi hãy chờ đấy, ta sẽ quay lại sau";
        setTypePK((byte) 5);
        point = 0;
    }

    public long getDameAttack(Player plAtt) {
        long baseDame = this.info.hpFull / 10;
        long dameCap;
        long weakPlayerHpThreshold = 50000;
        if (plAtt.info.hpFull < weakPlayerHpThreshold) {
            dameCap = plAtt.info.hpFull / 10;
        } else {
            dameCap = plAtt.info.hpFull / 20;
        }
        long dameAfterCap = Math.min(baseDame, dameCap);
        double randomFactor = Utils.nextInt(90, 111) / 100.0;
        long dameRandomized = (long) (dameAfterCap * randomFactor);
        long finalDame = dameRandomized - plAtt.info.defenseFull;
        if (finalDame <= 0) {
            return 1;
        }
        return finalDame;
    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList<>();
            skills.add(Skills.getSkill((byte) 1, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 5, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 3, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 4, (byte) 7).clone());
            TTNL = new Skill[7];
            for (int i = 0; i < 7; i++) {
                TTNL[i] = Skills.getSkill((byte) 8, (byte) (i + 1)).clone();
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill err");
        }
    }

    @Override
    public void sendNotificationWhenAppear(String map) {

    }

    @Override
    public void sendNotificationWhenDead(String text) {

    }

    @Override
    public void addTargetToList() {
    }

    public void joinMap() {
        Integer[] mapIdArray = new Integer[]{5, 7, 13, 10, 20, 19, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
        List<Integer> mapList = new ArrayList<>(Arrays.asList(mapIdArray));
        Collections.shuffle(mapList);
        for (Integer mapId : mapList) {
            TMap map = MapManager.getInstance().getMap(mapId);
            if (map == null || map.zones.isEmpty()) {
                continue;
            }
            List<Zone> zoneList = new ArrayList<>(map.zones);
            Collections.shuffle(zoneList);
            for (Zone zone : zoneList) {
                if (zone != null && zone.getBossInZone().isEmpty() && zone.zoneID >= 2) {
                    this.setLocation(map.mapID, zone.zoneID);
                    return; // Thoát khỏi phương thức vì đã tìm được chỗ
                }
            }
        }
    }

    @Override
    public void startDie() {
        Zone zone = this.zone;
        listTarget.clear();
        super.startDie();
        long HP = info.hpFull;
        if (HP < 1000000) {
            Utils.setTimeout(() -> {
                Broly broly = new Broly();
                broly.setInfo(HP, Long.MAX_VALUE, 1000, id, id);
                broly.joinMap();
            }, 30000);
        } else {
            Utils.setTimeout(() -> {
                SuperBroly superbroly = new SuperBroly();
                superbroly.setInfo(HP, Long.MAX_VALUE, 1000, id, id);
                superbroly.setLocation(zone);
            }, 5000);
        }
    }

    public void checkDie() {
    }

    @Override
    public void throwItem(Object obj) {

        if (obj == null) {
            return;
        }

    }

    @Override
    public void addTarget(Player _c) {
        if (_c != this) {
            if (!listTarget.contains(_c)) {
                this.listTarget.add(_c);
                chat(String.format("Mi làm ta nổi giận rồi đó %s", _c.name));
            }
        }
    }
    public int currentStatus;
    public long lastChangeStatus = 0;

    @Override
    public long injure(Player plAtt, Mob mob, long dameInput) {
        if (plAtt != null) {
            addTarget(plAtt);
        }
        return Math.min(dameInput, this.info.hpFull / 100);
    }

    public Skill oldSelect;

    public void usingTTNL() {
        this.select = TTNL[Utils.nextInt(TTNL.length)];
        startRecoveryEnery();
    }

    public void checkAttack() {
        if (this.info.hp > 0) {
            this.info.mp = this.info.mpFull = Long.MAX_VALUE;
        }
        if (System.currentTimeMillis() - lastChangeStatus >= 1000) {
            lastChangeStatus = System.currentTimeMillis();
            if (this.info.hp < info.hpFull && Utils.isTrue(1, 10)) {
                int percentHP = (int) ((this.info.hp * 100) / info.hpFull);
                if (percentHP > 80) {
                    usingTTNL();
                } else {
                    upPoint();
                    if (Utils.isTrue(1, 2)) {
                        usingTTNL();
                    }
                }
            }
        }
        if (System.currentTimeMillis() - lastUseRecoveryEnery >= 2000 && this.isRecoveryEnergy) {
            stopRecoveryEnery();
        }
    }

    @Override
    public void update() {
        checkAttack();
        super.update();
    }

    public void upPoint() {
        long hp = (long) (info.hpFull / (Utils.nextInt(25, 35)));

        if (hp > 0) {
            info.hp += hp;
            info.hpFull += hp;
            info.hp = Math.min(16070777, info.hp);
            info.hpFull = Math.min(16070777, info.hpFull);
            zone.service.playerLoadHP(this, (byte) 0);
        }
    }

    @Override
    public void updateEveryThirtySeconds() {
        if (!isDead()) {
            chat("Tránh xa ta ra, đừng làm ta nổi giận");
        }
    }

    @Override
    public void move() {
        if (Utils.nextInt(3) == 0) {
            super.move();
        }
    }

    @Override
    public void setDefaultHead() {

        setHead((short) 291);

    }

    @Override
    public void setDefaultBody() {

        setBody((short) 292);

    }

    @Override
    public void setDefaultLeg() {
        setLeg((short) 293);
    }

}
