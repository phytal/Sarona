package com.phytal.sarona.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import com.phytal.sarona.R
import com.phytal.sarona.internal.helpers.ThemeHelper.applyTheme
import com.phytal.sarona.ui.base.BasePreferenceFragment


class SettingsFragment : SharedPreferences.OnSharedPreferenceChangeListener, BasePreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_main, rootKey)

        Companion.context = this.requireContext()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        onSharedPreferenceChanged(preferenceManager.sharedPreferences, "pref_theme")

        val myPref = findPreference<Preference>("feedback")
        myPref?.setOnPreferenceClickListener {
            val url = "https://forms.gle/heRjNb2tWjfBmVsk8"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
            true
        }

        // listeners for preferences
        bindPreferenceSummaryToValue(findPreference("pref_gpaCalc_ap"))
        bindPreferenceSummaryToValue(findPreference("pref_gpaCalc_pap"))
        bindPreferenceSummaryToValue(findPreference("pref_gpaCalc_ol"))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key : String) {
        if (key == "pref_theme") {
            val newValue = sharedPreferences.getString("pref_theme","")
            val themeOption = newValue as String
            applyTheme(themeOption)
        }
    }

    companion object {
        const val TAG: String = "FRAGMENT_SETTINGS"
        private lateinit var context: Context

        private fun bindPreferenceSummaryToValue(preference: Preference?) {
            preference?.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference?.context)
                    .getString(preference?.key, "")
            )
        }

        private val sBindPreferenceSummaryToValueListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                var valid = true
                val new = newValue.toString()
                val parsed = new.toDoubleOrNull() ?: -1.0
                if (parsed <= 0.0) {
                    Toast.makeText(context, "Error: Invalid input.", Toast.LENGTH_SHORT).show()
                    valid = false
                }
                return@OnPreferenceChangeListener valid
            }
    }
}

