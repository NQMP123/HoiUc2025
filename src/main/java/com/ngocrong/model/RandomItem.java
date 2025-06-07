package com.ngocrong.model;

import com.ngocrong.consts.ItemName;
import com.ngocrong.event.Event;
import com.ngocrong.lib.RandomCollection;
import com.ngocrong.mob.Mob;

public class RandomItem {

    public static final RandomCollection<Mob.ItemType> MOB = new RandomCollection<>();
    public static final RandomCollection<Integer> COOLER = new RandomCollection<>();
    public static final RandomCollection<Integer> BOSS_12H = new RandomCollection<>();
    public static final RandomCollection<Integer> BLACK_GOKU = new RandomCollection<>();
    public static final RandomCollection<Integer> DRAGONBALL = new RandomCollection<>();
    public static final RandomCollection<Integer> DO_CUOI = new RandomCollection<>();
    public static final RandomCollection<Integer> BARRACK_VIP = new RandomCollection<>();
    public static final RandomCollection<Integer> SQUIDGAME = new RandomCollection<>();
    public static final RandomCollection<Integer> GINYUFORCE = new RandomCollection<>();
    public static final RandomCollection<Integer> GALAXYSOLDIER = new RandomCollection<>();
    public static final RandomCollection<Integer> HONG_DAO = new RandomCollection<>();
    public static final RandomCollection<Integer> HONG_DAO_CHIN = new RandomCollection<>();
    public static final RandomCollection<Integer> SAO_PHA_LE = new RandomCollection<>();
    public static final RandomCollection<Integer> RANDOM_EXPIRED_MASK_TET_2016 = new RandomCollection<>();
    public static final RandomCollection<Integer> RANDOM_EXPIRED_HE_2022 = new RandomCollection<>();
    public static final RandomCollection<Integer> EVENT_2016 = new RandomCollection<>();
    public static final RandomCollection<Integer> DA_NANG_CAP = new RandomCollection<>();
    public static final RandomCollection<Integer> BUA = new RandomCollection<>();
    public static final RandomCollection<Integer> GANG_THAN = new RandomCollection<>();
    public static final RandomCollection<Integer> FOOD = new RandomCollection<>();
    public static final RandomCollection<Integer> MOB_NORMAL = new RandomCollection<>();
    public static final RandomCollection<Integer> MOB_NAPPA_COLD_FUTURE = new RandomCollection<>();
    public static final RandomCollection<Integer> EVENT_OPEN = new RandomCollection<>();
    public static final RandomCollection<Integer> DO_THAN_LINH = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_CAP_2 = new RandomCollection<>();
    public static final RandomCollection<Integer> ITEM_CAP_1 = new RandomCollection<>();
    public static final RandomCollection<Integer> RUONG_GO_LEVEL_1 = new RandomCollection<>();
    public static final RandomCollection<Integer> NGOC_RONG_4_7_SAO = new RandomCollection<>();
    public static final RandomCollection<Integer> RUONG_GO_LEVEL_4 = new RandomCollection<>();
    public static final RandomCollection<Integer> RUONG_GO_LEVEL_9 = new RandomCollection<>();
    public static final RandomCollection<Integer> RUONG_GO_LEVEL_10 = new RandomCollection<>();
    public static final RandomCollection<Integer> RUONG_GO_LEVEL_11 = new RandomCollection<>();
    public static final RandomCollection<Integer> RUONG_GO_LEVEL_12 = new RandomCollection<>();
    public static final RandomCollection<Integer> RUONG_GO_LEVEL_13 = new RandomCollection<>();
//    public static final RandomCollection<Integer> XO_CA_VANG = new RandomCollection<>();
//    public static final RandomCollection<Integer> XO_CA_XANH = new RandomCollection<>();

