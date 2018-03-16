package com.dev.pavelharetskiy.weatherapp.di.components

import com.dev.pavelharetskiy.weatherapp.App
import com.dev.pavelharetskiy.weatherapp.di.modules.*
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.Presenter
import com.dev.pavelharetskiy.weatherapp.ui.WeatherActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (MainModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

    fun inject(activity: WeatherActivity)
}