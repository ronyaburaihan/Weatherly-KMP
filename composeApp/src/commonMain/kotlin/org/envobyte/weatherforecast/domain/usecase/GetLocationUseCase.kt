package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.core.permission.LocationData
import org.envobyte.weatherforecast.core.permission.LocationPermissionHandler
import org.envobyte.weatherforecast.core.permission.PermissionStatus

class GetLocationUseCase {
    suspend operator fun invoke(handler: LocationPermissionHandler): Result<LocationData> {
        val hasPermission = handler.isLocationPermissionGranted()
        val granted = if (hasPermission) true else handler.requestLocationPermission() == PermissionStatus.GRANTED
        if (!granted) return Result.failure(IllegalStateException("Location permission not granted"))

        val location = handler.getCurrentLocation()
        return location?.let { Result.success(it) }
            ?: Result.failure(IllegalStateException("Failed to get current location"))
    }
}