package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.content.Context
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.pavelharetskiy.weatherapp.App.Companion.daggerComponent
import com.dev.pavelharetskiy.weatherapp.iteractors.getCityWeather
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import android.net.ConnectivityManager
import com.dev.pavelharetskiy.weatherapp.DISCONNECT

@InjectViewState
class WeatherPresenter : MvpPresenter<IWeatherView>() {
    init {
        daggerComponent.inject(this)
    }

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var d: Disposable

    private val context: Context = daggerComponent.context

    fun onClickLoadForecast(city: String) {
        if (isNetworkConnected()) {
            d = getCityWeather(city)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                                refactoringWeatherData(it)
                                viewState.showForecast(it)
                                d.dispose()
                            },
                            {
                                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                                d.dispose()
                            })
        } else {
            Toast.makeText(context, DISCONNECT, Toast.LENGTH_SHORT).show()
        }
        viewState.swipeAnimFinish()
    }

    private fun refactoringWeatherData(w: WeatherResponseModel) {
        w.main.temp = w.main.temp - 273
        w.main.tempMin = w.main.tempMin - 273
        w.main.tempMax = w.main.tempMax - 273
        w.dt = w.dt * 1000L
    }

    private fun isNetworkConnected(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo.isConnected
    }
}