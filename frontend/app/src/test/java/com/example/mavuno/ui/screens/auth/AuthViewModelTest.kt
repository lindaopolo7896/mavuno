package com.example.mavuno.ui.screens.auth

import com.example.mavuno.data.model.AuthResponse
import com.example.mavuno.data.model.LoginRequest
import com.example.mavuno.data.model.RegisterRequest
import com.example.mavuno.data.model.Scan
import com.example.mavuno.data.model.ScanPageResponse
import com.example.mavuno.data.model.TokenRefreshResponse
import com.example.mavuno.data.model.User
import com.example.mavuno.data.remote.AuthApiService
import com.example.mavuno.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private class FakeAuthApiService : AuthApiService {
        var responseToReturn: Response<AuthResponse>? = null

        private val testUser = User(
            id = 1,
            email = "test@example.com",
            full_name = "Test User",
            phone_number = "+254700000000",
            date_joined = "2026-11-13T23:10:14.679307+03:00"
        )

        override suspend fun register(request: RegisterRequest): Response<AuthResponse> {
            return responseToReturn ?: Response.error(500, "".toResponseBody(null))
        }

        override suspend fun login(request: LoginRequest): Response<AuthResponse> {
            return responseToReturn ?: Response.error(500, "".toResponseBody(null))
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

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun login_success_updatesStateToSuccess() {
        val fakeService = FakeAuthApiService().apply {
            responseToReturn = Response.success(fakeAuthResponse)
        }
        val repository = AuthRepository(apiService = fakeService)
        val viewModel = AuthViewModel(authRepository = repository)

        viewModel.login("test@example.com", "password123")

        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthState.Success)
        assertEquals("test@example.com", (currentState as AuthState.Success).user.email)
    }

    @Test
    fun login_emptyInput_updatesStateToError() {
        val viewModel = AuthViewModel()

        viewModel.login("", "")

        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthState.Error)
        assertEquals("Email and password cannot be empty.", (currentState as AuthState.Error).message)
    }

    @Test
    fun login_invalidCredentials_updatesStateToErrorWithMessage() {
        val errorJson = """{"detail":"No active account found with the given credentials"}"""
        val fakeService = FakeAuthApiService().apply {
            responseToReturn = Response.error(401, errorJson.toResponseBody("application/json".toMediaType()))
        }
        val repository = AuthRepository(apiService = fakeService)
        val viewModel = AuthViewModel(authRepository = repository)

        viewModel.login("test@example.com", "wrongpassword")

        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthState.Error)
        assertEquals("No active account found with the given credentials", (currentState as AuthState.Error).message)
    }

    @Test
    fun register_success_updatesStateToSuccess() {
        val fakeService = FakeAuthApiService().apply {
            responseToReturn = Response.success(fakeAuthResponse)
        }
        val repository = AuthRepository(apiService = fakeService)
        val viewModel = AuthViewModel(authRepository = repository)

        viewModel.register("test@example.com", "password123", "Test User", "+254700000000")

        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthState.Success)
        assertEquals("test@example.com", (currentState as AuthState.Success).user.email)
    }

    @Test
    fun register_shortPassword_updatesStateToError() {
        val viewModel = AuthViewModel()

        viewModel.register("test@example.com", "short")

        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthState.Error)
        assertEquals("Password must be at least 8 characters long.", (currentState as AuthState.Error).message)
    }

    @Test
    fun resetState_resetsStateToIdle() {
        val viewModel = AuthViewModel()
        viewModel.login("", "") // transitions to Error

        viewModel.resetState()

        assertEquals(AuthState.Idle, viewModel.authState.value)
    }
}
