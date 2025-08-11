package org.envobyte.weatherforecast.presentation.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.envobyte.weatherforecast.presentation.navigation.Screen
import org.envobyte.weatherforecast.presentation.screen.component.PrimaryButton
import org.envobyte.weatherforecast.presentation.theme.IntroContentTextColor
import org.envobyte.weatherforecast.presentation.theme.PrimaryGradientBg
import org.envobyte.weatherforecast.presentation.theme.rubikFontFamily
import org.jetbrains.compose.resources.painterResource
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.ic_bg_circle
import weatherly.composeapp.generated.resources.ic_cloud
import weatherly.composeapp.generated.resources.ic_sun

@Composable
fun IntroScreen(
    navController: NavHostController
) {
    Column(modifier = Modifier.fillMaxSize().background(brush = PrimaryGradientBg)) {

        BoxWithConstraints(
            modifier = Modifier.weight(.7f).fillMaxWidth()
        ) {
            Image(
                painterResource(Res.drawable.ic_bg_circle),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Image(
                painterResource(Res.drawable.ic_sun),
                contentDescription = null,
                modifier = Modifier.padding(top = 63.dp).height(169.dp)
            )

            Image(
                painterResource(Res.drawable.ic_cloud),
                contentDescription = null,
                modifier = Modifier.padding(top = 63.dp, bottom = 25.dp).align(Alignment.BottomEnd)
            )
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .padding(bottom = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = 22.dp)
                ) {
                    Text(
                        "Never get caught \nin the rain again",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.W500,
                            color = IntroContentTextColor,
                            lineHeight = 39.sp,
                            fontFamily = rubikFontFamily(),
                        ),
                    )
                    Text(
                        "Stay ahead of the weather with our accurate forecasts",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = IntroContentTextColor,
                            fontFamily = rubikFontFamily()
                        )
                    )
                    Spacer(Modifier.height(36.dp))
                }
            }
        }
        PrimaryButton(
            onClick = {
                navController.navigate(Screen.Home)
            },
        )
        Spacer(
            Modifier
                .navigationBarsPadding()
                .height(58.dp)
        )
    }

}

