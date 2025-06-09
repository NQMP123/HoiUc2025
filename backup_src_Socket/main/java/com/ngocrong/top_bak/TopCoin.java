//package com.ngocrong.top;
//
//import com.ngocrong.server.mysql.MySQLConnect;
//import com.ngocrong.util.Utils;
//import org.apache.log4j.Logger;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class TopCoin extends Top {
//
//    private static final Logger logger = Logger.getLogger(TopCoin.class);
//
//    public TopCoin(int id, byte type, String name, byte limit) {
//        super(id, type, name, limit);
//    }
//
//    @Override
//    public void load() {
//        try {
//            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement("SELECT `id`, `name`, `head2`, `body`, `leg`, `gold` FROM `players` ORDER BY gold DESC LIMIT 100;");
//            ResultSet rs = ps.executeQuery();
//            try {
//                int i = 0;
//                while (rs.next()) {
//                    int id = rs.getInt("id");
//                    String name = rs.getString("name");
//                    short head = rs.getShort("head2");
//                    short body = rs.getShort("body");
//                    short leg = rs.getShort("leg");
//                    long gold = rs.getLong("gold");
//                    TopInfo info = new TopInfo();
//                    info.playerID = id;
//                    info.name = name;
//                    info.head = head;
//                    info.body = body;
//                    info.leg = leg;
//                    info.info = String.format("Vàng: %s", Utils.formatNumber(gold));
//                    info.info2 = "";
//                    info.score = gold;
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
//        } catch (Exception ex) { com.ngocrong.NQMP.UtilsNQMP.logError(ex);
//            logger.debug("failed", ex);
//        }
//    }
//
//    @Override
//    public void update() {
//        elements.clear();
//        load();
//    }
//}
