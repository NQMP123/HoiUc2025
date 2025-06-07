package com.ngocrong.bot.boss.BossDisciple;

import com.ngocrong.bot.Boss;
import com.ngocrong.consts.ItemName;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.item.ItemOption;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.server.SessionManager;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Broly extends Boss {

    private static final Logger logger = Logger.getLogger(Broly.class);

    public int level;

    public Broly(int level) {
        super();
        this.distanceToAddToList = 100;
        this.limit = 500;
        this.level = level;
        if (level == 0) {
            setInfo(Utils.nextInt(500, 3000), 100000, 10, 100, 20);
            this.name = "Broly " + Utils.nextInt(100);
        } else if (level == 1) {
            setInfo(Utils.nextInt(1000, 1700) * 100000L, 100000, 10000, 100, 20);
            this.name = "Super Broly " + Utils.nextInt(100);
        } else if (level == 2) {
            setInfo(Utils.nextInt(1000, 1700) * 2000000L, 100000, 100000, 3000, 20);
            this.name = "Super Broly Legend" + Utils.nextInt(100);
        }
        setDefaultPart();
        this.waitingTimeToLeave = 5000;
        this.sayTheLastWordBeforeDie = "Các ngươi hãy chờ đấy, ta sẽ quay lại sau";
        setTypePK((byte) 5);
        point = 0;
        if (level == 2) {
            info.options[201] = 80;
        }
    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList<>();
            skills.add(Skills.getSkill((byte) 1, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 5, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 3, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 4, (byte) 7).clone());
            Skill skill = Skills.getSkill((byte) 8, (byte) 7).clone();
            skill.coolDown = 10000;
            skills.add(skill);
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill err");
        }
    }

    @Override
    public void sendNotificationWhenAppear(String map) {
        if (level > 0) {
            SessionManager.chatVip(String.format("BOSS %s vừa xuất hiện tại %s", this.name, map));
            logger.debug(String.format("BOSS %s vừa xuất hiện tại %s khu vực %d", this.name, map, zone.zoneID));
        }
    }

    @Override
    public void sendNotificationWhenDead(String text) {

    }

    @Override
    public void startDie() {
        Zone z = zone;
        listTarget.clear();
        super.startDie();
        if (level == 0) {
            Utils.setTimeout(() -> {
                Broly broly = new Broly(1);
                if (info.hpFull > 1000000) {
                    broly.setInfo(info);
                }
                broly.setLocation(z.map.mapID, -1);
            }, 10000);
        } else if (level == 1) {
            Utils.setTimeout(() -> {
                Broly broly = new Broly(2);
                broly.setLocation(z.map.mapID, -1);
            }, 10000);
        } else if (level == 2) {
            Utils.setTimeout(() -> {
                Broly broly = new Broly(0);
                broly.setLocation(z.map.mapID, -1);
            }, 300000);
        }
    }

    @Override
    public void throwItem(Object obj) {
        if (level < 2) {
            return;
        }
        if (obj == null) {
            return;
        }
        Player c = (Player) obj;
        if (Utils.nextInt(100) < 100) {
            Item item = new Item(ItemName.MA_PHONG_BA);
            item.setDefaultOptions();
            item.addItemOption(new ItemOption(86, 0));
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

//    @Override
//    public long formatDamageInjure(Object attacker, long dame) {
//        if (level < 2) {
//            return Math.min(dame, info.hpFull / 20);
//        }
//        return Math.min(dame, info.hpFull / 100);
//    }
//    @Override
//    public void createPet(int type, int gender) {
//        Pet pet = new Pet();
//        pet.id = this.id + 1;
//        pet.name = "Đệ tử";
//        pet.itemBody = new Item[9];
//        pet.type = type;
//        if (type == 0) {
//            pet.gender = pet.classId = (byte) Utils.nextInt(3);
//        } else {
//            pet.gender = pet.classId = (byte) gender;
//        }
//        pet.info = new Info(pet);
//        pet.info.setChar(pet);
//        pet.info.setStamina();
//        pet.info.setPowerLimited();
//        pet.info.applyCharLevelPercent();
//        pet.skills = new ArrayList<>();
//        pet.skillOpened = 0;
//        pet.learnSkill();
//        pet.setMaster(this);
//        pet.petStatus = 0;
//        pet.info.setInfo();
//        pet.info.recovery(Info.ALL, 100, false);
//        pet.service = new Service(pet);
//        pet.setDefaultPart();
//        myPet = pet;
//        ArrayList<Item> bodys = new ArrayList<>();
//        for (Item item : pet.itemBody) {
//            if (item != null) {
//                bodys.add(item);
//            }
//        }
//        pet.followMaster();
//        service.petInfo((byte) 1);
//        if (zone != null) {
//            zone.enter(myPet);
//        }
//    }
//
//    @Override
//    public void addTarget(Player _c) {
//        if (_c != myPet && _c != this) {
//            if (!listTarget.contains(_c)) {
//                this.listTarget.add(_c);
//                chat(String.format("Mi làm ta nổi giận rồi đó %s", _c.name));
//            }
//        }
//    }
    @Override
    public void updateEveryThirtySeconds() {
        if (!isDead()) {
            long hp = (info.hpFull - info.hp) / 5;
            if (hp > 0) {
                info.hpFull += hp;
            }
            chat("Tránh xa ta ra, đừng làm ta nổi giận");
        }
    }

    @Override
    public void update() {
        if (info.hpFull > 160707770) {
            info.hpFull = 160707770;
        }
        super.update();
    }

    @Override
    public void move() {
        if (Utils.nextInt(3) == 0) {
            super.move();
        }
    }

    @Override
    public void setDefaultHead() {
        if (level == 2) {
            setHead((short) 390);
        } else if (level == 1) {
            setHead((short) 294);
        } else {
            setHead((short) 291);
        }
    }

    @Override
    public void setDefaultBody() {
        if (level > 0) {
            setBody((short) 295);
        } else {
            setBody((short) 292);
        }
    }

    @Override
    public void setDefaultLeg() {
        if (level > 0) {
            setLeg((short) 296);
        } else {
            setLeg((short) 293);
        }
    }

}
