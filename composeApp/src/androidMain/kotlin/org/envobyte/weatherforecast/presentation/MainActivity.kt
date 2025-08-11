package org.envobyte.weatherforecast.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.envobyte.weatherforecast.core.permission.AndroidLocationPermissionHandler
import org.envobyte.weatherforecast.App
import org.envobyte.weatherforecast.core.permission.LocationPermissionHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(isSystemInDarkTheme())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(isDarkMode = false)
}
