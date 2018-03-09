package com.dev.pavelharetskiy.weatherapp.di.modules

import android.content.Context
import android.net.ConnectivityManager
import com.dev.pavelharetskiy.weatherapp.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApplication(app: App): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideConnectivityManager(c: Context) = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}