package org.envobyte.weatherforecast.presentation.screen.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.envobyte.weatherforecast.presentation.theme.WeatherIconGradient
import org.jetbrains.compose.resources.painterResource
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.ic_back
import weatherly.composeapp.generated.resources.ic_tmp
import kotlin.math.abs

@Composable
fun TemperatureSplineChart(
    modifier: Modifier = Modifier,
    temps: List<Float> = listOf(29f, 31f, 32f, 32f, 25f, 29f, 31f),
    labels: List<String> = listOf("12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM")
) {
    val isToday by remember { mutableStateOf(true) }
    require(temps.size == labels.size && temps.size >= 2)

    val strokeWidth = 3.dp
    val gridStroke = 1.dp
    val gridColor = Color(0x11000000)

    val gradColors = remember {
        listOf(
            Color(0xFFFB9F43).copy(.4f),
            Color(0xFFFCA044).copy(.4f),
            Color(0xFFD1A46C).copy(.4f),
            Color(0xFF4DB2E8).copy(.4f),
            Color(0xFFAAA88F).copy(.4f),
            Color(0xFFFB9F43).copy(.4f)
        )
    }

    val lineGradGradient = remember {
        listOf(
            Color(0xFFFB9F43),
            Color(0xFFFCA044),
            Color(0xFFD1A46C),
            Color(0xFF4DB2E8),
            Color(0xFFAAA88F),
            Color(0xFFFB9F43)
        )
    }

    var thumbX by remember { mutableStateOf<Float?>(null) }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            var sampled by remember { mutableStateOf(emptyList<Offset>()) }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures { p -> thumbX = p.x } }
                    .pointerInput(Unit) {
                        detectDragGestures { _, drag ->
                            thumbX = (thumbX ?: 0f) + drag.x
                        }
                    }
            ) {
                val w = size.width
                val h = size.height

                val left = 8.dp.toPx()
                val right = w - 8.dp.toPx()
                val top = 8.dp.toPx()
                val bottom = h - 8.dp.toPx()

                val fillGrad = Brush.linearGradient(
                    colors = gradColors,
                )
                val lineGrad = Brush.linearGradient(
                    colors = lineGradGradient,
                )

                repeat(6) { i ->
                    val y = top + (bottom - top) * (i / 5f)
                    drawLine(
                        color = gridColor,
                        start = Offset(left, y),
                        end = Offset(right, y),
                        strokeWidth = gridStroke.toPx()
                    )
                }

                val minT = temps.minOrNull() ?: 0f
                val maxT = temps.maxOrNull() ?: 1f
                val spanY = kotlin.math.max(1f, maxT - minT)

                fun mapX(i: Int): Float {
                    val step = (right - left) / (temps.lastIndex)
                    return left + step * i
                }

                fun mapY(t: Float): Float {
                    val norm = (t - minT) / spanY
                    return bottom - (bottom - top) * norm
                }

                val points = temps.mapIndexed { i, t -> Offset(mapX(i), mapY(t)) }
                sampled = sampleCatmullRom(points, stepsPerSegment = 24)

                val clamped = sampled.map { p ->
                    Offset(p.x.coerceIn(left, right), p.y.coerceIn(top, bottom))
                }
                val path = Path().apply {
                    if (clamped.isNotEmpty()) {
                        moveTo(clamped.first().x, clamped.first().y)
                        for (i in 1 until clamped.size) lineTo(clamped[i].x, clamped[i].y)
                    }
                }

                val fillPath = Path().apply {
                    if (clamped.isNotEmpty()) {
                        moveTo(clamped.first().x, bottom)
                        lineTo(clamped.first().x, clamped.first().y)
                        for (i in 1 until sampled.size) lineTo(clamped[i].x, clamped[i].y)
                        lineTo(clamped.last().x, bottom)
                        close()
                    }
                }

                drawPath(fillPath, brush = fillGrad, style = Fill)
                drawPath(
                    path,
                    brush = lineGrad,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )

                thumbX = when {
                    thumbX == null -> clamped.getOrNull(clamped.size / 2)?.x
                    else -> thumbX!!.coerceIn(left, right)
                }
                val sel = clamped.minByOrNull { p -> kotlin.math.abs(p.x - thumbX!!) }
                if (sel != null && isToday) {
                    drawCircle(
                        color = Color(0x22000000),
                        radius = 10.dp.toPx(),
                        center = Offset(sel.x, sel.y + 1.5f)
                    )
                    drawCircle(color = Color.White, radius = 7.dp.toPx(), center = sel)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = Color(0xFF7A7F87),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(min = 32.dp)
                )
            }
        }
    }
}


