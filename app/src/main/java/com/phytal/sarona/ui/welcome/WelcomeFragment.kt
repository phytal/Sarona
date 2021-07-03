package com.phytal.sarona.ui.welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.phytal.sarona.R
import com.phytal.sarona.databinding.FragmentWelcomeBinding
import com.phytal.sarona.ui.courses.CoursesFragment
import com.phytal.sarona.ui.courses.CoursesFragmentDirections


class WelcomeFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var editLink: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWelcomeBinding.inflate(inflater)
        editLink = binding.editLinkLayout

        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.welcome_district, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.districtSpinner.adapter = adapter
        binding.districtSpinner.onItemSelectedListener = this

        binding.loginButton.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            val link = if (binding.districtSpinner.selectedItem.toString() == "Frisco ISD")
                "hac.friscoisd.org"
            else
                binding.editTextLink.text.toString()
            if (username.isEmpty()|| password.isEmpty()) {
                Toast.makeText(context, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            }
            else {
                val sharedPref = activity?.getSharedPreferences(getString(R.string.login_preference_file_key), Context.MODE_PRIVATE)
                if (sharedPref != null) {
                    with (sharedPref.edit()) {
                        putString(getString(R.string.saved_username_key), username)
                        putString(getString(R.string.saved_password_key), password)
                        putString(getString(R.string.saved_link_key), link)
                        apply()
                    }
                }

                val directions =
                    CoursesFragmentDirections.actionGlobalCoursesFragment()
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val district = parent?.getItemAtPosition(position).toString()
        editLink.visibility = if (district == "Other")
             View.VISIBLE
        else
           View.GONE
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}