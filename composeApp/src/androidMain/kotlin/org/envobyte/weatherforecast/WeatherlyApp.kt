package org.envobyte.weatherforecast

import android.app.Application
import org.envobyte.weatherforecast.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class WeatherlyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@WeatherlyApp)
        }
    }
}



