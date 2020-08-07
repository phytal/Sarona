package com.phytal.sarona.data

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import com.phytal.sarona.R
import org.litote.kmongo.reactivestreams.*
import org.litote.kmongo.coroutine.*

/**
 * An object which represents an account which can belong to a user. A single user can have
 * multiple accounts.
 */
data class Account(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val link: String,
    @DrawableRes val avatar: Int,
    var isCurrentAccount: Boolean = false
) {
    val fullName: String = "$firstName $lastName"
    @DrawableRes val checkedIcon: Int = if (isCurrentAccount) R.drawable.ic_done else 0
}



object AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account) =
            oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Account, newItem: Account) =
        oldItem.firstName == newItem.firstName
                && oldItem.lastName == newItem.lastName
                && oldItem.isCurrentAccount == newItem.isCurrentAccount
}
