package org.envobyte.weatherforecast

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.envobyte.weatherforecast.presentation.navigation.AppNavigation
import org.envobyte.weatherforecast.presentation.theme.WeatherlyTheme

@Composable
fun App(isDarkMode: Boolean) {
    WeatherlyTheme(isDarkMode = isDarkMode) {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}