package com.uvitos.fastoutfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uvitos.fastoutfit.ui.screens.SplashScreen  // corregido
import com.google.firebase.auth.FirebaseAuth
import com.uvitos.fastoutfit.ui.components.Routes
import com.uvitos.fastoutfit.ui.screens.AddScreen
import com.uvitos.fastoutfit.ui.screens.LoginScreen
import com.uvitos.fastoutfit.ui.screens.HomeScreen
import com.uvitos.fastoutfit.ui.screens.RegisterScreen
import com.uvitos.fastoutfit.ui.screens.SettingsScreen
import com.uvitos.fastoutfit.ui.viewmodel.AuthState
import com.uvitos.fastoutfit.ui.viewmodel.AuthViewModel
import com.uvitos.fastoutfit.ui.screens.WardrobeScreen

@Composable
fun FastOutfitNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    val startDestination = if (authViewModel.isUserLoggedIn) Routes.HOME else Routes.SPLASH

    NavHost(
        navController = navController,
        startDestination = startDestination  // corregido: usa la variable
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Routes.LOGIN) {
            val resetState by authViewModel.resetState.collectAsState()

            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    authViewModel.resetState()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
            LoginScreen(
                onLoginClick = { email, password -> authViewModel.login(email, password) },
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onGoogleSignIn = { /* TODO */ },
                onForgotPasswordClick = { email -> authViewModel.sendPasswordReset(email) },
                authState = authState,
            )
        }

        composable(Routes.REGISTER) {
            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    authViewModel.resetState()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            }
            RegisterScreen(
                onLoginClick = { navController.popBackStack() },
                onRegisterClick = { email, password, confirm ->
                    authViewModel.register(email, password, confirm)
                },
                authState = authState,
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                userName = FirebaseAuth.getInstance().currentUser?.email ?: "USUARIO",
                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                onProfileClick = { /* TODO: Navigate to profile */ },
                onShuffleClick = { /* TODO: Generate new outfit */ },
                onFavoriteClick = { /* TODO: Save outfit to favorites */ },
                onAddGarmentClick = { navController.navigate(Routes.ADD_ITEM) },
                onWardrobeClick = { navController.navigate(Routes.WARDROBE) }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onLogOutClick = {
                    authViewModel.signOut()
                    navController.navigate(Routes.LOGIN){
                        popUpTo(Routes.HOME) {inclusive = true}
                    }
                },
                onHomeClick = {
                    navController.popBackStack()
            }
            )
        }

        composable(Routes.WARDROBE) {
            WardrobeScreen(
                onHomeClick    = { navController.navigate(Routes.HOME) },
                onHelpClick    = { /* TODO: ayuda */ },
                onProfileClick = { /* TODO: perfil */ },
                onAddClick     = { navController.navigate(Routes.ADD_ITEM)},
                onFilterClick  = { category -> /* TODO: filtrar $category */ },
                onFavoriteClick = { garment -> /* TODO: marcar favorito ${garment.id} */ },
                onDeleteClick   = { garment -> /* TODO: eliminar ${garment.id} */ },
            )
        }

        composable(Routes.ADD_ITEM) {
            AddScreen(
                onHomeClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ADD_ITEM) {inclusive = true}
                    }
                },
                onHelpClick = {}
            )
        }
    }
}