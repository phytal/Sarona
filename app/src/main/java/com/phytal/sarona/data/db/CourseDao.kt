package com.phytal.sarona.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.phytal.sarona.data.db.entities.CourseList

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(course: CourseList)

    @Delete
    fun delete(course: CourseList)

    @Query("DELETE FROM course_table")
    fun deleteAllCourses()

    @Query("SELECT * FROM course_table")
    fun getAllCourses(): LiveData<CourseList>

    // TODO: this doesn't work
    @Query("SELECT * FROM course_table WHERE id LIKE :cid LIMIT 1")
    fun findById(cid: String): CourseList
}