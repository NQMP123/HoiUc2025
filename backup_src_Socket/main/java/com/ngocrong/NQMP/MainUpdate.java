/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.NQMP;

import com.ngocrong.NQMP.Mabu14H.Bu_Map;
import static com.ngocrong.NQMP.Mabu14H.Bu_Map.list;
import com.ngocrong.NQMP.Whis.RewardWhis;
import com.ngocrong.bot.BotCold;
import com.ngocrong.bot.VirtualBot;
import com.ngocrong.bot.VirtualBot_SoSinh;
import com.ngocrong.clan.ClanImage;
import com.ngocrong.consts.MapName;
import com.ngocrong.item.Item;
import com.ngocrong.map.MapManager;
import com.ngocrong.map.TMap;
import com.ngocrong.map.tzone.Zone;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.server.DragonBall;
import com.ngocrong.server.SQLStatement;
import com.ngocrong.server.Server;
import com.ngocrong.server.ServerMaintenance;
import com.ngocrong.server.SessionManager;
import com.ngocrong.server.mysql.MySQLConnect;
import com.ngocrong.top.Top;
import com.ngocrong.top.TopInfo;
import com.ngocrong.user.Player;
import static com.ngocrong.user.Player.generateCharacterName;
import com.ngocrong.util.Utils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.json.JSONArray;

/**
 *
 * @author Administrator
 */
public class MainUpdate implements Runnable {

    static boolean isResetDHVT = false;
    static boolean isRewardDHVT = false;
    public static LocalDateTime now;

    public static void updateMabu14H() {
         Bu_Map.initBoss();
        if (list != null && !list.isEmpty()) {
            // Iterate over a copy of the list to avoid ConcurrentModificationException
            // if bu.update() can modify the original list (e.g., remove boss upon death).
            List<Bu_Map> currentBuList = new ArrayList<>(list);
            for (Bu_Map bu : currentBuList) {
                if (bu != null) { // Safety check
                    bu.update();
                }
            }
        }
    }

