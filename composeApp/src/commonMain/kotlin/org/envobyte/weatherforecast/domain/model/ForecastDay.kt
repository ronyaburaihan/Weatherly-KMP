package org.envobyte.weatherforecast.domain.model

data class ForecastDay(
    val formattedDate: String,
    val temperature: Int,
    val precipitation: Int,
    val icon: String,
)