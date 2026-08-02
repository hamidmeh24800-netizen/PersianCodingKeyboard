package com.persiancodingkeyboard.data

import android.content.Context
import android.content.SharedPreferences
import com.persiancodingkeyboard.util.Constants

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREFS_NAME, Context.MODE_PRIVATE
    )

    var theme: String
        get() = prefs.getString(Constants.KEY_THEME, Constants.THEME_DARK) ?: Constants.THEME_DARK
        set(value) = prefs.edit().putString(Constants.KEY_THEME, value).apply()

    var soundEnabled: Boolean
        get() = prefs.getBoolean(Constants.KEY_SOUND_ENABLED, true)
        set(value) = prefs.edit().putBoolean(Constants.KEY_SOUND_ENABLED, value).apply()

    var soundVolume: Int
        get() = prefs.getInt(Constants.KEY_SOUND_VOLUME, Constants.DEFAULT_SOUND_VOLUME)
        set(value) = prefs.edit().putInt(Constants.KEY_SOUND_VOLUME, value).apply()

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(Constants.KEY_VIBRATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(Constants.KEY_VIBRATION_ENABLED, value).apply()

    var vibrationStrength: Int
        get() = prefs.getInt(Constants.KEY_VIBRATION_STRENGTH, Constants.DEFAULT_VIBRATION_STRENGTH)
        set(value) = prefs.edit().putInt(Constants.KEY_VIBRATION_STRENGTH, value).apply()

    var autoPair: Boolean
        get() = prefs.getBoolean(Constants.KEY_AUTO_PAIR, true)
        set(value) = prefs.edit().putBoolean(Constants.KEY_AUTO_PAIR, value).apply()

    var smartIndent: Boolean
        get() = prefs.getBoolean(Constants.KEY_SMART_INDENT, true)
        set(value) = prefs.edit().putBoolean(Constants.KEY_SMART_INDENT, value).apply()

    var toolbarEnabled: Boolean
        get() = prefs.getBoolean(Constants.KEY_TOOLBAR_ENABLED, true)
        set(value) = prefs.edit().putBoolean(Constants.KEY_TOOLBAR_ENABLED, value).apply()

    var keyboardHeight: Int
        get() = prefs.getInt(Constants.KEY_KEYBOARD_HEIGHT, Constants.DEFAULT_KEYBOARD_HEIGHT)
        set(value) = prefs.edit().putInt(Constants.KEY_KEYBOARD_HEIGHT, value).apply()

    var keySize: Int
        get() = prefs.getInt(Constants.KEY_KEY_SIZE, Constants.DEFAULT_KEY_SIZE)
        set(value) = prefs.edit().putInt(Constants.KEY_KEY_SIZE, value).apply()

    var fontSize: Int
        get() = prefs.getInt(Constants.KEY_FONT_SIZE, Constants.DEFAULT_FONT_SIZE)
        set(value) = prefs.edit().putInt(Constants.KEY_FONT_SIZE, value).apply()

    var clipboardEnabled: Boolean
        get() = prefs.getBoolean(Constants.KEY_CLIPBOARD_ENABLED, true)
        set(value) = prefs.edit().putBoolean(Constants.KEY_CLIPBOARD_ENABLED, value).apply()

    fun resetToDefaults() {
        prefs.edit().clear().apply()
    }
}
