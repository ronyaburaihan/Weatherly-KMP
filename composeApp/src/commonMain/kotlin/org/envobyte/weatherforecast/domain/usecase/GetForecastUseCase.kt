package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.model.ForecastDay
import org.envobyte.weatherforecast.domain.repository.WeatherRepository

class GetForecastUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(): Result<List<ForecastDay>> {
        return repository.getForecast(
            lat = 22.8373,
            lon = 89.5400
        )
    }
}