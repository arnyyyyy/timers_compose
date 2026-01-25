package com.arno.timers_compose.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// AI GENERATED
object ForestColors {
        val FoxOrange = Color(0xFFE67E22)
        val FoxWhite = Color(0xFFFDFDFD)
        val FoxNose = Color(0xFF2C3E50)

        val HedgehogBrown = Color(0xFF8B7355)
        val HedgehogSpikes = Color(0xFF4A4A4A)
        val HedgehogNose = Color(0xFF2C3E50)

        val TreeGreen = Color(0xFF27AE60)
        val TreeDarkGreen = Color(0xFF1E8449)
        val TreeTrunk = Color(0xFF6D4C41)

        val MushroomRed = Color(0xFFE74C3C)
        val MushroomWhite = Color(0xFFFDFDFD)
        val MushroomStem = Color(0xFFF5F5DC)

        val OwlBrown = Color(0xFF8D6E63)
        val OwlLightBrown = Color(0xFFBCAAA4)
        val OwlEyes = Color(0xFFFFC107)

        val LeafGreen = Color(0xFF4CAF50)
        val LeafDarkGreen = Color(0xFF388E3C)

        val AcornBrown = Color(0xFF795548)
        val AcornCap = Color(0xFF5D4037)

        val BunnyGray = Color(0xFF9E9E9E)
        val BunnyPink = Color(0xFFFFCDD2)
}

@Composable
fun ForestFox(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                val earPath = Path().apply {
                        moveTo(w * 0.25f, h * 0.35f)
                        lineTo(w * 0.15f, h * 0.05f)
                        lineTo(w * 0.35f, h * 0.25f)
                        close()
                }
                drawPath(earPath, ForestColors.FoxOrange)

                val earPath2 = Path().apply {
                        moveTo(w * 0.75f, h * 0.35f)
                        lineTo(w * 0.85f, h * 0.05f)
                        lineTo(w * 0.65f, h * 0.25f)
                        close()
                }
                drawPath(earPath2, ForestColors.FoxOrange)

                drawCircle(
                        color = ForestColors.FoxOrange,
                        radius = w * 0.35f,
                        center = Offset(w * 0.5f, h * 0.45f)
                )

                val muzzlePath = Path().apply {
                        moveTo(w * 0.5f, h * 0.35f)
                        quadraticTo(w * 0.7f, h * 0.5f, w * 0.5f, h * 0.75f)
                        quadraticTo(w * 0.3f, h * 0.5f, w * 0.5f, h * 0.35f)
                        close()
                }
                drawPath(muzzlePath, ForestColors.FoxWhite)

                drawCircle(
                        color = ForestColors.FoxNose,
                        radius = w * 0.045f,
                        center = Offset(w * 0.38f, h * 0.4f)
                )
                drawCircle(
                        color = ForestColors.FoxNose,
                        radius = w * 0.045f,
                        center = Offset(w * 0.62f, h * 0.4f)
                )

                drawCircle(
                        color = Color.White,
                        radius = w * 0.015f,
                        center = Offset(w * 0.37f, h * 0.39f)
                )
                drawCircle(
                        color = Color.White,
                        radius = w * 0.015f,
                        center = Offset(w * 0.61f, h * 0.39f)
                )

                val nosePath = Path().apply {
                        moveTo(w * 0.5f, h * 0.52f)
                        lineTo(w * 0.45f, h * 0.58f)
                        lineTo(w * 0.55f, h * 0.58f)
                        close()
                }
                drawPath(nosePath, ForestColors.FoxNose)
        }
}

@Composable
fun ForestHedgehog(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                for (i in 0..12) {
                        val angle = Math.toRadians((i * 15 + 120).toDouble())
                        val startX = w * 0.45f + (w * 0.2f * kotlin.math.cos(angle)).toFloat()
                        val startY = h * 0.5f + (h * 0.2f * kotlin.math.sin(angle)).toFloat()
                        val endX = w * 0.45f + (w * 0.42f * kotlin.math.cos(angle)).toFloat()
                        val endY = h * 0.5f + (h * 0.42f * kotlin.math.sin(angle)).toFloat()

                        drawLine(
                                color = ForestColors.HedgehogSpikes,
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = w * 0.04f
                        )
                }

                drawOval(
                        color = ForestColors.HedgehogBrown,
                        topLeft = Offset(w * 0.15f, h * 0.35f),
                        size = Size(w * 0.6f, h * 0.45f)
                )

                val muzzlePath = Path().apply {
                        moveTo(w * 0.7f, h * 0.5f)
                        quadraticTo(w * 0.9f, h * 0.55f, w * 0.85f, h * 0.65f)
                        quadraticTo(w * 0.75f, h * 0.7f, w * 0.65f, h * 0.6f)
                        close()
                }
                drawPath(muzzlePath, ForestColors.HedgehogLightBrown)

                drawCircle(
                        color = ForestColors.HedgehogNose,
                        radius = w * 0.04f,
                        center = Offset(w * 0.72f, h * 0.52f)
                )
                drawCircle(
                        color = Color.White,
                        radius = w * 0.012f,
                        center = Offset(w * 0.71f, h * 0.51f)
                )

                drawCircle(
                        color = ForestColors.HedgehogNose,
                        radius = w * 0.035f,
                        center = Offset(w * 0.84f, h * 0.58f)
                )
        }
}

