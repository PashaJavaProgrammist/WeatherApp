package com.dev.pavelharetskiy.weatherapp.di.modules

import android.content.Context
import com.dev.pavelharetskiy.weatherapp.App
import dagger.Binds
import dagger.Module


@Module
interface AppModule {

    @Binds
    fun provideApplication(app: App): Context

}