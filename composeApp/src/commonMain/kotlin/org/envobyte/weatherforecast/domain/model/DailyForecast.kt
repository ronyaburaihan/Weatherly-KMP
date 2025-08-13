package org.envobyte.weatherforecast.domain.model

data class DailyForecast(
    val formattedDate: String,
    val maxTemperature: String,
    val minTemperature: String,
    val averageTemperature: String,
    val precipitation: String,
    val icon: String,
    val sunriseTime: String,
    val sunsetTime: String,
)