using System;
using System.IO;
using UnityEngine;

public class VoiceRecorder
{
    private static VoiceRecorder instance;
    
    private AudioClip recordedClip;
    private bool isRecording = false;
    private bool isPlaying = false;
    private string microphoneDevice;
    private float recordingTime = 0f;
    private const int SAMPLE_RATE = 16000;
    private const int MAX_RECORDING_TIME = 30; // 30 seconds max
    private const float NOISE_GATE_THRESHOLD = 0.02f; // minimum gate threshold
    private const float DYNAMIC_THRESHOLD_FACTOR = 0.002f; // factor of average amplitude
    public static float playbackGain = 1.5f; // amplify playback volume, configurable
    
    public bool IsRecording => isRecording;
    public bool IsPlaying => isPlaying;
    public float RecordingTime => recordingTime;
    
    public static VoiceRecorder gI()
    {
        return instance ?? (instance = new VoiceRecorder());
    }
    
    public VoiceRecorder()
    {
        InitializeMicrophone();
    }
    
    private void InitializeMicrophone()
    {
        if (Microphone.devices.Length > 0)
        {
            microphoneDevice = Microphone.devices[0];
            UnityEngine.Debug.Log("Voice Recorder initialized with device: " + microphoneDevice);
        }
        else
        {
            UnityEngine.Debug.LogError("No microphone device found!");
        }
    }
    
    public bool StartRecording()
    {
        if (string.IsNullOrEmpty(microphoneDevice))
        {
            UnityEngine.Debug.LogError("No microphone device available");
            return false;
        }
        
        if (isRecording)
        {
            UnityEngine.Debug.LogWarning("Already recording");
            return false;
        }
        
        StopAllAudio();
        
        recordedClip = Microphone.Start(microphoneDevice, false, MAX_RECORDING_TIME, SAMPLE_RATE);
        isRecording = true;
        recordingTime = 0f;
        
        UnityEngine.Debug.Log("Started voice recording");
        return true;
    }
    
    public byte[] StopRecording()
    {
        if (!isRecording)
        {
            UnityEngine.Debug.LogWarning("Not currently recording");
            return null;
        }
        
        Microphone.End(microphoneDevice);
        isRecording = false;
        
        if (recordedClip == null)
        {
            UnityEngine.Debug.LogError("No recorded clip available");
            return null;
        }
        
        // Convert AudioClip to byte array
        byte[] audioData = ConvertAudioClipToByteArray(recordedClip);
        UnityEngine.Debug.Log($"Stopped voice recording. Duration: {recordingTime:F1}s, Data size: {audioData?.Length ?? 0} bytes");
        
        return audioData;
    }
    
    public void CancelRecording()
    {
        if (isRecording)
        {
            Microphone.End(microphoneDevice);
            isRecording = false;
            recordingTime = 0f;
            UnityEngine.Debug.Log("Voice recording cancelled");
        }
    }
    
    public bool PlayVoiceMessage(byte[] audioData)
    {
        if (audioData == null || audioData.Length == 0)
        {
            UnityEngine.Debug.LogError("No audio data to play");
            return false;
        }
        
        StopAllAudio();
        
        try
        {
            AudioClip clip = ConvertByteArrayToAudioClip(audioData);
            if (clip != null)
            {
                AudioSource audioSource = GetAudioSource();
                audioSource.clip = clip;
                audioSource.Play();
                isPlaying = true;
                
                // Stop playing after clip duration
                VoiceTimer.schedule(new VoiceTimerTask(() => {
                    isPlaying = false;
                }), (int)(clip.length * 1000));
                
                UnityEngine.Debug.Log($"Playing voice message. Duration: {clip.length:F1}s");
                return true;
            }
        }
        catch (Exception e)
        {
            UnityEngine.Debug.LogError("Error playing voice message: " + e.Message);
        }
        
        return false;
    }
    
    public void StopPlaying()
    {
        if (isPlaying)
        {
            AudioSource audioSource = GetAudioSource();
            audioSource.Stop();
            isPlaying = false;
            UnityEngine.Debug.Log("Stopped voice playback");
        }
    }
    
    private void StopAllAudio()
    {
        if (isRecording)
        {
            CancelRecording();
        }
        if (isPlaying)
        {
            StopPlaying();
        }
    }
    
    public void Update()
    {
        if (isRecording)
        {
            recordingTime += Time.deltaTime / Time.timeScale;
            
            // Auto stop if max time reached
            if (recordingTime >= MAX_RECORDING_TIME)
            {
                StopRecording();
            }
        }
    }
    
    private byte[] ConvertAudioClipToByteArray(AudioClip clip)
    {
        if (clip == null) return null;
        
        float[] samples = new float[clip.samples * clip.channels];
        clip.GetData(samples, 0);

        // Dynamic noise gate filter
        float sum = 0f;
        for (int i = 0; i < samples.Length; i++)
        {
            sum += Mathf.Abs(samples[i]);
        }
        float avg = sum / samples.Length;
        float threshold = Mathf.Max(NOISE_GATE_THRESHOLD, avg * DYNAMIC_THRESHOLD_FACTOR);
        for (int i = 0; i < samples.Length; i++)
        {
            if (Mathf.Abs(samples[i]) < threshold)
            {
                samples[i] = 0f;
            }
        }
        
        // Convert float samples to 16-bit PCM
        byte[] audioData = new byte[samples.Length * 2];
        for (int i = 0; i < samples.Length; i++)
        {
            short sample = (short)(samples[i] * 32767f);
            audioData[i * 2] = (byte)(sample & 0xFF);
            audioData[i * 2 + 1] = (byte)((sample >> 8) & 0xFF);
        }
        
        return CompressAudioData(audioData);
    }
    
