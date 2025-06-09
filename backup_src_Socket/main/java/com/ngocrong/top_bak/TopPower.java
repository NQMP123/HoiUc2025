//package com.ngocrong.top;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.ngocrong.data.PlayerData;
//import com.ngocrong.data.UserData;
//import com.ngocrong.repository.GameRepository;
//import com.ngocrong.server.mysql.MySQLConnect;
//import com.ngocrong.util.Utils;
//import org.apache.log4j.Logger;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.Optional;
//
//public class TopPower extends Top {
//
//    private static final Logger logger = Logger.getLogger(TopPower.class);
//
//    public TopPower(int id, byte type, String name, byte limit) {
//        super(id, type, name, limit);
//    }
//
//    @Override
//    public void load() {
//        try {
//            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement("SELECT `id`, `name`, `head2`, `body`, `leg`, CAST(JSON_UNQUOTE(JSON_EXTRACT(info,\"$.power\")) AS UNSIGNED) AS power FROM `players` ORDER BY power DESC LIMIT 100;");
//            ResultSet rs = ps.executeQuery();
//            try {
//                int i = 0;
//                while (rs.next()) {
//                    int id = rs.getInt("id");
//                    String name = rs.getString("name");
//                    short head = rs.getShort("head2");
//                    short body = rs.getShort("body");
//                    short leg = rs.getShort("leg");
//                    long power = rs.getLong("power");
//                    TopInfo info = new TopInfo();
//                    info.playerID = id;
//                    info.name = name;
//                    info.head = head;
//                    info.body = body;
//                    info.leg = leg;
//                    info.info = String.format("Sức mạnh: %s", Utils.formatNumber(power));
//                    info.info2 = "";
//                    info.score = power;
//                    i++;
//                    elements.add(info);
//                    if (i >= limit) {
//                        break;
//                    }
//                }
//            } finally {
//                rs.close();
//                ps.close();
//            }
//            update();
//            updateLowestScore();
//            //Utils.setTimeout(this::reward, 1000);
//        } catch (Exception ex) { com.ngocrong.NQMP.UtilsNQMP.logError(ex);
//            logger.debug("failed", ex);
//        }
//    }
//
//    public void reward() {
//        try {
//            Gson gson = new Gson();
//            for (int i = 0; i < elements.size(); i++) {
//                TopInfo info = elements.get(i);
//                Optional<UserData> data = GameRepository.getInstance().user.findById(info.playerID);
//                if (!data.isPresent()) {
//                    continue;
//                }
//                ArrayList<Integer> rewards = gson.fromJson(data.get().rewards, new TypeToken<ArrayList<Integer>>() {
//                }.getType());
//                if (i == 0) {
//                    rewards.add(0);
//                    rewards.add(6);
//                } else if (i == 1) {
//                    rewards.add(1);
//                    rewards.add(7);
//                } else if (i == 2) {
//                    rewards.add(2);
//                    rewards.add(8);
//                } else if (i == 3 || i == 4) {
//                    rewards.add(29);
//                    rewards.add(9);
//                } else if (i < 10) {
//                    rewards.add(30);
//                    rewards.add(10);
//                } else {
//                    rewards.add(34);
//                    rewards.add(19);
//                }
//                GameRepository.getInstance().playerData.setRewards(info.playerID, gson.toJson(rewards));
//            }
//            System.out.println("power reward");
//        }
//        catch (Exception ex) { com.ngocrong.NQMP.UtilsNQMP.logError(ex);
//            logger.error("reward", ex);
//        }
//    }
//}
