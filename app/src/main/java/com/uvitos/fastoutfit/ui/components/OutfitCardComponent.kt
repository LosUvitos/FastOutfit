package com.uvitos.fastoutfit.ui.components

import  androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.data.database.ClothingItem
import com.uvitos.fastoutfit.ui.theme.AccentMain
import com.uvitos.fastoutfit.ui.theme.ColorSecondary

@Composable
fun OutfitCardComponent(
    modifier: Modifier = Modifier,
    shirt: ClothingItem? = null,
    pant: ClothingItem? = null,
    upper: ClothingItem? = null,
    shoes: ClothingItem? = null,
    onShuffleClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit = {},
    cardBackgroundColor: Color = ColorSecondary
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(com.uvitos.fastoutfit.R.string.outfit_of_day),
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = shirt?.imagePath ?: ""
                    )

                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = upper?.imagePath ?: ""
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = pant?.imagePath ?: ""
                    )

                    GarmentPlaceholderCard(
                        modifier = Modifier.weight(1f),
                        imagePath = shoes?.imagePath ?: ""
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onShuffleClick) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = stringResource(com.uvitos.fastoutfit.R.string.cd_shuffle),
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = stringResource(com.uvitos.fastoutfit.R.string.cd_favorite),
                        tint = AccentMain,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
