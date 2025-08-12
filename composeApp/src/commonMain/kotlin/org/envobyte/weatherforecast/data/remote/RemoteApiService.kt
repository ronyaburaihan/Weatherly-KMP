package org.envobyte.weatherforecast.data.remote

import org.envobyte.weatherforecast.data.model.GeoLocationResponse
import org.envobyte.weatherforecast.data.model.WeatherApiResponse

interface RemoteApiService {
    suspend fun getCurrentWeatherAndForecast(
        latitude: Double,
        longitude: Double
    ): Result<WeatherApiResponse>

    suspend fun getLocationName(lat: Double, lon: Double): Result<GeoLocationResponse>
}