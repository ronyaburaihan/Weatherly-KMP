package org.envobyte.weatherforecast.data.repository

import org.envobyte.weatherforecast.domain.repository.PreferencesRepository

import kotlinx.coroutines.flow.Flow
import org.envobyte.weatherforecast.data.source.local.preferences.PreferencesDataSource

class PreferencesRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource
) : PreferencesRepository {

    override fun isFirstLaunch(): Flow<Boolean> = preferencesDataSource.isFirstLaunch()


    override suspend fun updateFirstLaunch(isFirstLaunch: Boolean) {
        preferencesDataSource.updateFirstLaunch(isFirstLaunch)
    }

}