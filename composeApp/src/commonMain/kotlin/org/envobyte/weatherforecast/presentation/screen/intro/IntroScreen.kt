package org.envobyte.weatherforecast.presentation.screen.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import org.envobyte.weatherforecast.presentation.navigation.Screen

@Composable
fun IntroScreen(
    navController: NavHostController
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            navController.navigate(Screen.Home) {
                //popUpTo(0)
            }
        }) {
            Text("Navigate to Home")
        }
    }
}