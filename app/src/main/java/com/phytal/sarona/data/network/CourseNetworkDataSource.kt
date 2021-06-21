package com.phytal.sarona.data.network

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.network.response.CourseResponse

interface CourseNetworkDataSource {
    val downloadedCurrentCourse: LiveData<ArrayList<ArrayList<Course>>>

    suspend fun fetchCurrent(
        hacLink: String,
        username: String,
        password: String
    )
}