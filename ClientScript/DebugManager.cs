//using System;
//using System.Collections.Generic;
//using System.Text;
//using System.IO;
//using UnityEngine;

///// <summary>
///// DebugManager - Quản lý debug logs tối ưu cho Unity APK
///// Hỗ trợ xuất file 30k+ logs, tương thích với Android/iOS
///// Sử dụng Singleton pattern với gI()
///// </summary>
//public class DebugManager
//{
//    #region Singleton Pattern
//    private static DebugManager instance;

//    public static DebugManager gI()
//    {
//        instance ??= new DebugManager();
//        return instance;
//    }

//    // Private constructor để đảm bảo Singleton
//    private DebugManager()
//    {
//        stringBuilder = new StringBuilder(8192);
//        InitializeFileLogging();
//    }
//    #endregion

//    #region Fields
//    private bool enableDebug = false;
//    private readonly List<string> debugMessages = new List<string>();
//    private readonly object lockObject = new object();
//    private int maxDebugEntries = int.MaxValue; // Tăng lên 30k cho APK

//    // Cache StringBuilder để tối ưu performance
//    private StringBuilder stringBuilder;

//    // File logging
//    private string logFilePath = "";
//    private string logDirectory = "";
//    private int logFileCounter = 1;
//    private const int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB per file
//    private bool autoSaveEnabled = true;
//    private int autoSaveInterval = 1000; // Auto save every 1000 messages
//    private int messagesSinceLastSave = 0;
//    #endregion

//    #region Initialization
//    private void InitializeFileLogging()
//    {
//        try
//        {
//            // Sử dụng persistentDataPath cho APK - không cần permission
//            logDirectory = Path.Combine(Application.persistentDataPath, "DebugLogs");

//            // Tạo thư mục nếu chưa có
//            if (!Directory.Exists(logDirectory))
//            {
//                Directory.CreateDirectory(logDirectory);
//            }

//            // Tạo tên file với timestamp
//            string timestamp = DateTime.Now.ToString("yyyyMMdd_HHmmss");
//            logFilePath = Path.Combine(logDirectory, $"debug_log_{timestamp}_{logFileCounter:D3}.txt");

//            Debug.Log($"📁 DebugManager initialized. Log path: {logFilePath}");
//        }
//        catch (Exception e)
//        {
//            Debug.LogError($"Failed to initialize file logging: {e.Message}");
//        }
//    }
//    #endregion

//    #region Public Methods

//    /// <summary>
//    /// Bật/tắt debug mode
//    /// </summary>
//    /// <param name="enable">True để bật debug, False để tắt</param>
//    public void SetDebugEnabled(bool enable)
//    {
//        enableDebug = enable;

//        if (enableDebug)
//        {
//            AddDebugString($"[DEBUG] Debug mode enabled at {DateTime.Now:HH:mm:ss}");
//            AddDebugString($"[DEBUG] Log directory: {logDirectory}");
//            AddDebugString($"[DEBUG] Max entries: {maxDebugEntries}");
//        }
//    }

//    /// <summary>
//    /// Thêm string vào debug log
//    /// </summary>
//    /// <param name="message">Nội dung cần debug</param>
//    public void AddDebugString(string message)
//    {
//        if (!enableDebug || string.IsNullOrEmpty(message))
//            return;

//        lock (lockObject)
//        {
//            // Thêm timestamp cho message
//            string timestampedMessage = $"[{DateTime.Now:HH:mm:ss.fff}] {message}";

//            debugMessages.Add(timestampedMessage);
//            messagesSinceLastSave++;

//            // Giới hạn số lượng entries để tránh memory overflow
//            if (debugMessages.Count > maxDebugEntries)
//            {
//                debugMessages.RemoveAt(0);
//            }

//            // Auto save nếu đủ số lượng messages
//            if (autoSaveEnabled && messagesSinceLastSave >= autoSaveInterval)
//            {
//                SaveToFileAsync();
//            }
//        }

//        // Log ra Unity Console (chỉ trong Editor hoặc Development Build)
//#if UNITY_EDITOR || DEVELOPMENT_BUILD
//        Debug.Log(message);
//#endif
//    }

