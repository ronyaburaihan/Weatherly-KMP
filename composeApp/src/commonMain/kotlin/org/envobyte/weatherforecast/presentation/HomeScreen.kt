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



@Composable
fun HomeScreen() {
    Scaffold {
        Text(modifier = Modifier.padding(it), text = "Weatherly App")


        GetPermisson()


    }
}



@Composable
fun GetPermisson(){
    val scope = rememberCoroutineScope()
    val locationHandler = getPlatformLocationHandler()

    Button(
        onClick = {
            scope.launch {
                val hasPermission = ensureLocationPermission(locationHandler)
                if (hasPermission) {
                    // Permission granted
                } else {
                    // Permission denied
                }
            }
        }
    ) {
        Text("Request Location")
    }
}

@Composable
expect fun getPlatformLocationHandler(): LocationPermissionHandler

suspend fun ensureLocationPermission(handler: LocationPermissionHandler): Boolean {
    if (!handler.isLocationPermissionGranted()) {
        val result = handler.requestLocationPermission()
        return result == PermissionStatus.GRANTED
    }
    return true
}


