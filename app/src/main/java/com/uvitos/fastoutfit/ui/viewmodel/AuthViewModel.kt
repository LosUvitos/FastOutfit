package com.uvitos.fastoutfit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState
    private val _resetState = MutableStateFlow<AuthState>(AuthState.Idle)
    val resetState: StateFlow<AuthState> = _resetState

    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Los campos no pueden ser vacios")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    val currentUserName: String
        get() = auth.currentUser?.displayName
            ?.takeIf { it.isNotBlank() }
            ?: auth.currentUser?.email?.substringBefore("@")
            ?: "Usuario"

    fun register(email: String, name: String, password: String, confirmPassword: String){
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Los campos no pueden ser vacios")
            return
        }

        if (password != confirmPassword){
            _authState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }

        val passwordError = validatePassword(password)
        if (passwordError != null){
            _authState.value = AuthState.Error(passwordError)
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                val user = auth.currentUser
                val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                    displayName = name
                }
                user?.updateProfile(profileUpdates)?.await()
                _authState.value = AuthState.Success
            } catch (e: Exception){
                _authState.value = AuthState.Error(e.message ?: "Error al intentar registrarse")
            }
        }
    }

    fun updateDisplayName(newName: String) {
        viewModelScope.launch {
            try {
                val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                    displayName = newName
                }
                auth.currentUser?.updateProfile(profileUpdates)?.await()
            } catch (_: Exception) { }
        }
    }

    fun loginWithGoogle(idToken: String){
        viewModelScope.launch{
            _authState.value = AuthState.Loading
            try{
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                _authState.value = AuthState.Success
            } catch (e: Exception){
                _authState.value = AuthState.Error(e.message ?: "Error con google")
            }
        }
    }

    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun signOutWithGoogle(context: android.content.Context){
        val credentialManager = androidx.credentials.CredentialManager.create(context)
        viewModelScope.launch {
            try {
                credentialManager.clearCredentialState(
                    androidx.credentials.ClearCredentialStateRequest()
                )
            } catch (_: Exception) { }
        }
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun sendPasswordReset(email: String){
        if (email.isBlank()){
            _resetState.value = AuthState.Error("Ingresa tu correo electrónico")
            return
        }
        viewModelScope.launch {
            _resetState.value = AuthState.Loading
            try {
                auth.sendPasswordResetEmail(email).await()
                _resetState.value = AuthState.Success
            } catch (e: Exception){
                _resetState.value = AuthState.Error(e.message ?: "Error al enviar el correo de reestablecimiento")
            }
        }
    }

    fun resetPasswordState(){
        _resetState.value = AuthState.Idle
    }
    fun resetState(){
        _authState.value = AuthState.Idle
    }

    private fun validatePassword(password: String): String? {
        if (password.length < 8)  return "Mínimo 8 caracteres"
        if (password.length > 16) return "Máximo 16 caracteres"
        if (!password.any { it.isUpperCase() }) return "Debe tener al menos una mayúscula"
        if (!password.any { it.isLowerCase() }) return "Debe tener al menos una minúscula"
        if (!password.any { it.isDigit() })     return "Debe tener al menos un número"
        if (!password.any { it in "_-@*#\$!" }) return "Debe tener al menos un signo (_-@*#\$!)"
        return null
    }
}