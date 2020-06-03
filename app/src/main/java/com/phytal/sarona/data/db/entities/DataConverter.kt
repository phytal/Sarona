package com.phytal.sarona.data.db.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun fromAssignmentList(assignmentList: List<Assignment>): String? {
        if (assignmentList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Assignment>>() {}.type
        return gson.toJson(assignmentList, type)
    }

    @TypeConverter
    fun toCurrentCourse(currentCourseListString: String): List<CurrentCourse>? {
        if (currentCourseListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<CurrentCourse>>() {}.type
        return gson.fromJson<List<CurrentCourse>>(currentCourseListString, type)
    }
    @TypeConverter
    fun fromCurrentCourse(currentCourseList: List<CurrentCourse>): String? {
        if (currentCourseList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<CurrentCourse>>() {}.type
        return gson.toJson(currentCourseList, type)
    }

    @TypeConverter
    fun toAssignmentList(assignmentListString: String): List<Assignment>? {
        if (assignmentListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Assignment>>() {}.type
        return gson.fromJson<List<Assignment>>(assignmentListString, type)
    }
}