package org.envobyte.weatherforecast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import org.envobyte.weatherforecast.presentation.navigation.AppNavigation
import org.envobyte.weatherforecast.presentation.theme.WeatherlyTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    isDarkMode: Boolean,
    viewModel: AppViewModel = koinViewModel()
) {
    val appState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    appState.initialScreen?.let { initialScreen ->
        WeatherlyTheme(isDarkMode = isDarkMode) {
            AppNavigation(
                navController = navController,
                initialScreen = initialScreen
            )
        }
    }
}