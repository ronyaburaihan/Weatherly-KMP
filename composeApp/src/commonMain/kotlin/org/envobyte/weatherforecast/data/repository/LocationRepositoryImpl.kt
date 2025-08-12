package org.envobyte.weatherforecast.data.repository

import org.envobyte.weatherforecast.data.location.LocationDataSource
import org.envobyte.weatherforecast.data.remote.RemoteApiService
import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.PermissionState
import org.envobyte.weatherforecast.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val remoteApiService: RemoteApiService,
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    override suspend fun getLocationName(lat: Double, lon: Double): Result<String> {
        val locationResponse = remoteApiService.getLocationName(lat, lon)
        return if (locationResponse.isSuccess) {
            val location = locationResponse.getOrThrow()
            val locationName = location.address?.city
                ?: location.address?.town
                ?: location.address?.village
                ?: location.displayName.split(",").firstOrNull()
                ?: "Unknown Location"
            Result.success(locationName)
        } else {
            Result.failure(locationResponse.exceptionOrNull() ?: Exception("API call failed"))
        }
    }

    override suspend fun getCurrentLocation(): Result<LocationData> {
        return try {
            val locationResult = locationDataSource.getCurrentLocation()
            if (locationResult.isSuccess) {
                val location = locationResult.getOrThrow()
                // Optionally get location name
                val nameResult = getLocationName(location.latitude, location.longitude)
                val locationWithName = location.copy(
                    name = nameResult.getOrNull()
                )
                Result.success(locationWithName)
            } else {
                locationResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestLocationPermission(): Result<PermissionState> {
        return locationDataSource.requestLocationPermission()
    }

    override suspend fun isLocationPermissionGranted(): Boolean {
        return locationDataSource.isLocationPermissionGranted()
    }
}