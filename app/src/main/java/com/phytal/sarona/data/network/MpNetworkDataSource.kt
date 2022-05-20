package com.phytal.sarona.data.network

import androidx.lifecycle.LiveData
import com.phytal.sarona.data.network.response.CurrentMpResponse
import com.phytal.sarona.data.network.response.MpResponse

interface MpNetworkDataSource {
    val downloadedCurrentMp: LiveData<CurrentMpResponse>
    val downloadedMp: LiveData<MpResponse>

    suspend fun fetchCurrentMp(
        hacLink: String,
        username: String,
        password: String
    )

    suspend fun fetchMp(
        hacLink: String,
        username: String,
        password: String,
        mp: Int
    )
}