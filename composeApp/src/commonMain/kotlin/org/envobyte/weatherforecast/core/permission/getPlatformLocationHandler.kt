package org.envobyte.weatherforecast.core.permission

import androidx.compose.runtime.Composable

@Composable
expect fun getPlatformLocationHandler(): LocationPermissionHandler


enum class PermissionStatus {
    GRANTED, DENIED, PERMANENTLY_DENIED
}


data class LocationData(
    val latitude: Double,
    val longitude: Double
)

expect class LocationProvider(context:Any?) {
    suspend fun getCurrentLocation(): LocationData?
}

suspend fun ensureLocationPermission(handler: LocationPermissionHandler): Boolean {
    if (!handler.isLocationPermissionGranted()) {
        val result = handler.requestLocationPermission()
        return result == PermissionStatus.GRANTED
    }
    return true
}

suspend fun getCurrentLocation(provider: LocationProvider) {
    val location = provider.getCurrentLocation()
    if (location != null) {
        println("Lat: ${location.latitude}, Lng: ${location.longitude}")
    } else {
        println("Failed to get location")
    }
}