package com.uvitos.fastoutfit

import com.google.firebase.auth.FirebaseAuth
import com.uvitos.fastoutfit.ui.viewmodel.AuthState
import com.uvitos.fastoutfit.ui.viewmodel.AuthViewModel
import com.uvitos.fastoutfit.util.MainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AuthViewModel
    private val mockAuth: FirebaseAuth = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = AuthViewModel(auth = mockAuth)
    }

    // ── validatePassword ─────────────────────────────────────────────────

    @Test
    fun `validatePassword - contraseña valida retorna null`() {
        val result = viewModel.validatePassword("Segura1@")
        assertEquals(null, result)
    }

    @Test
    fun `validatePassword - menos de 8 caracteres retorna error`() {
        val result = viewModel.validatePassword("Cor1@")
        assertEquals("Mínimo 8 caracteres", result)
    }

    @Test
    fun `validatePassword - mas de 16 caracteres retorna error`() {
        val result = viewModel.validatePassword("Contrasena1@MuyLarga")
        assertEquals("Máximo 16 caracteres", result)
    }

    @Test
    fun `validatePassword - sin mayuscula retorna error`() {
        val result = viewModel.validatePassword("sinmayus1@")
        assertEquals("Debe tener al menos una mayúscula", result)
    }

    @Test
    fun `validatePassword - sin minuscula retorna error`() {
        val result = viewModel.validatePassword("SINMINUS1@")
        assertEquals("Debe tener al menos una minúscula", result)
    }

    @Test
    fun `validatePassword - sin numero retorna error`() {
        val result = viewModel.validatePassword("SinNumero@")
        assertEquals("Debe tener al menos un número", result)
    }

    @Test
    fun `validatePassword - sin signo retorna error`() {
        val result = viewModel.validatePassword("SinSigno1")
        assertEquals("Debe tener al menos un signo (_-@*#\$!)", result)
    }

    // ── login - validación de campos vacíos ──────────────────────────────

    @Test
    fun `login - email vacio setea error`() = runTest {
        viewModel.login("", "password123")
        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals("Los campos no pueden ser vacios", (state as AuthState.Error).message)
    }

    @Test
    fun `login - password vacio setea error`() = runTest {
        viewModel.login("test@email.com", "")
        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals("Los campos no pueden ser vacios", (state as AuthState.Error).message)
    }

    @Test
    fun `login - ambos campos vacios setea error`() = runTest {
        viewModel.login("", "")
        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
    }

    // ── register - validación de campos ──────────────────────────────────

    @Test
    fun `register - email vacio setea error`() = runTest {
        viewModel.register("", "nombre", "Password1@", "Password1@")
        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
    }

    @Test
    fun `register - contrasenas no coinciden setea error`() = runTest {
        viewModel.register("test@email.com", "nombre", "Password1@", "Password2@")
        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals("Las contraseñas no coinciden", (state as AuthState.Error).message)
    }

    @Test
    fun `register - contrasena debil setea error`() = runTest {
        viewModel.register("test@email.com", "nombre", "debil", "debil")
        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
    }

    // ── resetState ───────────────────────────────────────────────────────

    @Test
    fun `resetState - vuelve a Idle`() = runTest {
        viewModel.login("", "")
        viewModel.resetState()
        assertEquals(AuthState.Idle, viewModel.authState.value)
    }

    // ── sendPasswordReset - validación ───────────────────────────────────

    @Test
    fun `sendPasswordReset - email vacio setea error en resetState`() = runTest {
        viewModel.sendPasswordReset("")
        val state = viewModel.resetState.value
        assertTrue(state is AuthState.Error)
        assertEquals("Ingresa tu correo electrónico", (state as AuthState.Error).message)
    }
}