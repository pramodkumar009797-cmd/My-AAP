package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * AttendanceLog entity for tracking daily student check-ins and check-outs.
 * Persisted locally using Room database.
 */
@Entity(tableName = "attendance_logs")
data class AttendanceLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // Format: YYYY-MM-DD (e.g. "2026-08-27")
    val studentName: String,
    val seatNumber: Int,
    val mobileNumber: String = "",
    val inTime: String = "", // Check-in time e.g. "08:30 AM"
    val outTime: String = "", // Check-out time e.g. "05:15 PM"
    val status: String = "Checked In", // "Checked In", "Checked Out", "Present"
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val checkInTime: String get() = inTime
    val checkOutTime: String get() = outTime

    val isCheckedOut: Boolean
        get() = outTime.isNotBlank() || status.equals("Checked Out", ignoreCase = true) || status.equals("Departed", ignoreCase = true)

    val isCurrentlyInside: Boolean
        get() = !isCheckedOut
}

typealias AttendanceRecordEntity = AttendanceLog
