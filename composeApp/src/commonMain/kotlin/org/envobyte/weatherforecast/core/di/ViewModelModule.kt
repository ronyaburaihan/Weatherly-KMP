package org.envobyte.weatherforecast.core.di

import org.envobyte.weatherforecast.AppViewModel
import org.envobyte.weatherforecast.presentation.screen.home.HomeViewModel
import org.envobyte.weatherforecast.presentation.screen.intro.IntroViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AppViewModel(get()) }
    viewModel { IntroViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
}