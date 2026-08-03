package com.persiancodingkeyboard.service

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.SystemClock
import com.persiancodingkeyboard.data.SettingsRepository
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.setPadding
import com.persiancodingkeyboard.manager.*
import com.persiancodingkeyboard.ui.SettingsActivity
import com.persiancodingkeyboard.util.Constants
import com.persiancodingkeyboard.view.KeyboardView

class PersianCodingKeyboardService : InputMethodService(), KeyboardView.KeyboardListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var rootView: View
    private lateinit var toolbarContainer: LinearLayout
    private lateinit var clipboardContainer: ScrollView

    private lateinit var themeManager: ThemeManager
    private lateinit var soundManager: SoundManager
    private lateinit var vibrationManager: VibrationManager
    private lateinit var autoPairManager: AutoPairManager
    private lateinit var smartIndentManager: SmartIndentManager
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var pythonToolbarManager: PythonToolbarManager
    private lateinit var settingsRepository: SettingsRepository

    private var currentLayout = Constants.LAYOUT_PERSIAN
    private var isShifted = false
    private var isCapsLock = false
    private var isClipboardVisible = false
    private var lastShiftTime = 0L

    override fun onCreate() {
        super.onCreate()
        themeManager = ThemeManager(this)
        soundManager = SoundManager(this)
        vibrationManager = VibrationManager(this)
        autoPairManager = AutoPairManager(this)
        smartIndentManager = SmartIndentManager(this)
        clipboardManager = ClipboardManager(this)
        pythonToolbarManager = PythonToolbarManager()
        settingsRepository = SettingsRepository(this)
    }

    override fun onCreateInputView(): View {
        rootView = createKeyboardLayout()
        return rootView
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        updateTheme()
        if (settingsRepository.toolbarEnabled) {
            showPythonToolbar()
        } else {
            hidePythonToolbar()
        }
    }

    private fun createKeyboardLayout(): View {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Python Toolbar
        toolbarContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            visibility = if (settingsRepository.toolbarEnabled) View.VISIBLE else View.GONE
        }

        val toolbarScroll = HorizontalScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            isHorizontalScrollBarEnabled = false
            addView(toolbarContainer)
        }
        container.addView(toolbarScroll)

        // Clipboard view
        clipboardContainer = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                400
            )
            visibility = View.GONE
        }
        container.addView(clipboardContainer)

        // Keyboard view
        keyboardView = KeyboardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                settingsRepository.keyboardHeight
            )
            listener = this@PersianCodingKeyboardService
        }
        container.addView(keyboardView)

        buildPythonToolbar()
        return container
    }

    private fun buildPythonToolbar() {
        toolbarContainer.removeAllViews()
        val theme = themeManager.currentTheme

        pythonToolbarManager.shortcuts.forEach { shortcut ->
            val textView = TextView(this).apply {
                text = shortcut
                setTextColor(theme.toolbarTextColor)
                setBackgroundColor(theme.toolbarBackgroundColor)
                textSize = 12f
                setPadding(16, 12, 16, 12)
                setOnClickListener {
                    playFeedback()
                    val formatted = pythonToolbarManager.getFormattedShortcut(shortcut)
                    currentInputConnection?.commitText(formatted, 1)
                    if (pythonToolbarManager.isIndentTrigger(shortcut)) {
                        currentInputConnection?.commitText("    ", 1)
                    }
                }
            }
            toolbarContainer.addView(textView)
        }
    }

    private fun showPythonToolbar() {
        toolbarContainer.parent?.let { (it as? View)?.visibility = View.VISIBLE }
        buildPythonToolbar()
    }

    private fun hidePythonToolbar() {
        toolbarContainer.parent?.let { (it as? View)?.visibility = View.GONE }
    }

    private fun updateTheme() {
        val theme = themeManager.currentTheme
        rootView.setBackgroundColor(theme.backgroundColor)
        buildPythonToolbar()
        keyboardView.invalidate()
    }

    override fun onKeyPress(key: String) {
        playFeedback()
        val inputConnection = currentInputConnection ?: return

        when (key) {
            Constants.KEY_BACKSPACE -> handleBackspace(inputConnection)
            Constants.KEY_ENTER -> handleEnter(inputConnection)
            Constants.KEY_TAB -> handleTab(inputConnection)
            Constants.KEY_SPACE -> handleSpace(inputConnection)
            Constants.KEY_SHIFT -> handleShift()
            Constants.KEY_LANGUAGE -> handleLanguageSwitch()
            Constants.KEY_SETTINGS -> handleSettings()
            Constants.KEY_CLIPBOARD -> handleClipboard()
            Constants.KEY_EMOJI -> handleEmoji()
            Constants.KEY_CODE -> handleCode()
            Constants.KEY_ESC -> handleEsc(inputConnection)
            Constants.KEY_HOME -> handleHome(inputConnection)
            Constants.KEY_END -> handleEnd(inputConnection)
            Constants.KEY_ARROW_LEFT -> handleArrowLeft(inputConnection)
            Constants.KEY_ARROW_RIGHT -> handleArrowRight(inputConnection)
            Constants.KEY_ARROW_UP -> handleArrowUp(inputConnection)
            Constants.KEY_ARROW_DOWN -> handleArrowDown(inputConnection)
            Constants.KEY_UNDO -> handleUndo(inputConnection)
            Constants.KEY_REDO -> handleRedo(inputConnection)
            else -> handleCharacter(key, inputConnection)
        }
    }

    override fun onKeyLongPress(key: String) {
        when (key) {
            Constants.KEY_BACKSPACE -> {
                // Delete word
                val ic = currentInputConnection ?: return
                val textBefore = ic.getTextBeforeCursor(50, 0)?.toString() ?: return
                val lastWord = textBefore.split(Regex("\\s+")).lastOrNull() ?: return
                for (i in lastWord.indices) {
                    ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                    ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
                }
            }
        }
    }

    private fun handleCharacter(char: String, inputConnection: InputConnection) {
        if (char.length == 1 && autoPairManager.shouldAutoPair(char[0])) {
            autoPairManager.handleKey(char, inputConnection)
        } else {
            inputConnection.commitText(char, 1)
        }

        // Auto unshift after typing if not caps lock
        if (isShifted && !isCapsLock && currentLayout == Constants.LAYOUT_ENGLISH) {
            isShifted = false
            keyboardView.setShift(false)
        }
    }

    private fun handleBackspace(inputConnection: InputConnection) {
        inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
        inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
    }

    private fun handleEnter(inputConnection: InputConnection) {
        val smartIndent = smartIndentManager.handleEnter(inputConnection)
        if (smartIndent != null) {
            inputConnection.commitText(smartIndent, 1)
        } else {
            inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun handleTab(inputConnection: InputConnection) {
        inputConnection.commitText("    ", 1)
    }

    private fun handleSpace(inputConnection: InputConnection) {
        inputConnection.commitText(" ", 1)
    }

    private fun handleShift() {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastShiftTime < 300) {
            // Double tap = Caps Lock
            isCapsLock = !isCapsLock
            isShifted = isCapsLock
        } else {
            isShifted = !isShifted
            if (!isShifted) isCapsLock = false
        }
        lastShiftTime = currentTime
        keyboardView.setShift(isShifted, isCapsLock)
    }

    private fun handleLanguageSwitch() {
        currentLayout = when (currentLayout) {
            Constants.LAYOUT_PERSIAN -> Constants.LAYOUT_ENGLISH
            Constants.LAYOUT_ENGLISH -> Constants.LAYOUT_PERSIAN
            else -> Constants.LAYOUT_PERSIAN
        }
        keyboardView.updateLayout(currentLayout)
    }

    private fun handleSettings() {
        val intent = Intent(this, SettingsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun handleClipboard() {
        if (isClipboardVisible) {
            clipboardContainer.visibility = View.GONE
            isClipboardVisible = false
        } else {
            showClipboardView()
            clipboardContainer.visibility = View.VISIBLE
            isClipboardVisible = true
        }
    }

    private fun showClipboardView() {
        clipboardContainer.removeAllViews()
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val items = clipboardManager.getItems()
        items.forEachIndexed { index, item ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val textView = TextView(this).apply {
                text = item.text.take(50) + if (item.text.length > 50) "..." else ""
                textSize = 14f
                setPadding(16, 12, 16, 12)
                setOnClickListener {
                    currentInputConnection?.commitText(item.text, 1)
                    clipboardContainer.visibility = View.GONE
                    isClipboardVisible = false
                }
            }
            row.addView(textView)

            val pinBtn = TextView(this).apply {
                text = if (item.isPinned) "📌" else "📍"
                textSize = 18f
                setPadding(16, 12, 16, 12)
                setOnClickListener {
                    clipboardManager.pinItem(index)
                    showClipboardView()
                }
            }
            row.addView(pinBtn)

            val delBtn = TextView(this).apply {
                text = "🗑️"
                textSize = 18f
                setPadding(16, 12, 16, 12)
                setOnClickListener {
                    clipboardManager.deleteItem(index)
                    showClipboardView()
                }
            }
            row.addView(delBtn)

            container.addView(row)
        }

        clipboardContainer.addView(container)
    }

    private fun handleEmoji() {
        currentLayout = if (currentLayout == Constants.LAYOUT_EMOJI) {
            Constants.LAYOUT_PERSIAN
        } else {
            Constants.LAYOUT_EMOJI
        }
        keyboardView.updateLayout(currentLayout)
    }

    private fun handleCode() {
        currentLayout = if (currentLayout == Constants.LAYOUT_CODE) {
            Constants.LAYOUT_PERSIAN
        } else {
            Constants.LAYOUT_CODE
        }
        keyboardView.updateLayout(currentLayout)
    }

    private fun handleEsc(inputConnection: InputConnection) {
        // Send ESC key
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            inputConnection.performEditorAction(android.view.inputmethod.EditorInfo.IME_ACTION_NONE)
        }
    }

    private fun handleHome(inputConnection: InputConnection) {
        inputConnection.setSelection(0, 0)
    }

    private fun handleEnd(inputConnection: InputConnection) {
        val text = inputConnection.getTextAfterCursor(1000, 0)
        val length = text?.length ?: 0
        val currentPos = inputConnection.getTextBeforeCursor(1000, 0)?.length ?: 0
        inputConnection.setSelection(currentPos + length, currentPos + length)
    }

    private fun handleArrowLeft(inputConnection: InputConnection) {
        val currentPosition = inputConnection.getTextBeforeCursor(1000, 0)?.length ?: 0
        if (currentPosition > 0) {
            inputConnection.setSelection(currentPosition - 1, currentPosition - 1)
        }
    }

    private fun handleArrowRight(inputConnection: InputConnection) {
        val currentPosition = inputConnection.getTextBeforeCursor(1000, 0)?.length ?: 0
        inputConnection.setSelection(currentPosition + 1, currentPosition + 1)
    }

    private fun handleArrowUp(inputConnection: InputConnection) {
        // Move cursor to previous line
        val textBefore = inputConnection.getTextBeforeCursor(1000, 0)?.toString() ?: return
        val lastNewline = textBefore.lastIndexOf('\n')
        if (lastNewline >= 0) {
            val prevNewline = textBefore.lastIndexOf('\n', lastNewline - 1)
            val targetPos = if (prevNewline >= 0) prevNewline + 1 else 0
            inputConnection.setSelection(targetPos, targetPos)
        }
    }

    private fun handleArrowDown(inputConnection: InputConnection) {
        // Move cursor to next line
        val textAfter = inputConnection.getTextAfterCursor(1000, 0)?.toString() ?: return
        val nextNewline = textAfter.indexOf('\n')
        if (nextNewline >= 0) {
            val currentPos = inputConnection.getTextBeforeCursor(1000, 0)?.length ?: 0
            inputConnection.setSelection(currentPos + nextNewline + 1, currentPos + nextNewline + 1)
        }
    }

    private fun handleUndo(inputConnection: InputConnection) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val eventTime = SystemClock.uptimeMillis()
            inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, KeyEvent.META_CTRL_ON))
            inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, KeyEvent.META_CTRL_ON))
        }
    }

    private fun handleRedo(inputConnection: InputConnection) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val eventTime = SystemClock.uptimeMillis()
            inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, KeyEvent.META_CTRL_ON or KeyEvent.META_SHIFT_ON))
            inputConnection.sendKeyEvent(KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, KeyEvent.META_CTRL_ON or KeyEvent.META_SHIFT_ON))
        }
    }

    override fun onSwipeLeft() {
        // Switch to previous layout
        handleLanguageSwitch()
    }

    override fun onSwipeRight() {
        // Switch to next layout
        handleLanguageSwitch()
    }

    override fun onSwipeUp() {
        // Show code layout
        handleCode()
    }

    override fun onSwipeDown() {
        // Hide keyboard
        requestHideSelf(0)
    }

    private fun playFeedback() {
        soundManager.playClickSound()
        vibrationManager.vibrate()
    }

    override fun onUpdateSelection(oldSelStart: Int, oldSelEnd: Int, newSelStart: Int, newSelEnd: Int, candidatesStart: Int, candidatesEnd: Int) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)
        // Track clipboard
        if (oldSelStart != oldSelEnd) {
            val selectedText = currentInputConnection?.getSelectedText(0)?.toString()
            if (!selectedText.isNullOrBlank()) {
                clipboardManager.addItem(selectedText)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}
