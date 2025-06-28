/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.top.AutoReward;

import com.ngocrong.item.Item;
import com.ngocrong.item.ItemOption;
import com.ngocrong.server.mysql.MySQLConnect;
import com.ngocrong.user.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject; 

/**
 *
 * @author Administrator
 */
public class AutoReward {

    public List<TopReward> list = new ArrayList<>();
    public static AutoReward instance;

    public static AutoReward gI() {
        if (instance == null) {
            instance = new AutoReward();
        }
        return instance;
    }

    private static final String FIND_ITEMS_QUERY = "SELECT * FROM nr_rewardtop ORDER BY `id` DESC";

    public static List<Item> jsontoItems(String json) {
        List<Item> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemObj = jsonArray.getJSONObject(i);

                int idItem = itemObj.optInt("idItem");
                try {
                    Item item = new Item(idItem);
                    item.quantity = itemObj.optInt("quantity", 1);
                    if (itemObj.has("options")) {
                        JSONArray optionsArray = itemObj.getJSONArray("options");
                        for (int j = 0; j < optionsArray.length(); j++) {
                            JSONObject optionObj = optionsArray.getJSONObject(j);
                            ItemOption option = new ItemOption(optionObj.optInt("id"), optionObj.optInt("param"));
                            item.options.add(option);
                        }
                    }
                    if (item.options.isEmpty()) {
                        item.setDefaultOptions();
                    }
                    items.add(item);
                } catch (Exception e) {
                    System.err.println("Error at id : " + idItem);
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void loadList() {
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(FIND_ITEMS_QUERY);
            ResultSet rs = ps.executeQuery();
            list.clear();
            try {
                while (rs.next()) {
                    TopReward top = new TopReward();
                    top.id = rs.getInt("id");
                    top.namePlayer = rs.getString("player_name");
                    top.isReward = rs.getByte("isReward") == 1;
                    top.infoTop = rs.getString("infoTop");
                    top.item = jsontoItems(rs.getString("item"));
                    list.add(top);
                }
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
                e.printStackTrace();

            } finally {
                rs.close();
                ps.close();
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
        }
    }
    private static final String UPDATE_STATUS_QUERY = "UPDATE nr_rewardtop SET isReward = 1 WHERE id = ?";

    public void setReward(int id) {
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(UPDATE_STATUS_QUERY);
            ps.setInt(1, id);
            try {
                int updated = ps.executeUpdate();
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
            } finally {
                ps.close();
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
        }
    }

    public void checkAndReward(Player player) {
        if (player == null) {
            return;
        }
        if (list.isEmpty()) {
            this.loadList();
        }
        TopReward topReward = list.stream()
                .filter(top -> top.namePlayer.equals(player.name) && !top.isReward)
                .findFirst()
                .orElse(null);
        if (topReward == null || topReward.isReward) {
            return;
        }
        try {
            if (topReward.item != null && !topReward.item.isEmpty()) {
                int slotsNeeded = topReward.item.size();
                if (player.getCountEmptyBag() < slotsNeeded) {
                    player.service.dialogMessage("Hành trang không đủ " + slotsNeeded + " ô trống! để nhận thưởng Top , hãy kiểm tra và thoát game vào lại");
                    return;
                }
                String message = "Bạn đã nhận được phần thưởng " + topReward.infoTop;

                for (Item item : topReward.item) {
                    message += "\nx" + item.quantity + item.template.name;
                    if (item.template.id == 2305) {
                        for (int i = 2275; i <= 2277; i++) {
                            Item itemNew = new Item(i);
                            itemNew.quantity = item.quantity / 3;
                            player.addItem(itemNew);
                        }
                        continue;
                    }
                    player.addItem(item);
                }
                setReward(topReward.id);
                topReward.isReward = true;
                player.service.dialogMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
