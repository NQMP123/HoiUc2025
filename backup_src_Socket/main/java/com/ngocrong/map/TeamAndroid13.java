package com.ngocrong.map;

import com.ngocrong.bot.Boss;
import com.ngocrong.bot.boss.android.Android13;
import com.ngocrong.bot.boss.android.Android14;
import com.ngocrong.bot.boss.android.Android15;
import com.ngocrong.util.Utils;

public class TeamAndroid13 {

    private final Android13 android13;

    private final Android14 android14;

    private final Android15 android15;

    public TeamAndroid13() {
        android13 = new Android13(this);
        android14 = new Android14(this);
        android15 = new Android15(this);
    }

    public void born() {
        TMap map = MapManager.getInstance().getMap(104);
        int zoneID = map.randomZoneID();

        android15.wakeUpFromDead();

        android14.wakeUpFromDead();

        android13.wakeUpFromDead();

        useAirshipToArrive(android15, 104, zoneID);
        useAirshipToArrive(android14, 104, zoneID);
        useAirshipToArrive(android13, 104, zoneID);
        android15.setTypePK((byte) 5);
    }

    public void next(Boss boss) {
        if (boss instanceof Android15) {
            Utils.setTimeout(() -> {
                android14.setTypePK((byte) 5);
            }, 1000L);
        }
        if (boss instanceof Android14) {
            Utils.setTimeout(() -> {
                android13.setTypePK((byte) 5);
            }, 1000L);
        }
        if (boss instanceof Android15) {
            end();
        }
    }

    private void end() {
        Utils.setTimeout(this::born, 600000L);
    }

    public void useAirshipToArrive(Boss boss, int mapID, int zoneID) {
        TMap map = MapManager.getInstance().getMap(mapID);
        boss.setTeleport((byte) 3);
        boss.setX((short) Utils.nextInt(100, map.width));
        boss.setY((short) 0);
        map.enterZone(boss, zoneID);
        boss.setY(boss.zone.map.collisionLand(boss.getX(), boss.getY()));
        boss.setTeleport((byte) 0);
        boss.sendNotificationWhenAppear(map.name);
    }
}
