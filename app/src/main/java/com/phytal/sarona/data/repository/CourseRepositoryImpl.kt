package com.phytal.sarona.data.repository

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.MpDao
import com.phytal.sarona.data.db.entities.MarkingPeriod
import com.phytal.sarona.data.network.MpNetworkDataSource
import com.phytal.sarona.data.network.response.CurrentMpResponse
import com.phytal.sarona.data.network.response.MpResponse
import com.phytal.sarona.data.network.response.PastMpResponse
import com.phytal.sarona.data.provider.LoginInformation
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class CourseRepositoryImpl(
    private val mpDao: MpDao,
    private val mpNetworkDataSource: MpNetworkDataSource
) : CourseRepository {

    init {
        mpNetworkDataSource.apply {
            downloadedCurrentMp.observeForever { newCurrentCourses ->
                if (newCurrentCourses != null)
                    persistFetchedCurrentMp(newCurrentCourses)
            }
            downloadedPastMps.observeForever { newPastCourses ->
                if (newPastCourses != null)
                    persistFetchedPastMps(newPastCourses)
            }
            downloadedMp.observeForever {  newCourses ->
                if (newCourses != null)
                    persistFetchedMp(newCourses)
            }
        }
    }

    override suspend fun getCurrentMp(loginInfo: LoginInformation): LiveData<out MarkingPeriod> {
        return withContext(Dispatchers.IO) {
            fetchCurrentMp(loginInfo)
            return@withContext mpDao.getCurrentMp()
        }
    }

    override suspend fun getPastMps(loginInfo: LoginInformation): LiveData<out List<MarkingPeriod>> {
        return withContext(Dispatchers.IO) {
            fetchPastMp(loginInfo)
            return@withContext mpDao.getPastMps()
        }
    }

    override suspend fun getMp(loginInfo: LoginInformation, mp: Int): LiveData<out MarkingPeriod> {
        return withContext(Dispatchers.IO) {
            fetchMp(loginInfo, mp)
            return@withContext mpDao.getMp(mp)
        }
    }

    override suspend fun getAllMps(): LiveData<out List<MarkingPeriod>> {
        return withContext(Dispatchers.IO) {
            return@withContext mpDao.getAllMps()
        }
    }

    @DelicateCoroutinesApi
    private fun persistFetchedCurrentMp(fetchedCourses: CurrentMpResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            mpDao.upsert(fetchedCourses.currentMp)
        }
    }

    @DelicateCoroutinesApi
    private fun persistFetchedPastMps(fetchedCourses: PastMpResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            for (pastCourseList in fetchedCourses.pastMps)
                mpDao.upsert(pastCourseList)
        }
    }

    @DelicateCoroutinesApi
    private fun persistFetchedMp(fetchedCourses: MpResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            mpDao.upsert(fetchedCourses.mp)
        }
    }

    private suspend fun fetchCurrentMp(loginInfo: LoginInformation) {
        mpNetworkDataSource.fetchCurrentMp(
            loginInfo.link,
            loginInfo.username,
            loginInfo.password
        )
    }

    private suspend fun fetchPastMp(loginInfo: LoginInformation) {
        mpNetworkDataSource.fetchPastMps(
            loginInfo.link,
            loginInfo.username,
            loginInfo.password
        )
    }

    private suspend fun fetchMp(loginInfo: LoginInformation, mp: Int) {
        mpNetworkDataSource.fetchMp(
            loginInfo.link,
            loginInfo.username,
            loginInfo.password,
            mp
        )
    }
}