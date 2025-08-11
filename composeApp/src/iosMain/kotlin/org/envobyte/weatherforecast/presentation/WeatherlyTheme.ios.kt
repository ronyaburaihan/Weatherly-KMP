package org.envobyte.weatherforecast.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.envobyte.weatherforecast.IOSLocationPermissionHandler
import org.envobyte.weatherforecast.core.permission.LocationPermissionHandler

@Composable
actual fun WeatherlyTheme(
    isDarkMode: Boolean,
    content: @Composable (() -> Unit)
) {
    CompositionLocalProvider {
        MaterialTheme(
            colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme(),
            typography = typography,
            content = content
        )
    }
}

@Composable
actual fun getPlatformLocationHandler(): LocationPermissionHandler {
    return IOSLocationPermissionHandler()
}