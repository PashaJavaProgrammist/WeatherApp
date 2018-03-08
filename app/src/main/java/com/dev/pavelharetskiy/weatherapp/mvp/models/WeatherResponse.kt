package com.dev.pavelharetskiy.weatherapp.mvp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WeatherResponseModel(@SerializedName("main")
                                @Expose
                                var main: Weather,
                                @SerializedName("dt")
                                @Expose
                                var dt: Long=0L,
                                @SerializedName("cod")
                                @Expose
                                var cod: Int?,
                                @SerializedName("name")
                                @Expose
                                var name: String="")

data class Weather(
        @SerializedName("temp")
        @Expose
        var temp: Double=0.0,
        @SerializedName("humidity")
        @Expose
        var humidity: Long=0L,
        @SerializedName("pressure")
        @Expose
        var pressure: Double=0.0,
        @SerializedName("temp_min")
        @Expose
        var tempMin: Double=0.0,
        @SerializedName("temp_max")
        @Expose
        var tempMax: Double=0.0

)