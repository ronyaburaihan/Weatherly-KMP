package org.envobyte.weatherforecast.core.di

import org.envobyte.weatherforecast.core.platform.DataStoreFactory
import org.koin.dsl.module

val platformModule = module {
    single { DataStoreFactory() }
}
