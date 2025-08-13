package org.envobyte.weatherforecast.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HourlyForecast(
    val shortDate: String,
    val shortDayName: String,
    val longDate: String,
    val formattedTime: String,
    val temperature: Double,
    val temperatureUnit: String,
    val icon: String
)