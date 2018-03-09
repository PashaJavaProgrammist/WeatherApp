package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.net.ConnectivityManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.pavelharetskiy.weatherapp.*
import com.dev.pavelharetskiy.weatherapp.App.Companion.daggerComponent
import com.dev.pavelharetskiy.weatherapp.iteractors.getCityWeather
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class WeatherPresenter : MvpPresenter<IWeatherView>() {

    init {
        daggerComponent.inject(this)
    }

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var textChanges: InitialValueObservable<CharSequence>

    private lateinit var d: Disposable
    private lateinit var textWatchDisposable: Disposable

    fun onClickLoadForecast(city: String) {
        if (isNetworkConnected()) {
            loadWeather(city)
        } else {
            viewState.showToast(DISCONNECT, false)
        }
        viewState.swipeAnimFinish()
    }

    private fun loadWeather(city: String) {
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
                            viewState.showToast(it.name, true)
                            viewState.setLog(FOUND)
                            viewState.showForecast(it)
                            d.dispose()
                        },
                        {
                            viewState.setLog("$NOTFOUND\n$it")
                            d.dispose()
                        })
    }

    private fun isNetworkConnected() = connectivityManager.activeNetworkInfo.isConnectedOrConnecting

    fun textObserve(textChanges: InitialValueObservable<CharSequence>) {
        this.textChanges = textChanges
        textWatchDisposable = textChanges
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .throttleWithTimeout(50, TimeUnit.MILLISECONDS)
                .subscribe(
                        {
                            if (it.isNotEmpty()) loadWeather(it.toString())
                        },
                        {
                            viewState.showToast("$TEXTWATCHERROR: $it", false)
                        })
    }

    fun disposeTextObserve() {
        try {
            textWatchDisposable.dispose()
        } catch (ex: ExceptionInInitializerError) {
            viewState.showToast(INITERROR, false)
        }
    }

}