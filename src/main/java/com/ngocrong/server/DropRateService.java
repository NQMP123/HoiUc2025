package com.ngocrong.server;

import com.ngocrong.data.DropRateData;
import com.ngocrong.repository.DropRateRepository;
import com.ngocrong.repository.GameRepository;
import com.ngocrong.util.Utils;

import java.util.Optional;

public class DropRateService {
    private static final int CONFIG_ID = 1;
    private static int mobRate = 100;
    private static int bossRate = 100;

    private static DropRateRepository repo() {
        return GameRepository.getInstance().dropRateRepository;
    }

    public static void load() {
        try {
            Optional<DropRateData> data = repo().findById(CONFIG_ID);
            if (data.isPresent()) {
                mobRate = Optional.ofNullable(data.get().getMobRate()).orElse(100);
                bossRate = Optional.ofNullable(data.get().getBossRate()).orElse(100);
            }
        } catch (Exception ignored) {
        }
    }

    public static void update(int mob, int boss) {
        DropRateData cfg = repo().findById(CONFIG_ID).orElse(new DropRateData(CONFIG_ID, mob, boss));
        cfg.setMobRate(mob);
        cfg.setBossRate(boss);
        repo().save(cfg);
        mobRate = mob;
        bossRate = boss;
    }

    public static int getMobRate() {
        return mobRate;
    }

    public static int getBossRate() {
        return bossRate;
    }

    public static boolean shouldDropMobItem() {
        return Utils.isTrue(mobRate, 100);
    }

    public static boolean shouldDropBossItem() {
        return Utils.isTrue(bossRate, 100);
    }
}
