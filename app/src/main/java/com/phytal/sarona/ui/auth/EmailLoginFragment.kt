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
import kotlinx.android.synthetic.main.fragment_email_login.*
import kotlinx.android.synthetic.main.fragment_email_login.view.*

class EmailLoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_email_login, container, false)
        val firebaseAuth = FirebaseAuth.getInstance()
        root.login_button_email.setOnClickListener {
            if (email_login_email.text.toString().isEmpty() || email_login_password.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "One or more credential fields is empty", Toast.LENGTH_LONG).show()
            }
            else {
                try {
                    firebaseAuth.signInWithEmailAndPassword(
                        email_login_email.text.toString(),
                        email_login_password.text.toString()
                    )
                    findNavController().navigate(R.id.nav_home)
                }
                catch (e: FirebaseAuthException) {
                    Toast.makeText(requireContext(), "Failed to login. Are your credentials correct?", Toast.LENGTH_SHORT).show()
                }
            }
        }
        root.no_account_clickable.setOnClickListener {
            findNavController().navigate(R.id.nav_reg_email)
        }
        return root
    }
}