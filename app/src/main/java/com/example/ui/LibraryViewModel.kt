package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LibraryDatabase
import com.example.data.model.AttendanceLog
import com.example.data.model.AttendanceRecordEntity
import com.example.data.model.LibrarySettingsEntity
import com.example.data.model.NoticeEntity
import com.example.data.model.SeatEntity
import com.example.data.model.SeatStatus
import com.example.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class MainTab {
    DASHBOARD,
    ATTENDANCE,
    HOLDER,
    SETTINGS
}

enum class SeatFilter {
    ALL,
    AVAILABLE,
    OCCUPIED,
    RESERVED
}

enum class AttendanceFilter {
    ALL,
    INSIDE,
    CHECKED_OUT
}

data class LibraryUiState(
    val currentTab: MainTab = MainTab.DASHBOARD,
    val seats: List<SeatEntity> = emptyList(),
    val notices: List<NoticeEntity> = emptyList(),
    val attendanceRecords: List<AttendanceRecordEntity> = emptyList(),
    val attendanceLogs: List<AttendanceLog> = emptyList(),
    val settings: LibrarySettingsEntity = LibrarySettingsEntity(),
    val selectedAttendanceDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val searchQuery: String = "",
    val filter: SeatFilter = SeatFilter.ALL,
    val attendanceSearchQuery: String = "",
    val attendanceFilter: AttendanceFilter = AttendanceFilter.ALL,
    val selectedSeat: SeatEntity? = null,
    val showSeatDialog: Boolean = false,
    val showAddNoticeDialog: Boolean = false,
    val showMarkAttendanceDialog: Boolean = false,
    val showResetConfirmation: Boolean = false,
    val isRefreshing: Boolean = false,
    val snackbarMessage: String? = null
) {
    val totalSeats: Int get() = if (seats.isNotEmpty()) seats.size else settings.totalCapacity
    val occupiedCount: Int get() = seats.count { it.isOccupied }
    val reservedCount: Int get() = seats.count { it.isReserved }
    val availableCount: Int get() = totalSeats - occupiedCount - reservedCount

    val monthlyFee: Int get() = settings.monthlyFee

    // Financial totals
    val totalStudents: Int get() = seats.count { it.studentName.trim().isNotEmpty() }
    val paidStudentsCount: Int get() = seats.count { it.studentName.trim().isNotEmpty() && it.feeStatus.equals("Paid", ignoreCase = true) }
    val pendingStudentsCount: Int get() = seats.count { it.studentName.trim().isNotEmpty() && !it.feeStatus.equals("Paid", ignoreCase = true) }
    val totalRevenueCollected: Int get() = paidStudentsCount * monthlyFee
    val totalPendingRevenue: Int get() = pendingStudentsCount * monthlyFee

    // Daily Attendance Logs for selected date
    val todayAttendanceLogs: List<AttendanceLog>
        get() = attendanceLogs.filter { it.date == selectedAttendanceDate }

    val totalAttendanceCount: Int get() = todayAttendanceLogs.size
    val currentlyInsideCount: Int get() = todayAttendanceLogs.count { it.isCurrentlyInside }
    val checkedOutCount: Int get() = todayAttendanceLogs.count { it.isCheckedOut }

    val filteredAttendanceLogs: List<AttendanceLog>
        get() {
            return todayAttendanceLogs.filter { log ->
                val matchesFilter = when (attendanceFilter) {
                    AttendanceFilter.ALL -> true
                    AttendanceFilter.INSIDE -> log.isCurrentlyInside
                    AttendanceFilter.CHECKED_OUT -> log.isCheckedOut
                }

                val matchesSearch = if (attendanceSearchQuery.isBlank()) {
                    true
                } else {
                    val query = attendanceSearchQuery.trim().lowercase()
                    log.studentName.lowercase().contains(query) ||
                            log.seatNumber.toString().contains(query) ||
                            log.mobileNumber.contains(query) ||
                            log.checkInTime.lowercase().contains(query) ||
                            log.checkOutTime.lowercase().contains(query)
                }

                matchesFilter && matchesSearch
            }
        }

    val filteredSeats: List<SeatEntity>
        get() {
            return seats.filter { seat ->
                val matchesFilter = when (filter) {
                    SeatFilter.ALL -> true
                    SeatFilter.AVAILABLE -> seat.status == SeatStatus.AVAILABLE
                    SeatFilter.OCCUPIED -> seat.status == SeatStatus.OCCUPIED
                    SeatFilter.RESERVED -> seat.status == SeatStatus.RESERVED
                }

                val matchesSearch = if (searchQuery.isBlank()) {
                    true
                } else {
                    val query = searchQuery.trim().lowercase()
                    seat.seatNumber.toString().contains(query) ||
                            seat.studentName.lowercase().contains(query) ||
                            seat.mobileNumber.contains(query)
                }

                matchesFilter && matchesSearch
            }
        }
}

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LibraryRepository

    private val _currentTab = MutableStateFlow(MainTab.DASHBOARD)
    private val _selectedAttendanceDate = MutableStateFlow(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    private val _searchQuery = MutableStateFlow("")
    private val _filter = MutableStateFlow(SeatFilter.ALL)
    private val _attendanceSearchQuery = MutableStateFlow("")
    private val _attendanceFilter = MutableStateFlow(AttendanceFilter.ALL)

    private val _selectedSeat = MutableStateFlow<SeatEntity?>(null)
    private val _showSeatDialog = MutableStateFlow(false)
    private val _showAddNoticeDialog = MutableStateFlow(false)
    private val _showMarkAttendanceDialog = MutableStateFlow(false)
    private val _showResetConfirmation = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _snackbarMessage = MutableStateFlow<String?>(null)

    init {
        val db = LibraryDatabase.getDatabase(application)
        repository = LibraryRepository(
            db.seatDao(),
            db.noticeDao(),
            db.attendanceDao(),
            db.settingsDao()
        )
        viewModelScope.launch {
            repository.checkAndInitializeDefaults()
        }
    }

    private data class DialogState(
        val selectedSeat: SeatEntity? = null,
        val showSeatDialog: Boolean = false,
        val showAddNoticeDialog: Boolean = false,
        val showMarkAttendanceDialog: Boolean = false,
        val showResetConfirmation: Boolean = false,
        val isRefreshing: Boolean = false,
        val snackbarMessage: String? = null
    )

    private val _dialogState = combine(
        _selectedSeat,
        _showSeatDialog,
        _showAddNoticeDialog,
        _showMarkAttendanceDialog,
        _showResetConfirmation,
        _isRefreshing,
        _snackbarMessage
    ) { params: Array<Any?> ->
        DialogState(
            selectedSeat = params[0] as? SeatEntity,
            showSeatDialog = params[1] as Boolean,
            showAddNoticeDialog = params[2] as Boolean,
            showMarkAttendanceDialog = params[3] as Boolean,
            showResetConfirmation = params[4] as Boolean,
            isRefreshing = params[5] as Boolean,
            snackbarMessage = params[6] as? String
        )
    }

    val uiState: StateFlow<LibraryUiState> = combine(
        repository.allSeats,
        repository.allNotices,
        repository.allAttendance,
        repository.allAttendanceLogs,
        repository.librarySettings,
        _currentTab,
        _selectedAttendanceDate,
        _searchQuery,
        _filter,
        _attendanceSearchQuery,
        _attendanceFilter,
        _dialogState
    ) { args: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val seats = args[0] as? List<SeatEntity> ?: emptyList()
        @Suppress("UNCHECKED_CAST")
        val notices = args[1] as? List<NoticeEntity> ?: emptyList()
        @Suppress("UNCHECKED_CAST")
        val attendance = args[2] as? List<AttendanceRecordEntity> ?: emptyList()
        @Suppress("UNCHECKED_CAST")
        val attendanceLogs = args[3] as? List<AttendanceLog> ?: emptyList()
        val settings = args[4] as? LibrarySettingsEntity ?: LibrarySettingsEntity()
        val tab = args[5] as MainTab
        val date = args[6] as String
        val query = args[7] as String
        val filter = args[8] as SeatFilter
        val attQuery = args[9] as String
        val attFilter = args[10] as AttendanceFilter
        val dialogState = args[11] as DialogState

        LibraryUiState(
            currentTab = tab,
            seats = seats,
            notices = notices,
            attendanceRecords = attendance,
            attendanceLogs = attendanceLogs,
            settings = settings,
            selectedAttendanceDate = date,
            searchQuery = query,
            filter = filter,
            attendanceSearchQuery = attQuery,
            attendanceFilter = attFilter,
            selectedSeat = dialogState.selectedSeat,
            showSeatDialog = dialogState.showSeatDialog,
            showAddNoticeDialog = dialogState.showAddNoticeDialog,
            showMarkAttendanceDialog = dialogState.showMarkAttendanceDialog,
            showResetConfirmation = dialogState.showResetConfirmation,
            isRefreshing = dialogState.isRefreshing,
            snackbarMessage = dialogState.snackbarMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LibraryUiState()
    )

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChanged(filter: SeatFilter) {
        _filter.value = filter
    }

    fun onAttendanceSearchChanged(query: String) {
        _attendanceSearchQuery.value = query
    }

    fun onAttendanceFilterChanged(filter: AttendanceFilter) {
        _attendanceFilter.value = filter
    }

    fun openSeat(seat: SeatEntity) {
        _selectedSeat.value = seat
        _showSeatDialog.value = true
    }

    fun closeSeatDialog() {
        _showSeatDialog.value = false
        _selectedSeat.value = null
    }

    fun saveSeat(
        seatNumber: Int,
        studentName: String,
        mobileNumber: String,
        inTime: String,
        outTime: String,
        feeStatus: String,
        isReserved: Boolean
    ) {
        viewModelScope.launch {
            val updated = SeatEntity(
                seatNumber = seatNumber,
                studentName = studentName.trim(),
                mobileNumber = mobileNumber.trim(),
                inTime = inTime.trim(),
                outTime = outTime.trim(),
                feeStatus = feeStatus,
                isReserved = isReserved,
                updatedTimestamp = System.currentTimeMillis()
            )
            repository.saveSeat(updated)

            // If student checked in with an inTime and studentName, auto log attendance for today
            if (studentName.isNotBlank() && inTime.isNotBlank()) {
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                repository.checkInStudent(
                    studentName = studentName.trim(),
                    seatNumber = seatNumber,
                    mobileNumber = mobileNumber.trim(),
                    checkInTime = inTime.trim(),
                    notes = "Checked in from Seat Map",
                    customDate = todayStr
                )
            }

            closeSeatDialog()
        }
    }

    fun clearSeat(seatNumber: Int) {
        viewModelScope.launch {
            repository.clearSeat(seatNumber)
            closeSeatDialog()
        }
    }

    fun openAddNoticeDialog() {
        _showAddNoticeDialog.value = true
    }

    fun closeAddNoticeDialog() {
        _showAddNoticeDialog.value = false
    }

    fun addNotice(text: String) {
        if (text.isNotBlank()) {
            viewModelScope.launch {
                repository.addNotice(text.trim())
                closeAddNoticeDialog()
            }
        }
    }

    fun deleteNotice(notice: NoticeEntity) {
        viewModelScope.launch {
            repository.deleteNotice(notice)
        }
    }

    // Attendance Methods
    fun setSelectedAttendanceDate(date: String) {
        _selectedAttendanceDate.value = date
    }

    fun openMarkAttendanceDialog() {
        _showMarkAttendanceDialog.value = true
    }

    fun closeMarkAttendanceDialog() {
        _showMarkAttendanceDialog.value = false
    }

    fun checkInStudent(
        seatNumber: Int,
        studentName: String,
        mobileNumber: String,
        checkInTime: String,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val timeStr = if (checkInTime.isNotBlank()) checkInTime else SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            repository.checkInStudent(
                studentName = studentName.trim(),
                seatNumber = seatNumber,
                mobileNumber = mobileNumber.trim(),
                checkInTime = timeStr,
                notes = notes.trim(),
                customDate = _selectedAttendanceDate.value
            )
            closeMarkAttendanceDialog()
            _snackbarMessage.value = "Checked-in: $studentName (Seat $seatNumber)"
        }
    }

    fun punchCheckOut(logId: Long, studentName: String, checkOutTime: String? = null) {
        viewModelScope.launch {
            val timeStr = if (!checkOutTime.isNullOrBlank()) checkOutTime else SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            repository.punchCheckOut(logId, timeStr)
            _snackbarMessage.value = "$studentName checked-out at $timeStr"
        }
    }

    fun deleteAttendanceLog(log: AttendanceLog) {
        viewModelScope.launch {
            repository.deleteAttendanceLog(log)
            _snackbarMessage.value = "Attendance record deleted for ${log.studentName}"
        }
    }

    fun syncCheckInsFromOccupiedSeats() {
        viewModelScope.launch {
            val occupiedSeats = uiState.value.seats.filter { it.isOccupied }
            val selectedDate = _selectedAttendanceDate.value
            val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

            var count = 0
            val currentDayLogs = uiState.value.attendanceLogs.filter { it.date == selectedDate }
            for (seat in occupiedSeats) {
                val alreadyLogged = currentDayLogs.any {
                    it.seatNumber == seat.seatNumber && it.studentName.equals(seat.studentName, ignoreCase = true)
                }
                if (!alreadyLogged) {
                    repository.checkInStudent(
                        studentName = seat.studentName,
                        seatNumber = seat.seatNumber,
                        mobileNumber = seat.mobileNumber,
                        checkInTime = if (seat.inTime.isNotBlank()) seat.inTime else timeNow,
                        notes = "Auto-synced from live seat map",
                        customDate = selectedDate
                    )
                    count++
                }
            }
            if (count > 0) {
                _snackbarMessage.value = "$count seated student(s) checked-in to attendance register"
            } else {
                _snackbarMessage.value = "All currently seated students are already checked-in"
            }
        }
    }

    // Library Holder & Admin Settings
    fun updateLibrarySettings(
        libraryName: String,
        branchName: String,
        ownerName: String,
        ownerPhone: String,
        monthlyFee: Int,
        openingHours: String,
        wifiPassword: String,
        pinProtectionEnabled: Boolean,
        ownerPin: String,
        address: String
    ) {
        viewModelScope.launch {
            val updated = LibrarySettingsEntity(
                id = 1,
                libraryName = libraryName.trim(),
                branchName = branchName.trim(),
                ownerName = ownerName.trim(),
                ownerPhone = ownerPhone.trim(),
                monthlyFee = monthlyFee,
                totalCapacity = 100,
                openingHours = openingHours.trim(),
                wifiPassword = wifiPassword.trim(),
                pinProtectionEnabled = pinProtectionEnabled,
                ownerPin = ownerPin.trim(),
                address = address.trim()
            )
            repository.updateSettings(updated)
            _snackbarMessage.value = "Library settings & owner controls updated successfully!"
        }
    }

    fun openResetConfirmation() {
        _showResetConfirmation.value = true
    }

    fun closeResetConfirmation() {
        _showResetConfirmation.value = false
    }

    fun resetAllLibrarySeats() {
        viewModelScope.launch {
            repository.resetAllSeats()
            closeResetConfirmation()
            _snackbarMessage.value = "All 100 seats have been cleared & reset."
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.checkAndInitializeDefaults()
            _isRefreshing.value = false
        }
    }
}
