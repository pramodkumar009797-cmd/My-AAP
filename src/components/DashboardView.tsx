import React from 'react';
import {
  SeatEntity,
  NoticeEntity,
  SeatFilter,
  SeatStatus,
  LibrarySettingsEntity,
} from '../types';
import { getSeatStatus, formatTimestamp } from '../utils/storage';
import {
  IndianRupee,
  Search,
  X,
  Bookmark,
  Megaphone,
  Trash2,
  Plus,
} from 'lucide-react';

interface DashboardViewProps {
  seats: SeatEntity[];
  notices: NoticeEntity[];
  settings: LibrarySettingsEntity;
  filter: SeatFilter;
  searchQuery: string;
  onFilterChange: (filter: SeatFilter) => void;
  onSearchChange: (query: string) => void;
  onOpenSeat: (seat: SeatEntity) => void;
  onOpenAddNotice: () => void;
  onDeleteNotice: (notice: NoticeEntity) => void;
}

export const DashboardView: React.FC<DashboardViewProps> = ({
  seats,
  notices,
  settings,
  filter,
  searchQuery,
  onFilterChange,
  onSearchChange,
  onOpenSeat,
  onOpenAddNotice,
  onDeleteNotice,
}) => {
  const totalSeats = seats.length > 0 ? seats.length : settings.totalCapacity;
  const occupiedCount = seats.filter((s) => getSeatStatus(s) === SeatStatus.OCCUPIED).length;
  const reservedCount = seats.filter((s) => s.isReserved).length;
  const availableCount = totalSeats - occupiedCount - reservedCount;

  // Filter & Search logic
  const filteredSeats = seats.filter((seat) => {
    const status = getSeatStatus(seat);
    const matchesFilter =
      filter === SeatFilter.ALL
        ? true
        : filter === SeatFilter.AVAILABLE
        ? status === SeatStatus.AVAILABLE
        : filter === SeatFilter.OCCUPIED
        ? status === SeatStatus.OCCUPIED
        : filter === SeatFilter.RESERVED
        ? status === SeatStatus.RESERVED
        : true;

    if (!matchesFilter) return false;

    if (!searchQuery.trim()) return true;
    const q = searchQuery.trim().toLowerCase();
    return (
      seat.seatNumber.toString().includes(q) ||
      seat.studentName.toLowerCase().includes(q) ||
      seat.mobileNumber.includes(q)
    );
  });

  return (
    <div className="space-y-4 pb-12" data-testid="dashboard_scroll_view">
      {/* 1. 4-Column Stat Cards */}
      <div className="grid grid-cols-4 gap-2">
        {/* TOTAL */}
        <button
          type="button"
          onClick={() => onFilterChange(SeatFilter.ALL)}
          data-testid="stat_card_total"
          className={`p-3 rounded-2xl flex flex-col items-center justify-center transition-all cursor-pointer bg-[#E8DEF8] border ${
            filter === SeatFilter.ALL
              ? 'border-2 border-[#311B92] shadow-sm scale-[1.02]'
              : 'border-[#D0BCFF] hover:border-[#311B92]/60'
          }`}
        >
          <span className="text-[10px] font-black tracking-wider text-[#311B92]/80 uppercase">
            TOTAL
          </span>
          <span className="text-xl font-black text-[#311B92] leading-tight mt-0.5">
            {totalSeats < 10 && totalSeats >= 0 ? `0${totalSeats}` : totalSeats}
          </span>
        </button>

        {/* OCCUPIED */}
        <button
          type="button"
          onClick={() =>
            onFilterChange(
              filter === SeatFilter.OCCUPIED ? SeatFilter.ALL : SeatFilter.OCCUPIED
            )
          }
          data-testid="stat_card_occ"
          className={`p-3 rounded-2xl flex flex-col items-center justify-center transition-all cursor-pointer bg-[#FDE7E7] border ${
            filter === SeatFilter.OCCUPIED
              ? 'border-2 border-[#991B1B] shadow-sm scale-[1.02]'
              : 'border-[#F9D2D2] hover:border-[#991B1B]/60'
          }`}
        >
          <span className="text-[10px] font-black tracking-wider text-[#991B1B]/80 uppercase">
            OCC
          </span>
          <span className="text-xl font-black text-[#991B1B] leading-tight mt-0.5">
            {occupiedCount < 10 && occupiedCount >= 0
              ? `0${occupiedCount}`
              : occupiedCount}
          </span>
        </button>

        {/* FREE */}
        <button
          type="button"
          onClick={() =>
            onFilterChange(
              filter === SeatFilter.AVAILABLE ? SeatFilter.ALL : SeatFilter.AVAILABLE
            )
          }
          data-testid="stat_card_free"
          className={`p-3 rounded-2xl flex flex-col items-center justify-center transition-all cursor-pointer bg-[#E7F7ED] border ${
            filter === SeatFilter.AVAILABLE
              ? 'border-2 border-[#166534] shadow-sm scale-[1.02]'
              : 'border-[#D1EBDD] hover:border-[#166534]/60'
          }`}
        >
          <span className="text-[10px] font-black tracking-wider text-[#166534]/80 uppercase">
            FREE
          </span>
          <span className="text-xl font-black text-[#166534] leading-tight mt-0.5">
            {availableCount < 10 && availableCount >= 0
              ? `0${availableCount}`
              : availableCount}
          </span>
        </button>

        {/* RESERVED */}
        <button
          type="button"
          onClick={() =>
            onFilterChange(
              filter === SeatFilter.RESERVED ? SeatFilter.ALL : SeatFilter.RESERVED
            )
          }
          data-testid="stat_card_res"
          className={`p-3 rounded-2xl flex flex-col items-center justify-center transition-all cursor-pointer bg-[#FFF4E0] border ${
            filter === SeatFilter.RESERVED
              ? 'border-2 border-[#92400E] shadow-sm scale-[1.02]'
              : 'border-[#FFE7C2] hover:border-[#92400E]/60'
          }`}
        >
          <span className="text-[10px] font-black tracking-wider text-[#92400E]/80 uppercase">
            RES
          </span>
          <span className="text-xl font-black text-[#92400E] leading-tight mt-0.5">
            {reservedCount < 10 && reservedCount >= 0
              ? `0${reservedCount}`
              : reservedCount}
          </span>
        </button>
      </div>

      {/* 2. Monthly Fee Hero Card */}
      <div
        data-testid="monthly_fee_card"
        className="rounded-3xl bg-[#3F51B5] text-white p-5 shadow-md shadow-indigo-500/20 flex items-center justify-between"
      >
        <div className="flex items-center gap-3.5">
          <div className="w-11 h-11 rounded-full bg-white/20 flex items-center justify-center">
            <IndianRupee className="w-6 h-6 text-white" />
          </div>
          <div>
            <h2 className="text-base font-bold text-white tracking-tight">
              Monthly Library Fee
            </h2>
            <p className="text-xs text-white/80 font-medium">
              Standard per seat monthly admission
            </p>
          </div>
        </div>
        <div className="text-2xl md:text-3xl font-black text-white">
          ₹{settings.monthlyFee}
        </div>
      </div>

      {/* 3. Search & Filters */}
      <div className="space-y-2.5">
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-[#3F51B5]">
            <Search className="w-5 h-5" />
          </div>
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder="Search seat #, student name, or phone..."
            data-testid="seat_search_input"
            className="w-full pl-11 pr-10 py-3 bg-white border border-slate-200 rounded-2xl text-sm font-medium focus:outline-none focus:border-[#3F51B5] focus:ring-1 focus:ring-[#3F51B5] shadow-xs placeholder:text-slate-400"
          />
          {searchQuery && (
            <button
              onClick={() => onSearchChange('')}
              className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-600"
            >
              <X className="w-4 h-4" />
            </button>
          )}
        </div>

        <div className="flex items-center gap-1.5 flex-wrap">
          <button
            onClick={() => onFilterChange(SeatFilter.ALL)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              filter === SeatFilter.ALL
                ? 'bg-[#E8DEF8] text-[#311B92] font-black border border-[#D0BCFF]'
                : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
            }`}
          >
            All ({totalSeats})
          </button>
          <button
            onClick={() => onFilterChange(SeatFilter.AVAILABLE)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              filter === SeatFilter.AVAILABLE
                ? 'bg-[#E7F7ED] text-[#166534] font-black border border-[#D1EBDD]'
                : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
            }`}
          >
            Free ({availableCount})
          </button>
          <button
            onClick={() => onFilterChange(SeatFilter.OCCUPIED)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              filter === SeatFilter.OCCUPIED
                ? 'bg-[#FDE7E7] text-[#991B1B] font-black border border-[#F9D2D2]'
                : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
            }`}
          >
            Occ ({occupiedCount})
          </button>
          <button
            onClick={() => onFilterChange(SeatFilter.RESERVED)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              filter === SeatFilter.RESERVED
                ? 'bg-[#FFF4E0] text-[#92400E] font-black border border-[#FFE7C2]'
                : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
            }`}
          >
            Res ({reservedCount})
          </button>
        </div>
      </div>

      {/* 4. Live Seat Map Header */}
      <div className="flex items-center justify-between pt-1">
        <h2 className="text-lg font-black tracking-tight text-slate-800">
          Live Seat Map
        </h2>
        <div className="flex items-center gap-3">
          <div className="flex items-center gap-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-[#86EFAC] inline-block"></span>
            <span className="text-xs font-bold text-slate-600">Free</span>
          </div>
          <div className="flex items-center gap-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-[#FCA5A5] inline-block"></span>
            <span className="text-xs font-bold text-slate-600">Busy</span>
          </div>
          <div className="flex items-center gap-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-[#FDE68A] inline-block"></span>
            <span className="text-xs font-bold text-slate-600">Res</span>
          </div>
        </div>
      </div>

      {/* 5. Seat Grid Container */}
      <div className="bg-white rounded-3xl p-4 border border-slate-200/80 shadow-xs">
        {filteredSeats.length === 0 ? (
          <div className="py-12 text-center text-sm font-semibold text-slate-500">
            No seats match your search or filter
          </div>
        ) : (
          <div
            data-testid="seat_map_wrap"
            className="grid grid-cols-5 sm:grid-cols-8 md:grid-cols-10 gap-2"
          >
            {filteredSeats.map((seat) => {
              const status = getSeatStatus(seat);
              const formattedNum =
                seat.seatNumber < 10
                  ? `0${seat.seatNumber}`
                  : `${seat.seatNumber}`;

              let bgClass = 'bg-[#86EFAC] border-emerald-300 text-[#14532D]';
              if (status === SeatStatus.OCCUPIED) {
                bgClass = 'bg-[#FCA5A5] border-red-300 text-[#7F1D1D]';
              } else if (status === SeatStatus.RESERVED) {
                bgClass = 'bg-[#FDE68A] border-amber-300 text-[#78350F]';
              }

              return (
                <button
                  key={seat.seatNumber}
                  onClick={() => onOpenSeat(seat)}
                  data-testid={`seat_tile_${seat.seatNumber}`}
                  className={`h-12 rounded-xl border flex flex-col items-center justify-center p-0.5 transition-transform hover:scale-105 active:scale-95 cursor-pointer shadow-xs ${bgClass}`}
                >
                  <span className="text-sm font-black tracking-tight leading-none">
                    {formattedNum}
                  </span>
                  {seat.isReserved ? (
                    <Bookmark className="w-2.5 h-2.5 mt-0.5 fill-current" />
                  ) : status === SeatStatus.OCCUPIED && seat.studentName ? (
                    <span className="text-[8px] font-extrabold uppercase truncate max-w-[38px] leading-tight mt-0.5">
                      {seat.studentName.slice(0, 3)}
                    </span>
                  ) : null}
                </button>
              );
            })}
          </div>
        )}
      </div>

      {/* 6. Announcements Header */}
      <div className="flex items-center justify-between pt-2">
        <h2 className="text-lg font-black tracking-tight text-slate-800">
          Announcements
        </h2>
        <button
          onClick={onOpenAddNotice}
          data-testid="add_notice_icon_button"
          className="text-xs font-black text-[#3F51B5] hover:text-indigo-800 flex items-center gap-1 cursor-pointer"
        >
          <Plus className="w-4 h-4" />
          Add Notice
        </button>
      </div>

      {/* 7. Notice List */}
      <div className="space-y-2.5">
        {notices.length === 0 ? (
          <div className="bg-white rounded-2xl p-4 border border-slate-200 text-sm font-medium text-slate-500">
            No announcements yet. Tap '+ Add Notice' to broadcast.
          </div>
        ) : (
          notices.map((notice) => (
            <div
              key={notice.id}
              data-testid={`notice_card_${notice.id}`}
              className="bg-white rounded-2xl p-4 border border-slate-200/80 shadow-xs flex items-center justify-between gap-3"
            >
              <div className="flex items-center gap-3.5 flex-1 min-w-0">
                <div className="w-10 h-10 rounded-xl bg-indigo-50 border border-indigo-100 flex items-center justify-center shrink-0 text-[#3F51B5]">
                  <Megaphone className="w-5 h-5" />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-bold text-slate-800 break-words">
                    {notice.text}
                  </p>
                  <p className="text-[11px] font-medium text-slate-400 mt-0.5">
                    {formatTimestamp(notice.timestamp)}
                  </p>
                </div>
              </div>
              <button
                onClick={() => onDeleteNotice(notice)}
                className="w-8 h-8 rounded-lg flex items-center justify-center text-slate-400 hover:text-red-600 hover:bg-red-50 transition-colors shrink-0 cursor-pointer"
                aria-label="Delete notice"
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
