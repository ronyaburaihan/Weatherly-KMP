package org.envobyte.weatherforecast

import androidx.compose.runtime.Composable
import org.envobyte.weatherforecast.core.permission.LocationPermissionHandler
import org.envobyte.weatherforecast.presentation.HomeScreen
import org.envobyte.weatherforecast.presentation.WeatherlyTheme

@Composable
fun App(isDarkMode: Boolean) {
    WeatherlyTheme(isDarkMode = isDarkMode) {
        HomeScreen()
    }
}







