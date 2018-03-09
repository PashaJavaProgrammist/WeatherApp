package com.dev.pavelharetskiy.weatherapp.di.components

import com.dev.pavelharetskiy.weatherapp.App
import com.dev.pavelharetskiy.weatherapp.di.builders.ActivityBuilder
import com.dev.pavelharetskiy.weatherapp.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, AndroidInjectionModule::class, ActivityBuilder::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}