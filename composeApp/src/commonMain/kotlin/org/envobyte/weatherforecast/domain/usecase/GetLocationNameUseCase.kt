package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.repository.LocationRepository

class GetLocationNameUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<String> {
        return locationRepository.getLocationName(lat, lon)
    }
}