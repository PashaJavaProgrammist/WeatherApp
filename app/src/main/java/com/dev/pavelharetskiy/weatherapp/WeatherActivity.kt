package com.dev.pavelharetskiy.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.WeatherPresenter
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
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

        btShow.setOnClickListener({ onClickShow() })
        swprfrshlt.setOnRefreshListener({ onSwipe() })
        swprfrshlt.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }

    @SuppressLint("SimpleDateFormat")
    private var sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")

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

    private fun onClickShow() {
        weatherPresenter.onClickLoadForecast(this.applicationContext, edCity.text.toString())
    }

    private fun onSwipe() {
        if (cityName.isNotEmpty()) {
            weatherPresenter.onClickLoadForecast(this.applicationContext, cityName)
        } else {
            swipeAnimFinish()
        }
    }
}
