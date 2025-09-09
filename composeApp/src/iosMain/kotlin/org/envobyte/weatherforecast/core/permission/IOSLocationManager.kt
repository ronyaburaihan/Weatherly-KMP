package org.envobyte.weatherforecast.core.permission

import kotlinx.coroutines.suspendCancellableCoroutine
import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.PermissionState
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.Continuation

private class LocationPermissionDelegate(
    private val continuation: Continuation<PermissionState>
) : NSObject(), CLLocationManagerDelegateProtocol {
    
    override fun locationManagerDidChangeAuthorization(
        manager: CLLocationManager
    ) {
        val status = CLLocationManager.Companion.authorizationStatus()
        val permissionState = when (status) {
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionState.GRANTED
            kCLAuthorizationStatusDenied -> PermissionState.DENIED
            kCLAuthorizationStatusRestricted -> PermissionState.PERMANENTLY_DENIED
            kCLAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
            else -> PermissionState.DENIED
        }
        continuation.resume(permissionState)
    }
}

class IOSLocationManager : LocationManager {
    private val locationManager = CLLocationManager()
    
    override suspend fun requestLocationPermission(): PermissionState {
        return suspendCancellableCoroutine { cont ->
            val status = CLLocationManager.Companion.authorizationStatus()

            when (status) {
                kCLAuthorizationStatusAuthorizedAlways,
                kCLAuthorizationStatusAuthorizedWhenInUse -> {
                    cont.resume(PermissionState.GRANTED)
                }

                kCLAuthorizationStatusDenied -> {
                    cont.resume(PermissionState.DENIED)
                }

                kCLAuthorizationStatusRestricted -> {
                    cont.resume(PermissionState.PERMANENTLY_DENIED)
                }

                kCLAuthorizationStatusNotDetermined -> {
                    val delegate = LocationPermissionDelegate(cont)
                    locationManager.delegate = delegate
                    locationManager.requestWhenInUseAuthorization()
                }

                else -> cont.resume(PermissionState.DENIED)
            }
        }
    }

    override suspend fun isLocationPermissionGranted(): Boolean {
        val status = CLLocationManager.Companion.authorizationStatus()
        return status == kCLAuthorizationStatusAuthorizedAlways ||
                status == kCLAuthorizationStatusAuthorizedWhenInUse
    }

    override suspend fun getCurrentLocation(): LocationData? {
        return LocationProvider(locationManager).getCurrentLocation()
    }
}