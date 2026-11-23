package com.example.mavuno.data.repository

import android.content.Context
import android.net.Uri
import com.example.mavuno.data.model.Scan
import com.example.mavuno.data.model.ScanPageResponse
import com.example.mavuno.data.remote.AuthApiService
import com.example.mavuno.data.remote.NetworkResult
import com.example.mavuno.data.remote.RetrofitClient
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Repository responsible for plant image uploads, disease prediction, history, and scan management via the Django backend.
 */
class ScanRepository(
    private val apiService: AuthApiService = RetrofitClient.apiService
) {

    /**
     * Uploads a leaf image and returns the AI plant prediction and saved scan object.
     */
    suspend fun predictScan(context: Context, imageUri: Uri, scannedAt: String? = null): NetworkResult<Scan> {
        return try {
            val file = uriToFile(context, imageUri)
            if (file == null || !file.exists()) {
                return NetworkResult.Error("Unable to process selected image file.")
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val scannedAtBody = scannedAt?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response: Response<Scan> = apiService.predictScan(body, scannedAtBody)
            if (response.isSuccessful) {
                val scan = response.body()
                if (scan != null) {
                    NetworkResult.Success(scan)
                } else {
                    NetworkResult.Error("Empty response body from server", response.code())
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseScanError(errorBody, response.code())
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    /**
     * Retrieves paginated scan history for the authenticated user.
     */
    suspend fun getScans(
        page: Int? = null,
        isHealthy: Boolean? = null,
        plant: String? = null,
        search: String? = null
    ): NetworkResult<ScanPageResponse> {
        return try {
            val response = apiService.getScans(page, isHealthy, plant, search)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Failed to load scan history", response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    /**
     * Retrieves a single scan by ID.
     */
    suspend fun getScanDetail(id: String): NetworkResult<Scan> {
        return try {
            val response = apiService.getScanDetail(id)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Scan not found", response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    /**
     * Deletes a scan by ID.
     */
    suspend fun deleteScan(id: String): NetworkResult<Unit> {
        return try {
            val response = apiService.deleteScan(id)
            if (response.isSuccessful || response.code() == 204) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("Failed to delete scan", response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("scan_", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    private fun parseScanError(errorJson: String?, statusCode: Int): String {
        if (errorJson.isNullOrBlank()) {
            return "Server returned an error (HTTP $statusCode)"
        }
        return try {
            val jsonElement = JsonParser.parseString(errorJson)
            if (!jsonElement.isJsonObject) {
                return "Server returned an error (HTTP $statusCode)"
            }
            val jsonObject = jsonElement.asJsonObject

            if (jsonObject.has("detail") && jsonObject.get("detail").isJsonPrimitive) {
                val detail = jsonObject.get("detail").asString
                if (detail.isNotBlank()) return detail
            }

            if (jsonObject.has("image") && jsonObject.get("image").isJsonArray) {
                val array = jsonObject.getAsJsonArray("image")
                val errors = mutableListOf<String>()
                for (i in 0 until array.size()) {
                    errors.add(array.get(i).asString)
                }
                if (errors.isNotEmpty()) {
                    return errors.joinToString(", ")
                }
            }

            "Server returned an error (HTTP $statusCode)"
        } catch (e: Exception) {
            "Server returned an error (HTTP $statusCode)"
        }
    }
}
