package com.example.mavuno.data.remote

/**
 * A generic class that holds a value with its loading status or error.
 * Used to wrap network response states for repository operations.
 */
sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>
    data class Exception(val throwable: Throwable) : NetworkResult<Nothing>
}
