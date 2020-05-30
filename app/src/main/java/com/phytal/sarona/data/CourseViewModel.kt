package com.phytal.sarona.data

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.phytal.sarona.data.entities.Course


class CourseViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository: CourseRepository = CourseRepository(application)
    private val allCourses: LiveData<List<Course>>

    fun insert(course: Course) {
        repository.insert(course)
    }

    fun update(course: Course) {
        repository.update(course)
    }

    fun delete(course: Course) {
        repository.delete(course)
    }

    fun deleteAllCourses() {
        repository.deleteAllCourses()
    }

    fun getAllCourses(): LiveData<List<Course>> {
        return allCourses
    }
    init {
        allCourses = repository.getAllCourses()
    }
}