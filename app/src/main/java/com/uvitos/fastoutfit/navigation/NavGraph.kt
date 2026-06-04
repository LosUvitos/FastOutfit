package com.uvitos.fastoutfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uvitos.fastoutfit.ui.screens.SplashScreen  // corregido
import com.google.firebase.auth.FirebaseAuth
import com.uvitos.fastoutfit.navigation.Routes
import com.uvitos.fastoutfit.ui.screens.AddScreen
import com.uvitos.fastoutfit.ui.screens.LoginScreen
import com.uvitos.fastoutfit.ui.screens.HomeScreen
import com.uvitos.fastoutfit.ui.screens.NotificationsScreen
import com.uvitos.fastoutfit.ui.screens.RegisterScreen
import com.uvitos.fastoutfit.ui.screens.SettingsScreen
import com.uvitos.fastoutfit.ui.viewmodel.AuthState
import com.uvitos.fastoutfit.ui.viewmodel.AuthViewModel
import com.uvitos.fastoutfit.ui.screens.WardrobeScreen
import kotlinx.coroutines.launch
import com.uvitos.fastoutfit.ui.screens.addScreenTest
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModel
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModelFactory
import com.uvitos.fastoutfit.ui.screens.UserScreen

@Composable
fun FastOutfitNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val clothingViewModel: ClothingViewModel = viewModel(
        factory = ClothingViewModelFactory(context)
    )
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
            val context = androidx.compose.ui.platform.LocalContext.current
            val scope = rememberCoroutineScope()

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
                onGoogleSignIn = {
                    val credentialManager = androidx.credentials.CredentialManager.create(context)
                    val googleIdOption = com.google.android.libraries.identity.googleid.GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(context.getString(com.uvitos.fastoutfit.R.string.google_sign_in_server_client_id))
                        .build()
                    val request = androidx.credentials.GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    scope.launch {
                        try {
                            val result = credentialManager.getCredential(context, request)
                            val credential = result.credential
                            val googleIdTokenCredential = com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
                                .createFrom(credential.data)
                            authViewModel.loginWithGoogle(googleIdTokenCredential.idToken)
                        } catch (e: Exception){
                            // El usuario canceló o error
                        }
                    }
                },

                onForgotPasswordClick = { email -> authViewModel.sendPasswordReset(email) },
                authState = authState,
            )
        }

        composable(Routes.REGISTER) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val scope = rememberCoroutineScope()

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
                onGoogleSignIn = {
                    val credentialManager = androidx.credentials.CredentialManager.create(context)
                    val googleIdOption = com.google.android.libraries.identity.googleid.GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(context.getString(com.uvitos.fastoutfit.R.string.google_sign_in_server_client_id))
                        .build()
                    val request = androidx.credentials.GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    scope.launch {
                        try {
                            val result = credentialManager.getCredential(context, request)
                            val credential = result.credential
                            val googleIdTokenCredential = com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
                                .createFrom(credential.data)
                            authViewModel.loginWithGoogle(googleIdTokenCredential.idToken)
                        } catch (_: Exception) { }
                    }
                },
                authState = authState,
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                userName = FirebaseAuth.getInstance().currentUser?.email ?: "USUARIO",
                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                onProfileClick = { navController.navigate(Routes.USER) },
                onShuffleClick = { /* TODO: Generate new outfit */ },
                onFavoriteClick = { /* TODO: Save outfit to favorites */ },
                onAddGarmentClick = { navController.navigate(Routes.ADD_ITEM) },
                onWardrobeClick = { navController.navigate(Routes.WARDROBE) }
            )
        }

        composable(Routes.SETTINGS) {
            val context = androidx.compose.ui.platform.LocalContext.current
            SettingsScreen(
                onLogOutClick = {
                    authViewModel.signOutWithGoogle(context)
                    navController.navigate(Routes.LOGIN){
                        popUpTo(Routes.HOME) {inclusive = true}
                    }
                },
                onHomeClick = {
                    navController.popBackStack()
            },

                onNotificationsClick = {navController.navigate(Routes.NOTIFICATIONS)}
            )
        }

        composable(Routes.USER) {
            UserScreen(
                    onHomeClick = {
                        navController.navigate(Routes.HOME)
                    },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                    onHelpClick = {
                        // TODO
                    }
                )
        }


        composable(Routes.WARDROBE) {
            WardrobeScreen(
                onHomeClick    = { navController.navigate(Routes.HOME) },
                onHelpClick    = { /* TODO: ayuda */ },
                onProfileClick = { /* TODO: perfil */ },
                onAddClick     = { navController.navigate(Routes.ADD_ITEM)},
                onFilterClick  = {/* TODO: filtrar $category */ },
                onFavoriteClick = { garment -> /* TODO: marcar favorito ${garment.id} */ },
                onDeleteClick   = { garment -> /* TODO: eliminar ${garment.id} */ },
                clothingViewModel = clothingViewModel
            )
        }

        composable(Routes.ADD_ITEM) {
            AddScreen(
                onHomeClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ADD_ITEM) {inclusive = true}
                    }
                },
                onHelpClick = {},
                onOkayClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ADD_ITEM) {inclusive = true}
                    }
                },
                viewModel = clothingViewModel
            )
        }

        composable(Routes.NOTIFICATIONS){
            NotificationsScreen (
                onBackClick = {navController.popBackStack()}
            )
        }
    }
}
