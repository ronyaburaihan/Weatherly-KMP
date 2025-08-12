package org.envobyte.weatherforecast.data.mapper

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.envobyte.weatherforecast.data.model.WeatherApiResponse
import org.envobyte.weatherforecast.domain.model.ForecastDay
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.model.WeatherInfo

fun WeatherApiResponse.toWeatherData(): WeatherData {
    return WeatherData(
        current = this.toWeatherInfo(),
        forecast = this.toForecastDays()
    )
}

fun WeatherApiResponse.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        location = "",
        greeting = getGreetingFromDateTime(current.time),
        temperature = "${current.temperature_2m.toInt()}${currentUnits.temperature_2m}",
        condition = mapWeatherCodeToCondition(current.weather_code),
        icon = mapWeatherCodeToIcon(current.weather_code),
        windSpeed = "${current.wind_speed_10m}${currentUnits.wind_speed_10m}",
        humidity = "${current.relative_humidity_2m}${currentUnits.relative_humidity_2m}",
        precipitation = "${current.precipitation}${currentUnits.precipitation}",
        date = formatDateString(current.time.substringBefore("T"), true)
    )
}

fun WeatherApiResponse.toForecastDays(): List<ForecastDay> {
    return daily.time.mapIndexed { index, dateString ->
        ForecastDay(
            formattedDate = formatDateString(dateString),
            temperature = averageTemperature(
                daily.temperature_2m_max[index].toInt(),
                daily.temperature_2m_min[index].toInt()
            ),
            icon = mapWeatherCodeToIcon(daily.weather_code[index]),
            precipitation = daily.precipitation_sum[index].toInt()
        )
    }
}

private fun averageTemperature(toInt: Int, toInt2: Int): Int {
    return (toInt + toInt2) / 2
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

fun formatDateString(dateString: String, includeYear: Boolean = false): String {
    val date = LocalDate.parse(dateString)

    val dayOfWeek = date.dayOfWeek.name.lowercase()
        .replaceFirstChar { it.titlecase() }
    val monthName = date.month.name.take(3).lowercase()
        .replaceFirstChar { it.titlecase() }

    return if (includeYear) {
        "$dayOfWeek, ${date.day} $monthName ${date.year}"
    } else {
        "$dayOfWeek, ${date.day} $monthName"
    }
}

fun getGreetingFromDateTime(dateTimeString: String): String {
    val dateTime = LocalDateTime.parse(dateTimeString)
    val hour = dateTime.hour

    return when (hour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}


// Extension functions for easier mapping in repository
fun WeatherApiResponse.toCurrentWeather(): WeatherInfo = this.toWeatherInfo()
fun WeatherApiResponse.toForecast(): List<ForecastDay> = this.toForecastDays()