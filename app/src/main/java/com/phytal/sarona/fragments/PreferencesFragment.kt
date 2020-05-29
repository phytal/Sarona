package com.phytal.sarona.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.preference.*
import com.phytal.sarona.R
import com.phytal.sarona.SettingsActivity
import com.phytal.sarona.fragments.PreferencesFragment.Companion.bindPreferenceSummaryToValue
//not working
class PreferencesFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    //private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_main, rootKey)

        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)


        val myPref = findPreference<Preference>("feedback")
        myPref?.setOnPreferenceClickListener {
            val url = "https://forms.gle/heRjNb2tWjfBmVsk8"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
            true
        }

        //TODO: make it work
        val editPref : EditTextPreference = preferenceScreen.findPreference<Preference>("pref_gpaCalc_ol")!! as EditTextPreference
        editPref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                val parsed = editPref.text.toDoubleOrNull() ?: -1.0
                if (parsed >= 0.0) {
                    false
                }
                true
            }

        // listeners for preferences
        PreferencesFragment.bindPreferenceSummaryToValue(findPreference("pref_gpaCalc_ap"))
        PreferencesFragment.bindPreferenceSummaryToValue(findPreference("pref_gpaCalc_pap"))
        PreferencesFragment.bindPreferenceSummaryToValue(findPreference("pref_gpaCalc_ol"))
        PreferencesFragment.bindPreferenceSummaryToValue(findPreference("pref_theme"))
        PreferencesFragment.bindPreferenceSummaryToValue(findPreference("pref_hacLink"))
        PreferencesFragment.bindPreferenceSummaryToValue(findPreference("pref_language"))
        PreferencesFragment.bindPreferenceSummaryToValue(findPreference("pref_gpaCalc_si"))
    }

    companion object {
        val TAG = SettingsActivity::class.java!!.simpleName

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
            Preference.OnPreferenceChangeListener { preference, newValue ->
                val stringValue = newValue.toString()

                if (preference is ListPreference) {
                    // For list preferences, look up the correct display value in
                    // the preference's 'entries' list.
                    val index = preference.findIndexOfValue(stringValue)

                    // Set the summary to reflect the new value.
                    preference.setSummary(
                        if (index >= 0)
                            preference.entries[index]
                        else
                            null
                    )

                } else if (preference is EditTextPreference) {
                    if (preference.getKey() == "pref_gpaCalc_si") {
                        if (TextUtils.isEmpty(stringValue)) {
                            // Empty values correspond to 'default'.
                            preference.setSummary(R.string.settings_summary_default_gpa_calculation)
                        }
                    } else if (preference.getKey() == "pref_gpaCalc_ap") {
                        if (TextUtils.isEmpty(stringValue)) {
                            // Empty values correspond to 'default'.
                            preference.setSummary(R.string.settings_summary_default_ap_value)
                        }
                    } else if (preference.getKey() == "pref_gpaCalc_pap") {
                        if (TextUtils.isEmpty(stringValue)) {
                            // Empty values correspond to 'default'.
                            preference.setSummary(R.string.settings_summary_default_pap_value)
                        }
                    } else if (preference.getKey() == "pref_gpaCalc_ol") {
                        if (TextUtils.isEmpty(stringValue)) {
                            // Empty values correspond to 'default'.
                            preference.setSummary(R.string.settings_summary_default_ol_value)
                        }
                    } else if (TextUtils.isEmpty(stringValue)) {
                        // Empty values correspond to 'default'.
                        preference.setSummary(R.string.settings_default)
                    }

                    // update the changed theme name to summary field
                    preference.setSummary(stringValue)
                } else {
                    preference.summary = stringValue
                }
                true
            }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}