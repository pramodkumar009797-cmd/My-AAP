package com.example.data.repository

import com.example.data.local.AttendanceDao
import com.example.data.local.NoticeDao
import com.example.data.local.SeatDao
import com.example.data.local.SettingsDao
import com.example.data.model.AttendanceLog
import com.example.data.model.LibrarySettingsEntity
import com.example.data.model.NoticeEntity
import com.example.data.model.SeatEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LibraryRepository(
    private val seatDao: SeatDao,
    private val noticeDao: NoticeDao,
    private val attendanceDao: AttendanceDao,
    private val settingsDao: SettingsDao
) {
    val allSeats: Flow<List<SeatEntity>> = seatDao.getAllSeatsFlow()
    val allNotices: Flow<List<NoticeEntity>> = noticeDao.getAllNoticesFlow()
    val allAttendanceLogs: Flow<List<AttendanceLog>> = attendanceDao.getAllAttendanceFlow()
    val allAttendance: Flow<List<AttendanceLog>> get() = allAttendanceLogs
    val librarySettings: Flow<LibrarySettingsEntity?> = settingsDao.getSettingsFlow()

    suspend fun checkAndInitializeDefaults() {
        val seatCount = seatDao.getSeatsCount()
        if (seatCount < 100) {
            val existingSeats = mutableListOf<SeatEntity>()
            for (i in 1..100) {
                val existing = seatDao.getSeatByNumber(i)
                if (existing == null) {
                    existingSeats.add(SeatEntity(seatNumber = i))
                }
            }
            if (existingSeats.isNotEmpty()) {
                seatDao.insertAllSeats(existingSeats)
            }
        }

        val noticeCount = noticeDao.getNoticesCount()
        if (noticeCount == 0) {
            val defaultNotices = listOf(
                NoticeEntity(text = "Bhagat Singh Library Malkhera में आपका स्वागत है।"),
                NoticeEntity(text = "Monthly fee ₹400 है।")
            )
            noticeDao.insertAllNotices(defaultNotices)
        }

        val currentSettings = settingsDao.getSettings()
        if (currentSettings == null) {
            settingsDao.insertOrUpdateSettings(LibrarySettingsEntity())
        }
    }

    suspend fun saveSeat(seat: SeatEntity) {
        seatDao.insertOrUpdateSeat(seat)
    }

    suspend fun clearSeat(seatNumber: Int) {
        seatDao.clearSeat(seatNumber)
    }

    suspend fun resetAllSeats() {
        val emptySeats = (1..100).map { seatNum ->
            SeatEntity(seatNumber = seatNum)
        }
        seatDao.insertAllSeats(emptySeats)
    }

    suspend fun addNotice(text: String) {
        if (text.isNotBlank()) {
            noticeDao.insertNotice(NoticeEntity(text = text.trim()))
        }
    }

    suspend fun deleteNotice(notice: NoticeEntity) {
        noticeDao.deleteNotice(notice)
    }

    // Attendance Log Operations (Check-In & Check-Out Tracking via Room)
    fun getAttendanceForDate(date: String): Flow<List<AttendanceLog>> {
        return attendanceDao.getAttendanceByDateFlow(date)
    }

    fun getAttendanceLogsForDate(date: String): Flow<List<AttendanceLog>> {
        return attendanceDao.getAttendanceByDateFlow(date)
    }

    suspend fun checkInStudent(
        studentName: String,
        seatNumber: Int,
        mobileNumber: String = "",
        checkInTime: String = "",
        notes: String = "",
        customDate: String? = null
    ): Long {
        val dateStr = customDate ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val inTimeStr = if (checkInTime.isNotBlank()) checkInTime.trim() else SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val record = AttendanceLog(
            date = dateStr,
            seatNumber = seatNumber,
            studentName = studentName.trim(),
            mobileNumber = mobileNumber.trim(),
            inTime = inTimeStr,
            outTime = "",
            status = "Checked In",
            notes = notes.trim(),
            timestamp = System.currentTimeMillis()
        )
        return attendanceDao.insertAttendance(record)
    }

    suspend fun recordAttendance(
        seatNumber: Int,
        studentName: String,
        mobileNumber: String = "",
        inTime: String = "",
        outTime: String = "",
        status: String = "Present",
        customDate: String? = null
    ) {
        val dateStr = customDate ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val inTimeStr = if (inTime.isNotBlank()) inTime.trim() else SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val record = AttendanceLog(
            date = dateStr,
            seatNumber = seatNumber,
            studentName = studentName.trim(),
            mobileNumber = mobileNumber.trim(),
            inTime = inTimeStr,
            outTime = outTime.trim(),
            status = status,
            timestamp = System.currentTimeMillis()
        )
        attendanceDao.insertAttendance(record)
    }

    suspend fun punchCheckOut(logId: Long, outTime: String? = null) {
        val timeStr = if (!outTime.isNullOrBlank()) outTime else SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        attendanceDao.punchCheckOut(logId, timeStr)
    }

    suspend fun deleteAttendanceLog(log: AttendanceLog) {
        attendanceDao.deleteAttendance(log)
    }

    suspend fun deleteAttendance(record: AttendanceLog) {
        attendanceDao.deleteAttendance(record)
    }

    suspend fun clearAttendanceForDate(date: String) {
        attendanceDao.clearAttendanceForDate(date)
    }

    // Settings & Holder Controls
    suspend fun updateSettings(settings: LibrarySettingsEntity) {
        settingsDao.insertOrUpdateSettings(settings)
    }
}
