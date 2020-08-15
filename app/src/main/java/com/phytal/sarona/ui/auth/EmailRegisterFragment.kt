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
import com.phytal.sarona.R
import kotlinx.android.synthetic.main.fragment_email_register.*
import kotlinx.android.synthetic.main.fragment_email_register.view.*

class EmailRegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_email_register, container, false)

        root.register_button.setOnClickListener {
            performRegister()
        }
        root.login_clickable.setOnClickListener {
            findNavController().navigate(R.id.nav_login_email)
        }
        return root
    }

    private fun performRegister() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val email = reg_email.text.toString()
        val password = reg_password.text.toString()
        val passwordConfirm = reg_confirm_password.text.toString()
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "One or more credential fields is empty",
                Toast.LENGTH_LONG
            ).show()
        } else if (password != passwordConfirm) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_LONG).show()
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d(TAG, "Successfully created user with uid ${it.result?.user?.uid}")
                    findNavController().navigate(R.id.nav_home)
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to create user: ${it.message}")
                    Toast.makeText(
                        requireContext(),
                        "Failed to create user: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    companion object {
        private const val TAG = "EMAIL_REG"
    }
}