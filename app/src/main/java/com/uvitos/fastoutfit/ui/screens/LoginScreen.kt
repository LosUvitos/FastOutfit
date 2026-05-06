package com.uvitos.fastoutfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fastoutfit.ui.components.*
import com.fastoutfit.ui.theme.*

/**
 * LoginScreen
 *
 * Matches the Figma design:
 *   ┌─────────────────────────┐
 *   │  [Bolt logo]            │
 *   │  FAST OUTFIT            │
 *   │  LOGIN                  │
 *   │  [Name field]           │
 *   │  [Password field]       │
 *   │  [LOG IN button]        │
 *   │  I DONT HAVE AN ACCOUNT │
 *   │  I FORGOT MY PASSWORD   │
 *   └─────────────────────────┘
 */
@Composable
fun LoginScreen(
    onLoginClick: (name: String, password: String) -> Unit = { _, _ -> },
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
) {
    var name     by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(60.dp))

            // ── Logo ──────────────────────────────────────────────────────
            BoltLogo()

            Spacer(Modifier.height(20.dp))

            // ── Title ─────────────────────────────────────────────────────
            Text(
                text = "FAST\nOUTFIT",
                color = TextPrimary,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 44.sp,
                letterSpacing = 3.sp,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "LOGIN",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
            )

            Spacer(Modifier.height(32.dp))

            // ── Fields ────────────────────────────────────────────────────
            OutfitTextField(
                value = value,
                onValueChange = { name = it },
                placeholder = "name",
            )

            Spacer(Modifier.height(12.dp))

            OutfitTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true,
            )

            Spacer(Modifier.height(40.dp))

            // ── Login button ──────────────────────────────────────────────
            GoldButton(
                text = "LOG IN",
                onClick = { onLoginClick(name, password) },
            )

            Spacer(Modifier.height(24.dp))

            // ── Bottom links ──────────────────────────────────────────────
            LinkText(text = "I DONT HAVE AN ACCOUNT", onClick = onRegisterClick)
            Spacer(Modifier.height(8.dp))
            LinkText(text = "I FORGOT MY PASSWORD", onClick = onForgotPasswordClick)
        }
    }
}

@Composable
private fun LinkText(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
            textDecoration = TextDecoration.Underline,
        )
    }
}

/** Shared metallic bolt logo used on Login & Register screens */
@Composable
fun BoltLogo(size: Int = 120) {
    // Outer metallic ring
    Surface(
        modifier = Modifier.size(size.dp),
        shape = CircleShape,
        color = Color(0xFFB0B0B0),
        shadowElevation = 12.dp,
        tonalElevation = 4.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = Color(0xFFD8D8D8),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Bolt icon — replace with your drawable
                    BoltShape(color = Color(0xFF1A1A1A))
                }
            }
        }
    }
}