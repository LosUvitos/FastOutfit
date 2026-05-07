package com.uvitos.fastoutfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fastoutfit.ui.screens.SplashScreen
import com.uvitos.fastoutfit.ui.components.Routes
import com.uvitos.fastoutfit.ui.screens.LoginScreen
import com.uvitos.fastoutfit.ui.screens.HomeScreen
import com.uvitos.fastoutfit.ui.screens.RegisterScreen
import com.uvitos.fastoutfit.ui.screens.SettingsScreen

@Composable
fun FastOutfitNavGraph()
{
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Routes.SPLASH
    ) {
        //Add here new Screens
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = {name, password -> navController.navigate(Routes.HOME)},
                onRegisterClick = {navController.navigate(Routes.REGISTER)}
                )
        }

        composable (Routes.SPLASH) {
            SplashScreen(onFinished = { navController.navigate(Routes.LOGIN) })
        }

        composable(Routes.HOME) {
                HomeScreen(
                    userName = "USUARIO",
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                    onProfileClick = { /* TODO: Navigate to profile */ },
                    onShuffleClick = { /* TODO: Generate new outfit */ },
                    onFavoriteClick = { /* TODO: Save outfit to favorites */ },
                    onAddGarmentClick = { /* TODO: Navigate to add garment */ },
                    onWardrobeClick = { /* TODO: Navigate to wardrobe */ }
                )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onLoginClick = {navController.navigate(Routes.LOGIN)}
            )
        }
        composable(Routes.SETTINGS) { SettingsScreen() }
    }
}
