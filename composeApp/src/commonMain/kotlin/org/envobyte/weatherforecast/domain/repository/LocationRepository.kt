package org.envobyte.weatherforecast.domain.repository

interface LocationRepository {
    suspend fun getLocationName(lat: Double, lon: Double): Result<String>
}