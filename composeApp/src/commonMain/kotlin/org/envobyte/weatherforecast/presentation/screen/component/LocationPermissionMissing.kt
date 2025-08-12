package org.envobyte.weatherforecast.presentation.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.envobyte.weatherforecast.core.permission.ensureLocationPermission
import org.envobyte.weatherforecast.core.permission.getPlatformLocationHandler
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Preview
@Composable
fun LocationPermissionScreen(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val handler = getPlatformLocationHandler()
    var statusText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📍", fontSize = 64.sp, textAlign = TextAlign.Center)
        Text("Enable your location", style = MaterialTheme.typography.headlineSmall)
        Text("Allow location access to get accurate weather for where you are.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            scope.launch {
                val granted = ensureLocationPermission(handler)
                if (granted) {
                    statusText = "Permission granted"
                    onPermissionGranted()
                } else {
                    statusText = "Permission denied"
                    onPermissionDenied()
                }
            }
        }) {
            Text("Allow location")
        }
        Spacer(Modifier.height(8.dp))
        Text("🛡️ You can change this anytime in Settings", style = MaterialTheme.typography.labelSmall)
        if (statusText.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(text = statusText, style = MaterialTheme.typography.labelSmall)
        }
    }
}