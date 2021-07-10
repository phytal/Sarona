package com.phytal.sarona.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.phytal.sarona.R
import com.phytal.sarona.util.SpinnerAdapter

class MenuBottomSheetDialogFragment(
    private val listener: AdapterView.OnItemSelectedListener
) : BottomSheetDialogFragment() {
    private lateinit var languageSpinner: Spinner
    private lateinit var themeSpinner: Spinner
    private var initialSelect = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.settings_dialog,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageSpinner = view.findViewById(R.id.language_spinner)
        themeSpinner = view.findViewById(R.id.theme_spinner)
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.user_preference_file_key),
            Context.MODE_PRIVATE
        )

        val themeAdapter = SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.pref_theme).toList()
        )
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        themeSpinner.adapter = themeAdapter

        // sets selected spinner item from shared preferences
        val themeValue = sharedPref?.getString(getString(R.string.saved_theme_key), "Default")
        for (i in 0 until themeSpinner.count) {
            if (themeValue == themeSpinner.getItemAtPosition(i).toString()) {
                themeSpinner.setSelection(i)
                break
            }
        }

        themeSpinner.onItemSelectedListener = listener

        val languageAdapter = SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.pref_language).toList()
        )
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        languageSpinner.adapter = languageAdapter
        languageSpinner.onItemSelectedListener = listener
    }

    fun dismissFragment() {
        if (!initialSelect)
            initialSelect = true
        else
            dismiss()
    }
}

