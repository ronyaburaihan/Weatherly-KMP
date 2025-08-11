package org.envobyte.weatherforecast

import androidx.compose.ui.window.ComposeUIViewController
import org.envobyte.weatherforecast.core.di.initKoin
import org.envobyte.weatherforecast.core.permission.LocationPermissionHandler
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val isDarkMode =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(isDarkMode = isDarkMode)
}
