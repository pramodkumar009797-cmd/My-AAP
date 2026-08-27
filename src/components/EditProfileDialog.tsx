import React, { useState } from 'react';
import { LibrarySettingsEntity } from '../types';
import {
  X,
  Building,
  MapPin,
  User,
  Phone,
  IndianRupee,
  Clock,
  Wifi,
  Save,
} from 'lucide-react';

interface EditProfileDialogProps {
  isOpen: boolean;
  settings: LibrarySettingsEntity;
  onClose: () => void;
  onSave: (updated: LibrarySettingsEntity) => void;
}

export const EditProfileDialog: React.FC<EditProfileDialogProps> = ({
  isOpen,
  settings,
  onClose,
  onSave,
}) => {
  if (!isOpen) return null;

  const [libraryName, setLibraryName] = useState(settings.libraryName);
  const [branchName, setBranchName] = useState(settings.branchName);
  const [ownerName, setOwnerName] = useState(settings.ownerName);
  const [ownerPhone, setOwnerPhone] = useState(settings.ownerPhone);
  const [monthlyFee, setMonthlyFee] = useState(settings.monthlyFee);
  const [openingHours, setOpeningHours] = useState(settings.openingHours);
  const [wifiPassword, setWifiPassword] = useState(settings.wifiPassword);
  const [address, setAddress] = useState(settings.address);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave({
      ...settings,
      libraryName: libraryName.trim() || 'Bhagat Singh Library',
      branchName: branchName.trim() || 'Malkhera',
      ownerName: ownerName.trim() || 'Library Holder / Owner',
      ownerPhone: ownerPhone.trim(),
      monthlyFee: Number(monthlyFee) || 400,
      openingHours: openingHours.trim(),
      wifiPassword: wifiPassword.trim(),
      address: address.trim(),
    });
    onClose();
  };

  return (
    <div
      data-testid="edit_profile_dialog"
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-xs overflow-y-auto"
    >
      <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-200 space-y-4 my-8">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-[#3F51B5]">
              <Building className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-black text-slate-800 tracking-tight">
                Edit Holder & Branch Details
              </h2>
              <p className="text-xs text-slate-500 font-medium">
                Update library administration preferences
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

        <form onSubmit={handleSubmit} className="space-y-3">
          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-bold text-slate-700">Library Name</label>
              <input
                type="text"
                value={libraryName}
                onChange={(e) => setLibraryName(e.target.value)}
                required
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-semibold focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-bold text-slate-700">Branch Name</label>
              <input
                type="text"
                value={branchName}
                onChange={(e) => setBranchName(e.target.value)}
                required
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-semibold focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
                <User className="w-3 h-3 text-[#3F51B5]" />
                Owner Name
              </label>
              <input
                type="text"
                value={ownerName}
                onChange={(e) => setOwnerName(e.target.value)}
                required
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-semibold focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
                <Phone className="w-3 h-3 text-[#3F51B5]" />
                Owner Phone
              </label>
              <input
                type="tel"
                value={ownerPhone}
                onChange={(e) => setOwnerPhone(e.target.value)}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-semibold focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
                <IndianRupee className="w-3 h-3 text-[#3F51B5]" />
                Monthly Fee (₹)
              </label>
              <input
                type="number"
                value={monthlyFee}
                onChange={(e) => setMonthlyFee(Number(e.target.value))}
                min={0}
                required
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-black text-[#3F51B5] focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
                <Clock className="w-3 h-3 text-slate-400" />
                Opening Hours
              </label>
              <input
                type="text"
                value={openingHours}
                onChange={(e) => setOpeningHours(e.target.value)}
                placeholder="06:00 AM - 10:00 PM"
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              />
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
              <Wifi className="w-3 h-3 text-[#3F51B5]" />
              Wi-Fi Password
            </label>
            <input
              type="text"
              value={wifiPassword}
              onChange={(e) => setWifiPassword(e.target.value)}
              placeholder="BSL@Malkhera2026"
              className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
            />
          </div>

          <div className="space-y-1">
            <label className="text-xs font-bold text-slate-700 flex items-center gap-1">
              <MapPin className="w-3 h-3 text-[#3F51B5]" />
              Address / Location
            </label>
            <input
              type="text"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              placeholder="Near Main Chowk, Malkhera, Rajasthan"
              className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
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
              data-testid="save_settings_button"
              className="flex-1 py-3 rounded-2xl bg-[#3F51B5] hover:bg-indigo-700 text-white font-black text-xs flex items-center justify-center gap-1.5 shadow-xs transition-colors cursor-pointer"
            >
              <Save className="w-4 h-4" />
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
