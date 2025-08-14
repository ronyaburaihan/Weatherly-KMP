package org.envobyte.weatherforecast.presentation.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.jetbrains.compose.resources.painterResource
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.close
import weatherly.composeapp.generated.resources.current_weather
import weatherly.composeapp.generated.resources.faq
import weatherly.composeapp.generated.resources.feedback
import weatherly.composeapp.generated.resources.info
import weatherly.composeapp.generated.resources.settings


enum class DrawerItem {
    WEATHER_INFO,
    UPDATE,
    FEEDBACK,
    FAQ,
    SETTINGS
}

@Composable
fun AppDrawerSheet(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    weatherData: WeatherData,
    onDismiss: () -> Unit,
    onClick: (DrawerItem) -> Unit
) {
    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = modifier.fillMaxWidth(),
        drawerContainerColor = Color.White,
        drawerContentColor = Color.Black,
        drawerTonalElevation = 10.dp,
        windowInsets = WindowInsets.statusBars,
        content = {
            DrawerContent(
                weatherData = weatherData, onDismiss = onDismiss,
                onclick = onClick
            )
        }
    )
}

@Composable
private fun DrawerContent(
    weatherData: WeatherData,
    onDismiss: () -> Unit,
    onclick: (DrawerItem) -> Unit
) {


    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = {
            onDismiss()

        }, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(
                painterResource(Res.drawable.close),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(modifier = Modifier) {
                WeatherIcon(
                    weatherData.current.icon,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Weatherly",
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Text(text = "Version 1.0", textAlign = TextAlign.Center, fontSize = 15.sp)
            Spacer(Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .background(Color.LightGray.copy(.3f)).height(1.dp)
                    .widthIn(190.dp)
            )

            Spacer(Modifier.height(36.dp))
            NavigationItem(
                title = "Weather Info",
                painter = painterResource(Res.drawable.current_weather),
                onClick = { onclick(DrawerItem.WEATHER_INFO) },
            )
            Spacer(Modifier.height(12.dp))
            NavigationItem(
                title = "Update",
                painter = painterResource(Res.drawable.info),
                onClick = { onclick(DrawerItem.UPDATE) },
            )
            Spacer(Modifier.height(12.dp))
            NavigationItem(
                title = "Feedback",
                painter = painterResource(Res.drawable.feedback),
                onClick = { onclick(DrawerItem.FEEDBACK) },
            )
            Spacer(Modifier.height(12.dp))
            NavigationItem(
                title = "Faq",
                painter = painterResource(Res.drawable.faq),
                onClick = { onclick(DrawerItem.FAQ) }
            )
            Spacer(Modifier.height(12.dp))
            NavigationItem(
                title = "Settings",
                painter = painterResource(Res.drawable.settings),
                onClick = { onclick(DrawerItem.SETTINGS) }
            )
        }
    }

}


@Composable
private fun NavigationItem(
    title: String,
    painter: Painter,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            ),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(10.dp))
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF262E3D)
        )
    }
}