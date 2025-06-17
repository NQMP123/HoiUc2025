using System.Diagnostics;

public class ChatTextField : IActionListener
{
    private static ChatTextField instance;

    public TField tfChat;

    public bool isShow;

    public IChatable parentScreen;

    private long lastChatTime;

    public Command left;

    public Command cmdChat;

    public Command right;

    public Command center;

    private int x;

    private int y;

    private int w;

    private int h;

    private bool isPublic;

    public Command cmdChat2;

    public Command cmdVoiceRecord;

    private bool isRecordingVoice = false;

    private VoiceMessageType currentChatType = VoiceMessageType.WORLD_CHAT;

    public int yBegin;

    public int yUp;

    public int KC;

    public string to;

    public string strChat = "Chat ";

    public ChatTextField()
    {
        tfChat = new TField();
        if (Main.isWindowsPhone)
        {
            tfChat.showSubTextField = false;
        }
        if (Main.isIPhone)
        {
            tfChat.isPaintMouse = false;
        }
        tfChat.name = "chat";
        if (Main.isWindowsPhone)
        {
            tfChat.strInfo = tfChat.name;
        }
        tfChat.width = GameCanvas.w - 6;
        if (Main.isPC && tfChat.width > 250)
        {
            tfChat.width = 250;
        }
        tfChat.height = mScreen.ITEM_HEIGHT + 2;
        tfChat.x = GameCanvas.w / 2 - tfChat.width / 2;
        tfChat.isFocus = true;
        tfChat.setMaxTextLenght(80);
    }

    public void initChatTextField()
    {
        left = new Command(mResources.OK, this, 8000, null, 1, GameCanvas.h - mScreen.cmdH + 1);
        right = new Command(mResources.DELETE, this, 8001, null, GameCanvas.w - 70, GameCanvas.h - mScreen.cmdH + 1);
        center = new Command("MIC", this, 8003, null, GameCanvas.w / 2 - 34, GameCanvas.h - mScreen.cmdH + 1);
        cmdVoiceRecord = center;
        w = tfChat.width + 20;
        h = tfChat.height + 26;
        x = GameCanvas.w / 2 - w / 2;
        y = tfChat.y - 18;
        if (Main.isPC && w > 320)
        {
            w = 320;
        }
        left.x = x-68;
        center.x = x + w - 68;
        right.x = center.x + w - 68;
        if (GameCanvas.isTouch)
        {
            tfChat.y -= 5;
            y -= 20;
            h += 30;
            left.x = GameCanvas.w / 2 - 120 - 5;
            right.x = GameCanvas.w / 2 + 50;
            center.x = GameCanvas.w / 2 - 35;
            left.y = GameCanvas.h - 30;
            right.y = GameCanvas.h - 30;
            center.y = GameCanvas.h - 30;
        }
        cmdChat = new Command();
        ActionChat actionChat = delegate (string str)
        {
            UnityEngine.Debug.Log("chat+" + str);
            tfChat.justReturnFromTextBox = false;
            tfChat.setText(str);
            parentScreen.onChatFromMe(str, to);
            tfChat.setText(string.Empty);
            right.caption = mResources.CLOSE;
        };
        cmdChat.actionChat = actionChat;
        cmdChat2 = new Command();
        cmdChat2.actionChat = delegate (string str)
        {
            tfChat.justReturnFromTextBox = false;
            if (parentScreen != null)
            {
                tfChat.setText(str);
                UnityEngine.Debug.Log("chat2+" + str);
                parentScreen.onChatFromMe(str, to);
                tfChat.setText(string.Empty);
                tfChat.clearKb();
                if (right != null)
                {
                    right.performAction();
                }
            }
            isShow = false;
        };
        yBegin = tfChat.y;
        yUp = GameCanvas.h / 2 - 2 * tfChat.height;
        if (Main.isWindowsPhone)
        {
            tfChat.showSubTextField = false;
        }
        if (Main.isIPhone)
        {
            tfChat.isPaintMouse = false;
        }
    }

    public void updateWhenKeyBoardVisible()
    {
    }

    public void keyPressed(int keyCode)
    {
        if (isShow)
        {
            tfChat.keyPressed(keyCode);
        }
        if (tfChat.getText().Equals(string.Empty))
        {
            right.caption = mResources.CLOSE;
        }
        else
        {
            right.caption = mResources.DELETE;
        }
    }

    public static ChatTextField gI()
    {
        return (instance != null) ? instance : (instance = new ChatTextField());
    }

