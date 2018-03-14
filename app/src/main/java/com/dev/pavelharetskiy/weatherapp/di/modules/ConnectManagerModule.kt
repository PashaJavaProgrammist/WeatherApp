package com.dev.pavelharetskiy.weatherapp.di.modules

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConnectManagerModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(c: Context) = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}