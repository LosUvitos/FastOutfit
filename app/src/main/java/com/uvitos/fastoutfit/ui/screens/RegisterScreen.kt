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
import com.uvitos.fastoutfit.ui.components.*
import com.uvitos.fastoutfit.ui.theme.*
import com.uvitos.fastoutfit.ui.viewmodel.AuthState
import androidx.compose.foundation.background
import androidx.compose.ui.text.toUpperCase

/**
 * LoginScreen
 *
 * Matches the Figma design:
 *   ┌─────────────────────────┐
 *   │  [Bolt logo]            │
 *   │  FAST OUTFIT            │
 *   │  REGISTER               │
 *   │  [E-mail field]         │
 *   │  [Name field]           │
 *   │  [Password field]       │
 *   │  [confirm P-word field] │
 *   │  I HAVE AN ACCOUNT      │
 *   └─────────────────────────┘
 */
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: (email: String, password: String, confirmPassword: String) -> Unit = { _, _, _ -> },
    authState: AuthState = AuthState.Idle,
) {
    var email     by remember { mutableStateOf("") }
    var name      by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var showtext  by remember { mutableStateOf(false) }
    val matchPasswords = password == password2

    AppBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(60.dp))
            BoltLogo2()
            Spacer(Modifier.height(20.dp))
            Text("FAST\nOUTFIT",
                color = TextPrimary,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center,
                lineHeight = 44.sp, letterSpacing = 3.sp
            )

            Spacer(Modifier.height(4.dp))

            Text("REGISTER",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )

            Spacer(Modifier.height(32.dp))

            OutfitTextField(value = email,
                onValueChange = { email = it },
                placeholder = "e-mail"
            )

            Spacer(Modifier.height(12.dp))

            OutfitTextField(value = name,
                onValueChange = { name = it },
                placeholder = "name"
            )

            Spacer(Modifier.height(12.dp))

            OutfitTextField(value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true
            )

            PasswordStrengthIndicator(password = password)

            Spacer(Modifier.height(12.dp))

            OutfitTextField(
                value = password2,
                onValueChange = { password2 = it; showtext = true },
                placeholder = "Confirm Password",
                isPassword = true,
            )

            if (showtext && !matchPasswords) {
                Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }

            // Error de Firebase
            if (authState is AuthState.Error) {
                Spacer(Modifier.height(8.dp))
                Text(authState.message, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(40.dp))

            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                GoldButton(
                    text = "REGISTER",
                    onClick = { onRegisterClick(email, password, password2) }
                )
            }

            Spacer(Modifier.height(24.dp))
            LinkText(text = "I HAVE AN ACCOUNT", onClick = onLoginClick)
        }
    }
}

fun verify(k1: String,k2: String): Boolean{
    return k1 == k2
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
fun BoltLogo2(size: Int = 120) {
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
                    //BoltShape(color = Color(0xFF1A1A1A))
                }
            }
        }
    }
}

@Composable
private fun PasswordStrengthIndicator(password: String) {
    if (password.isEmpty()) return

    val checks = listOf(
        password.length >= 8,
        password.length <= 16,
        password.any { it.isUpperCase() },
        password.any { it.isLowerCase() },
        password.any { it.isDigit() },
        password.any { it in "_-@*#\$!" }
    )
    val strength = checks.count { it }
    val (color, label) = when {
        strength <= 2 -> Color(0xFFE53935) to "Débil"
        strength <= 4 -> Color(0xFFFFA726) to "Regular"
        strength == 5 -> Color(0xFFFFEE58) to "Buena"
        else          -> Color(0xFF66BB6A) to "Fuerte"
    }

    Column(modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(6) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (index < strength) color else Color.Gray.copy(alpha = 0.3f))
                )
            }
        }
        Spacer(Modifier.height(2.dp))
        Text(label, color = color, fontSize = 11.sp, letterSpacing = 1.sp)
    }
}