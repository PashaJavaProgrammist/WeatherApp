package com.dev.pavelharetskiy.weatherapp.di.builders

import com.dev.pavelharetskiy.weatherapp.WeatherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): WeatherActivity
}