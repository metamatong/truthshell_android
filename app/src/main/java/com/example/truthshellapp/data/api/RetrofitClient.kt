package com.example.truthshellapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Replace with the actual base URL of the Perplexity Sonar API
    private const val BASE_URL = "https://truthshell-server.vercel.app/"

    // create the interceptor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
    }

    // build an OkHttpClient that uses it
    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // build Retrofit with that client
    val instance: TruthShellApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TruthShellApiService::class.java)
    }
}

