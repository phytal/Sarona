package com.phytal.sarona.ui.welcome

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phytal.sarona.data.network.HacApiService
import com.phytal.sarona.data.provider.LoginInformation
import com.phytal.sarona.data.repository.CourseRepository
import com.phytal.sarona.internal.Event
import com.phytal.sarona.internal.NoConnectivityException
import com.phytal.sarona.internal.lazyDeferred
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

class LoginViewModel(
    private val hacApiService: HacApiService
) : ViewModel() {
    private val _validLogin = MutableLiveData<Event<Boolean>>()
    val validLogin: LiveData<Event<Boolean>>
        get() = _validLogin

    @SuppressLint("CheckResult")
    fun fetchValidLogin(
        hacLink: String,
        username: String,
        password: String
    ) {
        try {
            hacApiService.getValidLogin(hacLink, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _validLogin.value = Event(result.msg == "Accepted")
                },
                    { error -> Log.e("Error", "Could not validate login information", error) })
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}