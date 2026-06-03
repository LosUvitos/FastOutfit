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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    onForgotPasswordClick: (email: String) -> Unit = {},
    authState: AuthState = AuthState.Idle,
    onGoogleSignIn: () ->  Unit = {},
) {
    var email     by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotDialog by remember { mutableStateOf(false) }

    if (authState is AuthState.Error){
        LaunchedEffect(authState) {
            // Error se muestra en UI
        }
    }

    if (showForgotDialog){
        ForgotPasswordDialog(
            onDismiss = { showForgotDialog = false},
            onSend = {emailInput -> onForgotPasswordClick(emailInput)
            showForgotDialog = false}
        )
    }
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
                text = stringResource(com.uvitos.fastoutfit.R.string.app_title),
                color = TextPrimary,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 44.sp,
                letterSpacing = 3.sp,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(com.uvitos.fastoutfit.R.string.login_title),
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
            )

            Spacer(Modifier.height(32.dp))

            // ── Fields ────────────────────────────────────────────────────
            OutfitTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(com.uvitos.fastoutfit.R.string.email_placeholder),
            )

            Spacer(Modifier.height(12.dp))

            OutfitTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(com.uvitos.fastoutfit.R.string.password_placeholder),
                isPassword = true,
            )

            // Mensaje de error
            if (authState is AuthState.Error){
                Spacer(Modifier.height(8.dp))
                Text(
                    text = authState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(40.dp))

            if (authState is AuthState.Loading){
                CircularProgressIndicator()
            } else {
                GoldButton(text = stringResource(com.uvitos.fastoutfit.R.string.log_in),
                    onClick = {onLoginClick(email, password)})
                Spacer(Modifier.height(12.dp))

                // Botón de google
                OutlinedButton(onClick = onGoogleSignIn, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(com.uvitos.fastoutfit.R.string.continue_with_google), color = TextPrimary)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Bottom links ──────────────────────────────────────────────
            LinkText(text = stringResource(com.uvitos.fastoutfit.R.string.i_dont_have_account), onClick = onRegisterClick)
            Spacer(Modifier.height(8.dp))
            LinkText(text = stringResource(com.uvitos.fastoutfit.R.string.i_forgot_password), onClick = { showForgotDialog = true})
        }
    }
}

