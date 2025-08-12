package org.envobyte.weatherforecast.core.di

import org.envobyte.weatherforecast.data.repository.LocationRepositoryImpl
import org.envobyte.weatherforecast.data.repository.PreferencesRepositoryImpl
import org.envobyte.weatherforecast.data.repository.WeatherRepositoryImpl
import org.envobyte.weatherforecast.domain.repository.LocationRepository
import org.envobyte.weatherforecast.domain.repository.PreferencesRepository
import org.envobyte.weatherforecast.domain.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get(), get()) }
}