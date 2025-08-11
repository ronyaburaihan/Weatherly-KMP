package org.envobyte.weatherforecast.domain.repository

import org.envobyte.weatherforecast.core.util.ResultWrapper
import org.envobyte.weatherforecast.domain.model.ForecastDay
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherInfo>
    suspend fun getForecast(lat: Double, lon: Double): Result<List<ForecastDay>>
    suspend fun getCompleteWeatherData(lat: Double, lon: Double): Result<WeatherData>
}