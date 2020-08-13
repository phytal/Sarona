package com.phytal.sarona.data

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import com.phytal.sarona.R

/**
 * An object which represents an account which can belong to a user. A single user can have
 * multiple accounts.
 */
data class Account(
    val id: String,
    val fullName: String,
    val username: String,
    val password: String,
    val link: String,
    @DrawableRes val avatar: Int,
    var isCurrentAccount: Boolean = false
) {
    @DrawableRes val checkedIcon: Int = if (isCurrentAccount) R.drawable.ic_done else 0
}



object AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account) =
            oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Account, newItem: Account) =
        oldItem.fullName == newItem.fullName
                && oldItem.username == newItem.username
                && oldItem.password == newItem.password
                && oldItem.link == newItem.link
                && oldItem.isCurrentAccount == newItem.isCurrentAccount
}
