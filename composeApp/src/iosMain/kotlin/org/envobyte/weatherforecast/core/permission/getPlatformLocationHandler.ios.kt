package org.envobyte.weatherforecast.core.permission

import androidx.compose.runtime.Composable
import org.envobyte.weatherforecast.IOSLocationPermissionHandler

@Composable
actual fun getPlatformLocationHandler(): LocationPermissionHandler {
    return IOSLocationPermissionHandler()
}