package com.persiancodingkeyboard.manager

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import com.persiancodingkeyboard.data.SettingsRepository

class SoundManager(context: Context) {
    private val settings = SettingsRepository(context)
    private val soundPool: SoundPool
    private var clickSoundId: Int = 0
    private var isLoaded = false

    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attributes)
            .build()

        // Load sound from raw resource
        try {
            clickSoundId = soundPool.load(context, com.persiancodingkeyboard.R.raw.key_click, 1)
            soundPool.setOnLoadCompleteListener { _, sampleId, status ->
                if (status == 0 && sampleId == clickSoundId) {
                    isLoaded = true
                }
            }
        } catch (e: Exception) {
            // Sound file not available, use system beep
            clickSoundId = 0
        }
    }

    fun playClickSound() {
        if (!settings.soundEnabled) return

        val volume = (settings.soundVolume / 100f).coerceAtMost(1.5f)

        if (clickSoundId != 0) {
            soundPool.play(clickSoundId, volume, volume, 1, 0, 1.0f)
        } else {
            // Fallback to system click
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use AudioManager for system sounds
            }
        }
    }

    fun release() {
        soundPool.release()
    }
}
