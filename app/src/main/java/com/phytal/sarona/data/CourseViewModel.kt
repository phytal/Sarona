package com.phytal.sarona.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.entities.CurrentCourse
import com.phytal.sarona.data.repository.CourseRepository


class CourseViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository: CourseRepository =
        CourseRepository(application)
    private val allCourses: LiveData<List<CurrentCourse>>

    fun upsert(course: CurrentCourse) {
        repository.upsert(course)
    }

    fun delete(course: CurrentCourse) {
        repository.delete(course)
    }

    fun deleteAllCourses() {
        repository.deleteAllCourses()
    }

    fun getAllCourses(): LiveData<List<CurrentCourse>> {
        return allCourses
    }
    init {
        allCourses = repository.getAllCourses()
    }
}