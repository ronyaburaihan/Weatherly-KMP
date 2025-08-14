package org.envobyte.weatherforecast.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.serialization.json.Json
import org.envobyte.weatherforecast.domain.model.DailyForecast
import org.envobyte.weatherforecast.domain.model.HourlyForecast
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.presentation.screen.component.TemperatureSplineChart
import org.envobyte.weatherforecast.presentation.theme.WeatherIconGradient
import org.jetbrains.compose.resources.painterResource
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.ic_back
import weatherly.composeapp.generated.resources.ic_tmp


private const val HOURS_PER_DAY = 24

private fun hoursForDay(
    allHourly: List<HourlyForecast>,
    selectedDayIndex: Int
): List<HourlyForecast> {
    if (allHourly.isEmpty()) return emptyList()
    val start = (selectedDayIndex * HOURS_PER_DAY).coerceAtMost(allHourly.size)
    val end = (start + HOURS_PER_DAY).coerceAtMost(allHourly.size)
    return allHourly.subList(start, end)
}


@Composable
fun DetailsScreen(
    navController: NavHostController,
    weatherJson: String
) {
    val weatherData = Json.decodeFromString<WeatherData>(weatherJson)

    DetailsContent(
        weatherData = weatherData,
        onClick = {
            navController.navigateUp()
        }
    )
}

private fun <T> List<T>.circularWindow(start: Int, size: Int): List<T> {
    if (isEmpty()) return emptyList()
    val n = minOf(size, this.size)
    return List(n) { i -> this[(start + i) % this.size] }
}

@Composable
fun DetailsContent(
    weatherData: WeatherData,
    onClick: () -> Unit
) {
    var selectedDayIndex by rememberSaveable { mutableStateOf(0) }


    val sevenHours = remember(selectedDayIndex, weatherData.hourlyForecasts) {
        hoursForDay(weatherData.hourlyForecasts, selectedDayIndex)
    }
    LaunchedEffect(Unit) {
        weatherData.hourlyForecasts.forEach {
            println(it.temperature)
        }
    }


    Column(Modifier.fillMaxSize()) {
        TopBar(onClick = onClick)

        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            Text("Date", modifier = Modifier.padding(start = 16.dp))
            Spacer(Modifier.height(16.dp))

            DateTemperatureEntry(
                dailyForecasts = weatherData.dailyForecasts,
                selectedIndex = selectedDayIndex,
                onClick = { idx -> selectedDayIndex = idx }
            )

            Spacer(Modifier.height(16.dp))

            val selectedDaily = weatherData.dailyForecasts[selectedDayIndex]
            WeatherInfoItem(
                date = selectedDaily.longDate,
                temperature = selectedDaily.averageTemperature,
                icon = selectedDaily.icon
            )
            val temps = remember(sevenHours) { sevenHours.map { it.temperature.toFloat() } }
            val labels = remember(sevenHours) { sevenHours.map { it.formattedTime } }


            if (temps.size >= 2 && temps.size == labels.size) {
                TemperatureSplineChart(
                    modifier = Modifier.fillMaxWidth(),
                    temps = temps,
                    labels = labels,
                    isToday = selectedDayIndex == 0
                )
            } else {
                Text(
                    "No forecast data available",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            WeatherDescription(
                highTmp = selectedDaily.maxTemperature,
                lowTmp = selectedDaily.minTemperature
            )
        }
    }
}


@Composable
private fun WeatherDescription(highTmp: String, lowTmp: String) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
            .background(WeatherIconGradient, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Text(
            "The weather forecast for today is mostly sunny with a mild temperature drop. The high will be around ${highTmp} and the low will be around ${lowTmp}. A slight chance of rain is expected in the afternoon",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DateTemperatureEntry(
    dailyForecasts: List<DailyForecast>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        itemsIndexed(dailyForecasts) { index, item ->
            DateTemperatureItem(
                dayName = item.shortDayName,
                onClick = {
                    onClick(index)
                },
                selected = selectedIndex == index,
                temperature = item.shortDate.toInt()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Hourly Forecast",
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = Modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(onClick = { onClick() }) {
                Icon(
                    painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
    )
}


@Composable
private fun DateTemperatureItem(
    dayName: String,
    temperature: Int,
    onClick: () -> Unit,
    selected: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
        )
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier.size(32.dp)
                .background(
                    brush = if (selected) WeatherIconGradient else SolidColor(Color.Transparent),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$temperature", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun WeatherInfoItem(date: String, temperature: String, icon: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier) {
            Text(text = date, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${temperature}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = icon,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 32.sp)
                )

            }
        }

        Row(
            modifier = Modifier.border(
                width = 1.dp,
                color = Color(0xFFE4E4E4),
                shape = RoundedCornerShape(360.dp)
            )
                .clip(RoundedCornerShape(360.dp))
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Temperature", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))
            Icon(
                painterResource(Res.drawable.ic_tmp),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}