package com.dev.pavelharetskiy.weatherapp.mvp.presenters

import android.content.Context
import android.net.ConnectivityManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.pavelharetskiy.weatherapp.*
import com.dev.pavelharetskiy.weatherapp.App.Companion.daggerComponent
import com.dev.pavelharetskiy.weatherapp.iteractors.RestIteractor
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
//import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
//import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class WeatherPresenter : MvpPresenter<IWeatherView>() {

    init {
        daggerComponent.inject(this)
    }

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var restIteractor: RestIteractor

    private lateinit var weather: WeatherResponseModel

    val dateFormat: DateFormat
        get() = android.text.format.DateFormat.getDateFormat(context)
    val timeFormat: DateFormat
        get() = android.text.format.DateFormat.getTimeFormat(context)

    private lateinit var requestDisp: Disposable
    var cityName = ""
    private var isViewAttached = false

    //    private lateinit var textChanges: InitialValueObservable<CharSequence>
    //var isAfterConfChanged: Boolean = false
    //    private lateinit var textWatchDisposable: Disposable

    fun onClickLoadForecast(city: String) {
        when {
            !isNetworkConnected() -> viewState.showToast(DISCONNECT, false)
            isNetworkConnected() -> loadWeather(city)
        }
        viewState.swipeAnimFinish()
    }

    fun loadWeather(city: String) {
        requestDisp = restIteractor.getCityWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (isViewAttached) {
                                viewState.showToast(it.name, true)
                                viewState.setLog(FOUND)
                                viewState.showForecast(it)
                            }
                            weather = it
                            requestDisp.dispose()
                        },
                        {
                            if (isViewAttached) {
                                viewState.setLog(ERROR)
                            }
                            requestDisp.dispose()
                        })
    }

    private fun isNetworkConnected() = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true

    override fun attachView(view: IWeatherView?) {
        super.attachView(view)
        isViewAttached = true
        try {
            viewState.showForecast(weather)
        } catch (ex: Exception) {
            viewState.showToast(ENTER_CITY, false)
        }
    }

    override fun detachView(view: IWeatherView?) {
        super.detachView(view)
        isViewAttached = false
//        textWatchDisposable.dispose()
    }

    //        Not mvp
    /*fun textObserve(textChanges: InitialValueObservable<CharSequence>) {
        this.textChanges = textChanges
        textWatchDisposable = textChanges
                .subscribeOn(Schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            when {
                                isAfterConfChanged -> isAfterConfChanged = false
                                !isNetworkConnected() -> viewState.showToast(DISCONNECT, false)
                                it.isNotEmpty() -> loadWeather(it.toString())
                            }
                        },
                        {
                            viewState.showToast("$TEXT_WATCH_ERROR: $it", true)
                        })
    }*/
}