package com.ngocrong.combine;

import com.ngocrong.server.DragonBall;
import com.ngocrong.server.Server;
import com.ngocrong.consts.CMDMenu;
import com.ngocrong.item.Item;
import com.ngocrong.lib.KeyValue;

public class NhapNgoc extends Combine {

    public NhapNgoc() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vào hành trang").append("\n");
        sb.append("Chọn 7 viên ngọc cùng sao").append("\n");
        sb.append("Sau đó chọn 'Làm phép'");
        setInfo(sb.toString());

        StringBuilder sb2 = new StringBuilder();
        sb2.append("Ta sẽ phù phép").append("\n");
        sb2.append("cho 7 viên Ngọc Rồng").append("\n");
        sb2.append("thành 1 viên Ngọc Rồng cấp cao");
        setInfo2(sb2.toString());
    }

    @Override
    public void showTab() {
        player.service.combine((byte) 0, this, (short) -1, (short) -1);
    }

    @Override
    public void confirm() {
        Server server = DragonBall.getInstance().getServer();
        if (itemCombine == null) {
            return;
        }
        Item item = null;
        if (itemCombine.size() == 1) {
            int index = itemCombine.get(0);
            item = player.itemBag[index];
        }
        if (item == null || !(item.id > 14 && item.id <= 20)) {
            player.service.dialogMessage("Cần 7 viên Ngọc Rồng từ 2 sao trở lên");
        } else {
            String name1 = item.template.name;
            String name2 = server.iTemplates.get(item.id - 1).name;
            String receive = "|2|Con có muốn biến 7 " + name1 + " thành\n1 viên " + name2;
            String require = "Cần 7 " + name1;
            boolean error = false;
            if (item.quantity < 7) {
                require = "|7|" + require;
                error = true;
            } else {
                require = "|1|" + require;
            }
            String info = receive + "\n" + require;
            player.menus.clear();
            if (!error) {
                player.menus.add(new KeyValue(CMDMenu.COMBINE, "Làm phép"));
            }
            player.menus.add(new KeyValue(CMDMenu.CANCEL, "Từ chối"));
            player.service.openUIConfirm(npc.templateId, info, npc.avatar, player.menus);

        }
    }

    @Override
    public void combine() {
        if (itemCombine == null) {
            return;
        }
        Item item = null;
        if (itemCombine.size() == 1) {
            int index = itemCombine.get(0);
            item = player.itemBag[index];
        }
        if (item == null || !(item.id > 14 && item.id <= 20)) {
            player.service.dialogMessage("Cần 7 viên Ngọc Rồng từ 2 sao trở lên");
        } else {
            player.removeItem(item.indexUI, 7);
            Item item2 = new Item(item.id - 1);
            item2.quantity = 1;
            result((byte) 5, item2.template.iconID);
            player.addItem(item2);
            update();
        }
    }

}
