/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.bot.boss.BossDisciple;

import com.ngocrong.bot.Boss;
import com.ngocrong.consts.ItemName;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.mob.Mob;
import com.ngocrong.server.SessionManager;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.SkillName;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class SuperMabu extends Boss {

    private static final Logger logger = Logger.getLogger(SuperMabu.class);

    public SuperMabu() {
        super();
        this.percentDame = 3;
        setInfo(1_000_000_000, 1000000000, 100000, 100, 20);
//        this.limitDame = this.info.originalHP / 300;
        this.name = "Super Mabu " + Utils.nextInt(100);
        setTypePK((byte) 5);
        this.limit = -1;
        this.waitingTimeToLeave = 0;
    }

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList<>();

            skills.add(Skills.getSkill((byte) SkillName.CHIEU_KAMEJOKO, (byte) 5).clone());
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_MASENKO, (byte) 5).clone());
            skills.add(Skills.getSkill((byte) SkillName.CHIEU_ANTOMIC, (byte) 5).clone());

            skills.add(Skills.getSkill((byte) SkillName.KHIEN_NANG_LUONG, (byte) 7).clone());
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill err");
        }

    }

    @Override
    public void killed(Object obj) {

        Player player = (Player) obj;
        if (player == null) {
            return;
        }
        if (Utils.isTrue(3, 10)) {
            ItemMap itemMap = new ItemMap(zone.autoIncrease++);
            Item quatrung = new Item(2197);
            quatrung.quantity = 1;
            itemMap.item = quatrung;
            itemMap.playerID = Math.abs(player.id);
            itemMap.x = getX();
            itemMap.y = zone.map.collisionLand(getX(), getY());
            zone.addItemMap(itemMap);
            zone.service.addItemMap(itemMap);
        } else {
            player.addGold(200_000_000);
            player.service.sendThongBao("Bạn nhận được 200tr vàng");
        }
    }

    @Override
    public void sendNotificationWhenAppear(String map) {
        SessionManager.chatVip(String.format("BOSS %s vừa xuất hiện tại %s", this.name, map));
        logger.debug(String.format("BOSS %s vừa xuất hiện tại %s khu vực %d", this.name, map, zone.zoneID));
    }

    @Override
    public void throwItem(Object obj) {

    }

    @Override
    public long injure(Player plAtt, Mob mob, long dameInput) {
        if (plAtt.myDisciple == null) {
            plAtt.service.sendThongBao("Bạn cần phải có đệ tử trước tiên");
            return 0;
        }
        if (plAtt.myDisciple.typeDisciple != 1 && plAtt.myDisciple.typeDisciple != 2) {
            plAtt.service.sendThongBao("Bạn cần phải có đệ tử Mabu trước tiên");
            return 0;
        }

        return dameInput;
    }

    @Override
    public void sendNotificationWhenDead(String name) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setDefaultHead() {
        setHead((short) 421);
    }

    @Override
    public void setDefaultBody() {
        setBody((short) 422);
    }

    @Override
    public void setDefaultLeg() {
        setLeg((short) 423);
    }

    @Override
    public void startDie() {
        super.startDie();
        Utils.setTimeout(() -> {
            int[] mapIDs = new int[]{5, 7, 13, 10, 20, 19, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
            SuperMabu bl = new SuperMabu();
            bl.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
        }, 15 * 60000);

    }

    @Override
    public Object targetDetect() {
        List<Player> enemiesCanAttack = this.zone.players;
        if (enemiesCanAttack.size() > 0) {
            Player target = randomChar(enemiesCanAttack);
            if (target != null) {
                return target;
            }
        }
        return null;
    }

    @Override
    public void updateEveryHalfSeconds() {
        super.updateEveryHalfSeconds();
        if (!isDead()) {
            if (isAttack() && meCanAttack()) {
                if (!isRecoveryEnergy() && !isCharge()) {
                    Object target = targetDetect();
                    if (target != null) {
                        attack(target);
                    }
                    useSkillNotFocus();
                }
            }
        }
    }

    @Override
    public void attack(Object obj) {
        long now = System.currentTimeMillis();
        if (now - lastTimeSkillShoot < 1000) {
            return;
        }
        Player target = (Player) obj;
        Skill skill = selectSkillAttack();
        if (skill != null) {
            int d = Utils.getDistance(0, 0, skill.dx, skill.dy);
            if (skill.template.id == SkillName.CHIEU_KAMEJOKO || skill.template.id == SkillName.CHIEU_MASENKO || skill.template.id == SkillName.CHIEU_ANTOMIC) {
                lastTimeSkillShoot = now;
            }
            this.select = skill;
            moveTo((short) (target.getX() + Utils.nextInt(-d, d)), target.getY());

            zone.attackPlayer(this, target);

        }
    }

}
