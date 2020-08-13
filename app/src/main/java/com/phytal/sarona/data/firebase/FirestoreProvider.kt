package com.phytal.sarona.data.firebase

import com.phytal.sarona.data.Account
import com.phytal.sarona.data.provider.LoginInformation

interface FirestoreProvider {
    //fun getAccount(loginInformation: LoginInformation) : Account
    fun createAccount(email: String, password: String) : Boolean
    fun saveAccount(account: Account) : Boolean
}