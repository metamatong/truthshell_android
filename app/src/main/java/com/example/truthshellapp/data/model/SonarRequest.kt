package com.example.truthshellapp.data.model

import com.google.gson.annotations.SerializedName

// Placeholder: Structure depends on actual Perplexity Sonar API
data class SonarRequest(
    @SerializedName("claim") // Assuming the API expects a field named "claim"
    val claimText: String
    // Add other parameters if required by the API (e.g., language, context)
)

