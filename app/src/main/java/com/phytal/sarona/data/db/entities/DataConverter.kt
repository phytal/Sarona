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
    fun toCourses(coursesString: String): List<Course>? {
        if (coursesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Course>>() {}.type
        return gson.fromJson<List<Course>>(coursesString, type)
    }
    @TypeConverter
    fun fromCourses(courses: List<Course>): String? {
        if (courses == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Course>>() {}.type
        return gson.toJson(courses, type)
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

    @TypeConverter
    fun toCourseList(courseListString: String): List<List<Course>>? {
        if (courseListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<List<Course>>>() {}.type
        return gson.fromJson<List<List<Course>>>(courseListString, type)
    }
    @TypeConverter
    fun fromCourseList(courseList: List<List<Course>>): String? {
        if (courseList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<List<Course>>>() {}.type
        return gson.toJson(courseList, type)
    }
}