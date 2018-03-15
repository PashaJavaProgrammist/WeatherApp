package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.net.ConnectivityManager
import com.dev.pavelharetskiy.weatherapp.DISCONNECT
import com.dev.pavelharetskiy.weatherapp.ENTER_CITY
import com.dev.pavelharetskiy.weatherapp.ERROR
import com.dev.pavelharetskiy.weatherapp.FOUND
import com.dev.pavelharetskiy.weatherapp.iteractors.RestIteractor
import com.dev.pavelharetskiy.weatherapp.mvp.models.DateFormatsList
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Presenter (var connectivityManager: ConnectivityManager,
                 var restIteractor: RestIteractor,
                 dateFormatsList: DateFormatsList) : IPresenter<IWeatherView> {

    var weaterView: IWeatherView? = null

    override fun attachView(view: IWeatherView) {
        weaterView = view
        isViewAttached = true
        try {
            weaterView?.showForecast(weather)
        } catch (ex: Exception) {
            weaterView?.setLog(ENTER_CITY)
        }
    }

    override fun detachView() {
        weaterView = null
        isViewAttached = false
    }

    private lateinit var weather: WeatherResponseModel

    var dateFormat = dateFormatsList.listFormats[1]
    var timeFormat = dateFormatsList.listFormats[0]

    private lateinit var requestDisp: Disposable
    var cityName = ""
    private var isViewAttached = false

    fun onClickLoadForecast(city: String) {
        when {
            !isNetworkConnected() -> weaterView?.showToast(DISCONNECT, false)
            isNetworkConnected() -> loadWeather(city)
        }
        weaterView?.swipeAnimFinish()
    }

    fun loadWeather(city: String) {
        requestDisp = restIteractor.getCityWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (isViewAttached) {
                                weaterView?.showToast(it.name, true)
                                weaterView?.setLog(FOUND)
                                weaterView?.showForecast(it)
                            }
                            weather = it
                            requestDisp.dispose()
                        },
                        {
                            if (isViewAttached) {
                                weaterView?.setLog(ERROR)
                            }
                            requestDisp.dispose()
                        })
    }

    private fun isNetworkConnected() = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true

}