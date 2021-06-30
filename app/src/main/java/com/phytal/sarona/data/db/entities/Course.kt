package com.phytal.sarona.data.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

const val CURRENT_COURSE_ID = 0
@kotlinx.parcelize.Parcelize
@Entity(tableName = "course_table")
data class Course(
    @TypeConverters(DataConverter::class)
    val assignments: List<Assignment>,
    @ColumnInfo(name = "average")
    val average: Double,
    @ColumnInfo(name = "course")
    val course: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "grade_types")
    val grade_types: List<GradeType>
) : Parcelable {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_COURSE_ID
}