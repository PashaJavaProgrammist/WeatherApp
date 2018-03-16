package com.dev.pavelharetskiy.weatherapp.mvp.presenters

interface IPresenter<in T> {

    fun attachView(view: T)
    fun detachView()

}