package com.example.mavuno.data.model

data class User(
    val id: Int,
    val email: String,
    val full_name: String?,
    val phone_number: String?,
    val date_joined: String
)

data class AuthResponse(
    val user: User,
    val refresh: String,
    val access: String
)

data class TokenRefreshResponse(
    val access: String,
    val refresh: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val full_name: String? = null,
    val phone_number: String? = null
)
