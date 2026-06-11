package com.uvitos.fastoutfit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.data.database.FavoriteOutfit
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.GarmentPlaceholderCard
import com.uvitos.fastoutfit.ui.components.OutfitDetailDialog
import com.uvitos.fastoutfit.ui.theme.*
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModel

@Composable
fun FavoriteOutfitsScreen(
    onBackClick: () -> Unit = {},
    clothingViewModel: ClothingViewModel
) {
    val favoriteOutfits by clothingViewModel.favoriteOutfits.collectAsState()
    var outfitToDelete by remember { mutableStateOf<FavoriteOutfit?>(null) }
    var selectedOutfitId by remember { mutableStateOf<Int?>(null) }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopBar(
                onBackClick = onBackClick
            )

            Text(
                text = "OUTFITS FAVORITOS",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            if (favoriteOutfits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "⭐",
                            fontSize = 48.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "No tienes outfits favoritos",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Guarda un outfit desde la pantalla principal",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favoriteOutfits, key = { it.id }) { outfit ->
                        FavoriteOutfitCard(
                            outfit = outfit,
                            clothingViewModel = clothingViewModel,
                            onClick = { selectedOutfitId = outfit.id },
                            onDeleteClick = { outfitToDelete = outfit }
                        )
                    }
                }
            }
        }
    }

    if (outfitToDelete != null) {
        AlertDialog(
            containerColor = Color(0xFF1E1E1E),
            onDismissRequest = { outfitToDelete = null },
            title = {
                Text(
                    text = "Eliminar outfit favorito",
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que quieres eliminar este outfit de favoritos?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    clothingViewModel.deleteFavoriteOutfit(outfitToDelete!!)
                    outfitToDelete = null
                }) {
                    Text(
                        text = "ELIMINAR",
                        color = Color(0xFFE53935)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { outfitToDelete = null }) {
                    Text(
                        text = "CANCELAR",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    if (selectedOutfitId != null) {
        val outfit = favoriteOutfits.find { it.id == selectedOutfitId }
        if (outfit != null) {
            OutfitDetailDialog(
                outfit = clothingViewModel.resolveFavoriteOutfit(outfit),
                onDismiss = { selectedOutfitId = null }
            )
        }
    }
}

@Composable
private fun TopBar(
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun FavoriteOutfitCard(
    outfit: FavoriteOutfit,
    clothingViewModel: ClothingViewModel,
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit
) {
    val resolved = remember(outfit) {
        clothingViewModel.resolveFavoriteOutfit(outfit)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable(onClick = onClick)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GarmentPlaceholderCard(
                    modifier = Modifier.weight(1f),
                    imagePath = resolved.shirt?.imagePath ?: "",
                    cardSize = 80.dp
                )
                GarmentPlaceholderCard(
                    modifier = Modifier.weight(1f),
                    imagePath = resolved.upper?.imagePath ?: "",
                    cardSize = 80.dp
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GarmentPlaceholderCard(
                    modifier = Modifier.weight(1f),
                    imagePath = resolved.pant?.imagePath ?: "",
                    cardSize = 80.dp
                )
                GarmentPlaceholderCard(
                    modifier = Modifier.weight(1f),
                    imagePath = resolved.shoes?.imagePath ?: "",
                    cardSize = 80.dp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.cd_delete),
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
