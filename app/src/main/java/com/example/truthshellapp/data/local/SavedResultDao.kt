package com.example.truthshellapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * DAO for managing saved TruthShell analysis results.
 */
@Dao
interface SavedResultDao {
    /**
     * Insert a new saved result into the database.
     */
    @Insert
    suspend fun insert(result: SavedResultEntity)

    /**
     * Retrieve all saved results ordered by timestamp descending.
     */
    @Query("SELECT * FROM saved_results ORDER BY timestamp DESC")
    suspend fun getAll(): List<SavedResultEntity>
}