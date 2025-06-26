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

        // Cache để tránh tính toán lại
        private static List<string> _cachedBossNames = null;
        private static Dictionary<string, List<NotifyBoss>> _cachedBossByName = new Dictionary<string, List<NotifyBoss>>();
        private static bool _cacheInvalid = true;

        // Giới hạn số lượng thông báo để tránh lag
        private const int MAX_NOTIFICATIONS = 1000;
        private const int MAX_PER_BOSS = 20;

        public static List<NotifyBoss> notis = new List<NotifyBoss>();

        public static List<string> listNameBoss()
        {
            if (_cacheInvalid || _cachedBossNames == null)
            {
                // Sử dụng HashSet để tránh trùng lặp, sau đó sort
                var uniqueNames = new HashSet<string>();
                foreach (NotifyBoss boss in notis)
                {
                    if (boss != null && !string.IsNullOrEmpty(boss.bossname))
                    {
                        uniqueNames.Add(boss.bossname);
                    }
                }
                _cachedBossNames = uniqueNames.ToList();
                _cachedBossNames.Sort();
                _cacheInvalid = false;
            }
            return _cachedBossNames;
        }

        public static List<NotifyBoss> notisByName(string bossname)
        {
            if (_cacheInvalid || !_cachedBossByName.ContainsKey(bossname))
            {
                var result = new List<NotifyBoss>();
                foreach (NotifyBoss boss in notis)
                {
                    if (boss != null && boss.bossname.Equals(bossname))
                    {
                        result.Add(boss);
                    }
                }
                result.Reverse();
                _cachedBossByName[bossname] = result;
            }
            return _cachedBossByName[bossname];
        }

        // Invalidate cache khi có thay đổi
        private static void InvalidateCache()
        {
            _cacheInvalid = true;
            _cachedBossByName.Clear();
        }

        // Cleanup old notifications để tránh memory leak
        private static void CleanupOldNotifications()
        {
            if (notis.Count > MAX_NOTIFICATIONS)
            {
                // Giữ lại những thông báo mới nhất
                var grouped = notis.GroupBy(n => n.bossname).ToList();
                notis.Clear();

                foreach (var group in grouped)
                {
                    var recentNotis = group.OrderByDescending(n => n.timeSpawn).Take(MAX_PER_BOSS);
                    notis.AddRange(recentNotis);
                }

                InvalidateCache();
            }
        }

        public string[] getLines()
        {
            return new string[4]
            {
                $"Xuất hiện: {timeSpawn.Hour}h:{timeSpawn.Minute}p:{timeSpawn.Second} ({getTimeAgo(timeSpawn)})",
                "Map :" + mapName,
                "Người tiêu diệt :" + namePlayerKill,
                timeKill == default(DateTime) ? "Thời gian chết : -1" : $"Thời gian chết: {timeKill.Hour}h:{timeKill.Minute}p:{timeKill.Second} ({getTimeAgo(timeKill)})"
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

            InvalidateCache();
            CleanupOldNotifications();
        }

        public static void addKillBoss(string bossname, string playerKill)
        {
            // Tìm boss chưa bị kill (tối ưu bằng cách break sớm)
            foreach (NotifyBoss boss in notis)
            {
                if (boss != null && boss.bossname.Equals(bossname) && boss.namePlayerKill.Equals(""))
                {
                    boss.namePlayerKill = playerKill;
                    boss.timeKill = DateTime.Now;
                    InvalidateCache();
                    break; // Chỉ cần update boss đầu tiên tìm thấy
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