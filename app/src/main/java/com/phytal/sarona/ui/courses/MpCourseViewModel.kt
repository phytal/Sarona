package com.phytal.sarona.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phytal.sarona.data.db.entities.MarkingPeriod
import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository
import com.phytal.sarona.internal.Event
import com.phytal.sarona.internal.lazyDeferred
import com.phytal.sarona.ui.base.CourseViewModel

class MpCourseViewModel(
    private val repository: CourseRepository,
    loginProvider: LoginProvider
) : CourseViewModel(loginProvider) {
    private var mp = -1
    private val _mpCourses = MutableLiveData<Event<MarkingPeriod?>>()
    val mpCourses: LiveData<Event<MarkingPeriod?>>
        get() = _mpCourses

    suspend fun fetchMpCourses() {
        _mpCourses.value = Event(repository.getMp(loginInformation, mp).value)
    }
//    val mpCourses by lazyDeferred {
//        repository.getMp(loginInformation, mp)
//    }

    fun setMp(mp: Int) {
        this.mp = mp
    }
}