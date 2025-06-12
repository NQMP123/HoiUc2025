//#if UNITY_EDITOR || UNITY_STANDALONE_WIN
//using UnityEngine;
//using System;
//using System.IO;
//using System.Runtime.InteropServices;
//using System.Threading;

//public static class HackInjectionDetector
//{
//    private const int FIRST_DELAY = 1000, INTERVAL = 30000;

//    /* Win32 declarations */
//    private const uint SNAPMOD = 0x00000008, SNAPMOD32 = 0x10, SNAPPROC = 0x2;
//    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Unicode)]
//    private struct MODULEENTRY32
//    {
//        public uint dwSize, th32ModuleID, th32ProcessID, GlblcntUsage, ProccntUsage;
//        public IntPtr modBaseAddr; public uint modBaseSize; public IntPtr hModule;
//        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 256)] public string szModule;
//        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 260)] public string szExePath;
//    }
//    [DllImport("kernel32.dll")]
//    private static extern IntPtr CreateToolhelp32Snapshot(
//       uint dwFlags,          // TH32CS_*
//       uint th32ProcessId);   // PID (0 = all)

//    [DllImport("kernel32.dll", CharSet = CharSet.Unicode)]
//    private static extern bool Module32First(
//        IntPtr hSnapshot,
//        ref MODULEENTRY32 lpme);

//    [DllImport("kernel32.dll", CharSet = CharSet.Unicode)]
//    private static extern bool Module32Next(
//        IntPtr hSnapshot,
//        ref MODULEENTRY32 lpme);

//    [DllImport("kernel32.dll")]
//    private static extern bool CloseHandle(
//        IntPtr hObject);

//    [DllImport("kernel32.dll")]
//    private static extern uint GetCurrentProcessId();

//    static HackInjectionDetector()
//    {
       
//    }
//    public static void init()
//    {
//        UnityEngine.Debug.LogError("InjectionDetector Started");
//        HackDetectorCore.RegisterContext(SynchronizationContext.Current);
//        new Thread(Worker) { IsBackground = true, Name = "AutoTrain" }.Start();
//    }
//    private static void Worker()
//    {
//        Thread.Sleep(FIRST_DELAY);
//        while (true)
//        {
//            if (HackDetectorCore.White.Count > 0 && Check()) ;     // chỉ quét nếu đã có whitelist
//            Thread.Sleep(INTERVAL);
//        }
//    }

//    private static bool Check()
//    {
//        IntPtr snap = IntPtr.Zero;
//        try
//        {
//            snap = CreateToolhelp32Snapshot(SNAPMOD | SNAPMOD32, GetCurrentProcessId());
//            if (snap == IntPtr.Zero || snap.ToInt64() == -1) return false;

//            MODULEENTRY32 me = new MODULEENTRY32();
//            me.dwSize = (uint)Marshal.SizeOf(typeof(MODULEENTRY32));

//            for (bool n = Module32First(snap, ref me); n; n = Module32Next(snap, ref me))
//            {
//                string f = me.szModule;
//                string p = me.szExePath;

//                if (string.IsNullOrEmpty(f) || !f.EndsWith(".dll", StringComparison.OrdinalIgnoreCase))
//                    continue;

//                bool safe =
//                    HackDetectorCore.White.Contains(f) ||
//                    HackDetectorCore.IsTrustedSignedCached(p) ||
//                    InSystemDir(p) ||
//                    InGameDir(p);

//                if (!safe)
//                {
//                    HackDetectorCore.TriggerDetection("DLL", p);
//                    return true;
//                }
//            }
//        }
//        catch { }
//        finally { if (snap != IntPtr.Zero) CloseHandle(snap); }
//        return false;
//    }

//    private static readonly string GAME_DIR =
//        AppDomain.CurrentDomain.BaseDirectory.ToLowerInvariant();
//    private static bool InSystemDir(string p)
//    {
//        p = p.ToLower(); return p.Contains(@"\windows\system32") || p.Contains(@"\windows\syswow64") || p.Contains(@"\windows\system32\driverstore");
//    }
//    private static bool InGameDir(string p) => p.ToLower().StartsWith(GAME_DIR);
//}
//#endif
