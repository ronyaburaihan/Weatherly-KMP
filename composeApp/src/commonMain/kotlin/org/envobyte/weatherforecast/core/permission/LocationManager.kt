package org.envobyte.weatherforecast.core.permission

import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.PermissionState

interface LocationManager {
    suspend fun requestLocationPermission(): PermissionState
    suspend fun isLocationPermissionGranted(): Boolean
    suspend fun getCurrentLocation(): LocationData?
}