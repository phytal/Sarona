package com.phytal.sarona.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.phytal.sarona.R
import com.phytal.sarona.data.firebase.FirestoreProvider
import com.phytal.sarona.data.provider.LoginInformation
import kotlinx.android.synthetic.main.fragment_add_account.*
import kotlinx.android.synthetic.main.fragment_add_account.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AddAccountFragment: Fragment(), KodeinAware {
    override val kodein by closestKodein()
    private val firestore: FirestoreProvider by instance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_add_account, container, false)

        root.add_account_button.setOnClickListener {
            if (!addAccount()) {
                Toast.makeText(requireContext(), "Unable to add another user to this account", Toast.LENGTH_LONG).show()
            }
        }
        return root
    }
    private fun addAccount() : Boolean {
        val username = add_account_username.text.toString()
        val password = add_account_password.text.toString()
        val link = add_account_link.text.toString()

        val info = firestore.getAccount()
        val success = info.accounts.add(LoginInformation(username, password, link))
        if (success)
            firestore.saveAccountToDatabase(info)
        return success
    }
}