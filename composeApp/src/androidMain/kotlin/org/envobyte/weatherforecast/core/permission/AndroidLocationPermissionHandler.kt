package org.envobyte.weatherforecast.core.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidLocationPermissionHandler(
    private val activity: ComponentActivity
) : LocationPermissionHandler {

    override suspend fun requestLocationPermission(): PermissionStatus {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // If already granted, return immediately
        val alreadyGranted = permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
        if (alreadyGranted) return PermissionStatus.GRANTED

        return suspendCancellableCoroutine { cont ->
            val launcher = activity.activityResultRegistry.register(
                "location_permission_${System.nanoTime()}",
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result: Map<String, Boolean> ->
                val grantedNow = result.values.all { it }
                val status = if (grantedNow) PermissionStatus.GRANTED else PermissionStatus.DENIED
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