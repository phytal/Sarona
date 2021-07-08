package com.phytal.sarona.data.db.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun toCourseList(jsonString: String): List<Course>? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Course>>() {}.type
        return gson.fromJson<List<Course>>(jsonString, type)
    }

    @TypeConverter
    fun fromCourseList(jsonString: List<Course>): String? {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Course>>() {}.type
        return gson.toJson(jsonString, type)
    }
}