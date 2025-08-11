package org.envobyte.weatherforecast.domain.model

data class ForecastDay(
    val date: String,
    val dayName: String,
    val temperature: Int,
    val condition: String,
    val icon: String,
)