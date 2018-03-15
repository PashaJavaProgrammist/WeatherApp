package com.dev.pavelharetskiy.weatherapp.mvp.views

import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel


interface IWeatherView {

    fun showForecast(data: WeatherResponseModel)

    fun swipeAnimFinish()

    fun showToast(text: String, longToast: Boolean)

    fun setLog(logInfo: String)
}