package com.dev.pavelharetskiy.weatherapp.di.modules

import android.content.Context
import com.dev.pavelharetskiy.weatherapp.mvp.models.DateFormatsList
import dagger.Module
import dagger.Provides
import java.text.DateFormat
import javax.inject.Singleton

@Module
class DateFormatModule {

    @Provides
    @Singleton
    fun provideTimeModule(context: Context): DateFormatsList {
        val list: ArrayList<DateFormat> = ArrayList()
        list.add(android.text.format.DateFormat.getTimeFormat(context))
        list.add(android.text.format.DateFormat.getDateFormat(context))
        return DateFormatsList(list)
    }
}