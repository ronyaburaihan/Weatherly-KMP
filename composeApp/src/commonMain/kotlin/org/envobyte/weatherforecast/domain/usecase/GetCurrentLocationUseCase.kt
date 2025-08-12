package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.repository.LocationRepository

class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<LocationData> {
        return locationRepository.getCurrentLocation()
    }
}