    public static void ResetDHVT() {

        try {
            GameRepository.getInstance().gameEventRepository.resetDHVT();
            GameRepository.getInstance().gameEventRepository.resetDHVT_SieuHang();
            for (Player player : SessionManager.getPlayers()) {
                player.roundDHVT23 = 0;
                player.timesOfDHVT23 = 0;
                player.setGetChest(false);
                player.countDhvtSieuHang = 1;
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("failed!", ex);

        }
    }

    public static void RewardDHVT23() {
//        Top topDHVT = Top.getTop(Top.TOP_DHVT_SIEU_HANG);
//        for (TopInfo info : topDHVT.getElements()) {
//            Player player = SessionManager.findChar(info.playerID);
//            if (player != null && player.zone != null && System.currentTimeMillis() - player.lastLogin >= 5000) {
//                if (!info.isReward) {
//                    info.isReward = true;
//                    short goldReward;
//                    if (info.rank == 1) {
//                        goldReward = 200;
//                    } else if (info.rank < 10) {
//                        goldReward = 50;
//                    } else if (info.rank < 50) {
//                        goldReward = 10;
//                    } else {
//                        goldReward = 0;
//                    }
//                    if (goldReward > 0) {
//                        GameRepository.getInstance().dhvtSieuHangRepository.setReward(info.playerID, 1);
//                        Item thoivang = new Item(457);
//                        thoivang.quantity = goldReward;
//                        player.addItem(thoivang);
//                        player.service.dialogMessage(String.format(
//                                "Bạn đạt Top %d ở Giải Đấu Siêu Hạng\n"
//                                + "Bạn nhận được %d thỏi vàng", info.rank, goldReward));
//                    }
//                }
//            }
//        }
    }

    private static final Logger logger = Logger.getLogger(MainUpdate.class);
    static boolean isSupportMisson;

    static long initCold = System.currentTimeMillis();
    static long initBot = System.currentTimeMillis();

    public static void update() {
        now = LocalDateTime.now();
        isSupportMisson = now.getHour() == 17 || now.getHour() == 18 || now.getHour() == 19;
        DHVT23_Service.update();
        ConSoMayMan.update();
        checkKickOut();
        if (!isResetDHVT && now.getHour() == 0 && now.getMinute() == 0 && now.getSecond() == 0) {
            isResetDHVT = true;
            ResetDHVT();
        }
        if (now.getHour() >= 20) {
            RewardDHVT23();
        }
        if (System.currentTimeMillis() - UtilsNQMP.lastCreateBot >= 1500) {
            VirtualBot_SoSinh bot = new VirtualBot_SoSinh(generateCharacterName());
            bot.setLocation(0, -1);
            UtilsNQMP.lastCreateBot = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - initCold >= 15000 && BotCold.TotalBotCold < 50) {
            initCold = System.currentTimeMillis();
            int[] map = new int[]{105, 106, 107, 108, 109, 110};
            TMap map2 = MapManager.getInstance().getMap(map[Utils.nextInt(map.length)]);
            UtilsNQMP.createBotCold(1, map2.mapID);
        }
        if (System.currentTimeMillis() - initBot >= 5000 && VirtualBot.TotalBot < 250) {
            initBot = System.currentTimeMillis();
            int[] map = new int[]{0, 7, 14, 5};
            VirtualBot.TotalBot++;
            UtilsNQMP.createBot(1, map[Utils.nextInt(map.length)]);
        }

//        System.err.println("Hour:"+now.getHour());
    }

    static void setBaoTri() {
        MainUpdate.runTaskDay(() -> {
            ServerMaintenance.BaoTri(5 * 60);
        }, "03:00");
    }

    static void setRewardWhis() {
        MainUpdate.runTaskDay(() -> {
            RewardWhis.reward();
        }, "00:00");
    }

    static void setMabu14h() {
        MainUpdate.runTaskDay(() -> {
            try {
                logger.info("Attempting Mabu 14H event: Initializing bosses...");
                Bu_Map.initBoss();
                logger.info("Mabu 14H event: Successfully initialized bosses.");
            } catch (Exception e) {
                logger.error("Error during Mabu 14H initialization: ", e);
            }
        }, "14:00");
    }

    @Override
    public void run() {
        Server server = DragonBall.getInstance().getServer();
        setBaoTri();
        setRewardWhis();
        setMabu14h(); 
        while (server.start) {
            try {
                Thread.sleep(100);
                update();
            } catch (InterruptedException ex) {
                com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                logger.error("failed!", ex);
            }
        }
    }

    public static boolean CanEnterZoneSupportMisson(Player player, Zone zone) {
        if (!isSupportMisson) {
            return true;
        }

        if (zone.map.mapID == 79 || zone.map.mapID == 82 || zone.map.mapID == 83) {
            if (player.taskMain != null && player.taskMain.id != 20 && !zone.getBossInZone().isEmpty()) {
                return false;
            }
        }
        if (zone.map.mapID == 80) {
            if (player.taskMain != null && player.taskMain.id != 21 && !zone.getBossInZone().isEmpty()) {
                return false;
            }
        }
        if (zone.map.mapID == 97) {
            if (player.taskMain != null && player.taskMain.id != 24 && !zone.getBossInZone().isEmpty()) {
                return false;
            }
        }
        if (zone.map.mapID == 100) {
            if (player.taskMain != null && player.taskMain.id != 25 && !zone.getBossInZone().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void checkKickOut() {
        try {
            int hour = now.getHour();
            if (hour != 14) {
                TMap map = MapManager.getInstance().getMap(MapName.CONG_PHI_THUYEN_2);
                if (map == null) {
                    return;
                }

                // Tạo danh sách tạm để tránh ConcurrentModificationException
                List<Player> playersToKick = new ArrayList<>();

                // Thu thập người chơi cần kick
                for (Zone zone : map.zones) {
                    if (zone != null) {
                        playersToKick.addAll(zone.players.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()));
                    }
                }

                // Thực hiện teleport cho từng người chơi
                for (Player player : playersToKick) {
                    try {
                        if (player != null && player.getSession() != null && player.getSession().user.getRole() != 1) {
                            player.teleport(0);
                        }
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);

                    }
                }
            }
            if (hour != 22) {
                TMap map = MapManager.getInstance().getMap(126);
                if (map == null) {
                    return;
                }

                // Tạo danh sách tạm để tránh ConcurrentModificationException
                List<Player> playersToKick = new ArrayList<>();

                // Thu thập người chơi cần kick
                for (Zone zone : map.zones) {
                    if (zone != null) {
                        playersToKick.addAll(zone.players.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()));
                    }
                }

                // Thực hiện teleport cho từng người chơi
                for (Player player : playersToKick) {
                    try {
                        if (player != null && player.getSession() != null && player.getSession().user.getRole() != 1) {
                            player.teleport(0);
                        }
                    } catch (Exception e) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(e);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetDay() {
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement("update nr_player set drop_item = '{}'");
            try {
                int updated = ps.executeUpdate();
            } catch (Exception e) {
                com.ngocrong.NQMP.UtilsNQMP.logError(e);
            } finally {
                ps.close();
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("Lỗi kết nối khi resetDay", ex);
        }

    }

    public static void runTaskDay(Runnable task, String time) {
        try {
            String callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            LocalDateTime localNow = LocalDateTime.now();
            ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
            ZonedDateTime nextRun = zonedNow.withHour(hour).withMinute(minute).withSecond(0);
            if (zonedNow.compareTo(nextRun) > 0) {
                nextRun = nextRun.plusDays(1);
            }
            Duration duration = Duration.between(zonedNow, nextRun);
            long initialDelay = duration.getSeconds();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(task, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
            System.out.println("Đã lên lịch task " + callerMethodName + " chạy lúc " + time + " mỗi ngày");
        } catch (Exception e) {
            String callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            System.err.println("Lỗi khi lên lịch task " + callerMethodName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runTaskDayInWindow(Runnable task, String timeStart, String timeEnd) {
        try {
            String callerMethodName = "UnnamedTaskInWindow";
            try {
                // Cố gắng lấy tên phương thức đã gọi để làm định danh cho task
                callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            } catch (Exception ignored) {}

            String[] startParts = timeStart.split(":");
            int startHour = Integer.parseInt(startParts[0]);
            int startMinute = Integer.parseInt(startParts[1]);

            String[] endParts = timeEnd.split(":");
            int endHour = Integer.parseInt(endParts[0]);
            int endMinute = Integer.parseInt(endParts[1]);

            ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime zonedNow = ZonedDateTime.now(currentZone);
            LocalTime localNow = zonedNow.toLocalTime();

            LocalTime localTimeStart = LocalTime.of(startHour, startMinute);
            LocalTime localTimeEnd = LocalTime.of(endHour, endMinute);

            // --- Kiểm tra và thực thi ngay lập tức ---
            if (localTimeStart.isAfter(localTimeEnd)) {
                // Trường hợp timeStart sau timeEnd (ví dụ: 22:00 đến 02:00)
                // Logic thực thi ngay có thể cần phức tạp hơn cho trường hợp qua đêm.
                // Hiện tại, chỉ cảnh báo và không thực thi ngay theo logic đơn giản.
                System.err.println("Cảnh báo cho task " + callerMethodName + ": timeStart (" + timeStart + 
                                   ") sau timeEnd (" + timeEnd + 
                                   "). Logic thực thi ngay lập tức có thể không hoạt động như mong đợi cho cửa sổ qua đêm.");
                // Nếu muốn xử lý cửa sổ qua đêm cho thực thi ngay:
                // if (!localNow.isBefore(localTimeStart) || localNow.isBefore(localTimeEnd)) {
                //     System.out.println("Task " + callerMethodName + " (cửa sổ qua đêm) đang chạy ngay lập tức...");
                //     task.run();
                // }
            } else {
                // Trường hợp cửa sổ chuẩn trong ngày: thời_gian_hiện_tại >= timeStart VÀ thời_gian_hiện_tại < timeEnd
                if (!localNow.isBefore(localTimeStart) && localNow.isBefore(localTimeEnd)) {
                    System.out.println("Task " + callerMethodName + " đang chạy ngay lập tức (thời gian hiện tại " + 
                                       localNow + " nằm trong khoảng " + timeStart + "-" + timeEnd + ")");
                    try {
                        task.run();
                    } catch (Exception e) {
                        System.err.println("Lỗi khi thực thi task " + callerMethodName + " ngay lập tức: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // --- Lên lịch chạy hàng ngày vào timeStart ---
            ZonedDateTime nextScheduledRunAtStartTime = zonedNow.withHour(startHour).withMinute(startMinute).withSecond(0).withNano(0);

            if (zonedNow.isAfter(nextScheduledRunAtStartTime)) {
                // Nếu thời gian hiện tại đã qua timeStart của hôm nay, lên lịch cho timeStart ngày mai.
                nextScheduledRunAtStartTime = nextScheduledRunAtStartTime.plusDays(1);
            }
            // Ngược lại, nó sẽ chạy vào timeStart của hôm nay.

            Duration initialDelayDuration = Duration.between(zonedNow, nextScheduledRunAtStartTime);
            long initialDelaySeconds = initialDelayDuration.getSeconds();

            if (initialDelaySeconds < 0) { // Đảm bảo initialDelay không âm
                initialDelaySeconds = 0;
            }

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(task, initialDelaySeconds, 24 * 60 * 60, TimeUnit.SECONDS);

            System.out.println("Đã lên lịch task '" + callerMethodName + "' để chạy hàng ngày vào lúc " + timeStart + ".");
            System.out.println("Lần chạy theo lịch tiếp theo dự kiến vào: " + 
                               nextScheduledRunAtStartTime.toString() + 
                               " (sau khoảng " + initialDelaySeconds + " giây tính từ " + zonedNow.toString() + ")");

        } catch (Exception e) {
            String tempCallerName = "unknown_task_setup";
            try {
                tempCallerName = Thread.currentThread().getStackTrace()[2].getMethodName();
            } catch (Exception ignored) {}
            System.err.println("Lỗi nghiêm trọng khi thiết lập runTaskDayInWindow cho '" + tempCallerName + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

}
