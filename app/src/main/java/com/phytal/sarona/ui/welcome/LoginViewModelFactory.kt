package com.phytal.sarona.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phytal.sarona.data.network.HacApiService
import com.phytal.sarona.data.repository.CourseRepository

class LoginViewModelFactory(
    private val hacApiService: HacApiService
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(hacApiService) as T
    }
}