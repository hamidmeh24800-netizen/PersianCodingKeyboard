package com.persiancodingkeyboard.manager

import android.view.inputmethod.InputConnection
import com.persiancodingkeyboard.data.SettingsRepository
import com.persiancodingkeyboard.util.Constants

class SmartIndentManager(context: android.content.Context) {
    private val settings = SettingsRepository(context)

    fun handleEnter(inputConnection: InputConnection?): String? {
        if (!settings.smartIndent || inputConnection == null) return null

        val textBefore = inputConnection.getTextBeforeCursor(50, 0)?.toString() ?: return null
        val lines = textBefore.split("\n")
        val currentLine = lines.lastOrNull()?.trim() ?: return null

        // Check if current line ends with a smart indent keyword
        val keyword = Constants.SMART_INDENT_KEYWORDS.find { keyword ->
            currentLine == keyword || currentLine.endsWith(" $keyword") || 
            currentLine.endsWith(":$keyword") || currentLine.endsWith("($keyword")
        }

        return if (keyword != null && (currentLine.endsWith(":") || 
                   Constants.SMART_INDENT_KEYWORDS.any { currentLine == it || currentLine.endsWith(" $it") })) {
            "\n    "
        } else if (currentLine.startsWith("    ") || currentLine.startsWith("\t")) {
            // Maintain current indentation
            val indent = currentLine.takeWhile { it == ' ' || it == '\t' }
            "\n$indent"
        } else {
            null
        }
    }

    fun getIndentForLine(line: String): String {
        val trimmed = line.trim()
        if (Constants.SMART_INDENT_KEYWORDS.any { trimmed == it || trimmed.endsWith(" $it") || trimmed.endsWith(":") }) {
            return "    "
        }
        return ""
    }
}
