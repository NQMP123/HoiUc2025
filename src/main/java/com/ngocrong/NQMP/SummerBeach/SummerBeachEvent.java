package com.ngocrong.NQMP.SummerBeach;

import com.ngocrong.consts.ItemName;
import com.ngocrong.consts.MapName;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemMap;
import com.ngocrong.lib.KeyValue;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;

public class SummerBeachEvent {
    public static void mobReward(Player player) {
        if (player == null || player.zone == null) {
            return;
        }
        if (player.zone.map.mapID != MapName.BAI_BIEN_NGAY_HE) {
            return;
        }
        if (Utils.isTrue(10, 100)) {
            Item it = new Item(ItemName._SAO_BIEN);
            it.setDefaultOptions();
            it.quantity = 1;
            ItemMap map = new ItemMap(player.zone.autoIncrease++);
            map.item = it;
            map.playerID = Math.abs(player.id);
            map.x = player.getX();
            map.y = player.zone.map.collisionLand(player.getX(), player.getY());
            player.zone.addItemMap(map);
            player.zone.service.addItemMap(map);
        } else if (Utils.isTrue(1, 1000)) {
            Item it = new Item(ItemName.MANH_CAPSULE_VIPPRO);
            it.setDefaultOptions();
            it.quantity = 1;
            ItemMap map = new ItemMap(player.zone.autoIncrease++);
            map.item = it;
            map.playerID = Math.abs(player.id);
            map.x = player.getX();
            map.y = player.zone.map.collisionLand(player.getX(), player.getY());
            player.zone.addItemMap(map);
            player.zone.service.addItemMap(map);
        }
    }

    public static boolean useItem(Player player, Item item) {
        if (player == null || item == null) {
            return false;
        }
        if (item.template.id == ItemName.CAPSULE_VIPPRO) {
            player.setListMap();
            player.listMapTransport.add(new KeyValue(MapName.BAI_BIEN_NGAY_HE, "Bãi biển", "Sự kiện"));
            player.listMapTransport.add(new KeyValue(MapName.VO_DAI_XEN_BO_HUNG, "Võ đài Siêu Bọ Hung", "Sự kiện"));
            player.listMapTransport.add(new KeyValue(MapName.HANG_BANG, "Hang Băng", "Sự kiện"));
            player.capsule = item.indexUI;
            player.service.mapTransport(player.listMapTransport);
            return true;
        }
        return false;
    }
}
