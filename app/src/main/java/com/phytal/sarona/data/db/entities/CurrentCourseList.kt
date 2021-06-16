package com.phytal.sarona.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

const val CURRENT_COURSE_LIST_ID = 0

@Entity(tableName = "course_table")
data class CurrentCourseList(
    @TypeConverters(DataConverter::class)
    val courses: List<Course>
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_COURSE_LIST_ID
}