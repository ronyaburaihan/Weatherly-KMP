package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.repository.LocationRepository

class GetLocationPermissionStatusUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Boolean {
        return locationRepository.isLocationPermissionGranted()
    }
}