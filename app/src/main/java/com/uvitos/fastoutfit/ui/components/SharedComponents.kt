package com.uvitos.fastoutfit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.ui.theme.*
import androidx.compose.runtime.setValue


/**
 * Full-screen dark navy background.
 * The scattered clothing icons are represented here as a tiled subtle pattern
 * using Box with a custom drawn overlay — in a real app you'd use a repeating
 * vector drawable or Canvas draw calls. Here we approximate with alpha overlays.
 */
@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Image(
        painter = painterResource(R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x00FFFFFF)),
        content = content
    )
}

/** Rounded input field matching the designs */
@Composable
fun OutfitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = if (isPassword && !passwordVisible)
        androidx.compose.ui.text.input.PasswordVisualTransformation()
    else
        androidx.compose.ui.text.input.VisualTransformation.None

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeholder, color = InputText.copy(alpha = 0.5f))
        },
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (isPassword){
                IconButton(onClick = {passwordVisible = !passwordVisible}) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                        Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar Contraseña" else "Mostrar contraseña",
                        tint = InputText.copy(alpha = 0.6f)
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = InputBackground,
            focusedContainerColor   = InputBackground,
            unfocusedBorderColor    = Color.Transparent,
            focusedBorderColor      = GoldAccent,
            unfocusedTextColor      = InputText,
            focusedTextColor        = InputText,
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
    )
}

/** Gold pill button */
@Composable
fun GoldButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
    ) {
        Text(
            text = text,
            color = BackgroundDark,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            letterSpacing = 1.5.sp,
        )
    }
}
/** Top app bar with home icon (gold) and profile circle */
@Composable
fun FastOutfitTopBar(
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Leading icon (? or settings)
        if (leadingIcon != null) {
            IconButton(onClick = onLeadingClick) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        } else {
            Spacer(Modifier.size(48.dp))
        }

        // Home icon (gold house with tag)
        IconButton(onClick = onHomeClick) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = stringResource(com.uvitos.fastoutfit.R.string.cd_home),
                tint = GoldAccent,
                modifier = Modifier.size(36.dp)
            )
        }

        // Profile circle
        IconButton(onClick = onProfileClick) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TextSecondary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(com.uvitos.fastoutfit.R.string.cd_profile),
                    tint = TextPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun BoltLogo(size: Int = 120) {
    Box(
        modifier = Modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.fastoutfitnb),
            contentDescription = "FastOutfit logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun LinkText(text: String, onClick: () -> Unit) {
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

@Composable
fun ForgotPasswordDialog(
    onDismiss: () -> Unit,
    onSend: (email: String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var sent  by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = {
            Text(
                text = stringResource(com.uvitos.fastoutfit.R.string.reset_password_title),
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
            )
        },
        text = {
            Column {
                if (sent) {
                    Text(
                        text = stringResource(com.uvitos.fastoutfit.R.string.reset_password_sent),
                        color = TextSecondary,
                        fontSize = 13.sp,
                    )
                } else {
                    Text(
                        text = stringResource(com.uvitos.fastoutfit.R.string.reset_password_message),
                        color = TextSecondary,
                        fontSize = 13.sp,
                    )
                    Spacer(Modifier.height(16.dp))
                    OutfitTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = stringResource(com.uvitos.fastoutfit.R.string.email_placeholder),
                    )
                }
            }
        },
        confirmButton = {
            if (sent) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(com.uvitos.fastoutfit.R.string.close), color = GoldAccent, letterSpacing = 2.sp)
                }
            } else {
                TextButton(onClick = {
                    if (email.isNotBlank()) {
                        sent = true
                        onSend(email)
                    }
                }) {
                    Text(stringResource(com.uvitos.fastoutfit.R.string.send), color = GoldAccent, letterSpacing = 2.sp)
                }
            }
        },
        dismissButton = {
            if (!sent) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(com.uvitos.fastoutfit.R.string.cancel), color = TextSecondary, letterSpacing = 2.sp)
                }
            }
        }
    )
}

@Composable
fun TopBarWithHelpHomeProfile(
    onHelpClick:    () -> Unit = {},
    onHomeClick:    () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        IconButton(
            onClick  = onHelpClick,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painter            = painterResource(R.drawable.ic_help),
                contentDescription = stringResource(com.uvitos.fastoutfit.R.string.cd_help),
                tint               = TextPrimary,
                modifier           = Modifier.size(28.dp),
            )
        }

        IconButton(
            onClick  = onHomeClick,
            modifier = Modifier.align(Alignment.Center),
        ) {
            Icon(
                imageVector        = Icons.Filled.Home,
                contentDescription = stringResource(com.uvitos.fastoutfit.R.string.cd_home),
                tint               = GoldAccent,
                modifier           = Modifier.size(34.dp),
            )
        }

        IconButton(
            onClick  = onProfileClick,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                painter            = painterResource(R.drawable.ic_profile),
                contentDescription = stringResource(com.uvitos.fastoutfit.R.string.cd_profile),
                tint               = TextPrimary,
                modifier           = Modifier.size(28.dp),
            )
        }
    }
}
@Composable
fun TopBarWithHelpHomeSettings(
    onHelpClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {

        IconButton(
            onClick = onHelpClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_help),
                contentDescription = "Help",
                tint = TextPrimary,
                modifier = Modifier.size(28.dp)
            )
        }

        IconButton(
            onClick = onHomeClick,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home",
                tint = GoldAccent,
                modifier = Modifier.size(34.dp)
            )
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = TextPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}