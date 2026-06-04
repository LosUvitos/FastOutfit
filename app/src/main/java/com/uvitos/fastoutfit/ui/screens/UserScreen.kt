package com.uvitos.fastoutfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.GoldButton
import com.uvitos.fastoutfit.ui.components.TopBarWithHelpHomeProfile
import com.uvitos.fastoutfit.ui.components.TopBarWithHelpHomeSettings
import com.uvitos.fastoutfit.ui.components.UserInfoCard
import com.uvitos.fastoutfit.ui.theme.TextPrimary

@Composable
fun UserScreen(
    userName: String = "EXAMPLE",
    userEmail: String = "EXAMPLE@email.com",

    onHelpClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},

    onEditNameClick: () -> Unit = {},
    onShowEmailClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},

    onSaveClick: () -> Unit = {}
) {

    var emailVisible by remember {
        mutableStateOf(false)
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
                value = userName,
                actionIcon = Icons.Default.Edit,
                onActionClick = onEditNameClick
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
                onActionClick = onChangePasswordClick
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

