package com.dev.pavelharetskiy.weatherapp.rest

import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface IRestService {

    @GET("weather")
    fun loadCityWeather(@Query("q") city: String,
                        @Query("appid") apikey: String,
                        @Query("units") units: String): Call<WeatherResponseModel>
}