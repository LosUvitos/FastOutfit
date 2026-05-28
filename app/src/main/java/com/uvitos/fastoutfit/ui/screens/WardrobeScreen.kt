package com.uvitos.fastoutfit.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.GarmentPlaceholderCard
import com.uvitos.fastoutfit.ui.components.WardrobeTabBar
import com.uvitos.fastoutfit.ui.theme.*

// Modelo de datos

enum class ClothingCategory(val label: String) {
    SHIRTS("SHIRTS"),
    PANTS("PANTS"),
    SHOES("SHOES"),
    UPPER("UPPER"),
}

// Prenda de ejemplo — más adelante se reemplaza con datos reales
data class GarmentItem(
    val id: Int,
    val category: ClothingCategory,
    // imageUrl ira aquí cuando haya backend
)

//  Pantalla principal

@Composable
fun WardrobeScreen(
    onHelpClick:      () -> Unit = {},
    onHomeClick:      () -> Unit = {},
    onProfileClick:   () -> Unit = {},
    onAddClick:       (ClothingCategory) -> Unit = {},
    onFilterClick:    (ClothingCategory) -> Unit = {},
    onFavoriteClick:  (GarmentItem) -> Unit = {},
    onDeleteClick:    (GarmentItem) -> Unit = {},
) {
    // Tab seleccionado actualmente
    var selectedCategory by remember { mutableStateOf(ClothingCategory.SHIRTS) }

    // Lista de prendas de ejemplo — se reemplazará con datos reales
    val allGarments = remember {
        listOf(
            GarmentItem(1,  ClothingCategory.SHIRTS),
            GarmentItem(2,  ClothingCategory.SHIRTS),
            GarmentItem(3,  ClothingCategory.SHIRTS),
            GarmentItem(4,  ClothingCategory.SHIRTS),
            GarmentItem(5,  ClothingCategory.PANTS),
            GarmentItem(6,  ClothingCategory.PANTS),
            GarmentItem(7,  ClothingCategory.PANTS),
            GarmentItem(8,  ClothingCategory.PANTS),
            GarmentItem(9,  ClothingCategory.SHOES),
            GarmentItem(10, ClothingCategory.SHOES),
            GarmentItem(11, ClothingCategory.SHOES),
            GarmentItem(12, ClothingCategory.SHOES),
            GarmentItem(13, ClothingCategory.UPPER),
            GarmentItem(14, ClothingCategory.UPPER),
            GarmentItem(15, ClothingCategory.UPPER),
            GarmentItem(16, ClothingCategory.UPPER),
        )
    }

    // Prendas filtradas por categoría activa
    val visibleGarments = allGarments.filter { it.category == selectedCategory }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {

            // Top bar
            WardrobeTopBar(
                onHelpClick    = onHelpClick,
                onHomeClick    = onHomeClick,
                onProfileClick = onProfileClick,
            )

            // Título categoría
            Text(
                text       = selectedCategory.label,
                color      = TextPrimary,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier   = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )

            // Barra filtro + agregar
            FilterAddBar(
                onFilterClick = { onFilterClick(selectedCategory) },
                onAddClick    = { onAddClick(selectedCategory) },
            )

            // Marcos de prendas
            LazyVerticalGrid(
                columns      = GridCells.Fixed(2),
                modifier     = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                contentPadding      = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
            ) {
                items(visibleGarments, key = { it.id }) { garment ->
                    GarmentCard(
                        garment         = garment,
                        onFavoriteClick = { onFavoriteClick(garment) },
                        onDeleteClick   = { onDeleteClick(garment) },
                    )
                }
            }

            // tabs inferiores
            WardrobeTabBar(
                selected = selectedCategory,
                onSelect = { selectedCategory = it },
            )
        }
    }
}

// Top bar

@Composable
private fun WardrobeTopBar(
    onHelpClick:    () -> Unit,
    onHomeClick:    () -> Unit,
    onProfileClick: () -> Unit,
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
                contentDescription = "Help",
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
                contentDescription = "Home",
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
                contentDescription = "Profile",
                tint               = TextPrimary,
                modifier           = Modifier.size(28.dp),
            )
        }
    }
}

// Filter + Add bar

@Composable
private fun FilterAddBar(
    onFilterClick: () -> Unit,
    onAddClick:    () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // FILTER button
        OutlinedButton(
            onClick      = onFilterClick,
            shape        = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors       = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
            border       = androidx.compose.foundation.BorderStroke(1.dp, TextSecondary),
        ) {
            Icon(
                imageVector        = Icons.Filled.FilterList,
                contentDescription = null,
                modifier           = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text          = "FILTER",
                fontSize      = 11.sp,
                fontWeight    = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            )
        }

        // ADD button
        IconButton(onClick = onAddClick) {
            Icon(
                imageVector        = Icons.Filled.Add,
                contentDescription = "Add garment",
                tint               = TextPrimary,
                modifier           = Modifier.size(28.dp),
            )
        }
    }
}

//  Garment card con botones ⚡ y 🗑

@Composable
private fun GarmentCard(
    garment:         GarmentItem,
    onFavoriteClick: () -> Unit,
    onDeleteClick:   () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Placeholder card (se reemplazará con imagen real)
        GarmentPlaceholderCard(
            cardSize = 150.dp,
            modifier = Modifier.fillMaxWidth(),
        )

        // Botones de cada marco
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick  = onFavoriteClick,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter        = painterResource(R.drawable.ic_fav),
                    contentDescription = "Favorite",
                    tint               = GoldAccent,
                    modifier           = Modifier.size(18.dp),
                )
            }

            IconButton(
                onClick  = onDeleteClick,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter        = painterResource(R.drawable.ic_trash),
                    contentDescription = "Delete",
                    tint               = TextSecondary,
                    modifier           = Modifier.size(18.dp),
                )
            }
        }
    }
}

// tabs

// ---------------------------DISEÑO ANTERIOR DE LOS TABS INFERIORES--------------------------------
/*@Composable
private fun WardrobeBottomTabs(
    selected: ClothingCategory,
    onSelect: (ClothingCategory) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GoldAccent.copy(alpha = 0.15f))
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        ClothingCategory.entries.forEach { category ->
            val isSelected = category == selected
            TextButton(
                onClick = { onSelect(category) },
                modifier = Modifier.weight(1f),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Indicador activo
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(3.dp)
                                .background(
                                    color = GoldAccent,
                                    shape = RoundedCornerShape(2.dp),
                                )
                        )
                        Spacer(Modifier.height(2.dp))
                    }
                    Text(
                        text          = category.label,
                        color         = if (isSelected) GoldAccent else TextSecondary,
                        fontSize      = 10.sp,
                        fontWeight    = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        letterSpacing = 1.sp,
                    )
                }
            }
        }
    }
}*/
//--------------------------------------------------------------------------------------------------

@Preview(
    name           = "Wardrobe Screen – Dark",
    showBackground = true,
    showSystemUi   = true,
    uiMode         = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun WardrobeScreenPreview() {
    FastOutfitTheme(
        darkTheme    = true,
        dynamicColor = false,
    ) {
        WardrobeScreen()
    }
}