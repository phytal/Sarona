package com.phytal.sarona.internal.helpers

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.phytal.sarona.R

object ThemeHelper {
    private const val LIGHT_MODE = R.id.menu_light.toString()
    private const val DARK_MODE = R.id.menu_dark.toString()
    const val DEFAULT_MODE = R.id.menu_system_default.toString()
    fun applyTheme(themePref: String) {
        when (themePref) {
            LIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            DARK_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }
    }
}