package com.persiancodingkeyboard.manager

import android.view.inputmethod.InputConnection
import com.persiancodingkeyboard.data.SettingsRepository
import com.persiancodingkeyboard.util.Constants

class AutoPairManager(context: android.content.Context) {
    private val settings = SettingsRepository(context)

    fun handleKey(key: String, inputConnection: InputConnection?): Boolean {
        if (!settings.autoPair || inputConnection == null) return false

        val closing = Constants.AUTO_PAIR_MAP[key.firstOrNull()]
        if (closing != null) {
            inputConnection.commitText(key + closing, 1)
            // Move cursor back between the pair
            inputConnection.setSelection(
                inputConnection.getTextBeforeCursor(100, 0)?.length ?: 0,
                inputConnection.getTextBeforeCursor(100, 0)?.length ?: 0
            )
            return true
        }
        return false
    }

    fun shouldAutoPair(char: Char): Boolean {
        return settings.autoPair && Constants.AUTO_PAIR_MAP.containsKey(char)
    }
}
