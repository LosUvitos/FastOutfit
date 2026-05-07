package com.uvitos.fastoutfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fastoutfit.ui.screens.SplashScreen
import com.uvitos.fastoutfit.ui.components.Routes
import com.uvitos.fastoutfit.ui.screens.LoginScreen

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
            SplashScreen(onFinished = {navController.navigate(Routes.LOGIN)})
        }
    }
}