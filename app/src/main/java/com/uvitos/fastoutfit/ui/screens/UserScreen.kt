package com.uvitos.fastoutfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.GoldButton
import com.uvitos.fastoutfit.ui.components.OutfitTextField
import com.uvitos.fastoutfit.ui.components.TopBarWithHelpHomeProfile
import com.uvitos.fastoutfit.ui.components.TopBarWithHelpHomeSettings
import com.uvitos.fastoutfit.ui.components.UserInfoCard
import com.uvitos.fastoutfit.ui.theme.GoldAccent
import com.uvitos.fastoutfit.ui.theme.TextPrimary
import com.uvitos.fastoutfit.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseAuth
import com.uvitos.fastoutfit.ui.components.ForgotPasswordDialog
import com.uvitos.fastoutfit.ui.viewmodel.AuthViewModel

@Composable
fun UserScreen(
    authViewModel: AuthViewModel,
    userName: String = "EXAMPLE",

    onHelpClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},

    onEditNameClick: () -> Unit = {},
    onShowEmailClick: () -> Unit = {},

    onSaveClick: () -> Unit = {}

) {

    var showResetDialog by remember {
        mutableStateOf(false)
    }

    var emailVisible by remember {
        mutableStateOf(false)
    }

    var showEditNameDialog by remember { mutableStateOf(false) }
    var editNameText by remember { mutableStateOf("") }

    val currentUser = FirebaseAuth.getInstance().currentUser

    val userEmail = currentUser?.email ?: "Correo no disponible"
    val userDisplayName = currentUser?.displayName ?: ""

    LaunchedEffect(userDisplayName) {
        editNameText = userDisplayName
    }

    if (showEditNameDialog) {
        AlertDialog(
            containerColor = Color(0xFF1E1E1E),
            onDismissRequest = { showEditNameDialog = false },
            title = {
                Text(
                    text = "Editar nombre",
                    color = TextPrimary
                )
            },
            text = {
                OutfitTextField(
                    value = editNameText,
                    onValueChange = { editNameText = it },
                    placeholder = "Nombre"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.updateDisplayName(editNameText)
                    showEditNameDialog = false
                }) {
                    Text(
                        text = "GUARDAR",
                        color = GoldAccent
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text(
                        text = "CANCELAR",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    if (showResetDialog) {

        ForgotPasswordDialog(
            onDismiss = {
                showResetDialog = false
            },

            onSend = { email ->
                authViewModel.sendPasswordReset(email)
            }
        )
    }

    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp)
        ) {

            TopBarWithHelpHomeSettings(
                onHelpClick = onHelpClick,
                onHomeClick = onHomeClick,
                onSettingsClick = onSettingsClick,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "MI PERFIL",
                color = TextPrimary,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            UserInfoCard(
                title = "Nombre de usuario",
                value = userDisplayName.ifBlank { userName },
                actionIcon = Icons.Default.Edit,
                onActionClick = {
                    editNameText = userDisplayName
                    showEditNameDialog = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            UserInfoCard(
                title = "Correo electrónico",
                value = if (emailVisible)
                    userEmail
                else
                    "••••••••••••••••••",

                actionIcon = Icons.Default.Visibility,
                onActionClick = {
                    emailVisible = !emailVisible
                    onShowEmailClick()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            UserInfoCard(
                title = "Contraseña",
                value = "••••••••••••",
                actionIcon = Icons.Default.Edit,
                onActionClick = {
                    showResetDialog = true
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            GoldButton(
                text = "GUARDAR",
                onClick = onSaveClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

