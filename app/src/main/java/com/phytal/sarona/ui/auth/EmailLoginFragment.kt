package com.phytal.sarona.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.phytal.sarona.R
import kotlinx.android.synthetic.main.fragment_email_login.*
import kotlinx.android.synthetic.main.fragment_email_login.view.*

class EmailLoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_email_login, container, false)

        root.login_button_email.setOnClickListener {
           performLogin()
        }
        root.no_account_clickable.setOnClickListener {
            findNavController().navigate(R.id.nav_reg_email)
        }
        return root
    }

    private fun performLogin() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val email = email_login_email.text.toString()
        val password = email_login_password.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "One or more credential fields is empty",
                Toast.LENGTH_LONG
            ).show()
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d(TAG, "Successfully logged in user with uid ${it.result?.user?.uid}")
                    findNavController().navigate(R.id.nav_home)
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to log in: ${it.message}")
                    Toast.makeText(
                        requireContext(),
                        "Failed to log in: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    companion object {
        private const val TAG = "EMAIL_LOGIN"
    }
}