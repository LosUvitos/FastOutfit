package com.uvitos.fastoutfit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uvitos.fastoutfit.ui.theme.GoldAccent
import com.uvitos.fastoutfit.ui.theme.TextPrimary
import com.uvitos.fastoutfit.ui.theme.TextSecondary
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModel

@Composable
fun OutfitDetailDialog(
    outfit: ClothingViewModel.Outfit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = Color(0xFF1E1E1E),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "OUTFIT",
                color = TextPrimary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = outfit.shirt?.imagePath ?: "",
                        cardSize = 160.dp
                    )
                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = outfit.upper?.imagePath ?: "",
                        cardSize = 160.dp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = outfit.pant?.imagePath ?: "",
                        cardSize = 160.dp
                    )
                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = outfit.shoes?.imagePath ?: "",
                        cardSize = 160.dp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "CERRAR",
                    color = GoldAccent
                )
            }
        }
    )
}
