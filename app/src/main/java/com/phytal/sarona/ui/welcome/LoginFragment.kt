package com.phytal.sarona.ui.welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.phytal.sarona.R
import com.phytal.sarona.databinding.FragmentLoginBinding
import com.phytal.sarona.ui.base.ScopedFragment
import com.phytal.sarona.ui.courses.CoursesFragmentDirections
import com.phytal.sarona.util.SpinnerAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class LoginFragment : ScopedFragment(), KodeinAware, AdapterView.OnItemSelectedListener {
    override val kodein by closestKodein()
    lateinit var linkLayout: TextInputLayout
    private val loginViewModelFactory by instance<LoginViewModelFactory>()
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.login_preference_file_key),
            Context.MODE_PRIVATE
        )

        // navigate to CourseFragment if saved login
        if (sharedPref?.getString(getString(R.string.saved_username_key), null) != null &&
            sharedPref.getString(getString(R.string.saved_password_key), null) != null &&
            sharedPref.getString(getString(R.string.saved_link_key), null) != null
        ) {
            val directions =
                CoursesFragmentDirections.actionGlobalCoursesFragment()
            findNavController().navigate(directions)
        }

        val binding = FragmentLoginBinding.inflate(inflater)
        linkLayout = binding.editLinkLayout

        val adapter = SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.welcome_district).toList()
        )
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

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter a username and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val loginSnackbar = Snackbar.make(
                    binding.root,
                    "Logging in..",
                    Snackbar.LENGTH_INDEFINITE
                )
                loginSnackbar.show()
                // Checks login information before proceeding
                loginViewModel.fetchValidLogin(link, username, password)
                loginViewModel.validLogin.observe(viewLifecycleOwner, Observer { result ->
                    result.getContentIfNotHandled()?.let {
                        loginSnackbar.dismiss()
                        if (it) {
                            if (sharedPref != null) {
                                with(sharedPref.edit()) {
                                    putString(getString(R.string.saved_username_key), username)
                                    putString(getString(R.string.saved_password_key), password)
                                    putString(getString(R.string.saved_link_key), link)
                                    apply()
                                }
                            }

                            val directions =
                                CoursesFragmentDirections.actionGlobalCoursesFragment()
                            findNavController().navigate(directions)
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Invalid login. If you think this is an error, please report this bug.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            return@Observer
                        }

                    }
                })
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val district = parent?.getItemAtPosition(position).toString()
        linkLayout.visibility = if (district == "Other")
            View.VISIBLE
        else
            View.GONE
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}