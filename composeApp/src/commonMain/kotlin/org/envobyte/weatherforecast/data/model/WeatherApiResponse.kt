package org.envobyte.weatherforecast.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherApiResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Double,
    @SerialName("current_units") val currentUnits: CurrentUnits,
    val current: CurrentWeather,
    @SerialName("daily_units") val dailyUnits: DailyUnits,
    val daily: DailyWeather
)

@Serializable
data class CurrentUnits(
    val time: String,
    val interval: String,
    val temperature_2m: String,
    val relative_humidity_2m: String,
    val precipitation: String,
    val weather_code: String,
    val wind_speed_10m: String
)

@Serializable
data class CurrentWeather(
    val time: String,
    val interval: Int,
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val precipitation: Double,
    val weather_code: Int,
    val wind_speed_10m: Double
)

@Serializable
data class DailyUnits(
    val time: String,
    val weather_code: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val precipitation_sum: String
)

@Serializable
data class DailyWeather(
    val time: List<String>,
    val weather_code: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val precipitation_sum: List<Double>
)