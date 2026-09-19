package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlowingArcVisualizer(
    modifier: Modifier = Modifier,
    amplitude: Float = 0f,
    isListening: Boolean = false,
    isSpeaking: Boolean = false,
    isProcessing: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "jarvis_anim")

    val rotation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isListening) 4000 else 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot1"
    )

    val rotation2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSpeaking) 3000 else 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot2"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    val activeColor = when {
        isSpeaking -> NeonPurple
        isListening -> CyberCyan
        isProcessing -> NeonEmerald
        else -> ElectricBlue
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = (size.minDimension / 2f) * 0.75f
            val dynamicRadius = baseRadius * (if (isListening || isSpeaking) (1f + amplitude * 0.4f) else pulse)

            // 1. Ambient Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        activeColor.copy(alpha = if (isListening || isSpeaking) 0.35f else 0.15f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = dynamicRadius * 1.5f
                ),
                radius = dynamicRadius * 1.5f,
                center = center
            )

            // 2. Outer Segmented Ring 1 (Rotates clockwise)
            val outerStroke = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            val segments = 6
            val sweepAngle = 38f
            for (i in 0 until segments) {
                val startAngle = rotation1 + i * (360f / segments)
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(CyberCyan, ElectricBlue, NeonPurple, CyberCyan),
                        center = center
                    ),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = outerStroke,
                    topLeft = Offset(center.x - dynamicRadius, center.y - dynamicRadius),
                    size = androidx.compose.ui.geometry.Size(dynamicRadius * 2, dynamicRadius * 2)
                )
            }

            // 3. Middle Ring (Rotates counter-clockwise)
            val midRadius = dynamicRadius * 0.78f
            val midStroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Square)
            val midSegments = 4
            for (i in 0 until midSegments) {
                val startAngle = rotation2 + i * (360f / midSegments)
                drawArc(
                    color = activeColor.copy(alpha = 0.7f),
                    startAngle = startAngle,
                    sweepAngle = 60f,
                    useCenter = false,
                    style = midStroke,
                    topLeft = Offset(center.x - midRadius, center.y - midRadius),
                    size = androidx.compose.ui.geometry.Size(midRadius * 2, midRadius * 2)
                )
            }

            // 4. Inner Cyber Reactor Core
            val coreRadius = dynamicRadius * 0.5f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        activeColor,
                        activeColor.copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = coreRadius
                ),
                radius = coreRadius,
                center = center
            )

            // 5. Audio Waveform Oscilloscope inside the core
            if (isListening || isSpeaking || isProcessing) {
                val wavePath = Path()
                val waveWidth = coreRadius * 1.4f
                val startX = center.x - waveWidth / 2f
                val endX = center.x + waveWidth / 2f
                val points = 30
                val amp = if (isListening) (amplitude * 35f + 8f) else 15f

                for (i in 0..points) {
                    val progress = i.toFloat() / points
                    val x = startX + progress * waveWidth
                    val freq = 3.5f
                    val y = center.y + sin(progress * freq * 2 * PI + wavePhase).toFloat() * amp * (1f - kotlin.math.abs(progress - 0.5f) * 1.6f).coerceAtLeast(0f)

                    if (i == 0) {
                        wavePath.moveTo(x, y)
                    } else {
                        wavePath.lineTo(x, y)
                    }
                }

                drawPath(
                    path = wavePath,
                    color = Color.White,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}
