package org.envobyte.weatherforecast.presentation.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.serialization.json.Json
import org.envobyte.weatherforecast.core.permission.getPlatformLocationHandler
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.presentation.navigation.Screen
import org.envobyte.weatherforecast.presentation.screen.component.AppDrawerSheet
import org.envobyte.weatherforecast.presentation.screen.component.AppTopBar
import org.envobyte.weatherforecast.presentation.screen.component.DrawerItem
import org.envobyte.weatherforecast.presentation.screen.component.LocationPermissionScreen
import org.envobyte.weatherforecast.presentation.screen.component.ShimmerEffect
import org.envobyte.weatherforecast.presentation.screen.component.SunriseSunsetCard
import org.envobyte.weatherforecast.presentation.screen.component.WeatherIcon
import org.envobyte.weatherforecast.presentation.theme.HomeScreenGradient
import org.envobyte.weatherforecast.presentation.theme.PrimaryTextColor
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.cloudy_weather
import weatherly.composeapp.generated.resources.ic_cloudy_sun
import weatherly.composeapp.generated.resources.img_bg_sunrise
import weatherly.composeapp.generated.resources.img_bg_sunset
import weatherly.composeapp.generated.resources.img_sunrise
import weatherly.composeapp.generated.resources.img_sunset

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
                    onClick = {
                        val weatherJson = Json.encodeToString(it)
                        navController.navigate(Screen.Details(weatherJson = weatherJson))
                    },
                    drawerItemsClick = { item ->
                        when (item) {
                            DrawerItem.FEEDBACK -> {
                                navController.navigate(Screen.Feedback)
                                print("Feedback clicked")
                            }
                            DrawerItem.FAQ -> {
                                // Handle Faq click
                            }
                            DrawerItem.SETTINGS -> {
                                // Handle Settings click
                            }
                            DrawerItem.UPDATE -> {
                                // Handle Update click
                            }
                            DrawerItem.WEATHER_INFO -> {
                                // Handle Weather Info click
                            }
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    locationName: String,
    weatherData: WeatherData,
    onClick: () -> Unit,
    drawerItemsClick: (DrawerItem) -> Unit
){

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
                        },
                        onClick = {
                            scope.launch {
                                modalDrawerState.close()
                                drawerItemsClick(it)
                            }

                        }
                    )
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
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()),
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            TemperatureSection(
                                locationName,
                                weatherData.current.temperature,
                                weatherData.current.condition
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            WeatherDetailsCard(
                                humidity = weatherData.current.humidity,
                                windSpeed = weatherData.current.windSpeed,
                                precipitation = weatherData.current.precipitation,
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            DailyForCast(weatherData, onClick = onClick)

                            val todayForecast = weatherData.dailyForecasts.firstOrNull()
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(bottom = 30.dp)
                                    .navigationBarsPadding()
                            ) {
                                SunriseSunsetCard(
                                    icon = Res.drawable.img_sunrise,
                                    title = "Sunrise",
                                    data = todayForecast?.sunriseTime ?: "",
                                    backgroundColor = Color(0xFFFFFFFF),
                                    contentColor = Color(0xFF000000),
                                    bottom = Res.drawable.img_bg_sunrise,
                                    modifier = Modifier.weight(1f)
                                )
                                SunriseSunsetCard(
                                    icon = Res.drawable.img_sunset,
                                    title = "Sunset",
                                    data = todayForecast?.sunsetTime ?: "",
                                    backgroundColor = Color(0xFFFFFFFF),
                                    contentColor = Color(0xFF000000),
                                    bottom = Res.drawable.img_bg_sunset,
                                    modifier = Modifier.weight(1f)
                                )
                            }


                        }
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
            text = locationName,
            style = MaterialTheme.typography.titleLarge.copy(
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
            text = weatherCondition,
            style = MaterialTheme.typography.bodyLarge.copy(
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
private fun DailyForCast(weatherData: WeatherData, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            .background(Color.White.copy(.8f), shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)).padding(16.dp)
    ) {
        Text(
            "Next days",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8E8E8E))
        )

        weatherData.dailyForecasts.forEach { forecast ->
            DailyForCastItem(
                modifier = Modifier.padding(vertical = 8.dp),
                icon = forecast.icon,
                temperature = forecast.averageTemperature,
                date = forecast.formattedDate,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun DailyForCastItem(
    modifier: Modifier = Modifier,
    icon: String,
    temperature: String,
    date: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        WeatherIcon(icon = icon)
        Spacer(Modifier.width(12.dp))
        Row(
            modifier = Modifier.weight(1f)
                .border(1.dp, color = Color.White, shape = RoundedCornerShape(360.dp))
                .clip(RoundedCornerShape(360.dp))
                .clickable {
                    onClick()
                }
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = temperature,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                    color = PrimaryTextColor
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = date,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF8E8E8E)
                )
            )
        }
    }
}
