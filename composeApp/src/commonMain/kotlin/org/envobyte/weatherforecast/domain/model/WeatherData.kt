package org.envobyte.weatherforecast.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val current: WeatherInfo,
    val dailyForecasts: List<DailyForecast>,
    val hourlyForecasts: List<HourlyForecast>
)