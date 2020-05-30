package com.phytal.sarona.data

import android.content.Context
import android.os.AsyncTask
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.phytal.sarona.data.dao.CourseDao
import com.phytal.sarona.data.entities.Course
import com.phytal.sarona.helpers.SingletonHolder


@Database(entities = [Course::class], version = 1)
abstract class CourseDatabase: RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object : SingletonHolder<CourseDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, CourseDatabase::class.java, "course_table.db")
            .fallbackToDestructiveMigration()
            .build()
    })
}
