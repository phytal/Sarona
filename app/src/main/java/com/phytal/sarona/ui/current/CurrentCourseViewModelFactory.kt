package com.phytal.sarona.ui.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phytal.sarona.data.repository.CourseRepository

class CurrentCourseViewModelFactory(
    private val repository: CourseRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CourseViewModel(repository) as T
    }
}