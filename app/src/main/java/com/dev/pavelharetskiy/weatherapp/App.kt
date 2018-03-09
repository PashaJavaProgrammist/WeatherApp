package com.dev.pavelharetskiy.weatherapp

import android.app.Activity
import android.app.Application
import com.dev.pavelharetskiy.weatherapp.di.components.AppComponent
import com.dev.pavelharetskiy.weatherapp.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    companion object {
        lateinit var daggerComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerAppComponent.builder().application(this).build()
        daggerComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}