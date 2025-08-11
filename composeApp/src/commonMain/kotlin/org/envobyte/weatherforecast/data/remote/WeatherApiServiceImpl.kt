package org.envobyte.weatherforecast.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.envobyte.weatherforecast.data.model.WeatherApiResponse

class WeatherApiServiceImpl(private val httpClient: HttpClient) : WeatherApiService {

    private val baseUrl = "https://api.open-meteo.com/v1"

    override suspend fun getCurrentWeatherAndForecast(
        latitude: Double,
        longitude: Double
    ): Result<WeatherApiResponse> {
        val url = "$baseUrl/forecast"
        return try {
            val response = httpClient.get(url) {
                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter(
                    "current",
                    "temperature_2m,relative_humidity_2m,precipitation,weather_code,wind_speed_10m"
                )
                parameter(
                    "daily",
                    "weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum"
                )
                parameter("timezone", "auto")
                parameter("forecast_days", 7)
            }
            Result.success(response.body<WeatherApiResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}