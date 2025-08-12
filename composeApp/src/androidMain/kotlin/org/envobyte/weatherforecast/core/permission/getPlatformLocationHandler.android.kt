package org.envobyte.weatherforecast.core.permission

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.envobyte.weatherforecast.core.permission.AndroidLocationPermissionHandler

@SuppressLint("ContextCastToActivity")
@Composable
actual fun getPlatformLocationHandler(): LocationPermissionHandler {
    val activity = LocalContext.current as ComponentActivity
    return AndroidLocationPermissionHandler(activity)
}

@Composable
actual fun getPlatformContext(): Any? {
    return LocalContext.current
}