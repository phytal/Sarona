package com.phytal.sarona

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phytal.sarona.fragments.PreferencesFragment


class ClassesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.fragment_classes)
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.fragment_container,
                PreferencesFragment()
            )
            .commit()
    }
}