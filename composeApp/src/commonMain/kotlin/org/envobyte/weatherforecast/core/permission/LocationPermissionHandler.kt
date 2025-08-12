package org.envobyte.weatherforecast.core.permission

interface LocationPermissionHandler {
    suspend fun requestLocationPermission(): PermissionStatus
    suspend fun isLocationPermissionGranted(): Boolean

}