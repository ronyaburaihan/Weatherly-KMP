package org.envobyte.weatherforecast.data.mapper

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.envobyte.weatherforecast.data.model.WeatherApiResponse
import org.envobyte.weatherforecast.domain.model.DailyForecast
import org.envobyte.weatherforecast.domain.model.HourlyForecast
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.model.WeatherInfo

fun WeatherApiResponse.toWeatherData(): WeatherData {
    return WeatherData(
        current = this.toWeatherInfo(),
        dailyForecasts = this.toDailyForecast(),
        hourlyForecasts = this.toHourlyForecast()
    )
}

fun WeatherApiResponse.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
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

fun WeatherApiResponse.toDailyForecast(): List<DailyForecast> {
    return daily.time.mapIndexed { index, dateString ->
        val (dayName, monthName, date) = formatDateDay(daily.time[index].substringBefore("T"))
        DailyForecast(
            shortDate = date,
            shortDayName = dayName,
            longDate = formatDateString(
                daily.time[index].substringBefore("T"),
                isShortDayName = true
            ),
            formattedDate = formatDateString(daily.time[index].substringBefore("T"), false),
            maxTemperature = "${daily.temperature_2m_max[index]}${dailyUnits.temperature_2m_max}.",
            minTemperature = "${daily.temperature_2m_min[index]}${dailyUnits.temperature_2m_min}",
            averageTemperature = "${
                averageTemperature(
                    daily.temperature_2m_max[index].toInt(),
                    daily.temperature_2m_min[index].toInt()
                )
            }${dailyUnits.temperature_2m_max}",
            icon = mapWeatherCodeToIcon(daily.weather_code[index]),
            precipitation = "${daily.precipitation_sum[index]}${dailyUnits.precipitation_sum}",
            sunriseTime = formatDayAndTime(daily.sunrise[index]),
            sunsetTime = formatDayAndTime(daily.sunset[index])
        )
    }
}

fun WeatherApiResponse.toHourlyForecast(): List<HourlyForecast> {
    return hourly.time.mapIndexed { index, dateString ->
        HourlyForecast(
            formattedTime = formatHourTime(dateString),
            temperature = hourly.temperature[index],
            temperatureUnit = hourlyUnits.temperature,
            icon = mapWeatherCodeToIcon(hourly.weatherCode[index])
        )
    }
}

private fun formatHourTime(dateTimeString: String): String {
    val hour = LocalDateTime.parse(dateTimeString).hour
    return when {
        hour == 0 -> "12AM"
        hour < 12 -> "${hour}AM"
        hour == 12 -> "12PM"
        else -> "${hour - 12}PM"
    }
}

private fun formatDateDay(dateTimeString: String): Triple<String, String, String> {
    val date = LocalDate.parse(dateTimeString)
    val dayOfWeek = date.dayOfWeek.name.take(1).uppercase()
    val monthName = date.month.name.take(3).lowercase()
        .replaceFirstChar { it.titlecase() }
    val dayOfMonth = date.day.toString()
    return Triple(dayOfWeek, monthName, dayOfMonth)
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

fun formatDateString(
    dateString: String,
    isShortDayName: Boolean = false,
    includeYear: Boolean = false
): String {
    val date = LocalDate.parse(dateString)

    val dayOfWeek = date.dayOfWeek.name.lowercase()
        .let { if (isShortDayName) it.take(3) else it }
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
    val hour = LocalDateTime.parse(dateTimeString).hour
    return when (hour) {
        in 0..4 -> "Late Night"
        in 5..11 -> "Good Morning"
        12 -> "Good Noon"
        in 13..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}

fun formatDayAndTime(dateTimeString: String): String {
    val dateTime = LocalDateTime.parse(dateTimeString)
    val dayName = dateTime.dayOfWeek.name
        .lowercase()
        .replaceFirstChar { it.titlecase() }

    val hour = dateTime.hour
    val minute = dateTime.minute
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    return "$dayName,\n $hour12:$minute$amPm"
}


// Extension functions for easier mapping in repository
fun WeatherApiResponse.toCurrentWeather(): WeatherInfo = this.toWeatherInfo()
fun WeatherApiResponse.toForecast(): List<DailyForecast> = this.toDailyForecast()