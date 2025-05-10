package com.example.truthshellapp.data.api

import com.example.truthshellapp.data.model.SonarRequest
import com.example.truthshellapp.data.model.SonarResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PerplexityApiService {

    // Define the endpoint for the Sonar API
    // Replace "sonar_endpoint" with the actual path
    // The exact structure depends on the Perplexity Sonar API documentation
    @POST("sonar_endpoint") 
    suspend fun checkClaim(
        @Header("Authorization") apiKey: String, // Placeholder for API Key header
        @Body request: SonarRequest
    ): Response<SonarResponse> 

    // Add other API methods if needed
}