    public void startChat(int firstCharacter, IChatable parentScreen, string to)
    {
        right.caption = mResources.CLOSE;
        this.to = to;
        if (Main.isWindowsPhone)
        {
            tfChat.showSubTextField = false;
        }
        if (Main.isIPhone)
        {
            tfChat.isPaintMouse = false;
        }
        tfChat.keyPressed(firstCharacter);
        if (!tfChat.getText().Equals(string.Empty) && GameCanvas.currentDialog == null)
        {
            this.parentScreen = parentScreen;
            isShow = true;
        }
    }

    public void startChat(IChatable parentScreen, string to)
    {
        right.caption = mResources.CLOSE;
        this.to = to;
        currentChatType = (string.IsNullOrEmpty(to) || to.Equals("")) ? VoiceMessageType.WORLD_CHAT : VoiceMessageType.PRIVATE_CHAT;
        
        if (Main.isWindowsPhone)
        {
            tfChat.showSubTextField = false;
        }
        if (Main.isIPhone)
        {
            tfChat.isPaintMouse = false;
        }
        if (GameCanvas.currentDialog == null)
        {
            isShow = true;
            tfChat.isFocus = true;
            if (!Main.isPC)
            {
                ipKeyboard.openKeyBoard(strChat, ipKeyboard.TEXT, string.Empty, cmdChat);
                tfChat.setFocusWithKb(isFocus: true);
            }
        }
        tfChat.setText(string.Empty);
        tfChat.clearAll();
        isPublic = false;
        
        UpdateVoiceButtonText();
    }

    public void startChat2(IChatable parentScreen, string to)
    {
        tfChat.setFocusWithKb(isFocus: true);
        this.to = to;
        this.parentScreen = parentScreen;
        currentChatType = (string.IsNullOrEmpty(to) || to.Equals("")) ? VoiceMessageType.WORLD_CHAT : VoiceMessageType.PRIVATE_CHAT;
        
        if (Main.isWindowsPhone)
        {
            tfChat.showSubTextField = false;
        }
        if (Main.isIPhone)
        {
            tfChat.isPaintMouse = false;
        }
        if (GameCanvas.currentDialog == null)
        {
            isShow = true;
            if (!Main.isPC)
            {
                ipKeyboard.openKeyBoard(strChat, ipKeyboard.TEXT, string.Empty, cmdChat2);
                tfChat.setFocusWithKb(isFocus: true);
            }
        }
        tfChat.setText(string.Empty);
        tfChat.clearAll();
        isPublic = false;
        
        UpdateVoiceButtonText();
    }

    public void updateKey()
    {
    }

    public void update()
    {
        if (!isShow)
        {
            return;
        }
        
        // Update voice recording
        if (isRecordingVoice)
        {
            VoiceRecorder.gI().Update();
            UpdateVoiceButtonText();
        }
        
        tfChat.update();
        if (Main.isWindowsPhone)
        {
            updateWhenKeyBoardVisible();
        }
        if (tfChat.justReturnFromTextBox)
        {
            tfChat.justReturnFromTextBox = false;
            UnityEngine.Debug.Log("chat3+" + tfChat.getText());
            parentScreen.onChatFromMe(tfChat.getText(), to);
            tfChat.setText(string.Empty);
            right.caption = mResources.CLOSE;
        }
        if (!Main.isPC)
        {
            return;
        }
        if (GameCanvas.keyPressed[15])
        {
            if (left != null && tfChat.getText() != string.Empty)
            {
                left.performAction();
            }
            GameCanvas.keyPressed[15] = false;
            GameCanvas.keyPressed[(!Main.isPC) ? 5 : 25] = false;
        }
        if (GameCanvas.keyPressed[14])
        {
            if (right != null)
            {
                right.performAction();
            }
            GameCanvas.keyPressed[14] = false;
        }
    }

    public void close()
    {
        tfChat.setText(string.Empty);
        isShow = false;
    }

    public void paint(mGraphics g)
    {
        if (isShow && !Main.isIPhone)
        {
            int num = ((!Main.isWindowsPhone) ? (y - KC) : (tfChat.y - 5));
            int num2 = ((!Main.isWindowsPhone) ? x : 0);
            int num3 = ((!Main.isWindowsPhone) ? w : GameCanvas.w);
            PopUp.paintPopUp(g, num2, num, num3, h, -1, isButton: true);
            if (Main.isPC)
            {
                mFont.tahoma_7b_green2.drawString(g, strChat + to, tfChat.x, tfChat.y - ((!GameCanvas.isTouch) ? 12 : 17), 0);
                GameCanvas.paintz.paintCmdBar(g, left, center, right);
            }
            tfChat.paint(g);
        }
    }

