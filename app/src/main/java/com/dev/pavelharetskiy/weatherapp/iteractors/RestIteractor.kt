package com.dev.pavelharetskiy.weatherapp.iteractors

import com.dev.pavelharetskiy.weatherapp.APIKEY
import com.dev.pavelharetskiy.weatherapp.rest.getRestApi


fun getCityWeather(city: String) =
        getRestApi().loadCityWeather(city, APIKEY)
