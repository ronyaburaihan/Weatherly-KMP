package org.envobyte.weatherforecast.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.envobyte.weatherforecast.data.model.GeoLocationResponse
import org.envobyte.weatherforecast.data.model.WeatherApiResponse

class RemoteApiServiceImpl(private val httpClient: HttpClient) : RemoteApiService {

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
                    "weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,sunrise,sunset"
                )
                parameter("timezone", "auto")
                parameter("forecast_days", 7)
                parameter("hourly", "temperature_2m,weather_code")
            }
            Result.success(response.body<WeatherApiResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLocationName(
        lat: Double,
        lon: Double
    ): Result<GeoLocationResponse> {
        return try {
            val url = "https://nominatim.openstreetmap.org/reverse"
            val response = httpClient.get(url) {
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("format", "json")
                parameter("addressdetails", 1)
            }
            val geocodeResponse = response.body<GeoLocationResponse>()
            if (geocodeResponse.address == null)
                Result.failure(Exception("Address not found"))
            else
                Result.success(geocodeResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}