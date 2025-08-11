package org.envobyte.weatherforecast.core.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidLocationPermissionHandler(
    private val activity: Activity
) : LocationPermissionHandler {

    private val requestCode = 2001

    override suspend fun requestLocationPermission(): PermissionStatus {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return suspendCancellableCoroutine { cont ->
            val granted = permissions.all {
                ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
            }

            if (granted) {
                cont.resume(PermissionStatus.GRANTED)
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode)
                // You need to handle onRequestPermissionsResult in Activity:
                // Resume continuation based on result
            }
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
}