//    /// <summary>
//    /// Lưu toàn bộ debug content ra file và trả về đường dẫn
//    /// </summary>
//    /// <param name="fileName">Tên file tùy chọn</param>
//    /// <returns>Đường dẫn file đã lưu</returns>
//    public string GetDebugContent(string fileName = null)
//    {
//        if (!enableDebug)
//        {
//            Debug.LogWarning("Debug is disabled");
//            return "";
//        }

//        lock (lockObject)
//        {
//            try
//            {
//                string content = BuildDebugContent();
//                string filePath = SaveContentToFile(content, fileName);

//                AddDebugString($"[EXPORT] Debug content saved to: {filePath}");
//                Debug.Log($"📄 Debug content exported to: {filePath}");
//                CopyToClipboard(filePath);
//                return filePath;
//            }
//            catch (Exception e)
//            {
//                string error = $"Failed to export debug content: {e.Message}";
//                AddDebugString($"[ERROR] {error}");
//                Debug.LogError(error);
//                return "";
//            }
//        }
//    }
//    private void CopyToClipboard(string text)
//    {
//        try
//        {
//            // Sử dụng GUIUtility cho cross-platform clipboard
//            GUIUtility.systemCopyBuffer = text;
//            AddDebugString("[DEBUG] Content copied to clipboard");
//        }
//        catch (Exception e)
//        {
//            AddDebugString($"[WARNING] Clipboard copy failed: {e.Message}");
//        }
//    }
//    /// <summary>
//    /// Lấy toàn bộ debug content dạng string (không lưu file)
//    /// </summary>
//    /// <returns>String chứa toàn bộ debug messages</returns>
//    public string GetDebugContentRaw()
//    {
//        if (!enableDebug)
//            return "Debug is disabled";

//        lock (lockObject)
//        {
//            return BuildDebugContent();
//        }
//    }

//    /// <summary>
//    /// Lưu logs ra file ngay lập tức
//    /// </summary>
//    /// <returns>Đường dẫn file đã lưu</returns>
//    public string SaveLogsNow()
//    {
//        return GetDebugContent();
//    }

//    /// <summary>
//    /// Lấy danh sách tất cả files log đã tạo
//    /// </summary>
//    /// <returns>Array đường dẫn files</returns>
//    public string[] GetAllLogFiles()
//    {
//        try
//        {
//            if (!Directory.Exists(logDirectory))
//                return new string[0];

//            return Directory.GetFiles(logDirectory, "debug_log_*.txt");
//        }
//        catch (Exception e)
//        {
//            Debug.LogError($"Failed to get log files: {e.Message}");
//            return new string[0];
//        }
//    }

//    /// <summary>
//    /// Xóa tất cả file logs cũ
//    /// </summary>
//    public void ClearAllLogFiles()
//    {
//        try
//        {
//            string[] logFiles = GetAllLogFiles();
//            foreach (string file in logFiles)
//            {
//                File.Delete(file);
//            }

//            AddDebugString($"[CLEANUP] Deleted {logFiles.Length} log files");
//            Debug.Log($"🗑️ Deleted {logFiles.Length} log files");
//        }
//        catch (Exception e)
//        {
//            string error = $"Failed to clear log files: {e.Message}";
//            AddDebugString($"[ERROR] {error}");
//            Debug.LogError(error);
//        }
//    }

//    /// <summary>
//    /// Lấy thông tin chi tiết về logs
//    /// </summary>
//    /// <returns>Thông tin logging</returns>
//    public string GetLoggingInfo()
//    {
//        lock (lockObject)
//        {
//            StringBuilder info = new StringBuilder();
//            info.AppendLine($"📊 Debug Manager Info:");
//            info.AppendLine($"   Enabled: {enableDebug}");
//            info.AppendLine($"   Messages in memory: {debugMessages.Count}/{maxDebugEntries}");
//            info.AppendLine($"   Log directory: {logDirectory}");
//            info.AppendLine($"   Current log file: {Path.GetFileName(logFilePath)}");
//            info.AppendLine($"   Auto save: {autoSaveEnabled} (every {autoSaveInterval} messages)");
//            info.AppendLine($"   Messages since last save: {messagesSinceLastSave}");

