package com.example.mavuno.data.remote

import com.example.mavuno.data.model.AuthResponse
import com.example.mavuno.data.model.LoginRequest
import com.example.mavuno.data.model.RegisterRequest
import com.example.mavuno.data.model.Scan
import com.example.mavuno.data.model.ScanPageResponse
import com.example.mavuno.data.model.TokenRefreshResponse
import com.example.mavuno.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {

    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body request: Map<String, String>): Response<TokenRefreshResponse>

    @POST("auth/token/refresh/")
    fun refreshTokenSync(@Body request: Map<String, String>): Call<TokenRefreshResponse>

    @POST("auth/logout/")
    suspend fun logout(@Body request: Map<String, String>): Response<Void>

    @Multipart
    @POST("scans/predict/")
    suspend fun predictScan(
        @Part image: MultipartBody.Part,
        @Part("scanned_at") scannedAt: RequestBody? = null
    ): Response<Scan>

    @GET("scans/")
    suspend fun getScans(
        @Query("page") page: Int? = null,
        @Query("is_healthy") isHealthy: Boolean? = null,
        @Query("plant") plant: String? = null,
        @Query("search") search: String? = null
    ): Response<ScanPageResponse>

    @GET("scans/{id}/")
    suspend fun getScanDetail(@Path("id") id: String): Response<Scan>

    @DELETE("scans/{id}/")
    suspend fun deleteScan(@Path("id") id: String): Response<Void>

    @GET("users/me/")
    suspend fun getProfile(): Response<User>

    @PATCH("users/me/")
    suspend fun updateProfile(@Body body: Map<String, String?>): Response<User>

    @DELETE("users/me/")
    suspend fun deleteAccount(): Response<Void>
}
