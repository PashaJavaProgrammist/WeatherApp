package com.dev.pavelharetskiy.weatherapp.mvp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WeatherResponseModel(@SerializedName("main")
                                @Expose
                                var main: Weather?,
                                @SerializedName("dt")
                                @Expose
                                var dt: Long?,
                                @SerializedName("cod")
                                @Expose
                                var cod: Int?,
                                @SerializedName("name")
                                @Expose
                                var name: String="")

data class Weather(
        @SerializedName("temp")
        @Expose
        var temp: Double?,
        @SerializedName("humidity")
        @Expose
        var humidity: Long?,
        @SerializedName("pressure")
        @Expose
        var pressure: Double?,
        @SerializedName("temp_min")
        @Expose
        var tempMin: Double?,
        @SerializedName("temp_max")
        @Expose
        var tempMax: Double?

)