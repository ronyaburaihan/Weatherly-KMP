package org.envobyte.weatherforecast

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.envobyte.weatherforecast.core.di.initKoin
import org.envobyte.weatherforecast.core.permission.LocationPermissionHandler
import org.koin.android.ext.koin.androidContext
import org.envobyte.weatherforecast.core.di.platformModule

class WeatherlyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@WeatherlyApp)
            modules(platformModule)
        }
    }
}



