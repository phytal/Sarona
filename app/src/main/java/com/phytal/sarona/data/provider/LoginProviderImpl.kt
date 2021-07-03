package com.phytal.sarona.data.provider

import android.content.Context
import android.content.SharedPreferences
import com.phytal.sarona.R

class LoginProviderImpl(context: Context) : LoginProvider {
    private val appContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = appContext.getSharedPreferences(appContext.resources.getString(R.string.login_preference_file_key), Context.MODE_PRIVATE)

    override fun getLoginInfo(): LoginInformation {
        // can't really have a default value
        val username = preferences.getString(appContext.resources.getString(R.string.saved_username_key), "username")
        val password = preferences.getString(appContext.resources.getString(R.string.saved_password_key), "password")
        val link = preferences.getString(appContext.resources.getString(R.string.saved_link_key), "link")
        return LoginInformation(username!!, password!!, link!!)
    }
}