package org.envobyte.weatherforecast.presentation.screen.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val transitionAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerAnimation"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = transitionAnimation.value - 200f, y = 0f),
        end = Offset(x = transitionAnimation.value + 200f, y = 0f)
    )
}

@Composable
fun TopShimmer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    val brush = rememberShimmerBrush()
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Box(
                modifier = modifier
                    .clip(shape)
                    .background(brush)
                    .width(105.dp)
                    .height(20.dp)
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = modifier
                    .clip(shape)
                    .background(brush)
                    .width(105.dp)
                    .height(20.dp)
            )
        }
        Box(modifier = modifier.clip(CircleShape).size(40.dp).background(brush))
    }
}


@Composable
fun ShimmerForecastCard(
    rows: Int = 4,
) {
    val brush = rememberShimmerBrush()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {

            Box(
                modifier = Modifier.width(150.dp).height(24.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(brush)
            )
            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(rows) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(64.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(brush)
                    )
                }
            }
        }
    }
}

@Composable
fun TemperatureShimmer() {
    val brush = rememberShimmerBrush()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.width(126.dp).height(28.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(brush)
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.width(221.dp).height(114.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(brush)
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.width(97.dp).height(25.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(brush)
        )
    }
}

@Composable
fun WeatherDetailsShimmer() {
    val brush = rememberShimmerBrush()
    Box(
        modifier = Modifier.fillMaxWidth().height(82.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(brush)
    )
}


@Composable
fun ShimmerEffect() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 28.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Spacer(Modifier.height(16.dp))
        TopShimmer()
        Spacer(Modifier.height(37.dp))
        Spacer(Modifier.height(12.dp))
        TemperatureShimmer()
        Spacer(Modifier.height(36.dp))
        WeatherDetailsShimmer()
        Spacer(Modifier.height(24.dp))

        Spacer(Modifier.height(16.dp))
        ShimmerForecastCard(rows = 3)
    }
}
