package com.persiancodingkeyboard.manager

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class ClipboardManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("clipboard", Context.MODE_PRIVATE)
    private val maxItems = 20

    data class ClipboardItem(
        val text: String,
        val timestamp: Long,
        val isPinned: Boolean = false
    )

    fun addItem(text: String) {
        if (text.isBlank()) return
        val items = getItems().toMutableList()

        // Remove duplicate
        items.removeAll { it.text == text }

        // Add new item at top
        items.add(0, ClipboardItem(text, System.currentTimeMillis()))

        // Keep only max items (pinned items don't count)
        val pinned = items.filter { it.isPinned }
        val unpinned = items.filter { !it.isPinned }.take(maxItems - pinned.size)

        saveItems(pinned + unpinned)
    }

    fun getItems(): List<ClipboardItem> {
        val json = prefs.getString("items", "[]") ?: "[]"
        val array = JSONArray(json)
        val items = mutableListOf<ClipboardItem>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            items.add(ClipboardItem(
                text = obj.getString("text"),
                timestamp = obj.getLong("timestamp"),
                isPinned = obj.optBoolean("pinned", false)
            ))
        }
        return items
    }

    fun pinItem(index: Int) {
        val items = getItems().toMutableList()
        if (index in items.indices) {
            items[index] = items[index].copy(isPinned = !items[index].isPinned)
            saveItems(items)
        }
    }

    fun deleteItem(index: Int) {
        val items = getItems().toMutableList()
        if (index in items.indices) {
            items.removeAt(index)
            saveItems(items)
        }
    }

    fun clear() {
        prefs.edit().remove("items").apply()
    }

    private fun saveItems(items: List<ClipboardItem>) {
        val array = JSONArray()
        items.forEach { item ->
            val obj = JSONObject()
            obj.put("text", item.text)
            obj.put("timestamp", item.timestamp)
            obj.put("pinned", item.isPinned)
            array.put(obj)
        }
        prefs.edit().putString("items", array.toString()).apply()
    }
}
