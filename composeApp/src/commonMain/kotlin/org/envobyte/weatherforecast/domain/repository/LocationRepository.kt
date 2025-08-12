package org.envobyte.weatherforecast.domain.repository

import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.PermissionState

interface LocationRepository {
    suspend fun getLocationName(lat: Double, lon: Double): Result<String>
    suspend fun getCurrentLocation(): Result<LocationData>
    suspend fun requestLocationPermission(): Result<PermissionState>
    suspend fun isLocationPermissionGranted(): Boolean
}