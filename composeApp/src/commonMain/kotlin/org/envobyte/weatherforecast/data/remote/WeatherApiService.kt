package org.envobyte.weatherforecast.data.remote

import org.envobyte.weatherforecast.data.model.WeatherApiResponse

interface WeatherApiService {
    suspend fun getCurrentWeatherAndForecast(
        latitude: Double,
        longitude: Double
    ): Result<WeatherApiResponse>
}