package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.model.LocationData
import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.repository.WeatherRepository

class GetWeatherDataUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(locationData: LocationData): Result<WeatherData> {
        return repository.getCompleteWeatherData(
            lat = locationData.latitude,
            lon = locationData.longitude
        )
    }
}