package com.phytal.sarona.data.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.CourseDatabase
import com.phytal.sarona.data.db.CurrentCourseDao
import com.phytal.sarona.data.db.entities.CurrentCourse


class CourseRepository(application: Application) {
    private val currentCourseDao: CurrentCourseDao
    private val allCourses: LiveData<List<CurrentCourse>>

    fun upsert(course: CurrentCourse) {
        UpsertCourseAsyncTask(
            currentCourseDao
        ).execute(course)
    }
    fun delete(course: CurrentCourse) {
        DeleteCourseAsyncTask(
            currentCourseDao
        ).execute(course)
    }

    fun deleteAllCourses() {
        DeleteAllCoursesAsyncTask(
            currentCourseDao
        ).execute()
    }

    fun getAllCourses(): LiveData<List<CurrentCourse>> {
        return allCourses
    }

    private class UpsertCourseAsyncTask(currentCourseDao: CurrentCourseDao) :
        AsyncTask<CurrentCourse, Void?, Void?>() {
        private val currentCourseDao: CurrentCourseDao = currentCourseDao
        override fun doInBackground(vararg courses: CurrentCourse): Void? {
            currentCourseDao.upsert(courses[0])
            return null
        }
    }

    private class DeleteCourseAsyncTask(currentCourseDao: CurrentCourseDao) :
        AsyncTask<CurrentCourse, Void?, Void?>() {
        private val currentCourseDao: CurrentCourseDao = currentCourseDao
        override fun doInBackground(vararg courses: CurrentCourse): Void? {
            currentCourseDao.delete(courses[0])
            return null
        }
    }

    private class DeleteAllCoursesAsyncTask(currentCourseDao: CurrentCourseDao) :
        AsyncTask<Void?, Void?, Void?>() {
        private val currentCourseDao: CurrentCourseDao = currentCourseDao
        override fun doInBackground(vararg params: Void?): Void? {
            currentCourseDao.deleteAllCourses()
            return null
        }
    }

    init {
        val database: CourseDatabase = CourseDatabase.getInstance(application)
        currentCourseDao = database.courseDao()
        allCourses = currentCourseDao.getAllCourses()
    }
}