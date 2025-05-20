package com.example.truthshellapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a saved TruthShell analysis result.
 */
@Entity(tableName = "saved_results")
data class SavedResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalClaim: String,
    val confidenceScore: Int,
    val confidenceLabel: String,
    val sources: List<String>,
    val timestamp: Long
)