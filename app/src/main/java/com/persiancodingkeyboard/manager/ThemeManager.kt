package com.persiancodingkeyboard.manager

import android.content.Context
import android.graphics.Color
import com.persiancodingkeyboard.data.SettingsRepository
import com.persiancodingkeyboard.util.Constants

data class ThemeColors(
    val backgroundColor: Int,
    val keyBackgroundColor: Int,
    val keyTextColor: Int,
    val keyPressedColor: Int,
    val specialKeyColor: Int,
    val specialKeyTextColor: Int,
    val toolbarBackgroundColor: Int,
    val toolbarTextColor: Int,
    val accentColor: Int,
    val displayBackgroundColor: Int,
    val displayTextColor: Int,
    val borderColor: Int
)

class ThemeManager(context: Context) {
    private val settings = SettingsRepository(context)

    val currentTheme: ThemeColors
        get() = when (settings.theme) {
            Constants.THEME_LIGHT -> lightTheme
            Constants.THEME_BLUE -> blueTheme
            Constants.THEME_GREEN -> greenTheme
            Constants.THEME_PURPLE -> purpleTheme
            Constants.THEME_CYBERPUNK -> cyberpunkTheme
            Constants.THEME_HACKER -> hackerTheme
            else -> darkTheme
        }

    private val darkTheme = ThemeColors(
        backgroundColor = Color.parseColor("#1a1a2e"),
        keyBackgroundColor = Color.parseColor("#16213e"),
        keyTextColor = Color.parseColor("#eaeaea"),
        keyPressedColor = Color.parseColor("#0f3460"),
        specialKeyColor = Color.parseColor("#e94560"),
        specialKeyTextColor = Color.parseColor("#ffffff"),
        toolbarBackgroundColor = Color.parseColor("#16213e"),
        toolbarTextColor = Color.parseColor("#e94560"),
        accentColor = Color.parseColor("#e94560"),
        displayBackgroundColor = Color.parseColor("#0f3460"),
        displayTextColor = Color.parseColor("#eaeaea"),
        borderColor = Color.parseColor("#533483")
    )

    private val lightTheme = ThemeColors(
        backgroundColor = Color.parseColor("#f5f5f5"),
        keyBackgroundColor = Color.parseColor("#ffffff"),
        keyTextColor = Color.parseColor("#333333"),
        keyPressedColor = Color.parseColor("#e0e0e0"),
        specialKeyColor = Color.parseColor("#0066cc"),
        specialKeyTextColor = Color.parseColor("#ffffff"),
        toolbarBackgroundColor = Color.parseColor("#ffffff"),
        toolbarTextColor = Color.parseColor("#0066cc"),
        accentColor = Color.parseColor("#0066cc"),
        displayBackgroundColor = Color.parseColor("#ffffff"),
        displayTextColor = Color.parseColor("#333333"),
        borderColor = Color.parseColor("#cccccc")
    )

    private val blueTheme = ThemeColors(
        backgroundColor = Color.parseColor("#0a192f"),
        keyBackgroundColor = Color.parseColor("#112240"),
        keyTextColor = Color.parseColor("#ccd6f6"),
        keyPressedColor = Color.parseColor("#233554"),
        specialKeyColor = Color.parseColor("#64ffda"),
        specialKeyTextColor = Color.parseColor("#0a192f"),
        toolbarBackgroundColor = Color.parseColor("#112240"),
        toolbarTextColor = Color.parseColor("#64ffda"),
        accentColor = Color.parseColor("#64ffda"),
        displayBackgroundColor = Color.parseColor("#112240"),
        displayTextColor = Color.parseColor("#ccd6f6"),
        borderColor = Color.parseColor("#233554")
    )

    private val greenTheme = ThemeColors(
        backgroundColor = Color.parseColor("#0d1f0d"),
        keyBackgroundColor = Color.parseColor("#1a3a1a"),
        keyTextColor = Color.parseColor("#c8e6c9"),
        keyPressedColor = Color.parseColor("#2e5c2e"),
        specialKeyColor = Color.parseColor("#4caf50"),
        specialKeyTextColor = Color.parseColor("#ffffff"),
        toolbarBackgroundColor = Color.parseColor("#1a3a1a"),
        toolbarTextColor = Color.parseColor("#4caf50"),
        accentColor = Color.parseColor("#4caf50"),
        displayBackgroundColor = Color.parseColor("#1a3a1a"),
        displayTextColor = Color.parseColor("#c8e6c9"),
        borderColor = Color.parseColor("#2e5c2e")
    )

    private val purpleTheme = ThemeColors(
        backgroundColor = Color.parseColor("#1a0b2e"),
        keyBackgroundColor = Color.parseColor("#2d1b4e"),
        keyTextColor = Color.parseColor("#e1bee7"),
        keyPressedColor = Color.parseColor("#4a148c"),
        specialKeyColor = Color.parseColor("#ce93d8"),
        specialKeyTextColor = Color.parseColor("#1a0b2e"),
        toolbarBackgroundColor = Color.parseColor("#2d1b4e"),
        toolbarTextColor = Color.parseColor("#ce93d8"),
        accentColor = Color.parseColor("#ce93d8"),
        displayBackgroundColor = Color.parseColor("#2d1b4e"),
        displayTextColor = Color.parseColor("#e1bee7"),
        borderColor = Color.parseColor("#4a148c")
    )

    private val cyberpunkTheme = ThemeColors(
        backgroundColor = Color.parseColor("#0a0a0a"),
        keyBackgroundColor = Color.parseColor("#1a1a1a"),
        keyTextColor = Color.parseColor("#00ff9f"),
        keyPressedColor = Color.parseColor("#2a2a2a"),
        specialKeyColor = Color.parseColor("#ff00ff"),
        specialKeyTextColor = Color.parseColor("#ffffff"),
        toolbarBackgroundColor = Color.parseColor("#1a1a1a"),
        toolbarTextColor = Color.parseColor("#ff00ff"),
        accentColor = Color.parseColor("#ff00ff"),
        displayBackgroundColor = Color.parseColor("#0a0a0a"),
        displayTextColor = Color.parseColor("#00ff9f"),
        borderColor = Color.parseColor("#ff00ff")
    )

    private val hackerTheme = ThemeColors(
        backgroundColor = Color.parseColor("#000000"),
        keyBackgroundColor = Color.parseColor("#0a0a0a"),
        keyTextColor = Color.parseColor("#00ff00"),
        keyPressedColor = Color.parseColor("#1a1a1a"),
        specialKeyColor = Color.parseColor("#003300"),
        specialKeyTextColor = Color.parseColor("#00ff00"),
        toolbarBackgroundColor = Color.parseColor("#0a0a0a"),
        toolbarTextColor = Color.parseColor("#00ff00"),
        accentColor = Color.parseColor("#00ff00"),
        displayBackgroundColor = Color.parseColor("#000000"),
        displayTextColor = Color.parseColor("#00ff00"),
        borderColor = Color.parseColor("#003300")
    )

    fun setTheme(themeName: String) {
        settings.theme = themeName
    }

    fun getThemeList(): List<Pair<String, String>> = listOf(
        Constants.THEME_DARK to "Dark",
        Constants.THEME_LIGHT to "Light",
        Constants.THEME_BLUE to "Blue",
        Constants.THEME_GREEN to "Green",
        Constants.THEME_PURPLE to "Purple",
        Constants.THEME_CYBERPUNK to "Cyberpunk",
        Constants.THEME_HACKER to "Hacker"
    )
}
