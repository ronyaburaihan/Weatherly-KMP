package org.envobyte.weatherforecast.core.permission

import androidx.compose.runtime.Composable

@Composable
expect fun getPlatformLocationHandler(): LocationPermissionHandler



suspend fun ensureLocationPermission(handler: LocationPermissionHandler): Boolean {
    if (!handler.isLocationPermissionGranted()) {
        val result = handler.requestLocationPermission()
        return result == PermissionStatus.GRANTED
    }
    return true
}

suspend fun getCurrentLocation(provider: LocationProvider): String {
    val location = provider.getCurrentLocation()
    if (location != null) {
        return ("Lat: ${location.latitude}, Lng: ${location.longitude}")
    } else {
        return ("Failed to get location")
    }
}