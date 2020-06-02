package com.phytal.sarona.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phytal.sarona.data.network.response.CourseResponse
import com.phytal.sarona.internal.NoConnectivityException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class CourseNetworkDataSourceImpl(
    private val hacApiService: HacApiService
) : CourseNetworkDataSource {

    private val _downloadedCurrentCourse = MutableLiveData<CourseResponse>()
    override val downloadedCurrentCourse: LiveData<CourseResponse>
        get() = _downloadedCurrentCourse

    override suspend fun fetchCurrent(hacLink: String, username: String, password: String) {
        try {
           hacApiService.getAllCourses(hacLink, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ result -> _downloadedCurrentCourse.postValue(result)},
                {error -> Log.e("Error", "Could not fetch current courses", error)})
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}