package com.phytal.sarona.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.phytal.sarona.data.db.entities.CurrentCourse
import com.phytal.sarona.data.db.entities.CurrentCourseList

@Dao
interface CurrentCourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(course: CurrentCourseList)

    @Delete
    fun delete(course: CurrentCourseList)

    @Query("DELETE FROM current_course_table")
    fun deleteAllCourses()

    @Query("SELECT * FROM current_course_table")
    fun getAllCourses(): LiveData<CurrentCourseList>

    @Query("SELECT * FROM current_course_table WHERE id LIKE :cid LIMIT 1")
    fun findById(cid: String): CurrentCourseList
}