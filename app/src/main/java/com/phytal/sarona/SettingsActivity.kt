package com.phytal.sarona

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phytal.sarona.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
                //TODO: fix, get right view container
            .replace(R.id.nav_host_fragment,
                SettingsFragment()
            )
            .commit()
    }
}
