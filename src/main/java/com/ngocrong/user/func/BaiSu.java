/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.user.func;

import com.ngocrong.NQMP.UtilsNQMP;
import com.ngocrong.consts.NpcName;
import com.ngocrong.lib.KeyValue;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;

/**
 *
 * @author Administrator
 */
public class BaiSu {

    public static void Action(Player player1, Player player2) {
        if (player1 == null || player2 == null) {
            return;
        }

        if (player1.baiSu_id != -1) {
            if (player1.baiSu_id == player2.id) {
                player1.service.sendThongBao("2 Bạn đã là sư đồ với nhau rồi");

            } else {
                player1.service.sendThongBao("Bạn đã có sư - đệ rồi");
            }
            return;
        }
        if (player2.baiSu_id != -1) {

            return;
        }
        player1.service.sendThongBao("Đã gửi lời mời rồi , hãy chờ đối phương đồng ý");

        var menus = player2.menus;
        menus.add(new KeyValue(303, "Đồng ý", player1.id));
        menus.add(new KeyValue(-1, "Từ chối", player1));
        player2.service.openUIConfirm(NpcName.CON_MEO, String.format("%s muốn bái bạn làm sư phụ\nbạn có đồng ý không ?\n"
                + "Khi cả 2 là sư đồ với nhau và train với nhau sẽ được tăng TNSM",
                player1.name), player2.getPetAvatar(), menus);
    }

    public static void accept(Player player1, Player player2) {
        player1.baiSu_id = player2.id;
        player2.baiSu_id = player1.id;
        insert(player1.id, player2.id);
    }

    public static void insert(int id1, int id2) {
        UtilsNQMP.ExcuteQuery(String.format("INSERT INTO `nrobaby`.`nr_baisu` (`player1`, `player2`) VALUES (%d, %d);", id1, id2));
    }
}
