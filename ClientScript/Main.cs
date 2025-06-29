﻿using System.Threading;
using UnityEngine;
using Debug = UnityEngine.Debug;

public class Main : MonoBehaviour
{
    public static bool isPC;

    public static Main main;

    public static mGraphics g;

    public static GameMidlet midlet;

    public static string res = "res";

    public static string mainThreadName;

    public static bool started;

    public static bool isIpod;

    public static bool isIphone4;


    public static bool isWindowsPhone;

    public static bool isIPhone;

    public static bool IphoneVersionApp;

    public static string IMEI;

    public static int versionIp;

    public static int numberQuit = 1;

    public static int typeClient = 4;

    public const sbyte PC_VERSION = 4;

    public const sbyte IP_APPSTORE = 5;

    public const sbyte WINDOWSPHONE = 6;

    private int level;

    public const sbyte IP_JB = 3;

    private int updateCount;

    private long paintCount;

    private long count;

    private int fps;

    public int max;

    private int up;

    private int upmax;

    private long timefps;

    private long timeup;

    private bool isRun;

    public static int waitTick;

    public static int f;

    public static bool isResume;

    public static bool isMiniApp = true;

    public static bool isQuitApp;

    private Vector2 lastMousePos = default(Vector2);

    public static int a = 1;

    private void Start()
    {
       // isIPhone = true;
        if (started)
        {
            return;
        }
        Screen.orientation = ScreenOrientation.LandscapeLeft;
        Screen.sleepTimeout = SleepTimeout.NeverSleep;
        if (Thread.CurrentThread.Name != "Main")
        {
            Thread.CurrentThread.Name = "Main";
        }
        mainThreadName = Thread.CurrentThread.Name;
        if ((Application.platform == RuntimePlatform.Android) || (Application.platform == RuntimePlatform.IPhonePlayer))
        {
            isPC = false;
        }
        else
        {
            isPC = true;
        }
        //isPC = false;
        started = true;
        UnityEngine.Time.timeScale = 2f;
        QualitySettings.vSyncCount = 1; // disable vSync for consistent FPS
        Application.targetFrameRate = 60;
        if (isPC)
        {
            level = 2;

            Screen.SetResolution(1024, 600, false);
        }

    }

    private void SetInit()
    {
        base.enabled = true;
    }

    private void OnHideUnity(bool isGameShown)
    {
        if (!isGameShown)
        {
            Time.timeScale = 0f;
        }
        else
        {
            Time.timeScale = 1f;
        }
    }

    private void OnGUI()
    {
        if (count >= 10)
        {
            if (fps == 0)
            {
                timefps = mSystem.currentTimeMillis();
            }
            else if (mSystem.currentTimeMillis() - timefps > 1000)
            {
                max = fps;
                fps = 0;
                timefps = mSystem.currentTimeMillis();
            }
            fps++;
            checkInput();
            Session_ME.update();
            Session_ME2.update();
            //VoiceSession.gI().update();
            if (Event.current.type.Equals(EventType.Repaint))
            {
                float point = isIPhone ? (Screen.orientation == ScreenOrientation.LandscapeLeft ? Screen.safeArea.x : 0) : 0;
                g.translate(point, 0);
                g.setColor(0);
                g.fillRect((int)point, 0, Screen.width, Screen.height);
                GameMidlet.gameCanvas.paint(g);
                paintCount++;
                g.reset();
            }
        }
    }

    public void setsizeChange()
    {
        if (!isRun)
        {
            Screen.orientation = ScreenOrientation.AutoRotation;
            Application.runInBackground = true;
            QualitySettings.vSyncCount = 1;
            Time.timeScale = 2f;
            Application.targetFrameRate = 60;
            base.useGUILayout = false;
            if (main == null)
            {
                main = this;
            }
            isRun = true;
            ScaleGUI.initScaleGUI();
            if (isPC)
            {
                IMEI = SystemInfo.deviceUniqueIdentifier;
            }
            else
            {
                IMEI = GetMacAddress();
            }
            if (isWindowsPhone)
            {
                typeClient = 6;
            }
            if (isPC)
            {
                typeClient = 4;
            }
            if (IphoneVersionApp)
            {
                typeClient = 5;
            }
            if (iPhoneSettings.generation == iPhoneGeneration.iPodTouch4Gen)
            {
                isIpod = true;
            }
            if (iPhoneSettings.generation == iPhoneGeneration.iPhone4)
            {
                isIphone4 = true;
            }
            g = new mGraphics();
            midlet = new GameMidlet();
            TileMap.loadBg();
            Paint.loadbg();
            PopUp.loadBg();
            GameScr.loadBg();
            InfoMe.gI().loadCharId();
            Panel.loadBg();
            Menu.loadBg();
            Key.mapKeyPC();
            SoundMn.gI().loadSound(TileMap.mapID);
            g.CreateLineMaterial();
        }
    }

    public string GetMacAddress()
    {
        return string.Empty;
    }

    public static void setBackupIcloud(string path)
    {
    }

    public void doClearRMS()
    {
        if (isPC)
        {
            int num = Rms.loadRMSInt("lastZoomlevel");
            if (num != mGraphics.zoomLevel)
            {
                Rms.clearAll();
                Rms.saveRMSInt("lastZoomlevel", mGraphics.zoomLevel);
                //Rms.saveRMSInt("levelScreenKN", level);
            }
        }
    }

