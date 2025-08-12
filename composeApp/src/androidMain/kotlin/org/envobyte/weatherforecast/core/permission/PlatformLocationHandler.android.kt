package org.envobyte.weatherforecast.core.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CompletableDeferred

@Composable
actual fun getPlatformLocationHandler(): LocationManager {
    val context = LocalContext.current

    var pending by remember { mutableStateOf<CompletableDeferred<PermissionStatus>?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result: Map<String, Boolean> ->
            val grantedNow = result.values.all { it }
            pending?.complete(if (grantedNow) PermissionStatus.GRANTED else PermissionStatus.DENIED)
            pending = null
        }
    )

    return object : LocationManager {
        override suspend fun requestLocationPermission(): PermissionStatus {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (arePermissionsGranted(context, permissions)) {
                return PermissionStatus.GRANTED
            }
            val deferred = CompletableDeferred<PermissionStatus>()
            pending = deferred
            launcher.launch(permissions)
            return deferred.await()
        }

        override suspend fun isLocationPermissionGranted(): Boolean {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            return arePermissionsGranted(context, permissions)
        }

        override suspend fun getCurrentLocation(): LocationData? {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (!arePermissionsGranted(context, permissions)) return null
            return LocationProvider(context).getCurrentLocation()
        }
    }
}

private fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean =
    permissions.all { perm ->
        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
    }