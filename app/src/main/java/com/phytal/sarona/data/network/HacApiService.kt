package com.phytal.sarona.data.network

import com.phytal.sarona.data.network.adapter.NetworkResponseAdapterFactory
import com.phytal.sarona.data.network.response.CurrentMpResponse
import com.phytal.sarona.data.network.response.LoginResponse
import com.phytal.sarona.data.network.response.MpResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface HacApiService {

    @GET("currentMp")
    fun getCurrentMp(
        @Query("l") hacLink: String,
        @Query("u") username: String,
        @Query("p") password: String
    ): Observable<CurrentMpResponse>

    @GET("mp")
    fun getMp(
        @Query("l") hacLink: String,
        @Query("u") username: String,
        @Query("p") password: String,
        @Query("mp") mp: Int
    ): Observable<MpResponse>

    @GET("login")
    fun getValidLogin(
        @Query("l") hacLink: String,
        @Query("u") username: String,
        @Query("p") password: String
    ): Observable<LoginResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): HacApiService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val rxAdapter: RxJava2CallAdapterFactory =
                RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
            return Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl("https://flask-hac-api.herokuapp.com/")
                .build()
                .create(HacApiService::class.java)
        }
    }
}