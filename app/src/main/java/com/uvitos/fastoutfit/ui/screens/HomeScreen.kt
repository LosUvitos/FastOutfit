package com.uvitos.fastoutfit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.BottomNavigationComponent
import com.uvitos.fastoutfit.ui.components.FloatingActionButtonComponent
import com.uvitos.fastoutfit.ui.components.OutfitCardComponent
import com.uvitos.fastoutfit.ui.components.TopBarComponent
import com.uvitos.fastoutfit.ui.components.WelcomeHeaderComponent
import com.uvitos.fastoutfit.ui.theme.AzulOscuro
import com.uvitos.fastoutfit.ui.theme.GoldAccent
import com.uvitos.fastoutfit.ui.theme.TextSecondary
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModel
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModelFactory


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String = "USUARIO",
    onSettingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onAddGarmentClick: () -> Unit = {},
    onWardrobeClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val clothingViewModel: ClothingViewModel = viewModel(
        factory = ClothingViewModelFactory(context)
    )

    val randomOutfit by clothingViewModel.randomOutfit.collectAsState()
    var showFavoriteDialog by remember { mutableStateOf(false) }

    AppBackground() {
        Scaffold(
            modifier = modifier.systemBarsPadding(),
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        TopBarComponent(
                            onSettingsClick = onSettingsClick,
                            onProfileClick = onProfileClick
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        WelcomeHeaderComponent(userName = userName)

                        Spacer(modifier = Modifier.height(24.dp))

                        OutfitCardComponent(
                            shirt = randomOutfit.shirt,
                            pant = randomOutfit.pant,
                            upper = randomOutfit.upper,
                            shoes = randomOutfit.shoes,
                            onShuffleClick = {
                                clothingViewModel.generateRandomOutfit() },
                            onFavoriteClick = { showFavoriteDialog = true }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 24.dp)
                    ) {
                        BottomNavigationComponent(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .wrapContentHeight(),
                            onWardrobeClick = onWardrobeClick
                        )

                        FloatingActionButtonComponent(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 8.dp, bottom = 8.dp),
                            onClick = onAddGarmentClick
                        )
                    }
                }
            }
        }
    }

    if (showFavoriteDialog) {
        AlertDialog(
            containerColor = Color(0xFF1E1E1E),
            onDismissRequest = { showFavoriteDialog = false },
            title = {
                Text(
                    text = "Guardar Outfit",
                    color = androidx.compose.ui.graphics.Color.White
                )
            },
            text = {
                Text(
                    text = "¿Guardar Outfit como favorito?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    clothingViewModel.saveCurrentOutfitAsFavorite()
                    showFavoriteDialog = false
                }) {
                    Text(
                        text = "GUARDAR",
                        color = GoldAccent
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showFavoriteDialog = false }) {
                    Text(
                        text = "CANCELAR",
                        color = TextSecondary
                    )
                }
            }
        )
    }
}
