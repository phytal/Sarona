package com.phytal.sarona.data.provider

interface LoginProvider {
    fun getLoginInfo(): LoginInformation

}

data class LoginInformation(
    val username: String = "",
    val password: String = "",
    val link: String = ""
)