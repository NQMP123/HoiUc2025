﻿using System.Collections.Generic;
using System.Linq;
using UnityEngine;

namespace Assets.Scripts.Assembly_CSharp.HSNR
{
    public class AutoTrain : IActionListener, IChatable
    {
        private static AutoTrain _Instance;

        private static bool isAvoidSuperMob;

        private static bool isGoBack;

        private static bool isGobackCoordinate;

        public static long[] currTimeAK = new long[10];


        private static int gobackX;

        private static int gobackY;

        private static int gobackMapID;

        private static int gobackZoneID;

        public static bool isAutoTrain;

        private static int minimumMPGoHome;

        private static string[] inputMPPercentGoHome;

        public static List<int> listMobIds;

        public static long lastTimeAddNewMob;

        private static long lastTimeTeleportToMob;

        public static AutoTrain getInstance()
        {
            if (_Instance == null)
                _Instance = new AutoTrain();
            return _Instance;
        }

        public static void Update()
        {
            if (isAutoTrain && GameCanvas.gameTick % 20 == 0 && !GameCanvas.menu.showMenu)
                DoIt();
            if (Char.myCharz().cStamina <= 5 && GameCanvas.gameTick % 100 == 0)
                UseGrape();
            if (!isGoBack)
                return;
            if (Char.myCharz().meDead && GameCanvas.gameTick % 100 == 0)
                Service.gI().returnTownFromDead();
            if (isMeOutOfMP())
            {
                int num = 21 + Char.myCharz().cgender;
                if (TileMap.mapID != num)
                {
                    GameScr.isAutoPlay = false;
                    Char.myCharz().mobFocus = null;
                    if (GameCanvas.gameTick % 50 == 0)
                        AutoMap.instance.StartRunToMapId(num);
                }
            }
            else
            {
                if (isMeOutOfMP())
                    return;
                if (TileMap.mapID != gobackMapID)
                {
                    GameScr.isAutoPlay = false;
                    AutoMap.instance.StartRunToMapId(gobackMapID);
                }
                if (TileMap.mapID == gobackMapID)
                {
                    if (!isGobackCoordinate && GameCanvas.gameTick % 100 == 0)
                        GameScr.isAutoPlay = true;
                    if (TileMap.zoneID != gobackZoneID && !Char.ischangingMap && !Controller.isStopReadMessage && GameCanvas.gameTick % 100 == 0)
                        Service.gI().requestChangeZone(gobackZoneID, -1);
                    if (isGobackCoordinate && (Char.myCharz().cx != gobackX || Char.myCharz().cy != gobackY) && GameCanvas.gameTick % 100 == 0)
                        TeleportTo(gobackX, gobackY);
                }
            }
        }

        public void onChatFromMe(string text, string to)
        {
            if (ChatTextField.gI().tfChat.getText() != null && !ChatTextField.gI().tfChat.getText().Equals(string.Empty) && !text.Equals(string.Empty) && text != null)
            {
                if (ChatTextField.gI().strChat.Equals(inputMPPercentGoHome[0]))
                {
                    try
                    {
                        int num = (minimumMPGoHome = int.Parse(ChatTextField.gI().tfChat.getText()));
                        GameScr.info1.addInfo("Về Nhà Khi MP Dưới\n[" + num + "%]", 0);
                    }
                    catch
                    {
                        GameScr.info1.addInfo("%MP Không Hợp Lệ, Vui Lòng Nhập Lại", 0);
                    }
                    GameUtils.gI().resetTF();
                }
            }
            else
                ChatTextField.gI().isShow = false;
        }

        public void onCancelChat() => GameUtils.gI().resetTF();

