package com.phytal.sarona.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository

class PastCourseViewModelFactory(
    private val repository: CourseRepository,
    private val loginProvider: LoginProvider
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PastCourseViewModel(repository, loginProvider) as T
    }
}