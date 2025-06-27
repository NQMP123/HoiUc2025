/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.NQMP;

import com.ngocrong.consts.CMDMenu;
import com.ngocrong.consts.NpcName;
import com.ngocrong.lib.KeyValue;
import com.ngocrong.map.MapManager;
import com.ngocrong.map.TMap;
import com.ngocrong.map.TeamAndroid13;
import com.ngocrong.user.Player;

/**
 *
 * @author Administrator
 */
public class AdminHandle {

    public static final int CMD_Menu_Admin = 1171;
    public static AdminHandle instance = new AdminHandle();

    public static AdminHandle gI() {
        return instance;
    }
    public String[] listBoss = new String[]{
        "TĐST", "Fide Đại Ca", "Android 19 20", "Android 13 14 15", "Pic Poc KK", "Xên bọ hung"
    };

    public void showMenu(Player _c) {
        _c.menus.clear();

        _c.menus.add(new KeyValue(CMD_Menu_Admin, "Spawn Boss", 1));
        _c.menus.add(new KeyValue(CMD_Menu_Admin, "Delete Boss", 2));

        _c.service.openUIConfirm(NpcName.CON_MEO, "Bạn muốn làm gì", _c.getPetAvatar(), _c.menus);
    }

    public void perform(int idAction, Player player, Object... p) {
        switch (idAction) {
            case 1: {
                showBoss(player, 3);
                break;
            }
            case 2: {
                showBoss(player, 4);
                break;
            }
            case 3: {
                var index = ((Integer) p[0]);
                callBoss(index);
                break;
            }
            case 4: {
                var index = ((Integer) p[0]);
                deleteBoss(index);
                break;
            }
        }
    }

    public void showBoss(Player _c, int type) {
        _c.menus.clear();
        for (int i = 0; i < listBoss.length; i++) {
            _c.menus.add(new KeyValue(CMD_Menu_Admin, listBoss[i], type, i));
        }
        _c.service.openUIConfirm(NpcName.CON_MEO, "Chọn Boss muốn thực hiện", _c.getPetAvatar(), _c.menus);
    }

    public void callBoss(int index) {
        if (index == 3) {
            TeamAndroid13 teamAndroid13 = new TeamAndroid13();
            teamAndroid13.born();
        }
    }

    public void deleteBoss(int index) {
        if (index == 3) {
            TMap map = MapManager.getInstance().getMap(104);
            TeamAndroid13.clearAllboss(map);
        }
    }
}
