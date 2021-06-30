package com.phytal.sarona.data.db.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
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
}