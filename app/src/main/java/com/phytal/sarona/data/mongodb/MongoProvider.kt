package com.phytal.sarona.data.mongodb

import com.phytal.sarona.data.Account
import com.phytal.sarona.data.provider.LoginInformation

interface MongoProvider {
    fun getAccount(loginInformation: LoginInformation) : Account?
}