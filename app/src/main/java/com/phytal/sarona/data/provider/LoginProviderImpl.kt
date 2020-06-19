package com.phytal.sarona.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

const val LOGIN_USERNAME = "LOGIN_USERNAME"
const val LOGIN_PASSWORD = "LOGIN_PASSWORD"
const val LOGIN_LINK = "LOGIN_LINK"

class LoginProviderImpl(context: Context) : LoginProvider {
    private val appContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    override fun getLoginInfo(): LoginInformation {
        // can't really have a default value
        val username = preferences.getString(LOGIN_USERNAME, "username")
        val password = preferences.getString(LOGIN_PASSWORD, "password")
        val link = preferences.getString(LOGIN_LINK, "link")
        return LoginInformation(username!!, password!!, link!!)
    }
}