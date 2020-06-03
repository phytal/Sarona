package com.phytal.sarona.data.repository

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.entities.CurrentCourse
import com.phytal.sarona.data.db.entities.CurrentCourseList


interface CourseRepository {
    suspend fun getCurrentCourses() : LiveData<out CurrentCourseList>

}