package org.envobyte.weatherforecast.core.di

import org.koin.dsl.module

val appModule = module {
    includes(localModule, networkModule, repositoryModule, useCaseModule, viewModelModule)
}