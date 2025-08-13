package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.repository.LocationRepository

class GetLocationNameUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(locationData: LocationData): Result<String> {
        return locationRepository.getLocationName(
            locationData.latitude,
            locationData.longitude
        )
    }
}