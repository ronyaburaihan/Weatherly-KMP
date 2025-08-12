package org.envobyte.weatherforecast.domain.model

data class WeatherInfo(
    val location: String,
    val greeting: String,
    val temperature: String,
    val condition: String,
    val icon: String,
    val windSpeed: String,
    val humidity: String,
    val precipitation: String,
    val date: String,
)