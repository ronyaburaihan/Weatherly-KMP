package org.envobyte.weatherforecast.core.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.PermissionState
import kotlin.coroutines.resume

class AndroidLocationManager(
    private val activity: ComponentActivity
) : LocationManager {

    override suspend fun requestLocationPermission(): PermissionState {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        // If already granted, return immediately
        val alreadyGranted = permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
        if (alreadyGranted) return PermissionState.GRANTED

        return suspendCancellableCoroutine { cont ->
            val launcher = activity.activityResultRegistry.register(
                "location_permission_${System.nanoTime()}",
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result: Map<String, Boolean> ->
                val grantedNow = result.values.all { it }
                val status = if (grantedNow) PermissionState.GRANTED else PermissionState.DENIED
                if (cont.isActive) cont.resume(status)
            }

            cont.invokeOnCancellation { launcher.unregister() }
            launcher.launch(permissions)
        }
    }

    override suspend fun isLocationPermissionGranted(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override suspend fun getCurrentLocation(): LocationData? {
        // Delegate to shared provider for consistency
        return LocationProvider(activity).getCurrentLocation()
    }
}