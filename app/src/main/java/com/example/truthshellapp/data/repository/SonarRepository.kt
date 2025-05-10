package com.example.truthshellapp.data.repository

import com.example.truthshellapp.data.api.PerplexityApiService
import com.example.truthshellapp.data.api.RetrofitClient
import com.example.truthshellapp.data.model.SonarRequest
import com.example.truthshellapp.data.model.SonarResponse
import retrofit2.Response

class SonarRepository(private val apiService: PerplexityApiService = RetrofitClient.instance) {

    // Function to call the API
    // The API key should ideally be stored securely (e.g., build config, keystore) and not hardcoded
    suspend fun checkClaim(apiKey: String, claimText: String): Response<SonarResponse> {
        val request = SonarRequest(claimText = claimText)
        // Prepend "Bearer " or the required prefix if the API expects it
        val authHeader = "Bearer $apiKey" // Adjust if needed
        return apiService.checkClaim(authHeader, request)
    }

    // You can add caching logic here if needed
}

