package com.phytal.sarona.ui.courses

import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository
import com.phytal.sarona.internal.lazyDeferred
import com.phytal.sarona.ui.base.CourseViewModel

class PastCourseViewModel(
    private val repository: CourseRepository,
    loginProvider: LoginProvider
) : CourseViewModel(loginProvider) {
    val pastCourses by lazyDeferred {
        repository.getPastMps(loginInformation)
    }
}