    public static void init() {
        //MOB.add(85, Mob.ItemType.NONE);
        MOB.add(7, Mob.ItemType.GOLD);
        MOB.add(7, Mob.ItemType.ITEM);
        MOB.add(2, Mob.ItemType.EQUIP);
        MOB.add(2, Mob.ItemType.CARD);
        MOB.add(0.01, Mob.ItemType.GEM);
        if (Event.isEvent()) {
            MOB.add(1, Mob.ItemType.EVENT);
        }

        COOLER.add(1, ItemName.AO_DA_CALIC);
        COOLER.add(1, ItemName.AO_BAC_ZEALOT);
        COOLER.add(1, ItemName.AO_KAIO);
        COOLER.add(1, ItemName.QUAN_DA_CALIC);
        COOLER.add(1, ItemName.QUAN_BAC_ZEALOT);
        COOLER.add(1, ItemName.QUAN_KAIO);
        COOLER.add(0.5, ItemName.GANG_DA_CALIC);
        COOLER.add(0.5, ItemName.GANG_BAC_ZEALOT);
        COOLER.add(0.5, ItemName.GANG_KAIO);
        COOLER.add(1, ItemName.GIAY_DA_CALIC);
        COOLER.add(1, ItemName.GIAY_BAC_ZEALOT);
        COOLER.add(1, ItemName.GIAY_KAIO);

        DO_CUOI.add(10, ItemName.AO_LUONG_LONG);
        DO_CUOI.add(20, ItemName.QUAN_LUONG_LONG);
        DO_CUOI.add(20, ItemName.GANG_LUONG_LONG);
        DO_CUOI.add(20, ItemName.GIAY_LUONG_LONG);
        DO_CUOI.add(10, ItemName.AO_JEAN_CALIC);
        DO_CUOI.add(20, ItemName.QUAN_JEAN_CALIC);
        DO_CUOI.add(20, ItemName.GANG_JEAN_CALIC);
        DO_CUOI.add(20, ItemName.GIAY_JEAN_CALIC);
        DO_CUOI.add(10, ItemName.AO_VANG_ZEALOT);
        DO_CUOI.add(20, ItemName.QUAN_VANG_ZEALOT);
        DO_CUOI.add(20, ItemName.GANG_VANG_ZEALOT);
        DO_CUOI.add(20, ItemName.GIAY_VANG_ZEALOT);
        DO_CUOI.add(20, ItemName.RADA_CAP_12);

        BOSS_12H.add(0.1, ItemName.QUAN_THAN_LINH);
        BOSS_12H.add(0.1, ItemName.QUAN_THAN_NAMEC);
        BOSS_12H.add(0.1, ItemName.QUAN_THAN_XAYDA);
        BOSS_12H.add(1, ItemName.AO_DA_CALIC);
        BOSS_12H.add(1, ItemName.AO_BAC_ZEALOT);
        BOSS_12H.add(1, ItemName.AO_KAIO);
        BOSS_12H.add(1, ItemName.QUAN_DA_CALIC);
        BOSS_12H.add(1, ItemName.QUAN_BAC_ZEALOT);
        BOSS_12H.add(1, ItemName.QUAN_KAIO);
        BOSS_12H.add(0.5, ItemName.GANG_DA_CALIC);
        BOSS_12H.add(0.5, ItemName.GANG_BAC_ZEALOT);
        BOSS_12H.add(0.5, ItemName.GANG_KAIO);
        BOSS_12H.add(1, ItemName.GIAY_DA_CALIC);
        BOSS_12H.add(1, ItemName.GIAY_BAC_ZEALOT);
        BOSS_12H.add(1, ItemName.GIAY_KAIO);

        BLACK_GOKU.add(1, ItemName.GIAY_THAN_LINH);
        BLACK_GOKU.add(1, ItemName.GIAY_THAN_NAMEC);
        BLACK_GOKU.add(1, ItemName.GIAY_THAN_XAYDA);

        DRAGONBALL.add(5, ItemName.NGOC_RONG_4_SAO);
        DRAGONBALL.add(10, ItemName.NGOC_RONG_5_SAO);
        DRAGONBALL.add(15, ItemName.NGOC_RONG_6_SAO);
        DRAGONBALL.add(20, ItemName.NGOC_RONG_7_SAO);

        BARRACK_VIP.add(1, ItemName.AO_THAN_LINH);
        BARRACK_VIP.add(1, ItemName.AO_THAN_NAMEC);
        BARRACK_VIP.add(1, ItemName.AO_THAN_XAYDA);

        SQUIDGAME.add(1, ItemName.BUP_BE);
        SQUIDGAME.add(1, ItemName.LINH_BAO_VE_TAM_GIAC);
        SQUIDGAME.add(1, ItemName.LINH_BAO_VE_TRON);
        SQUIDGAME.add(1, ItemName.LINH_BAO_VE_VUONG);

        GINYUFORCE.add(3, ItemName.HONG_NGOC);
        GINYUFORCE.add(2, ItemName.NGOC);
        GINYUFORCE.add(1, ItemName.VE_QUAY_NGOC_VANG);
        GINYUFORCE.add(0.1, ItemName.NGOC_RONG_3_SAO);
        GINYUFORCE.add(0.01, ItemName.NGOC_RONG_2_SAO);

        GALAXYSOLDIER.add(3, ItemName.HONG_NGOC);
        GALAXYSOLDIER.add(2, ItemName.NGOC);
        GALAXYSOLDIER.add(1, ItemName.VE_QUAY_NGOC_VANG);
        GALAXYSOLDIER.add(0.5, ItemName.MANH_VO_BONG_TAI);
        GALAXYSOLDIER.add(0.5, ItemName.DA_XANH_LAM);
        GALAXYSOLDIER.add(0.5, ItemName.MANH_HON_BONG_TAI);

        HONG_DAO.add(10, ItemName.CHU_AN);
        HONG_DAO.add(30, ItemName.CHU_GIAI);
        HONG_DAO.add(50, ItemName.CHU_KHAI);
        HONG_DAO_CHIN.add(1, ItemName.CHU_PHONG);

        HONG_DAO_CHIN.add(30, ItemName.CHU_AN);
        HONG_DAO_CHIN.add(30, ItemName.CHU_GIAI);
        HONG_DAO_CHIN.add(30, ItemName.CHU_KHAI);
        HONG_DAO_CHIN.add(10, ItemName.CHU_PHONG);

        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_964);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_965);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_DAME_TO_HP);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_DAME_TO_MP);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_PST);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_TNSM);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_VANG);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_XG_CAN_CHIET);
        SAO_PHA_LE.add(1, ItemName.SAO_PHA_LE_XG_CHUONG);

        RANDOM_EXPIRED_MASK_TET_2016.add(0.0001, -1);
        RANDOM_EXPIRED_MASK_TET_2016.add(10, 30);
        RANDOM_EXPIRED_MASK_TET_2016.add(20, 14);
        RANDOM_EXPIRED_MASK_TET_2016.add(30, 7);
        RANDOM_EXPIRED_MASK_TET_2016.add(40, 3);

        EVENT_2016.add(40, ItemName.QUA_HONG_DAO);
        EVENT_2016.add(35, ItemName.QUA_HONG_DAO_CHIN);
        EVENT_2016.add(25, ItemName.CHU_PHONG);

        DA_NANG_CAP.add(1, ItemName.DA_LUC_BAO);
        DA_NANG_CAP.add(1, ItemName.DA_RUBY);
        DA_NANG_CAP.add(1, ItemName.DA_SAPHIA);
        DA_NANG_CAP.add(1, ItemName.DA_THACH_ANH_TIM);
        DA_NANG_CAP.add(1, ItemName.DA_TITAN);

        BUA.add(1, ItemName.BUA_BAT_TU);
        BUA.add(1, ItemName.BUA_DA_TRAU);
        BUA.add(1, ItemName.BUA_DEO_DAI);
        BUA.add(1, ItemName.BUA_MANH_ME);
        BUA.add(1, ItemName.BUA_OAI_HUNG);
        BUA.add(1, ItemName.BUA_TRI_TUE);
        BUA.add(1, ItemName.BUA_THU_HUT);

        GANG_THAN.add(1, ItemName.GANG_THAN_LINH);
        GANG_THAN.add(1, ItemName.GANG_THAN_NAMEC);
        GANG_THAN.add(1, ItemName.GANG_THAN_XAYDA);

        FOOD.add(0.8, ItemName.BANH_PUDDING);
        FOOD.add(0.9, ItemName.XUC_XICH);
        FOOD.add(0.7, ItemName.KEM_DAU);
        FOOD.add(1, ItemName.MI_LY);
        FOOD.add(0.5, ItemName.SUSHI);

        RANDOM_EXPIRED_HE_2022.add(0.1, -1);
        RANDOM_EXPIRED_HE_2022.add(10, 30);
        RANDOM_EXPIRED_HE_2022.add(30, 14);
        RANDOM_EXPIRED_HE_2022.add(60, 7);
        RANDOM_EXPIRED_HE_2022.add(100, 3);

        MOB_NORMAL.add(1, ItemName.NGOC_RONG_5_SAO);
        MOB_NORMAL.add(1, ItemName.NGOC_RONG_6_SAO);
        MOB_NORMAL.add(1, ItemName.NGOC_RONG_7_SAO);
        MOB_NORMAL.add(1, ItemName.AO_VAI_THO);
        MOB_NORMAL.add(1, ItemName.AO_VAI_3_LO);
        MOB_NORMAL.add(1, ItemName.AO_SOI_LEN);
        MOB_NORMAL.add(1, ItemName.QUAN_VAI_THO);
        MOB_NORMAL.add(1, ItemName.QUAN_SOI_LEN);
        MOB_NORMAL.add(1, ItemName.QUAN_VAI_DEN);
        MOB_NORMAL.add(1, ItemName.GANG_VAI_THO);
        MOB_NORMAL.add(1, ItemName.GANG_SOI_LEN);
        MOB_NORMAL.add(1, ItemName.GANG_VAI_DEN);
        MOB_NORMAL.add(1, ItemName.GIAY_VAI_THO);
        MOB_NORMAL.add(1, ItemName.GIAY_SOI_LEN);
        MOB_NORMAL.add(1, ItemName.GIAY_NHUA);
        MOB_NORMAL.add(1, ItemName.RADA_CAP_1);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_964);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_965);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_DAME_TO_HP);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_DAME_TO_MP);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_PST);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_TNSM);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_VANG);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_XG_CAN_CHIET);
        MOB_NORMAL.add(1, ItemName.SAO_PHA_LE_XG_CHUONG);

        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.NGOC_RONG_5_SAO);
        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.NGOC_RONG_6_SAO);
        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.NGOC_RONG_7_SAO);
