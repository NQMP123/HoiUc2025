﻿using UnityEngine;

namespace Assets.Scripts.Assembly_CSharp.HSNR
{
    public class AutoPean : IActionListener, IChatable
    {
        public static AutoPean _Instance;

        private static bool isAutoRequestPean;

        public static bool isAutoBuffPean;

        private static long lastTimeRequestedPean;

        private static bool isAutoDonatePean;

        private static bool isAutoHarvestPean;

        private static int minimumHPPercent;

        private static int minimumMPPercent;

        private static int minimumHP;

        private static int minimumMP;

        private static bool isSaveData;

        private static string[] inputHPPercent;

        private static string[] inputMPPercent;

        private static string[] inputHP;

        private static string[] inputMP;

        public static AutoPean getInstance()
        {
            if (_Instance == null)
                _Instance = new AutoPean();
            return _Instance;
        }

        public static void Update()
        {
            updatePetPean();
            if (isAutoRequestPean)
            { RequestPean(); }
            if (isAutoDonatePean && GameCanvas.gameTick % 20 == 0)
            { DonatePean(); }
            if (isAutoHarvestPean)
            { HarvestPean(); }
            if (!Char.myCharz().meDead)
            { AutoUsePean(); }
        }
        public static void updatePetPean()
        {

            if (isAutoBuffPean && Char.myCharz().havePet)
            {
                if (GameCanvas.gameTick % 50 == 0)
                {
                    Service.gI().petInfo();
                }
                var p = Char.myPetz();
                if (GameCanvas.gameTick % 20 == 0 && !p.meDead && (p.cHP < p.cHPFull >> 2 || p.cMP < p.cMPFull >> 2 || p.cStamina < 150))
                {
                    GameScr.gI().doUseHP();
                }
            }
        }
        public void onChatFromMe(string text, string to)
        {
            if (ChatTextField.gI().tfChat.getText() != null && !ChatTextField.gI().tfChat.getText().Equals(string.Empty) && !text.Equals(string.Empty) && text != null)
            {
                if (ChatTextField.gI().strChat.Equals(inputHPPercent[0]))
                {
                    try
                    {
                        int num = int.Parse(ChatTextField.gI().tfChat.getText());
                        if (num >= 100)
                            num = 99;
                        if (num <= 0)
                            num = 1;
                        minimumHPPercent = num;
                        GameScr.info1.addInfo("Auto pean khi HP dưới: " + num + "%", 0);
                        if (isSaveData)
                            Rms.saveRMSInt("AutoPeanPercentHP", minimumHPPercent);
                    }
                    catch
                    {
                        GameScr.info1.addInfo("%HP không hợp lệ, vui lòng nhập lại", 0);
                    }
                    GameUtils.gI().resetTF();
                }
                else if (ChatTextField.gI().strChat.Equals(inputMPPercent[0]))
                {
                    try
                    {
                        int num2 = int.Parse(ChatTextField.gI().tfChat.getText());
                        if (num2 >= 100)
                            num2 = 99;
                        if (num2 <= 0)
                            num2 = 1;
                        minimumMPPercent = num2;
                        GameScr.info1.addInfo("Auto pean khi MP dưới: " + num2 + "%", 0);
                        if (isSaveData)
                            Rms.saveRMSInt("AutoPeanPercentMP", minimumMPPercent);
                    }
                    catch
                    {
                        GameScr.info1.addInfo("%MP không hợp lệ, vui lòng nhập lại", 0);
                    }
                    GameUtils.gI().resetTF();
                }
                else if (ChatTextField.gI().strChat.Equals(inputHP[0]))
                {
                    try
                    {
                        int num3 = (minimumHP = int.Parse(ChatTextField.gI().tfChat.getText()));
                        if (isSaveData)
                            Rms.saveRMSString("AutoPeanHP", minimumHP.ToString());
                        GameScr.info1.addInfo("Auto pean khi HP dưới: " + NinjaUtil.getMoneys(num3) + "HP", 0);
                    }
                    catch
                    {
                        GameScr.info1.addInfo("HP không hợp lệ, vui lòng nhập lại", 0);
                    }
                    GameUtils.gI().resetTF();
                }
                else
                {
                    if (!ChatTextField.gI().strChat.Equals(inputMP[0]))
                        return;
                    try
                    {
                        int num4 = (minimumMP = int.Parse(ChatTextField.gI().tfChat.getText()));
                        if (isSaveData)
                            Rms.saveRMSString("AutoPeanMP", minimumMP.ToString());
                        GameScr.info1.addInfo("Auto pean khi MP dưới: " + NinjaUtil.getMoneys(num4) + "MP", 0);
                    }
                    catch
                    {
                        GameScr.info1.addInfo("MP không hợp lệ, vui lòng nhập lại", 0);
                    }
                    GameUtils.gI().resetTF();
                }
            }
            else
            {
                ChatTextField.gI().isShow = false;
                GameUtils.gI().resetTF();
            }
        }

