package org.envobyte.weatherforecast.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
actual fun WeatherlyTheme(
    isDarkMode: Boolean,
    content: @Composable (() -> Unit)
) {
    CompositionLocalProvider {
        MaterialTheme(
            colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme(),
            typography = typography,
            content = content
        )
    }
}