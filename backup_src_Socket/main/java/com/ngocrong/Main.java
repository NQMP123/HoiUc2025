package com.ngocrong;

import com.ngocrong.repository.*;
import com.ngocrong.server.DragonBall;
import com.ngocrong.server.DropRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        GameRepository.getInstance().user = userDataRepository;
        GameRepository.getInstance().player = playerDataRepository;
        GameRepository.getInstance().giftCode = giftCodeDataRepository;
        GameRepository.getInstance().giftCodeHistory = giftCodeHistoryDataRepository;
        GameRepository.getInstance().disciple = discipleDataRepository;
        GameRepository.getInstance().clan = clanDataRepository;
        GameRepository.getInstance().clanMember = clanMemberDataRepository;
        GameRepository.getInstance().consignmentItem = consignmentItemDataRepository;
        GameRepository.getInstance().ccuDataRepository = ccuDataRepository;
        GameRepository.getInstance().eventOpenRepository = eventOpenRepository;
        GameRepository.getInstance().dhvtSieuHangRepository = dhvtSieuHangRepository;
        GameRepository.getInstance().dhvtSieuHangRewardRepository = dhvtSieuHangRewardRepository;
        GameRepository.getInstance().gameEventRepository = gameEventRepository;
        GameRepository.getInstance().eventVQTD = eventVQTD;
        GameRepository.getInstance().eventTet = eventTet;
        GameRepository.getInstance().historyGoldBar = historyGoldBar;
        GameRepository.getInstance().historyTradeRepository = historyTradeRepository;
        GameRepository.getInstance().whisDataRepository = whisDataRepository;
        GameRepository.getInstance().dropRateRepository = dropRateRepository;
        GameRepository.getInstance().osinCheckInRepository = osinReward;
        DropRateService.load();

        DragonBall.getInstance().start();
    }

    @Autowired
    OsinCheckInRepository osinReward;

    @Autowired
    WhisDataRepository whisDataRepository;

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    PlayerDataRepository playerDataRepository;

    @Autowired
    GiftCodeDataRepository giftCodeDataRepository;

    @Autowired
    GiftCodeHistoryDataRepository giftCodeHistoryDataRepository;

    @Autowired
    DiscipleDataRepository discipleDataRepository;

    @Autowired
    ClanDataRepository clanDataRepository;

    @Autowired
    ClanMemberDataRepository clanMemberDataRepository;

    @Autowired
    ConsignmentItemDataRepository consignmentItemDataRepository;

    @Autowired
    CCUDataRepository ccuDataRepository;
    @Autowired
    EventOpenRepository eventOpenRepository;
    @Autowired
    DHVTSieuHangRepository dhvtSieuHangRepository;
    @Autowired
    DhvtSieuHangRewardRepository dhvtSieuHangRewardRepository;

    @Autowired
    GameEventRepository gameEventRepository;

    @Autowired
    EventVQTD eventVQTD;

    @Autowired
    SuKienTetRepository eventTet;

    @Autowired
    HistoryGoldBarRepository historyGoldBar;
    @Autowired
    HistoryTradeRepository historyTradeRepository;

    @Autowired
    DropRateRepository dropRateRepository;
}
