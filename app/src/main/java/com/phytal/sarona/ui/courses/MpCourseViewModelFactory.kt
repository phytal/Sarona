package com.phytal.sarona.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository

class MpCourseViewModelFactory(
    private val repository: CourseRepository,
    private val loginProvider: LoginProvider
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MpCourseViewModel(repository, loginProvider) as T
    }
}