package com.phytal.sarona.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phytal.sarona.R
import com.phytal.sarona.ui.fragments.SettingsFragment


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()
        //replaceFragment(SettingsFragment(), R.id.fragment_container)
    }
}