    private AudioClip ConvertByteArrayToAudioClip(byte[] audioData)
    {
        try
        {
            byte[] decompressedData = DecompressAudioData(audioData);
            
            // Convert 16-bit PCM to float samples
            float[] samples = new float[decompressedData.Length / 2];
            for (int i = 0; i < samples.Length; i++)
            {
                short sample = (short)((decompressedData[i * 2 + 1] << 8) | decompressedData[i * 2]);
                float s = sample / 32767f;
                samples[i] = Mathf.Clamp(s, -1f, 1f);
            }

            // Apply dynamic noise gate then amplify
            float sum = 0f;
            for (int i = 0; i < samples.Length; i++)
            {
                sum += Mathf.Abs(samples[i]);
            }
            float avg = sum / samples.Length;
            float threshold = Mathf.Max(NOISE_GATE_THRESHOLD, avg * DYNAMIC_THRESHOLD_FACTOR);
            for (int i = 0; i < samples.Length; i++)
            {
                if (Mathf.Abs(samples[i]) < threshold)
                {
                    samples[i] = 0f;
                }
                else
                {
                    float s = samples[i] * playbackGain;
                    samples[i] = Mathf.Clamp(s, -1f, 1f);
                }

            }
            
            AudioClip clip = AudioClip.Create("VoiceMessage", samples.Length, 1, SAMPLE_RATE, false);
            clip.SetData(samples, 0);
            
            return clip;
        }
        catch (Exception e)
        {
            UnityEngine.Debug.LogError("Error converting audio data: " + e.Message);
            return null;
        }
    }
    
    private byte[] CompressAudioData(byte[] audioData)
    {
        // Simple compression - could be improved with proper audio compression
        try
        {
            using (MemoryStream ms = new MemoryStream())
            {
                using (System.IO.Compression.GZipStream gz = new System.IO.Compression.GZipStream(ms, System.IO.Compression.CompressionMode.Compress))
                {
                    gz.Write(audioData, 0, audioData.Length);
                }
                return ms.ToArray();
            }
        }
        catch (Exception e)
        {
            UnityEngine.Debug.LogError("Error compressing audio: " + e.Message);
            return audioData; // Return original if compression fails
        }
    }
    
    private byte[] DecompressAudioData(byte[] compressedData)
    {
        try
        {
            using (MemoryStream ms = new MemoryStream(compressedData))
            {
                using (System.IO.Compression.GZipStream gz = new System.IO.Compression.GZipStream(ms, System.IO.Compression.CompressionMode.Decompress))
                {
                    using (MemoryStream output = new MemoryStream())
                    {
                        gz.CopyTo(output);
                        return output.ToArray();
                    }
                }
            }
        }
        catch (Exception e)
        {
            UnityEngine.Debug.LogError("Error decompressing audio: " + e.Message);
            return compressedData; // Return original if decompression fails
        }
    }
    
    private AudioSource GetAudioSource()
    {
        // Get or create AudioSource component
        AudioSource audioSource = Camera.main?.GetComponent<AudioSource>();
        if (audioSource == null)
        {
            GameObject audioObj = new GameObject("VoiceMessagePlayer");
            audioSource = audioObj.AddComponent<AudioSource>();
            audioSource.playOnAwake = false;
        }

        // ensure maximum volume
        audioSource.volume = 1.0f;
        return audioSource;
    }
    
    public bool HasMicrophone()
    {
        return !string.IsNullOrEmpty(microphoneDevice);
    }
    
    public string GetMicrophonePermissionStatus()
    {
        if (!HasMicrophone())
            return "No microphone device";
            
        return Application.HasUserAuthorization(UserAuthorization.Microphone) ? "Granted" : "Denied";
    }
}

// Voice Timer helper class for scheduling tasks
public class VoiceTimerTask
{
    private System.Action action;
    
    public VoiceTimerTask(System.Action action)
    {
        this.action = action;
    }
    
    public void Run()
    {
        action?.Invoke();
    }
}

public class VoiceTimer
{
    public static void schedule(VoiceTimerTask task, int delayMs)
    {
        // Simple timer implementation using Unity's Invoke
        GameObject timerObj = new GameObject("VoiceTimer");
        VoiceTimerComponent timer = timerObj.AddComponent<VoiceTimerComponent>();
        timer.ScheduleTask(task, delayMs / 1000f);
    }
}

public class VoiceTimerComponent : MonoBehaviour
{
    public void ScheduleTask(VoiceTimerTask task, float delay)
    {
        Invoke(nameof(ExecuteTask), delay);
        this.task = task;
    }
    
    private VoiceTimerTask task;
    
    private void ExecuteTask()
    {
        task?.Run();
        Destroy(gameObject);
    }
}