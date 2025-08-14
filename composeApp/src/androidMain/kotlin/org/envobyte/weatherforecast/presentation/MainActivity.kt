package org.envobyte.weatherforecast.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.envobyte.weatherforecast.App
import org.envobyte.weatherforecast.AppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            appViewModel.uiState.value.initialScreen == null
        }

        setContent {
            App(
                isSystemInDarkTheme(),
                viewModel = appViewModel
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(isDarkMode = false)
}
