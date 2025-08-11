package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.model.WeatherData
import org.envobyte.weatherforecast.domain.repository.WeatherRepository

class GetWeatherDataUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(): Result<WeatherData> {
        return repository.getCompleteWeatherData(
            lat = 22.8373,
            lon = 89.5400
        )
    }
}