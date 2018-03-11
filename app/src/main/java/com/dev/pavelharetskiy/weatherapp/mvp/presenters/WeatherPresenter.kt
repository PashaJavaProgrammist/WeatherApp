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

    private lateinit var requestDisp: Disposable
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
        requestDisp = getCityWeather(city)
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
                            requestDisp.dispose()
                        },
                        {
                            viewState.setLog(ERROR)
                            requestDisp.dispose()
                        })
    }

    private fun isNetworkConnected() = connectivityManager.activeNetworkInfo.isConnectedOrConnecting

    fun textObserve(textChanges: InitialValueObservable<CharSequence>) {
        this.textChanges = textChanges
        textWatchDisposable = textChanges
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                //.skip(1)
                //.throttleWithTimeout(300, TimeUnit.MILLISECONDS)
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it.isNotEmpty()) loadWeather(it.toString())
                        },
                        {
                            viewState.showToast("$TEXTWATCHERROR: $it", false)
                        })
    }

    override fun detachView(view: IWeatherView?) {
        super.detachView(view)
        textWatchDisposable.dispose()
        try {
            requestDisp.dispose()
        } catch (ex: Exception) {
            viewState.setLog(ERROR)
        }
    }

}