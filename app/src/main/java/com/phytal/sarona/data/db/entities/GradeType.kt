package com.phytal.sarona.data.db.entities

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class GradeType(
    val category: String,
    val weight: Double
) : Parcelable