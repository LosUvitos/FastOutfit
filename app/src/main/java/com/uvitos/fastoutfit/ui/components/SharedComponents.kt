package com.fastoutfit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fastoutfit.ui.theme.*

/**
 * Full-screen dark navy background.
 * The scattered clothing icons are represented here as a tiled subtle pattern
 * using Box with a custom drawn overlay — in a real app you'd use a repeating
 * vector drawable or Canvas draw calls. Here we approximate with alpha overlays.
 */
@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
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
    val visualTransformation = if (isPassword)
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
                contentDescription = "Home",
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
                    contentDescription = "Profile",
                    tint = TextPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}