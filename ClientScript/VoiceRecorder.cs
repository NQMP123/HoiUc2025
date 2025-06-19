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
    private const int SAMPLE_RATE = 15000;
    private const int MAX_RECORDING_TIME = 30; // 30 seconds max
    private const float NOISE_GATE_THRESHOLD = 0.002f; // minimum gate threshold
    private const float DYNAMIC_THRESHOLD_FACTOR = 0.0001f; // factor of average amplitude
    public static float playbackGain = 1.5f; // amplify playback volume, configurable
    public static float minimumValue = 0.02f; // minimum volume threshold for noise filtering, configurable
    public static float noiseReductionStrength = 0.7f; // strength of noise reduction (0.0 - 1.0)
    public static float speechThreshold = 0.02f; // threshold to detect speech vs silence

    // Adaptive noise reduction parameters
    private float[] noiseProfile = null;
    private const int NOISE_PROFILE_SAMPLES = 1024; // samples for noise profiling
    private const float SMOOTH_FACTOR = 0.1f; // smoothing factor for gating

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

        // Apply advanced noise reduction
        ApplyAdvancedNoiseReduction(samples);

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

            // Apply advanced noise reduction and amplification
            ApplyAdvancedNoiseReduction(samples);
            ApplyAmplification(samples);

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

    /// <summary>
    /// Advanced noise reduction with adaptive noise profiling and smooth gating
    /// </summary>
    /// <param name="samples">Audio samples to process</param>
    private void ApplyAdvancedNoiseReduction(float[] samples)
    {
        // Step 1: Build noise profile from quiet sections
        BuildNoiseProfile(samples);

        // Step 2: Apply windowed analysis and noise reduction
        int windowSize = 512;
        float[] smoothGate = new float[samples.Length];

        for (int i = 0; i < samples.Length; i += windowSize)
        {
            int endIdx = Mathf.Min(i + windowSize, samples.Length);

            // Analyze current window
            float windowEnergy = 0f;
            for (int j = i; j < endIdx; j++)
            {
                windowEnergy += samples[j] * samples[j];
            }
            windowEnergy = Mathf.Sqrt(windowEnergy / (endIdx - i));

            // Determine if this window contains speech
            bool isSpeech = windowEnergy > speechThreshold;

            // Calculate noise reduction factor for this window
            float reductionFactor = CalculateNoiseReduction(windowEnergy, isSpeech);

            // Apply smooth gating
            for (int j = i; j < endIdx; j++)
            {
                smoothGate[j] = reductionFactor;
            }
        }

        // Step 3: Smooth the gating envelope to avoid artifacts
        SmoothGatingEnvelope(smoothGate);

        // Step 4: Apply noise reduction with smooth gating
        for (int i = 0; i < samples.Length; i++)
        {
            // Apply noise reduction based on smooth gate
            float originalSample = samples[i];
            float noiseReduced = ApplySpectralSubtraction(originalSample, i);

            // Blend between original and noise-reduced based on gate
            samples[i] = Mathf.Lerp(noiseReduced, originalSample, smoothGate[i]);

            // Final hard threshold for very low signals
            if (Mathf.Abs(samples[i]) < minimumValue)
            {
                samples[i] = 0f;
            }
        }

        UnityEngine.Debug.Log($"Applied advanced noise reduction (strength: {noiseReductionStrength:F2})");
    }

    /// <summary>
    /// Build noise profile from quiet sections of audio
    /// </summary>
    /// <param name="samples">Audio samples</param>
    private void BuildNoiseProfile(float[] samples)
    {
        if (noiseProfile == null)
        {
            noiseProfile = new float[NOISE_PROFILE_SAMPLES];
        }

        // Find quiet sections (first 10% of audio or sections below speech threshold)
        int quietSamples = 0;
        int maxQuietSamples = Mathf.Min(samples.Length / 10, NOISE_PROFILE_SAMPLES);

        for (int i = 0; i < samples.Length && quietSamples < maxQuietSamples; i++)
        {
            if (Mathf.Abs(samples[i]) < speechThreshold * 0.5f)
            {
                noiseProfile[quietSamples] = samples[i];
                quietSamples++;
            }
        }

        // If not enough quiet samples, use beginning of audio
        if (quietSamples < maxQuietSamples / 2)
        {
            int beginSamples = Mathf.Min(samples.Length, NOISE_PROFILE_SAMPLES);
            for (int i = 0; i < beginSamples; i++)
            {
                noiseProfile[i] = samples[i];
            }
        }
    }

    /// <summary>
    /// Calculate noise reduction factor based on signal characteristics
    /// </summary>
    /// <param name="energy">Window energy</param>
    /// <param name="isSpeech">Whether window contains speech</param>
    /// <returns>Noise reduction factor (0 = full reduction, 1 = no reduction)</returns>
    private float CalculateNoiseReduction(float energy, bool isSpeech)
    {
        if (isSpeech)
        {
            // During speech: reduce noise but preserve speech
            float speechRatio = Mathf.Clamp01((energy - speechThreshold) / speechThreshold);
            return Mathf.Lerp(1f - noiseReductionStrength * 0.5f, 1f, speechRatio);
        }
        else
        {
            // During silence: aggressive noise reduction
            return 1f - noiseReductionStrength;
        }
    }

    /// <summary>
    /// Apply spectral subtraction for noise reduction
    /// </summary>
    /// <param name="sample">Input sample</param>
    /// <param name="index">Sample index</param>
    /// <returns>Noise reduced sample</returns>
    private float ApplySpectralSubtraction(float sample, int index)
    {
        if (noiseProfile == null) return sample;

        // Simple spectral subtraction approximation
        int profileIndex = index % noiseProfile.Length;
        float estimatedNoise = noiseProfile[profileIndex];

        // Subtract estimated noise
        float cleaned = sample - estimatedNoise * noiseReductionStrength;

        // Prevent over-subtraction artifacts
        if (Mathf.Sign(cleaned) != Mathf.Sign(sample))
        {
            cleaned = sample * 0.1f; // Reduce but don't flip phase
        }

        return cleaned;
    }

    /// <summary>
    /// Smooth gating envelope to reduce artifacts
    /// </summary>
    /// <param name="envelope">Gating envelope to smooth</param>
    private void SmoothGatingEnvelope(float[] envelope)
    {
        for (int i = 1; i < envelope.Length; i++)
        {
            envelope[i] = Mathf.Lerp(envelope[i - 1], envelope[i], SMOOTH_FACTOR);
        }
    }

    /// <summary>
    /// Apply amplification to processed samples
    /// </summary>
    /// <param name="samples">Samples to amplify</param>
    private void ApplyAmplification(float[] samples)
    {
        for (int i = 0; i < samples.Length; i++)
        {
            if (samples[i] != 0f) // Only amplify non-silenced samples
            {
                float amplified = samples[i] * playbackGain;
                samples[i] = Mathf.Clamp(amplified, -1f, 1f);
            }
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

    /// <summary>
    /// Set the minimum volume threshold for noise filtering
    /// </summary>
    /// <param name="value">Threshold value (0.0 to 1.0)</param>
    public static void SetMinimumValue(float value)
    {
        minimumValue = Mathf.Clamp(value, 0f, 1f);
        UnityEngine.Debug.Log($"Noise filter minimum value set to: {minimumValue:F4}");
    }

    /// <summary>
    /// Set noise reduction strength
    /// </summary>
    /// <param name="strength">Strength value (0.0 to 1.0)</param>
    public static void SetNoiseReductionStrength(float strength)
    {
        noiseReductionStrength = Mathf.Clamp(strength, 0f, 1f);
        UnityEngine.Debug.Log($"Noise reduction strength set to: {noiseReductionStrength:F2}");
    }

    /// <summary>
    /// Set speech detection threshold
    /// </summary>
    /// <param name="threshold">Threshold value (0.0 to 1.0)</param>
    public static void SetSpeechThreshold(float threshold)
    {
        speechThreshold = Mathf.Clamp(threshold, 0f, 1f);
        UnityEngine.Debug.Log($"Speech threshold set to: {speechThreshold:F4}");
    }

    /// <summary>
    /// Get current minimum value threshold
    /// </summary>
    /// <returns>Current threshold value</returns>
    public static float GetMinimumValue()
    {
        return minimumValue;
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