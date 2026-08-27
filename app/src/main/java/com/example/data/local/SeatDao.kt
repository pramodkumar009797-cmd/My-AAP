package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SeatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeatDao {
    @Query("SELECT * FROM seats ORDER BY seatNumber ASC")
    fun getAllSeatsFlow(): Flow<List<SeatEntity>>

    @Query("SELECT * FROM seats WHERE seatNumber = :seatNumber LIMIT 1")
    suspend fun getSeatByNumber(seatNumber: Int): SeatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSeat(seat: SeatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSeats(seats: List<SeatEntity>)

    @Update
    suspend fun updateSeat(seat: SeatEntity)

    @Query("SELECT COUNT(*) FROM seats")
    suspend fun getSeatsCount(): Int

    @Query("UPDATE seats SET studentName = '', mobileNumber = '', inTime = '', outTime = '', feeStatus = 'Pending', isReserved = 0 WHERE seatNumber = :seatNumber")
    suspend fun clearSeat(seatNumber: Int)
}
