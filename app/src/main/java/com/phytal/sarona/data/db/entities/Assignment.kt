package com.phytal.sarona.data.db.entities

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Assignment(
    val can_be_dropped: String,
    val date_assigned: String,
    val date_due: String,
    val max_points: Double,
    val percentage: Double,
    val score: Double,
    val title_of_assignment: String,
    val total_points: Double,
    val type_of_grade: String,
    val weight: Double,
    val weighted_total_points  : Double,
    val weighted_score : Double
) : Parcelable