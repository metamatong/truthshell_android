package com.example.truthshellapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlin.jvm.Volatile

/**
 * Room database for storing saved TruthShell analysis results.
 */
@Database(
    entities = [SavedResultEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedResultDao(): SavedResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retrieve a singleton instance of AppDatabase.
         */
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "truthshell.db"
            ).build()
    }
}