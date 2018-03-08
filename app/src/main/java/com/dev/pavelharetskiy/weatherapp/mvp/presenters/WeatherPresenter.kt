package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.content.Context
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.pavelharetskiy.weatherapp.APIKEY
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
                            viewState.showForecast(it)
                            viewState.swipeAnimFinish()
                            d.dispose()
                        },
                        {
                            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                            d.dispose()
                        })
    }

}