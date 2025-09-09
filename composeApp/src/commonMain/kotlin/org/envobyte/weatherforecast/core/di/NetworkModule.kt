package org.envobyte.weatherforecast.core.di

import org.envobyte.weatherforecast.data.source.remote.RemoteApiService
import org.envobyte.weatherforecast.data.source.remote.RemoteApiServiceImpl
import org.envobyte.weatherforecast.data.source.remote.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single<RemoteApiService> { RemoteApiServiceImpl(get()) }

}