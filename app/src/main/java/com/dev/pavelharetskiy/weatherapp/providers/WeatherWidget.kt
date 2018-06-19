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
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class WeatherWidget : AppWidgetProvider() {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var restIteractor: RestIteractor

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for ((i, _) in appWidgetIds.withIndex()) {
            val appWidgetId = appWidgetIds[i]

            val widgetView = RemoteViews(context.packageName, R.layout.widget)

            //ProgressBar Visibility
            widgetView.setViewVisibility(R.id.progress, View.VISIBLE)

            //Widget update
            val updateIntent = Intent(context, WeatherWidget::class.java)
            updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))

            val pIntent = PendingIntent.getBroadcast(context, appWidgetId, updateIntent, 0)
            widgetView.setOnClickPendingIntent(R.id.wd_add, pIntent)

            //Coroutine
            launch(CommonPool) {
                val response = async {
                    restIteractor.getCityWeather(context.getString(R.string.minsk)).execute()
                }
                val weatherResponseModel = response.await()
                launch(UI) {
                    when (weatherResponseModel.code()) {
                        200 -> {
                            widgetView.setTextViewText(R.id.city, weatherResponseModel.body()?.name)
                            widgetView.setTextViewText(R.id.degrees, "${weatherResponseModel.body()?.main?.temp} °C")
                        }
                        else -> {
                            widgetView.setTextViewText(R.id.city, context.getString(R.string.please))
                            widgetView.setTextViewText(R.id.degrees, context.getString(R.string.refresh))
                        }
                    }
                    widgetView.setViewVisibility(R.id.progress, View.GONE)
                    appWidgetManager.updateAppWidget(appWidgetId, widgetView)
                }
            }
        }
    }
}