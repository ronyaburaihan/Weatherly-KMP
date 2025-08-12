package org.envobyte.weatherforecast.core.permission

interface LocationManager {
    suspend fun requestLocationPermission(): PermissionStatus
    suspend fun isLocationPermissionGranted(): Boolean
    suspend fun getCurrentLocation(): LocationData?

}