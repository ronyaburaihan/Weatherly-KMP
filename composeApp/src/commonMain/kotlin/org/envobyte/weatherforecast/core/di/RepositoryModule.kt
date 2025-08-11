package org.envobyte.weatherforecast.core.di

import org.envobyte.weatherforecast.data.repository.PreferencesRepositoryImpl
import org.envobyte.weatherforecast.domain.repository.PreferencesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
}