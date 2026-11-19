package com.example.mavuno.data.repository

import com.example.mavuno.data.local.TokenManager
import com.example.mavuno.data.model.AuthResponse
import com.example.mavuno.data.model.LoginRequest
import com.example.mavuno.data.model.RegisterRequest
import com.example.mavuno.data.model.User
import com.example.mavuno.data.remote.AuthApiService
import com.example.mavuno.data.remote.NetworkResult
import com.example.mavuno.data.remote.RetrofitClient
import com.google.gson.JsonParser
import retrofit2.Response
import java.io.IOException

/**
 * Repository responsible for user authentication (Login and Registration) and profile management.
 * Interacts with AuthApiService and handles responses, errors, and secure token storage.
 */
class AuthRepository(
    private val apiService: AuthApiService = RetrofitClient.apiService,
    private val tokenManager: TokenManager? = null
) {

    /**
     * Authenticates a user with email and password.
     */
    suspend fun login(loginRequest: LoginRequest): NetworkResult<AuthResponse> {
        return try {
            val response = apiService.login(loginRequest)
            handleAuthResponse(response)
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    /**
     * Registers a new user with registration details.
     */
    suspend fun register(registerRequest: RegisterRequest): NetworkResult<AuthResponse> {
        return try {
            val response = apiService.register(registerRequest)
            handleAuthResponse(response)
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    /**
     * Logs out the user by calling backend logout endpoint and clearing stored tokens.
     */
    suspend fun logout(): NetworkResult<Unit> {
        return try {
            val refreshToken = tokenManager?.getRefreshToken()
            if (!refreshToken.isNullOrBlank()) {
                val response = apiService.logout(mapOf("refresh" to refreshToken))
                tokenManager?.clearTokens()
                if (response.isSuccessful || response.code() == 400 || response.code() == 204) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error("Logout failed", response.code())
                }
            } else {
                tokenManager?.clearTokens()
                NetworkResult.Success(Unit)
            }
        } catch (e: Exception) {
            tokenManager?.clearTokens()
            NetworkResult.Exception(e)
        }
    }

    /**
     * Retrieves the authenticated user's profile.
     */
    suspend fun getProfile(): NetworkResult<User> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Failed to fetch profile", response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    /**
     * Updates the authenticated user's profile.
     */
    suspend fun updateProfile(fullName: String?, phoneNumber: String?): NetworkResult<User> {
        return try {
            val body = mutableMapOf<String, String?>()
            if (fullName != null) body["full_name"] = fullName
            if (phoneNumber != null) body["phone_number"] = phoneNumber

            val response = apiService.updateProfile(body)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorMsg = parseErrorResponseBody(response.errorBody()?.string(), response.code())
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    /**
     * Deletes the user's account and clears tokens.
     */
    suspend fun deleteAccount(): NetworkResult<Unit> {
        return try {
            val response = apiService.deleteAccount()
            if (response.isSuccessful || response.code() == 204) {
                tokenManager?.clearTokens()
                NetworkResult.Success(Unit)
            } else {
                val errorMsg = parseErrorResponseBody(response.errorBody()?.string(), response.code())
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    private fun handleAuthResponse(response: Response<AuthResponse>): NetworkResult<AuthResponse> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                // Securely save access and refresh tokens upon successful authentication
                tokenManager?.saveTokens(
                    accessToken = body.access,
                    refreshToken = body.refresh
                )
                NetworkResult.Success(body)
            } else {
                NetworkResult.Error("Empty response body from server", response.code())
            }
        } else {
            val errorMessage = parseErrorResponseBody(response.errorBody()?.string(), response.code())
            NetworkResult.Error(errorMessage, response.code())
        }
    }

    private fun parseErrorResponseBody(errorJson: String?, statusCode: Int): String {
        if (errorJson.isNullOrBlank()) {
            return "Server returned an error (HTTP $statusCode)"
        }
        return try {
            val jsonElement = JsonParser.parseString(errorJson)
            if (!jsonElement.isJsonObject) {
                return "Server returned an error (HTTP $statusCode)"
            }
            val jsonObject = jsonElement.asJsonObject

            // 1. Check for "detail" field (DRF standard detail error)
            if (jsonObject.has("detail") && jsonObject.get("detail").isJsonPrimitive) {
                val detail = jsonObject.get("detail").asString
                if (detail.isNotBlank()) return detail
            }

            // 2. Check for "non_field_errors" array
            if (jsonObject.has("non_field_errors") && jsonObject.get("non_field_errors").isJsonArray) {
                val array = jsonObject.getAsJsonArray("non_field_errors")
                if (array.size() > 0) {
                    val errors = mutableListOf<String>()
                    for (i in 0 until array.size()) {
                        errors.add(array.get(i).asString)
                    }
                    return errors.joinToString("; ")
                }
            }

            // 3. Extract field-level errors (e.g., {"email": ["An account with this email already exists."]})
            val errorMessages = mutableListOf<String>()
            for ((key, value) in jsonObject.entrySet()) {
                if (value.isJsonArray) {
                    val array = value.asJsonArray
                    val fieldErrors = mutableListOf<String>()
                    for (i in 0 until array.size()) {
                        fieldErrors.add(array.get(i).asString)
                    }
                    if (fieldErrors.isNotEmpty()) {
                        errorMessages.add("$key: ${fieldErrors.joinToString(", ")}")
                    }
                } else if (value.isJsonPrimitive) {
                    val strVal = value.asString
                    if (strVal.isNotBlank()) {
                        errorMessages.add("$key: $strVal")
                    }
                }
            }

            if (errorMessages.isNotEmpty()) {
                errorMessages.joinToString("\n")
            } else {
                "Server returned an error (HTTP $statusCode)"
            }
        } catch (e: Exception) {
            "Server returned an error (HTTP $statusCode)"
        }
    }
}
