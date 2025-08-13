package org.envobyte.weatherforecast.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    val greeting: String,
    val temperature: String,
    val condition: String,
    val icon: String,
    val windSpeed: String,
    val humidity: String,
    val precipitation: String,
    val date: String,
)