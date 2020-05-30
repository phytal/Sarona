package com.phytal.sarona.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.phytal.sarona.data.dao.CourseDao
import com.phytal.sarona.data.entities.Course


class CourseRepository(application: Application) {
    private val courseDao: CourseDao
    private val allCourses: LiveData<List<Course>>

    fun insert(course: Course) {
        InsertCourseAsyncTask(courseDao).execute(course)
    }

    fun update(course: Course) {
        UpdateCourseAsyncTask(courseDao).execute(course)
    }

    fun delete(course: Course) {
        DeleteCourseAsyncTask(courseDao).execute(course)
    }

    fun deleteAllCourses() {
        DeleteAllCoursesAsyncTask(courseDao).execute()
    }

    fun getAllCourses(): LiveData<List<Course>> {
        return allCourses
    }

    private class InsertCourseAsyncTask(courseDao: CourseDao) :
        AsyncTask<Course, Void?, Void?>() {
        private val courseDao: CourseDao = courseDao
        override fun doInBackground(vararg courses: Course): Void? {
            courseDao.insert(courses[0])
            return null
        }
    }

    private class UpdateCourseAsyncTask(courseDao: CourseDao) :
        AsyncTask<Course, Void?, Void?>() {
        private val courseDao: CourseDao = courseDao
        override fun doInBackground(vararg courses: Course): Void? {
            courseDao.update(courses[0])
            return null
        }
    }

    private class DeleteCourseAsyncTask(courseDao: CourseDao) :
        AsyncTask<Course, Void?, Void?>() {
        private val courseDao: CourseDao = courseDao
        override fun doInBackground(vararg courses: Course): Void? {
            courseDao.delete(courses[0])
            return null
        }
    }

    private class DeleteAllCoursesAsyncTask(courseDao: CourseDao) :
        AsyncTask<Void?, Void?, Void?>() {
        private val courseDao: CourseDao = courseDao
        override fun doInBackground(vararg params: Void?): Void? {
            courseDao.deleteAllCourses()
            return null
        }
    }

    init {
        val database: CourseDatabase = CourseDatabase.getInstance(application)
        courseDao = database.courseDao()
        allCourses = courseDao.getAllCourses()
    }
}