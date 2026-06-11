package com.uvitos.fastoutfit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.data.database.ClothingItem
import com.uvitos.fastoutfit.ui.theme.GoldAccent
import com.uvitos.fastoutfit.ui.theme.TextPrimary
import com.uvitos.fastoutfit.ui.theme.TextSecondary

@Composable
fun GarmentDetailDialog(
    garment: ClothingItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = Color(0xFF1E1E1E),
        onDismissRequest = onDismiss,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                GarmentPlaceholderCard(
                    modifier = Modifier.fillMaxWidth(),
                    imagePath = garment.imagePath,
                    cardSize = 240.dp
                )

                Text(
                    text = garment.name,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )
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