        public void perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 1:
                    {
                        int num = (int)p;
                        listMobIds.Clear();
                        for (int i = 0; i < GameScr.vMob.size(); i++)
                        {
                            Mob mob = (Mob)GameScr.vMob.elementAt(i);
                            if (!mob.isMobMe && mob.templateId == num)
                                listMobIds.Add(mob.mobId);
                        }
                        TurnOnAutoTrain();
                        break;
                    }
                case 2:
                    {
                        listMobIds.Clear();
                        for (int j = 0; j < GameScr.vMob.size(); j++)
                        {
                            Mob mob2 = (Mob)GameScr.vMob.elementAt(j);
                            if (!mob2.isMobMe)
                                listMobIds.Add(mob2.mobId);
                        }
                        TurnOnAutoTrain();
                        break;
                    }
                case 3:
                    TurnOnAutoTrain();
                    break;
                case 4:
                    isAvoidSuperMob = !isAvoidSuperMob;
                    GameScr.info1.addInfo("Né Siêu Quái\n" + (isAvoidSuperMob ? "[OFF]" : "[ON]"), 0);
                    break;
                case 5:
                    ShowMenuGoback();
                    break;
                case 6:
                    listMobIds.Clear();
                    isAutoTrain = false;
                    GameScr.info1.addInfo("Đã Clear Danh Sách Train!", 0);
                    break;
                case 7:
                    if (Char.myCharz().mobFocus == null)
                        GameScr.info1.addInfo("Vui Lòng Chọn Quái!", 0);
                    if (Char.myCharz().mobFocus != null)
                    {
                        listMobIds.Add(Char.myCharz().mobFocus.mobId);
                        GameScr.info1.addInfo("Đã Thêm Quái: " + Char.myCharz().mobFocus.mobId, 0);
                    }
                    break;
                case 8:
                    isAutoTrain = false;
                    Char.myCharz().mobFocus = null;
                    GameScr.info1.addInfo("Đã Tắt Auto Train!", 0);
                    break;
                case 9:
                    if (isGoBack)
                    {
                        isGoBack = false;
                        GameScr.info1.addInfo("Goback\n[OFF]", 0);
                    }
                    else if (!isGoBack)
                    {
                        isGobackCoordinate = false;
                        isGoBack = true;
                        gobackMapID = TileMap.mapID;
                        gobackZoneID = TileMap.zoneID;
                        GameScr.info1.addInfo("Goback\n[" + TileMap.mapNames[gobackMapID] + "]\n[" + gobackZoneID + "]", 0);
                    }
                    break;
                case 10:
                    if (isGoBack)
                    {
                        isGoBack = false;
                        GameScr.info1.addInfo("Goback\n[OFF]", 0);
                    }
                    else if (!isGoBack)
                    {
                        isGobackCoordinate = true;
                        isGoBack = true;
                        gobackMapID = TileMap.mapID;
                        gobackZoneID = TileMap.zoneID;
                        gobackX = Char.myCharz().cx;
                        gobackY = Char.myCharz().cy;
                        GameScr.info1.addInfo("Goback Tọa Độ\n[" + gobackX + "-" + gobackY + "]", 0);
                    }
                    break;
                case 11:
                    ChatTextField.gI().strChat = inputMPPercentGoHome[0];
                    ChatTextField.gI().tfChat.name = inputMPPercentGoHome[1];
                    ChatTextField.gI().startChat2(getInstance(), string.Empty);
                    break;
            }
        }

        public static void ShowMenu()
        {
            MyVector myVector = new MyVector();
            List<Mob> list = new List<Mob>();
            if (isAutoTrain && !GameScr.canAutoPlay)
                myVector.addElement(new Command("Dừng Auto", getInstance(), 8, null));
            for (int i = 0; i < GameScr.vMob.size(); i++)
            {
                Mob mob = (Mob)GameScr.vMob.elementAt(i);
                if (mob.isMobMe)
                    continue;
                bool flag = false;
                for (int j = 0; j < list.Count; j++)
                {
                    if (mob.templateId == list[j].templateId)
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                {
                    list.Add(mob);
                    myVector.addElement(new Command("Tàn Sát\n" + mob.getTemplate().name + "\n[" + NinjaUtil.getMoneys(mob.maxHp) + "HP]", getInstance(), 1, mob.templateId));
                }
            }
            myVector.addElement(new Command("Tàn Sát Tất Cả", getInstance(), 2, null));
            myVector.addElement(new Command("Đánh Theo Vị Trí", getInstance(), 3, null));
            myVector.addElement(new Command("Loại Trừ\nSiêu Quái\n" + (isAvoidSuperMob ? "[OFF]" : "[ON]"), getInstance(), 4, null));
            myVector.addElement(new Command("Goback", getInstance(), 5, null));
            myVector.addElement(new Command("Làm Trống\nDanh Sách", getInstance(), 6, null));
            if (Char.myCharz().mobFocus != null)
                myVector.addElement(new Command("Thêm\n[" + Char.myCharz().mobFocus.getTemplate().name + "]\n[" + Char.myCharz().mobFocus.mobId + "]", getInstance(), 7, null));
            GameCanvas.menu.startAt(myVector, 3);
        }

        private static void ShowMenuGoback()
        {
            MyVector myVector = new MyVector();
            myVector.addElement(new Command("Goback\n" + (isGoBack ? ("[" + TileMap.mapNames[gobackMapID] + "]\n[" + gobackZoneID + "]") : "[OFF]"), getInstance(), 9, null));
            myVector.addElement(new Command("Goback Tọa Độ\n" + ((!isGoBack || !isGobackCoordinate) ? "[OFF]" : ("[" + gobackX + "-" + gobackY + "]")), getInstance(), 10, null));
            myVector.addElement(new Command("Về Nhà Khi MP Dưới\n[" + minimumMPGoHome + "%]", getInstance(), 11, null));
            GameCanvas.menu.startAt(myVector, 3);
        }

        private static void ResetChatTextField()
        {
            ChatTextField.gI().strChat = "Chat";
            ChatTextField.gI().tfChat.name = "chat";
            ChatTextField.gI().isShow = false;
        }

        private static void TeleportTo(int x, int y)
        {
            GameUtils.gI().GotoXY(x, y);
        }

        private static bool isMeCanAttack(Mob mob)
        {
            
            return true;
        }

        private static bool isMeOutOfMP()
        {
            return Char.myCharz().cMP < Char.myCharz().cMPFull * minimumMPGoHome / 100;
        }

        private static Mob GetNextMob(int type)
        {

            long num = mSystem.currentTimeMillis();
            Mob result = null;
            for (int i = 0; i < GameScr.vMob.size(); i++)
            {
                Mob mob2 = (Mob)GameScr.vMob.elementAt(i);
                if (mob2.status != 0 && mob2.status != 1 && mob2.hp > 0 && !mob2.isMobMe)
                {
                    if (AutoTrain.listMobIds.Contains(mob2.mobId))
                    {
                        return mob2;
                    }
                }
            }
            return result;
        }
        private static int getSkill()
        {
            for (int i = 0; i < GameScr.keySkill.Length; i++)
            {
                if (GameScr.keySkill[i] == Char.myCharz().myskill)
                {
                    return i;
                }
            }
            return 0;
        }

        public static long getTimeSkill(Skill s)
        {
            if (s.template.id == 29 || s.template.id == 22 || s.template.id == 7 || s.template.id == 18 || s.template.id == 23)
            {
                return (long)s.coolDown + 500L;
            }
            long num = (long)((double)s.coolDown * 1.2);
            if (num < 406)
            {
                return 406L;
            }
            return num;
        }
        private static void TurnOnAutoTrain()
        {
            if (listMobIds.Count == 0)
            {
                GameScr.info1.addInfo("Danh Sách Tàn Sát Trống!", 0);
                isAutoTrain = false;
                return;
            }
            isAutoTrain = true;
        }

        static AutoTrain()
        {
            listMobIds = new List<int>();
            minimumMPGoHome = 5;
            inputMPPercentGoHome = new string[2] { "Nhập %MP", "%MP" };
        }

        private static void DoIt()
        {
            if (Char.myCharz().isWaiting())
            {
                return;
            }
            if (Char.myCharz().statusMe == 14 || Char.myCharz().statusMe == 5)
                return;
            if (listMobIds.Count == 0)
            {
                if (mSystem.currentTimeMillis() - lastTimeAddNewMob > 5000L)
                {
                    lastTimeAddNewMob = mSystem.currentTimeMillis();
                    GameScr.info1.addInfo("Danh Sách Tàn Sát Trống!", 0);
                }
                isAutoTrain = false;
                return;
            }
            if (Char.myCharz().mobFocus != null && (Char.myCharz().mobFocus == null || !Char.myCharz().mobFocus.isMobMe))
            {
                if (Char.myCharz().mobFocus.hp <= 0 || Char.myCharz().mobFocus.status == 1 || Char.myCharz().mobFocus.status == 0 || !isMeCanAttack(Char.myCharz().mobFocus))
                    Char.myCharz().mobFocus = null;
            }
            else
            {
                if (AutoPick.isAutoPick)
                {
                    AutoPick.FocusToNearestItem();
                    if (Char.myCharz().itemFocus != null)
                    {
                        AutoPick.PickIt();
                        AutoPick.FocusToNearestItem();
                    }
                }
                else
                    Char.myCharz().itemFocus = null;
                if (Char.myCharz().itemFocus == null)
                {
                    
                    Mob nextMob = GetNextMob(0);
                    if (nextMob == null)
                    {
                        nextMob = GetNextMob(1);
                    }
                    if (nextMob != null)
                    {
                        Char.myCharz().mobFocus = nextMob;
                        TeleportTo(nextMob.x, nextMob.y);
                    }
                }
            }
            if (Char.myCharz().mobFocus == null || (Char.myCharz().skillInfoPaint() != null && Char.myCharz().indexSkill < Char.myCharz().skillInfoPaint().Length && Char.myCharz().dart != null && Char.myCharz().arr != null))
                return;
            if (Char.myCharz().mobFocus != null  && (Math.abs(Char.myCharz().mobFocus.x - Char.myCharz().cx) > 100 || Math.abs(Char.myCharz().mobFocus.y - Char.myCharz().cy) > 100) && mSystem.currentTimeMillis() - lastTimeTeleportToMob > 100L)
            {
                TeleportTo(Char.myCharz().mobFocus.x, Char.myCharz().mobFocus.y);
            }
            Skill skill = null;
            Skill[] skillcheck = Main.isPC ? GameScr.keySkill : GameScr.onScreenSkill;
            for (int m = 0; m < skillcheck.Length; m++)
            {
                Skill s = skillcheck[m];
                List<int> ints = new List<int>() { 6, 9, 10, 20, 22, 24, 19, 7, 11, 18, 26, 8, 14, 21, 23, 25 };
                if (s != null && !ints.Contains(s.template.id) && mSystem.currentTimeMillis() - s.lastTimeUseThisSkill >= s.coolDown + 50)
                {
                    long num2 = 0;
                    num2 = ((skillcheck[m].template.manaUseType == 2) ? 1 : ((skillcheck[m].template.manaUseType == 1) ? (skillcheck[m].manaUse * Char.myCharz().cMPFull / 100) : skillcheck[m].manaUse));
                    if (Char.myCharz().cMP >= num2)
                    {
                        skill = s;
                        break;
                    }
                }
            }
            if (skill != Char.myCharz().myskill)
            {
                Char.myCharz().myskill = skill;
                Service.gI().selectSkill(skill.template.id);
                GameScr.gI().lastSkill = skill;
            }
            if (skill != null)
            {
                if (skill.template.type == 1)
                {
                    Ak();
                    skill.lastTimeUseThisSkill = mSystem.currentTimeMillis();
                }
                else
                {
                    GameScr.gI().doUseSkill(skill, true);
                }
            }
        }
        public static void Ak()
        {
            if (Char.myCharz().stone || Char.isLoadingMap || Char.myCharz().meDead || Char.myCharz().statusMe == 14 || Char.myCharz().statusMe == 5 || Char.myCharz().myskill.template.type == 3 || Char.myCharz().myskill.template.id == 10 || Char.myCharz().myskill.template.id == 11 || Char.myCharz().myskill.paintCanNotUseSkill)
            {
                return;
            }
            int skill = getSkill();
            if (mSystem.currentTimeMillis() - currTimeAK[skill] > getTimeSkill(Char.myCharz().myskill))
            {
                if (GameScr.gI().isMeCanAttackMob(Char.myCharz().mobFocus) && (double)Res.abs(Char.myCharz().mobFocus.xFirst - Char.myCharz().cx) < (double)Char.myCharz().myskill.dx * 1.5)
                {
                    Char.myCharz().myskill.lastTimeUseThisSkill = mSystem.currentTimeMillis();
                    AkMob();
                    currTimeAK[skill] = mSystem.currentTimeMillis();
                }
                else if (Char.myCharz().charFocus != null && Char.myCharz().isMeCanAttackOtherPlayer(Char.myCharz().charFocus) && (double)Res.abs(Char.myCharz().charFocus.cx - Char.myCharz().cx) < (double)Char.myCharz().myskill.dx * 1.5)
                {
                    Char.myCharz().myskill.lastTimeUseThisSkill = mSystem.currentTimeMillis();
                    AkChar();
                    currTimeAK[skill] = mSystem.currentTimeMillis();
                }
            }
        }

        public static void AkChar()
        {
            try
            {
                MyVector myVector = new MyVector();
                myVector.addElement(Char.myCharz().charFocus);
                Service.gI().sendPlayerAttack(new MyVector(), myVector, 2);
                Char.myCharz().cMP -= Char.myCharz().myskill.manaUse;
            }
            catch
            {
            }
        }

        public static void AkMob()
        {
            try
            {
                MyVector myVector = new MyVector();
                myVector.addElement(Char.myCharz().mobFocus);
                Service.gI().sendPlayerAttack(myVector, new MyVector(), 1);
                Char.myCharz().cMP -= Char.myCharz().myskill.manaUse;
            }
            catch
            {
            }
        }
        public static void UseGrape()
        {
            int num = 0;
            Item item;
            while (true)
            {
                if (num < Char.myCharz().arrItemBag.Length)
                {
                    item = Char.myCharz().arrItemBag[num];
                    if (item != null && item.template.id == 212)
                        break;
                    num++;
                    continue;
                }
                int num2 = 0;
                Item item2;
                while (true)
                {
                    if (num2 < Char.myCharz().arrItemBag.Length)
                    {
                        item2 = Char.myCharz().arrItemBag[num2];
                        if (item2 != null && item2.template.id == 211)
                            break;
                        num2++;
                        continue;
                    }
                    return;
                }
                Service.gI().useItem(0, 1, (sbyte)item2.indexUI, -1);
                return;
            }
            Service.gI().useItem(0, 1, (sbyte)item.indexUI, -1);
        }
    }
}
