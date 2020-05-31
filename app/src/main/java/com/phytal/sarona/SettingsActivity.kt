package com.phytal.sarona

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phytal.sarona.fragments.SettingsFragment
import com.phytal.sarona.helpers.replaceFragment


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()
        //replaceFragment(SettingsFragment(), R.id.fragment_container)
    }
}
