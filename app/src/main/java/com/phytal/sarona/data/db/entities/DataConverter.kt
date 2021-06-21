package com.phytal.sarona.data.db.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun fromAssignmentList(jsonString: List<Assignment>): String? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Assignment>>() {}.type
        return gson.toJson(jsonString, type)
    }

    @TypeConverter
    fun toCourses(jsonString: String): List<Course>? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Course>>() {}.type
        return gson.fromJson<List<Course>>(jsonString, type)
    }
    @TypeConverter
    fun fromCourses(jsonString: List<Course>): String? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Course>>() {}.type
        return gson.toJson(jsonString, type)
    }

    @TypeConverter
    fun toAssignmentList(jsonString: String): List<Assignment>? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Assignment>>() {}.type
        return gson.fromJson<List<Assignment>>(jsonString, type)
    }

    @TypeConverter
    fun toCourseList(jsonString: String): ArrayList<ArrayList<Course>>? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<ArrayList<ArrayList<Course>>>() {}.type
        return gson.fromJson<ArrayList<ArrayList<Course>>>(jsonString, type)
    }

    @TypeConverter
    fun fromCourseList(jsonString: ArrayList<ArrayList<Course>>): String? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<ArrayList<ArrayList<Course>>>() {}.type
        return gson.toJson(jsonString, type)
    }

    @TypeConverter
    fun fromCourse(jsonString: Course): String? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<Course>() {}.type
        return gson.toJson(jsonString, type)
    }
    @TypeConverter
    fun toCourse(jsonString: String): Course? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<Course>() {}.type
        return gson.fromJson<Course>(jsonString, type)
    }

}