import React from 'react';
import {
  SeatEntity,
  LibrarySettingsEntity,
  SeatStatus,
} from '../types';
import { getSeatStatus } from '../utils/storage';
import {
  UserCheck,
  Phone,
  Settings,
  IndianRupee,
  Megaphone,
  Share2,
  CheckCircle,
  Clock,
  AlertTriangle,
  Users,
} from 'lucide-react';

interface HolderControlViewProps {
  seats: SeatEntity[];
  settings: LibrarySettingsEntity;
  onOpenEditProfile: () => void;
  onOpenAddNotice: () => void;
  onShareReport: () => void;
  onToggleFeeStatus: (seatNumber: number) => void;
  onOpenResetConfirm: () => void;
}

export const HolderControlView: React.FC<HolderControlViewProps> = ({
  seats,
  settings,
  onOpenEditProfile,
  onOpenAddNotice,
  onShareReport,
  onToggleFeeStatus,
  onOpenResetConfirm,
}) => {
  const occupiedSeats = seats.filter((s) => getSeatStatus(s) === SeatStatus.OCCUPIED);
  const paidSeats = occupiedSeats.filter((s) => s.feeStatus.toLowerCase() === 'paid');
  const pendingSeats = occupiedSeats.filter((s) => s.feeStatus.toLowerCase() !== 'paid');

  const monthlyFee = settings.monthlyFee || 400;
  const totalCollected = paidSeats.length * monthlyFee;
  const totalPending = pendingSeats.length * monthlyFee;

  return (
    <div className="space-y-4 pb-12" data-testid="holder_view">
      {/* 1. Holder Profile Card */}
      <div className="bg-white rounded-3xl p-5 border border-slate-200/80 shadow-xs space-y-4">
        <div className="flex items-start justify-between gap-3">
          <div className="flex items-center gap-3.5">
            <div className="w-14 h-14 rounded-2xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-[#3F51B5]">
              <UserCheck className="w-7 h-7" />
            </div>
            <div>
              <h2 className="text-lg font-black text-slate-800 tracking-tight leading-tight">
                {settings.ownerName}
              </h2>
              <p className="text-xs font-bold text-slate-500 mt-0.5">
                {settings.libraryName} • {settings.branchName}
              </p>
              {settings.ownerPhone && (
                <a
                  href={`tel:${settings.ownerPhone}`}
                  className="text-xs font-bold text-[#3F51B5] hover:underline flex items-center gap-1 mt-1"
                >
                  <Phone className="w-3.5 h-3.5" />
                  {settings.ownerPhone}
                </a>
              )}
            </div>
          </div>

          <button
            onClick={onOpenEditProfile}
            data-testid="edit_holder_profile_button"
            className="px-3 py-1.5 rounded-xl bg-slate-100 hover:bg-indigo-50 hover:text-[#3F51B5] text-slate-600 text-xs font-black flex items-center gap-1.5 transition-colors cursor-pointer"
          >
            <Settings className="w-4 h-4" />
            Edit Profile
          </button>
        </div>
      </div>

      {/* 2. Fees & Revenue Matrix Hero */}
      <div className="rounded-3xl bg-[#3F51B5] text-white p-5 shadow-md shadow-indigo-500/20 space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-xs font-bold text-white/80 uppercase tracking-wide">
              Monthly Fee Register
            </p>
            <h2 className="text-xl font-black text-white tracking-tight">
              ₹{monthlyFee} / Month Rate
            </h2>
          </div>
          <div className="w-11 h-11 rounded-2xl bg-white/20 flex items-center justify-center">
            <IndianRupee className="w-6 h-6 text-white" />
          </div>
        </div>

        {/* 2 Big Matrix Stat Cards */}
        <div className="grid grid-cols-2 gap-3">
          <div className="bg-white/15 rounded-2xl p-3 space-y-1">
            <span className="text-[10px] font-bold text-emerald-200 block uppercase">
              COLLECTED
            </span>
            <div className="text-xl font-black text-white">₹{totalCollected}</div>
            <span className="text-[11px] text-white/80 font-medium block">
              {paidSeats.length} Students Paid
            </span>
          </div>

          <div className="bg-white/15 rounded-2xl p-3 space-y-1">
            <span className="text-[10px] font-bold text-amber-200 block uppercase">
              PENDING DUES
            </span>
            <div className="text-xl font-black text-white">₹{totalPending}</div>
            <span className="text-[11px] text-white/80 font-medium block">
              {pendingSeats.length} Students Pending
            </span>
          </div>
        </div>
      </div>

      {/* 3. Holder Command Buttons */}
      <div className="grid grid-cols-2 gap-3">
        <button
          onClick={onOpenAddNotice}
          data-testid="holder_post_notice_button"
          className="p-3.5 rounded-2xl bg-white border border-slate-200 shadow-xs hover:border-[#3F51B5] hover:bg-indigo-50/50 flex items-center justify-center gap-2 text-xs font-black text-[#3F51B5] transition-all cursor-pointer"
        >
          <Megaphone className="w-4 h-4" />
          Post Notice
        </button>

        <button
          onClick={onShareReport}
          data-testid="holder_share_report_button"
          className="p-3.5 rounded-2xl bg-white border border-slate-200 shadow-xs hover:border-[#3F51B5] hover:bg-indigo-50/50 flex items-center justify-center gap-2 text-xs font-black text-[#3F51B5] transition-all cursor-pointer"
        >
          <Share2 className="w-4 h-4" />
          Share Report
        </button>
      </div>

      {/* 4. Student Fee Register */}
      <div className="space-y-3 pt-2">
        <div className="flex items-center justify-between">
          <h2 className="text-lg font-black tracking-tight text-slate-800 flex items-center gap-2">
            <Users className="w-5 h-5 text-[#3F51B5]" />
            Student Fee Register ({occupiedSeats.length})
          </h2>
        </div>

        {occupiedSeats.length === 0 ? (
          <div className="bg-white rounded-3xl p-8 border border-slate-200 text-center text-sm font-medium text-slate-500">
            No students currently seated. Assign seats on the Seat Map to track fees.
          </div>
        ) : (
          <div className="space-y-2.5">
            {occupiedSeats.map((seat) => {
              const isPaid = seat.feeStatus.toLowerCase() === 'paid';
              const formattedSeat =
                seat.seatNumber < 10
                  ? `0${seat.seatNumber}`
                  : `${seat.seatNumber}`;

              return (
                <div
                  key={seat.seatNumber}
                  data-testid={`fee_row_seat_${seat.seatNumber}`}
                  className="bg-white rounded-2xl p-3.5 border border-slate-200/80 shadow-xs flex items-center justify-between gap-3"
                >
                  <div className="flex items-center gap-3 min-w-0">
                    <div className="w-10 h-10 rounded-xl bg-indigo-50 border border-indigo-100 flex items-center justify-center font-black text-xs text-[#3F51B5] shrink-0">
                      {formattedSeat}
                    </div>
                    <div className="min-w-0">
                      <p className="text-sm font-black text-slate-800 truncate">
                        {seat.studentName}
                      </p>
                      {seat.mobileNumber ? (
                        <a
                          href={`tel:${seat.mobileNumber}`}
                          className="text-xs font-bold text-[#3F51B5] hover:underline flex items-center gap-1"
                        >
                          <Phone className="w-3 h-3" />
                          {seat.mobileNumber}
                        </a>
                      ) : (
                        <span className="text-xs text-slate-400">
                          In: {seat.inTime || 'N/A'}
                        </span>
                      )}
                    </div>
                  </div>

                  <button
                    onClick={() => onToggleFeeStatus(seat.seatNumber)}
                    data-testid={`toggle_fee_status_${seat.seatNumber}`}
                    className={`px-3 py-1.5 rounded-xl text-xs font-black flex items-center gap-1.5 transition-all cursor-pointer shrink-0 ${
                      isPaid
                        ? 'bg-emerald-50 text-emerald-700 border border-emerald-200 hover:bg-emerald-100'
                        : 'bg-amber-50 text-amber-800 border border-amber-200 hover:bg-amber-100'
                    }`}
                  >
                    {isPaid ? (
                      <>
                        <CheckCircle className="w-3.5 h-3.5" />
                        Paid (₹{monthlyFee})
                      </>
                    ) : (
                      <>
                        <Clock className="w-3.5 h-3.5" />
                        Pending
                      </>
                    )}
                  </button>
                </div>
              );
            })}
          </div>
        )}
      </div>

      {/* 5. Danger Zone */}
      <div className="pt-4 border-t border-slate-200">
        <div className="bg-red-50/70 rounded-3xl p-5 border border-red-200/80 space-y-3">
          <div className="flex items-center gap-2.5 text-red-700">
            <AlertTriangle className="w-5 h-5" />
            <h3 className="text-sm font-black uppercase tracking-wider">
              Danger Zone
            </h3>
          </div>
          <p className="text-xs text-red-600 font-medium">
            Resetting all 100 seats will clear student names, times, phone numbers, and reservations back to default available state.
          </p>
          <button
            onClick={onOpenResetConfirm}
            data-testid="reset_all_seats_button"
            className="w-full py-3 rounded-2xl bg-red-600 hover:bg-red-700 text-white font-black text-xs md:text-sm shadow-xs transition-colors cursor-pointer"
          >
            Reset All 100 Seats
          </button>
        </div>
      </div>
    </div>
  );
};
