package com.phytal.sarona.data.repository

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.entities.CourseList
import com.phytal.sarona.data.provider.LoginInformation


interface CourseRepository {
    suspend fun getCurrentCourses(loginInfo: LoginInformation) : LiveData<out CourseList>

}