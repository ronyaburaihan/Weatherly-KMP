package org.envobyte.weatherforecast.core.di

import org.envobyte.weatherforecast.domain.usecase.GetCurrentLocationUseCase
import org.envobyte.weatherforecast.domain.usecase.GetFirstTimeUseCase
import org.envobyte.weatherforecast.domain.usecase.GetLocationNameUseCase
import org.envobyte.weatherforecast.domain.usecase.GetLocationPermissionStatusUseCase
import org.envobyte.weatherforecast.domain.usecase.GetWeatherDataUseCase
import org.envobyte.weatherforecast.domain.usecase.RequestLocationPermissionUseCase
import org.envobyte.weatherforecast.domain.usecase.SaveFirstTimeUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetFirstTimeUseCase(get()) }
    factory { SaveFirstTimeUseCase(get()) }
    factory { GetWeatherDataUseCase(get()) }
    factory { GetLocationNameUseCase(get()) }
    factory { GetLocationPermissionStatusUseCase(get()) }
    factory { RequestLocationPermissionUseCase(get()) }
    factory { GetCurrentLocationUseCase(get()) }
}