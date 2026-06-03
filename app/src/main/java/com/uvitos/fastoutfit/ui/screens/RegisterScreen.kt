package com.uvitos.fastoutfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.ui.components.*
import com.uvitos.fastoutfit.ui.theme.*
import com.uvitos.fastoutfit.ui.viewmodel.AuthState

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
    onGoogleSignIn: () -> Unit = {},
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
            BoltLogo()
            Spacer(Modifier.height(20.dp))
            Text(stringResource(com.uvitos.fastoutfit.R.string.app_title),
                color = TextPrimary,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center,
                lineHeight = 44.sp, letterSpacing = 3.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(stringResource(com.uvitos.fastoutfit.R.string.register_title),
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )

            Spacer(Modifier.height(32.dp))

            OutfitTextField(value = email,
                onValueChange = { email = it },
                placeholder = stringResource(com.uvitos.fastoutfit.R.string.email_placeholder)
            )

            Spacer(Modifier.height(12.dp))

            OutfitTextField(value = name,
                onValueChange = { name = it },
                placeholder = stringResource(com.uvitos.fastoutfit.R.string.name_placeholder)
            )

            Spacer(Modifier.height(12.dp))

            OutfitTextField(value = password,
                onValueChange = { password = it },
                placeholder = stringResource(com.uvitos.fastoutfit.R.string.password_placeholder),
                isPassword = true
            )

            PasswordStrengthIndicator(password = password)

            Spacer(Modifier.height(12.dp))

            OutfitTextField(
                value = password2,
                onValueChange = { password2 = it; showtext = true },
                placeholder = stringResource(com.uvitos.fastoutfit.R.string.confirm_password_placeholder),
                isPassword = true,
            )

            if (showtext && !matchPasswords) {
                Text(stringResource(com.uvitos.fastoutfit.R.string.passwords_dont_match), color = MaterialTheme.colorScheme.error,
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
                    text = stringResource(com.uvitos.fastoutfit.R.string.register),
                    onClick = { onRegisterClick(email, password, password2) }
                )
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = onGoogleSignIn, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(com.uvitos.fastoutfit.R.string.continue_with_google), color = TextPrimary)
                }
            }

            Spacer(Modifier.height(24.dp))
            LinkText(text = stringResource(com.uvitos.fastoutfit.R.string.i_have_account), onClick = onLoginClick)
        }
    }
}

@Composable
private fun PasswordStrengthIndicator(password: String) {
    if (password.isEmpty()) return

    val rules = listOf(
        stringResource(com.uvitos.fastoutfit.R.string.password_min_chars) to (password.length >= 8),
        stringResource(com.uvitos.fastoutfit.R.string.password_max_chars) to (password.length <= 16),
        stringResource(com.uvitos.fastoutfit.R.string.password_need_upper) to password.any { it.isUpperCase() },
        stringResource(com.uvitos.fastoutfit.R.string.password_need_lower) to password.any { it.isLowerCase() },
        stringResource(com.uvitos.fastoutfit.R.string.password_need_digit) to password.any { it.isDigit() },
        stringResource(com.uvitos.fastoutfit.R.string.password_need_special) to password.any { it in "_-@*#\$!" }
    )

    val allValid = rules.all { it.second }
    if (allValid) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color(0x1F2933),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,

    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            rules.forEach { (label, passed) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = if (passed) "✓" else "✗",
                        color = if (passed) Color(0xFF66BB6A) else Color(0xFFE53935),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = if (passed) Color(0xFF66BB6A) else TextSecondary,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}