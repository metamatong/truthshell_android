package com.example.truthshellapp.data.api

import com.example.truthshellapp.data.model.TruthRequest
import com.example.truthshellapp.data.model.TruthResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TruthShellApiService {
    /** JSON endpoint: send { "text": "…" } */
    @POST("/analyze")
    suspend fun analyzeText(
        @Header("X-API-KEY") apiKey: String,
        @Body request: TruthRequest
    ): Response<TruthResponse>

    /** Multipart endpoint: send form-data with mode + file */
    @Multipart
    @POST("/analyze/file")
    suspend fun analyzeFile(
        @Header("X-API-KEY") apiKey: String,
        @Part("mode") mode: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<TruthResponse>

}

