﻿using System.Collections.Generic;
using System;

namespace Assets.Scripts.Assembly_CSharp.HSNR
{
    public class AutoPick : IActionListener, IChatable
    {
        private static AutoPick _Instance;

        public static bool isAutoPick;

        public static long lastTimePickedItem;

        private static int maximumPickDistance;

        private static bool isTeleportToItem;

        private static bool isPickAll;

        public static int pickByList;

        private static List<int> listItemAutoPick;

        private static string[] inputMaximumPickDistance;

        private static string[] inputItemID;

        public static AutoPick getInstance()
        {
            if (_Instance == null)
                _Instance = new AutoPick();
            return _Instance;
        }

        public static void Update()
        {
            if (!isAutoPick)
                return;
            if (isNRDMap(TileMap.mapID))
                try
                {
                    for (int i = 0; i < GameScr.vItemMap.size(); i++)
                    {
                        ItemMap itemMap = (ItemMap)GameScr.vItemMap.elementAt(i);
                        if (itemMap == null || !isNRD(itemMap))
                        {
                            continue;
                        }
                        int num = Math.Abs(Char.myCharz().cx - itemMap.x);
                        if (itemMap != null && num >= 60)
                        {
                            TeleportTo(itemMap.x, itemMap.y);
                        }
                        if ((itemMap.playerId == Char.myCharz().charID || itemMap.playerId == -1) && num <= 60 && itemMap != null && mSystem.currentTimeMillis() - lastTimePickedItem > 550L && isNRD(itemMap))
                        {
                            Service.gI().pickItem(itemMap.itemMapID);
                            lastTimePickedItem = mSystem.currentTimeMillis();
                            break;
                        }
                    }
                }
                catch (Exception)
                {
                }
            if (!isNRDMap(TileMap.mapID))
            {
                FocusToNearestItem();
                if (Char.myCharz().itemFocus != null)
                    PickIt();
            }
        }

        public void onChatFromMe(string text, string to)
        {
            if (ChatTextField.gI().tfChat.getText() != null && !ChatTextField.gI().tfChat.getText().Equals(string.Empty) && !text.Equals(string.Empty) && text != null)
            {
                if (ChatTextField.gI().strChat.Equals(inputMaximumPickDistance[0]))
                {
                    try
                    {
                        int num = (maximumPickDistance = int.Parse(ChatTextField.gI().tfChat.getText()));
                        GameScr.info1.addInfo("Khoảng Cách Nhặt: " + num, 0);
                    }
                    catch
                    {
                        GameScr.info1.addInfo("Số Không Hợp Lệ, Vui Lòng Nhập Lại!", 0);
                    }
                   GameUtils.gI().resetTF();
                }
                else if (ChatTextField.gI().strChat.Equals(inputItemID[0]))
                {
                    try
                    {
                        int num2 = int.Parse(ChatTextField.gI().tfChat.getText());
                        listItemAutoPick.Add(num2);
                        GameScr.info1.addInfo("Đã Thêm Item " + num2, 0);
                    }
                    catch
                    {
                        GameScr.info1.addInfo("Số Không Hợp Lệ, Vui Lòng Nhập Lại!", 0);
                    }
                   GameUtils.gI().resetTF();
                }
            }
            else
                ChatTextField.gI().isShow = false;
        }

