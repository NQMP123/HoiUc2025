using Assets.src.g;
using System;
using UnityEngine;

namespace Assets.Scripts.Assembly_CSharp.HSNR
{
    public class MainMod : IActionListener
    {
        private static MainMod instance;
        public bool isAutoRevive;
        private Button[] button = new Button[11];
        public static MainMod gI()
        {
            if (instance == null)
            {
                instance = new MainMod();
            }
            return instance;

        }
        public MainMod()
        {
            button = new Button[]
               {
                    new Button("J", this, 12),
                    new Button("K", this, 13),
                    new Button("L", this, 14),
                    new Button("A", this, 15),
                    new Button("F", this, 16),
                    new Button("E", this, 17),
                    new Button("G", this, 18),
                    new Button("N", this, 19),
                    new Button("X", this, 20),
                    new Button("C", this, 21),
                    new Button("M", this, 22),
               };
        }
        public void Paint(mGraphics g)
        {
            string mapInfo = string.Concat(new object[]
            {
                $"Map: {TileMap.mapNames[TileMap.mapID]} [{TileMap.zoneID}]\n",
                $"Time: {DateTime.Now.ToString("HH:mm:ss dd/MM/yyyy")}"
            });
            string strHP = NinjaUtil.getMoneys(Char.myCharz().cHP);
            string strMP = NinjaUtil.getMoneys(Char.myCharz().cMP);
            GUIStyle guiStyle = new GUIStyle(GUI.skin.label);
            guiStyle.normal.textColor = Color.yellow;
            mFont.tahoma_7b_red.drawString(g, $"FPS: {((byte)(Main.main.max)).ToString()}", 85, 30, 0, mFont.tahoma_7b_dark);
            long ping = Service.gI().delayPing;
            mFont font = ping < 50 ? mFont.tahoma_7b_green : ping < 100 ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_red;
            font.drawString(g, $"{ping.ToString()} ms", 85, 42, 0, mFont.tahoma_7b_dark);
            mFont.tahoma_7b_white.drawString(g, mapInfo, 20, GameCanvas.hh / 2 + 10, mFont.LEFT, mFont.tahoma_7b_dark);
            guiStyle.fontSize = 8 * mGraphics.zoomLevel;
        
            g.drawString(strHP, 85, 4, guiStyle);
            guiStyle.fontSize = 7 * mGraphics.zoomLevel;
            g.drawString(strMP, 85, 19, guiStyle);
            paintCharInfo(g, Char.myCharz());
            ListChars.gI().paintListCharsInMap(g);
            Boss.gI().paintBossesScreen(g);
            paintModStatus(g);
            paintButton(g);
        }
        private void paintButton(mGraphics g)
        {
            if (GameScr.isAnalog == 1 && GameCanvas.isTouch && !ChatTextField.gI().isShow && !GameCanvas.menu.showMenu && !GameCanvas.panel.isShow && !Char.isLoadingMap && !Char.myCharz().meDead)
            {
                if (button != null)
                {

                    for (int i = 0; i < button.Length; i++)
                    {
                        if (i <= 2)
                        {
                            if (button[i] != null)
                            {
                                button[i].x = 20 + 23 * i;
                                button[i].y = GameCanvas.h - 105;
                                button[i].Paint(g);
                            }
                        }
                        else if (i >= 3 && i <= 8)
                        {
                            if (button[i] != null)
                            {
                               
                                button[i].x =  ((GameCanvas.w / 2) - (23*6)) + 23*i;
                                button[i].y = GameScr.ySkill - GameScr.imgSkill.getHeight() / 2 - 40;
                                button[i].Paint(g);
                            }
                        }
                        else
                        {
                            if (button[i] != null)
                            {
                                button[i].x = GameScr.xTG + 45 + 23 * (i - 11);
                                button[i].y = GameScr.yTG - 30;
                                button[i].Paint(g);
                            }
                        }
                    }
                }
            }
        }
        private void paintCharInfo(mGraphics g, Char c)
        {
            string strInfo = string.Empty;
            if (c.mobFocus != null)
            {
                strInfo = $"{c.mobFocus.getTemplate().name}\n[{NinjaUtil.getMoneys(c.mobFocus.hp)}/{NinjaUtil.getMoneys(c.mobFocus.getTemplate().hp)}]";
            }
            else if (c.charFocus != null && !c.charFocus.isPet && !c.charFocus.isMiniPet)
            {
                strInfo = $"{c.charFocus.cName}\n{NinjaUtil.getMoneys(c.charFocus.cHP)}/{NinjaUtil.getMoneys(c.charFocus.cHPFull)}";
                if (c.charFocus.clanID != -1)
                {
                    strInfo += $"\nClanID: {c.charFocus.clanID}";
                }
            }
            if (!string.IsNullOrEmpty(strInfo))
                mFont.tahoma_7b_red.drawString(g, strInfo, GameCanvas.w / 2, 60, mGraphics.VCENTER | mGraphics.HCENTER, mFont.tahoma_7b_dark);
        }
        private void paintModStatus(mGraphics g)
        {
            int num = GameCanvas.hh / 2 + 30;
            if (AutoSkill.isAutoSendAttack)
            {
                mFont.tahoma_7b_white.drawString(g, "Tự đánh: on", 20, num, 0, mFont.tahoma_7b_dark);
                num += 10;
            }
            if (isAutoRevive)
            {
                mFont.tahoma_7b_white.drawString(g, "Hồi sinh: on", 20, num, 0, mFont.tahoma_7b_dark);
                num += 10;
            }
            if (AutoPick.isAutoPick)
            {
                mFont.tahoma_7b_white.drawString(g, "Auto nhặt: on", 20, num, 0, mFont.tahoma_7b_dark);
                num += 10;
            }
        }
        public void Update()
        {
            ListChars.gI().Update();
            AutoItem.gI().Update();
            AutoMap.instance.Update();
            AutoSkill.Update();
            AutoPean.Update();
            AutoPick.Update();
            AutoTrain.Update();
            if (isAutoRevive)
            {
                if (Char.myCharz().meDead && GameCanvas.gameTick % 20 == 0)
                {
                    if (Char.myCharz().luong > 0)
                    {
                        Service.gI().wakeUpFromDead();
                        Char.myCharz().liveFromDead();
                    }
                }
            }
            if (mSystem.currentTimeMillis() - Service.gI().lastRequestPing >= 1000L)
            {
                Service.gI().pingToServer();
            }
        }
        public void UpdateKey()
        {
            ListChars.gI().UpdateKey();
            if (GameScr.isAnalog == 1 && GameCanvas.isTouch && !ChatTextField.gI().isShow && !GameCanvas.menu.showMenu && !GameCanvas.panel.isShow && !Char.isLoadingMap && !Char.myCharz().meDead)
            {
                if (button != null)
                {
                    for (int i = 0; i < button.Length; i++)
                    {
                        if (button[i] != null && button[i].isPointerPressInside())
                        {
                            button[i].performAction();
                        }
                    }
                }
            }
        }
        public void HotKey()
        {
            switch (GameCanvas.keyAsciiPress)
            {
                case 'x':
                    showMenu();
                    break;
                case 'j':
                    AutoMap.instance.LeftWay();
                    break;
                case 'l':
                    AutoMap.instance.CenterWay();
                    break;
                case 'k':
                    AutoMap.instance.RightWay();
                    break;
                case 'a':
                    AutoSkill.getInstance().perform(1, null);
                    break;
                case 'b':
                    Service.gI().friend(0, -1);
                    break;
                case 'c':
                    for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
                    {
                        Item item = Char.myCharz().arrItemBag[i];
                        if (item != null && (item.template.id == 194 || item.template.id == 193))
                        {
                            Service.gI().useItem(0, 1, (sbyte)item.indexUI, -1);
                            break;
                        }
                    }
                    break;
                case 'd':
                    AutoSkill.FreezeSelectedSkill();
                    break;
                case 'e':
                    isAutoRevive = !isAutoRevive;
                    GameScr.info1.addInfo("Auto Hồi Sinh\n" + (isAutoRevive ? "[ON]" : "[OFF]"), 0);
                    break;
                case 'f':
                    bool isNhapThe = Char.myCharz().isNhapThe;
                    if (GameUtils.gI().IsExistItem(454))
                    {
                        GameUtils.gI().UseItem(454);
                    }
                    if (GameUtils.gI().IsExistItem(921))
                    {
                        GameUtils.gI().UseItem(921);
                    }
                    if (isNhapThe)
                        Service.gI().petStatus(3);
                    break;
                case 'g':
                    if (Char.myCharz().charFocus == null)
                        GameScr.info1.addInfo("Vui Lòng Chọn Mục Tiêu!", 0);
                    else
                    {
                        Service.gI().giaodich(0, Char.myCharz().charFocus.charID, -1, -1);
                        GameScr.info1.addInfo("Đã Gửi Lời Mời Giao Dịch Đến: " + Char.myCharz().charFocus.cName, 0);
                    }
                    break;
                case 'm':
                    Service.gI().openUIZone();
                    break;
                case 'n':
                    AutoPick.getInstance().perform(1, null);
                    break;
                case 't':
                    AutoTrain.ShowMenu();
                    break;
                case 's':
                    for (int j = 0; j < GameScr.vCharInMap.size(); j++)
                    {
                        Char @char = (Char)GameScr.vCharInMap.elementAt(j);
                        if (!@char.cName.Equals("") && isBoss(@char) && (Char.myCharz().charFocus == null || (Char.myCharz().charFocus != null && Char.myCharz().charFocus.cName != @char.cName)))
                        {
                            Char.myCharz().charFocus = @char;
                            break;
                        }
                    }
                    break;
            }
        }
        public static bool isBoss(Char ch)
        {
            if (ch.cName != null && ch.cName != "" && !ch.isPet && !ch.isMiniPet && char.IsUpper(char.Parse(ch.cName.Substring(0, 1))) && ch.cName != "Trọng tài" && !ch.cName.StartsWith("#"))
                return !ch.cName.StartsWith("$");
            return false;
        }
        public void setPosBoolean()
        {
            Rms.saveRMSInt("sanboss", 1);
            Rms.saveRMSInt("showchar", 1);
        }
        public void loadData()
        {
            Boss.gI().isShow = Rms.loadRMSInt("sanboss") == 1;
            ListChars.gI().isShow = Rms.loadRMSInt("showchar") == 1;
        }
        private void showMenu()
        {
            MyVector myVector = new MyVector();
            myVector.addElement(new Command("Auto Map", this, 1, null));
            myVector.addElement(new Command("Auto Skill", this, 2, null));
            myVector.addElement(new Command("Auto Pean", this, 3, null));
            myVector.addElement(new Command("Auto Pick", this, 4, null));
            myVector.addElement(new Command("Auto Train", this, 5, null));
            myVector.addElement(new Command("More", this, 7, null));
            GameCanvas.menu.startAt(myVector, 0);
        }
        private void showMoreMenu()
        {
            MyVector myVector = new MyVector();
            myVector.addElement(new Command("Đồ Họa", this, 8, null));
            GameCanvas.menu.startAt(myVector, 0);
        }
        private void showGraphicsMenu()
        {
            MyVector myVector = new MyVector();
            myVector.addElement(new Command("Thông Báo\nBoss\n" + (Boss.gI().isShow ? "[ON]" : "[OFF]"), this, 9, null));
            myVector.addElement(new Command("Danh Sách\nNhân Vật\nTrong Khu\n" + (ListChars.gI().isShow ? "[ON]" : "[OFF]"), this, 10, null));
         
            GameCanvas.menu.startAt(myVector, 0);
        }
        public bool Chat(string text)
        {
            if (text.StartsWith("k"))
            {
                int zone = int.Parse(text.Substring(1));
                Service.gI().requestChangeZone(zone, -1);
                text = "";
                return true;
            }
            else if (text.StartsWith("cheat"))
            {
                int cheat = int.Parse(text.Substring(5));
                Time.timeScale = cheat;
                text = "";
                return true;
            }
            else if (text.StartsWith("cheatf"))
            {
                float cheat = int.Parse(text.Substring(6)) / 10.0f;
                Time.timeScale = cheat;
                text = "";
                return true;
            }
            else if (text.StartsWith("s"))
            {
                int speed = int.Parse(text.Substring(1));
                Char.myCharz().cspeed = speed;
                text = "";
                return true;
            }
            return false;
        }
        public void perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 1:
                    AutoMap.instance.ShowMapsMenu();
                    break;
                case 2:
                    AutoSkill.ShowMenu();
                    break;
                case 3:
                    AutoPean.ShowMenu();
                    break;
                case 4:
                    AutoPick.getInstance().perform(1, null);
                    break;
                case 5:
                    AutoTrain.ShowMenu();
                    break;
                case 7:
                    showMoreMenu();
                    break;
                case 8:
                    showGraphicsMenu();
                    break;
                case 9:
                    Boss.gI().isShow = !Boss.gI().isShow;
                    Rms.saveRMSInt("sanboss", Boss.gI().isShow ? 1 : 0);
                    break;
                case 10:
                    ListChars.gI().isShow = !ListChars.gI().isShow;
                    Rms.saveRMSInt("showchar", ListChars.gI().isShow ? 1 : 0);
                    break;


            }
            if (idAction >= 12 && idAction <= 22)
            {
                char[] nChar = new char[] { 'j', 'k', 'l', 'a', 'f', 'e', 'g', 'n', 'x', 'c', 'm' };
                GameCanvas.keyAsciiPress = nChar[idAction - 12];
            }
        }
    }
}
