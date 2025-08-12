package org.envobyte.weatherforecast.core.permission

import androidx.compose.runtime.Composable

@Composable
actual fun getPlatformLocationHandler(): LocationManager {
    return IOSLocationManager()
}