        public void onCancelChat()
        {
            if (GameScr.isPaintMessage)
            {
                GameScr.isPaintMessage = false;
                ChatTextField.gI().center = null;
            }
            GameUtils.gI().resetTF();
        }
        public bool autoNRSD;
        public void perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 1:
                    isAutoPick = !isAutoPick;
                    pickByList = 0;
                    GameScr.info1.addInfo("Auto Nhặt\n" + (isAutoPick ? "[ON]" : "[OFF]"), 0);
                    break;
                case 2:
                    isPickAll = !isPickAll;
                    GameScr.info1.addInfo("Nhặt Tất Cả\n" + (isPickAll ? "[ON]" : "[OFF]"), 0);
                    break;
                case 3:
                    isAutoPick = !isAutoPick;
                    pickByList = 1;
                    GameScr.info1.addInfo("Nhặt Theo Danh Sách\n" + (isAutoPick ? "[ON]" : "[OFF]"), 0);
                    break;
                case 4:
                    isTeleportToItem = !isTeleportToItem;
                    GameScr.info1.addInfo("Dịch Đến Item\n" + (isTeleportToItem ? "[ON]" : "[OFF]"), 0);
                    break;
                case 5:
                    ChatTextField.gI().strChat = inputMaximumPickDistance[0];
                    ChatTextField.gI().tfChat.name = inputMaximumPickDistance[1];
                    ChatTextField.gI().startChat2(getInstance(), string.Empty);
                    break;
                case 6:
                    if (listItemAutoPick.Count == 0)
                        GameScr.info1.addInfo("Danh Sách Trống!", 0);
                    if (listItemAutoPick.Count > 0)
                    {
                        string text = "";
                        for (int i = 0; i < listItemAutoPick.Count; i++)
                        {
                            text = text + listItemAutoPick[i] + " ";
                        }
                        GameScr.info1.addInfo(text, 0);
                    }
                    break;
                case 7:
                    listItemAutoPick.Clear();
                    GameScr.info1.addInfo("Đã Clear Danh Sách Nhặt!", 0);
                    break;
                case 8:
                    ChatTextField.gI().strChat = inputItemID[0];
                    ChatTextField.gI().tfChat.name = inputItemID[1];
                    ChatTextField.gI().startChat2(getInstance(), string.Empty);
                    break;
                case 9:
                    listItemAutoPick.Add(Char.myCharz().itemFocus.template.id);
                    GameScr.info1.addInfo("Đã thêm " + Char.myCharz().itemFocus.template.name + " [" + Char.myCharz().itemFocus.template.id + "]", 0);
                    break;
            }
        }

        public static void ShowMenu()
        {
            MyVector myVector = new MyVector();
            myVector.addElement(new Command("Auto Nhặt\n" + ((!isAutoPick || pickByList != 0) ? "[OFF]" : "[ON]"), getInstance(), 1, null));
            myVector.addElement(new Command("Nhặt Tất Cả\n" + (isPickAll ? "[ON]" : "[OFF]"), getInstance(), 2, null));
            myVector.addElement(new Command("Nhặt Theo Danh Sách\n" + ((!isAutoPick || pickByList != 1) ? "[OFF]" : "[ON]"), getInstance(), 3, null));
            myVector.addElement(new Command("Dịch Đến Item\n" + (isTeleportToItem ? "[ON]" : "[OFF]"), getInstance(), 4, null));
            myVector.addElement(new Command("Khoảng Cách Nhặt\n[" + maximumPickDistance + "]", getInstance(), 5, null));
            myVector.addElement(new Command("Xem Danh Sách Nhặt", getInstance(), 6, null));
            myVector.addElement(new Command("Clear Danh Sách Nhặt", getInstance(), 7, null));
            myVector.addElement(new Command("Thêm ItemID", getInstance(), 8, null));
            if (Char.myCharz().itemFocus != null)
                myVector.addElement(new Command("Thêm: " + Char.myCharz().itemFocus.template.name + " [" + Char.myCharz().itemFocus.template.id + "] ", getInstance(), 9, null));
            GameCanvas.menu.startAt(myVector, 3);
        }

        private static void ResetChatTextField()
        {
            ChatTextField.gI().strChat = "Chat";
            ChatTextField.gI().tfChat.name = "chat";
            ChatTextField.gI().isShow = false;
        }

        private static void smethod_0()
        {
        }

        private static void smethod_1()
        {
        }

        public static void FocusToNearestItem()
        {
            if (Char.myCharz().itemFocus != null)
                return;
            int num = 0;
            ItemMap itemMap;
            while (true)
            {
                if (num < GameScr.vItemMap.size())
                {
                    itemMap = (ItemMap)GameScr.vItemMap.elementAt(num);
                    int num2 = Math.abs(Char.myCharz().cx - itemMap.x);
                    int num3 = Math.abs(Char.myCharz().cy - itemMap.y);
                    if (num2 <= maximumPickDistance && num3 <= maximumPickDistance && isPickIt(itemMap) && itemMap.template.id != 673)
                        break;
                    num++;
                    continue;
                }
                return;
            }
            Char.myCharz().itemFocus = itemMap;
        }

        public static void PickIt()
        {
            if (mSystem.currentTimeMillis() - lastTimePickedItem < 550L || Char.myCharz().itemFocus == null)
                return;
            if (isTeleportToItem && !Char.isLockKey)
            {
                TeleportTo(Char.myCharz().itemFocus.x, Char.myCharz().itemFocus.y);
                GameCanvas.clearKeyHold();
                GameCanvas.clearKeyPressed();
                if (Char.myCharz().itemFocus.template.id != 673)
                {
                    Service.gI().pickItem(Char.myCharz().itemFocus.itemMapID);
                    lastTimePickedItem = mSystem.currentTimeMillis();
                }
                return;
            }
            if (Char.myCharz().cx < Char.myCharz().itemFocus.x)
                Char.myCharz().cdir = 1;
            else
                Char.myCharz().cdir = -1;
            int num = Math.abs(Char.myCharz().cx - Char.myCharz().itemFocus.x);
            int num2 = Math.abs(Char.myCharz().cy - Char.myCharz().itemFocus.y);
            if (num <= 40 && num2 < 40)
            {
                GameCanvas.clearKeyHold();
                GameCanvas.clearKeyPressed();
                if (Char.myCharz().itemFocus.template.id != 673)
                {
                    Service.gI().pickItem(Char.myCharz().itemFocus.itemMapID);
                    lastTimePickedItem = mSystem.currentTimeMillis();
                }
            }
            else
            {
                Char.myCharz().currentMovePoint = new MovePoint(Char.myCharz().itemFocus.x, Char.myCharz().itemFocus.y);
                Char.myCharz().endMovePointCommand = new Command(null, null, 8002, null);
                GameCanvas.clearKeyHold();
                GameCanvas.clearKeyPressed();
            }
        }

        private static void TeleportTo(int x, int y)
        {
            Char.myCharz().cx = x;
            Char.myCharz().cy = y;
            Service.gI().charMove();
            Char.myCharz().cx = x;
            Char.myCharz().cy = y + 1;
            Service.gI().charMove();
            Char.myCharz().cx = x;
            Char.myCharz().cy = y;
            Service.gI().charMove();
        }

        private static bool isPickIt(ItemMap item)
        {
            if (isPickAll)
                return true;
            if (pickByList == 0)
            {
                if (item.playerId != Char.myCharz().charID)
                    return item.playerId == -1;
                return true;
            }
            if (pickByList == 1 && listItemAutoPick.Contains(item.template.id))
            {
                if (item.playerId != Char.myCharz().charID)
                    return item.playerId == -1;
                return true;
            }
            return false;
        }

        private static bool isNRDMap(int mapID)
        {
            if (mapID >= 85)
                return mapID <= 91;
            return false;
        }

        private static bool isNRD(ItemMap item)
        {
            if (item.template.id >= 372)
                return item.template.id <= 378;
            return false;
        }

        static AutoPick()
        {
            maximumPickDistance = 50;
            listItemAutoPick = new List<int>();
            inputMaximumPickDistance = new string[2] { "Nhập khoảng cách nhặt", "khoảng cách (>50)" };
            inputItemID = new string[2] { "Nhập ID của item", "ID" };
        }
    }
}
