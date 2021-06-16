package com.phytal.sarona.ui.current

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository
import com.phytal.sarona.internal.lazyDeferred


class CourseViewModel(
    private val repository: CourseRepository,
    loginProvider: LoginProvider
) : ViewModel() {
    private val loginInformation = loginProvider.getLoginInfo()

    val courses by lazyDeferred {
        repository.getCurrentCourses(loginInformation)
    }
}