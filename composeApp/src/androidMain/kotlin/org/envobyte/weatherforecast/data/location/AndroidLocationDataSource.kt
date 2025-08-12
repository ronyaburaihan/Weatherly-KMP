package org.envobyte.weatherforecast.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.PermissionState
import kotlin.coroutines.resume

class AndroidLocationDataSource(
    private val context: Context
) : LocationDataSource {

    override suspend fun requestLocationPermission(): Result<PermissionState> {
        return try {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (arePermissionsGranted(permissions)) {
                Result.success(PermissionState.GRANTED)
            } else {
                // For non-Composable contexts, return NOT_DETERMINED to indicate UI handling needed
                Result.success(PermissionState.NOT_DETERMINED)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isLocationPermissionGranted(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return arePermissionsGranted(permissions)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<LocationData> {
        return try {
            if (!isLocationPermissionGranted()) {
                return Result.failure(Exception("Location permission not granted"))
            }

            suspendCancellableCoroutine { cont ->
                val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                fusedClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            cont.resume(Result.success(LocationData(location.latitude, location.longitude)))
                        } else {
                            fusedClient.getCurrentLocation(
                                Priority.PRIORITY_HIGH_ACCURACY,
                                CancellationTokenSource().token
                            ).addOnSuccessListener { fresh ->
                                if (fresh != null) {
                                    cont.resume(Result.success(LocationData(fresh.latitude, fresh.longitude)))
                                } else {
                                    cont.resume(Result.failure(Exception("Unable to get location")))
                                }
                            }.addOnFailureListener { e ->
                                cont.resume(Result.failure(e))
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        cont.resume(Result.failure(e))
                    }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun arePermissionsGranted(permissions: Array<String>): Boolean =
        permissions.all { perm ->
            ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
        }
}