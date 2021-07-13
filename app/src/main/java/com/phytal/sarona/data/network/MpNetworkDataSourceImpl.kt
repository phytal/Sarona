package com.phytal.sarona.data.network

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phytal.sarona.data.network.response.CurrentMpResponse
import com.phytal.sarona.data.network.response.MpResponse
import com.phytal.sarona.data.network.response.PastMpResponse
import com.phytal.sarona.internal.NoConnectivityException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MpNetworkDataSourceImpl(
    private val hacApiService: HacApiService
) : MpNetworkDataSource {

    private val _downloadedPastMps = MutableLiveData<PastMpResponse>()
    override val downloadedPastMps: LiveData<PastMpResponse>
        get() = _downloadedPastMps

    @SuppressLint("CheckResult")
    override suspend fun fetchPastMps(
        hacLink: String,
        username: String,
        password: String
    ) {
        try {
            hacApiService.getPastMps(hacLink, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _downloadedPastMps.postValue(result)
                },
                    { error -> Log.e("Error", "Could not fetch past marking periods", error) })
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private val _downloadedCurrentMp = MutableLiveData<CurrentMpResponse>()
    override val downloadedCurrentMp: LiveData<CurrentMpResponse>
        get() = _downloadedCurrentMp

    @SuppressLint("CheckResult")
    override suspend fun fetchCurrentMp(
        hacLink: String,
        username: String,
        password: String
    ) {
        try {
            hacApiService.getCurrentMp(hacLink, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _downloadedCurrentMp.postValue(result)
                },
                    { error -> Log.e("Error", "Could not fetch current marking period", error) })
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private val _downloadedMp = MutableLiveData<MpResponse>()
    override val downloadedMp: LiveData<MpResponse>
        get() = _downloadedMp

    @SuppressLint("CheckResult")
    override suspend fun fetchMp(
        hacLink: String,
        username: String,
        password: String,
        mp: Int
    ) {
        try {
            hacApiService.getMp(hacLink, username, password, mp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _downloadedMp.postValue(result)
                },
                    { error -> Log.e("Error", "Could not fetch current marking period", error) })
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}