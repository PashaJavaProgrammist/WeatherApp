package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.net.ConnectivityManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.pavelharetskiy.weatherapp.App.Companion.daggerComponent
import com.dev.pavelharetskiy.weatherapp.DISCONNECT
import com.dev.pavelharetskiy.weatherapp.iteractors.getCityWeather
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class WeatherPresenter : MvpPresenter<IWeatherView>() {

    init {
        daggerComponent.inject(this)
    }

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var d: Disposable

    fun onClickLoadForecast(city: String) {
        if (isNetworkConnected()) {
            d = getCityWeather(city)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .map(
                            {
                                it.main.temp = it.main.temp - 273
                                it.main.tempMin = it.main.tempMin - 273
                                it.main.tempMax = it.main.tempMax - 273
                                it.dt = it.dt * 1000L
                                it
                            }
                    )
                    .subscribe(
                            {
                                viewState.showToast(it.toString(), true)
                                viewState.showForecast(it)
                                d.dispose()
                            },
                            {
                                viewState.showToast(it.toString(), false)
                                d.dispose()
                            })
        } else {
            viewState.showToast(DISCONNECT, false)
        }
        viewState.swipeAnimFinish()
    }

    private fun isNetworkConnected(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}