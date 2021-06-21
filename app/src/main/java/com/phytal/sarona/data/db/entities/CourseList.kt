package com.phytal.sarona.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

const val COURSE_LIST_ID = 0

@Entity(tableName = "course_table")
data class CourseList(
    @TypeConverters(DataConverter::class)
    val yearCourses: ArrayList<ArrayList<Course>>
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = COURSE_LIST_ID
}