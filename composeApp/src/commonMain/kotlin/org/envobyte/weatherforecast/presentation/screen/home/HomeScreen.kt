package org.envobyte.weatherforecast.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.presentation.screen.component.AppTopBar
import org.envobyte.weatherforecast.presentation.theme.HomeScreenGradient
import org.envobyte.weatherforecast.presentation.theme.PrimaryTextColor
import org.envobyte.weatherforecast.presentation.theme.rubikFontFamily
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.blur_sun
import weatherly.composeapp.generated.resources.cloudy_weather
import weatherly.composeapp.generated.resources.ic_cloudy_sun

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    uiState.weatherData?.let {
        HomeContent(
            it
        )
    }

}

@Composable
fun HomeContent(weatherData: WeatherData) {

    Box(modifier = Modifier.fillMaxSize().background(HomeScreenGradient)) {
        Image(
            painterResource(Res.drawable.cloudy_weather),
            contentDescription = null,
            modifier = Modifier.statusBarsPadding()
        )
        Image(
            painterResource(Res.drawable.blur_sun),
            contentDescription = null,
            modifier = Modifier.align(
                Alignment.CenterEnd
            ).height(619.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTopBar()
            Spacer(Modifier.height(33.dp))
            TemperatureSection(
                "London",
                "${weatherData.current.temperature}°C",
                weatherData.current.condition
            )
            WeatherDetailsCard(
                humidity = "${weatherData.current.humidity}%",
                uvIndex = weatherData.current.uvIndex.toString(),
                precipitation = "${weatherData.current.precipitation}mm",
            )
        }
    }
}

@Composable
fun TemperatureSection(locationName: String, celsius: String, weatherCondition: String) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            locationName, style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W500,
                fontFamily = rubikFontFamily(),
                color = PrimaryTextColor
            )
        )
        Text(
            celsius, style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.W500,
                fontSize = 96.sp,
                fontFamily = rubikFontFamily(),
                color = PrimaryTextColor
            )
        )
        Text(
            weatherCondition, style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W400,
                fontFamily = rubikFontFamily(),
                color = PrimaryTextColor
            )
        )
    }
}

@Composable
fun WeatherDetailsCard(humidity: String, uvIndex: String, precipitation: String) {

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(24.dp)
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherInfoItem(
            status = "Uv Index",
            value = uvIndex,
        )
        WeatherInfoItem(
            status = "Humidity",
            value = humidity
        )
        WeatherInfoItem(
            status = "Precipitation",
            value = precipitation
        )
    }
}

@Composable
private fun WeatherInfoItem(
    status: String,
    value: String,
) {
    Column {
        Image(
            painterResource(Res.drawable.ic_cloudy_sun),
            modifier = Modifier.size(12.dp),
            contentDescription = null
        )
        Spacer(Modifier.height(4.dp))
        Text(
            status, style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = rubikFontFamily(),
                color = Color(0xFF8E8E8E)
            )
        )
        Spacer(Modifier.height(4.dp))

        Text(
            value, style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = rubikFontFamily(),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        )

    }
}

