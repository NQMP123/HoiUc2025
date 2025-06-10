using System.Collections.Generic;
using System;
using UnityEngine.Analytics;

namespace Assets.Scripts.Assembly_CSharp.HSNR
{
    public class Boss
    {
        public Dictionary<string, List<BossInfo>> hashBosses;
        private List<BossInfo> bosses = new List<BossInfo>();
        public DateTime AppearTime;
        private static Boss instance;
        public bool isShow;
        public Boss()
        {
            hashBosses = new Dictionary<string, List<BossInfo>>();
        }
        public static Boss gI()
        {
            if (instance == null)
            {
                instance = new Boss();
            }
            return instance;
        }
        private void getKillerInfo(string player)
        {
            string[] array = player.Replace(": Đã tiêu diệt được ", "|").Replace(" mọi người đều ngưỡng mộ.", "").Split(new char[] { '|' });
            if (!this.hashBosses.ContainsKey(array[1].Trim()))
            {
                this.hashBosses.Add(array[1].Trim(), new List<BossInfo>());
            }
            List<BossInfo> list = this.hashBosses[array[1].Trim()];
            if (list.Count == 0)
            {
                list.Add(new BossInfo
                {
                    player = array[0].Trim(),
                    name = array[1].Trim(),
                    timeEnd = new DateTime?(DateTime.Now)
                });
                return;
            }
            list[0].player = array[0].Trim();
            list[0].name = array[1].Trim();
            list[0].timeEnd = new DateTime?(DateTime.Now);
        }
        private int getMapID(string mapName)
        {
            for (int i = 0; i < TileMap.mapNames.Length; i++)
            {
                if (TileMap.mapNames[i] != null && TileMap.mapNames[i] == mapName)
                {
                    return i;
                }
            }
            return -1;
        }
        private void getBOSSInfo(string string_0)
        {
            string[] array = string_0.Replace(string_0.StartsWith("BOSS") ? "BOSS " : "Boss ", "").Replace(" vừa xuất hiện tại ", "|").Replace(" appear at ", "|")
                .Split(new char[] { '|' });
            BossInfo bossInfo = new BossInfo
            {
                name = array[0].Trim(),
                map = array[1].Trim(),
                player = "",
                mapID = this.getMapID(array[0]),
                timeStart = new DateTime?(DateTime.Now)
            };
            if (!this.hashBosses.ContainsKey(bossInfo.name))
            {
                this.hashBosses.Add(bossInfo.name, new List<BossInfo>());
            }
            this.hashBosses[bossInfo.name].Insert(0, bossInfo);
            bosses.Add(bossInfo);
            if (bosses.Count > 5)
            {
                bosses.RemoveAt(0);
            }
        }
        public void paintBossesScreen(mGraphics g)
        {
            if (!isShow) return;
            for (int i = 0; i < bosses.Count; i++)
            {
                var bossInfos = bosses[i];
                string name = bossInfos.name;
                string map = bossInfos.getMapName();
                var time = bossInfos.getStartTimeSpan();
                TimeSpan timeSpan = DateTime.Now.Subtract(AppearTime);
                int num = (int)timeSpan.TotalSeconds;
                string bBoss = string.Concat(new object[] {
                name + " - " + map + " - " + ((num < 60) ? (num + "s") : (timeSpan.Minutes + "ph")) + " trước"
                });
                g.setColor(2721889, 0.5f);
                g.fillRect(GameCanvas.w - 23, 39 + 12 * i, 21, 9);
                mFont.tahoma_7b_yellow.drawString(g, bBoss, GameCanvas.w, 37 + 12 * i, mFont.RIGHT, mFont.tahoma_7b_dark);
            }
        }
        public void chatVip(string info)
        {
            if (info.Contains("tiêu diệt"))
            {
                getKillerInfo(info);
            }
            else if (info.StartsWith("BOSS") || info.StartsWith("Boss"))
            {
                getBOSSInfo(info);
                AppearTime = DateTime.Now;
            }

        }
    }
}
