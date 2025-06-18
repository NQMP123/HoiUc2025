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

    public static int getTotalTodayCheckIns() {
        return repo().countToday();
    }

    public static void checkIn(Player player) {
        if (player == null) {
            return;
        }
        if (player.getSession().user.getActivated() == 0) {
            player.service.sendThongBao("Bạn cần kích hoạt thành viên để tham gia tính năng này");
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
            return;
        }

        int totalBeforeThisCheckIn = getTotalTodayCheckIns();
        if (totalBeforeThisCheckIn >= MILESTONES[0]) {
            player.service.sendThongBao("Hoạt động đã kết thúc");
            return;
        }
        OsinCheckInData newData = new OsinCheckInData();
        newData.setPlayerId(player.id);
        newData.setCheckinDate(Instant.now());
        newData.setRewarded((byte) 0);
        newData.setIs_rewarded(0); // Mặc định là chưa nhận thưởng

        repo().save(newData); // Phương thức này phải có khả năng insert/update.

        player.service.sendThongBao("Điểm danh thành công!");

        int totalAfterThisCheckIn = getTotalTodayCheckIns(); // Lấy lại tổng sau khi đã save
        processGlobalMilestoneRewards(totalBeforeThisCheckIn, totalAfterThisCheckIn);

        int currentHighestMilestone = getHighestAchievedMilestone(totalAfterThisCheckIn);
        if (currentHighestMilestone > 0) {
            tryGiveRewardToPlayer(player, newData, currentHighestMilestone);
        }
    }

    private static void processGlobalMilestoneRewards(int previousTotal, int currentTotal) {
        int currentHighestAchievedMilestone = getHighestAchievedMilestone(currentTotal);
        if (currentHighestAchievedMilestone == 0) {
            return; // Chưa đạt mốc nào cả
        }

        int previousHighestAchievedMilestone = getHighestAchievedMilestone(previousTotal);

        // 1. Thông báo nếu đạt một mốc MỚI (server vừa vượt qua mốc này)
        if (currentHighestAchievedMilestone > previousHighestAchievedMilestone) {

            String announcement = String.format("Chúc mừng! Mốc điểm danh %d người đã đạt! Hãy tham gia điểm danh ngay", currentHighestAchievedMilestone);
            SessionManager.addThongBaoAll(announcement);

        }

        // 2. Sau khi có khả năng đạt mốc mới (hoặc mốc cũ nhưng có người mới điểm
        // danh),
        // quét tất cả những người đã điểm danh hôm nay mà chưa nhận quà.
        List<OsinCheckInData> candidates = repo().findAllTodayUnrewarded(currentHighestAchievedMilestone);
        for (OsinCheckInData checkInData : candidates) {
            tryGiveRewardToPlayer(null, checkInData, currentHighestAchievedMilestone);
        }
    }

    private static void tryGiveRewardToPlayer(Player playerToReward, OsinCheckInData checkInData, int currentMilestone) {
        if (checkInData.getRewarded() != null && checkInData.getIs_rewarded() >= currentMilestone) {
            return; // Đã nhận quà rồi
        }

        Item rewardItem = getRewardItemForMilestoneValue(currentMilestone);

        Player p = playerToReward;
        if (p == null) {
            p = SessionManager.findChar(checkInData.getPlayerId());
        }

        boolean isX2 = currentMilestone >= 5;

        if (p != null) {
            if (isX2) {
                Item base = new Item(ItemName.PHIEU_X2_TNSM);
                p.setItemTime(ItemTimeName.PHIEU_X2_TNSM, base.template.iconID, true, Utils.getSecondsUntilEndOfDay());
                p.service.sendThongBao(String.format("Bạn nhận được x2 TNSM trong ngày từ mốc %d người điểm danh.", currentMilestone));
                checkInData.setRewarded((byte) 1);
                checkInData.setIs_rewarded(currentMilestone);
                repo().save(checkInData);
            }
            if (currentMilestone == 5) {
                Item reward = new Item(Utils.nextInt(1021, 1023));
                reward.quantity = 1;
                p.addItem(reward);
            }
            if (rewardItem == null) {
                return;
            }

            Item rewardToGive = rewardItem.clone();
            if (p.addItem(rewardToGive)) {
                p.service.sendThongBao(String.format("Bạn nhận được quà điểm danh mốc %d: %s x%d.",
                        currentMilestone,
                        rewardToGive.template.name, rewardToGive.quantity));
                checkInData.setRewarded((byte) 1);
                checkInData.setIs_rewarded(currentMilestone);
                repo().save(checkInData);
            } else if (playerToReward != null && p.id == playerToReward.id) {
                p.service.sendThongBao(String.format("Hành trang không đủ chỗ trống để nhận quà điểm danh mốc %d.",
                        currentMilestone));
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
        if (milestoneValue >= 6) {
            reward = new Item(ItemName.THOI_VANG);
            reward.quantity = 5;
        }
        if (milestoneValue >= 5) {

        } else if (milestoneValue >= 4) {
            reward = new Item(ItemName.VANG_190);
            reward.quantity = 1_000_000_000;
        } else if (milestoneValue >= 3) {
            int[] vt = {342, 343, 344, 345};
            reward = new Item(vt[Utils.nextInt(vt.length)]);
            reward.quantity = 3;
        } else if (milestoneValue >= 2) {
            reward = new Item(ItemName.VANG_190);
            reward.quantity = 500_000_000;
        } else if (milestoneValue >= 1) {
            reward = new Item(ItemName.VANG_190);
            reward.quantity = 500_000_000;
        }

        if (reward != null) {
            reward.setDefaultOptions();
        }
        return reward;
    }

    public static void checkPendingReward(Player player) {
        if (player == null || player.itemTimes == null || player.itemTimes.isEmpty()) {
            return;
        }
        Utils.setTimeout(() -> {
            var itemTime = player.getItemTime(ItemTimeName.PHIEU_X2_TNSM);
            if (itemTime != null) {
                if (getTotalTodayCheckIns() >= 5) {
                    player.setTimeForItemtime(ItemTimeName.PHIEU_X2_TNSM, Utils.getSecondsUntilEndOfDay());
                } else {
                    player.setTimeForItemtime(ItemTimeName.PHIEU_X2_TNSM, 1);
                }
            }
        }, 5000);
    }

    // Các phương thức cũ isCheckInDay(), isRewardDay(), today(),
    // receiveReward(Player)
    // đã được loại bỏ do thay đổi logic hoạt động.
}
