package com.example.truthshellapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Replace with the actual base URL of the Perplexity Sonar API
    private const val BASE_URL = "https://api.perplexity.ai/" // Placeholder URL

    val instance: PerplexityApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) 
            // Add OkHttpClient here if custom configuration (e.g., logging interceptor) is needed
            .build()

        retrofit.create(PerplexityApiService::class.java)
    }
}

