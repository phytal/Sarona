package com.phytal.sarona.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.phytal.sarona.data.db.entities.MarkingPeriod

@Dao
interface MpDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(mp: MarkingPeriod)

    @Delete
    fun delete(mp: MarkingPeriod)

    @Query("DELETE FROM mp_table")
    fun deleteAllMps()

    @Query("SELECT * FROM mp_table")
    fun getAllMps(): LiveData<List<MarkingPeriod>>

    @Query("SELECT * FROM mp_table WHERE mp LIKE :mp+1 LIMIT 1")
    fun getMp(mp: Int): LiveData<MarkingPeriod>

    @Query("SELECT * FROM mp_table WHERE current IS 1 LIMIT 1")
    fun getCurrentMp(): LiveData<MarkingPeriod>
}