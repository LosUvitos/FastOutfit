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

    fun register(email: String, password: String, confirmPassword: String){
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Los campos no pueden ser vacios")
            return
        }

        if (password != confirmPassword){
            _authState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Success
            } catch (e: Exception){
                _authState.value = AuthState.Error(e.message ?: "Error al intentar registrarse")
            }
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

    fun resetState(){
        _authState.value = AuthState.Idle
    }
}