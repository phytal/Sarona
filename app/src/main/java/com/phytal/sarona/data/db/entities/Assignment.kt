package com.phytal.sarona.data.db.entities

data class Assignment(
//    val name: String,
//    val category: String,
//    val score: Double,
//    val maxPoints: Double,
//    val canBeDropped: Boolean,
//    val extraCredit: Boolean,
//    val hasAttachments: Boolean
    val can_be_dropped: String,
    val date_assigned: String,
    val date_due: String,
    val max_points: Double,
    val percentage: Double,
    val score: Double,
    val title_of_assignment: String,
    val total_points: Double,
    val type_of_grade: String,
    val weight: Double

)