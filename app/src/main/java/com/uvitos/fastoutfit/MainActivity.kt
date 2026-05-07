package com.uvitos.fastoutfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uvitos.fastoutfit.ui.screens.HomeScreen
import com.uvitos.fastoutfit.ui.theme.FastOutfitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FastOutfitTheme {
                HomeScreen(
                    userName = "USUARIO",
                    onSettingsClick = { /* TODO: Navigate to settings */ },
                    onProfileClick = { /* TODO: Navigate to profile */ },
                    onShuffleClick = { /* TODO: Generate new outfit */ },
                    onFavoriteClick = { /* TODO: Save outfit to favorites */ },
                    onAddGarmentClick = { /* TODO: Navigate to add garment */ },
                    onWardrobeClick = { /* TODO: Navigate to wardrobe */ }
                )
            }
        }
    }
}
