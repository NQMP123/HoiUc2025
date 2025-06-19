using UnityEngine;

public class GameMidlet
{
    public static string IP = "160.191.2.73";

    public static int PORT = 14445;

    public static string IP2;

    public static int PORT2;

    public static string VOICE_IP;
    public static int VOICE_PORT = 14446;
    public static string ICON_IP;
    public static int ICON_PORT = 14447;

    public static sbyte PROVIDER;

    public static string VERSION = "0.0.3";

    public static GameCanvas gameCanvas;

    public static GameMidlet instance;

    public static bool isConnect2;

    public static bool isBackWindowsPhone;

    public GameMidlet()
    {
        initGame();
    }

    public void initGame()
    {
        instance = this;
        MotherCanvas.instance = new MotherCanvas();
        Session_ME.gI().setHandler(Controller.gI());
        Session_ME2.gI().setHandler(Controller.gI());
        VoiceSession.gI().setHandler(Controller.gI());
        IconSession.gI().setHandler(Controller.gI());
        Session_ME2.isMainSession = false;
        VOICE_IP = IP;
        ICON_IP = IP;
        instance = this;
        gameCanvas = new GameCanvas();
        gameCanvas.start();
        SplashScr.loadImg();
        SplashScr.loadSplashScr();
        GameCanvas.currentScreen = new SplashScr();
    }

    public void exit()
    {
        if (Main.typeClient == 6)
        {
            mSystem.exitWP();
            return;
        }
        GameCanvas.bRun = false;
        mSystem.gcc();
        notifyDestroyed();
    }

    public static void sendSMS(string data, string to, Command successAction, Command failAction)
    {
        Cout.println("SEND SMS");
    }

    public static void flatForm(string url)
    {
        Cout.LogWarning("PLATFORM REQUEST: " + url);
        Application.OpenURL(url);
    }

    public void notifyDestroyed()
    {
        Main.exit();
    }

    public void platformRequest(string url)
    {
        Cout.LogWarning("PLATFORM REQUEST: " + url);
        Application.OpenURL(url);
    }
}
