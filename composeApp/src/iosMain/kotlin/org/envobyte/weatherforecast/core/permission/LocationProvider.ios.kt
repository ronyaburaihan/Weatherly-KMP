package org.envobyte.weatherforecast.core.permission

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.*
import platform.darwin.NSObjectProtocol
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

private class LocationDelegate(
    private val onLocation: (LocationData?) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {
    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(
        manager: CLLocationManager,
        didUpdateLocations: List<*>
    ) {
        val loc = (didUpdateLocations.lastOrNull() as? CLLocation)?.coordinate?.useContents {
            LocationData(latitude, longitude)
        }
        onLocation(loc)
        manager.stopUpdatingLocation()
    }

    override fun locationManager(
        manager: CLLocationManager,
        didFailWithError: NSError
    ) {
        onLocation(null)
    }
}

actual class LocationProvider actual constructor(
    context: Any?
) {
    actual suspend fun getCurrentLocation(): LocationData? =
        suspendCancellableCoroutine { cont ->
            val manager = CLLocationManager()
            val delegate = LocationDelegate { cont.resume(it) }
            manager.delegate = delegate
            manager.startUpdatingLocation()
        }
}