/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.bot;

import com.ngocrong.NQMP.UtilsNQMP;
import com.ngocrong.map.MapManager;
import com.ngocrong.map.TMap;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.mob.Mob;
import com.ngocrong.skill.Skills;
import com.ngocrong.util.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class VirtualBot_SoSinh extends VirtualBot {

    public static int BotSS;

    public VirtualBot_SoSinh(String name) {
        super(name);
        this.gender = (byte) Utils.nextInt(3);
        this.info.power = Utils.nextInt(1500000, 1500005);
        this.setInfo(Utils.nextInt(3000, 5000), Long.MAX_VALUE, Utils.nextInt(50, 70), 10000, 1);
        short[][] hair = new short[][]{
            {64, 30, 31}, {9, 29, 32}, {6, 27, 28}
        };
        short head = hair[this.gender][Utils.nextInt(hair[this.gender].length)];
        short body = (short) (this.gender == 0 ? 14 : this.gender == 1 ? 10 : 16);
        short leg = (short) (this.gender == 0 ? 15 : this.gender == 1 ? 11 : 17);
        boolean Ctrang = Utils.isTrue(5, 10);
        this.setHead(Ctrang ? (short) 906 : head);
        this.setBody(Ctrang ? (short) 880 : body);
        this.setLeg(Ctrang ? (short) 881 : leg);
        timeSpawn = System.currentTimeMillis();
    }

    @Override
    public void update() {
        this.info.mp = this.info.mpFull = Long.MAX_VALUE;

        updateNextMap();
        if (zone != null) {
            if (this.zone.map.mapID != mapNext) {
                TMap map = MapManager.getInstance().getMap(mapNext);
                if (map != null) {
                    Zone zone = map.getMinPlayerZone();
                    this.zone.leave(this);
                    zone.enter(this);
                }
            }
        } else {
            TMap map = MapManager.getInstance().getMap(mapNext);
            if (map != null) {
                Zone zone = map.getMinPlayerZone();
                zone.enter(this);
            }
        }

        attack();
    }

    @Override
    void setBag1() {
        byte[] bag = new byte[]{19, 20, 21, 22, 105, 106};
        this.clanID = Utils.nextInt(0, 100);
        this.setBag(bag[Utils.nextInt(bag.length)]);
        this.name = Utils.getAbbre("[HUNR]") + this.name;
    }

    @Override
    public boolean isBoss() {
        return false;
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    public void updateNextMap() {
        long time = System.currentTimeMillis() - this.timeSpawn;
        int oldMapNext = this.mapNext;
        if (time > 700_000 && this.clanID == -1) {
            setBag1();
        }
        if (time < 50_000) {
            this.mapNext = this.gender == 0 ? 0 : this.gender == 1 ? 7 : 14;

        } else if (time < 100_000) {
            this.mapNext = this.gender == 0 ? 1 : this.gender == 1 ? 8 : 15;

        } else if (time < 150_000) {
            this.mapNext = this.gender == 0 ? 2 : this.gender == 1 ? 9 : 16;

        } else if (time < 300_000) {
            this.mapNext = this.gender == 0 ? 9 : this.gender == 1 ? 16 : 2;
            if (oldMapNext != this.mapNext) {
                this.setInfo(Utils.nextInt(18000, 22000), Utils.nextInt(18000, 22000), Utils.nextInt(500, 700), 10000, 1);
            }

        } else if (time < 450_000) {
            this.mapNext = this.gender == 0 ? 16 : this.gender == 1 ? 2 : 9;

        } else if (time < 500_000) {
            this.mapNext = this.gender == 0 ? 3 : this.gender == 1 ? 11 : 17;
            if (oldMapNext != this.mapNext) {
                this.setInfo(Utils.nextInt(18000, 22000), Utils.nextInt(18000, 22000), Utils.nextInt(500, 700), 10000, 1);
            }

        } else if (time < 600_000) {
            this.mapNext = this.gender == 0 ? 18 : this.gender == 1 ? 4 : 12;
            if (oldMapNext != this.mapNext) {
                this.setInfo(Utils.nextInt(18000, 22000), Utils.nextInt(18000, 22000), Utils.nextInt(200, 500), 10000, 1);
            }

        } else if (time < 650_000) {
            this.mapNext = this.gender == 0 ? 18 : this.gender == 1 ? 4 : 12;
            if (oldMapNext != this.mapNext) {
                this.setInfo(Utils.nextInt(18000, 22000), Utils.nextInt(18000, 22000), Utils.nextInt(500, 800), 10000, 1);
            }

        } else if (time < 700_000) {
            this.mapNext = 27;
            if (oldMapNext != this.mapNext) {
                this.setInfo(Utils.nextInt(18000, 22000), Utils.nextInt(18000, 22000), Utils.nextInt(500, 1000), 10000, 1);
            }

        } else if (time < 800_000) {
            this.mapNext = 31;

        } else if (time < 900_000) {
            this.mapNext = 35;

        } else if (time < 1_000_000) {
            this.mapNext = 30;
            if (oldMapNext != this.mapNext) {
                this.setInfo(this.info.hpFull, 3000000, 1000, 10000, 1);
            }

        } else if (time < 1_100_000) {
            // bamboo - 60 phút
            this.mapNext = 34;

        } else if (time < 1_150_000) {
            this.mapNext = 38;

        } else if (time < 1_200_000) {
            this.mapNext = 6;
            if (oldMapNext != this.mapNext) {
                this.setInfo(this.info.hpFull, 3000000, 3000, 10000, 1);
            }

        } else if (time < 1_300_000) {
            this.mapNext = 10;

        } else if (time < 1_400_000) {
            this.mapNext = 19;

        } else {
            this.mapNext = -1;
            this.zone.leave(this);
            this.close();
        }
    }

}
