package com.ngocrong.event;

import com.ngocrong.consts.ItemName;
import com.ngocrong.data.OsinCheckInData;
import com.ngocrong.item.Item;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.repository.OsinCheckInRepository;
import com.ngocrong.server.SessionManager;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

// LƯU Ý QUAN TRỌNG CHO NGƯỜI DÙNG:
// Các lỗi linter bạn thấy sau khi áp dụng thay đổi này có thể là do các phần sau CHƯA được triển khai
// hoặc không thể truy cập được từ file này. Vui lòng kiểm tra và hoàn thiện chúng:
// 1. Trong OsinCheckInRepository:
//    - public [return_type] save(OsinCheckInData data) // Phải hỗ trợ cả insert và update
//    - public int countToday()
//    - public Optional<OsinCheckInData> findTodayByPlayer(long playerId)
//    - public List<OsinCheckInData> findAllTodayUnrewarded() // Lấy các record có checkinDate là hôm nay và rewarded = 0
// 2. Trong Player:
//    - public boolean isOnline()
//    - public boolean addItem(Item item)
// 3. Trong com.ngocrong.server.Manager (hoặc lớp quản lý người chơi/server tương đương):
//    - public static Manager getInstance()
//    - public Player getPlayer(long playerId)
//    - public void sendNotificationToAllPlayers(String message)

public class OsinCheckInEvent {

    private static OsinCheckInRepository repo() {
        return GameRepository.getInstance().osinCheckInRepository;
    }

    // Các mốc điểm danh, sắp xếp từ cao đến thấp để dễ xử lý
    private static final int[] MILESTONES = { 1000, 500, 400, 300, 200 };

    /**
     * Lấy tổng số lượt điểm danh trong ngày hôm nay.
     * Yêu cầu: OsinCheckInRepository.countToday() phải được triển khai.
     */
    public static int getTotalTodayCheckIns() {
        return repo().countToday();
    }

    /**
     * Xử lý khi người chơi thực hiện điểm danh.
     * Yêu cầu: OsinCheckInRepository.findTodayByPlayer(long) và
     * OsinCheckInRepository.save(OsinCheckInData) phải được triển khai.
     */
    public static void checkIn(Player player) {
        if (player == null) {
            return;
        }

        Optional<OsinCheckInData> existingCheckInOpt = repo().findTodayByPlayer(player.id);

        if (existingCheckInOpt.isPresent()) {
            OsinCheckInData existingCheckInData = existingCheckInOpt.get();
            if (existingCheckInData.getRewarded() != null && existingCheckInData.getRewarded() == 1) {
                player.service.sendThongBao("Bạn đã điểm danh và nhận quà hôm nay rồi.");
            } else {
                player.service.sendThongBao("Bạn đã điểm danh hôm nay. Chờ đạt mốc để nhận quà.");
            }
            if (existingCheckInData.getRewarded() == null || existingCheckInData.getRewarded() == 0) {
                int currentTotal = getTotalTodayCheckIns();
                int currentMilestone = getHighestAchievedMilestone(currentTotal);
                if (currentMilestone > 0) {
                    tryGiveRewardToPlayer(player, existingCheckInData, currentMilestone);
                }
            }
            return;
        }

        int totalBeforeThisCheckIn = getTotalTodayCheckIns();

        OsinCheckInData newData = new OsinCheckInData();
        newData.setPlayerId(player.id);
        newData.setCheckinDate(Instant.now());
        newData.setRewarded((byte) 0); // Mặc định là chưa nhận thưởng

        repo().save(newData); // Phương thức này phải có khả năng insert/update.

        player.service.sendThongBao("Điểm danh thành công!");

        int totalAfterThisCheckIn = getTotalTodayCheckIns(); // Lấy lại tổng sau khi đã save
        processGlobalMilestoneRewards(totalBeforeThisCheckIn, totalAfterThisCheckIn);

        int currentHighestMilestone = getHighestAchievedMilestone(totalAfterThisCheckIn);
        if (currentHighestMilestone > 0) {
            tryGiveRewardToPlayer(player, newData, currentHighestMilestone);
        }
    }

