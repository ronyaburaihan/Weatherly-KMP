package org.envobyte.weatherforecast.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.envobyte.weatherforecast.core.permission.LocationProvider
import org.envobyte.weatherforecast.core.permission.ensureLocationPermission
import org.envobyte.weatherforecast.core.permission.getCurrentLocation
import org.envobyte.weatherforecast.core.permission.getPlatformLocationHandler
import org.envobyte.weatherforecast.core.permission.getPlatformContext
import org.envobyte.weatherforecast.presentation.screen.component.LocationPermissionScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val viewModel: HomeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val handler = getPlatformLocationHandler()
    val provider = LocationProvider(getPlatformContext())

    var permissionGranted by remember { mutableStateOf<Boolean?>(null) }
    var locationText by remember { mutableStateOf("Requesting location...") }

    LaunchedEffect(Unit) {
        // Check current permission state on entry
        permissionGranted = handler.isLocationPermissionGranted()
    }

    Box {
        Column {
            if (uiState.firstLaunchCompleted) {
                Text(text = "First launch is completed=" + uiState.firstLaunchCompleted)
            } else {
                Text(text = "First launch is completed=" + uiState.firstLaunchCompleted)
            }

            when (permissionGranted) {
                true -> {
                    // Fetch location once permission is granted
                    LaunchedEffect("fetch_location") {
                        val granted = ensureLocationPermission(handler)
                        locationText = if (granted) {
                            getCurrentLocation(provider)
                        } else {
                            "Location permission not granted"
                        }
                    }
                    Text(text = locationText)
                }
                false, null -> {
                    LocationPermissionScreen(
                        onPermissionGranted = {
                            permissionGranted = true
                            locationText = "Requesting location..."
                            viewModel.refreshWeather()
                        },
                        onPermissionDenied = {
                            permissionGranted = false
                        }
                    )
                }
            }
        }
    }
}