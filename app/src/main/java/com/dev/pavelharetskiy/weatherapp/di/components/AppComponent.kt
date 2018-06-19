package com.dev.pavelharetskiy.weatherapp.di.components

import com.dev.pavelharetskiy.weatherapp.App
import com.dev.pavelharetskiy.weatherapp.di.modules.AppModule
import com.dev.pavelharetskiy.weatherapp.di.modules.ConnectManagerModule
import com.dev.pavelharetskiy.weatherapp.di.modules.DateFormatModule
import com.dev.pavelharetskiy.weatherapp.di.modules.RestModule
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.WeatherPresenter
import com.dev.pavelharetskiy.weatherapp.providers.WeatherWidget
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (ConnectManagerModule::class), (RestModule::class), (DateFormatModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(wp: WeatherPresenter)

    fun inject(app: App)
    fun inject(weatherWidget: WeatherWidget)
}