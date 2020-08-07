/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phytal.sarona.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phytal.sarona.R

/**
 * An static data store of [Account]s. This includes both [Account]s owned by the current user and
 * all [Account]s of the current user's contacts.
 */
object AccountStore {

    private val allUserAccounts = mutableListOf(
        Account(
            0,
            "Jeff",
            "Hansen",
            "jeffh123",
            "123",
            "d",
            R.drawable.sarona_logo,
            true
        )
    )

    private val _userAccounts: MutableLiveData<List<Account>> = MutableLiveData()
    val userAccounts: LiveData<List<Account>>
        get() = _userAccounts

    init {
        postUpdateUserAccountsList()
    }

    /**
     * Get the current user's default account.
     */
    fun getDefaultUserAccount() = allUserAccounts.first()

    /**
     * Get all [Account]s owned by the current user.
     */
    fun getAllUserAccounts() = allUserAccounts

    /**
     * Whether or not the given [Account.id] uid is an account owned by the current user.
     */
    fun isUserAccount(id: Long): Boolean = allUserAccounts.any { it.id == id }

    fun setCurrentUserAccount(accountId: Long): Boolean {
        var updated = false
        allUserAccounts.forEachIndexed { index, account ->
            val shouldCheck = account.id == accountId
            if (account.isCurrentAccount != shouldCheck) {
                allUserAccounts[index] = account.copy(isCurrentAccount = shouldCheck)
                updated = true
            }
        }
        if (updated) postUpdateUserAccountsList()
        return updated
    }

    private fun postUpdateUserAccountsList() {
        val newList = allUserAccounts.toList()
        _userAccounts.value = newList
    }
}