package com.phytal.sarona.data.db.entities

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Assignment(
    val comment: String?,
    val date_assigned: String,
    val date_due: String,
    val max_points: String,
    val percentage: String,
    val score: String,
    val title_of_assignment: String,
    val total_points: String,
    val type_of_grade: String,
    val weight: String,
    val weighted_total_points  : String,
    val weighted_score : String
) : Parcelable