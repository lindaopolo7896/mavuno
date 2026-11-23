package com.example.mavuno.data.repository

import com.example.mavuno.data.model.AuthResponse
import com.example.mavuno.data.model.LoginRequest
import com.example.mavuno.data.model.RegisterRequest
import com.example.mavuno.data.model.Scan
import com.example.mavuno.data.model.ScanPageResponse
import com.example.mavuno.data.model.TokenRefreshResponse
import com.example.mavuno.data.model.User
import com.example.mavuno.data.remote.AuthApiService
import com.example.mavuno.data.remote.NetworkResult
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class AuthRepositoryTest {

    private class FakeAuthApiService : AuthApiService {
        var shouldThrowNetworkException = false
        var responseToReturn: Response<AuthResponse>? = null

        private val testUser = User(
            id = 1,
            email = "test@example.com",
            full_name = "Test User",
            phone_number = "+254700000000",
            date_joined = "2026-11-13T23:10:14.679307+03:00"
        )

        override suspend fun register(request: RegisterRequest): Response<AuthResponse> {
            if (shouldThrowNetworkException) {
                throw IOException("Network failure")
            }
            return responseToReturn ?: Response.error<AuthResponse>(500, "".toResponseBody(null))
        }

        override suspend fun login(request: LoginRequest): Response<AuthResponse> {
            if (shouldThrowNetworkException) {
                throw IOException("Network failure")
            }
            return responseToReturn ?: Response.error<AuthResponse>(500, "".toResponseBody(null))
        }

        override suspend fun refreshToken(request: Map<String, String>): Response<TokenRefreshResponse> {
            return Response.error(500, "".toResponseBody(null))
        }

        override fun refreshTokenSync(request: Map<String, String>): Call<TokenRefreshResponse> {
            throw UnsupportedOperationException()
        }

        override suspend fun logout(request: Map<String, String>): Response<Void> {
            return Response.success(null)
        }

        override suspend fun predictScan(image: MultipartBody.Part, scannedAt: RequestBody?): Response<Scan> {
            return Response.error(500, "".toResponseBody(null))
        }

        override suspend fun getScans(page: Int?, isHealthy: Boolean?, plant: String?, search: String?): Response<ScanPageResponse> {
            return Response.error(500, "".toResponseBody(null))
        }

        override suspend fun getScanDetail(id: String): Response<Scan> {
            return Response.error(404, "".toResponseBody(null))
        }

        override suspend fun deleteScan(id: String): Response<Void> {
            return Response.success(null)
        }

        override suspend fun getProfile(): Response<User> {
            return Response.success(testUser)
        }

        override suspend fun updateProfile(body: Map<String, String?>): Response<User> {
            return Response.success(testUser)
        }

        override suspend fun deleteAccount(): Response<Void> {
            return Response.success(null)
        }
    }

    private val fakeUser = User(
        id = 1,
        email = "test@example.com",
        full_name = "Test User",
        phone_number = "+254700000000",
        date_joined = "2026-11-13T23:10:14.679307+03:00"
    )

    private val fakeAuthResponse = AuthResponse(
        user = fakeUser,
        refresh = "fake_refresh_token",
        access = "fake_access_token"
    )

    @Test
    fun login_success_returnsSuccessResult() = runBlocking {
        val fakeService = FakeAuthApiService().apply {
            responseToReturn = Response.success(fakeAuthResponse)
        }
        val repository = AuthRepository(apiService = fakeService)

        val result = repository.login(LoginRequest("test@example.com", "password123"))

        assertTrue(result is NetworkResult.Success)
        val successData = (result as NetworkResult.Success).data
        assertEquals("fake_access_token", successData.access)
        assertEquals("fake_refresh_token", successData.refresh)
        assertEquals("test@example.com", successData.user.email)
    }

    @Test
    fun login_invalidCredentials_returnsErrorResultWithDetailMessage() = runBlocking {
        val errorJson = """{"detail":"No active account found with the given credentials"}"""
        val fakeService = FakeAuthApiService().apply {
            responseToReturn = Response.error(401, errorJson.toResponseBody("application/json".toMediaType()))
        }
        val repository = AuthRepository(apiService = fakeService)

        val result = repository.login(LoginRequest("test@example.com", "wrongpass"))

        assertTrue(result is NetworkResult.Error)
        val errorResult = result as NetworkResult.Error
        assertEquals(401, errorResult.code)
        assertEquals("No active account found with the given credentials", errorResult.message)
    }

    @Test
    fun register_existingEmail_returnsErrorResultWithFieldError() = runBlocking {
        val errorJson = """{"email":["An account with this email already exists."]}"""
        val fakeService = FakeAuthApiService().apply {
            responseToReturn = Response.error(400, errorJson.toResponseBody("application/json".toMediaType()))
        }
        val repository = AuthRepository(apiService = fakeService)

        val result = repository.register(RegisterRequest("test@example.com", "password123"))

        assertTrue(result is NetworkResult.Error)
        val errorResult = result as NetworkResult.Error
        assertEquals(400, errorResult.code)
        assertEquals("email: An account with this email already exists.", errorResult.message)
    }

    @Test
    fun login_networkException_returnsExceptionResult() = runBlocking {
        val fakeService = FakeAuthApiService().apply {
            shouldThrowNetworkException = true
        }
        val repository = AuthRepository(apiService = fakeService)

        val result = repository.login(LoginRequest("test@example.com", "password123"))

        assertTrue(result is NetworkResult.Exception)
        val exceptionResult = result as NetworkResult.Exception
        assertTrue(exceptionResult.throwable is IOException)
    }
}
