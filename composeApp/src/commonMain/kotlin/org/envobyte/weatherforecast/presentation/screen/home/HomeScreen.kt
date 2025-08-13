package org.envobyte.weatherforecast.presentation.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.core.permission.getPlatformLocationHandler
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.presentation.screen.component.AppDrawerSheet
import org.envobyte.weatherforecast.presentation.screen.component.AppTopBar
import org.envobyte.weatherforecast.presentation.screen.component.LocationPermissionScreen
import org.envobyte.weatherforecast.presentation.screen.component.ShimmerEffect
import org.envobyte.weatherforecast.presentation.screen.component.WeatherIcon
import org.envobyte.weatherforecast.presentation.theme.HomeScreenGradient
import org.envobyte.weatherforecast.presentation.theme.PrimaryTextColor
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.cloudy_weather
import weatherly.composeapp.generated.resources.ic_cloudy_sun

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val locationManager = getPlatformLocationHandler()

    LaunchedEffect(Unit) {
        homeViewModel.checkLocationPermission(locationManager)
    }

    LaunchedEffect(uiState.needLocationPermission) {
        if (uiState.needLocationPermission == false) {
            homeViewModel.requestCurrentLocation(locationManager)
        }
    }

    Crossfade(
        targetState = uiState,
        animationSpec = tween(durationMillis = 700)
    ) { state ->
        if (state.error != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(state.error)
            }
        } else if (state.needLocationPermission == true) {
            LocationPermissionScreen(
                requestPermission = { homeViewModel.requestLocationPermission(locationManager) }
            )
        } else if (state.isLoading) {
            ShimmerEffect()
        } else {
            uiState.weatherData?.let {
                HomeContent(
                    locationName = uiState.locationName ?: "",
                    weatherData = it,
                )
            }
        }
    }
}

@Composable
fun HomeContent(locationName: String, weatherData: WeatherData) {

    val scope = rememberCoroutineScope()
    val modalDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AppDrawerSheet(
                        drawerState = modalDrawerState,
                        weatherData = weatherData,
                        onDismiss = {
                            scope.launch {
                                modalDrawerState.close()
                            }
                        })
                }

            },
            modifier = Modifier,
            drawerState = modalDrawerState,
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(modifier = Modifier.fillMaxSize().background(HomeScreenGradient)) {
                    Image(
                        painterResource(Res.drawable.cloudy_weather),
                        contentDescription = null,
                        modifier = Modifier.statusBarsPadding()
                    )
                    Text(
                        text = weatherData.current.icon,
                        modifier = Modifier.align(
                            Alignment.CenterEnd
                        ).offset(x = 50.dp)
                            .blur(5.dp),
                        fontSize = 550.sp
                    )

                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppTopBar(
                            title = weatherData.current.greeting,
                            subtitle = weatherData.current.date,
                            onMenuClick = {
                                scope.launch {
                                    modalDrawerState.open()
                                }
                            }
                        )
                        Spacer(Modifier.height(33.dp))
                        TemperatureSection(
                            locationName,
                            weatherData.current.temperature,
                            weatherData.current.condition
                        )
                        Spacer(Modifier.height(36.dp))
                        WeatherDetailsCard(
                            humidity = weatherData.current.humidity,
                            windSpeed = weatherData.current.windSpeed,
                            precipitation = weatherData.current.precipitation,
                        )
                        Spacer(Modifier.height(20.dp))
                        DailyForCast(weatherData)
                    }
                }
            }
        }
    }
}

@Composable
fun TemperatureSection(
    locationName: String,
    celsius: String,
    weatherCondition: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            locationName, style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W500,
                color = PrimaryTextColor
            )
        )
        Text(
            text = celsius,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.W500,
                fontSize = 96.sp,
                color = PrimaryTextColor
            )
        )
        Text(
            weatherCondition, style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W400,
                color = PrimaryTextColor
            )
        )
    }
}

@Composable
fun WeatherDetailsCard(
    humidity: String,
    windSpeed: String,
    precipitation: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherInfoItem(
            status = "Wind Speed",
            value = windSpeed,
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
                color = Color(0xFF8E8E8E)
            )
        )
        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        )

    }
}

@Composable
private fun DailyForCast(weatherData: WeatherData) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp).navigationBarsPadding()
            .background(Color.White.copy(.8f), shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)).padding(16.dp)
    ) {
        Text(
            "Next days",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8E8E8E))
        )
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(weatherData.forecast, key = { item -> item.formattedDate }) { item ->
                DailyForCastItem(
                    icon = item.icon,
                    temperature = item.temperature.toString(),
                    date = item.formattedDate
                )
            }
        }
    }
}

@Composable
private fun DailyForCastItem(
    icon: String,
    temperature: String,
    date: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        WeatherIcon(icon = icon)
        Spacer(Modifier.width(12.dp))
        Row(
            modifier = Modifier.weight(1f)
                .border(1.dp, color = Color.White, shape = RoundedCornerShape(360.dp))
                .clip(RoundedCornerShape(360.dp))
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                "$temperature°", style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp, fontWeight = FontWeight.W500, color = PrimaryTextColor
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF8E8E8E)
                )
            )
        }
    }
}
