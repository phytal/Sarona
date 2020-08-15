package com.phytal.sarona.data.firebase

interface FirestoreProvider {
    fun saveAccountToDatabase(user: DatabaseUser) : Boolean
    fun deleteAccount() : Boolean
    fun getAccount() : DatabaseUser
    fun createNewAccount() : Boolean
}