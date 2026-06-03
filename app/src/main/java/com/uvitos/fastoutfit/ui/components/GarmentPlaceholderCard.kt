package com.uvitos.fastoutfit.ui.components

import androidx.compose.animation.core.copy
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uvitos.fastoutfit.ui.theme.AccentMain

@Composable
fun GarmentPlaceholderCard(
    modifier: Modifier = Modifier,
    borderColor: Color = AccentMain,
    innerColor: Color = Color(0xFFD9D9D9),
    stringColor: Color = Color(0xFF8B6F47),
    rivetColor: Color = Color(0xFF2F2F2F),
    rivetHighlight: Color = Color(0xFF8A8A8A),
    frameColor: Color = Color(0xFFE6C48B),
    cardSize: Dp = 150.dp
) {

    val frameRadius = 10.dp
    val innerRadius = 6.dp
    val frameThickness = 10.dp

    Box(
        modifier = modifier
            .size(cardSize)
            .padding(top = 18.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        // =========================
        // 🪢 Hanging String + Rivet
        // =========================
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val centerX = size.width / 2f

            val stringTopY = 0f
            val stringBottomY = 20f

            // Left rope
            val leftRope = Path().apply {
                moveTo(centerX - 3f, stringTopY)
                quadraticBezierTo(
                    centerX - 7f,
                    stringBottomY / 2f,
                    centerX - 1.5f,
                    stringBottomY
                )
            }

            drawPath(
                path = leftRope,
                color = stringColor,
                style = Stroke(
                    width = 2.2f,
                    pathEffect = PathEffect.cornerPathEffect(8f)
                )
            )

            // Rope highlight
            drawPath(
                path = leftRope,
                color = Color(0xFFD2A06B).copy(alpha = 0.35f),
                style = Stroke(
                    width = 0.8f
                )
            )

            // Right rope
            val rightRope = Path().apply {
                moveTo(centerX + 3f, stringTopY + 1f)
                quadraticBezierTo(
                    centerX + 7f,
                    stringBottomY / 2f,
                    centerX + 1.5f,
                    stringBottomY
                )
            }

            drawPath(
                path = rightRope,
                color = stringColor,
                style = Stroke(
                    width = 2.2f,
                    pathEffect = PathEffect.cornerPathEffect(8f)
                )
            )

            // Rope highlight
            drawPath(
                path = rightRope,
                color = Color(0xFFD2A06B).copy(alpha = 0.35f),
                style = Stroke(
                    width = 0.8f
                )
            )

            // =========================
// ⚫ Metallic Rivet (Agrandado)
// =========================

            val rivetCenter = androidx.compose.ui.geometry.Offset(centerX, stringBottomY + 1f)

// Outer shadow (Aumentado de 5.8f a 12f)
            drawCircle(
                color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.18f),
                radius = 12f,
                center = androidx.compose.ui.geometry.Offset(centerX, stringBottomY + 2.5f)
            )

// Rivet base (Aumentado de 4.6f a 10f y el gradiente a 14f)
            drawCircle(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(
                        rivetHighlight,
                        rivetColor
                    ),
                    center = androidx.compose.ui.geometry.Offset(centerX - 2f, stringBottomY - 2f),
                    radius = 14f
                ),
                radius = 10f,
                center = rivetCenter
            )

// Metallic glossy highlight (Aumentado de 1.1f a 3f)
            drawCircle(
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.5f),
                radius = 3f,
                center = androidx.compose.ui.geometry.Offset(centerX - 2.5f, stringBottomY - 1.5f)
            )
        }

        // =========================
        // 🟫 Main Frame
        // =========================
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize()
        ) {

            // Ambient Shadow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = 2.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(frameRadius)
                    )
            )

            // Outer Frame
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(frameRadius))
                    .background(frameColor)
                    .border(
                        width = 0.7.dp,
                        color = Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(frameRadius)
                    )
            ) {

                // Subtle top highlight
                Canvas(
                    modifier = Modifier.matchParentSize()
                ) {

                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.10f),
                                Color.Transparent
                            )
                        ),
                        size = Size(size.width, size.height * 0.22f),
                        cornerRadius = CornerRadius(18f, 18f)
                    )

                    // Inner shadow
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.06f),
                                Color.Transparent
                            )
                        ),
                        topLeft = Offset(0f, 0f),
                        size = Size(size.width, size.height * 0.18f),
                        cornerRadius = CornerRadius(18f, 18f)
                    )
                }

                // =========================
                // ⬜ Inner Area
                // =========================
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(frameThickness)
                        .clip(RoundedCornerShape(innerRadius))
                        .background(innerColor)
                        .border(
                            width = 0.6.dp,
                            color = Color.White.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(innerRadius)
                        )
                ) {

                    // Subtle inner lighting
                    Canvas(
                        modifier = Modifier.matchParentSize()
                    ) {

                        // Top soft light
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.08f),
                                    Color.Transparent
                                )
                            ),
                            size = Size(size.width, size.height * 0.18f)
                        )

                        // Bottom subtle depth
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.03f)
                                ),
                                startY = size.height * 0.75f,
                                endY = size.height
                            ),
                            topLeft = Offset(0f, size.height * 0.75f),
                            size = Size(size.width, size.height * 0.25f)
                        )
                    }
                }
            }
        }
    }
}