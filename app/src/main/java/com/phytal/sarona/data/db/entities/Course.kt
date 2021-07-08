package com.phytal.sarona.data.db.entities

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Course(
    val assignments: List<Assignment>,
    val average: Double,
    val course: String,
    val name: String,
    val grade_types: List<GradeType>
) : Parcelable