        public void onCancelChat() => GameUtils.gI().resetTF();

        public void perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 1:
                    isAutoRequestPean = !isAutoRequestPean;
                    GameScr.info1.addInfo("Xin Đậu\n" + (isAutoRequestPean ? "[ON]" : "[OFF]"), 0);
                    if (isSaveData)
                        Rms.saveRMSInt("AutoPeanIsAutoRequestPean", isAutoRequestPean ? 1 : 0);
                    break;
                case 2:
                    isAutoDonatePean = !isAutoDonatePean;
                    GameScr.info1.addInfo("Cho Đậu\n" + (isAutoDonatePean ? "[ON]" : "[OFF]"), 0);
                    if (isSaveData)
                        Rms.saveRMSInt("AutoPeanIsAutoSendPean", isAutoDonatePean ? 1 : 0);
                    break;
                case 3:
                    isAutoHarvestPean = !isAutoHarvestPean;
                    GameScr.info1.addInfo("Thu Đậu\n" + (isAutoHarvestPean ? "[ON]" : "[OFF]"), 0);
                    if (isSaveData)
                        Rms.saveRMSInt("AutoPeanIsAutoHarvestPean", isAutoHarvestPean ? 1 : 0);
                    break;
                case 4:
                    if (minimumHP != 0)
                    {
                        minimumHP = 0;
                        GameScr.info1.addInfo("Auto đậu: 0HP", 0);
                        if (isSaveData)
                            Rms.saveRMSString("AutoPeanHP", minimumHP.ToString());
                    }
                    else if (minimumHP == 0)
                    {
                        ChatTextField.gI().strChat = inputHP[0];
                        ChatTextField.gI().tfChat.name = inputHP[1];
                        ChatTextField.gI().startChat2(getInstance(), string.Empty);
                    }
                    break;
                case 5:
                    if (minimumHPPercent != 0)
                    {
                        minimumHPPercent = 0;
                        GameScr.info1.addInfo("Auto đậu: 0% HP", 0);
                        if (isSaveData)
                            Rms.saveRMSInt("AutoPeanPercentHP", minimumHPPercent);
                    }
                    else if (minimumHPPercent == 0)
                    {
                        ChatTextField.gI().strChat = inputHPPercent[0];
                        ChatTextField.gI().tfChat.name = inputHPPercent[1];
                        ChatTextField.gI().startChat2(getInstance(), string.Empty);
                    }
                    break;
                case 6:
                    if (minimumMP != 0)
                    {
                        minimumMP = 0;
                        GameScr.info1.addInfo("Auto đậu: 0MP", 0);
                        if (isSaveData)
                            Rms.saveRMSString("AutoPeanMP", minimumMP.ToString());
                    }
                    else if (minimumMP == 0)
                    {
                        ChatTextField.gI().strChat = inputMP[0];
                        ChatTextField.gI().tfChat.name = inputMP[1];
                        ChatTextField.gI().startChat2(getInstance(), string.Empty);
                    }
                    break;
                case 7:
                    if (minimumMPPercent != 0)
                    {
                        minimumMPPercent = 0;
                        GameScr.info1.addInfo("Auto đậu: 0% MP", 0);
                        if (isSaveData)
                            Rms.saveRMSInt("AutoPeanPercentMP", minimumMPPercent);
                    }
                    else if (minimumMPPercent == 0)
                    {
                        ChatTextField.gI().strChat = inputMPPercent[0];
                        ChatTextField.gI().tfChat.name = inputMPPercent[1];
                        ChatTextField.gI().startChat2(getInstance(), string.Empty);
                    }
                    break;
                case 8:
                    isSaveData = !isSaveData;
                    GameScr.info1.addInfo("Lưu Cài Đặt\n" + (isSaveData ? "[ON]" : "[OFF]"), 0);
                    Rms.saveRMSInt("AutoPeanIsSaveRms", isSaveData ? 1 : 0);
                    if (isSaveData)
                        SaveData();
                    break;
                case 9:
                    isAutoBuffPean = !isAutoBuffPean;
                    GameScr.info1.addInfo("Cho Đậu Đệ\n" + (isAutoBuffPean ? "[ON]" : "[OFF]"), 0);
                    if (isSaveData)
                        Rms.saveRMSInt("isAutoBuffPean", isAutoBuffPean ? 1 : 0);
                    break;
            }
        }

        public static void ShowMenu()
        {
            LoadData();
            MyVector myVector = new MyVector();
            myVector.addElement(new Command("Auto Dùng\nĐậu\nCho Đệ\n" + (isAutoBuffPean ? "[ON]" : "[OFF]"), getInstance(), 9, null));
            myVector.addElement(new Command("Auto Xin Đậu\n" + (isAutoRequestPean ? "[ON]" : "[OFF]"), getInstance(), 1, null));
            myVector.addElement(new Command("Auto Cho Đậu\n" + (isAutoDonatePean ? "[ON]" : "[OFF]"), getInstance(), 2, null));
            myVector.addElement(new Command("Auto Thu\nHoạch Đậu\n" + (isAutoHarvestPean ? "[ON]" : "[OFF]"), getInstance(), 3, null));
            myVector.addElement(new Command("Dùng Đậu\nKhi Dưới: " + NinjaUtil.getMoneys(minimumHP) + "HP", getInstance(), 4, null));
            myVector.addElement(new Command("Dùng Đậu\nKhi HP Dưới: " + minimumHPPercent + "%", getInstance(), 5, null));
            myVector.addElement(new Command("Dùng Đậu\n Khi Dưới: " + NinjaUtil.getMoneys(minimumMP) + "MP", getInstance(), 6, null));
            myVector.addElement(new Command("Dùng Đậu\n Khi MP Dưới: " + minimumMPPercent + "%", getInstance(), 7, null));
            myVector.addElement(new Command("Lưu Cài Đặt\n" + (isSaveData ? "[ON]" : "[OFF]"), getInstance(), 8, null));
            GameCanvas.menu.startAt(myVector, 3);
        }

        private static void LoadData()
        {
            isSaveData = Rms.loadRMSInt("AutoPeanIsSaveRms") == 1;
            if (isSaveData)
            {
                isAutoBuffPean = Rms.loadRMSInt("isAutoBuffPean") == 1;
                isAutoRequestPean = Rms.loadRMSInt("AutoPeanIsAutoRequestPean") == 1;
                isAutoDonatePean = Rms.loadRMSInt("AutoPeanIsAutoSendPean") == 1;
                isAutoHarvestPean = Rms.loadRMSInt("AutoPeanIsAutoHarvestPean") == 1;
                if (Rms.loadRMSInt("AutoPeanPercentHP") == -1)
                    minimumHPPercent = 0;
                else
                    minimumHPPercent = Rms.loadRMSInt("AutoPeanPercentHP");
                if (Rms.loadRMSInt("AutoPeanPercentMP") == -1)
                    minimumMPPercent = 0;
                else
                    minimumMPPercent = Rms.loadRMSInt("AutoPeanPercentMP");
                if (Rms.loadRMSString("AutoPeanHP") == null)
                    minimumHP = 0;
                else
                    minimumHP = int.Parse(Rms.loadRMSString("AutoPeanHP"));
                if (Rms.loadRMSString("AutoPeanMP") == null)
                    minimumMP = 0;
                else
                    minimumMP = int.Parse(Rms.loadRMSString("AutoPeanMP"));
            }
        }

        private static void SaveData()
        {
            Rms.saveRMSInt("AutoPeanIsAutoRequestPean", isAutoRequestPean ? 1 : 0);
            Rms.saveRMSInt("AutoPeanIsAutoSendPean", isAutoDonatePean ? 1 : 0);
            Rms.saveRMSInt("AutoPeanIsAutoHarvestPean", isAutoHarvestPean ? 1 : 0);
            Rms.saveRMSString("AutoPeanHP", minimumHP.ToString());
            Rms.saveRMSInt("AutoPeanPercentHP", minimumHPPercent);
            Rms.saveRMSString("AutoPeanMP", minimumMP.ToString());
            Rms.saveRMSInt("AutoPeanPercentMP", minimumMPPercent);
        }

        private static void ResetChatTextField()
        {
            ChatTextField.gI().strChat = "Chat";
            ChatTextField.gI().tfChat.name = "chat";
            ChatTextField.gI().isShow = false;
        }

        private static void RequestPean()
        {
            if (mSystem.currentTimeMillis() - lastTimeRequestedPean >= 301000L)
            {
                lastTimeRequestedPean = mSystem.currentTimeMillis();
                Service.gI().clanMessage(1, "", -1);
            }
        }

        private static void DonatePean()
        {
            int num = 0;
            ClanMessage clanMessage;
            while (true)
            {
                if (num < ClanMessage.vMessage.size())
                {
                    clanMessage = (ClanMessage)ClanMessage.vMessage.elementAt(num);
                    if (clanMessage.maxCap != 0 && clanMessage.playerName != Char.myCharz().cName && clanMessage.recieve != clanMessage.maxCap)
                        break;
                    num++;
                    continue;
                }
                return;
            }
            Service.gI().clanDonate(clanMessage.id);
        }

        private static void HarvestPean()
        {
            if (TileMap.mapID != 21 && TileMap.mapID != 22 && TileMap.mapID != 23)
                return;
            int num = 0;
            for (int i = 0; i < Char.myCharz().arrItemBox.Length; i++)
            {
                if (Char.myCharz().arrItemBox[i] != null && Char.myCharz().arrItemBox[i].template.type == 6)
                    num += Char.myCharz().arrItemBox[i].quantity;
            }
            if (num < 20 && GameCanvas.gameTick % 100 == 0)
            {
                for (int j = 0; j < Char.myCharz().arrItemBag.Length; j++)
                {
                    if (Char.myCharz().arrItemBag[j] != null && Char.myCharz().arrItemBag[j].template.type == 6)
                    {
                        Service.gI().getItem(1, (sbyte)j);
                        break;
                    }
                }
            }
            if (GameScr.gI().magicTree.currPeas > 0 && (GameScr.hpPotion < 10 || num < 20) && GameCanvas.gameTick % 200 == 0)
            {
                Service.gI().openMenu(4);
                Service.gI().menu(4, 0, 0);
            }
        }

        private static void AutoUsePean()
        {
            if (GameScr.hpPotion > 0)
            {
                if (minimumHPPercent != 0 && isMyHPLowerThan(minimumHP, minimumHPPercent) && GameCanvas.gameTick % 20 == 0)
                { 
                    GameScr.gI().doUseHP();
                    Debug.LogError("minimumHPPercent " + minimumHPPercent);
                }
                else if (minimumMPPercent != 0 && isMyMPLowerThan(minimumMP, minimumMPPercent) && GameCanvas.gameTick % 20 == 0)
                {
                    GameScr.gI().doUseHP();
                }
               
            }
            else
            {
              
            }
        }

        private static int MyHPPercent()
        {
            return (int)(Char.myCharz().cHP * 100L / Char.myCharz().cHPFull);
        }

        private static int MyMPPercent()
        {
            return (int)(Char.myCharz().cMP * 100L / Char.myCharz().cMPFull);
        }

        private static bool isMyHPLowerThan(int minHP, int minHPPercent)
        {
            if (Char.myCharz().cHP > 0)
            {
                if (MyHPPercent() < minHPPercent && minHPPercent != 0)
                {
                    return true; 
                }
                if (minHP  != 0 && Char.myCharz().cHP < minHP)
                {
                    return true;
                }
            }
            return false;
        }

        private static bool isMyMPLowerThan(int minMP, int minMPPercent)
        {
            if (Char.myCharz().cMP > 0)
            {
                if (MyMPPercent() < minMPPercent && minMPPercent != 0)
                {
                    return true;
                }
                if (minMP != 0 && Char.myCharz().cHP < minMP)
                {
                    return true;
                }
            }
            return false;
        }

        static AutoPean()
        {
            inputHPPercent = new string[2] { "Nhập %HP Pean", "%HP" };
            inputMPPercent = new string[2] { "Nhập %MP Pean", "%MP" };
            inputHP = new string[2] { "Nhập HP Pean", "HP" };
            inputMP = new string[2] { "Nhập MP Pean", "MP" };
            LoadData();
        }
    }
}
