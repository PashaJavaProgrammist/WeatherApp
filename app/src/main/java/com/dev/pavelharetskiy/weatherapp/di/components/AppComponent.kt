package com.dev.pavelharetskiy.weatherapp.di.components

import com.dev.pavelharetskiy.weatherapp.App
import com.dev.pavelharetskiy.weatherapp.di.modules.AppModule
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.WeatherPresenter
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(wp: WeatherPresenter)

    fun inject(app: App)
}