package org.envobyte.weatherforecast.presentation.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.envobyte.weatherforecast.presentation.theme.PrimaryTextColor
import org.jetbrains.compose.resources.painterResource
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.menu_button

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    subtitle: String = "",
    onMenuClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = PrimaryTextColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        actions = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.menu_button),
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
    )
}

