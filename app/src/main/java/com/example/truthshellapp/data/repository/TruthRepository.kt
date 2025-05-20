package com.example.truthshellapp.data.repository

import android.util.Log
import com.example.truthshellapp.data.api.RetrofitClient
import com.example.truthshellapp.data.api.TruthShellApiService
import com.example.truthshellapp.data.model.TextPayload
import com.example.truthshellapp.data.model.TruthRequest
import com.example.truthshellapp.data.model.TruthResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class TruthRepository(private val apiService: TruthShellApiService = RetrofitClient.instance) {

    /** Call the JSON /analyze endpoint */
    suspend fun analyzeText(apiKey: String, text: String): Response<TruthResponse> {
        val request = TruthRequest(text = text)
        val response = apiService.analyzeText(apiKey, request)

        if (!response.isSuccessful) {
            Log.e(
                "TruthShellAPI",
                "analyzeText failed – HTTP ${response.code()}: ${response.errorBody()?.string()}"
            )
        }

        return response
    }
    /** Call the multipart /analyze/file endpoint */
    suspend fun analyzeFile(
        apiKey: String,
        mode: String,
        filePart: MultipartBody.Part
        ): Response<TruthResponse> {
        val modeBody = mode.toRequestBody("text/plain".toMediaType())
        val response = apiService.analyzeFile(apiKey, modeBody, filePart)

        if (!response.isSuccessful) {
            Log.e(
                "TruthShellAPI",
                "analyzeFile failed – HTTP ${response.code()}: ${response.errorBody()?.string()}"
            )
        }

        return response
        }

    // You can add caching logic here if needed
}

