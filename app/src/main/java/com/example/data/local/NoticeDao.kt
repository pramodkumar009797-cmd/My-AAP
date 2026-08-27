package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.NoticeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notices ORDER BY id DESC")
    fun getAllNoticesFlow(): Flow<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: NoticeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotices(notices: List<NoticeEntity>)

    @Delete
    suspend fun deleteNotice(notice: NoticeEntity)

    @Query("SELECT COUNT(*) FROM notices")
    suspend fun getNoticesCount(): Int
}
