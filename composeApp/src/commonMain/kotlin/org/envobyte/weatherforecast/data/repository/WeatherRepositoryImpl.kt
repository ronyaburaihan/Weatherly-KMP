package org.envobyte.weatherforecast.data.repository

import org.envobyte.weatherforecast.data.mapper.toCurrentWeather
import org.envobyte.weatherforecast.data.mapper.toForecast
import org.envobyte.weatherforecast.data.mapper.toWeatherData
import org.envobyte.weatherforecast.data.source.remote.RemoteApiService
import org.envobyte.weatherforecast.domain.model.DailyForecast
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.model.WeatherInfo
import org.envobyte.weatherforecast.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val apiService: RemoteApiService
) : WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherInfo> {
        return try {
            val apiResult = apiService.getCurrentWeatherAndForecast(lat, lon)

            if (apiResult.isFailure) {
                return Result.failure(apiResult.exceptionOrNull() ?: Exception("API call failed"))
            }

            val response = apiResult.getOrThrow()
            val weatherInfo = response.toCurrentWeather()

            Result.success(weatherInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getForecast(lat: Double, lon: Double): Result<List<DailyForecast>> {
        return try {
            val apiResult = apiService.getCurrentWeatherAndForecast(lat, lon)

            if (apiResult.isFailure) {
                return Result.failure(apiResult.exceptionOrNull() ?: Exception("API call failed"))
            }

            val response = apiResult.getOrThrow()
            val forecast = response.toForecast()

            Result.success(forecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCompleteWeatherData(lat: Double, lon: Double): Result<WeatherData> {
        return try {
            val apiResult = apiService.getCurrentWeatherAndForecast(lat, lon)

            if (apiResult.isFailure) {
                return Result.failure(apiResult.exceptionOrNull() ?: Exception("API call failed"))
            }

            val response = apiResult.getOrThrow()
            val weatherData = response.toWeatherData()

            Result.success(weatherData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}