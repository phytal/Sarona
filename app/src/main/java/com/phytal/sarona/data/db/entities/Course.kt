package com.phytal.sarona.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

const val CURRENT_COURSE_ID = 0

@Entity(tableName = "course_table")
data class Course(
    @TypeConverters(DataConverter::class)
    val assignments: List<Assignment>,
//    @ColumnInfo(name = "course_average")
//    val courseAverage: Double,
//    @ColumnInfo(name = "course_id")
//    val courseId: String,
//    @ColumnInfo(name = "course_name")
//    val courseName: String
    @ColumnInfo(name = "average")
    val average: Double,
    @ColumnInfo(name = "course")
    val course: String,
    @ColumnInfo(name = "name")
    val name: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_COURSE_ID
}