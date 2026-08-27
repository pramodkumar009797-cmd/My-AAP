package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SeatStatus {
    AVAILABLE,
    OCCUPIED,
    RESERVED
}

@Entity(tableName = "seats")
data class SeatEntity(
    @PrimaryKey val seatNumber: Int,
    val studentName: String = "",
    val mobileNumber: String = "",
    val inTime: String = "",
    val outTime: String = "",
    val feeStatus: String = "Pending", // "Paid", "Pending"
    val isReserved: Boolean = false,
    val updatedTimestamp: Long = System.currentTimeMillis()
) {
    val isOccupied: Boolean
        get() = studentName.trim().isNotEmpty() && outTime.trim().isEmpty()

    val status: SeatStatus
        get() = when {
            isReserved -> SeatStatus.RESERVED
            isOccupied -> SeatStatus.OCCUPIED
            else -> SeatStatus.AVAILABLE
        }
}
