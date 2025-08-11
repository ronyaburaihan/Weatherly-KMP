package org.envobyte.weatherforecast.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object Intro : Screen()

    @Serializable
    data object Home : Screen()
}