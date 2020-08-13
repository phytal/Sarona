package com.phytal.sarona.ui.auth

import android.os.Bundle
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
        val firebaseAuth = FirebaseAuth.getInstance()
        root.register_button.setOnClickListener {
            val email = reg_email.text.toString()
            val password = reg_password.text.toString()
            val passwordConfirm = reg_confirm_password.text.toString()
            if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                Toast.makeText(requireContext(), "One or more credential fields is empty", Toast.LENGTH_LONG).show()
            }
            else if (password != passwordConfirm) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_LONG).show()
            }
            else {
                try {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                    findNavController().navigate(R.id.nav_home)
                }
                catch (e: FirebaseAuthException) {
                    Toast.makeText(requireContext(), "Failed to create account, is this email already registered?", Toast.LENGTH_SHORT).show()
                }
            }
        }
        root.login_clickable.setOnClickListener {
            findNavController().navigate(R.id.nav_login_email)
        }
        return root
    }
}