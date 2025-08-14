package org.envobyte.weatherforecast.presentation.screen.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TemperatureSplineChart(
    modifier: Modifier = Modifier,
    temps: List<Float>,
    labels: List<String>,
    isToday: Boolean
) {
    require(temps.size == labels.size && temps.size >= 2)

    val now = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
    val nowHourF = remember(now) { now.hour + now.minute / 60f }

    val scrollState = rememberScrollState()
    val hourWidth = 56.dp
    val hPadding = 16.dp
    val totalWidth = hourWidth * (temps.size - 1) + hPadding * 2

    fun labelToHour24(label: String): Int? {
        val s = label.trim().lowercase()
        s.toIntOrNull()?.let { return it.coerceIn(0, 23) }
        s.substringBefore(':').toIntOrNull()?.let { return it.coerceIn(0, 23) }

        val m = Regex("""^\s*(\d{1,2})\s*([ap])m\s*$""").matchEntire(s)
        if (m != null) {
            val h12 = m.groupValues[1].toInt().coerceIn(1, 12)
            val ap = m.groupValues[2]
            val h24 = when (ap) {
                "a" -> if (h12 == 12) 0 else h12
                else -> if (h12 == 12) 12 else h12 + 12
            }
            return h24
        }

        val m2 = Regex("""^\s*(\d{1,2})\s*(am|pm)\s*$""").matchEntire(s)
        if (m2 != null) {
            val h12 = m2.groupValues[1].toInt().coerceIn(1, 12)
            val ap = m2.groupValues[2]
            val h24 = when (ap) {
                "am" -> if (h12 == 12) 0 else h12
                else -> if (h12 == 12) 12 else h12 + 12
            }
            return h24
        }

        Regex("""\d+""").find(s)?.value?.toIntOrNull()?.let { raw ->
            return raw.coerceIn(0, 23)
        }
        return null
    }

    val labelHours: List<Int> = remember(labels) {
        labels.mapNotNull { labelToHour24(it) }
    }

    val highlightIndex: Int? = remember(labelHours, nowHourF, isToday) {
        if (!isToday || labelHours.size != labels.size) null
        else {
            labelHours.indices.minByOrNull { i -> kotlin.math.abs(labelHours[i] - nowHourF) }
        }
    }


    var thumbX by remember { mutableStateOf<Float?>(null) }
    val textMeasurer = rememberTextMeasurer()
    Column(modifier, horizontalAlignment = Alignment.Start) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .horizontalScroll(scrollState)
                .height(240.dp)
        ) {
            Box(
                Modifier.width(totalWidth).padding(
                    vertical = 10.dp
                )
            ) {
                var sampled by remember { mutableStateOf(emptyList<Offset>()) }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {


                    val w = size.width
                    val h = size.height

                    val left = 8.dp.toPx()
                    val right = w - 8.dp.toPx()
                    val top = 8.dp.toPx()
                    val bottom = h - 8.dp.toPx()
                    val chartTop = top + (bottom - top) * 0.35f
                    val chartBottom = bottom - (bottom - top) * 0.15f
                    val fillGrad = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFB9F43).copy(.4f),
                            Color(0xFFFCA044).copy(.4f),
                            Color(0xFFD1A46C).copy(.4f),
                            Color(0xFF4DB2E8).copy(.4f),
                            Color(0xFFAAA88F).copy(.4f),
                            Color(0xFFFB9F43).copy(.4f)
                        )
                    )
                    val lineGrad = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFB9F43),
                            Color(0xFFFCA044),
                            Color(0xFFD1A46C),
                            Color(0xFF4DB2E8),
                            Color(0xFFAAA88F),
                            Color(0xFFFB9F43)
                        )
                    )

                    val gridColor = Color(0x11000000)
                    repeat(6) { i ->
                        val y = top + (bottom - top) * (i / 5f)
                        drawLine(
                            color = gridColor,
                            start = Offset(left, y),
                            end = Offset(right, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    val domainMin = 20f
                    val domainMax = 40f

                    val minT = domainMin
                    val maxT = domainMax
                    val spanY = (maxT - minT).coerceAtLeast(0.1f)

                    val rawMin = temps.minOrNull() ?: 0f
                    val rawMax = temps.maxOrNull() ?: 1f
                    val pad = 0.5f

                    fun mapX(i: Int): Float {
                        val step = (right - left) / (temps.lastIndex)
                        return left + step * i
                    }

                    fun mapY(t: Float): Float {
                        val norm = (t - minT) / spanY
                        return chartBottom - (chartBottom - chartTop) * norm
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
                            for (i in 1 until clamped.size) lineTo(clamped[i].x, clamped[i].y)
                            lineTo(clamped.last().x, bottom)
                            close()
                        }
                    }

                    drawPath(fillPath, brush = fillGrad, style = Fill)
                    val endPaddingPx = 20.dp.toPx()

                    for (i in temps.indices) {
                        val desiredX = mapX(i)

                        val sel = clamped.minByOrNull { p -> kotlin.math.abs(p.x - desiredX) }
                        val yOnCurve = sel?.y ?: mapY(temps[i])

                        val text = "${temps[i].toInt()}°"
                        val style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2E3A44)
                        )

                        val layout = textMeasurer.measure(AnnotatedString(text), style = style)

                        val dy = 8.dp.toPx()
                        var textX = desiredX - layout.size.width / 2f
                        val textY =
                            (yOnCurve - dy - layout.size.height).coerceAtLeast(top + 2.dp.toPx())

                        if (i == temps.lastIndex) {
                            textX -= endPaddingPx
                        }
                        if (i == 0) {
                            textX += endPaddingPx - 12
                        }
                        drawText(layout, topLeft = Offset(textX, textY))
                    }

                    drawPath(
                        path,
                        brush = lineGrad,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    if (isToday && highlightIndex != null && highlightIndex in temps.indices) {
                        val desiredX = mapX(highlightIndex)
                        val sel = clamped.minByOrNull { p -> abs(p.x - desiredX) }
                        if (sel != null) {
                            drawCircle(
                                color = Color(0x22000000),
                                radius = 10.dp.toPx(),
                                center = Offset(sel.x, sel.y + 1.8f)
                            )
                            drawCircle(color = Color.White, radius = 8.dp.toPx(), center = sel)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = hPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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


fun catmullRom(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset {
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

fun sampleCatmullRom(points: List<Offset>, stepsPerSegment: Int): List<Offset> {
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
