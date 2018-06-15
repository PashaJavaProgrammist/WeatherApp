package com.dev.pavelharetskiy.weatherapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.dev.pavelharetskiy.weatherapp.R
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import com.dev.pavelharetskiy.weatherapp.mvp.presenters.WeatherPresenter
import com.dev.pavelharetskiy.weatherapp.mvp.views.IWeatherView
//import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*

class WeatherActivity : MvpAppCompatActivity(), IWeatherView {

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

    override fun showForecast(data: WeatherResponseModel) {
        launch(CommonPool) {
            weatherPresenter.cityName = data.name
            val date = weatherPresenter.dateFormat.format(Date(data.dt * 1000)).plus(getString(R.string.space)).plus(weatherPresenter.timeFormat.format(Date(data.dt * 1000)))
            val hum = getString(R.string.humidity).plus(getString(R.string.two_dotes)).plus(data.main.humidity).plus(getString(R.string.procents))
            val pressure = getString(R.string.pres).plus(data.main.pressure.toString()).plus(getString(R.string.hPA))
            val temp = getString(R.string.temp).plus(getString(R.string.format_two_num).format(data.main.temp))
            launch(UI) {
                tvCityName.text = data.name
                tvAvTemp.text = temp
                tvDate.text = date
                tvHum.text = hum
                tvPresh.text = pressure
            }
        }
    }

    override fun swipeAnimFinish() {
        swprfrshlt.isRefreshing = false
    }

    override fun showToast(text: String, longToast: Boolean) {
        val length: Int = if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(this, text, length).show()
    }

    override fun setLog(logInfo: String) {
        tvlog.text = logInfo
    }

    override fun onResume() {
        super.onResume()

        edCity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(city: Editable) {
                weatherPresenter.loadWeather(city.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun onSwipe() {
        if (weatherPresenter.cityName.isNotEmpty()) {
            weatherPresenter.onClickLoadForecast(weatherPresenter.cityName)
        } else {
            swipeAnimFinish()
        }
    }
}
