package com.ngocrong.combine;

import com.ngocrong.bot.Boss;
import com.ngocrong.item.ItemOption;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.util.Utils;
import com.ngocrong.consts.CMDMenu;
import com.ngocrong.consts.ItemName;
import com.ngocrong.item.Item;
import com.ngocrong.lib.KeyValue;
import org.apache.log4j.Logger;

import java.time.Instant;

public class NangPorata extends Combine {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(NangPorata.class);
    public static final int PERCENT = 100;
    public static final int[] REQUIRE = {1, 9999, 20, 50};

    public NangPorata() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vào hành trang");
        sb.append("\n");
        sb.append("Chọn bông tai Porata");
        sb.append("\n");
        sb.append("Chọn mảnh bông tai để nâng cấp, số lượng 9999 cái");
        sb.append("\n");
        sb.append("Sau đó chọn 'Nâng cấp'");
        setInfo(sb.toString());

        StringBuilder sb2 = new StringBuilder();
        sb2.append("Ta sẽ phù phép");
        sb2.append("\n");
        sb2.append("Cho bông tai Porata của ngươi");
        sb2.append("\n");
        sb2.append("thành cấp 2");
        setInfo2(sb2.toString());
    }

    @Override
    public void showTab() {
        player.service.combine((byte) 0, this, (short) -1, (short) -1);
    }

    @Override
    public void confirm() {
        if (itemCombine == null) {
            return;
        }
        if (player.isNhapThe()) {
            player.service.dialogMessage("Vui lòng tách hợp thể ra trước");
            return;
        }
        String text = String.format("Cần %d bông tai Porata và %d mảnh vỡ bông tai và 20 thỏi vàng", REQUIRE[0], REQUIRE[1]);
        if (itemCombine.size() != 3) {
            player.service.dialogMessage(text);
            return;
        }
        Item[] items = new Item[3];
        for (byte index : this.itemCombine) {
            Item item = player.itemBag[index];
            if (item != null) {
                if (item.template.id == 454) {
                    items[0] = item;
                }
                if (item.template.id == 933) {
                    items[1] = item;
                }
                if (item.template.id == 457) {
                    items[2] = item;
                }
            }
        }
        if (items[0] == null || items[1] == null || items[2] == null) {
            player.service.dialogMessage(text);
            return;
        }
        if (items[2] == null || items[2].quantity < REQUIRE[2]) {
            player.service.dialogMessage("Cần thêm 20 thỏi vàng");
            return;
        }
        if (items[0].quantity < REQUIRE[0] || items[1].quantity < REQUIRE[1]) {
            player.service.dialogMessage(text);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("|2|").append(String.format("%s [+%d]", items[0].template.name, 2)).append("\n");
        sb.append(String.format("Tỉ lệ thành công: %d%%", PERCENT)).append("\n");
        sb.append(String.format("Cần %d Mảnh vỡ bông tai", REQUIRE[1])).append("\n");
        sb.append("|7|").append(String.format("Thất bại -%d Mảnh vỡ bông tai", REQUIRE[3]));
        player.menus.clear();
        player.menus.add(new KeyValue(CMDMenu.COMBINE, String.format("Nâng cấp\n%s thỏi vàng", Utils.formatNumber(REQUIRE[2]))));
        player.menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
        player.service.openUIConfirm(npc.templateId, sb.toString(), npc.avatar, player.menus);
    }

    @Override
    public void combine() {
        if (itemCombine == null) {
            return;
        }
        if (player.isNhapThe()) {
            player.service.dialogMessage("Vui lòng tách hợp thể ra trước");
            return;
        }
        String text = String.format("Cần %d bông tai Porata và %d mảnh vỡ bông tai và 20 thỏi vàng", REQUIRE[0], REQUIRE[1]);
        if (itemCombine.size() != 3) {
            player.service.dialogMessage(text);
            return;
        }
        Item[] items = new Item[2];
        for (byte index : this.itemCombine) {
            Item item = player.itemBag[index];
            if (item != null) {
                if (item.template.id == ItemName.BONG_TAI_PORATA) {
                    items[0] = item;
                }
                if (item.template.id == ItemName.MANH_VO_BONG_TAI) {
                    items[1] = item;
                }
            }
        }
        if (items[0] == null || items[1] == null) {
            player.service.dialogMessage(text);
            return;
        }
//        ItemOption mvbt = null;
//        for (ItemOption o : items[1].options) {
//            if (o.optionTemplate.id == 31) {
//                mvbt = o;
//                break;
//            }
//        }
        int indexGoldBar = player.getIndexBagById(ItemName.THOI_VANG);
        if (indexGoldBar < 0) {
            player.service.serverMessage2("Không đủ thỏi vàng");
            return;
        }
        Item tv = player.itemBag[indexGoldBar];

        if (items[0].quantity < REQUIRE[0] || items[1].quantity < REQUIRE[1]) {
            player.service.dialogMessage(text);
            return;
        }
        if (REQUIRE[2] > tv.quantity) {
            player.service.serverMessage2("Không đủ thỏi vàng");
            return;
        }
        player.pointThoiVang += REQUIRE[2];
        player.isChangePoint = true;
        player.removeItem(indexGoldBar, REQUIRE[2]);
        if (true) {
            items[1].quantity -= REQUIRE[1];
            Item item = new Item(ItemName.BONG_TAI_PORATA_CAP_2);
            item.quantity = 1;
            item.addItemOption(new ItemOption(72, 2));
            item.indexUI = items[0].indexUI;
            player.itemBag[item.indexUI] = item;
            result((byte) 2);
        } else {
            items[1].quantity -= REQUIRE[3];
            result((byte) 3);
        }
        if (items[1].quantity <= 0) {
            player.itemBag[items[1].indexUI] = null;
        }
        player.service.setItemBag();
        update();
    }

}
