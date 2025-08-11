package org.envobyte.weatherforecast.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.envobyte.weatherforecast.core.permission.LocationProvider
import org.envobyte.weatherforecast.core.permission.ensureLocationPermission
import org.envobyte.weatherforecast.core.permission.getCurrentLocation
import org.envobyte.weatherforecast.core.permission.getPlatformLocationHandler

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    Box {
        Text(text = "Weatherly App")

        val handler = getPlatformLocationHandler() // This gets the platform-specific handler
        val provider = LocationProvider(this)
        // Suppose you have a LaunchedEffect to request location permission and get location:
        LaunchedEffect(Unit) {
            val granted = ensureLocationPermission(handler)
            if (granted) {
                val location = getCurrentLocation(provider)
                // Do something with the location, e.g., update state, UI, etc.
               print(location)
            } else {
                // Handle permission not granted
            }
        }
    }
}