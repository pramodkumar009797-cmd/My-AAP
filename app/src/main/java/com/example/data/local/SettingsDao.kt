package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.LibrarySettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM library_settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<LibrarySettingsEntity?>

    @Query("SELECT * FROM library_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): LibrarySettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: LibrarySettingsEntity)
}
