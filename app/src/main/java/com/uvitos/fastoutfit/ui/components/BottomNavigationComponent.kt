package com.uvitos.fastoutfit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uvitos.fastoutfit.ui.theme.AccentMain

@Composable
fun BottomNavigationComponent(
    modifier: Modifier = Modifier,
    iconColor: Color = AccentMain,
    backgroundColor: Color = Color.Transparent,
    onWardrobeClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
                .clickable(onClick = onWardrobeClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Dashboard,
                contentDescription = "Wardrobe",
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
