package org.envobyte.weatherforecast.presentation

import androidx.compose.runtime.Composable

@Composable
expect fun WeatherlyTheme(isDarkMode: Boolean, content: @Composable () -> Unit)