private val ForestColors.HedgehogLightBrown: Color
        get() = Color(0xFFD7CCC8)

@Composable
fun ForestTree(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                drawRect(
                        color = ForestColors.TreeTrunk,
                        topLeft = Offset(w * 0.4f, h * 0.7f),
                        size = Size(w * 0.2f, h * 0.3f)
                )

                val bottomLayer = Path().apply {
                        moveTo(w * 0.5f, h * 0.25f)
                        lineTo(w * 0.15f, h * 0.75f)
                        lineTo(w * 0.85f, h * 0.75f)
                        close()
                }
                drawPath(bottomLayer, ForestColors.TreeDarkGreen)

                val middleLayer = Path().apply {
                        moveTo(w * 0.5f, h * 0.15f)
                        lineTo(w * 0.2f, h * 0.55f)
                        lineTo(w * 0.8f, h * 0.55f)
                        close()
                }
                drawPath(middleLayer, ForestColors.TreeGreen)

                val topLayer = Path().apply {
                        moveTo(w * 0.5f, h * 0.02f)
                        lineTo(w * 0.28f, h * 0.35f)
                        lineTo(w * 0.72f, h * 0.35f)
                        close()
                }
                drawPath(topLayer, ForestColors.TreeGreen)

                drawCircle(
                        color = Color(0xFFFFD700),
                        radius = w * 0.04f,
                        center = Offset(w * 0.5f, h * 0.05f)
                )
        }
}

@Composable
fun ForestMushroom(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                drawOval(
                        color = ForestColors.MushroomStem,
                        topLeft = Offset(w * 0.35f, h * 0.5f),
                        size = Size(w * 0.3f, h * 0.48f)
                )

                drawArc(
                        color = ForestColors.MushroomRed,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(w * 0.1f, h * 0.15f),
                        size = Size(w * 0.8f, h * 0.7f)
                )

                drawCircle(
                        color = ForestColors.MushroomWhite,
                        radius = w * 0.06f,
                        center = Offset(w * 0.35f, h * 0.35f)
                )
                drawCircle(
                        color = ForestColors.MushroomWhite,
                        radius = w * 0.05f,
                        center = Offset(w * 0.55f, h * 0.28f)
                )
                drawCircle(
                        color = ForestColors.MushroomWhite,
                        radius = w * 0.045f,
                        center = Offset(w * 0.7f, h * 0.4f)
                )
                drawCircle(
                        color = ForestColors.MushroomWhite,
                        radius = w * 0.04f,
                        center = Offset(w * 0.45f, h * 0.45f)
                )
        }
}

@Composable
fun ForestOwl(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                val leftEar = Path().apply {
                        moveTo(w * 0.2f, h * 0.3f)
                        lineTo(w * 0.15f, h * 0.08f)
                        lineTo(w * 0.35f, h * 0.22f)
                        close()
                }
                drawPath(leftEar, ForestColors.OwlBrown)

                val rightEar = Path().apply {
                        moveTo(w * 0.8f, h * 0.3f)
                        lineTo(w * 0.85f, h * 0.08f)
                        lineTo(w * 0.65f, h * 0.22f)
                        close()
                }
                drawPath(rightEar, ForestColors.OwlBrown)

                drawOval(
                        color = ForestColors.OwlBrown,
                        topLeft = Offset(w * 0.15f, h * 0.2f),
                        size = Size(w * 0.7f, h * 0.75f)
                )

                drawOval(
                        color = ForestColors.OwlLightBrown,
                        topLeft = Offset(w * 0.25f, h * 0.45f),
                        size = Size(w * 0.5f, h * 0.45f)
                )

                drawCircle(
                        color = ForestColors.OwlLightBrown,
                        radius = w * 0.15f,
                        center = Offset(w * 0.35f, h * 0.38f)
                )
                drawCircle(
                        color = ForestColors.OwlLightBrown,
                        radius = w * 0.15f,
                        center = Offset(w * 0.65f, h * 0.38f)
                )

                drawCircle(
                        color = ForestColors.OwlEyes,
                        radius = w * 0.1f,
                        center = Offset(w * 0.35f, h * 0.38f)
                )
                drawCircle(
                        color = ForestColors.OwlEyes,
                        radius = w * 0.1f,
                        center = Offset(w * 0.65f, h * 0.38f)
                )

                drawCircle(
                        color = Color.Black,
                        radius = w * 0.05f,
                        center = Offset(w * 0.35f, h * 0.38f)
                )
                drawCircle(
                        color = Color.Black,
                        radius = w * 0.05f,
                        center = Offset(w * 0.65f, h * 0.38f)
                )

                drawCircle(
                        color = Color.White,
                        radius = w * 0.02f,
                        center = Offset(w * 0.33f, h * 0.36f)
                )
                drawCircle(
                        color = Color.White,
                        radius = w * 0.02f,
                        center = Offset(w * 0.63f, h * 0.36f)
                )

                val beak = Path().apply {
                        moveTo(w * 0.5f, h * 0.45f)
                        lineTo(w * 0.45f, h * 0.55f)
                        lineTo(w * 0.55f, h * 0.55f)
                        close()
                }
                drawPath(beak, Color(0xFFFF9800))
        }
}

