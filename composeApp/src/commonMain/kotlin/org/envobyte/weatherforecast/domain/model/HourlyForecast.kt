package org.envobyte.weatherforecast.domain.model

data class HourlyForecast(
    val shortDate: String,
    val shortDayName: String,
    val longDate: String,
    val formattedTime: String,
    val temperature: Double,
    val temperatureUnit: String,
    val icon: String
)