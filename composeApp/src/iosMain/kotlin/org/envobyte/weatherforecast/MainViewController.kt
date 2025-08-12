package org.envobyte.weatherforecast

import androidx.compose.ui.window.ComposeUIViewController
import org.envobyte.weatherforecast.core.di.initKoin
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle
import org.envobyte.weatherforecast.core.di.platformModule

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin {
            modules(platformModule)
        }
    }
) {
    val isDarkMode =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(isDarkMode = isDarkMode)
}
