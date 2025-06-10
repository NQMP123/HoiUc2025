/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.combine;

import com.ngocrong.consts.CMDMenu;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemOption;
import com.ngocrong.lib.KeyValue;
import com.ngocrong.util.Utils;

/**
 *
 * @author Administrator
 */
public class DoiDoKichHoat extends Combine {

    static final int goldRequire = 500_000_000;

    public DoiDoKichHoat() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vào hành trang").append("\n");
        sb.append("Chọn 1 món đồ Hủy Diệt").append("\n");
        sb.append("Sau đó chọn 'Đổi Đồ'");
        setInfo(sb.toString());

        StringBuilder sb2 = new StringBuilder();
        sb2.append("Ta sẽ phù phép").append("\n");
        sb2.append("Biến đồ Hủy Diệt thành Đồ Kích hoạt").append("\n");
        setInfo2(sb2.toString());
    }
    public int type;

    @Override
    public void confirm() {
        if (itemCombine == null) {
            return;
        }
        if (itemCombine.size() != 1) {
            player.service.dialogMessage("Số lượng trang bị không hợp lệ");
            return;
        }
        Item item = player.itemBag[itemCombine.get(0)];
        if (item == null || item.template.type >= 5 || item.template.level != 14) {
            player.service.dialogMessage("Vật phẩm không hợp lệ");
            return;
        }
        String info = "Sau khi cường hóa, sẽ nhận được 1 trang bị kích hoạt ngẫu nhiên (cần 500tr vàng)";
        player.menus.clear();
        if (item.template.type != 4) {
            player.menus.add(new KeyValue(CMDMenu.COMBINE, "Đồng ý\n500tr vàng", -1));
        } else {
            player.menus.add(new KeyValue(CMDMenu.COMBINE, "Chỉ Số SKH\nTrái Đất", 0));
            player.menus.add(new KeyValue(CMDMenu.COMBINE, "Chỉ Số SKH\nNamec", 1));
            player.menus.add(new KeyValue(CMDMenu.COMBINE, "Chỉ Số SKH\nXayda", 2));

        }
        player.menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
        player.service.openUIConfirm(npc.templateId, info, npc.avatar, player.menus);
    }

    static int getTempId(Item item) {
        int[][] ao = new int[][]{
            {0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233},
            {1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237},
            {2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241}
        };
        int[][] quan = new int[][]{
            {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245},
            {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249},
            {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253}
        };
        int[][] gang = new int[][]{
            {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257},
            {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261},
            {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265}
        };
        int[][] giay = new int[][]{
            {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269},
            {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273},
            {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277}
        };
        int[] rd = new int[]{12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281};
        int[] temp = new int[]{};
        switch (item.template.type) {
            case 0:
                temp = ao[item.template.gender];
                break;
            case 1:
                temp = quan[item.template.gender];
                break;
            case 2:
                temp = gang[item.template.gender];
                break;
            case 3:
                temp = giay[item.template.gender];
                break;
            case 4:
                temp = rd;
        }
        return temp[0];
    }
    public static final int[][][] OPTIONS = {
        {
            {127, 139}, {128, 140}, {129, 141}
        }, {
            {130, 142}, {131, 143}, {132, 144}
        }, {
            {133, 136}, {134, 137}, {135, 138}
        }
    };

    @Override
    public void combine() {
        if (itemCombine == null) {
            return;
        }
        if (itemCombine.size() != 1) {
            player.service.dialogMessage("Số lượng trang bị không hợp lệ");
            return;
        }
        Item item = player.itemBag[itemCombine.get(0)];
        if (item == null || item.template.type >= 5 || item.template.level != 14) {
            player.service.dialogMessage("Vật phẩm không hợp lệ");
            return;
        }
        if (player.gold < 500000000) {
            player.service.sendThongBao("Bạn không đủ vàng");
            return;
        }
        player.addGold(-500000000);
        short oldIcon = item.template.iconID;
        result((byte) 2, oldIcon, item.template.iconID);
        item = new Item(getTempId(item));
        item.quantity = 1;
        item.setDefaultOptions();
        int index = Utils.nextInt(3);
        if (item.template.gender == 3 && type != -1) {
            item.addItemOption(new ItemOption(OPTIONS[type][index][0], 0));
            item.addItemOption(new ItemOption(OPTIONS[type][index][1], 0));
        } else {
            item.addItemOption(new ItemOption(OPTIONS[item.template.gender][index][0], 0));
            item.addItemOption(new ItemOption(OPTIONS[item.template.gender][index][1], 0));
        }
        item.addItemOption(new ItemOption(30, 0));
        item.indexUI = itemCombine.get(0);
        player.itemBag[item.indexUI] = item;
        player.service.refreshItem((byte) 1, item);
        update();

    }

    @Override
    public void showTab() {
        player.service.combine((byte) 0, this, (short) -1, (short) -1);
    }

}
