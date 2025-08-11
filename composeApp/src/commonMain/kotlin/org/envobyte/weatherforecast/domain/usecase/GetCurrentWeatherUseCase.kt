package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.model.WeatherInfo
import org.envobyte.weatherforecast.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(): Result<WeatherInfo> {
        return repository.getCurrentWeather(
            lat = 22.8373,
            lon = 89.5400
        )
    }
}