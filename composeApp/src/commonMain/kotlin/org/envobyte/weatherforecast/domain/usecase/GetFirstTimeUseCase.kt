package org.envobyte.weatherforecast.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.envobyte.weatherforecast.domain.repository.PreferencesRepository

class GetFirstTimeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return preferencesRepository.isFirstLaunch()
    }
}