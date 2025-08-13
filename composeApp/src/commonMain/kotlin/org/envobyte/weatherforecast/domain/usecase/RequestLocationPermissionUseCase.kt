package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.core.permission.LocationManager
import org.envobyte.weatherforecast.domain.model.PermissionState
import org.envobyte.weatherforecast.domain.repository.LocationRepository

class RequestLocationPermissionUseCase() {
    suspend operator fun invoke(locationManager: LocationManager): PermissionState {
        return locationManager.requestLocationPermission()
    }
}