package com.dev.pavelharetskiy.weatherapp.di.modules

import com.dev.pavelharetskiy.weatherapp.iteractors.RestIteractor
import com.dev.pavelharetskiy.weatherapp.rest.RestService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RestModule {

    @Provides
    @Singleton
    fun provideRestService() = RestService()

    @Provides
    @Singleton
    fun provideRestIteractor(restService: RestService) = RestIteractor(restService)
}
