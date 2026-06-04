package com.uvitos.fastoutfit.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.notifications.NotificationScheduler
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.GoldButton
import com.uvitos.fastoutfit.ui.theme.*
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit = {},
) {
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(false) }
    var selectedHour   by remember { mutableStateOf(8) }
    var selectedMinute by remember { mutableStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Solicitar permiso POST_NOTIFICATIONS en Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationsEnabled = granted
        if (granted) NotificationScheduler.schedule(context, selectedHour, selectedMinute)
    }

    if (showTimePicker) {
        TimePickerDialog(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            onConfirm = { h, m ->
                selectedHour = h
                selectedMinute = m
                showTimePicker = false
                if (notificationsEnabled) {
                    NotificationScheduler.schedule(context, h, m)
                }
            },
            onDismiss = { showTimePicker = false }
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

            Text(
                text = "NOTIFICACIONES",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
            )

            Spacer(Modifier.height(48.dp))

            // Toggle de notificaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Outfit del día", color = TextPrimary, fontSize = 15.sp)
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                notificationsEnabled = true
                                NotificationScheduler.schedule(context, selectedHour, selectedMinute)
                            }
                        } else {
                            notificationsEnabled = false
                            NotificationScheduler.cancel(context)
                        }
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = GoldAccent, checkedTrackColor = GoldAccent.copy(alpha = 0.5f))
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = TextSecondary.copy(alpha = 0.2f))
            Spacer(Modifier.height(24.dp))

            // Selector de hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Hora del recordatorio", color = TextPrimary, fontSize = 15.sp)
                TextButton(
                    onClick = { showTimePicker = true },
                    enabled = notificationsEnabled,
                ) {
                    Text(
                        text = "%02d:%02d".format(selectedHour, selectedMinute),
                        color = if (notificationsEnabled) GoldAccent else TextSecondary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.height(48.dp))

            // Botón de prueba para la presentación
            if (notificationsEnabled) {
                OutlinedButton(
                    onClick = { NotificationScheduler.scheduleTest(context) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        "PROBAR NOTIFICACIÓN (3s)",
                        color = GoldAccent,
                        letterSpacing = 1.sp,
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onBackClick) {
                Text("← VOLVER", color = TextSecondary, letterSpacing = 2.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = androidx.compose.ui.graphics.Color(0xFF1E1E1E),
        title = {
            Text("Hora del recordatorio", color = TextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            TimePicker(state = state)
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }) {
                Text("CONFIRMAR", color = GoldAccent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = TextSecondary)
            }
        }
    )
}