package com.phytal.sarona.data.repository

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.CurrentCourseDao
import com.phytal.sarona.data.db.entities.CurrentCourseList
import com.phytal.sarona.data.network.CourseNetworkDataSource
import com.phytal.sarona.data.network.response.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class CourseRepositoryImpl (
    private val currentCourseDao: CurrentCourseDao,
    private val courseNetworkDataSource: CourseNetworkDataSource
): CourseRepository {

    init {
        courseNetworkDataSource.downloadedCurrentCourse.observeForever { newCurrentCourses ->
            persistFetchedCurrentCourse(newCurrentCourses)
        }
    }
    override suspend fun getCurrentCourses(): LiveData<out CurrentCourseList> {
        return withContext(Dispatchers.IO) {
            initCourseData()
            return@withContext currentCourseDao.getAllCourses()
        }
    }

    private fun persistFetchedCurrentCourse(fetchedCourses: CourseResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentCourseDao.upsert(CurrentCourseList(fetchedCourses.currentAssignmentList))
        }
    }

    //TODO
    private suspend fun initCourseData() {
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentCourses()
    }

    //TODO: not hardcoded
    private suspend fun fetchCurrentCourses() {
        courseNetworkDataSource.fetchCurrent(
            "https://hac.friscoisd.org",
            "Zhang.W",
            "William826"
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val fiveMinutesAgo = ZonedDateTime.now().minusMinutes(5)
        return lastFetchTime.isBefore(fiveMinutesAgo)
    }
}