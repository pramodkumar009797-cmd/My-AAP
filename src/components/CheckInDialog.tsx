import React, { useState } from 'react';
import { getCurrentTimeString } from '../utils/storage';
import { X, UserPlus, User, Phone, Clock, FileText } from 'lucide-react';

interface CheckInDialogProps {
  isOpen: boolean;
  totalSeats: number;
  onClose: () => void;
  onCheckIn: (
    studentName: string,
    seatNumber: number,
    mobileNumber: string,
    inTime: string,
    notes: string
  ) => void;
}

export const CheckInDialog: React.FC<CheckInDialogProps> = ({
  isOpen,
  totalSeats,
  onClose,
  onCheckIn,
}) => {
  if (!isOpen) return null;

  const [studentName, setStudentName] = useState('');
  const [seatNumber, setSeatNumber] = useState<number>(1);
  const [mobileNumber, setMobileNumber] = useState('');
  const [inTime, setInTime] = useState(getCurrentTimeString());
  const [notes, setNotes] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (studentName.trim() && seatNumber > 0) {
      onCheckIn(
        studentName.trim(),
        seatNumber,
        mobileNumber.trim(),
        inTime.trim() || getCurrentTimeString(),
        notes.trim()
      );
      onClose();
    }
  };

  return (
    <div
      data-testid="check_in_dialog"
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-xs overflow-y-auto"
    >
      <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-200 space-y-4 my-8">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-[#3F51B5]">
              <UserPlus className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-black text-slate-800 tracking-tight">
                Student Attendance Check-In
              </h2>
              <p className="text-xs text-slate-500 font-medium">
                Log daily arrival into register
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="w-8 h-8 rounded-full bg-slate-100 hover:bg-slate-200 flex items-center justify-center text-slate-500 cursor-pointer"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-3.5">
          {/* Seat Number Selector */}
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-slate-700">
              Assigned Seat Number (1 - {totalSeats})
            </label>
            <input
              type="number"
              min={1}
              max={totalSeats}
              value={seatNumber}
              onChange={(e) => setSeatNumber(Number(e.target.value))}
              required
              data-testid="checkin_seat_input"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-bold text-[#3F51B5] focus:bg-white focus:outline-none focus:border-[#3F51B5]"
            />
          </div>

          {/* Student Name */}
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
              <User className="w-3.5 h-3.5 text-[#3F51B5]" />
              Student Name
            </label>
            <input
              type="text"
              value={studentName}
              onChange={(e) => setStudentName(e.target.value)}
              placeholder="e.g. Rahul Sharma"
              required
              data-testid="checkin_name_input"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              autoFocus
            />
          </div>

          {/* Mobile */}
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
              <Phone className="w-3.5 h-3.5 text-[#3F51B5]" />
              Mobile Number (Optional)
            </label>
            <input
              type="tel"
              value={mobileNumber}
              onChange={(e) => setMobileNumber(e.target.value)}
              placeholder="+91 98765 43210"
              data-testid="checkin_phone_input"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
            />
          </div>

          {/* In Time */}
          <div className="space-y-1.5">
            <div className="flex items-center justify-between">
              <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
                <Clock className="w-3.5 h-3.5 text-[#3F51B5]" />
                Check-In Time
              </label>
              <button
                type="button"
                onClick={() => setInTime(getCurrentTimeString())}
                className="text-[10px] font-black text-[#3F51B5] hover:underline cursor-pointer"
              >
                Set Current Time
              </button>
            </div>
            <input
              type="text"
              value={inTime}
              onChange={(e) => setInTime(e.target.value)}
              placeholder="08:30 AM"
              data-testid="checkin_intime_input"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
            />
          </div>

          {/* Notes */}
          <div className="space-y-1.5">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
              <FileText className="w-3.5 h-3.5 text-slate-400" />
              Notes / Remark (Optional)
            </label>
            <input
              type="text"
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="e.g. Morning Shift"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
            />
          </div>

          <div className="flex items-center gap-2 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-3 rounded-2xl bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              data-testid="confirm_checkin_button"
              className="flex-1 py-3 rounded-2xl bg-[#3F51B5] hover:bg-indigo-700 text-white font-black text-xs shadow-xs transition-colors cursor-pointer"
            >
              Record Check-In
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
