package com.phytal.sarona.data.repository

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.db.entities.MarkingPeriod
import com.phytal.sarona.data.provider.LoginInformation

interface CourseRepository {
    suspend fun getCurrentMp(loginInfo: LoginInformation): LiveData<out MarkingPeriod>
    suspend fun getMp(loginInfo: LoginInformation, mp: Int): LiveData<out MarkingPeriod>
    suspend fun getAllMps(): LiveData<out List<MarkingPeriod>>
}