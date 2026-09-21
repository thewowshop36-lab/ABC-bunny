package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class SoundManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private val scope = CoroutineScope(Dispatchers.Default)

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error init TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("SoundManager", "US English not supported in TTS")
            } else {
                isTtsReady = true
                tts?.setPitch(1.3f) // Child-friendly cheerful higher pitch
                tts?.setSpeechRate(0.9f) // Clear, friendly pace for toddlers
            }
        }
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (isTtsReady) {
            tts?.speak(text, queueMode, null, "UTTERANCE_${System.currentTimeMillis()}")
        }
    }

    fun speakLetterPhonics(char: Char, word: String, phonicsSound: String) {
        val letterName = char.uppercaseChar().toString()
        val speech = "$letterName! $phonicsSound!"
        speak(speech)
    }

    fun speakPraise() {
        val praises = listOf(
            "Yay! Super job!",
            "Awesome!",
            "You did it! High five!",
            "Woohoo! Star student!",
            "Terrific tracing!"
        )
        speak(praises.random(), queueMode = TextToSpeech.QUEUE_ADD)
    }

    fun triggerHaptic(durationMs: Long = 40) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (e: Exception) {
            // Haptic might not be present
        }
    }

    // High quality synthetic sound effects generated dynamically via AudioTrack
    fun playPopSound() {
        triggerHaptic(30)
        scope.launch {
            playToneSweep(startFreq = 800f, endFreq = 200f, durationMs = 80)
        }
    }

    fun playStarChime() {
        triggerHaptic(50)
        scope.launch {
            playNote(523.25f, 90) // C5
            playNote(659.25f, 90) // E5
            playNote(783.99f, 90) // G5
            playNote(1046.50f, 180) // C6
        }
    }

    fun playSuccessFanfare() {
        triggerHaptic(80)
        scope.launch {
            playNote(440f, 80)
            playNote(554.37f, 80)
            playNote(659.25f, 80)
            playNote(880f, 220)
        }
    }

    fun playChewSound() {
        triggerHaptic(40)
        scope.launch {
            playToneSweep(startFreq = 350f, endFreq = 180f, durationMs = 100)
            playToneSweep(startFreq = 300f, endFreq = 150f, durationMs = 120)
        }
    }

    fun playSparkle() {
        scope.launch {
            playToneSweep(startFreq = 1200f, endFreq = 2400f, durationMs = 90)
        }
    }

    private fun playNote(frequency: Float, durationMs: Int) {
        val sampleRate = 22050
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val time = i.toDouble() / sampleRate
            val envelope = (1.0 - (i.toDouble() / numSamples)) // Fade out
            val sample = sin(2.0 * Math.PI * frequency * time) * envelope * Short.MAX_VALUE * 0.4
            buffer[i] = sample.toInt().toShort()
        }

        playRawPcm(buffer, sampleRate)
    }

    private fun playToneSweep(startFreq: Float, endFreq: Float, durationMs: Int) {
        val sampleRate = 22050
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val progress = i.toFloat() / numSamples
            val freq = startFreq + (endFreq - startFreq) * progress
            val time = i.toDouble() / sampleRate
            val envelope = (1.0 - progress.toDouble())
            val sample = sin(2.0 * Math.PI * freq * time) * envelope * Short.MAX_VALUE * 0.45
            buffer[i] = sample.toInt().toShort()
        }

        playRawPcm(buffer, sampleRate)
    }

    private fun playRawPcm(buffer: ShortArray, sampleRate: Int) {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(maxOf(buffer.size * 2, minBufferSize))
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            // Release after playing
            scope.launch {
                kotlinx.coroutines.delay(buffer.size * 1000L / sampleRate + 50)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (e: Exception) {
                    // Ignore release
                }
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing pcm", e)
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
