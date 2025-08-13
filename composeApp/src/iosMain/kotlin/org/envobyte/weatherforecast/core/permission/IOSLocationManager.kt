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

class IOSLocationManager : NSObject(), CLLocationManagerDelegateProtocol,
    LocationManager {
    private val locationManager = CLLocationManager()
    override suspend fun requestLocationPermission(): PermissionState {
        locationManager.delegate = this
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
                    locationManager.requestWhenInUseAuthorization()
                    // In delegate method, resume continuation
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