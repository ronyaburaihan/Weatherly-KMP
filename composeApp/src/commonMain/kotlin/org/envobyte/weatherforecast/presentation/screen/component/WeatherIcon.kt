package org.envobyte.weatherforecast.presentation.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.envobyte.weatherforecast.presentation.theme.WeatherIconGradient

@Composable
fun WeatherIcon(icon : String,modifier: Modifier= Modifier){
    Box(
        modifier = modifier.size(64.dp).background(
            WeatherIconGradient, shape = CircleShape
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 30.sp
        )
    }
}