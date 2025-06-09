/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.bot;

import com.ngocrong.item.Item;
import com.ngocrong.map.MapManager;
import com.ngocrong.map.TMap;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.mob.Mob;
import com.ngocrong.network.Service;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Info;
import com.ngocrong.util.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class VirtualBot extends Boss {

    public int mapSpawn = 0;
    public int mapNext = 0;
    public long timeSpawn = 0;
    public boolean isInit = false;
    public boolean isTrain = false;

    public VirtualBot(String name) {
        super();
        this.name = name;
        this.id = Utils.nextInt(1000000, 9999999);

    }
    public static int TotalBot;

    public VirtualBot(long hp, long mp, long dame, short head, short body, short leg, String name, Zone zone) {
        super();
        this.setInfo(hp, mp, dame, 100, 5);
        this.setBuaThuHut(true);
        this.name = name;
        if (zone.map.mapID != 5) {
            this.setX((short) Utils.nextInt(0, zone.map.width - 50));
        } else {
            if (Utils.nextInt(10) % 2 == 0) {
                this.setX((short) Utils.nextInt(0, zone.map.width - 50));
            } else {
                this.setX((short) Utils.nextInt(888, 1280));
            }
        }
        timeSpawn = System.currentTimeMillis();
        this.itemBag = new Item[100];
        setBag1();
        service = new Service(this);
        this.id = Utils.nextInt(1000000, 9999999);
    }

    @Override
    public void setInfo(long hp, long mp, long dame, int def, int crit) {
        info.originalHP = hp;
        info.originalMP = Long.MAX_VALUE;
        info.originalDamage = dame;
        info.originalDefense = def;
        info.originalCritical = crit;
        info.setInfo();
        info.recovery(Info.ALL, 100, false);
    }

    void setBag1() {
        if (!isTrain) {
            if (Utils.nextInt(10) <= 7 && mapSpawn != 0 && mapSpawn != 7 && mapSpawn != 14) {
                byte[] bag = new byte[]{19, 20, 21, 22, 105, 106};
                this.clanID = Utils.nextInt(0, 100);
                this.setBag(bag[Utils.nextInt(bag.length)]);
                this.name = "[HUNR-2025] " + this.name;
            }
            if (!isTrain && Utils.nextInt(10) <= 7) {
                byte[] bag = new byte[]{19, 20, 21, 22, 105, 106};
                this.clanID = Utils.nextInt(0, 100);
                this.setBag(bag[Utils.nextInt(bag.length)]);
                this.name = "[HUNR-2025] " + this.name;
            }
        }
    }

    @Override
    public boolean isBoss() {
        return false;
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    long lastupdate = 0;
    public static List<Integer> mapTrains = new ArrayList<>(List.of(0, 7, 14,
            1, 2, 3, 4, 6, 27, 28, 29, 30,
            8, 9, 10, 11, 12, 13, 31, 32, 33, 34,
            15, 16, 17, 18, 19, 20, 36, 37, 38));

    @Override
    public void update() {
        if (this.zone != null) {
            if (!isTrain) {
                if (System.currentTimeMillis() - lastupdate >= Utils.nextInt(5000, 60000)) {
                    try {
                        super.update();
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);
                    }
                    lastupdate = System.currentTimeMillis();
                }
            }
        }

    }
    public Mob mobFocus = null;

    public void attack() {
        if (zone != null) {
            if (mobFocus == null) {
                List<Mob> mobs = zone.getListMob();
                double minDistance = Double.MAX_VALUE;

                int playerX = this.getX();
                int playerY = this.getY();

                for (Mob mob : mobs) {
                    if (mob != null && mob.status != 0 && mob.status != 1 && !mob.isMobMe && mob.hp > 0) {  // Kiểm tra mob không null
                        // Tính khoảng cách giữa player và mob
                        double distance = Math.sqrt(
                                Math.pow(mob.x - playerX, 2)
                                + Math.pow(mob.y - playerY, 2)
                        );

                        // Cập nhật mob gần nhất
                        if (distance < minDistance) {
                            minDistance = distance;
                            mobFocus = mob;
                        }
                    }
                }
            }
            if (mobFocus != null) {
                this.select = skills.get(0);
                this.select.manaUse = 0;
                Mob mob = mobFocus;
                if (mob.status == 0 || mob.status == 1 || mob.isMobMe || mob.hp <= 0) {
                    mobFocus = null;
                    return;
                }
                if (Math.abs(this.getX() - mob.x) > select.dx * 1.2 || Math.abs(this.getY() - mob.y) > select.dy * 1.2) {
                    if (this.meCanMove()) {
                        int stepSize = 100; // Khoảng cách di chuyển mỗi lần
                        int directionX = (mob.x > this.getX()) ? 1 : -1;
                        int directionY = (mob.y > this.getY()) ? 1 : -1;
                        int currentX = this.getX();
                        int currentY = this.getY();
                        int newX = currentX;
                        int newY = currentY;

                        if (Math.abs(mob.x - currentX) > stepSize) {
                            newX = mob.x;
                        } else {
                            newX = mob.x;
                        }

                        if (Math.abs(mob.y - currentY) > stepSize) {
                            newY = mob.y;
                        } else {
                            newY = mob.y;
                        }

                        this.moveTo(newX, newY);
                    }
                    return;
                }
                if (this.meCanAttack()) {
                    this.zone.attackNpc(this, mobFocus, false);
                }
            }
        }
    }

    @Override
    public void initSkill() {

        try {
            skills = new ArrayList<>();
            skills.add(Skills.getSkill((byte) (gender == 0 ? 0 : gender == 1 ? 9 : 17), (byte) 7).clone());
        } catch (CloneNotSupportedException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);

        }

    }

    @Override
    public void sendNotificationWhenAppear(String map) {
    }

    @Override
    public void sendNotificationWhenDead(String name) {
    }

    @Override
    public void setDefaultLeg() {
    }

    @Override
    public void setDefaultBody() {
    }

    @Override
    public void setDefaultHead() {
    }
}
