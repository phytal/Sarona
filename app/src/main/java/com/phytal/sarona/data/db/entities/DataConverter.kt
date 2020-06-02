package com.phytal.sarona.data.db.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun fromAssignmentList(countryLang: List<Assignment>): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Assignment>>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toAssignmentList(countryLangString: String): List<Assignment>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Assignment>>() {}.type
        return gson.fromJson<List<Assignment>>(countryLangString, type)
    }
}