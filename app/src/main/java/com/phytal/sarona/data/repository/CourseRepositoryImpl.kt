package com.phytal.sarona.data.repository

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.CourseDao
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.db.entities.CourseList
import com.phytal.sarona.data.network.CourseNetworkDataSource
import com.phytal.sarona.data.provider.LoginInformation
import kotlinx.coroutines.*
import java.time.ZonedDateTime

@DelicateCoroutinesApi
class CourseRepositoryImpl (
    private val courseDao: CourseDao,
    private val courseNetworkDataSource: CourseNetworkDataSource
): CourseRepository {

    init {
        courseNetworkDataSource.downloadedCurrentCourse.observeForever { newCurrentCourses ->
            persistFetchedCurrentCourse(newCurrentCourses)
        }
    }
    override suspend fun getCurrentCourses(loginInfo: LoginInformation): LiveData<out CourseList> {
        return withContext(Dispatchers.IO) {
            fetchCurrentCourses(loginInfo)
            return@withContext courseDao.getAllCourses()
        }
    }

    @DelicateCoroutinesApi
    private fun persistFetchedCurrentCourse(fetchedCourses: ArrayList<ArrayList<Course>>) {
        GlobalScope.launch(Dispatchers.IO) {
            courseDao.upsert(CourseList(fetchedCourses))
        }
    }

    private suspend fun fetchCurrentCourses(loginInfo: LoginInformation) {
        courseNetworkDataSource.fetchCurrent(
            loginInfo.link,
            loginInfo.username,
            loginInfo.password
        )
    }
}