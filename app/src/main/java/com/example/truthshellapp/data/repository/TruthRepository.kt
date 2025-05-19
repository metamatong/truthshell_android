package com.example.truthshellapp.data.repository

import com.example.truthshellapp.data.api.RetrofitClient
import com.example.truthshellapp.data.api.TruthShellApiService
import com.example.truthshellapp.data.model.TruthRequest
import com.example.truthshellapp.data.model.TruthResponse
import retrofit2.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

class TruthRepository(private val apiService: TruthShellApiService = RetrofitClient.instance) {

    /** Call the JSON /analyze endpoint */
    suspend fun analyzeText(apiKey: String, text: String): Response<TruthResponse> {
        val request = TruthRequest(text = text)
        return apiService.analyzeText(apiKey, request)
    }
    /** Call the multipart /analyze/file endpoint */
    suspend fun analyzeFile(
        apiKey: String,
        mode: String,
        filePart: MultipartBody.Part
        ): Response<TruthResponse> {
        val modeBody = mode.toRequestBody("text/plain".toMediaType())
        return apiService.analyzeFile(apiKey, modeBody, filePart)
        }

    // You can add caching logic here if needed
}

