package com.dev.pavelharetskiy.weatherapp.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.dev.pavelharetskiy.weatherapp.App
import com.dev.pavelharetskiy.weatherapp.R
import com.dev.pavelharetskiy.weatherapp.iteractors.RestIteractor
import com.dev.pavelharetskiy.weatherapp.mvp.models.WeatherResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class WeatherWidget : AppWidgetProvider() {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var restIteractor: RestIteractor

    var updateInProgress = false

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for ((i, _) in appWidgetIds.withIndex()) {
            val appWidgetId = appWidgetIds[i]

            val widgetView = RemoteViews(context.packageName, R.layout.widget)

            //Widget update
            val updateIntent = Intent(context, WeatherWidget::class.java)
            updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))

            val pIntent = PendingIntent.getBroadcast(context, appWidgetId, updateIntent, 0)
            widgetView.setOnClickPendingIntent(R.id.wd_add, pIntent)

            //ProgressBar visibility
            widgetView.setViewVisibility(R.id.progress, View.VISIBLE)

            if (!updateInProgress) {
                updateInProgress = true
                restIteractor.getCityWeather(context.getString(R.string.minsk)).enqueue(object : Callback<WeatherResponseModel> {

                    override fun onFailure(call: Call<WeatherResponseModel>?, t: Throwable?) {
                        widgetView.setTextViewText(R.id.city, context.getString(R.string.some))
                        widgetView.setTextViewText(R.id.degrees, context.getString(R.string.error))
                        widgetView.setViewVisibility(R.id.progress, View.GONE)
                        appWidgetManager.updateAppWidget(appWidgetId, widgetView)
                        updateInProgress = false
                    }

                    override fun onResponse(call: Call<WeatherResponseModel>?, response: Response<WeatherResponseModel>?) {
                        when (response?.code()) {
                            200 -> {
                                widgetView.setTextViewText(R.id.city, response.body()?.name)
                                widgetView.setTextViewText(R.id.degrees, "${response.body()?.main?.temp} °C")
                            }
                            else -> {
                                widgetView.setTextViewText(R.id.city, context.getString(R.string.please))
                                widgetView.setTextViewText(R.id.degrees, context.getString(R.string.refresh))
                            }
                        }
                        widgetView.setViewVisibility(R.id.progress, View.GONE)
                        appWidgetManager.updateAppWidget(appWidgetId, widgetView)
                        updateInProgress = false
                    }
                })
            }
            appWidgetManager.updateAppWidget(appWidgetId, widgetView)
        }
    }
}