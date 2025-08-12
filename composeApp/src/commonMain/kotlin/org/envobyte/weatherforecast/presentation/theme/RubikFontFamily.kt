package org.envobyte.weatherforecast.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.rubik_bold
import weatherly.composeapp.generated.resources.rubik_medium
import weatherly.composeapp.generated.resources.rubik_regular
import weatherly.composeapp.generated.resources.rubik_semi_bold

@Composable
fun rubikFontFamily() = FontFamily(
    Font(Res.font.rubik_regular, weight = FontWeight.Normal),
    Font(Res.font.rubik_medium, weight = FontWeight.Medium),
    Font(Res.font.rubik_semi_bold, weight = FontWeight.SemiBold),
    Font(Res.font.rubik_bold, weight = FontWeight.Bold)
)


@Composable
fun rubikTypography() = Typography().run {
    val fontFamily = rubikFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}