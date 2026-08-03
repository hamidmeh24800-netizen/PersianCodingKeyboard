package com.persiancodingkeyboard.ui

import android.os.Bundle
import androidx.preference.*
import com.persiancodingkeyboard.R
import com.persiancodingkeyboard.data.SettingsRepository
import com.persiancodingkeyboard.util.Constants

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var settingsRepository: SettingsRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        preferenceManager.sharedPreferencesName = Constants.PREFS_NAME
        setPreferencesFromResource(R.xml.preferences, rootKey)
        settingsRepository = SettingsRepository(requireContext())

        setupThemePreference()
        setupSoundPreferences()
        setupVibrationPreferences()
        setupClipboardPreference()
        setupAppearancePreferences()
        setupResetPreference()
    }

    private fun setupThemePreference() {
        findPreference<ListPreference>(Constants.KEY_THEME)?.apply {
            entryValues = arrayOf(
                Constants.THEME_DARK,
                Constants.THEME_LIGHT,
                Constants.THEME_BLUE,
                Constants.THEME_GREEN,
                Constants.THEME_PURPLE,
                Constants.THEME_CYBERPUNK,
                Constants.THEME_HACKER
            )
            entries = arrayOf("Dark", "Light", "Blue", "Green", "Purple", "Cyberpunk", "Hacker")
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.theme = newValue as String
                true
            }
        }
    }

    private fun setupSoundPreferences() {
        findPreference<SwitchPreferenceCompat>(Constants.KEY_SOUND_ENABLED)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.soundEnabled = newValue as Boolean
                true
            }
        }

        findPreference<SeekBarPreference>(Constants.KEY_SOUND_VOLUME)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.soundVolume = newValue as Int
                true
            }
        }
    }

    private fun setupVibrationPreferences() {
        findPreference<SwitchPreferenceCompat>(Constants.KEY_VIBRATION_ENABLED)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.vibrationEnabled = newValue as Boolean
                true
            }
        }

        findPreference<SeekBarPreference>(Constants.KEY_VIBRATION_STRENGTH)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.vibrationStrength = newValue as Int
                true
            }
        }
    }

    private fun setupClipboardPreference() {
        findPreference<SwitchPreferenceCompat>(Constants.KEY_CLIPBOARD_ENABLED)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.clipboardEnabled = newValue as Boolean
                true
            }
        }

        findPreference<Preference>("clear_clipboard")?.apply {
            setOnPreferenceClickListener {
                val clipboardManager = com.persiancodingkeyboard.manager.ClipboardManager(requireContext())
                clipboardManager.clear()
                true
            }
        }
    }


    private fun setupAppearancePreferences() {
        findPreference<SeekBarPreference>(Constants.KEY_KEYBOARD_HEIGHT)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.keyboardHeight = newValue as Int
                true
            }
        }
        findPreference<SeekBarPreference>(Constants.KEY_KEY_SIZE)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.keySize = newValue as Int
                true
            }
        }
        findPreference<SeekBarPreference>(Constants.KEY_FONT_SIZE)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                settingsRepository.fontSize = newValue as Int
                true
            }
        }
    }

    private fun setupResetPreference() {
        findPreference<Preference>("reset_defaults")?.apply {
            setOnPreferenceClickListener {
                settingsRepository.resetToDefaults()
                activity?.recreate()
                true
            }
        }
    }
}
