package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.content.Context
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.pavelharetskiy.weatherapp.APIKEY
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import com.dev.pavelharetskiy.weatherapp.rest.getRestApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class WeatherPresenter : MvpPresenter<IWeatherView>() {

    lateinit var d: Disposable

//    @Inject
//    lateinit var context: Context

    fun onClickLoadForecast(context: Context, city: String) {
        d = getRestApi().loadCityWeather(city, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            // Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                            refactoringWeatherData(it)
                            viewState.showForecast(it)
                            viewState.swipeAnimFinish()
                            d.dispose()
                        },
                        {
                            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                            d.dispose()
                        })
    }

    private fun refactoringWeatherData(w: WeatherResponseModel) {
        w.main.temp = w.main.temp - 273
        w.main.tempMin = w.main.tempMin - 273
        w.main.tempMax = w.main.tempMax - 273
        w.dt = w.dt * 1000L
    }

}