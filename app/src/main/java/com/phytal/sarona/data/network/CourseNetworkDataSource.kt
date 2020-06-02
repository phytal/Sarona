package com.phytal.sarona.data.network

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.network.response.CourseResponse

interface CourseNetworkDataSource {
    val downloadedCurrentCourse: LiveData<CourseResponse>

    suspend fun fetchCurrent(
        hacLink: String,
        username: String,
        password: String
    )
}