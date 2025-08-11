package org.envobyte.weatherforecast.core.di

import org.koin.dsl.module

val appModule = module {
    includes(platformModule, localModule, networkModule, repositoryModule, useCaseModule, viewModelModule)
}