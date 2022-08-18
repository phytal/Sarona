package com.phytal.sarona.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository

class CurrentCourseViewModelFactory(
    private val repository: CourseRepository,
    private val loginProvider: LoginProvider
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentCourseViewModel(repository, loginProvider) as T
    }
}