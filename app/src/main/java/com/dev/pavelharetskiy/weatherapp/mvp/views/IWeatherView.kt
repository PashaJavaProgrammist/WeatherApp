package com.dev.pavelharetskiy.weatherapp.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel

@StateStrategyType(AddToEndSingleStrategy::class)
interface IWeatherView : MvpView {

    fun showForecast(data: WeatherResponseModel)
    fun swipeAnimFinish()
    fun showToast(text: String, longToast: Boolean)
    fun setLog(logInfo: String)
}