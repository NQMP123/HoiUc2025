/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.NQMP;

import com.ngocrong.bot.BotCold;
import com.ngocrong.bot.VirtualBot;
import com.ngocrong.bot.VirtualBot_SoSinh;
import com.ngocrong.data.DHVTSieuHangData;
import com.ngocrong.data.SuKienTetData;
import com.ngocrong.item.Item;
import com.ngocrong.map.MapManager;
import com.ngocrong.map.TMap;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.model.RandomItem;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.server.mysql.MySQLConnect;
import com.ngocrong.top.Top;
import com.ngocrong.user.Player;
import static com.ngocrong.user.Player.generateCharacterName;
import com.ngocrong.util.Utils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.json.JSONArray;

/**
 *
 * @author Administrator
 */
public class UtilsNQMP {

    public static void getTopSieuHang(Player player) {
        try {
            Optional<DHVTSieuHangData> rs = GameRepository.getInstance().dhvtSieuHangRepository.findFirstByPlayerId(player.id);
            if (rs != null && rs.isPresent()) {
                DHVTSieuHangData data = rs.get();
                player.pointDhvtSieuhang = data.point;
            } else {
                player.pointDhvtSieuhang = 101;
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            e.printStackTrace();
        }
        try {
            Optional<SuKienTetData> rs = GameRepository.getInstance().eventTet.findFirstByName(player.id);
            if (rs != null && rs.isPresent()) {
                SuKienTetData data = rs.get();
                JSONArray info = new JSONArray(data.point);
                player.pointHoaSumVay = info.getInt(0);
                player.pointHoaSacMau = info.getInt(1);
                player.pointThoiVang = info.getInt(2);
            } else {
                player.pointHoaSumVay = 0;
                player.pointHoaSacMau = 0;
                player.pointThoiVang = 0;
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            e.printStackTrace();
        }
    }

    public static void updateTopSieuHang() {
        Top.updateTop(Top.TOP_DHVT_SIEU_HANG);
    }

    public static int checkDrop() {
        //RadioTest
//        if (Utils.nextInt(1, 100) == 1) {
//            int[] awj = new int[]{
//                555, 556, 557, 558, 559, 563, 567
//            };
//            return RandomItem.DO_THAN_LINH.next();
//        } else if (Utils.nextInt(1, 100) == 1) {
//            return RandomItem.DO_CUOI.next();
//        } else if (Utils.nextInt(1, 100) == 1) {
//            int[] awj = new int[]{
//                650, 651, 658,
//                652, 653, 660,
//                654, 655, 662
//            };
//            return RandomItem.DO_THAN_LINH.next();
//        }
//        if (true) {
//            return -1;
//        }
        if (Utils.nextInt(1, 10000) == 1) {
            int[] awj = new int[]{
                555, 556, 557, 558, 559, 563, 567
            };
            return awj[Utils.nextInt(awj.length)];
        } else if (Utils.nextInt(1, 1000) == 1) {
            return RandomItem.DO_CUOI.next();
        }
//        else if (Utils.nextInt(1, 150000) == 1) {
//            int[] awj = new int[]{
//                650, 651, 658,
//                652, 653, 660,
//                654, 655, 662
//            };
//            return awj[Utils.nextInt(awj.length)];
//        }
        return -1;
    }

    public static long lastCreateBot = 0;

    public static void createBotCold(int quantity, Zone zoneJoin) {
        short head = 873, body = 874, leg = 875;
        for (int i = 0; i < quantity; i++) {
            long hp = Utils.nextInt(500000, 650000);
            long mp = Utils.nextInt(500000, 650000);
            long sd = Utils.nextInt(250000, 350000);
            String name = generateCharacterName();
            BotCold bot = new BotCold(hp, mp, sd, head, body, leg, name, zoneJoin);
            bot.id = Utils.nextInt(1000, 100000);
            bot.gender = 1;
            zoneJoin.enter(bot);

        }
    }

    public static void createBotCold(int quantity, int mapID) {

        short head = 873, body = 874, leg = 875;
        for (int i = 0; i < quantity; i++) {
            long hp = Utils.nextInt(500000, 650000);
            long mp = Utils.nextInt(500000, 650000);
            long sd = Utils.nextInt(250000, 350000);
            TMap map = MapManager.getInstance().getMap(mapID);
            if (map == null) {
                break;
            }
            Zone zone = map.getMinPlayerZone();
            if (zone.players.isEmpty()) {
                String name = generateCharacterName();
                BotCold bot = new BotCold(hp, mp, sd, head, body, leg, name, zone);
                bot.id = Utils.nextInt(1000, 100000);
                bot.gender = 1;
                zone.enter(bot);
            }
        }

    }

    public static void createBot2(int quantity, int mapID) {
        createBot(quantity, mapID, false);
    }

    public static void createBot(int quantity, int mapID) {

        createBot(quantity, mapID, false);

    }

    public static void createBot(int quantity, int mapID, boolean isTrain) {
        lastCreateBot = System.currentTimeMillis();
        short[][] part = new short[][]{
            {1245, 1246, 1247},
            {1248, 1249, 1250},
            {1251, 1252, 1253},
            {1263, 1264, 1265},
            {1254, 1255, 1256},
            {1257, 1258, 1259}};
        for (int i = 0; i < quantity; i++) {
            long hp = Utils.nextInt(200000, 5000000);
            long mp = Utils.nextInt(200000, 5000000);
            long sd = 100;
            short[] randompart = part[Utils.nextInt(part.length)];
            String name = generateCharacterName();
            TMap map = MapManager.getInstance().getMap(mapID);
            if (map == null) {
                break;
            }
            Zone zone = map.getZoneByID(map.randomZoneID());
            if (zone != null) {
                if (zone.getPlayers().size() > zone.maxVirtual) {
                    zone = map.getMinPlayerZone();
                }
            } else {
                zone = map.getMinPlayerZone();
            }
            int head, body, leg = 0;
            VirtualBot bot = new VirtualBot(hp, mp, sd, (short) 0, (short) 0, (short) 0, name, zone);
            if (zone != null && zone.getPlayers().size() <= zone.maxVirtual) {
                bot.isTrain = isTrain;
                bot.gender = (byte) Utils.nextInt(3);
                bot.info.power = Utils.nextLong(20_000_000_000L, 40_000_000_000L);
                bot.id = Utils.nextInt(1000, 100000);
                if (Utils.nextInt(10) >= 5) {
                    bot.setNhapThe(true);
                    bot.typePorata = 0;
                    bot.fusionType = 6;
                    bot.updateSkin();
                } else {
                    bot.setHead(randompart[0]);
                    bot.setBody(randompart[1]);
                    bot.setLeg(randompart[2]);
                }

                zone.enter(bot);
            } else {
                bot.close();
            }

        }
    }

    public static void logError(String log) {
        try {
            System.err.println("Error message: " + log);
            System.err.println("Complete stack trace:");

            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            // Bỏ qua phần tử đầu tiên vì nó là getStackTrace() method
            for (int i = 1; i < stackTraceElements.length; i++) {
                StackTraceElement element = stackTraceElements[i];
                String traceInfo = String.format("    at %s.%s(%s:%d)",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getFileName(),
                        element.getLineNumber());
                System.err.println(traceInfo);
            }
        } catch (Exception e) {
            // Tránh gọi đệ quy logError ở đây
            System.err.println("Error in logError: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void writeLogCloseSession(String playerName) {
        try {
            // Lấy thời gian hiện tại để thêm vào log
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            // Lấy stack trace
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            // Tạo StringBuilder để build nội dung log
            StringBuilder logContent = new StringBuilder();
            // Thêm thông tin tên nhân vật và số lượng item
            logContent.append("\n\nName: ").append(playerName).append("\n");

            logContent.append("Time :").append(timestamp).append("\n");

            // Format và thêm stack trace
            for (int i = 1; i < stackTrace.length; i++) {
                StackTraceElement element = stackTrace[i];
                logContent.append(String.format("  at %s.%s(%s:%d)\n",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getFileName(),
                        element.getLineNumber()
                ));
            }
            logContent.append("=====================================\n\n");

            // Ghi vào file
            try (FileWriter fw = new FileWriter("logCloseSession.txt", true); BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(logContent.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logError(Exception ex) {
        try {
            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
            String errorLocation = String.format("Error at %s.%s(%s:%d)",
                    caller.getClassName(),
                    caller.getMethodName(),
                    caller.getFileName(),
                    caller.getLineNumber());

            System.err.println(errorLocation);
            // Thay vì gọi logger.error() có thể gây vòng lặp
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } catch (Exception e) {
            // Tránh gọi đệ quy logError ở đây
            System.err.println("Error in logError: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getStringTrade(Collection<Item> itemTrade) {
        StringBuilder result = new StringBuilder();
        int lineLength = 0;

        for (Item item : itemTrade) {
            if (item != null) {
                String itemStr = String.format("[%s-%d],", item.template.name, item.quantity);
                if (lineLength + itemStr.length() > 100) {
                    result.append("\n");
                    lineLength = 0;
                }
                result.append(itemStr);
                lineLength += itemStr.length();
            }
        }
        return result.toString();
    }

    public static String getStringTrade(Item[] itemTrade) {
        StringBuilder result = new StringBuilder();
        int lineLength = 0;

        for (Item item : itemTrade) {
            if (item != null) {
                String itemStr = String.format("[%s-%d],", item.template.name, item.quantity);
                if (lineLength + itemStr.length() > 100) {
                    result.append("\n");
                    lineLength = 0;
                }
                result.append(itemStr);
                lineLength += itemStr.length();
            }
        }
        return result.toString();
    }

    public static long getTableChecksum(String tableName) {
        try {
            String query = "CHECKSUM TABLE " + tableName;
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long sum = rs.getLong("Checksum");
                try {
                    ps.close();
                    rs.close();
                } catch (Exception e) {
                }
                System.err.println("CheckSum " + tableName + " : " + sum);
                return sum;
            }
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            e.printStackTrace();
        }
        return 0;
    }

    public static void ExcuteQuery(String query) {
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(query);
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

}
