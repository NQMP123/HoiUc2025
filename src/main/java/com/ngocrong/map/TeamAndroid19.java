package com.ngocrong.map;

import com.ngocrong.bot.Boss;
import com.ngocrong.bot.boss.android.*;
import com.ngocrong.consts.MapName;
import com.ngocrong.util.Utils;

public class TeamAndroid19 {

    private final Android19 android19;

    private final Android20 android20;

    public TeamAndroid19() {
        android19 = new Android19(this);
        android20 = new Android20(this);
    }

    public void born() {
        int[] maps = new int[]{MapName.THANH_PHO_PHIA_NAM};
        int z = Utils.nextInt(maps.length);
        int mapID = maps[z];
        TMap map = MapManager.getInstance().getMap(mapID);
        int zoneID = map.randomZoneID();
        if (android19.isDead()) {
            android19.wakeUpFromDead();
        }
        if (android20.isDead()) {
            android20.wakeUpFromDead();
        }
        useAirshipToArrive(android19, mapID, zoneID);
        useAirshipToArrive(android20, mapID, zoneID);
        android19.setTypePK((byte) 5);
    }

    public void next(Boss boss) {
        if (boss instanceof Android19) {
            Utils.setTimeout(() -> {
                android20.setTypePK((byte) 5);
            }, 1000L);
        }
        if (boss instanceof Android20) {
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
