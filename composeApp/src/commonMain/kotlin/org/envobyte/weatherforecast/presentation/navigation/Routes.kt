package org.envobyte.weatherforecast.presentation.navigation

import kotlinx.serialization.Serializable
import org.envobyte.weatherforecast.presentation.screen.feedback.FeedbackData

@Serializable
sealed class Screen {

    @Serializable
    data object Intro : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data class Details(val weatherJson: String) : Screen()

    @Serializable
    data object Feedback : Screen()
}