package com.example.truthshellapp.data.model

import com.google.gson.annotations.SerializedName

// Placeholder: Structure depends on actual Perplexity Sonar API
// Based on the mockups provided
data class TruthResponse(
    @SerializedName("confidence_score")
    val confidenceScore: Int?, // e.g., 98

    @SerializedName("confidence_label")
    val confidenceLabel: String?, // e.g., "Very Likely True"

    @SerializedName("sources")
    val sources: List<SourceItem>?,

    @SerializedName("error_message") // Optional: Field for API errors
    val errorMessage: String?
)

data class SourceItem(
    @SerializedName("url")
    val url: String,

    @SerializedName("title") // Optional: Title of the source
    val title: String?,

    @SerializedName("snippet") // Optional: Snippet from the source
    val snippet: String?
)

