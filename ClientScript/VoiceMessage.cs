using System;

public class VoiceMessage
{
    public byte[] audioData;
    public string senderName;
    public string receiverName; // null for world chat
    public float duration;
    public long timestamp;
    public VoiceMessageType messageType;
    public bool isPlaying = false;
    public bool hasPlayed = false;
    
    public VoiceMessage()
    {
        timestamp = mSystem.currentTimeMillis();
    }
    
    public VoiceMessage(byte[] audioData, string senderName, string receiverName, float duration, VoiceMessageType type)
    {
        this.audioData = audioData;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.duration = duration;
        this.messageType = type;
        this.timestamp = mSystem.currentTimeMillis();
    }
    
    public bool IsWorldChat()
    {
        return messageType == VoiceMessageType.WORLD_CHAT;
    }
    
    public bool IsPrivateChat()
    {
        return messageType == VoiceMessageType.PRIVATE_CHAT;
    }
    
    public string GetDisplayText()
    {
        string durationText = $"{duration:F1}s";
        string icon = isPlaying ? "🔊" : (hasPlayed ? "🎵" : "🎤");
        
        if (IsWorldChat())
        {
            return $"{icon} [Thế Giới] {senderName}: Voice ({durationText})";
        }
        else
        {
            return $"{icon} Voice message ({durationText})";
        }
    }
    
    public void Play()
    {
        if (audioData != null && audioData.Length > 0)
        {
            isPlaying = true;
            bool success = VoiceRecorder.gI().PlayVoiceMessage(audioData);
            if (success)
            {
                hasPlayed = true;
                // Schedule to update playing status
                VoiceTimer.schedule(new VoiceTimerTask(() => {
                    isPlaying = false;
                }), (int)(duration * 1000));
            }
            else
            {
                isPlaying = false;
            }
        }
    }
    
    public void StopPlaying()
    {
        if (isPlaying)
        {
            VoiceRecorder.gI().StopPlaying();
            isPlaying = false;
        }
    }
    
    public long GetAgeInSeconds()
    {
        return (mSystem.currentTimeMillis() - timestamp) / 1000;
    }
    
    public bool IsExpired(int maxAgeSeconds = 300) // 5 minutes default
    {
        return GetAgeInSeconds() > maxAgeSeconds;
    }
    
    public int GetDataSize()
    {
        return audioData?.Length ?? 0;
    }
    
    public override string ToString()
    {
        return $"VoiceMessage[{messageType}] from {senderName} to {receiverName ?? "ALL"} - {duration:F1}s ({GetDataSize()} bytes)";
    }
}

public enum VoiceMessageType
{
    WORLD_CHAT = 0,
    PRIVATE_CHAT = 1
}

public class VoiceMessageManager
{
    private static VoiceMessageManager instance;
    private MyVector voiceMessages;
    private const int MAX_VOICE_MESSAGES = 50;
    private const int MAX_MESSAGE_AGE_SECONDS = 300; // 5 minutes
    public static bool AutoPlay = false;
    
    public static VoiceMessageManager gI()
    {
        return instance ?? (instance = new VoiceMessageManager());
    }
    
    public VoiceMessageManager()
    {
        voiceMessages = new MyVector();
    }
    
    public void AddVoiceMessage(VoiceMessage voiceMsg)
    {
        if (voiceMsg == null) return;

        voiceMessages.addElement(voiceMsg);
        
        // Clean old messages
        CleanupOldMessages();
        
        // Limit total messages
        while (voiceMessages.size() > MAX_VOICE_MESSAGES)
        {
            voiceMessages.removeElementAt(0);
        }
        
        UnityEngine.Debug.Log($"Added voice message: {voiceMsg}");
        if (AutoPlay)
        {
            StopAllVoiceMessages();
            voiceMsg.Play();
        }
    }
    
    public VoiceMessage GetVoiceMessage(int index)
    {
        if (index >= 0 && index < voiceMessages.size())
        {
            return (VoiceMessage)voiceMessages.elementAt(index);
        }
        return null;
    }
    
    public int GetVoiceMessageCount()
    {
        return voiceMessages.size();
    }
    
    public MyVector GetWorldChatVoiceMessages()
    {
        MyVector worldMessages = new MyVector();
        for (int i = 0; i < voiceMessages.size(); i++)
        {
            VoiceMessage msg = (VoiceMessage)voiceMessages.elementAt(i);
            if (msg.IsWorldChat())
            {
                worldMessages.addElement(msg);
            }
        }
        return worldMessages;
    }
    
    public MyVector GetPrivateChatVoiceMessages(string otherPlayerName)
    {
        MyVector privateMessages = new MyVector();
        for (int i = 0; i < voiceMessages.size(); i++)
        {
            VoiceMessage msg = (VoiceMessage)voiceMessages.elementAt(i);
            if (msg.IsPrivateChat() && 
                (msg.senderName.Equals(otherPlayerName) || msg.receiverName.Equals(otherPlayerName)))
            {
                privateMessages.addElement(msg);
            }
        }
        return privateMessages;
    }
    
    public void StopAllVoiceMessages()
    {
        for (int i = 0; i < voiceMessages.size(); i++)
        {
            VoiceMessage msg = (VoiceMessage)voiceMessages.elementAt(i);
            if (msg.isPlaying)
            {
                msg.StopPlaying();
            }
        }
        VoiceRecorder.gI().StopPlaying();
    }
    
    public void CleanupOldMessages()
    {
        for (int i = voiceMessages.size() - 1; i >= 0; i--)
        {
            VoiceMessage msg = (VoiceMessage)voiceMessages.elementAt(i);
            if (msg.IsExpired(MAX_MESSAGE_AGE_SECONDS))
            {
                voiceMessages.removeElementAt(i);
            }
        }
    }
    
    public void ClearAllMessages()
    {
        StopAllVoiceMessages();
        voiceMessages.removeAllElements();
        UnityEngine.Debug.Log("Cleared all voice messages");
    }
    
    public void Update()
    {
        // Update voice recorder
        VoiceRecorder.gI().Update();
        
        // Periodic cleanup
        if (mSystem.currentTimeMillis() % 30000 < 100) // Every 30 seconds
        {
            CleanupOldMessages();
        }
    }
    
    public int GetTotalDataSize()
    {
        int totalSize = 0;
        for (int i = 0; i < voiceMessages.size(); i++)
        {
            VoiceMessage msg = (VoiceMessage)voiceMessages.elementAt(i);
            totalSize += msg.GetDataSize();
        }
        return totalSize;
    }
    
    public string GetStatusInfo()
    {
        return $"Voice Messages: {voiceMessages.size()}/{MAX_VOICE_MESSAGES}, Total Size: {GetTotalDataSize()} bytes";
    }
}