    public static void closeKeyBoard()
    {
        if (TouchScreenKeyboard.visible)
        {
            TField.kb.active = false;
            TField.kb = null;
        }
    }

    void FixedUpdate()
    {
        Rms.update();
        count++;
        if (count >= 10)
        {

            if (up == 0)
            {
                timeup = mSystem.currentTimeMillis();
            }
            else if (mSystem.currentTimeMillis() - timeup > 1000)
            {
                upmax = up;
                up = 0;
                timeup = mSystem.currentTimeMillis();
            }
            up++;
            setsizeChange();
            updateCount++;
            ipKeyboard.update();
            GameMidlet.gameCanvas.update();
            Image.update();
            DataInputStream.update();
            f++;
            if (f > 8)
            {
                f = 0;
            }
        }
    }

    private void Update()
    {

    }

    private void checkInput()
    {
        float vPoint = isIPhone ? (Screen.orientation == ScreenOrientation.LandscapeLeft ? Screen.safeArea.x : 0) : 0;
        Vector3 vectorScale = new Vector2(vPoint, 0);
        if (Input.GetMouseButtonDown(0))
        {

            Vector3 mousePosition = Input.mousePosition - vectorScale;
            GameMidlet.gameCanvas.pointerPressed((int)(mousePosition.x / (float)mGraphics.zoomLevel), (int)(((float)Screen.height - mousePosition.y) / (float)mGraphics.zoomLevel) + mGraphics.addYWhenOpenKeyBoard);
            lastMousePos.x = mousePosition.x / (float)mGraphics.zoomLevel;
            lastMousePos.y = mousePosition.y / (float)mGraphics.zoomLevel + (float)mGraphics.addYWhenOpenKeyBoard;
        }
        if (Input.GetMouseButton(0))
        {
            Vector3 mousePosition2 = Input.mousePosition - vectorScale;
            GameMidlet.gameCanvas.pointerDragged((int)(mousePosition2.x / (float)mGraphics.zoomLevel), (int)(((float)Screen.height - mousePosition2.y) / (float)mGraphics.zoomLevel) + mGraphics.addYWhenOpenKeyBoard);
            lastMousePos.x = mousePosition2.x / (float)mGraphics.zoomLevel;
            lastMousePos.y = mousePosition2.y / (float)mGraphics.zoomLevel + (float)mGraphics.addYWhenOpenKeyBoard;
        }
        if (Input.GetMouseButtonUp(0))
        {
            Vector3 mousePosition3 = Input.mousePosition - vectorScale;
            lastMousePos.x = mousePosition3.x / (float)mGraphics.zoomLevel;
            lastMousePos.y = mousePosition3.y / (float)mGraphics.zoomLevel + (float)mGraphics.addYWhenOpenKeyBoard;
            GameMidlet.gameCanvas.pointerReleased((int)(mousePosition3.x / (float)mGraphics.zoomLevel), (int)(((float)Screen.height - mousePosition3.y) / (float)mGraphics.zoomLevel) + mGraphics.addYWhenOpenKeyBoard);
        }
        if (Input.anyKeyDown && Event.current.type == EventType.KeyDown)
        {
            int num = MyKeyMap.map(Event.current.keyCode);
            if (Input.GetKey(KeyCode.LeftShift) || Input.GetKey(KeyCode.RightShift))
            {
                switch (Event.current.keyCode)
                {
                    case KeyCode.Alpha2:
                        num = 64;
                        break;
                    case KeyCode.Minus:
                        num = 95;
                        break;
                }
            }
            if (num != 0)
            {
                GameMidlet.gameCanvas.keyPressedz(num);
            }
        }
        if (Event.current.type == EventType.KeyUp)
        {
            int num2 = MyKeyMap.map(Event.current.keyCode);
            if (num2 != 0)
            {
                GameMidlet.gameCanvas.keyReleasedz(num2);
            }
        }
        if (isPC)
        {
            GameMidlet.gameCanvas.scrollMouse((int)(Input.GetAxis("Mouse ScrollWheel") * 10f));
            float x = Input.mousePosition.x - vectorScale.x;
            float y = Input.mousePosition.y;
            int x2 = (int)x / mGraphics.zoomLevel;
            int y2 = (Screen.height - (int)y) / mGraphics.zoomLevel;
            GameMidlet.gameCanvas.pointerMouse(x2, y2);
        }
    }

    private void OnApplicationQuit()
    {
        Debug.LogWarning("APP QUIT");
        GameCanvas.bRun = false;
        Session_ME.gI().close();
        Session_ME2.gI().close();
        if (isPC)
        {
            Application.Quit();
        }
    }

    private void OnApplicationPause(bool paused)
    {
        isResume = false;
        if (paused)
        {
            if (GameCanvas.isWaiting())
            {
                isQuitApp = true;
            }
        }
        else
        {
            isResume = true;
        }
        if (TouchScreenKeyboard.visible)
        {
            TField.kb.active = false;
            TField.kb = null;
        }
        if (isQuitApp)
        {
            Application.Quit();
        }
    }

    public static void exit()
    {
        if (isPC)
        {
            main.OnApplicationQuit();
        }
        else
        {
            a = 0;
        }
    }

}
