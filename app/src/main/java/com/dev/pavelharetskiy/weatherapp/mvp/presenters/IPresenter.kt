package com.dev.pavelharetskiy.weatherapp.mvp.presenters

interface IPresenter<IWeatherView> {

    fun attachView(view: IWeatherView)
    fun detachView()

}