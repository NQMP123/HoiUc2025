package com.ngocrong.bot.boss.Cell;

import com.ngocrong.bot.Boss;
import com.ngocrong.bot.boss.fide.Fide;
import com.ngocrong.consts.ItemName;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.lib.RandomCollection;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.server.SessionManager;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class SieuBoHung extends Boss {

    private static final Logger logger = Logger.getLogger(Fide.class);
    public static RandomCollection<Integer> ITEMS = new RandomCollection<>();

    static {
        ITEMS.add(20, ItemName.GANG_THAN_LINH);
        ITEMS.add(20, ItemName.GANG_THAN_XAYDA);
        ITEMS.add(20, ItemName.GANG_THAN_NAMEC);
        ITEMS.add(20, ItemName.NHAN_THAN_LINH);
        ITEMS.add(10, ItemName.NGOC_RONG_3_SAO);
        ITEMS.add(10, ItemName.NGOC_RONG_2_SAO);

    }

    private final boolean isSuper;

    public static int count;

    public SieuBoHung(boolean isSuper) {
        super();
        this.isSuper = isSuper;
        this.distanceToAddToList = 500;
        this.limit = -1;
        if (!this.isSuper) {
            this.name = "Xên Hoàn Thiện";
            setInfo(500000000L, 1000000, 100000, 1000, 50);
        } else {
            this.name = "Siêu Bọ Hung";
            setInfo(500000000L, 1000000, 2000000, 1000, 50);
            count++;
        }
        this.waitingTimeToLeave = 5000;
        setTypePK((byte) 5);
        point = 5;
    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList<>();
            skills.add(Skills.getSkill((byte) 0, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 1, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 2, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 3, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 4, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 5, (byte) 7).clone());
            skills.add(Skills.getSkill((byte) 14, (byte) 7).clone());
        } catch (CloneNotSupportedException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill");
        }
    }

    @Override
    public void killed(Object obj) {
        super.killed(obj);
        if (obj == null) {
            return;
        }
        Player killer = (Player) obj;
        if (isSuper && killer.taskMain != null && killer.taskMain.id == 26 && killer.taskMain.index == 3) {
            killer.updateTaskCount(1);
        }
    }

    @Override
    public void throwItem(Object obj) {
        if (obj == null) {
            return;
        }
        int[] dtl = {555, 556, 557, 558, 559, 563, 567, ItemName.GANG_THAN_NAMEC, ItemName.GANG_THAN_NAMEC};
        Player c = (Player) obj;
        GeneralDrop(c);

    }

    @Override
    public void startDie() {
        Zone z = zone;
        super.startDie();
        if (!isSuper) {
            Utils.setTimeout(() -> {
                SieuBoHung sieuBoHung = new SieuBoHung(true);
                sieuBoHung.setLocation(z);
            }, waitingTimeToLeave + 1000);
        } else {
            Utils.setTimeout(() -> {
                SieuBoHung sieuBoHung = new SieuBoHung(false);
                sieuBoHung.setLocation(103, -1);
            }, 600000);
        }
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
    public void setDefaultLeg() {
        setLeg((short) 236);
    }

    @Override
    public void setDefaultBody() {
        setBody((short) 235);
    }

    @Override
    public void setDefaultHead() {
        setHead((short) 234);
    }

    @Override
    public void updateEveryFiveSeconds() {
        super.updateEveryFiveSeconds();
        /* if (zone != null) {
            List<Player> outZones = zone.getListChar(Zone.TYPE_HUMAN).stream()
                    .filter(player -> player.info.power < 60000000000L
                            || !player.isFullEquipStar(5)
                            || player.myPet == null
                            || player.myPet.info.power < 50000000000L
                            || !player.myPet.isFullEquipStar(5))
                    .collect(Collectors.toList());
            outZones.forEach(Player::goHome);
        }*/
    }
}
