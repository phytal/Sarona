package com.phytal.sarona

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.phytal.sarona.internal.helpers.ThemeHelper
import com.phytal.sarona.internal.helpers.ThemeHelper.applyTheme


class SaronaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val themePref =
            sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE)
        applyTheme(themePref!!)
    }
}