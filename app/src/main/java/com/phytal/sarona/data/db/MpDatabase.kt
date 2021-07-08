package com.phytal.sarona.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phytal.sarona.data.db.entities.MarkingPeriod
import com.phytal.sarona.data.db.entities.DataConverter
import com.phytal.sarona.internal.helpers.SingletonHolder

@Database(entities = [MarkingPeriod::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class MpDatabase: RoomDatabase() {

    abstract fun mpDao(): MpDao

    companion object : SingletonHolder<MpDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, MpDatabase::class.java, "mp_table.db")
            .fallbackToDestructiveMigration()
            .build()
    })
}
