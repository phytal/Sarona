package com.phytal.sarona

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phytal.sarona.fragments.PreferencesFragment
import com.phytal.sarona.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment,
                PreferencesFragment()
            )
            .commit()
    }
}
