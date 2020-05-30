package com.phytal.sarona.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_table")
data class Course (
    @PrimaryKey(autoGenerate = true) val id : Int,

    val title: String?,
    val grade: Double
)