//            try
//            {
//                string[] logFiles = GetAllLogFiles();
//                info.AppendLine($"   Total log files: {logFiles.Length}");

//                long totalSize = 0;
//                foreach (string file in logFiles)
//                {
//                    totalSize += new FileInfo(file).Length;
//                }
//                info.AppendLine($"   Total logs size: {totalSize / 1024 / 1024:F1} MB");
//            }
//            catch (Exception e)
//            {
//                info.AppendLine($"   Error getting file info: {e.Message}");
//            }

//            return info.ToString();
//        }
//    }

//    /// <summary>
//    /// Thêm debug với category
//    /// </summary>
//    /// <param name="category">Category/Tag</param>
//    /// <param name="message">Nội dung debug</param>
//    public void AddDebugString(string category, string message)
//    {
//        if (!enableDebug || string.IsNullOrEmpty(message))
//            return;

//        AddDebugString($"[{category.ToUpper()}] {message}");
//    }

//    /// <summary>
//    /// Thêm debug với level (Info, Warning, Error)
//    /// </summary>
//    /// <param name="message">Nội dung debug</param>
//    /// <param name="level">Level debug</param>
//    public void AddDebugString(string message, DebugLevel level)
//    {
//        if (!enableDebug || string.IsNullOrEmpty(message))
//            return;

//        string levelPrefix = level switch
//        {
//            DebugLevel.Info => "[INFO]",
//            DebugLevel.Warning => "[WARNING]",
//            DebugLevel.Error => "[ERROR]",
//            _ => "[DEBUG]"
//        };

//        AddDebugString($"{levelPrefix} {message}");
//    }

//    /// <summary>
//    /// Xóa toàn bộ debug messages trong memory
//    /// </summary>
//    public void ClearDebugMessages()
//    {
//        lock (lockObject)
//        {
//            debugMessages.Clear();
//            messagesSinceLastSave = 0;
//        }

//        if (enableDebug)
//        {
//            AddDebugString("[DEBUG] Debug messages cleared from memory");
//        }
//    }

//    /// <summary>
//    /// Lấy List<string> copy của debug messages
//    /// </summary>
//    /// <returns>Copy của List debug messages</returns>
//    public List<string> GetDebugMessages()
//    {
//        lock (lockObject)
//        {
//            return new List<string>(debugMessages);
//        }
//    }

//    /// <summary>
//    /// Lấy số lượng debug messages hiện tại
//    /// </summary>
//    /// <returns>Số lượng messages</returns>
//    public int GetDebugCount()
//    {
//        lock (lockObject)
//        {
//            return debugMessages.Count;
//        }
//    }
//    #endregion

//    #region Private Methods

//    /// <summary>
//    /// Build debug content string
//    /// </summary>
//    private string BuildDebugContent()
//    {
//        if (debugMessages.Count == 0)
//            return "No debug messages";

//        stringBuilder.Clear();
//        stringBuilder.AppendLine($"=== DEBUG REPORT - {DateTime.Now:yyyy-MM-dd HH:mm:ss} ===");
//        stringBuilder.AppendLine($"Total Messages: {debugMessages.Count}");
//        stringBuilder.AppendLine($"Platform: {Application.platform}");
//        stringBuilder.AppendLine($"Unity Version: {Application.unityVersion}");
//        stringBuilder.AppendLine($"Device Model: {SystemInfo.deviceModel}");
//        stringBuilder.AppendLine($"OS: {SystemInfo.operatingSystem}");
//        stringBuilder.AppendLine($"Memory: {SystemInfo.systemMemorySize} MB");
//        stringBuilder.AppendLine($"Graphics: {SystemInfo.graphicsDeviceName}");
//        stringBuilder.AppendLine("=" + new string('=', 50));
//        stringBuilder.AppendLine();

//        foreach (string message in debugMessages)
//        {
//            stringBuilder.AppendLine(message);
//        }

//        return stringBuilder.ToString();
//    }

//    /// <summary>
//    /// Lưu content ra file
//    /// </summary>
//    private string SaveContentToFile(string content, string customFileName = null)
//    {
//        string filePath;

