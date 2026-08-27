import React, { useState } from 'react';
import {
  AttendanceLog,
  AttendanceFilter,
  SeatEntity,
  SeatStatus,
} from '../types';
import {
  formatDateForDisplay,
  getTodayDateString,
  isLogInside,
  getSeatStatus,
} from '../utils/storage';
import {
  ChevronLeft,
  ChevronRight,
  UserPlus,
  RefreshCw,
  Search,
  X,
  Phone,
  Clock,
  Trash2,
  LogOut,
  Calendar,
} from 'lucide-react';

interface AttendanceViewProps {
  selectedDate: string;
  attendanceLogs: AttendanceLog[];
  seats: SeatEntity[];
  onDateChange: (newDate: string) => void;
  onOpenCheckInDialog: () => void;
  onSyncOccupiedSeats: () => void;
  onPunchOut: (logId: number) => void;
  onDeleteLog: (log: AttendanceLog) => void;
}

export const AttendanceView: React.FC<AttendanceViewProps> = ({
  selectedDate,
  attendanceLogs,
  seats,
  onDateChange,
  onOpenCheckInDialog,
  onSyncOccupiedSeats,
  onPunchOut,
  onDeleteLog,
}) => {
  const [filter, setFilter] = useState<AttendanceFilter>(AttendanceFilter.ALL);
  const [searchQuery, setSearchQuery] = useState('');

  const todayStr = getTodayDateString();
  const isToday = selectedDate === todayStr;

  // Filter logs for this date
  const logsForDate = attendanceLogs.filter((log) => log.date === selectedDate);
  const insideCount = logsForDate.filter((log) => isLogInside(log)).length;
  const checkedOutCount = logsForDate.length - insideCount;

  // Count occupied seats that could be synced
  const occupiedSeats = seats.filter((s) => getSeatStatus(s) === SeatStatus.OCCUPIED);

  // Date shifting
  const handleShiftDate = (days: number) => {
    const [y, m, d] = selectedDate.split('-').map(Number);
    const date = new Date(y, m - 1, d);
    date.setDate(date.getDate() + days);
    const newY = date.getFullYear();
    const newM = String(date.getMonth() + 1).padStart(2, '0');
    const newD = String(date.getDate()).padStart(2, '0');
    onDateChange(`${newY}-${newM}-${newD}`);
  };

  // Filter & Search
  const filteredLogs = logsForDate.filter((log) => {
    const inside = isLogInside(log);
    const matchesFilter =
      filter === AttendanceFilter.ALL
        ? true
        : filter === AttendanceFilter.INSIDE
        ? inside
        : !inside;

    if (!matchesFilter) return false;

    if (!searchQuery.trim()) return true;
    const q = searchQuery.trim().toLowerCase();
    return (
      log.studentName.toLowerCase().includes(q) ||
      log.seatNumber.toString().includes(q) ||
      log.mobileNumber.includes(q) ||
      log.inTime.toLowerCase().includes(q) ||
      log.outTime.toLowerCase().includes(q)
    );
  });

  return (
    <div className="space-y-4 pb-12" data-testid="attendance_view">
      {/* 1. Date Selector Navigation Card */}
      <div className="bg-white rounded-3xl p-3.5 border border-slate-200/80 shadow-xs flex items-center justify-between">
        <button
          onClick={() => handleShiftDate(-1)}
          data-testid="attendance_prev_date_button"
          aria-label="Previous day"
          className="w-10 h-10 rounded-2xl bg-slate-100 hover:bg-indigo-50 hover:text-[#3F51B5] flex items-center justify-center transition-colors cursor-pointer"
        >
          <ChevronLeft className="w-5 h-5 text-slate-700" />
        </button>

        <div className="text-center flex-1 px-2">
          <div className="flex items-center justify-center gap-1.5 text-xs font-black uppercase text-[#3F51B5]">
            <Calendar className="w-3.5 h-3.5" />
            {isToday ? 'TODAY' : 'DATE'}
          </div>
          <div className="text-sm md:text-base font-black text-slate-800 tracking-tight">
            {formatDateForDisplay(selectedDate)}
          </div>
        </div>

        <div className="flex items-center gap-1.5">
          {!isToday && (
            <button
              onClick={() => onDateChange(todayStr)}
              className="px-2.5 py-1.5 rounded-xl bg-indigo-50 text-[#3F51B5] text-xs font-black hover:bg-indigo-100 transition-colors cursor-pointer"
            >
              Today
            </button>
          )}
          <button
            onClick={() => handleShiftDate(1)}
            data-testid="attendance_next_date_button"
            aria-label="Next day"
            className="w-10 h-10 rounded-2xl bg-slate-100 hover:bg-indigo-50 hover:text-[#3F51B5] flex items-center justify-center transition-colors cursor-pointer"
          >
            <ChevronRight className="w-5 h-5 text-slate-700" />
          </button>
        </div>
      </div>

      {/* 2. Hero Check-in Summary Card */}
      <div className="rounded-3xl bg-[#3F51B5] text-white p-5 shadow-md shadow-indigo-500/20 space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-xs font-bold text-white/80 tracking-wide uppercase">
              Daily Attendance Register
            </p>
            <h2 className="text-xl font-black text-white tracking-tight">
              {logsForDate.length} Students Logged
            </h2>
          </div>
          <div className="w-12 h-12 rounded-2xl bg-white/20 flex items-center justify-center text-2xl font-black text-white">
            {logsForDate.length < 10 ? `0${logsForDate.length}` : logsForDate.length}
          </div>
        </div>

        {/* 3 Metric Badges */}
        <div className="grid grid-cols-3 gap-2">
          <div className="bg-white/15 rounded-2xl p-2.5 text-center">
            <span className="text-[10px] font-bold text-emerald-200 block uppercase">
              Inside Library
            </span>
            <span className="text-lg font-black text-white">{insideCount}</span>
          </div>
          <div className="bg-white/15 rounded-2xl p-2.5 text-center">
            <span className="text-[10px] font-bold text-amber-200 block uppercase">
              Checked Out
            </span>
            <span className="text-lg font-black text-white">
              {checkedOutCount}
            </span>
          </div>
          <div className="bg-white/15 rounded-2xl p-2.5 text-center">
            <span className="text-[10px] font-bold text-indigo-200 block uppercase">
              Total Entries
            </span>
            <span className="text-lg font-black text-white">
              {logsForDate.length}
            </span>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center gap-2 pt-1">
          <button
            onClick={onOpenCheckInDialog}
            data-testid="attendance_check_in_button"
            className="flex-1 py-3 px-4 rounded-2xl bg-white text-[#3F51B5] font-black text-xs md:text-sm flex items-center justify-center gap-2 shadow-xs hover:bg-indigo-50 transition-all cursor-pointer"
          >
            <UserPlus className="w-4 h-4" />
            + Check-In Student
          </button>
          {occupiedSeats.length > 0 && isToday && (
            <button
              onClick={onSyncOccupiedSeats}
              data-testid="attendance_sync_seats_button"
              className="py-3 px-3.5 rounded-2xl bg-white/20 hover:bg-white/30 text-white font-bold text-xs flex items-center justify-center gap-1.5 transition-all cursor-pointer"
              title="Auto-log currently occupied seats"
            >
              <RefreshCw className="w-4 h-4" />
              Sync ({occupiedSeats.length})
            </button>
          )}
        </div>
      </div>

      {/* 3. Search & Filter Bar */}
      <div className="space-y-2.5">
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-[#3F51B5]">
            <Search className="w-5 h-5" />
          </div>
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search student, seat #, phone, or time..."
            data-testid="attendance_search_input"
            className="w-full pl-11 pr-10 py-3 bg-white border border-slate-200 rounded-2xl text-sm font-medium focus:outline-none focus:border-[#3F51B5] focus:ring-1 focus:ring-[#3F51B5] shadow-xs placeholder:text-slate-400"
          />
          {searchQuery && (
            <button
              onClick={() => setSearchQuery('')}
              className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-600"
            >
              <X className="w-4 h-4" />
            </button>
          )}
        </div>

        <div className="flex items-center gap-1.5">
          <button
            onClick={() => setFilter(AttendanceFilter.ALL)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              filter === AttendanceFilter.ALL
                ? 'bg-[#E8DEF8] text-[#311B92] font-black border border-[#D0BCFF]'
                : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
            }`}
          >
            All Logs ({logsForDate.length})
          </button>
          <button
            onClick={() => setFilter(AttendanceFilter.INSIDE)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              filter === AttendanceFilter.INSIDE
                ? 'bg-[#E7F7ED] text-[#166534] font-black border border-[#D1EBDD]'
                : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
            }`}
          >
            Inside ({insideCount})
          </button>
          <button
            onClick={() => setFilter(AttendanceFilter.CHECKED_OUT)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              filter === AttendanceFilter.CHECKED_OUT
                ? 'bg-[#FFF4E0] text-[#92400E] font-black border border-[#FFE7C2]'
                : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
            }`}
          >
            Checked Out ({checkedOutCount})
          </button>
        </div>
      </div>

      {/* 4. Logs List */}
      <div className="space-y-3">
        {filteredLogs.length === 0 ? (
          <div className="bg-white rounded-3xl p-8 border border-slate-200 text-center space-y-2">
            <p className="text-sm font-bold text-slate-700">
              No attendance records found for this date
            </p>
            <p className="text-xs text-slate-400">
              Tap '+ Check-In Student' or 'Sync Seats' to record student attendance.
            </p>
          </div>
        ) : (
          filteredLogs.map((log) => {
            const inside = isLogInside(log);
            const formattedSeat =
              log.seatNumber < 10 ? `0${log.seatNumber}` : `${log.seatNumber}`;

            return (
              <div
                key={log.id}
                data-testid={`attendance_item_${log.id}`}
                className="bg-white rounded-3xl p-4 border border-slate-200/80 shadow-xs space-y-3 hover:border-slate-300 transition-all"
              >
                <div className="flex items-start justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <div className="w-11 h-11 rounded-2xl bg-indigo-50 border border-indigo-100 flex items-center justify-center font-black text-sm text-[#3F51B5]">
                      {formattedSeat}
                    </div>
                    <div>
                      <h3 className="text-base font-black text-slate-800 leading-tight">
                        {log.studentName}
                      </h3>
                      {log.mobileNumber ? (
                        <a
                          href={`tel:${log.mobileNumber}`}
                          className="text-xs font-bold text-[#3F51B5] hover:underline flex items-center gap-1 mt-0.5"
                        >
                          <Phone className="w-3 h-3" />
                          {log.mobileNumber}
                        </a>
                      ) : (
                        <span className="text-xs text-slate-400 font-medium">
                          No phone number
                        </span>
                      )}
                    </div>
                  </div>

                  <div className="flex items-center gap-1.5">
                    <span
                      className={`px-2.5 py-1 rounded-full text-[10px] font-black uppercase tracking-wider ${
                        inside
                          ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                          : 'bg-slate-100 text-slate-600 border border-slate-200'
                      }`}
                    >
                      {inside ? 'INSIDE' : 'CHECKED OUT'}
                    </span>
                    <button
                      onClick={() => onDeleteLog(log)}
                      data-testid={`delete_attendance_${log.id}`}
                      className="w-8 h-8 rounded-xl flex items-center justify-center text-slate-400 hover:text-red-600 hover:bg-red-50 transition-colors cursor-pointer"
                      title="Delete record"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>

                {/* Timing Row */}
                <div className="flex items-center justify-between text-xs bg-slate-50/80 rounded-2xl p-2.5 border border-slate-100">
                  <div className="flex items-center gap-1.5 text-slate-600 font-bold">
                    <Clock className="w-3.5 h-3.5 text-slate-400" />
                    <span>In: {log.inTime || 'N/A'}</span>
                  </div>
                  <div className="flex items-center gap-1.5 text-slate-600 font-bold">
                    <span>Out: {log.outTime || '—'}</span>
                  </div>

                  {inside && (
                    <button
                      onClick={() => onPunchOut(log.id)}
                      data-testid={`punch_out_button_${log.id}`}
                      className="px-2.5 py-1 rounded-xl bg-amber-500 hover:bg-amber-600 text-white font-black text-[11px] flex items-center gap-1 shadow-xs transition-colors cursor-pointer"
                    >
                      <LogOut className="w-3 h-3" />
                      Punch Out
                    </button>
                  )}
                </div>

                {log.notes && (
                  <p className="text-xs text-slate-500 italic pl-1">
                    Note: {log.notes}
                  </p>
                )}
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};
