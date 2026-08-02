package com.persiancodingkeyboard.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.persiancodingkeyboard.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }

        supportActionBar?.title = getString(R.string.settings_title)
    }
}
