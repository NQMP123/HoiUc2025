using System;

namespace Assets.Scripts.Assembly_CSharp.HSNR
{
    public class BossInfo
    {
        public DateTime? timeStart;
        public DateTime? timeEnd;
        public string player;
        public string name;
        public int mapID;
        public string map;
        public BossInfo()
        {
            timeStart = timeEnd = null;
        }
        public string getStartTime()
        {
            if (this.timeStart == null)
            {
                return "Chưa có thông tin";
            }
            TimeSpan timeSpan = DateTime.Now.Subtract(this.timeStart.Value);
            int num = (int)timeSpan.TotalSeconds;
            return string.Concat(new string[]
            {
            this.timeStart.Value.ToString("HH"),
            "h:",
            this.timeStart.Value.ToString("mm"),
            " (",
            (num < 60) ? (num.ToString() + "s") : (timeSpan.Minutes.ToString() + "ph"),
            " trước)"
            });
        }
        public string getStartTimeSpan()
        {
            TimeSpan timeSpan = DateTime.Now.Subtract(this.timeStart.Value);
            int num = (int)timeSpan.TotalSeconds;
            return string.Concat(new string[]
            {
            this.timeStart.Value.ToString("HH"),
            "h:",
            this.timeStart.Value.ToString("mm")
            });
        }
        public string getMapName()
        {
            if (this.map != null && !(this.map == ""))
            {
                return this.map;
            }
            return "Chưa có thông tin";
        }
        public string getTimeEnd()
        {
            if (this.timeEnd == null)
            {
                return "Chưa có thông tin";
            }
            TimeSpan timeSpan = DateTime.Now.Subtract(this.timeEnd.Value);
            int num = (int)timeSpan.TotalSeconds;
            return string.Concat(new string[]
            {
            this.timeEnd.Value.ToString("HH"),
            "h:",
            this.timeEnd.Value.ToString("mm"),
            " (",
            (num < 60) ? (num.ToString() + "s") : (timeSpan.Minutes.ToString() + "ph"),
            " trước)"
            });
        }
        public string getPlayerName()
        {
            if (this.player != null && !(this.player == ""))
            {
                return this.player;
            }
            return "Chưa có thông tin";
        }
    }
}
