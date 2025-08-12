package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.model.PermissionState
import org.envobyte.weatherforecast.domain.repository.LocationRepository

class RequestLocationPermissionUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<PermissionState> {
        return locationRepository.requestLocationPermission()
    }
}