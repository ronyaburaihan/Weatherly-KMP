package org.envobyte.weatherforecast.domain.model

import kotlinx.serialization.Serializable

@Serializable
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