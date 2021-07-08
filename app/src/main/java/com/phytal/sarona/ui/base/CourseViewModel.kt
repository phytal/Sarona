package com.phytal.sarona.ui.base

import androidx.lifecycle.ViewModel
import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.repository.CourseRepository

abstract class CourseViewModel(
    loginProvider: LoginProvider
) : ViewModel() {
    val loginInformation = loginProvider.getLoginInfo()
}