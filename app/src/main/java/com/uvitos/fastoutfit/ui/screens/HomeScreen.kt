package com.uvitos.fastoutfit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.ui.components.BottomNavigationComponent
import com.uvitos.fastoutfit.ui.components.FloatingActionButtonComponent
import com.uvitos.fastoutfit.ui.components.OutfitCardComponent
import com.uvitos.fastoutfit.ui.components.TopBarComponent
import com.uvitos.fastoutfit.ui.components.WelcomeHeaderComponent

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String = "USUARIO",
    onSettingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onAddGarmentClick: () -> Unit = {},
    onWardrobeClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.systemBarsPadding(),
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

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
                        onShuffleClick = onShuffleClick,
                        onFavoriteClick = onFavoriteClick
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
