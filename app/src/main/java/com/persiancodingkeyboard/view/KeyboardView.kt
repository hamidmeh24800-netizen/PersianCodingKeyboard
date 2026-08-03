package com.persiancodingkeyboard.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.persiancodingkeyboard.manager.ThemeManager
import com.persiancodingkeyboard.data.SettingsRepository
import com.persiancodingkeyboard.util.Constants
import kotlin.math.ceil

data class KeyData(
    val label: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val isSpecial: Boolean = false,
    val isSpace: Boolean = false,
    val isEmoji: Boolean = false
)

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    interface KeyboardListener {
        fun onKeyPress(key: String)
        fun onKeyLongPress(key: String)
        fun onSwipeLeft()
        fun onSwipeRight()
        fun onSwipeUp()
        fun onSwipeDown()
    }

    var listener: KeyboardListener? = null
    var currentLayout: String = Constants.LAYOUT_PERSIAN
    var isShifted: Boolean = false
    var isCapsLock: Boolean = false

    private val themeManager = ThemeManager(context)
    private val settingsRepository = SettingsRepository(context)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val keys = mutableListOf<KeyData>()
    private var pressedKey: KeyData? = null
    private var keyHeight = 0f
    private var keyWidth = 0f
    private var padding = 8f
    private var cornerRadius = 8f

    private val handler = Handler(Looper.getMainLooper())
    private var longPressRunnable: Runnable? = null
    private var isLongPress = false

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e1 == null) return false
            val dx = e2.x - e1.x
            val dy = e2.y - e1.y

            if (kotlin.math.abs(dx) > kotlin.math.abs(dy)) {
                if (dx > 100) listener?.onSwipeRight()
                else if (dx < -100) listener?.onSwipeLeft()
            } else {
                if (dy > 100) listener?.onSwipeDown()
                else if (dy < -100) listener?.onSwipeUp()
            }
            return true
        }
    })

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateKeys()
    }

    private fun calculateKeys() {
        keys.clear()
        val width = this.width.toFloat()
        val height = this.height.toFloat()

        if (width <= 0 || height <= 0) return

        val layout = when (currentLayout) {
            Constants.LAYOUT_PERSIAN -> Constants.PERSIAN_NUMERIC_KEYS + Constants.PERSIAN_KEYS
            Constants.LAYOUT_ENGLISH -> Constants.ENGLISH_NUMERIC_KEYS + if (isShifted || isCapsLock) Constants.ENGLISH_KEYS_SHIFT else Constants.ENGLISH_KEYS
            Constants.LAYOUT_CODE -> Constants.CODE_KEYS
            Constants.LAYOUT_EMOJI -> Constants.EMOJI_KEYS
            else -> Constants.PERSIAN_NUMERIC_KEYS + Constants.PERSIAN_KEYS
        }

        val rows = layout.size + 2 // +2 for switch keys row and special keys row
        val availableHeight = height - padding * 2
        keyHeight = (availableHeight / rows).coerceAtLeast(settingsRepository.keySize.toFloat())

        var currentY = padding

        layout.forEach { row ->
            val cols = row.size
            val availableWidth = width - padding * 2
            keyWidth = (availableWidth - (cols - 1) * padding) / cols

            var currentX = padding
            row.forEach { label ->
                keys.add(KeyData(
                    label = label,
                    x = currentX,
                    y = currentY,
                    width = keyWidth,
                    height = keyHeight - padding,
                    isEmoji = currentLayout == Constants.LAYOUT_EMOJI
                ))
                currentX += keyWidth + padding
            }
            currentY += keyHeight
        }

        // Switch keys row
        val switchKeys = listOf(
            Triple(Constants.KEY_SHIFT, 1.0f, true),
            Triple(Constants.KEY_LANGUAGE, 1.0f, true),
            Triple(Constants.KEY_CODE, 1.0f, true),
            Triple(Constants.KEY_EMOJI, 1.0f, true),
            Triple(Constants.KEY_SETTINGS, 1.0f, true)
        )
        val switchTotalWeight = switchKeys.sumOf { it.second.toDouble() }.toFloat()
        val switchAvailableWidth = width - padding * 2 - (switchKeys.size - 1) * padding
        val switchUnitWidth = switchAvailableWidth / switchTotalWeight
        
        var switchX = padding
        switchKeys.forEach { (label, weight, isSpecial) ->
            val w = switchUnitWidth * weight
            keys.add(KeyData(
                label = label,
                x = switchX,
                y = currentY,
                width = w,
                height = keyHeight - padding,
                isSpecial = isSpecial
            ))
            switchX += w + padding
        }
        currentY += keyHeight

        // Special keys row
        val specialKeys = listOf(
            Triple(Constants.KEY_BACKSPACE, 1.5f, true),
            Triple(Constants.KEY_ENTER, 1.5f, true),
            Triple(Constants.KEY_TAB, 1.2f, true),
            Triple(Constants.KEY_SPACE, 3.5f, true)
        )

        val specialTotalWeight = specialKeys.sumOf { it.second.toDouble() }.toFloat()
        val specialAvailableWidth = width - padding * 2 - (specialKeys.size - 1) * padding
        val unitWidth = specialAvailableWidth / specialTotalWeight

        var currentX = padding
        specialKeys.forEach { (label, weight, isSpecial) ->
            val w = unitWidth * weight
            keys.add(KeyData(
                label = label,
                x = currentX,
                y = currentY,
                width = w,
                height = keyHeight - padding,
                isSpecial = isSpecial,
                isSpace = label == Constants.KEY_SPACE
            ))
            currentX += w + padding
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val theme = themeManager.currentTheme

        // Background
        canvas.drawColor(theme.backgroundColor)

        keys.forEach { key ->
            val isPressed = key == pressedKey
            val bgColor = when {
                isPressed -> theme.keyPressedColor
                key.isSpecial -> theme.specialKeyColor
                else -> theme.keyBackgroundColor
            }

            val textColor = when {
                key.isSpecial -> theme.specialKeyTextColor
                else -> theme.keyTextColor
            }

            // Draw key background
            paint.color = bgColor
            val rect = RectF(key.x, key.y, key.x + key.width, key.y + key.height)
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

            // Draw border
            paint.color = theme.borderColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
            paint.style = Paint.Style.FILL

            // Draw text
            textPaint.color = textColor
            textPaint.textSize = if (key.isEmoji) key.height * 0.5f else settingsRepository.fontSize.toFloat()
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.DEFAULT_BOLD

            val textY = key.y + key.height / 2 + textPaint.textSize / 3
            canvas.drawText(key.label, key.x + key.width / 2, textY, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val key = findKeyAt(event.x, event.y)
                if (key != null) {
                    pressedKey = key
                    isLongPress = false
                    invalidate()

                    longPressRunnable = Runnable {
                        isLongPress = true
                        listener?.onKeyLongPress(key.label)
                    }
                    handler.postDelayed(longPressRunnable!!, 500)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val key = findKeyAt(event.x, event.y)
                if (key != pressedKey) {
                    pressedKey = key
                    invalidate()
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                longPressRunnable?.let { handler.removeCallbacks(it) }

                val key = findKeyAt(event.x, event.y)
                if (key != null && key == pressedKey && !isLongPress) {
                    listener?.onKeyPress(key.label)
                }
                pressedKey = null
                invalidate()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                longPressRunnable?.let { handler.removeCallbacks(it) }
                pressedKey = null
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun findKeyAt(x: Float, y: Float): KeyData? {
        return keys.find { x >= it.x && x <= it.x + it.width && y >= it.y && y <= it.y + it.height }
    }

    fun updateLayout(layout: String) {
        currentLayout = layout
        calculateKeys()
        invalidate()
    }

    fun setShift(shifted: Boolean, capsLock: Boolean = false) {
        isShifted = shifted
        isCapsLock = capsLock
        if (currentLayout == Constants.LAYOUT_ENGLISH) {
            calculateKeys()
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = settingsRepository.keyboardHeight
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }
}
