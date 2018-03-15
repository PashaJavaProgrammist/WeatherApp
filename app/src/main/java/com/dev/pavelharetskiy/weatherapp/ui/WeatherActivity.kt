package com.dev.pavelharetskiy.weatherapp.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.dev.pavelharetskiy.weatherapp.App.Companion.daggerComponent
import com.dev.pavelharetskiy.weatherapp.R
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.Presenter
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
import kotlinx.android.synthetic.main.activity_weather.*
import java.util.*
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(), IWeatherView {

    @Inject
    lateinit var weatherPresenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        daggerComponent.inject(this)

        setContentView(R.layout.activity_weather)

        swprfrshlt.setOnRefreshListener({ onSwipe() })
        swprfrshlt.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }

    override fun showForecast(data: WeatherResponseModel) {
        weatherPresenter.cityName = data.name
        tvCityName.text = data.name
        tvAvTemp.text = getString(R.string.temp).plus(getString(R.string.format_two_num).format(data.main.temp))
        tvDate.text = weatherPresenter.dateFormat.format(Date(data.dt * 1000)).plus(getString(R.string.space)).plus(weatherPresenter.timeFormat.format(Date(data.dt * 1000)))
        tvHum.text = getString(R.string.humidity).plus(getString(R.string.two_dotes)).plus(data.main.humidity).plus(getString(R.string.procents))
        tvPresh.text = getString(R.string.pres).plus(data.main.pressure.toString()).plus(getString(R.string.hPA))
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

    override fun onResume() {
        super.onResume()

        weatherPresenter.attachView(this)
        edCity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(city: Editable) {
                weatherPresenter.loadWeather(city.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onPause() {
        super.onPause()
        weatherPresenter.detachView()
    }

    private fun onSwipe() {
        if (weatherPresenter.cityName.isNotEmpty()) {
            weatherPresenter.onClickLoadForecast(weatherPresenter.cityName)
        } else {
            swipeAnimFinish()
        }
    }
}