//        if (!string.IsNullOrEmpty(customFileName))
//        {
//            string timestamp = DateTime.Now.ToString("yyyyMMdd_HHmmss");
//            filePath = Path.Combine(logDirectory, $"{customFileName}_{timestamp}.txt");
//        }
//        else
//        {
//            // Kiểm tra size file hiện tại
//            if (File.Exists(logFilePath) && new FileInfo(logFilePath).Length > MAX_FILE_SIZE)
//            {
//                // Tạo file mới
//                logFileCounter++;
//                string timestamp = DateTime.Now.ToString("yyyyMMdd_HHmmss");
//                logFilePath = Path.Combine(logDirectory, $"debug_log_{timestamp}_{logFileCounter:D3}.txt");
//            }
//            filePath = logFilePath;
//        }

//        File.WriteAllText(filePath, content, Encoding.UTF8);
//        return filePath;
//    }

//    /// <summary>
//    /// Lưu logs async để không block main thread
//    /// </summary>
//    private void SaveToFileAsync()
//    {
//        try
//        {
//            if (debugMessages.Count == 0) return;

//            string content = BuildDebugContent();
//            string tempFilePath = logFilePath + ".tmp";

//            // Lưu vào file tạm
//            File.WriteAllText(tempFilePath, content, Encoding.UTF8);

//            // Di chuyển file tạm thành file chính
//            if (File.Exists(logFilePath))
//                File.Delete(logFilePath);
//            File.Move(tempFilePath, logFilePath);

//            messagesSinceLastSave = 0;
//        }
//        catch (Exception e)
//        {
//            Debug.LogError($"Failed to auto-save logs: {e.Message}");
//        }
//    }
//    #endregion

//    #region Properties

//    /// <summary>
//    /// Kiểm tra debug có được bật không
//    /// </summary>
//    public bool IsDebugEnabled => enableDebug;

//    /// <summary>
//    /// Số lượng debug messages tối đa
//    /// </summary>
//    public int MaxDebugEntries
//    {
//        get => maxDebugEntries;
//        set => maxDebugEntries = Math.Max(1000, value); // Minimum 1000 entries
//    }

//    /// <summary>
//    /// Bật/tắt auto save
//    /// </summary>
//    public bool AutoSaveEnabled
//    {
//        get => autoSaveEnabled;
//        set => autoSaveEnabled = value;
//    }

//    /// <summary>
//    /// Interval auto save (số messages)
//    /// </summary>
//    public int AutoSaveInterval
//    {
//        get => autoSaveInterval;
//        set => autoSaveInterval = Math.Max(100, value); // Minimum 100 messages
//    }

//    /// <summary>
//    /// Đường dẫn thư mục logs
//    /// </summary>
//    public string LogDirectory => logDirectory;

//    /// <summary>
//    /// Đường dẫn file log hiện tại
//    /// </summary>
//    public string CurrentLogFile => logFilePath;
//    #endregion
//}

///// <summary>
///// Debug Level enum
///// </summary>
//public enum DebugLevel
//{
//    Info,
//    Warning,
//    Error
//}

///// <summary>
///// Extension methods để dễ sử dụng
///// </summary>
//public static class DebugManagerExtensions
//{
//    public static void LogPerformance(this DebugManager debug, string operation, long timeMs)
//    {
//        string level = timeMs > 50 ? "ERROR" : timeMs > 10 ? "WARNING" : "INFO";
//        debug.AddDebugString($"[PERFORMANCE_{level}] {operation} took {timeMs}ms");
//    }

//    public static void LogMemory(this DebugManager debug)
//    {
//        long totalMemory = GC.GetTotalMemory(false);
//        debug.AddDebugString("MEMORY", $"Total: {totalMemory / 1024 / 1024:F1}MB");
//    }

//    public static void LogFPS(this DebugManager debug, float fps)
//    {
//        string level = fps < 20 ? "ERROR" : fps < 40 ? "WARNING" : "INFO";
//        debug.AddDebugString($"[FPS_{level}] Current FPS: {fps:F1}");
//    }
//}