package org.envobyte.weatherforecast.domain.model

data class WeatherData(
    val current: WeatherInfo,
    val dailyForecasts: List<DailyForecast>,
    val hourlyForecasts: List<HourlyForecast>
)