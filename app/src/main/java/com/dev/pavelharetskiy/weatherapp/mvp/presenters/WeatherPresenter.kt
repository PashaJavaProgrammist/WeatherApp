package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.net.ConnectivityManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.pavelharetskiy.weatherapp.App.Companion.daggerComponent
import com.dev.pavelharetskiy.weatherapp.DISCONNECT
import com.dev.pavelharetskiy.weatherapp.ENTER_CITY
import com.dev.pavelharetskiy.weatherapp.ERROR
import com.dev.pavelharetskiy.weatherapp.FOUND
import com.dev.pavelharetskiy.weatherapp.iteractors.RestIteractor
import com.dev.pavelharetskiy.weatherapp.mvp.models.DateFormatsList
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

@InjectViewState
class WeatherPresenter : MvpPresenter<IWeatherView>() {

    init {
        daggerComponent.inject(this)
    }

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var restIteractor: RestIteractor

    @Inject
    lateinit var dateFormatsList: DateFormatsList

    private lateinit var weather: WeatherResponseModel

    var dateFormat = dateFormatsList.listFormats[1]
    var timeFormat = dateFormatsList.listFormats[0]

    var cityName = ""
    private var isViewAttached = false

    fun onClickLoadForecast(city: String) {
        when {
            !isNetworkConnected() -> viewState.showToast(DISCONNECT, false)
            isNetworkConnected() -> loadWeather(city)
        }
        viewState.swipeAnimFinish()
    }

    fun loadWeather(city: String) {
        //Coroutine
        launch(CommonPool) {
            val response = async {
                restIteractor.getCityWeather(city).execute()
            }
            val weatherResponseModel = response.await()
            launch(UI) {
                if (isViewAttached) {
                    when (weatherResponseModel.code()) {
                        200 -> weatherResponseModel.body()?.also {
                            viewState.showToast(it.name, true)
                            viewState.setLog(FOUND)
                            viewState.showForecast(it)
                            weather = it
                        }
                        else -> viewState.setLog(ERROR)
                    }
                }
            }
        }
    }

    private fun isNetworkConnected() = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true

    override fun attachView(view: IWeatherView?) {
        super.attachView(view)
        isViewAttached = true
        try {
            viewState.showForecast(weather)
        } catch (ex: Exception) {
            viewState.setLog(ENTER_CITY)
        }
    }

    override fun detachView(view: IWeatherView?) {
        super.detachView(view)
        isViewAttached = false
    }

}