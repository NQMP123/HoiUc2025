/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.collection;

import com.ngocrong.item.ItemOption;
import com.ngocrong.server.SQLStatement;
import com.ngocrong.server.mysql.MySQLConnect;
import com.google.gson.annotations.SerializedName;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class Card {

    private static Logger logger = Logger.getLogger(Card.class);

    public static ArrayList<CardTemplate> templates;

    public static void loadTemplate() {
        try {
            templates = new ArrayList<>();
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(SQLStatement.LOAD_COLLECTION);
            ResultSet rs = ps.executeQuery();
            try {
                while (rs.next()) {
                    CardTemplate card = new CardTemplate();
                    card.id = rs.getShort("id");
                    card.name = rs.getString("name");
                    card.info = rs.getString("info");
                    card.max_amount = rs.getByte("max_amount");
                    card.icon = rs.getShort("icon");
                    card.rank = rs.getByte("rank");
                    card.type = rs.getByte("type");
                    card.templateID = rs.getShort("template_id");
                    card.head = rs.getShort("head");
                    card.body = rs.getShort("body");
                    card.leg = rs.getShort("leg");
                    card.bag = rs.getShort("bag");
                    card.aura = rs.getShort("aura");
                    card.options = new ArrayList<>();
                    JSONArray jArr = new JSONArray(rs.getString("options"));
                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject obj = jArr.getJSONObject(i);
                        int id = obj.getInt("id");
                        int param = obj.getInt("param");
                        int active_card = obj.getInt("active_card");
                        ItemOption itemOption = new ItemOption(id, param);
                        itemOption.activeCard = (byte) active_card;
                        card.options.add(itemOption);
                    }
                    templates.add(card);
                }
            } finally {
                rs.close();
                ps.close();
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("failed!", ex);
        }
    }

    @SerializedName("id")
    public int id;
    @SerializedName("amount")
    public int amount;
    @SerializedName("level")
    public int level;
    @SerializedName("use")
    public boolean isUse;
    public transient CardTemplate template;

    public void levelUp() {
        this.amount = 0;
        this.level++;
    }

    public void setTemplate() {
        for (CardTemplate t : templates) {
            if (t.id == this.id) {
                this.template = t;
                break;
            }
        }
    }
}
