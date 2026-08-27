import React, { useState, useEffect } from 'react';
import {
  MainTab,
  SeatFilter,
  SeatStatus,
  SeatEntity,
  NoticeEntity,
  AttendanceLog,
  LibrarySettingsEntity,
  FolderType,
} from './types';
import {
  loadSeats,
  saveSeats,
  loadNotices,
  saveNotices,
  loadAttendanceLogs,
  saveAttendanceLogs,
  loadSettings,
  saveSettings,
  getTodayDateString,
  getCurrentTimeString,
  getSeatStatus,
} from './utils/storage';
import { Header } from './components/Header';
import { Navigation } from './components/Navigation';
import { DashboardView } from './components/DashboardView';
import { AttendanceView } from './components/AttendanceView';
import { HolderControlView } from './components/HolderControlView';
import { SettingsFoldersView } from './components/SettingsFoldersView';
import { SeatDetailsDialog } from './components/SeatDetailsDialog';
import { AddNoticeDialog } from './components/AddNoticeDialog';
import { CheckInDialog } from './components/CheckInDialog';
import { EditProfileDialog } from './components/EditProfileDialog';
import { ResetConfirmDialog } from './components/ResetConfirmDialog';
import { FolderDetailModal } from './components/FolderDetailModal';

export const App: React.FC = () => {
  // Navigation & View State
  const [currentTab, setCurrentTab] = useState<MainTab>(MainTab.DASHBOARD);
  const [seatFilter, setSeatFilter] = useState<SeatFilter>(SeatFilter.ALL);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedDate, setSelectedDate] = useState<string>(getTodayDateString());

  // App Data State
  const [seats, setSeats] = useState<SeatEntity[]>([]);
  const [notices, setNotices] = useState<NoticeEntity[]>([]);
  const [attendanceLogs, setAttendanceLogs] = useState<AttendanceLog[]>([]);
  const [settings, setSettings] = useState<LibrarySettingsEntity>(loadSettings());

  // Dialog & Modal State
  const [activeSeatForEdit, setActiveSeatForEdit] = useState<SeatEntity | null>(null);
  const [isAddNoticeOpen, setIsAddNoticeOpen] = useState(false);
  const [isCheckInOpen, setIsCheckInOpen] = useState(false);
  const [isEditProfileOpen, setIsEditProfileOpen] = useState(false);
  const [isResetConfirmOpen, setIsResetConfirmOpen] = useState(false);
  const [activeFolderDetail, setActiveFolderDetail] = useState<FolderType | null>(null);

  // Toast Notification
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  // Initial Data Load
  useEffect(() => {
    setSeats(loadSeats());
    setNotices(loadNotices());
    setAttendanceLogs(loadAttendanceLogs());
    setSettings(loadSettings());
  }, []);

  const occupiedCount = seats.filter((s) => getSeatStatus(s) === SeatStatus.OCCUPIED).length;

  // Seat Handlers
  const handleSaveSeat = (updatedSeat: SeatEntity, shouldLogAttendance: boolean) => {
    const newSeats = seats.map((s) =>
      s.seatNumber === updatedSeat.seatNumber ? updatedSeat : s
    );
    setSeats(newSeats);
    saveSeats(newSeats);

    // Auto-log to attendance if requested
    if (shouldLogAttendance && updatedSeat.studentName.trim().length > 0) {
      const today = getTodayDateString();
      const inTime = updatedSeat.inTime || getCurrentTimeString();

      // Check if student already logged today for this seat
      const existing = attendanceLogs.find(
        (l) =>
          l.date === today &&
          l.seatNumber === updatedSeat.seatNumber &&
          l.studentName.toLowerCase() === updatedSeat.studentName.trim().toLowerCase()
      );

      if (!existing) {
        const newLog: AttendanceLog = {
          id: Date.now(),
          date: today,
          seatNumber: updatedSeat.seatNumber,
          studentName: updatedSeat.studentName.trim(),
          mobileNumber: updatedSeat.mobileNumber.trim(),
          inTime: inTime,
          outTime: updatedSeat.outTime.trim(),
          status: updatedSeat.outTime.trim() ? 'Checked Out' : 'Checked In',
          notes: 'Auto-logged from Seat Map',
          timestamp: Date.now(),
        };
        const newLogs = [newLog, ...attendanceLogs];
        setAttendanceLogs(newLogs);
        saveAttendanceLogs(newLogs);
      }
    }

    setActiveSeatForEdit(null);
    showToast(`Seat ${updatedSeat.seatNumber < 10 ? '0' : ''}${updatedSeat.seatNumber} updated successfully`);
  };

  const handleClearSeat = (seatNumber: number) => {
    const newSeats = seats.map((s) =>
      s.seatNumber === seatNumber
        ? {
            seatNumber,
            studentName: '',
            mobileNumber: '',
            inTime: '',
            outTime: '',
            feeStatus: 'Pending',
            isReserved: false,
            updatedTimestamp: Date.now(),
          }
        : s
    );
    setSeats(newSeats);
    saveSeats(newSeats);
    setActiveSeatForEdit(null);
    showToast(`Seat ${seatNumber < 10 ? '0' : ''}${seatNumber} cleared`);
  };

  const handleResetAllSeats = () => {
    const resetSeats: SeatEntity[] = Array.from({ length: 100 }, (_, i) => ({
      seatNumber: i + 1,
      studentName: '',
      mobileNumber: '',
      inTime: '',
      outTime: '',
      feeStatus: 'Pending',
      isReserved: false,
      updatedTimestamp: Date.now(),
    }));
    setSeats(resetSeats);
    saveSeats(resetSeats);
    showToast('All 100 seats reset to available');
  };

  // Notice Handlers
  const handleAddNotice = (text: string) => {
    const newNotice: NoticeEntity = {
      id: Date.now(),
      text,
      timestamp: Date.now(),
    };
    const updated = [newNotice, ...notices];
    setNotices(updated);
    saveNotices(updated);
    showToast('Announcement posted');
  };

  const handleDeleteNotice = (notice: NoticeEntity) => {
    const updated = notices.filter((n) => n.id !== notice.id);
    setNotices(updated);
    saveNotices(updated);
    showToast('Announcement deleted');
  };

  // Attendance Handlers
  const handleCheckIn = (
    studentName: string,
    seatNumber: number,
    mobileNumber: string,
    inTime: string,
    notes: string
  ) => {
    const newLog: AttendanceLog = {
      id: Date.now(),
      date: selectedDate,
      seatNumber,
      studentName,
      mobileNumber,
      inTime: inTime || getCurrentTimeString(),
      outTime: '',
      status: 'Checked In',
      notes,
      timestamp: Date.now(),
    };
    const updatedLogs = [newLog, ...attendanceLogs];
    setAttendanceLogs(updatedLogs);
    saveAttendanceLogs(updatedLogs);

    // If date is today, also occupy the seat on live map
    if (selectedDate === getTodayDateString()) {
      const updatedSeats = seats.map((s) =>
        s.seatNumber === seatNumber
          ? {
              ...s,
              studentName,
              mobileNumber: mobileNumber || s.mobileNumber,
              inTime: inTime || getCurrentTimeString(),
              outTime: '',
              updatedTimestamp: Date.now(),
            }
          : s
      );
      setSeats(updatedSeats);
      saveSeats(updatedSeats);
    }

    showToast(`Checked in ${studentName} for Seat ${seatNumber}`);
  };

  const handleSyncOccupiedSeats = () => {
    const today = getTodayDateString();
    const occupied = seats.filter((s) => getSeatStatus(s) === SeatStatus.OCCUPIED);
    let count = 0;
    const currentLogs = [...attendanceLogs];

    occupied.forEach((seat) => {
      const alreadyPresent = currentLogs.some(
        (l) =>
          l.date === today &&
          l.seatNumber === seat.seatNumber &&
          l.studentName.toLowerCase() === seat.studentName.trim().toLowerCase()
      );
      if (!alreadyPresent) {
        currentLogs.unshift({
          id: Date.now() + Math.random() * 1000,
          date: today,
          seatNumber: seat.seatNumber,
          studentName: seat.studentName.trim(),
          mobileNumber: seat.mobileNumber.trim(),
          inTime: seat.inTime.trim() || getCurrentTimeString(),
          outTime: seat.outTime.trim(),
          status: seat.outTime.trim() ? 'Checked Out' : 'Checked In',
          notes: 'Synced from Active Seats',
          timestamp: Date.now(),
        });
        count++;
      }
    });

    if (count > 0) {
      setAttendanceLogs(currentLogs);
      saveAttendanceLogs(currentLogs);
      showToast(`Synced ${count} occupied students to today's register`);
    } else {
      showToast('All seated students are already synced');
    }
  };

  const handlePunchOut = (logId: number) => {
    const outTime = getCurrentTimeString();
    const targetLog = attendanceLogs.find((l) => l.id === logId);

    const updatedLogs = attendanceLogs.map((log) =>
      log.id === logId
        ? {
            ...log,
            outTime,
            status: 'Checked Out',
          }
        : log
    );
    setAttendanceLogs(updatedLogs);
    saveAttendanceLogs(updatedLogs);

    // If log was for today, update the seat's outTime
    if (targetLog && targetLog.date === getTodayDateString()) {
      const updatedSeats = seats.map((s) =>
        s.seatNumber === targetLog.seatNumber &&
        s.studentName.toLowerCase() === targetLog.studentName.toLowerCase()
          ? {
              ...s,
              outTime,
              updatedTimestamp: Date.now(),
            }
          : s
      );
      setSeats(updatedSeats);
      saveSeats(updatedSeats);
    }

    showToast('Student punched out');
  };

  const handleDeleteLog = (log: AttendanceLog) => {
    const updated = attendanceLogs.filter((l) => l.id !== log.id);
    setAttendanceLogs(updated);
    saveAttendanceLogs(updated);
    showToast('Attendance record deleted');
  };

  // Fee Status Toggle
  const handleToggleFeeStatus = (seatNumber: number) => {
    const newSeats = seats.map((s) => {
      if (s.seatNumber === seatNumber) {
        const nextStatus = s.feeStatus.toLowerCase() === 'paid' ? 'Pending' : 'Paid';
        return {
          ...s,
          feeStatus: nextStatus,
          updatedTimestamp: Date.now(),
        };
      }
      return s;
    });
    setSeats(newSeats);
    saveSeats(newSeats);
    showToast(`Seat ${seatNumber} fee status updated`);
  };

  // Settings Handlers
  const handleSaveSettings = (newSettings: LibrarySettingsEntity) => {
    setSettings(newSettings);
    saveSettings(newSettings);
    showToast('Library settings saved');
  };

  // Share / Export Report
  const handleShareReport = () => {
    const occupied = seats.filter((s) => getSeatStatus(s) === SeatStatus.OCCUPIED);
    const reserved = seats.filter((s) => s.isReserved);
    const available = 100 - occupied.length - reserved.length;
    const paid = occupied.filter((s) => s.feeStatus.toLowerCase() === 'paid');
    const pending = occupied.filter((s) => s.feeStatus.toLowerCase() !== 'paid');

    const reportText = `*📚 ${settings.libraryName.toUpperCase()} (${settings.branchName.toUpperCase()})*
📅 Date: ${new Date().toLocaleDateString('en-US', { day: 'numeric', month: 'short', year: 'numeric' })}
⏰ Hours: ${settings.openingHours}

*💺 SEAT STATUS SUMMARY*
• Total Capacity: 100
• Seated / Occupied: ${occupied.length}
• Free Seats: ${available}
• Reserved Seats: ${reserved.length}

*💰 MONTHLY FEES (₹${settings.monthlyFee}/mo)*
• Total Collected: ₹${paid.length * settings.monthlyFee} (${paid.length} Students)
• Pending Dues: ₹${pending.length * settings.monthlyFee} (${pending.length} Students)

*👤 CONTACT & LOCATION*
• Owner: ${settings.ownerName} (${settings.ownerPhone})
• Wi-Fi: ${settings.wifiPassword}
• Address: ${settings.address}`;

    if (navigator.clipboard) {
      navigator.clipboard.writeText(reportText);
      showToast('📋 Library Report copied to clipboard!');
    }
  };

  return (
    <div className="min-h-screen bg-[#FBFBFF] text-slate-800 pb-20 selection:bg-indigo-500 selection:text-white">
      {/* Toast Notification */}
      {toastMessage && (
        <div className="fixed top-16 left-1/2 -translate-x-1/2 z-50 bg-slate-900 text-white px-4 py-2 rounded-2xl text-xs font-bold shadow-xl border border-slate-700 animate-fade-in flex items-center gap-2">
          <span>{toastMessage}</span>
        </div>
      )}

      {/* Header */}
      <Header
        currentTab={currentTab}
        settings={settings}
        occupiedCount={occupiedCount}
        onOpenAddNotice={() => setIsAddNoticeOpen(true)}
      />

      {/* Main Container */}
      <main className="max-w-3xl mx-auto px-4 pt-4">
        {currentTab === MainTab.DASHBOARD && (
          <DashboardView
            seats={seats}
            notices={notices}
            settings={settings}
            filter={seatFilter}
            searchQuery={searchQuery}
            onFilterChange={setSeatFilter}
            onSearchChange={setSearchQuery}
            onOpenSeat={(seat) => setActiveSeatForEdit(seat)}
            onOpenAddNotice={() => setIsAddNoticeOpen(true)}
            onDeleteNotice={handleDeleteNotice}
          />
        )}

        {currentTab === MainTab.ATTENDANCE && (
          <AttendanceView
            selectedDate={selectedDate}
            attendanceLogs={attendanceLogs}
            seats={seats}
            onDateChange={setSelectedDate}
            onOpenCheckInDialog={() => setIsCheckInOpen(true)}
            onSyncOccupiedSeats={handleSyncOccupiedSeats}
            onPunchOut={handlePunchOut}
            onDeleteLog={handleDeleteLog}
          />
        )}

        {currentTab === MainTab.HOLDER && (
          <HolderControlView
            seats={seats}
            settings={settings}
            onOpenEditProfile={() => setIsEditProfileOpen(true)}
            onOpenAddNotice={() => setIsAddNoticeOpen(true)}
            onShareReport={handleShareReport}
            onToggleFeeStatus={handleToggleFeeStatus}
            onOpenResetConfirm={() => setIsResetConfirmOpen(true)}
          />
        )}

        {currentTab === MainTab.SETTINGS && (
          <SettingsFoldersView
            settings={settings}
            onSelectTab={setCurrentTab}
            onOpenFolderDetail={(type) => setActiveFolderDetail(type)}
          />
        )}
      </main>

      {/* Bottom Navigation */}
      <Navigation currentTab={currentTab} onSelectTab={setCurrentTab} />

      {/* Modals & Dialogs */}
      <SeatDetailsDialog
        seat={activeSeatForEdit}
        isOpen={Boolean(activeSeatForEdit)}
        monthlyFee={settings.monthlyFee}
        onClose={() => setActiveSeatForEdit(null)}
        onSave={handleSaveSeat}
        onClear={handleClearSeat}
      />

      <AddNoticeDialog
        isOpen={isAddNoticeOpen}
        onClose={() => setIsAddNoticeOpen(false)}
        onAddNotice={handleAddNotice}
      />

      <CheckInDialog
        isOpen={isCheckInOpen}
        totalSeats={seats.length || 100}
        onClose={() => setIsCheckInOpen(false)}
        onCheckIn={handleCheckIn}
      />

      <EditProfileDialog
        isOpen={isEditProfileOpen}
        settings={settings}
        onClose={() => setIsEditProfileOpen(false)}
        onSave={handleSaveSettings}
      />

      <ResetConfirmDialog
        isOpen={isResetConfirmOpen}
        onClose={() => setIsResetConfirmOpen(false)}
        onConfirm={handleResetAllSeats}
      />

      <FolderDetailModal
        type={activeFolderDetail}
        settings={settings}
        seats={seats}
        attendanceLogs={attendanceLogs}
        onClose={() => setActiveFolderDetail(null)}
        onShareReport={handleShareReport}
      />
    </div>
  );
};

export default App;
