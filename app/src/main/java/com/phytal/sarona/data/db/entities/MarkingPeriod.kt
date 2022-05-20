package com.phytal.sarona.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "mp_table", indices = [Index(value = ["mp"], unique = true)])
data class MarkingPeriod(
    @PrimaryKey
    val mp: Int,
    val current: Boolean,
    @TypeConverters(DataConverter::class)
    val courses: List<Course>
)