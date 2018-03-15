package com.dev.pavelharetskiy.weatherapp.di.modules

import android.net.ConnectivityManager
import com.dev.pavelharetskiy.weatherapp.iteractors.RestIteractor
import com.dev.pavelharetskiy.weatherapp.mvp.models.DateFormatsList
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.Presenter
import com.dev.pavelharetskiy.weatherapp.rest.RestService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun providePresenter(connectivityManager: ConnectivityManager,
                           restIteractor: RestIteractor,
                           dateFormatsList: DateFormatsList) = Presenter(connectivityManager, restIteractor, dateFormatsList)
}