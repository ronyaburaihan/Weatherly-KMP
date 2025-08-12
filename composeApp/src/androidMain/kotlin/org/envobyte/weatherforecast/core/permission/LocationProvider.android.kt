package org.envobyte.weatherforecast.core.permission


import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class LocationProvider actual constructor(
    private val context: Any?
) {

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): LocationData? =
        suspendCancellableCoroutine { cont ->
            val fusedClient = LocationServices.getFusedLocationProviderClient(context as Context)
            fusedClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        cont.resume(LocationData(location.latitude, location.longitude))
                    } else {
                        fusedClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            CancellationTokenSource().token
                        ).addOnSuccessListener { fresh ->
                            if (fresh != null) {
                                cont.resume(LocationData(fresh.latitude, fresh.longitude))
                            } else {
                                cont.resume(null)
                            }
                        }.addOnFailureListener {
                            cont.resume(null)
                        }
                    }
                }
                .addOnFailureListener {
                    fusedClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        CancellationTokenSource().token
                    ).addOnSuccessListener { fresh ->
                        if (fresh != null) {
                            cont.resume(LocationData(fresh.latitude, fresh.longitude))
                        } else {
                            cont.resume(null)
                        }
                    }.addOnFailureListener {
                        cont.resume(null)
                    }
                }
        }
}