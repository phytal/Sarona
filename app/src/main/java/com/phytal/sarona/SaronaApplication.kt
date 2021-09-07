package com.phytal.sarona

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.phytal.sarona.data.db.MpDatabase
import com.phytal.sarona.data.network.*
import com.phytal.sarona.data.provider.LoginProvider
import com.phytal.sarona.data.provider.LoginProviderImpl
import com.phytal.sarona.data.repository.CourseRepository
import com.phytal.sarona.data.repository.CourseRepositoryImpl
import com.phytal.sarona.internal.helpers.ThemeHelper
import com.phytal.sarona.internal.helpers.ThemeHelper.applyTheme
import com.phytal.sarona.ui.courses.CurrentCourseViewModelFactory
import com.phytal.sarona.ui.courses.MpCourseViewModelFactory
import com.phytal.sarona.ui.courses.OtherCourseViewModelFactory
import com.phytal.sarona.ui.welcome.LoginViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class SaronaApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SaronaApplication))

        bind() from singleton { MpDatabase.getInstance(this@SaronaApplication) }
        bind() from singleton { instance<MpDatabase>().mpDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { HacApiService(instance()) }
        bind<MpNetworkDataSource>() with singleton { MpNetworkDataSourceImpl(instance()) }
        bind<CourseRepository>() with singleton { CourseRepositoryImpl(instance(), instance()) }
        bind<LoginProvider>() with singleton { LoginProviderImpl(instance()) }
        bind() from provider { CurrentCourseViewModelFactory(instance(), instance()) }
        bind() from provider { OtherCourseViewModelFactory(instance(), instance()) }
        bind() from provider { MpCourseViewModelFactory(instance(), instance()) }
        bind() from provider { LoginViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        val sharedPreferences = this.getSharedPreferences(
            getString(R.string.user_preference_file_key),
            Context.MODE_PRIVATE
        )
        val themePref =
            sharedPreferences.getString(
                getString(R.string.saved_theme_key),
                ThemeHelper.DEFAULT_MODE
            )
        applyTheme(themePref!!)
    }
}