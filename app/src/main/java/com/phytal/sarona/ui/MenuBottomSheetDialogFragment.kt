package com.phytal.sarona.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.phytal.sarona.R
import com.phytal.sarona.databinding.SettingsDialogBinding
import com.phytal.sarona.ui.welcome.LoginFragmentDirections
import com.phytal.sarona.util.SpinnerAdapter

class MenuBottomSheetDialogFragment(
    private val themeListener: AdapterView.OnItemSelectedListener,
    private val languageListener: AdapterView.OnItemSelectedListener
) : BottomSheetDialogFragment() {
    private lateinit var binding: SettingsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.themeSpinner.adapter = themeAdapter

        // sets selected spinner item from shared preferences
        val themeValue = sharedPref?.getString(getString(R.string.saved_theme_key), "Default")
        for (i in 0 until binding.themeSpinner.count) {
            if (themeValue == binding.themeSpinner.getItemAtPosition(i).toString()) {
                binding.themeSpinner.tag = i
                binding.themeSpinner.setSelection(i)
                break
            }
        }

        binding.themeSpinner.onItemSelectedListener = themeListener

        val languageAdapter = SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.pref_language).toList()
        )
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.languageSpinner.adapter = languageAdapter
//      sets selected spinner item from shared preferences
        val langValue = sharedPref?.getString(getString(R.string.saved_language_key), "English")
        for (i in 0 until binding.languageSpinner.count) {
            if (langValue == binding.languageSpinner.getItemAtPosition(i).toString()) {
                binding.languageSpinner.tag = i
                binding.languageSpinner.setSelection(i)
                break
            }
        }

        binding.languageSpinner.onItemSelectedListener = languageListener

        binding.logoutButton.setOnClickListener {
            val loginSharedPref = activity?.getSharedPreferences(
                getString(R.string.login_preference_file_key),
                Context.MODE_PRIVATE
            )

            if (loginSharedPref != null) {
                with(loginSharedPref.edit()) {
                    clear()
                    apply()
                }
            }
            val directions =
                LoginFragmentDirections.actionGlobalLoginFragment()
            findNavController().navigate(directions)
            dismiss()
            // snackbar doesnt work here :(
            Toast.makeText(it.context, "Successfully logged out!", Toast.LENGTH_LONG).show()
        }

        binding.aboutButton.setOnClickListener {
            val url = "https://linktr.ee/simplegrade"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}

