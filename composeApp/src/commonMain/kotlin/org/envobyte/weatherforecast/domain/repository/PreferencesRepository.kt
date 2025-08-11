package org.envobyte.weatherforecast.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun isFirstLaunch(): Flow<Boolean>

    suspend fun updateFirstLaunch(isFirstLaunch: Boolean)

}