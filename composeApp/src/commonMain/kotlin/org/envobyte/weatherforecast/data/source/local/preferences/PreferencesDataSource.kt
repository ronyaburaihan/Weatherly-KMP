package org.envobyte.weatherforecast.data.source.local.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    fun isFirstLaunch(): Flow<Boolean>
    suspend fun updateFirstLaunch(isFirstLaunch: Boolean)

}