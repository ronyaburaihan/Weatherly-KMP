package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.core.permission.LocationManager

class GetLocationPermissionStatusUseCase() {
    suspend operator fun invoke(locationManager: LocationManager): Boolean {
        return locationManager.isLocationPermissionGranted()
    }
}