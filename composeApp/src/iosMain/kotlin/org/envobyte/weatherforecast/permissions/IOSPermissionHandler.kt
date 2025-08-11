package org.envobyte.weatherforecast.permissions


import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IOSLocationPermissionHandler : NSObject(), CLLocationManagerDelegateProtocol, LocationPermissionHandler {

    private val locationManager = CLLocationManager()

    override suspend fun requestLocationPermission(): PermissionStatus {
        locationManager.delegate = this
        return suspendCancellableCoroutine { cont ->
            val status = CLLocationManager.authorizationStatus()

            when (status) {
                kCLAuthorizationStatusAuthorizedAlways,
                kCLAuthorizationStatusAuthorizedWhenInUse -> {
                    cont.resume(PermissionStatus.GRANTED)
                }
                kCLAuthorizationStatusDenied -> {
                    cont.resume(PermissionStatus.DENIED)
                }
                kCLAuthorizationStatusRestricted -> {
                    cont.resume(PermissionStatus.PERMANENTLY_DENIED)
                }
                kCLAuthorizationStatusNotDetermined -> {
                    locationManager.requestWhenInUseAuthorization()
                    // In delegate method, resume continuation
                }
                else -> cont.resume(PermissionStatus.DENIED)
            }
        }
    }

    override suspend fun isLocationPermissionGranted(): Boolean {
        val status = CLLocationManager.authorizationStatus()
        return status == kCLAuthorizationStatusAuthorizedAlways ||
                status == kCLAuthorizationStatusAuthorizedWhenInUse
    }
}