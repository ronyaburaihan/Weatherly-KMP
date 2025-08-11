package org.envobyte.weatherforecast.data.mapper

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.envobyte.weatherforecast.data.model.WeatherApiResponse
import org.envobyte.weatherforecast.domain.model.ForecastDay
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.model.WeatherInfo
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun WeatherApiResponse.toWeatherData(): WeatherData {
    return WeatherData(
        current = this.toWeatherInfo(),
        forecast = this.toForecastDays()
    )
}

fun WeatherApiResponse.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        location = "Current Location", // Open-Meteo doesn't provide location name
        temperature = current.temperature_2m.toInt(),
        condition = mapWeatherCodeToCondition(current.weather_code),
        icon = mapWeatherCodeToIcon(current.weather_code),
        uvIndex = 0, // Open-Meteo basic plan doesn't include UV index
        humidity = current.relative_humidity_2m,
        precipitation = current.precipitation.toInt(),
        date = current.time
    )
}

fun WeatherApiResponse.toForecastDays(): List<ForecastDay> {
    return daily.time.mapIndexed { index, dateString ->
        ForecastDay(
            date = dateString,
            dayName = formatDayName(dateString, index),
            temperature = daily.temperature_2m_max[index].toInt(),
            condition = mapWeatherCodeToCondition(daily.weather_code[index]),
            icon = mapWeatherCodeToIcon(daily.weather_code[index])
        )
    }
}

private fun mapWeatherCodeToCondition(code: Int): String {
    return when (code) {
        0 -> "Clear sky"
        1, 2, 3 -> "Partly cloudy"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rain"
        71, 73, 75 -> "Snow"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers"
        85, 86 -> "Snow showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm with hail"
        else -> "Unknown"
    }
}

private fun mapWeatherCodeToIcon(code: Int): String {
    return when (code) {
        0 -> "☀️" // Clear sky
        1, 2, 3 -> "⛅" // Partly cloudy
        45, 48 -> "🌫️" // Foggy
        51, 53, 55, 61, 63, 65 -> "🌧️" // Drizzle/Rain
        71, 73, 75, 77 -> "❄️" // Snow
        80, 81, 82 -> "🌦️" // Rain showers
        85, 86 -> "🌨️" // Snow showers
        95, 96, 99 -> "⛈️" // Thunderstorm
        else -> "❓" // Unknown
    }
}

@OptIn(ExperimentalTime::class)
private fun formatDayName(dateString: String, index: Int): String {
    return when (index) {
        0 -> "Today"
        1 -> "Tomorrow"
        else -> {
            try {
                val dayNames = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
                val currentTime = Clock.System.now()
                val currentDay = currentTime.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek.ordinal
                val targetDay = (currentDay + index) % 7
                dayNames[targetDay]
            } catch (e: Exception) {
                "Day ${index + 1}"
            }
        }
    }
}

// Extension functions for easier mapping in repository
fun WeatherApiResponse.toCurrentWeather(): WeatherInfo = this.toWeatherInfo()
fun WeatherApiResponse.toForecast(): List<ForecastDay> = this.toForecastDays()