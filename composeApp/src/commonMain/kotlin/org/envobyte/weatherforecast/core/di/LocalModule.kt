package org.envobyte.weatherforecast.core.di


import org.envobyte.weatherforecast.core.platform.DataStoreFactory
import org.envobyte.weatherforecast.data.local.preferences.PreferencesDataSource
import org.envobyte.weatherforecast.data.local.preferences.PreferencesDataSourceImpl
import org.koin.dsl.module


val localModule = module {
    single { get<DataStoreFactory>().createDataStore() }
    single<PreferencesDataSource> { PreferencesDataSourceImpl(get()) }
}