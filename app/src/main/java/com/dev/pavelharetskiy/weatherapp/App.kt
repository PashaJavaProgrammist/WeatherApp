package com.dev.pavelharetskiy.weatherapp

import android.app.Application
import com.dev.pavelharetskiy.weatherapp.di.components.AppComponent
import com.dev.pavelharetskiy.weatherapp.di.components.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var daggerComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerAppComponent.builder().application(this).build()
        daggerComponent.inject(this)
    }
}