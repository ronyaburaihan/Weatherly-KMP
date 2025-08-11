package org.envobyte.weatherforecast.core.di

import org.envobyte.weatherforecast.domain.usecase.GetFirstTimeUseCase
import org.envobyte.weatherforecast.domain.usecase.SaveFirstTimeUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetFirstTimeUseCase(get()) }
    factory { SaveFirstTimeUseCase(get()) }
}