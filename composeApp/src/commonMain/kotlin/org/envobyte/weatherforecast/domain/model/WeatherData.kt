package org.envobyte.weatherforecast.domain.model

data class WeatherData(
    val current: WeatherInfo,
    val forecast: List<ForecastDay>
)