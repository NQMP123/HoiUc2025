package com.ngocrong.event;

import com.ngocrong.consts.ItemName;
import com.ngocrong.data.OsinCheckInData;
import com.ngocrong.item.Item;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.repository.OsinCheckInRepository;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class OsinCheckInEvent {

    private static OsinCheckInRepository repo() {
        return GameRepository.getInstance().osinCheckInRepository;
    }

    private static DayOfWeek today() {
        return LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfWeek();
    }

    public static boolean isCheckInDay() {
        DayOfWeek d = today();
        return d == DayOfWeek.MONDAY || d == DayOfWeek.WEDNESDAY || d == DayOfWeek.FRIDAY;
    }

    public static boolean isRewardDay() {
        DayOfWeek d = today();
        return d == DayOfWeek.TUESDAY || d == DayOfWeek.THURSDAY || d == DayOfWeek.SATURDAY;
    }

    public static void checkIn(Player player) {
        if (player == null) return;
        if (!isCheckInDay()) {
            player.service.sendThongBao("Hôm nay không thể điểm danh");
            return;
        }
        if (repo().countTodayByPlayer(player.id) > 0) {
            player.service.sendThongBao("Bạn đã điểm danh hôm nay");
            return;
        }
        OsinCheckInData data = new OsinCheckInData();
        data.setPlayerId(player.id);
        data.setCheckinDate(java.time.Instant.now());
        data.setRewarded((byte) 0);
        repo().save(data);
        player.service.sendThongBao("Điểm danh thành công");
    }

    public static void receiveReward(Player player) {
        if (player == null) return;
        if (!isRewardDay()) {
            player.service.sendThongBao("Hôm nay không thể nhận quà");
            return;
        }
        Optional<OsinCheckInData> opt = repo().findYesterday(player.id);
        if (!opt.isPresent()) {
            player.service.sendThongBao("Bạn không điểm danh hôm qua");
            return;
        }
        OsinCheckInData data = opt.get();
        if (data.getRewarded() != null && data.getRewarded() == 1) {
            player.service.sendThongBao("Bạn đã nhận quà rồi");
            return;
        }
        int total = repo().countYesterday();
        Item reward = null;
        if (total >= 1000) {
            reward = new Item(ItemName.THOI_VANG);
            reward.quantity = 5;
        } else if (total >= 500) {
            reward = new Item(ItemName.PHIEU_X2_TNSM);
            reward.quantity = 1;
        } else if (total >= 400) {
            int[] vt = {342, 343, 344, 345};
            reward = new Item(vt[Utils.nextInt(vt.length)]);
            reward.quantity = 2;
        } else if (total >= 300) {
            int[] sp = {441, 442, 443, 444, 445, 446, 447};
            reward = new Item(sp[Utils.nextInt(sp.length)]);
            reward.quantity = 2;
        } else if (total >= 200) {
            int[] nr = {ItemName.NGOC_RONG_5_SAO, ItemName.NGOC_RONG_6_SAO, ItemName.NGOC_RONG_7_SAO};
            reward = new Item(nr[Utils.nextInt(nr.length)]);
            reward.quantity = 1;
        } else {
            player.service.sendThongBao("Chưa đủ mốc người điểm danh");
            return;
        }
        if (reward != null) {
            reward.setDefaultOptions();
            if (player.addItem(reward)) {
                player.service.sendThongBao("Bạn nhận được " + reward.template.name);
                data.setRewarded((byte) 1);
                repo().save(data);
            } else {
                player.service.sendThongBao("Hành trang không đủ chỗ trống");
            }
        }
    }
}
