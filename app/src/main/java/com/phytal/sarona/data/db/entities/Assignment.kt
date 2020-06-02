package com.phytal.sarona.data.db.entities

data class Assignment(
    val name: String,
    val category: String,
    val score: Double,
    val maxPoints: Double,
    val canBeDropped: Boolean,
    val extraCredit: Boolean,
    val hasAttachments: Boolean
)