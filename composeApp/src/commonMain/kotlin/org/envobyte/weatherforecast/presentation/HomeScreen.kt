package org.envobyte.weatherforecast.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.envobyte.weatherforecast.core.permission.LocationPermissionHandler
import org.envobyte.weatherforecast.core.permission.PermissionStatus
import org.envobyte.weatherforecast.core.permission.ensureLocationPermission
import org.envobyte.weatherforecast.core.permission.getPlatformLocationHandler


@Composable
fun HomeScreen() {
    Scaffold {
        Text(modifier = Modifier.padding(it), text = "Weatherly App")
    }
}

