package com.phytal.sarona.requests

import com.phytal.sarona.models.Course
//import com.phytal.sarona.models.CourseList
import com.phytal.sarona.models.Model
import io.reactivex.Observable
import io.reactivex.Single
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
            Observable<Model.Result>

    companion object {
        fun create(): HacApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://hac-web-api-production.herokuapp.com/")
                .build()

            return retrofit.create(HacApiService::class.java)
        }
    }
}