    /**
     * Xử lý các mốc phần thưởng toàn cục khi có người mới điểm danh.
     * Thông báo cho toàn server nếu đạt mốc mới.
     * Phát quà cho những người chơi đủ điều kiện (đã điểm danh, chưa nhận quà).
     */
    private static void processGlobalMilestoneRewards(int previousTotal, int currentTotal) {
        int currentHighestAchievedMilestone = getHighestAchievedMilestone(currentTotal);
        if (currentHighestAchievedMilestone == 0) {
            return; // Chưa đạt mốc nào cả
        }

        int previousHighestAchievedMilestone = getHighestAchievedMilestone(previousTotal);

        // 1. Thông báo nếu đạt một mốc MỚI (server vừa vượt qua mốc này)
        if (currentHighestAchievedMilestone > previousHighestAchievedMilestone) {
            Item rewardDetails = getRewardItemForMilestoneValue(currentHighestAchievedMilestone);
            if (rewardDetails != null) {
                String announcement = String.format("Chúc mừng! Mốc điểm danh %d người đã đạt! Phần thưởng: %s (x%d).",
                        currentHighestAchievedMilestone,
                        rewardDetails.template.name,
                        rewardDetails.quantity);
                SessionManager.addBigMessage(announcement);
            }
        }

        // 2. Sau khi có khả năng đạt mốc mới (hoặc mốc cũ nhưng có người mới điểm
        // danh),
        // quét tất cả những người đã điểm danh hôm nay mà chưa nhận quà.
        List<OsinCheckInData> candidates = repo().findAllTodayUnrewarded();
        for (OsinCheckInData checkInData : candidates) {
            tryGiveRewardToPlayer(null, checkInData, currentHighestAchievedMilestone);
        }
    }

    /**
     * Thử trao phần thưởng cho một người chơi dựa trên mốc hiện tại.
     * 
     * @param playerToReward   Người chơi cụ thể để thử trao quà (có thể null nếu
     *                         chỉ có checkInData).
     * @param checkInData      Dữ liệu điểm danh của người chơi.
     * @param currentMilestone Mốc cao nhất server đã đạt.
     */
    private static void tryGiveRewardToPlayer(Player playerToReward, OsinCheckInData checkInData,
            int currentMilestone) {
        if (checkInData.getRewarded() != null && checkInData.getRewarded() == 1) {
            return; // Đã nhận quà rồi
        }

        Item rewardItem = getRewardItemForMilestoneValue(currentMilestone);
        if (rewardItem == null) {
            return; // Không có quà cho mốc này (không nên xảy ra nếu currentMilestone > 0)
        }

        Player p = playerToReward;
        if (p == null) { // Nếu không truyền player cụ thể, lấy từ Manager
            p = SessionManager.findChar(checkInData.getPlayerId());
        }

        if (p != null ) {
            Item rewardToGive = rewardItem.clone(); // QUAN TRỌNG: Clone item!
            if (p.addItem(rewardToGive)) {
                p.service.sendThongBao(String.format("Bạn nhận được quà điểm danh mốc %d: %s x%d.",
                        currentMilestone,
                        rewardToGive.template.name, rewardToGive.quantity));
                checkInData.setRewarded((byte) 1); // Đánh dấu đã nhận thưởng
                repo().save(checkInData); // Phương thức này phải hỗ trợ update.
            } else {
                // Chỉ thông báo cho người chơi nếu đó là người vừa điểm danh hoặc
                // playerToReward được cung cấp
                if (playerToReward != null && p.id == playerToReward.id) {
                    p.service.sendThongBao(String.format("Hành trang không đủ chỗ trống để nhận quà điểm danh mốc %d.",
                            currentMilestone));
                }
            }
        }
    }

    /**
     * Xác định mốc cao nhất đã đạt được dựa trên tổng số lượt điểm danh.
     */
    private static int getHighestAchievedMilestone(int totalCheckIns) {
        for (int milestone : MILESTONES) {
            if (totalCheckIns >= milestone) {
                return milestone;
            }
        }
        return 0;
    }

    /**
     * Lấy thông tin vật phẩm thưởng cho một mốc nhất định.
     */
    private static Item getRewardItemForMilestoneValue(int milestoneValue) {
        Item reward = null;
        if (milestoneValue >= 1000) {
            reward = new Item(ItemName.THOI_VANG);
            reward.quantity = 5;
        } else if (milestoneValue >= 500) {
            reward = new Item(ItemName.PHIEU_X2_TNSM);
            reward.quantity = 1;
        } else if (milestoneValue >= 400) {
            int[] vt = { 342, 343, 344, 345 };
            reward = new Item(vt[Utils.nextInt(vt.length)]);
            reward.quantity = 2;
        } else if (milestoneValue >= 300) {
            int[] sp = { 441, 442, 443, 444, 445, 446, 447 };
            reward = new Item(sp[Utils.nextInt(sp.length)]);
            reward.quantity = 2;
        } else if (milestoneValue >= 200) {
            int[] nr = { ItemName.NGOC_RONG_5_SAO, ItemName.NGOC_RONG_6_SAO, ItemName.NGOC_RONG_7_SAO };
            reward = new Item(nr[Utils.nextInt(nr.length)]);
            reward.quantity = 1;
        }

        if (reward != null) {
            reward.setDefaultOptions();
        }
        return reward;
    }

    // Các phương thức cũ isCheckInDay(), isRewardDay(), today(),
    // receiveReward(Player)
    // đã được loại bỏ do thay đổi logic hoạt động.
}
