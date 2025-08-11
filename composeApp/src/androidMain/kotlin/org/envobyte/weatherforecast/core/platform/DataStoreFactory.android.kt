package org.envobyte.weatherforecast.core.platform

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

actual class DataStoreFactory(private val context: Context) {
    actual fun createDataStore(): DataStore<Preferences> {
        return context.dataStore
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)