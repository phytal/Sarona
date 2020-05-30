package com.phytal.sarona.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.phytal.sarona.data.entities.Course

@Dao
interface CourseDao {
    @Insert
    fun insert(course: Course)

    @Update
    fun update(course: Course)

    @Delete
    fun delete(course: Course)

    @Query("DELETE FROM course_table")
    fun deleteAllCourses()

    @Query("SELECT * FROM course_table")
    fun getAllCourses(): LiveData<List<Course>>

    @Query("SELECT * FROM course_table WHERE id LIKE :cid LIMIT 1")
    fun findById(cid: String): Course
}