//        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.SAO_PHA_LE_DAME_TO_HP);
//        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.SAO_PHA_LE_DAME_TO_MP);
//        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.SAO_PHA_LE_PST);
//        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.SAO_PHA_LE_TNSM);
//        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.SAO_PHA_LE_VANG);
//        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.SAO_PHA_LE_XG_CAN_CHIET);
//        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.SAO_PHA_LE_XG_CHUONG);
        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.DA_LUC_BAO);
        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.DA_RUBY);
        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.DA_SAPHIA);
        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.DA_THACH_ANH_TIM);
        MOB_NAPPA_COLD_FUTURE.add(1, ItemName.DA_TITAN);

        EVENT_OPEN.add(0, ItemName.CON_CUA);

        DO_THAN_LINH.add(3, ItemName.AO_THAN_LINH);
        DO_THAN_LINH.add(3, ItemName.AO_THAN_NAMEC);
        DO_THAN_LINH.add(3, ItemName.AO_THAN_XAYDA);
        DO_THAN_LINH.add(3, ItemName.QUAN_THAN_LINH);
        DO_THAN_LINH.add(3, ItemName.QUAN_THAN_NAMEC);
        DO_THAN_LINH.add(1, ItemName.QUAN_THAN_XAYDA);
        DO_THAN_LINH.add(1, ItemName.GANG_THAN_LINH);
        DO_THAN_LINH.add(3, ItemName.GANG_THAN_NAMEC);
        DO_THAN_LINH.add(1, ItemName.GANG_THAN_XAYDA);
        DO_THAN_LINH.add(3, ItemName.GIAY_THAN_LINH);
        DO_THAN_LINH.add(1, ItemName.GIAY_THAN_NAMEC);
        DO_THAN_LINH.add(3, ItemName.GIAY_THAN_XAYDA);
        DO_THAN_LINH.add(0.5, ItemName.NHAN_THAN_LINH);

        ITEM_CAP_2.add(1, ItemName.CUONG_NO_2);
        ITEM_CAP_2.add(1, ItemName.BO_KHI_2);
        ITEM_CAP_2.add(1, ItemName.BO_HUYET_2);

        ITEM_CAP_1.add(1, ItemName.CUONG_NO);
        ITEM_CAP_1.add(1, ItemName.BO_KHI);
        ITEM_CAP_1.add(1, ItemName.BO_HUYET);
        ITEM_CAP_1.add(1, ItemName.GIAP_XEN_BO_HUNG);
        ITEM_CAP_1.add(1, ItemName.AN_DANH);

        RUONG_GO_LEVEL_1.add(10, ItemName.MANH_KHUNG_LONG);
        RUONG_GO_LEVEL_1.add(10, ItemName.MANH_LON_LOI);
        RUONG_GO_LEVEL_1.add(10, ItemName.MANH_QUY_DAT);
        RUONG_GO_LEVEL_1.add(10, ItemName.MANH_KHUNG_LONG_ME);
        RUONG_GO_LEVEL_1.add(10, ItemName.MANH_LON_LOI_ME);

        RUONG_GO_LEVEL_4.add(10, ItemName.MANH_QUY_DAT_ME);
        RUONG_GO_LEVEL_4.add(10, ItemName.MANH_THAN_LAN_BAY);
        RUONG_GO_LEVEL_4.add(10, ItemName.MANH_PHI_LONG);
        RUONG_GO_LEVEL_4.add(10, ItemName.MANH_QUY_BAY);
        RUONG_GO_LEVEL_4.add(10, ItemName.MANH_LINH_DOC_NHAN);

        RUONG_GO_LEVEL_9.add(10, ItemName.MANH_LINH_DOC_NHAN2);
        RUONG_GO_LEVEL_9.add(10, ItemName.MANH_TRUNG_UY_TRANG);
        RUONG_GO_LEVEL_9.add(10, ItemName.MANH_TRUNG_UY_XANH_LO);
        RUONG_GO_LEVEL_9.add(10, ItemName.MANH_SOI_XAM);
        RUONG_GO_LEVEL_9.add(10, ItemName.MANH_NINJA_AO_TIM);

    }
}
