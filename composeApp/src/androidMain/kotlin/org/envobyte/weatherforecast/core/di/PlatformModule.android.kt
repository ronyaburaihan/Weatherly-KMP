package org.envobyte.weatherforecast.core.di

import android.content.Context
import org.envobyte.weatherforecast.core.platform.DataStoreFactory
import org.koin.dsl.module

actual val platformModule = module {
    single { DataStoreFactory(get<Context>()) }
}