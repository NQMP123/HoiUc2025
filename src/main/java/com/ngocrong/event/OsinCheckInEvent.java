package com.ngocrong.event;

import com.ngocrong.consts.ItemName;
import com.ngocrong.consts.ItemTimeName;
import com.ngocrong.data.OsinCheckInData;
import com.ngocrong.item.Item;
import com.ngocrong.item.ItemTime;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.repository.OsinCheckInRepository;
import com.ngocrong.server.SessionManager;
import com.ngocrong.data.PlayerData;
import com.ngocrong.repository.PlayerDataRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class OsinCheckInEvent {

    private static OsinCheckInRepository repo() {
        return GameRepository.getInstance().osinCheckInRepository;
    }
    
    private static final int[] MILESTONES = {2000, 1000, 500, 400, 300, 200};

    /**
     * Lấy tổng số lượt điểm danh hôm nay
     */
    public static int getTotalTodayCheckIns() {
        return repo().countToday();
    }

    /**
     * Xử lý điểm danh của người chơi
     */
    public static void checkIn(Player player) {
        if (player == null) {
            return;
        }
        
        // Kiểm tra kích hoạt thành viên
        if (player.getSession().user.getActivated() == 0) {
            player.service.sendThongBao("Bạn cần kích hoạt thành viên để tham gia tính năng này");
            return;
        }
        
        // Kiểm tra đã điểm danh hôm nay chưa
        Optional<OsinCheckInData> existingCheckInOpt = repo().findTodayByPlayer(player.id);
        if (existingCheckInOpt.isPresent()) {
            OsinCheckInData existingCheckInData = existingCheckInOpt.get();
            if (existingCheckInData.getRewarded() != null && existingCheckInData.getRewarded() == 1) {
                player.service.sendThongBao("Bạn đã điểm danh và nhận quà hôm nay rồi.");
            } else {
                player.service.sendThongBao("Bạn đã điểm danh hôm nay. Chờ đạt mốc để nhận quà.");
            }
            return;
        }

        // Kiểm tra sự kiện còn slot không
        int totalBeforeThisCheckIn = getTotalTodayCheckIns();
        if (totalBeforeThisCheckIn >= MILESTONES[0]) {
            player.service.sendThongBao("Hoạt động đã kết thúc");
            return;
        }
        
        // Lưu dữ liệu điểm danh
        OsinCheckInData newData = new OsinCheckInData();
        newData.setPlayerId(player.id);
        newData.setCheckinDate(Instant.now());
        newData.setRewarded((byte) 0);
        newData.setIs_rewarded(0);
        repo().save(newData);

        player.service.sendThongBao("Điểm danh thành công!");

        // Xử lý mốc thưởng
        int totalAfterThisCheckIn = getTotalTodayCheckIns();
        processGlobalMilestoneRewards(totalBeforeThisCheckIn, totalAfterThisCheckIn);

        // Trao thưởng cho người vừa điểm danh nếu đủ điều kiện
        int currentHighestMilestone = getHighestAchievedMilestone(totalAfterThisCheckIn);
        if (currentHighestMilestone > 0) {
            tryGiveRewardToPlayer(player, newData, currentHighestMilestone);
        }
    }

    /**
     * Xử lý mốc thưởng toàn server
     */
    private static void processGlobalMilestoneRewards(int previousTotal, int currentTotal) {
        int currentHighestAchievedMilestone = getHighestAchievedMilestone(currentTotal);
        if (currentHighestAchievedMilestone == 0) {
            return; // Chưa đạt mốc nào cả
        }

        int previousHighestAchievedMilestone = getHighestAchievedMilestone(previousTotal);

        // Thông báo nếu đạt mốc mới
        if (currentHighestAchievedMilestone > previousHighestAchievedMilestone) {
            String announcement = String.format("Chúc mừng! Mốc điểm danh %d người đã đạt! Hãy tham gia điểm danh ngay", currentHighestAchievedMilestone);
            SessionManager.addThongBaoAll(announcement);
        }

        // Trao thưởng cho tất cả người đã điểm danh mà chưa nhận quà ở mốc này
        List<OsinCheckInData> candidates = repo().findAllTodayUnrewarded(currentHighestAchievedMilestone);
        for (OsinCheckInData checkInData : candidates) {
            tryGiveRewardToPlayer(null, checkInData, currentHighestAchievedMilestone);
        }
    }

    /**
     * Trao thưởng cho người chơi - ĐÃ FIX BUG
     */
    private static void tryGiveRewardToPlayer(Player playerToReward, OsinCheckInData checkInData, int currentMilestone) {
        // Kiểm tra đã nhận thưởng mốc này chưa
        if (checkInData.getRewarded() != null && checkInData.getIs_rewarded() >= currentMilestone) {
            return; // Đã nhận quà rồi
        }

        Player p = playerToReward;
        if (p == null) {
            p = SessionManager.findChar(checkInData.getPlayerId());
        }

        if (p == null) {
            return; // Không tìm thấy người chơi online
        }

        boolean hasGivenAnyReward = false;
        StringBuilder rewardMessage = new StringBuilder();

        // 1. Trao x2 TNSM cho mốc >= 500
        boolean isX2 = currentMilestone >= 500;
        if (isX2) {
            Item base = new Item(ItemName.PHIEU_X2_TNSM);
            p.setItemTime(ItemTimeName.PHIEU_X2_TNSM, base.template.iconID, true, Utils.getSecondsUntilEndOfDay());
            rewardMessage.append(String.format("x2 TNSM trong ngày từ mốc %d người", currentMilestone));
            hasGivenAnyReward = true;
        }

        // 2. Trao thưởng đặc biệt cho các mốc cụ thể - ĐÃ FIX BUG
        Item specialReward = getSpecialRewardForMilestone(currentMilestone);
        if (specialReward != null) {
            if (p.addItem(specialReward)) {
                if (rewardMessage.length() > 0) {
                    rewardMessage.append(" + ");
                }
                rewardMessage.append(String.format("%s x%d", specialReward.template.name, specialReward.quantity));
                hasGivenAnyReward = true;
            } else if (playerToReward != null && p.id == playerToReward.id) {
                p.service.sendThongBao("Hành trang không đủ chỗ trống để nhận một phần quà điểm danh.");
            }
        }

        // 3. Trao thưởng thông thường theo mốc
        Item regularReward = getRegularRewardForMilestone(currentMilestone);
        if (regularReward != null) {
            Item rewardToGive = regularReward.clone();
            if (p.addItem(rewardToGive)) {
                if (rewardMessage.length() > 0) {
                    rewardMessage.append(" + ");
                }
                rewardMessage.append(String.format("%s x%d", rewardToGive.template.name, rewardToGive.quantity));
                hasGivenAnyReward = true;
            } else if (playerToReward != null && p.id == playerToReward.id) {
                p.service.sendThongBao("Hành trang không đủ chỗ trống để nhận một phần quà điểm danh.");
            }
        }

        // 4. Cập nhật trạng thái đã nhận thưởng - CHỈ MỘT LẦN
        if (hasGivenAnyReward) {
            checkInData.setRewarded((byte) 1);
            checkInData.setIs_rewarded(currentMilestone);
            repo().save(checkInData);
            
            // Thông báo tổng hợp
            if (rewardMessage.length() > 0) {
                p.service.sendThongBao(String.format("Bạn nhận được quà điểm danh mốc %d: %s", 
                    currentMilestone, rewardMessage.toString()));
            }
        }
    }

    /**
     * Lấy thưởng đặc biệt cho mốc cụ thể (thỏi vàng)
     */
    private static Item getSpecialRewardForMilestone(int milestone) {
        Item reward = null;
        
        switch (milestone) {
            case 500:
                reward = new Item(ItemName.THOI_VANG);
                reward.quantity = 100000; // 100k thỏi vàng
                break;
            case 1000:
                reward = new Item(ItemName.THOI_VANG);
                reward.quantity = 100000; // 100k thỏi vàng
                break;
            case 2000:
                reward = new Item(ItemName.THOI_VANG);
                reward.quantity = 200000; // 200k thỏi vàng
                break;
        }
        
        if (reward != null) {
            reward.setDefaultOptions();
        }
        return reward;
    }

    /**
     * Lấy thưởng thông thường theo mốc
     */
    private static Item getRegularRewardForMilestone(int milestone) {
        Item reward = null;
        
        if (milestone >= 400) {
            // Mốc 400+: x3 Vệ tinh ngẫu nhiên
            int[] satellites = {342, 343, 344, 345};
            reward = new Item(satellites[Utils.nextInt(satellites.length)]);
            reward.quantity = 3;
        } else if (milestone >= 200) {
            // Mốc 200, 300: 500 triệu vàng
            reward = new Item(ItemName.VANG_190);
            reward.quantity = 500_000_000;
        }
        
        if (reward != null) {
            reward.setDefaultOptions();
        }
        return reward;
    }

    /**
     * Xác định mốc cao nhất đã đạt được dựa trên tổng số lượt điểm danh
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
     * Lấy thông tin vật phẩm thưởng cho một mốc nhất định (DEPRECATED - được tách thành 2 hàm riêng)
     * Giữ lại để tương thích ngược
     */
    @Deprecated
    private static Item getRewardItemForMilestoneValue(int milestoneValue) {
        // Chỉ trả về regular reward, special reward được xử lý riêng
        return getRegularRewardForMilestone(milestoneValue);
    }

    /**
     * Kiểm tra và xử lý x2 TNSM pending
     */
    public static void checkPendingReward(Player player) {
        if (player == null || player.itemTimes == null || player.itemTimes.isEmpty()) {
            return;
        }
        Utils.setTimeout(() -> {
            var itemTime = player.getItemTime(ItemTimeName.PHIEU_X2_TNSM);
            if (itemTime != null) {
                if (getTotalTodayCheckIns() >= 500) {
                    player.setTimeForItemtime(ItemTimeName.PHIEU_X2_TNSM, Utils.getSecondsUntilEndOfDay());
                } else {
                    player.setTimeForItemtime(ItemTimeName.PHIEU_X2_TNSM, 1);
                }
            }
        }, 3000);
    }

    /**
     * Lấy thông tin trạng thái sự kiện (cho debug/admin)
     */
    public static String getEventStatus() {
        int total = getTotalTodayCheckIns();
        int currentMilestone = getHighestAchievedMilestone(total);
        
        StringBuilder status = new StringBuilder();
        status.append("=== TRẠNG THÁI SỰ KIỆN ĐIỂM DANH ===\n");
        status.append(String.format("Tổng số người đã điểm danh: %d\n", total));
        status.append(String.format("Mốc hiện tại: %d\n", currentMilestone));
        status.append("Các mốc: ");
        for (int milestone : MILESTONES) {
            if (total >= milestone) {
                status.append(String.format("[✓%d] ", milestone));
            } else {
                status.append(String.format("[✗%d] ", milestone));
            }
        }
        status.append("\n");
        
        return status.toString();
    }
}