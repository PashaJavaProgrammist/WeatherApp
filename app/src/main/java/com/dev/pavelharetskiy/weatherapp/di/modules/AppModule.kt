package com.dev.pavelharetskiy.weatherapp.di.modules

import com.dev.pavelharetskiy.weatherapp.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApplication(app: App) = app.applicationContext
}