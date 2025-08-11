package org.envobyte.weatherforecast.domain.model

data class WeatherInfo(
    val location: String,
    val temperature: Int,
    val condition: String,
    val icon: String,
    val uvIndex: Int,
    val humidity: Int,
    val precipitation: Int,
    val date: String,
)