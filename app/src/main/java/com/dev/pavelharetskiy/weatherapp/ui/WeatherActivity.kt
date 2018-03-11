package com.dev.pavelharetskiy.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.dev.pavelharetskiy.weatherapp.CITYNAMEKEY
import com.dev.pavelharetskiy.weatherapp.PATTERNDATE
import com.dev.pavelharetskiy.weatherapp.R
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.WeatherPresenter
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_weather.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : MvpAppCompatActivity(), IWeatherView {

    private var cityName: String = ""

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var weatherPresenter: WeatherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        swprfrshlt.setOnRefreshListener({ onSwipe() })
        swprfrshlt.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }

    @SuppressLint("SimpleDateFormat")
    private var sdf: SimpleDateFormat = SimpleDateFormat(PATTERNDATE)

    @SuppressLint("SetTextI18n")
    override fun showForecast(data: WeatherResponseModel) {
        tvCityName.text = data.name
        cityName = data.name
        tvAvTemp.text = "%.2f C".format(data.main.temp)
        tvDate.text = sdf.format(Date(data.dt))
        tvHum.text = data.main.humidity.toString() + "%"
        tvPresh.text = data.main.pressure.toString()
        tvMinMax.text = "%.2f C".format(data.main.tempMin) + " -  %.2f C".format(data.main.tempMax)
    }

    override fun swipeAnimFinish() {
        swprfrshlt.isRefreshing = false
    }

    override fun showToast(text: String, longToast: Boolean) {
        val lengts: Int = if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(this, text, lengts).show()
    }

    override fun setLog(logInfo: String) {
        tvlog.text = logInfo
    }

    override fun onStart() {
        super.onStart()
        weatherPresenter.textObserve(RxTextView.textChanges(edCity))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CITYNAMEKEY, cityName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(CITYNAMEKEY)) {
            cityName = savedInstanceState.getString(CITYNAMEKEY)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherPresenter.isAfterConfChanged = true
    }

    private fun onSwipe() {
        if (cityName.isNotEmpty()) {
            weatherPresenter.onClickLoadForecast(cityName)
        } else {
            swipeAnimFinish()
        }
    }
}
