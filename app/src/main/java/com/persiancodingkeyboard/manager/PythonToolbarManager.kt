package com.persiancodingkeyboard.manager

import com.persiancodingkeyboard.util.Constants

class PythonToolbarManager {

    val shortcuts: List<String> = Constants.PYTHON_SHORTCUTS

    fun getFormattedShortcut(shortcut: String): String {
        return when {
            shortcut.endsWith("()") -> shortcut
            shortcut in listOf("def", "class", "if", "elif", "else", "for", "while", "try", "except", "finally", "with") -> "$shortcut:"
            else -> shortcut
        }
    }

    fun isIndentTrigger(shortcut: String): Boolean {
        return shortcut in listOf("def", "class", "if", "elif", "else", "for", "while", "try", "except", "finally", "with")
    }
}
