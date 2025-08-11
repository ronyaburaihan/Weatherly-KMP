package org.envobyte.weatherforecast.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import org.envobyte.weatherforecast.presentation.screen.home.HomeScreen
import org.envobyte.weatherforecast.presentation.screen.intro.IntroScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = remember(backstackEntry) {
        backstackEntry?.destination?.route?.substringBefore("?")
    }

    val topBarScreens = remember {
        setOf(
            Screen.Home::class.qualifiedName,
        )
    }

    val fullScreenScreens = remember {
        setOf(
            Screen.Intro::class.qualifiedName,
            Screen.Home::class.qualifiedName,
        )
    }

    val showTopBar = remember(currentRoute) { currentRoute in topBarScreens }
    val isFullScreen = remember(currentRoute) { currentRoute in fullScreenScreens }

    Scaffold(
        /*topBar = {
            AnimatedVisibility(
                visible = showTopBar,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {

            }
        },*/
    ) { innerPadding ->
        val animatedPadding by animateDpAsState(
            targetValue = if (!isFullScreen) innerPadding.calculateTopPadding() else 0.dp,
            animationSpec = tween(
                durationMillis = 250,
                easing = FastOutSlowInEasing
            ),
            label = "top_padding"
        )

        val animatedBottomPadding by animateDpAsState(
            targetValue = if (!isFullScreen) innerPadding.calculateBottomPadding() else 0.dp,
            animationSpec = tween(
                durationMillis = 250,
                easing = FastOutSlowInEasing
            ),
            label = "bottom_padding"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = animatedPadding,
                    bottom = animatedBottomPadding
                )
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Intro,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable<Screen.Intro> {
                    IntroScreen(navController = navController)
                }
                composable<Screen.Home> {
                    HomeScreen(navController = navController)
                }
            }
        }
    }
}