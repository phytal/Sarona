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
            initCourseData(loginInfo)
            return@withContext courseDao.getAllCourses()
        }
    }

    @DelicateCoroutinesApi
    private fun persistFetchedCurrentCourse(fetchedCourses: ArrayList<ArrayList<Course>>) {
        GlobalScope.launch(Dispatchers.IO) {
            courseDao.upsert(CourseList(fetchedCourses))
        }
    }

    //TODO
    private suspend fun initCourseData(loginInfo: LoginInformation) {
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentCourses(loginInfo)
    }

    private suspend fun fetchCurrentCourses(loginInfo: LoginInformation) {
        courseNetworkDataSource.fetchCurrent(
            loginInfo.link,
            loginInfo.username,
            loginInfo.password
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val fiveMinutesAgo = ZonedDateTime.now().minusMinutes(5)
        return lastFetchTime.isBefore(fiveMinutesAgo)
    }
}