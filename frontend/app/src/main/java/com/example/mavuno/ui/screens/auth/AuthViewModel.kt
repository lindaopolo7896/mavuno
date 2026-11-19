package com.example.mavuno.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mavuno.data.model.LoginRequest
import com.example.mavuno.data.model.RegisterRequest
import com.example.mavuno.data.remote.NetworkResult
import com.example.mavuno.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel managing authentication state and actions (Login and Registration).
 */
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /**
     * Performs user login with input validation and duplicate submission guard.
     */
    fun login(email: String, password: String) {
        // Prevent duplicate submissions while a request is in flight
        if (_authState.value is AuthState.Loading) return

        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty.")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val request = LoginRequest(email = trimmedEmail, password = trimmedPassword)
            val result = authRepository.login(request)
            _authState.value = when (result) {
                is NetworkResult.Success -> AuthState.Success(result.data.user)
                is NetworkResult.Error -> AuthState.Error(result.message)
                is NetworkResult.Exception -> AuthState.Error(
                    result.throwable.localizedMessage ?: "A network error occurred. Please check your connection."
                )
            }
        }
    }

    /**
     * Performs user registration with input validation and duplicate submission guard.
     */
    fun register(
        email: String,
        password: String,
        fullName: String? = null,
        phoneNumber: String? = null
    ) {
        // Prevent duplicate submissions while a request is in flight
        if (_authState.value is AuthState.Loading) return

        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedFullName = fullName?.trim()?.ifBlank { null }
        val trimmedPhoneNumber = phoneNumber?.trim()?.ifBlank { null }

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            _authState.value = AuthState.Error("Email and password are required.")
            return
        }

        if (trimmedPassword.length < 8) {
            _authState.value = AuthState.Error("Password must be at least 8 characters long.")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val request = RegisterRequest(
                email = trimmedEmail,
                password = trimmedPassword,
                full_name = trimmedFullName,
                phone_number = trimmedPhoneNumber
            )
            val result = authRepository.register(request)
            _authState.value = when (result) {
                is NetworkResult.Success -> AuthState.Success(result.data.user)
                is NetworkResult.Error -> AuthState.Error(result.message)
                is NetworkResult.Exception -> AuthState.Error(
                    result.throwable.localizedMessage ?: "A network error occurred. Please check your connection."
                )
            }
        }
    }

    /**
     * Resets authentication state back to Idle.
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
