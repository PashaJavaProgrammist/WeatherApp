package com.dev.pavelharetskiy.weatherapp.di.modules

import android.content.Context
import android.net.ConnectivityManager
import com.dev.pavelharetskiy.weatherapp.iteractors.RestIteractor
import com.dev.pavelharetskiy.weatherapp.mvp.models.DateFormatsList
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.Presenter
import com.dev.pavelharetskiy.weatherapp.rest.RestService
import dagger.Module
import dagger.Provides
import java.text.DateFormat
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideRestService() = RestService()

    @Provides
    @Singleton
    fun provideRestIteractor(restService: RestService) = RestIteractor(restService)

    @Provides
    @Singleton
    fun provideTimeModule(context: Context): DateFormatsList {
        val list: ArrayList<DateFormat> = ArrayList()
        list.add(android.text.format.DateFormat.getTimeFormat(context))
        list.add(android.text.format.DateFormat.getDateFormat(context))
        return DateFormatsList(list)
    }

    @Provides
    @Singleton
    fun providePresenter(connectivityManager: ConnectivityManager,
                         restIteractor: RestIteractor,
                         dateFormatsList: DateFormatsList) = Presenter(connectivityManager, restIteractor, dateFormatsList)

    @Provides
    @Singleton
    fun provideConnectivityManager(c: Context) = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}