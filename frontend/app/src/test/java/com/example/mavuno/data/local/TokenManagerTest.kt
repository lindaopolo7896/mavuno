package com.example.mavuno.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeTokenStorage {
    private val map = mutableMapOf<String, String>()

    fun saveTokens(accessToken: String, refreshToken: String) {
        map["access_token"] = accessToken
        map["refresh_token"] = refreshToken
    }

    fun getAccessToken(): String? = map["access_token"]

    fun getRefreshToken(): String? = map["refresh_token"]

    fun clearTokens() {
        map.remove("access_token")
        map.remove("refresh_token")
    }

    fun hasTokens(): Boolean = !getAccessToken().isNullOrEmpty()
}

class TokenManagerTest {

    private lateinit var tokenStorage: FakeTokenStorage

    @Before
    fun setUp() {
        tokenStorage = FakeTokenStorage()
    }

    @Test
    fun saveTokens_storesBothTokensCorrectly() {
        tokenStorage.saveTokens("access_123", "refresh_456")

        assertEquals("access_123", tokenStorage.getAccessToken())
        assertEquals("refresh_456", tokenStorage.getRefreshToken())
        assertTrue(tokenStorage.hasTokens())
    }

    @Test
    fun clearTokens_removesBothTokens() {
        tokenStorage.saveTokens("access_123", "refresh_456")
        tokenStorage.clearTokens()

        assertNull(tokenStorage.getAccessToken())
        assertNull(tokenStorage.getRefreshToken())
        assertFalse(tokenStorage.hasTokens())
    }
}
