package com.phytal.sarona.data.network

import com.phytal.sarona.data.network.adapter.NetworkResponseAdapterFactory
import com.phytal.sarona.data.network.response.CourseResponse
//import com.phytal.sarona.models.CourseList
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface HacApiService {

    @GET("api/hac")
    fun getAllCourses(@Query("hacLink") hacLink: String,
                      @Query("username") username: String,
                      @Query("password") password: String):
            Observable<CourseResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): HacApiService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            val rxAdapter: RxJava2CallAdapterFactory =
                RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
            return Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://hac-web-api-production.herokuapp.com/")
                .build()
                .create(HacApiService::class.java)
        }
    }
}