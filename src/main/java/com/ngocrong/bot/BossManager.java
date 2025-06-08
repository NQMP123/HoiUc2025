package com.ngocrong.bot;

import com.ngocrong.NQMP.Cumber.Cumber;
import com.ngocrong.NQMP.MainUpdate;
import com.ngocrong.NQMP.TamThangBa.NuThan;
import com.ngocrong.NQMP.Tet2025.BossTet1;
import com.ngocrong.bot.boss.BaoCat;
import com.ngocrong.bot.boss.BlackGoku;
import com.ngocrong.bot.boss.BossTet;
import com.ngocrong.bot.boss.Cell.SieuBoHung;
import com.ngocrong.bot.boss.Cell.XenBoHung;
import com.ngocrong.bot.boss.Cell.XenCon;
import com.ngocrong.bot.boss.Chilled;
import com.ngocrong.bot.boss.Raiti;
import com.ngocrong.bot.boss.BossDisciple.Broly;

import com.ngocrong.bot.boss.BossDisciple.SuperMabu;
import com.ngocrong.bot.boss.bill.Berus;
import com.ngocrong.bot.boss.fide.*;
import com.ngocrong.consts.MapName;
import com.ngocrong.map.Boss_Tet;
import com.ngocrong.map.GalaxySoldier;
import com.ngocrong.map.GinyuForce;
import com.ngocrong.map.HaiTacManager;
import com.ngocrong.map.TayDuManager;
import com.ngocrong.map.TeamAndroid13;
import com.ngocrong.map.TeamAndroid16;
import com.ngocrong.map.TeamAndroid19;
import com.ngocrong.util.Utils;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BossManager {

    public static void bornBoss() {
        //bossShizuka();
        bossCooler();
        bossBlackGoku();
        bossGinyu();
        bossGalaxySoldier();

        bossHaiTac();
        bossTayDu();
        bossFide();
        bossKuKu();
        bossMapDauDinh();
        bossRamBo();
        bossXenBoHung();
        bossSieuBoHung();
        bossAndroid13();
        bossAndroid19();
        bossAndroid16();
        bossXenCon();
        bossSuperBroly();
        bossRaiti();
        NuThan();
//        bossTet2();
//        bossCumber();
        try {
            Utils.setTimeout(() -> {
                BaoCat baocat = new BaoCat();
                baocat.joinMap();
            }, 30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Utils.setTimeout(() -> {
            FideGold fideGold = new FideGold();
            fideGold.setLocation(MapName.DONG_KARIN, 1);
        }, 300000);
        Utils.setTimeout(() -> {
            FideGold fideGold = new FideGold();
            fideGold.setLocation(MapName.THUNG_LUNG_NAMEC, 1);
        }, 300000);
        Utils.setTimeout(() -> {
            FideGold fideGold = new FideGold();
            fideGold.setLocation(MapName.THANH_PHO_VEGETA, 1);
        }, 300000);*/
//        bossDarkPic();
//        bossBlackWhite();
//        bossNguHanhSon();
//        bossBill();
    }

//    public static void bossShizuka() {
//        LocalDateTime localNow = LocalDateTime.now();
//        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
//        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
//        ZonedDateTime zonedNext5 = zonedNow.withHour(12).withMinute(30).withSecond(0);
//        if (zonedNow.compareTo(zonedNext5) > 0) {
//            zonedNext5 = zonedNext5.plusDays(1);
//        }
//        Duration duration = Duration.between(zonedNow, zonedNext5);
//        long initalDelay = duration.getSeconds();
//        Runnable runnable = new Runnable() {
//            public void run() {
//                TMap map = MapManager.getInstance().getMap(155);
//                for (Zone z : map.zones) {
//                    Boss boss = new Shizuka();
//                    boss.setLocation(z);
//                }
//
//            }
//        };
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(runnable, initalDelay, 1 * 24 * 60 * 60L, TimeUnit.SECONDS);
//    }
    public static void bossCooler() {
        Utils.setTimeout(() -> {
            Cooler cooler = new Cooler((byte) 0);
            cooler.setLocation(110, -1);
        }, 30000);
    }

    public static void bossFide() {
        Utils.setTimeout(() -> {
            Fide fide = new Fide((byte) 0);
            fide.setLocation(80, -1);
        }, 30000);
    }

    public static void bossXenBoHung() {
        Utils.setTimeout(() -> {
            XenBoHung xenBoHung = new XenBoHung((byte) 0);
            xenBoHung.setLocation(100, -1);
        }, 30000);
    }

    public static void bossSieuBoHung() {
        Utils.setTimeout(() -> {
            SieuBoHung sieuBoHung = new SieuBoHung(false);
            sieuBoHung.setLocation(103, -1);
        }, 30000);
    }

    public static void bossXenCon() {
        for (int i = 0; i < 7; i++) {
            Utils.setTimeout(() -> {
                XenCon xc = new XenCon();
                xc.setLocation(103, -1);
            }, 30000);
        }
    }

    public static void bossKuKu() {
        for (int i = 0; i < 3; i++) {
            int[] mapIDs = new int[]{68, 69, 70, 71, 72};
            Utils.setTimeout(() -> {
                KuKu kuKu = new KuKu();
                kuKu.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
            }, 30000);
        }
    }

    public static void bossMapDauDinh() {
        for (int i = 0; i < 3; i++) {
            int[] mapIDs = new int[]{64, 65, 63, 66, 67};
            Utils.setTimeout(() -> {
                MapDauDinh mapDauDinh = new MapDauDinh();
                mapDauDinh.setLocation(69, -1);
            }, 30000);
        }
    }

    public static void bossRamBo() {
        for (int i = 0; i < 3; i++) {
            int[] mapIDs = new int[]{73, 74, 75, 76, 77};
            Utils.setTimeout(() -> {
                RamBo ramBo = new RamBo();
                ramBo.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
            }, 30000);
        }
    }

//    public static void bossDarkPic() {
//        int[] mapIDs = new int[]{MapName.NAM_BULON, MapName.DONG_BULON};
//        Utils.setTimeout(() -> {
//            DarkPic pic = new DarkPic(false);
//            pic.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
//        }, 10000);
//    }
//    public static void bossBlackWhite() {
//        int[] mapIDs = new int[]{MapName.THANH_PHO_1, MapName.THANH_PHO_2, MapName.THANH_PHO_3};
//        Utils.setTimeout(() -> {
//            BlackWhite blackWhite = new BlackWhite(false);
//            blackWhite.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
//        }, 10000);
//    }
    public static void bossBlackGoku() {
        for (int i = 0; i < 3; i++) {
            int[] mapIDs = new int[]{103};
            Utils.setTimeout(() -> {
                BlackGoku bl = new BlackGoku(false);
                bl.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
            }, 30000);
        }
    }

    public static void bossChilled() {
        Utils.setTimeout(() -> {
            Chilled chilled = new Chilled(false);
            chilled.setLocation(160, -1);
        }, 30000);
    }

//
//    public static void bossNguHanhSon() {
//        int[] maps = new int[]{MapName.NGU_HANH_SON, MapName.NGU_HANH_SON_2, MapName.NGU_HANH_SON_3};
//        Utils.setTimeout(() -> {
//            Boss boss = new BatGioi();
//            boss.setLocation(maps[Utils.nextInt(maps.length)], -1);
//        }, 300000);
//    }
    public static void bossBill() {
        for (int i = 0; i < 5; i++) {
            Utils.setTimeout(() -> {
                Boss boss = new Berus();
                boss.setLocation(MapName.HANH_TINH_BILL, -1);
            }, 30000);
        }
    }

    public static void bossGalaxySoldier() {
        MainUpdate.runTaskDayInWindow(() -> {
            GalaxySoldier gB = new GalaxySoldier();
            gB.next((byte) 0);
        }, "06:00", "12:00");
    }

    public static void bossHaiTac() {
        MainUpdate.runTaskDay(() -> {
            HaiTacManager haitac = new HaiTacManager();
            haitac.spawnTeam();

        }, "06:00");

    }

    public static void bossTayDu() {
        MainUpdate.runTaskDay(() -> {
            TayDuManager taydu = new TayDuManager();
            taydu.spawnTeam();
        }, "08:00");
    }

    public static void bossGinyu() {
        Utils.setTimeout(() -> {
            GinyuForce gB = new GinyuForce((byte) 0);
            gB.born();
        }, 30000);
        Utils.setTimeout(() -> {
            GinyuForce gB = new GinyuForce((byte) 1);
            gB.born();
        }, 30000);
    }

    public static void bossAndroid13() {
        Utils.setTimeout(() -> {
            TeamAndroid13 teamAndroid13 = new TeamAndroid13();
            teamAndroid13.born();
        }, 30000);
    }

    public static void bossAndroid19() {
        Utils.setTimeout(() -> {
            TeamAndroid19 teamAndroid19 = new TeamAndroid19();
            teamAndroid19.born();
        }, 30000);
    }

    public static void bossAndroid16() {
        Utils.setTimeout(() -> {
            TeamAndroid16 teamAndroid16 = new TeamAndroid16();
            teamAndroid16.born();
        }, 30000);
    }

    public static void bossSuperBroly() {
        int[] mapIDs = new int[]{5, 7, 13, 10, 20, 19, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
        Utils.setTimeout(() -> {
            for (int i = 0; i < 60; i++) {
                Broly broly = new Broly();
                broly.joinMap();
            }
            for (int i = 0; i < 2; i++) {
                SuperMabu supermabu = new SuperMabu();
                supermabu.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
            }
        }, 32000);

    }

    private static void NuThan() {

        Utils.setTimeout(() -> {
            for (int i = 0; i < 20; i++) {
                NuThan boss = new NuThan();
                boss.setLocation(177, -1);
            }
        }, 30000);
    }

    private static void bossTet2() {
        for (int i = 0; i < 3; i++) {
            int[] mapIDs = new int[]{1, 2, 8, 9, 15, 16};
            Utils.setTimeout(() -> {
                BossTet1 boss = new BossTet1();
                boss.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
            }, 30000);
        }
        Utils.setTimeout(() -> {
            Boss_Tet boss = new Boss_Tet();
            boss.born();
        }, 35000);
    }

    private static void bossCumber() {
        int[] mapIDs = new int[]{19};
        Utils.setTimeout(() -> {
            Cumber boss = new Cumber(false);
            boss.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
        }, 30000);

    }

    private static void bossRaiti() {
        int[] mapIDs = new int[]{5, 6, 27, 28, 29, 30, 13, 33, 34, 10, 35, 36, 37, 38, 19, 20};
        Utils.setTimeout(() -> {
            Raiti boss = new Raiti();
            boss.setLocation(mapIDs[Utils.nextInt(mapIDs.length)], -1);
        }, 30000);
    }
}
