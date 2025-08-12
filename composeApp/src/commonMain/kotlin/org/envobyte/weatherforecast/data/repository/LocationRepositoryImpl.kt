package org.envobyte.weatherforecast.data.repository

import org.envobyte.weatherforecast.data.remote.RemoteApiService
import org.envobyte.weatherforecast.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val remoteApiService: RemoteApiService
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
}