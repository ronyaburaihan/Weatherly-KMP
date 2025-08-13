package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.core.permission.LocationManager
import org.envobyte.weatherforecast.domain.model.LocationData

class GetCurrentLocationUseCase(
) {
    suspend operator fun invoke(locationManager: LocationManager): LocationData? {
        return locationManager.getCurrentLocation()
    }
}