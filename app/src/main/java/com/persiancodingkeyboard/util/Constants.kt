package com.persiancodingkeyboard.util

object Constants {
    const val PREFS_NAME = "persian_coding_keyboard_prefs"

    // Settings keys
    const val KEY_THEME = "theme"
    const val KEY_SOUND_ENABLED = "sound_enabled"
    const val KEY_SOUND_VOLUME = "sound_volume"
    const val KEY_VIBRATION_ENABLED = "vibration_enabled"
    const val KEY_VIBRATION_STRENGTH = "vibration_strength"
    const val KEY_AUTO_PAIR = "auto_pair"
    const val KEY_SMART_INDENT = "smart_indent"
    const val KEY_TOOLBAR_ENABLED = "toolbar_enabled"
    const val KEY_KEYBOARD_HEIGHT = "keyboard_height"
    const val KEY_KEY_SIZE = "key_size"
    const val KEY_FONT_SIZE = "font_size"
    const val KEY_CLIPBOARD_ENABLED = "clipboard_enabled"

    // Default values
    const val DEFAULT_SOUND_VOLUME = 50
    const val DEFAULT_VIBRATION_STRENGTH = 30
    const val DEFAULT_KEYBOARD_HEIGHT = 280
    const val DEFAULT_KEY_SIZE = 48
    const val DEFAULT_FONT_SIZE = 18

    // Themes
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_BLUE = "blue"
    const val THEME_GREEN = "green"
    const val THEME_PURPLE = "purple"
    const val THEME_CYBERPUNK = "cyberpunk"
    const val THEME_HACKER = "hacker"

    // Layout types
    const val LAYOUT_PERSIAN = "persian"
    const val LAYOUT_ENGLISH = "english"
    const val LAYOUT_CODE = "code"
    const val LAYOUT_EMOJI = "emoji"
    const val LAYOUT_CLIPBOARD = "clipboard"

    // Auto-pair characters
    val AUTO_PAIR_MAP = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '"' to '"',
        "'" to "'",
        '`' to '`'
    )

    // Smart indent keywords
    val SMART_INDENT_KEYWORDS = setOf(
        "def", "class", "if", "elif", "else", "for", "while",
        "try", "except", "finally", "with"
    )

    // Python toolbar shortcuts
    val PYTHON_SHORTCUTS = listOf(
        "def", "class", "import", "from", "if", "elif", "else",
        "for", "while", "try", "except", "finally", "return",
        "print()", "len()", "range()", "list()", "dict()",
        "set()", "tuple()", "with", "as", "lambda", "pass",
        "break", "continue"
    )

    // Persian layout
    val PERSIAN_KEYS = listOf(
        listOf("ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح"),
        listOf("ج", "چ", "پ", "ش", "س", "ی", "ب", "ل", "ا", "ت"),
        listOf("ن", "م", "ک", "گ", "ظ", "ط", "ز", "ر", "ذ", "د"),
        listOf("ئ", "و", "ژ", "آ", "؟", "،", "؛", "٪", ".", "ـ")
    )

    // English layout
    val ENGLISH_KEYS = listOf(
        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
        listOf("z", "x", "c", "v", "b", "n", "m")
    )

    val ENGLISH_KEYS_SHIFT = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    // Code symbols layout
    val CODE_KEYS = listOf(
        listOf("{", "}", "[", "]", "(", ")", "<", ">"),
        listOf(";", ":", "'", "\"", "`", "/", "\\", "|"),
        listOf("&", "=", "+", "-", "*", "%", "!", "~"),
        listOf("^", "@", "#", "$", "_", "?", ".", ",")
    )

    // Emoji layout
    val EMOJI_KEYS = listOf(
        listOf("😀", "😂", "🥰", "😎", "🤔", "👍", "👎", "🔥", "💯", "✅"),
        listOf("❌", "⭐", "🚀", "💻", "🐍", "⚡", "🎯", "🎉", "🔧", "🔑"),
        listOf("❤️", "💙", "💚", "💛", "💜", "🖤", "🤍", "🤎", "💖", "💗"),
        listOf("👋", "🙏", "✌️", "👌", "🤝", "💪", "🧠", "👀", "👂", "🗣️")
    )

    // Special keys
    const val KEY_BACKSPACE = "BACKSPACE"
    const val KEY_ENTER = "ENTER"
    const val KEY_TAB = "TAB"
    const val KEY_SPACE = "SPACE"
    const val KEY_SHIFT = "SHIFT"
    const val KEY_LANGUAGE = "LANGUAGE"
    const val KEY_SETTINGS = "SETTINGS"
    const val KEY_CLIPBOARD = "CLIPBOARD"
    const val KEY_EMOJI = "EMOJI"
    const val KEY_CODE = "CODE"
    const val KEY_ESC = "ESC"
    const val KEY_HOME = "HOME"
    const val KEY_END = "END"
    const val KEY_ARROW_LEFT = "ARROW_LEFT"
    const val KEY_ARROW_RIGHT = "ARROW_RIGHT"
    const val KEY_ARROW_UP = "ARROW_UP"
    const val KEY_ARROW_DOWN = "ARROW_DOWN"
    const val KEY_UNDO = "UNDO"
    const val KEY_REDO = "REDO"
}