private fun catmullRom(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset {
    val t2 = t * t
    val t3 = t2 * t
    val x = 0.5f * ((2f * p1.x) +
            (-p0.x + p2.x) * t +
            (2f * p0.x - 5f * p1.x + 4f * p2.x - p3.x) * t2 +
            (-p0.x + 3f * p1.x - 3f * p2.x + p3.x) * t3)
    val y = 0.5f * ((2f * p1.y) +
            (-p0.y + p2.y) * t +
            (2f * p0.y - 5f * p1.y + 4f * p2.y - p3.y) * t2 +
            (-p0.y + 3f * p1.y - 3f * p2.y + p3.y) * t3)
    return Offset(x, y)
}

private fun sampleCatmullRom(points: List<Offset>, stepsPerSegment: Int): List<Offset> {
    if (points.size < 2) return points
    val out = mutableListOf<Offset>()
    val pts = buildList {
        add(points.first())
        addAll(points)
        add(points.last())
    }
    for (i in 0 until pts.size - 3) {
        val p0 = pts[i]
        val p1 = pts[i + 1]
        val p2 = pts[i + 2]
        val p3 = pts[i + 3]
        for (s in 0..stepsPerSegment) {
            val t = s / stepsPerSegment.toFloat()
            out += catmullRom(p0, p1, p2, p3, t)
        }
    }
    val dedup = mutableListOf<Offset>()
    for (p in out) {
        if (dedup.isEmpty() || abs(dedup.last().x - p.x) > 0.5f) dedup += p
    }
    return dedup
}


@Composable
fun DetailsScreen(date: String, navController: NavHostController) {
    DetailsContent(date = date, onClick = {
        navController.navigateUp()

    })
}

@Composable
fun DetailsContent(
    date: String,
    onClick: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(onClick = onClick)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Details", modifier = Modifier.padding(start = 16.dp))
            Spacer(Modifier.height(16.dp))
            DateTemperatureEntry()
            Spacer(Modifier.height(16.dp))
            WeatherInfoItem(date = date)
            TemperatureSplineChart()
            WeatherDescription()
        }
    }
}

@Composable
private fun WeatherDescription() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
            .background(WeatherIconGradient, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Text(
            "" +
                    "The weather forecast for today is mostly sunny " +
                    "with a mild temperature drop. The high will be" +
                    "around 25°C and the low will be around 19°C. A" +
                    "slight chance of rain is expected in the afternoon",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DateTemperatureEntry() {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val list = listOf(
        DayTempItem("T", "30"),
        DayTempItem("F", "32"),
        DayTempItem("S", "32"),
        DayTempItem("S", "29"),
        DayTempItem("M", "23"),
        DayTempItem("T", "34"),
        DayTempItem("W", "21"),
        DayTempItem("T", "26"),
    )
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(list) { index, item ->
            DateTemperatureItem(
                dayTempItem = item,
                onClick = {
                    selectedIndex = index
                },
                selected = selectedIndex == index
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onClick: () -> Unit) {
    TopAppBar(
        title = { Text("Details") },
        modifier = Modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(onClick = {
                onClick()
            }) {
                Icon(
                    painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
    )
}

data class DayTempItem(
    val day: String,
    val temperature: String
)

@Composable
private fun DateTemperatureItem(dayTempItem: DayTempItem, onClick: () -> Unit, selected: Boolean) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayTempItem.day,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
        )
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier.size(32.dp)
                .background(
                    brush = if (selected) WeatherIconGradient else SolidColor(Color.Transparent),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = dayTempItem.temperature, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun WeatherInfoItem(date: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier) {
            Text(text = date, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${30}°C",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "⛅",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 32.sp)
                )

            }
        }

        Row(
            modifier = Modifier.border(
                width = 1.dp,
                color = Color(0xFFE4E4E4),
                shape = RoundedCornerShape(360.dp)
            )
                .clip(RoundedCornerShape(360.dp))
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Temperature", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))
            Icon(
                painterResource(Res.drawable.ic_tmp),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}