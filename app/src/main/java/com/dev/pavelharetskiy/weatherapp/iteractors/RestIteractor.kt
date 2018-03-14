package com.dev.pavelharetskiy.weatherapp.iteractors

import com.dev.pavelharetskiy.weatherapp.API_KEY
import com.dev.pavelharetskiy.weatherapp.UNITS
import com.dev.pavelharetskiy.weatherapp.rest.RestService

class RestIteractor(var restService: RestService) {

    fun getCityWeather(city: String) =
            restService.getRestApi().loadCityWeather(city, API_KEY, UNITS)
}