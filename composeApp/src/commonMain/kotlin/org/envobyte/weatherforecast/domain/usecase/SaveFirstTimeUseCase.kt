package org.envobyte.weatherforecast.domain.usecase

import org.envobyte.weatherforecast.domain.repository.PreferencesRepository

class SaveFirstTimeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isFirstLaunch: Boolean) {
        preferencesRepository.updateFirstLaunch(isFirstLaunch)
    }
}