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
import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.PermissionState

@Composable
actual fun getPlatformLocationHandler(): LocationManager {
    val context = LocalContext.current

    var pending by remember { mutableStateOf<CompletableDeferred<PermissionState>?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result: Map<String, Boolean> ->
            val grantedNow = result.values.all { it }
            pending?.complete(if (grantedNow) PermissionState.GRANTED else PermissionState.DENIED)
            pending = null
        }
    )

    return object : LocationManager {
        override suspend fun requestLocationPermission(): PermissionState {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (arePermissionsGranted(context, permissions)) {
                return PermissionState.GRANTED
            }
            val deferred = CompletableDeferred<PermissionState>()
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