    public void perform(int idAction, object p)
    {
        switch (idAction)
        {
            case 8000:
                Cout.LogError("perform chat 8000");
                if (parentScreen != null)
                {
                    long num = mSystem.currentTimeMillis();
                    if (num - lastChatTime >= 1000)
                    {
                        lastChatTime = num;
                        UnityEngine.Debug.Log("chat4+" + tfChat.getText());
                        parentScreen.onChatFromMe(tfChat.getText(), to);
                        tfChat.setText(string.Empty);
                        right.caption = mResources.CLOSE;
                        tfChat.clearKb();
                    }
                }
                break;
            case 8001:
                Cout.LogError("perform chat 8001");
                if (tfChat.getText().Equals(string.Empty))
                {
                    isShow = false;
                    parentScreen.onCancelChat();
                }
                tfChat.clear();
                break;
            case 8002:
                break;
            case 8003:
                HandleVoiceRecordAction();
                break;
        }
    }

    private void HandleVoiceRecordAction()
    {
        if (!VoiceRecorder.gI().HasMicrophone())
        {
            // Show microphone permission message
            ChatPopup.addChatPopup("Microphone not available or permission denied", 3000, null);
            return;
        }

        if (!isRecordingVoice)
        {
            // Start recording
            if (VoiceRecorder.gI().StartRecording())
            {
                isRecordingVoice = true;
                UpdateVoiceButtonText();
                UnityEngine.Debug.Log($"Started voice recording for {currentChatType}");
            }
            else
            {
                ChatPopup.addChatPopup("Failed to start voice recording", 2000, null);
            }
        }
        else
        {
            // Stop recording and send
            byte[] audioData = VoiceRecorder.gI().StopRecording();
            isRecordingVoice = false;
            UpdateVoiceButtonText();

            if (audioData != null && audioData.Length > 0)
            {
                float duration = VoiceRecorder.gI().RecordingTime;
                SendVoiceMessage(audioData, duration);
            }
            else
            {
                ChatPopup.addChatPopup("Voice recording failed", 2000, null);
            }
        }
    }

    private void SendVoiceMessage(byte[] audioData, float duration)
    {
        if (parentScreen == null) return;

        try
        {
            // Create voice message
            VoiceMessage voiceMsg = new VoiceMessage(
                audioData,
                Char.myCharz().cName,
                currentChatType == VoiceMessageType.PRIVATE_CHAT ? to : null,
                duration,
                currentChatType
            );

            // Add to local storage
            VoiceMessageManager.gI().AddVoiceMessage(voiceMsg);

            // Send to server
            SendVoiceMessageToServer(voiceMsg);

            // Show confirmation
            string chatType = currentChatType == VoiceMessageType.WORLD_CHAT ? "World Chat" : "Private Chat";
            //ChatPopup.addChatPopup($"Voice message sent ({duration:F1}s) - {chatType}", 2000, null);

            UnityEngine.Debug.Log($"Voice message sent: {voiceMsg}");
        }
        catch (System.Exception e)
        {
            UnityEngine.Debug.LogError("Error sending voice message: " + e.Message);
            //ChatPopup.addChatPopup("Failed to send voice message", 2000, null);
        }
    }

    private void SendVoiceMessageToServer(VoiceMessage voiceMsg)
    {
        try
        {
            Message msg;
            msg = new Message(-58);
            if (voiceMsg.IsWorldChat())
            {
                msg.writer().writeByte(0);
            }
            else
            {
                msg.writer().writeByte(1);
                msg.writer().writeUTF(voiceMsg.receiverName);
            }

            msg.writer().writeUTF(voiceMsg.senderName);
            msg.writer().writeFloat(voiceMsg.duration);
            msg.writer().writeLong(voiceMsg.timestamp);
            msg.writer().writeInt(voiceMsg.audioData.Length);
            msg.writer().write(voiceMsg.audioData);

            Session_ME.gI().sendMessage(msg);
            msg.cleanup();
        }
        catch (System.Exception e)
        {
            UnityEngine.Debug.LogError("Error sending voice message to server: " + e.Message);
        }
    }

    private void UpdateVoiceButtonText()
    {
        if (cmdVoiceRecord != null)
        {
            if (isRecordingVoice)
            {
                float recordTime = VoiceRecorder.gI().RecordingTime;
                cmdVoiceRecord.caption = $"REC {recordTime:F1}s";
            }
            else
            {
                string chatTypePrefix = currentChatType == VoiceMessageType.WORLD_CHAT ? "🌍" : "💬";
                cmdVoiceRecord.caption = $"{chatTypePrefix}MIC";
            }
        }
    }
}
