import React from 'react';
import { FolderType, LibrarySettingsEntity, SeatEntity, AttendanceLog } from '../types';
import {
  X,
  Wifi,
  Clock,
  MapPin,
  Share2,
  FileText,
  Copy,
  Check,
} from 'lucide-react';

interface FolderDetailModalProps {
  type: FolderType | null;
  settings: LibrarySettingsEntity;
  seats: SeatEntity[];
  attendanceLogs: AttendanceLog[];
  onClose: () => void;
  onShareReport: () => void;
}

export const FolderDetailModal: React.FC<FolderDetailModalProps> = ({
  type,
  settings,
  seats,
  attendanceLogs,
  onClose,
  onShareReport,
}) => {
  const [copied, setCopied] = React.useState(false);

  if (!type) return null;

  const handleCopyWifi = () => {
    navigator.clipboard.writeText(settings.wifiPassword || 'BSL@Malkhera2026');
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div
      data-testid="folder_detail_modal"
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-xs overflow-y-auto"
    >
      <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-200 space-y-4 my-8">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-[#3F51B5]">
              {type === FolderType.FACILITIES ? (
                <Wifi className="w-5 h-5" />
              ) : (
                <Share2 className="w-5 h-5" />
              )}
            </div>
            <div>
              <h2 className="text-base font-black text-slate-800 tracking-tight">
                {type === FolderType.FACILITIES
                  ? 'Facilities & Branch Details'
                  : 'Data Backup & Reports'}
              </h2>
              <p className="text-xs text-[#3F51B5] font-bold">
                {type === FolderType.FACILITIES
                  ? 'लाइब्रेरी सुविधाएं एवं नियम'
                  : 'डेटा बैकअप एवं शेयरिंग'}
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

        {type === FolderType.FACILITIES && (
          <div className="space-y-3.5">
            {/* Timings */}
            <div className="p-3.5 bg-slate-50 rounded-2xl border border-slate-200 space-y-1">
              <div className="flex items-center gap-2 text-xs font-bold text-slate-700">
                <Clock className="w-4 h-4 text-[#3F51B5]" />
                Daily Timings (समय)
              </div>
              <p className="text-sm font-black text-slate-800 pl-6">
                {settings.openingHours || '06:00 AM - 10:00 PM'}
              </p>
            </div>

            {/* Wi-Fi */}
            <div className="p-3.5 bg-slate-50 rounded-2xl border border-slate-200 space-y-1">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2 text-xs font-bold text-slate-700">
                  <Wifi className="w-4 h-4 text-[#3F51B5]" />
                  High-Speed Student Wi-Fi
                </div>
                <button
                  onClick={handleCopyWifi}
                  className="text-[11px] font-bold text-[#3F51B5] flex items-center gap-1 hover:underline cursor-pointer"
                >
                  {copied ? (
                    <>
                      <Check className="w-3 h-3 text-emerald-600" />
                      Copied!
                    </>
                  ) : (
                    <>
                      <Copy className="w-3 h-3" />
                      Copy
                    </>
                  )}
                </button>
              </div>
              <p className="text-sm font-mono font-bold text-slate-800 pl-6">
                {settings.wifiPassword || 'BSL@Malkhera2026'}
              </p>
            </div>

            {/* Address */}
            <div className="p-3.5 bg-slate-50 rounded-2xl border border-slate-200 space-y-1">
              <div className="flex items-center gap-2 text-xs font-bold text-slate-700">
                <MapPin className="w-4 h-4 text-[#3F51B5]" />
                Address & Location
              </div>
              <p className="text-xs font-medium text-slate-700 pl-6">
                {settings.address || 'Near Main Chowk, Malkhera, Rajasthan'}
              </p>
            </div>

            {/* Rules */}
            <div className="p-3.5 bg-indigo-50/60 rounded-2xl border border-indigo-100 space-y-1 text-xs text-slate-700">
              <span className="font-bold text-[#3F51B5] block">
                Library Rules (नियम व शर्तें):
              </span>
              <ul className="list-disc list-inside space-y-0.5 text-[11px] text-slate-600 font-medium">
                <li>शांत वातावरण बनाए रखें (Maintain silence)</li>
                <li>समय पर मासिक शुल्क जमा कराएं (Pay fees on time)</li>
                <li>अपनी नियत सीट पर ही बैठें (Occupancy discipline)</li>
              </ul>
            </div>
          </div>
        )}

        {type === FolderType.BACKUP && (
          <div className="space-y-3.5">
            <div className="p-4 bg-slate-50 rounded-2xl border border-slate-200 space-y-2">
              <div className="flex items-center gap-2 text-xs font-bold text-slate-700">
                <FileText className="w-4 h-4 text-[#3F51B5]" />
                Full Library Status Summary
              </div>
              <p className="text-xs text-slate-600 font-medium leading-relaxed">
                You can generate a comprehensive textual status report with current occupied seats, attendance counts, fees collected, and share it via WhatsApp, SMS, or copy to clipboard.
              </p>
            </div>

            <div className="grid grid-cols-2 gap-2 text-center text-xs">
              <div className="p-3 bg-indigo-50 rounded-2xl border border-indigo-100">
                <span className="text-[10px] text-slate-500 font-bold block uppercase">
                  Seats Configured
                </span>
                <span className="text-base font-black text-[#3F51B5]">
                  {seats.length}
                </span>
              </div>
              <div className="p-3 bg-indigo-50 rounded-2xl border border-indigo-100">
                <span className="text-[10px] text-slate-500 font-bold block uppercase">
                  Total Logs
                </span>
                <span className="text-base font-black text-[#3F51B5]">
                  {attendanceLogs.length}
                </span>
              </div>
            </div>

            <button
              onClick={() => {
                onShareReport();
                onClose();
              }}
              className="w-full py-3 rounded-2xl bg-[#3F51B5] hover:bg-indigo-700 text-white font-black text-xs flex items-center justify-center gap-2 shadow-xs transition-colors cursor-pointer"
            >
              <Share2 className="w-4 h-4" />
              Generate & Copy Report
            </button>
          </div>
        )}

        <button
          onClick={onClose}
          className="w-full py-2.5 rounded-2xl bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs cursor-pointer"
        >
          Close
        </button>
      </div>
    </div>
  );
};
