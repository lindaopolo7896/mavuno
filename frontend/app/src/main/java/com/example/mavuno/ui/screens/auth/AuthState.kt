package com.example.mavuno.ui.screens.auth

import com.example.mavuno.data.model.User

/**
 * Sealed interface representing all possible UI states for authentication flows.
 */
sealed interface AuthState {
    /** Initial state before any authentication action */
    object Idle : AuthState

    /** Loading state while an authentication network request is in flight */
    object Loading : AuthState

    /** Success state containing the authenticated user details */
    data class Success(val user: User) : AuthState

    /** Error state containing a user-friendly error message */
    data class Error(val message: String) : AuthState
}
