package com.phytal.sarona.data.network

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.network.response.CurrentMpResponse
import com.phytal.sarona.data.network.response.PastMpResponse

interface MpNetworkDataSource {
    val downloadedPastMps: LiveData<PastMpResponse>
    val downloadedCurrentMp: LiveData<CurrentMpResponse>
    val validLogin: LiveData<Boolean>

    suspend fun fetchCurrentMp(
        hacLink: String,
        username: String,
        password: String
    )
    suspend fun fetchPastMps(
        hacLink: String,
        username: String,
        password: String
    )

    suspend fun isValidLogin(
        hacLink: String,
        username: String,
        password: String
    )
}