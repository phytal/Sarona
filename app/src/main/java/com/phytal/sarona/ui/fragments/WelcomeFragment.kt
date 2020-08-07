package com.phytal.sarona.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.phytal.sarona.R
import com.phytal.sarona.ui.NavigationHost
import com.phytal.sarona.ui.home.HomeFragment


class WelcomeFragment : Fragment(), AdapterView.OnItemSelectedListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_welcome, container, false)
        val loginButton = root.findViewById<MaterialButton>(R.id.login_button)
        val spinner: Spinner = root.findViewById(R.id.district_spinner)
        val usernameField = root.findViewById<TextInputEditText>(R.id.editTextUsername)
        val passwordField = root.findViewById<TextInputEditText>(R.id.editTextPassword)

        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.welcome_district, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        loginButton.setOnClickListener {
            if (usernameField.text.isNullOrEmpty()|| passwordField.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            }
            else {
                (activity as NavigationHost).navigateTo(HomeFragment(), false) // Navigate to the next Fragment
            }
        }

        return root
    }

    companion object {
        const val TAG = "FRAGMENT_WELCOME"
    }
    private lateinit var link: String
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val district = parent?.getItemAtPosition(position).toString()
        if (district == "Frisco ISD") {
            link = "https://hac.friscoisd.org"
        }
        else {
            //prompt user to type in link
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.nav_view).visibility = View.GONE
//        (activity as AppCompatActivity?)!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).visibility = View.GONE
//    }
//
//    override fun onStop() {
//        super.onStop()
//        (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.nav_view).visibility = View.VISIBLE
//        (activity as AppCompatActivity?)!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).visibility = View.VISIBLE
//    }

}