@Composable
fun ForestLeaf(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                val leafPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.1f)
                        quadraticTo(w * 0.9f, h * 0.3f, w * 0.5f, h * 0.9f)
                        quadraticTo(w * 0.1f, h * 0.3f, w * 0.5f, h * 0.1f)
                        close()
                }
                drawPath(leafPath, ForestColors.LeafGreen)

                drawLine(
                        color = ForestColors.LeafDarkGreen,
                        start = Offset(w * 0.5f, h * 0.15f),
                        end = Offset(w * 0.5f, h * 0.85f),
                        strokeWidth = w * 0.02f
                )

                for (i in 1..4) {
                        val y = h * (0.25f + i * 0.13f)
                        drawLine(
                                color = ForestColors.LeafDarkGreen,
                                start = Offset(w * 0.5f, y),
                                end = Offset(w * (0.3f - i * 0.02f), y + h * 0.05f),
                                strokeWidth = w * 0.012f
                        )
                        drawLine(
                                color = ForestColors.LeafDarkGreen,
                                start = Offset(w * 0.5f, y),
                                end = Offset(w * (0.7f + i * 0.02f), y + h * 0.05f),
                                strokeWidth = w * 0.012f
                        )
                }
        }
}

@Composable
fun ForestAcorn(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                drawArc(
                        color = ForestColors.AcornCap,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(w * 0.2f, h * 0.15f),
                        size = Size(w * 0.6f, h * 0.4f)
                )

                drawRect(
                        color = ForestColors.AcornCap,
                        topLeft = Offset(w * 0.45f, h * 0.08f),
                        size = Size(w * 0.1f, h * 0.12f)
                )

                drawOval(
                        color = ForestColors.AcornBrown,
                        topLeft = Offset(w * 0.22f, h * 0.32f),
                        size = Size(w * 0.56f, h * 0.6f)
                )

                for (i in 0..3) {
                        for (j in 0..2) {
                                val x = w * (0.28f + i * 0.12f)
                                val y = h * (0.22f + j * 0.06f)
                                if (y < h * 0.35f) {
                                        drawCircle(
                                                color = Color(0xFF4E342E),
                                                radius = w * 0.02f,
                                                center = Offset(x, y)
                                        )
                                }
                        }
                }
        }
}

@Composable
fun ForestBunny(modifier: Modifier = Modifier, size: Dp = 48.dp) {
        Canvas(modifier = modifier.size(size)) {
                val w = this.size.width
                val h = this.size.height

                drawOval(
                        color = ForestColors.BunnyGray,
                        topLeft = Offset(w * 0.25f, h * 0.02f),
                        size = Size(w * 0.15f, h * 0.35f)
                )
                drawOval(
                        color = ForestColors.BunnyGray,
                        topLeft = Offset(w * 0.6f, h * 0.02f),
                        size = Size(w * 0.15f, h * 0.35f)
                )

                drawOval(
                        color = ForestColors.BunnyPink,
                        topLeft = Offset(w * 0.28f, h * 0.06f),
                        size = Size(w * 0.09f, h * 0.25f)
                )
                drawOval(
                        color = ForestColors.BunnyPink,
                        topLeft = Offset(w * 0.63f, h * 0.06f),
                        size = Size(w * 0.09f, h * 0.25f)
                )

                drawCircle(
                        color = ForestColors.BunnyGray,
                        radius = w * 0.32f,
                        center = Offset(w * 0.5f, h * 0.55f)
                )

                drawCircle(
                        color = Color(0xFFBDBDBD),
                        radius = w * 0.12f,
                        center = Offset(w * 0.3f, h * 0.6f)
                )
                drawCircle(
                        color = Color(0xFFBDBDBD),
                        radius = w * 0.12f,
                        center = Offset(w * 0.7f, h * 0.6f)
                )

                drawCircle(
                        color = Color.Black,
                        radius = w * 0.045f,
                        center = Offset(w * 0.38f, h * 0.48f)
                )
                drawCircle(
                        color = Color.Black,
                        radius = w * 0.045f,
                        center = Offset(w * 0.62f, h * 0.48f)
                )

                drawCircle(
                        color = Color.White,
                        radius = w * 0.015f,
                        center = Offset(w * 0.37f, h * 0.47f)
                )
                drawCircle(
                        color = Color.White,
                        radius = w * 0.015f,
                        center = Offset(w * 0.61f, h * 0.47f)
                )

                drawOval(
                        color = ForestColors.BunnyPink,
                        topLeft = Offset(w * 0.44f, h * 0.58f),
                        size = Size(w * 0.12f, h * 0.08f)
                )
        }
}

