package com.phytal.sarona.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phytal.sarona.data.db.entities.CourseList
import com.phytal.sarona.data.db.entities.DataConverter
import com.phytal.sarona.internal.helpers.SingletonHolder


@Database(entities = [CourseList::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class CourseDatabase: RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object : SingletonHolder<CourseDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, CourseDatabase::class.java, "course_table.db")
            .fallbackToDestructiveMigration()
            .build()
    })
}
