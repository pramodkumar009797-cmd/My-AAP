package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AttendanceLog
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_logs ORDER BY timestamp DESC")
    fun getAllAttendanceFlow(): Flow<List<AttendanceLog>>

    @Query("SELECT * FROM attendance_logs WHERE date = :date ORDER BY seatNumber ASC")
    fun getAttendanceByDateFlow(date: String): Flow<List<AttendanceLog>>

    @Query("SELECT * FROM attendance_logs WHERE date = :date ORDER BY seatNumber ASC")
    suspend fun getAttendanceByDate(date: String): List<AttendanceLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<AttendanceLog>)

    @Update
    suspend fun updateAttendance(record: AttendanceLog)

    @Query("UPDATE attendance_logs SET outTime = :outTime, status = 'Checked Out' WHERE id = :id")
    suspend fun punchCheckOut(id: Long, outTime: String)

    @Delete
    suspend fun deleteAttendance(record: AttendanceLog)

    @Query("DELETE FROM attendance_logs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM attendance_logs WHERE date = :date")
    suspend fun clearAttendanceForDate(date: String)

    @Query("DELETE FROM attendance_logs")
    suspend fun clearAllAttendance()
}
