package com.phytal.sarona.data.db.entities

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil

@kotlinx.parcelize.Parcelize
data class GradeType(
    val category: String,
    val grade: Double,
    val weight: Double
) : Parcelable

object GradeDiffCallback : DiffUtil.ItemCallback<GradeType>() {
    override fun areItemsTheSame(oldItem: GradeType, newItem: GradeType) = oldItem.category == newItem.category
    override fun areContentsTheSame(oldItem: GradeType, newItem: GradeType) = oldItem == newItem
}