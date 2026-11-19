package com.example.mavuno.data.remote

import android.content.Context
import com.example.mavuno.data.local.TokenManager
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://mavuno.ramscode.com/api/v1/"
    private var tokenManager: TokenManager? = null

    fun init(context: Context) {
        if (tokenManager == null) {
            tokenManager = TokenManager(context.applicationContext)
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Use BASIC level for development to avoid logging sensitive request/response bodies (passwords, tokens)
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = tokenManager?.getAccessToken()
        val request = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        chain.proceed(request)
    }

    private val tokenAuthenticator = Authenticator { _, response ->
        if (response.request.url.encodedPath.contains("auth/token/refresh/")) {
            return@Authenticator null
        }

        val refreshToken = tokenManager?.getRefreshToken()
        if (refreshToken.isNullOrBlank()) {
            tokenManager?.clearTokens()
            return@Authenticator null
        }

        synchronized(RetrofitClient) {
            val currentAccessToken = tokenManager?.getAccessToken()
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")
            if (!currentAccessToken.isNullOrBlank() && currentAccessToken != requestToken) {
                return@Authenticator response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(
                        OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .build()
                    )
                    .build()

                val service = retrofit.create(AuthApiService::class.java)
                val refreshResponse = service.refreshTokenSync(mapOf("refresh" to refreshToken)).execute()

                if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                    val body = refreshResponse.body()!!
                    val newAccess = body.access
                    val newRefresh = body.refresh ?: refreshToken
                    tokenManager?.saveTokens(newAccess, newRefresh)

                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccess")
                        .build()
                } else {
                    tokenManager?.clearTokens()
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}
