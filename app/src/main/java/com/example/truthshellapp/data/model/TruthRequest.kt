package com.example.truthshellapp.data.model

import com.google.gson.annotations.SerializedName

// Placeholder: Structure depends on actual Perplexity Sonar API
data class TextPayload(
    @SerializedName("text")
    val text: String
)

data class TruthRequest(
    val text: String? = null,
    val image_base64: String? = null,
    val audio_base64: String? = null
)

