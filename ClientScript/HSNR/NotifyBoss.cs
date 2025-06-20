using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assets.Moding
{
    class NotifyBoss
    {
        public string bossname;
        public string mapName;
        public DateTime timeSpawn = DateTime.Now;
        public DateTime timeKill;
        public string namePlayerKill = "";
        public static List<string> listNameBoss()
        {
            var x = new List<string>();
            foreach (NotifyBoss boss in notis)
            {
                if (boss != null && !x.Contains(boss.bossname))
                {
                    x.Add(boss.bossname);
                }
            }
            x.Sort();
            return x;
        }
        public static List<NotifyBoss> notis = new List<NotifyBoss>();
        public static List<NotifyBoss> notisByName(string bossname)
        {
            var x = new List<NotifyBoss>();
            foreach (NotifyBoss boss in notis)
            {
                if (boss != null && boss.bossname.Equals(bossname))
                {
                    x.Add(boss);
                }
            }
            x.Reverse();
            return x;
        }
        public string[] getLines()
        {
            return new string[4]
            {
                $"Xuất hiện: {timeSpawn.Hour}h:{timeSpawn.Minute}p:{timeSpawn.Second} ({getTimeAgo(timeSpawn)})",
                "Map :" + mapName,
                "Người tiêu diệt :" + namePlayerKill,
                timeKill == null ? "Thời gian chết : -1" : $"Thời gian chết: {timeKill.Hour}h:{timeKill.Minute}p:{timeKill.Second} ({getTimeAgo(timeKill)})"
            };
        }

        public static string getTimeAgo(DateTime time)
        {
            TimeSpan timeDiff = DateTime.Now - time;
            if (timeDiff.TotalSeconds < 60)
            {
                int seconds = (int)timeDiff.TotalSeconds;
                return $"{seconds}s trước";
            }
            else if (timeDiff.TotalMinutes < 60)
            {
                int minutes = (int)timeDiff.TotalMinutes;
                return $"{minutes}p trước";
            }
            else
            {
                int hours = (int)timeDiff.TotalHours;
                return $"{hours}h trước";
            }
        }
        public static void addSpawnBoss(string bossname, string mapname)
        {
            NotifyBoss boss = new NotifyBoss();
            boss.bossname = bossname;
            boss.mapName = mapname;
            notis.Add(boss);
        }
        public static void addKillBoss(string bossname, string playerKill)
        {
            foreach (NotifyBoss boss in notis)
            {
                if (boss != null && boss.bossname.Equals(bossname) && boss.namePlayerKill.Equals(""))
                {
                    boss.namePlayerKill = playerKill;
                    boss.timeKill = DateTime.Now;
                }
            }
        }
        public static void checkChatVip(string chatVip)
        {
            if (chatVip.StartsWith("BOSS") && chatVip.Contains("vừa xuất hiện tại"))
            {
                try
                {
                    string[] parts = chatVip.Split(new string[] { "BOSS ", " vừa xuất hiện tại " }, StringSplitOptions.None);
                    if (parts.Length == 3)
                    {
                        string bossName = parts[1];
                        string mapName = parts[2];
                        addSpawnBoss(bossName, mapName);
                    }
                }
                catch { }
            }
            else if (chatVip.Contains("Đã tiêu diệt được"))
            {
                try
                {
                    string[] parts = chatVip.Split(new string[] { ": Đã tiêu diệt được ", " mọi người đều ngưỡng mộ." }, StringSplitOptions.None);
                    if (parts.Length == 3)
                    {
                        string playerName = parts[0];
                        string bossName = parts[1];
                        addKillBoss(bossName, playerName);
                    }
                }
                catch { }
            }
        }
    }
}
