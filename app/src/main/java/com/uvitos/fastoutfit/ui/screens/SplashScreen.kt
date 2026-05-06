package com.fastoutfit.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.theme.*
import kotlinx.coroutines.delay

/**
 * SplashScreen
 *
 * Shows the metallic lightning-bolt logo centered on the dark background.
 * Two states mirror the Figma designs:
 *   • Loading  → dark/grey bolt  (icon tint = grey)
 *   • Loaded   → gold bolt       (icon tint = GoldAccent)
 *
 * After [splashDuration] ms the [onFinished] callback fires and the app
* navigates to  Login.
 *
 * Usage in NavGraph:
 *   composable("splash") {
 *       SplashScreen(onFinished = { navController.navigate("login") { popUpTo("splash") { inclusive = true } } })
 *   }
 */
@Composable
fun SplashScreen(
    splashDuration: Long = 2500L,
    onFinished: () -> Unit = {},
) {
    // Animate from grey → gold to simulate the "loaded" state transition
    var loaded by remember { mutableStateOf(false) }

    val boltColor by animateColorAsState(
        targetValue = if (loaded) GoldAccent else Color(0xFF888888),
        animationSpec = tween(durationMillis = 800, easing = EaseInOut),
        label = "boltColor"
    )

    val ringScale by animateFloatAsState(
        targetValue = if (loaded) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ringScale"
    )

    LaunchedEffect(Unit) {
        delay(splashDuration / 2)
        loaded = true
        delay(splashDuration / 2)
        onFinished()
    }

    AppBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            // Outer metallic ring
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(ringScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFD0D0D0),
                                Color(0xFF808080),
                                Color(0xFF404040),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                // Inner dark circle
                Box(
                    modifier = Modifier
                        .size(155.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFE8E8E8),
                                    Color(0xFFA0A0A0),
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    // Lightning bolt — use a vector drawable in your real project
                    // Placeholder: colored Box shaped like a bolt
                    BoltShape(color = boltColor)
                }
            }
        }
    }
}

/** Simple lightning-bolt drawn with Canvas / shapes */
@Composable
private fun BoltShape(color: Color) {
    // In a real project replace this with:
    //   Icon(painter = painterResource(R.drawable.ic_bolt), tint = color, ...)
    Box(
        modifier = Modifier
            .size(60.dp, 90.dp)
            .background(
                color = color,
                shape = androidx.compose.foundation.shape.GenericShape { size, _ ->
                    // Simple polygon bolt shape
                    moveTo(size.width * 0.65f, 0f)
                    lineTo(size.width * 0.2f, size.height * 0.50f)
                    lineTo(size.width * 0.55f, size.height * 0.50f)
                    lineTo(size.width * 0.35f, size.height)
                    lineTo(size.width * 0.80f, size.height * 0.50f)
                    lineTo(size.width * 0.45f, size.height * 0.50f)
                    close()
                }
            )
    )
}
