import React, { useState, useEffect } from 'react';
import { SeatEntity, SeatStatus } from '../types';
import { getSeatStatus, getCurrentTimeString } from '../utils/storage';
import {
  X,
  User,
  Phone,
  Clock,
  IndianRupee,
  Bookmark,
  Trash2,
  Save,
} from 'lucide-react';

interface SeatDetailsDialogProps {
  seat: SeatEntity | null;
  isOpen: boolean;
  monthlyFee: number;
  onClose: () => void;
  onSave: (updatedSeat: SeatEntity, shouldLogAttendance: boolean) => void;
  onClear: (seatNumber: number) => void;
}

export const SeatDetailsDialog: React.FC<SeatDetailsDialogProps> = ({
  seat,
  isOpen,
  monthlyFee,
  onClose,
  onSave,
  onClear,
}) => {
  if (!isOpen || !seat) return null;

  const [studentName, setStudentName] = useState(seat.studentName);
  const [mobileNumber, setMobileNumber] = useState(seat.mobileNumber);
  const [inTime, setInTime] = useState(seat.inTime);
  const [outTime, setOutTime] = useState(seat.outTime);
  const [feeStatus, setFeeStatus] = useState(seat.feeStatus || 'Pending');
  const [isReserved, setIsReserved] = useState(seat.isReserved);
  const [logAttendance, setLogAttendance] = useState(true);

  useEffect(() => {
    if (seat) {
      setStudentName(seat.studentName);
      setMobileNumber(seat.mobileNumber);
      setInTime(seat.inTime);
      setOutTime(seat.outTime);
      setFeeStatus(seat.feeStatus || 'Pending');
      setIsReserved(seat.isReserved);
    }
  }, [seat]);

  const status = getSeatStatus({
    ...seat,
    studentName,
    outTime,
    isReserved,
  });

  const formattedSeatNumber =
    seat.seatNumber < 10 ? `0${seat.seatNumber}` : `${seat.seatNumber}`;

  const handleSetCurrentInTime = () => {
    setInTime(getCurrentTimeString());
  };

  const handleSetCurrentOutTime = () => {
    setOutTime(getCurrentTimeString());
  };

  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const updated: SeatEntity = {
      ...seat,
      studentName: studentName.trim(),
      mobileNumber: mobileNumber.trim(),
      inTime: inTime.trim(),
      outTime: outTime.trim(),
      feeStatus: feeStatus,
      isReserved: isReserved,
      updatedTimestamp: Date.now(),
    };
    onSave(updated, logAttendance && updated.studentName.length > 0);
  };

  return (
    <div
      data-testid="seat_details_dialog"
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-xs overflow-y-auto"
    >
      <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-200 space-y-5 my-8">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-2xl bg-[#3F51B5] text-white flex items-center justify-center font-black text-lg shadow-sm">
              {formattedSeatNumber}
            </div>
            <div>
              <h2 className="text-lg font-black text-slate-800 tracking-tight">
                Seat {formattedSeatNumber} Management
              </h2>
              <span
                className={`inline-block px-2 py-0.5 rounded-md text-[10px] font-black uppercase tracking-wider ${
                  status === SeatStatus.OCCUPIED
                    ? 'bg-red-50 text-red-700 border border-red-200'
                    : status === SeatStatus.RESERVED
                    ? 'bg-amber-50 text-amber-700 border border-amber-200'
                    : 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                }`}
              >
                {status}
              </span>
            </div>
          </div>

          <button
            onClick={onClose}
            className="w-9 h-9 rounded-full bg-slate-100 hover:bg-slate-200 flex items-center justify-center text-slate-500 cursor-pointer"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleFormSubmit} className="space-y-4">
          {/* Student Name */}
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1.5">
              <User className="w-3.5 h-3.5 text-[#3F51B5]" />
              Student Name
            </label>
            <input
              type="text"
              value={studentName}
              onChange={(e) => setStudentName(e.target.value)}
              placeholder="e.g. Ramesh Kumar"
              data-testid="seat_student_name_input"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
            />
          </div>

          {/* Mobile Number */}
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1.5">
              <Phone className="w-3.5 h-3.5 text-[#3F51B5]" />
              Mobile Number
            </label>
            <input
              type="tel"
              value={mobileNumber}
              onChange={(e) => setMobileNumber(e.target.value)}
              placeholder="+91 98765 43210"
              data-testid="seat_mobile_input"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
            />
          </div>

          {/* In Time & Out Time */}
          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1.5">
              <div className="flex items-center justify-between">
                <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
                  <Clock className="w-3 h-3 text-[#3F51B5]" />
                  In Time
                </label>
                <button
                  type="button"
                  onClick={handleSetCurrentInTime}
                  className="text-[10px] font-black text-[#3F51B5] hover:underline cursor-pointer"
                >
                  Set Now
                </button>
              </div>
              <input
                type="text"
                value={inTime}
                onChange={(e) => setInTime(e.target.value)}
                placeholder="08:00 AM"
                data-testid="seat_in_time_input"
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>

            <div className="space-y-1.5">
              <div className="flex items-center justify-between">
                <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
                  <Clock className="w-3 h-3 text-slate-400" />
                  Out Time
                </label>
                <button
                  type="button"
                  onClick={handleSetCurrentOutTime}
                  className="text-[10px] font-black text-[#3F51B5] hover:underline cursor-pointer"
                >
                  Set Now
                </button>
              </div>
              <input
                type="text"
                value={outTime}
                onChange={(e) => setOutTime(e.target.value)}
                placeholder="05:00 PM"
                data-testid="seat_out_time_input"
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>
          </div>

          {/* Fee Status */}
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1.5">
              <IndianRupee className="w-3.5 h-3.5 text-[#3F51B5]" />
              Fee Payment Status
            </label>
            <div className="grid grid-cols-2 gap-2">
              <button
                type="button"
                onClick={() => setFeeStatus('Paid')}
                className={`py-2 rounded-xl text-xs font-bold border transition-all cursor-pointer ${
                  feeStatus.toLowerCase() === 'paid'
                    ? 'bg-emerald-50 border-emerald-300 text-emerald-800 font-black shadow-xs'
                    : 'bg-white border-slate-200 text-slate-600 hover:bg-slate-50'
                }`}
              >
                Paid (₹{monthlyFee})
              </button>
              <button
                type="button"
                onClick={() => setFeeStatus('Pending')}
                className={`py-2 rounded-xl text-xs font-bold border transition-all cursor-pointer ${
                  feeStatus.toLowerCase() !== 'paid'
                    ? 'bg-amber-50 border-amber-300 text-amber-800 font-black shadow-xs'
                    : 'bg-white border-slate-200 text-slate-600 hover:bg-slate-50'
                }`}
              >
                Pending Dues
              </button>
            </div>
          </div>

          {/* Reserved Checkbox */}
          <div className="flex items-center justify-between p-3 bg-amber-50/60 rounded-2xl border border-amber-200/80">
            <div className="flex items-center gap-2.5">
              <Bookmark className="w-4 h-4 text-amber-700" />
              <div>
                <span className="text-xs font-bold text-amber-900 block">
                  Mark as Reserved
                </span>
                <span className="text-[10px] text-amber-700">
                  Block seat for special reservation
                </span>
              </div>
            </div>
            <input
              type="checkbox"
              checked={isReserved}
              onChange={(e) => setIsReserved(e.target.checked)}
              data-testid="seat_is_reserved_checkbox"
              className="w-4 h-4 rounded text-amber-600 focus:ring-amber-500 cursor-pointer"
            />
          </div>

          {/* Check-in Auto Log Option */}
          {studentName.trim().length > 0 && (
            <div className="flex items-center gap-2 text-xs text-slate-600 px-1">
              <input
                type="checkbox"
                id="autoLogAtt"
                checked={logAttendance}
                onChange={(e) => setLogAttendance(e.target.checked)}
                className="w-3.5 h-3.5 rounded text-[#3F51B5] cursor-pointer"
              />
              <label htmlFor="autoLogAtt" className="cursor-pointer font-medium">
                Auto-log to today's Attendance Register
              </label>
            </div>
          )}

          {/* Action Buttons */}
          <div className="flex items-center gap-2 pt-2">
            <button
              type="button"
              onClick={() => onClear(seat.seatNumber)}
              data-testid="clear_seat_button"
              className="px-4 py-3 rounded-2xl bg-red-50 hover:bg-red-100 text-red-700 text-xs font-black flex items-center gap-1.5 transition-colors cursor-pointer"
            >
              <Trash2 className="w-4 h-4" />
              Clear
            </button>

            <button
              type="submit"
              data-testid="save_seat_button"
              className="flex-1 py-3 rounded-2xl bg-[#3F51B5] hover:bg-indigo-700 text-white text-xs font-black flex items-center justify-center gap-2 shadow-xs transition-colors cursor-pointer"
            >
              <Save className="w-4 h-4" />
              Save Seat
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
