package com.persiancodingkeyboard.manager

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.persiancodingkeyboard.data.SettingsRepository

class VibrationManager(context: Context) {
    private val settings = SettingsRepository(context)
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate() {
        if (!settings.vibrationEnabled) return
        if (!vibrator.hasVibrator()) return

        val strength = settings.vibrationStrength
        val duration = (strength * 0.5).toLong().coerceIn(10, 100)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(
                duration,
                strength.coerceIn(1, 255)
            )
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}
