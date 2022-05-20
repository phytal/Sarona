package com.phytal.sarona.ui.courses

import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository
import com.phytal.sarona.internal.lazyDeferred
import com.phytal.sarona.ui.base.CourseViewModel

class MpCourseViewModel(
    private val repository: CourseRepository,
    loginProvider: LoginProvider
) : CourseViewModel(loginProvider) {
    private var mp = 0

    val mpCourses by lazyDeferred {
        repository.getMp(loginInformation, mp)
    }

    fun setMp(mp: Int) {
        this.mp = mp-1
    }
}