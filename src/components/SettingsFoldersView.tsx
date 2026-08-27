import React from 'react';
import { MainTab, FolderType, LibrarySettingsEntity } from '../types';
import {
  Folder,
  ChevronRight,
  UserCheck,
  ShieldCheck,
  IndianRupee,
  Wifi,
  Share2,
} from 'lucide-react';

interface SettingsFoldersViewProps {
  settings: LibrarySettingsEntity;
  onSelectTab: (tab: MainTab) => void;
  onOpenFolderDetail: (type: FolderType) => void;
}

export const SettingsFoldersView: React.FC<SettingsFoldersViewProps> = ({
  settings,
  onSelectTab,
  onOpenFolderDetail,
}) => {
  const folders = [
    {
      id: FolderType.ATTENDANCE,
      icon: UserCheck,
      title: 'Daily Attendance Register',
      hindiTitle: 'दैनिक उपस्थिति रजिस्टर',
      desc: 'Check-ins, punch outs, daily student presence logs and history.',
      actionType: 'navigate',
      targetTab: MainTab.ATTENDANCE,
    },
    {
      id: FolderType.HOLDER,
      icon: ShieldCheck,
      title: 'Library Holder (Owner) Control',
      hindiTitle: 'लाइब्रेरी धारक एवं सम्पूर्ण नियंत्रण',
      desc: 'Master administration, contact details, holder profile & security.',
      actionType: 'navigate',
      targetTab: MainTab.HOLDER,
    },
    {
      id: FolderType.FEES,
      icon: IndianRupee,
      title: 'Monthly Fees & Accounts',
      hindiTitle: 'मासिक शुल्क व हिसाब',
      desc: `₹${settings.monthlyFee}/month rate, collected vs pending dues matrix.`,
      actionType: 'navigate',
      targetTab: MainTab.HOLDER,
    },
    {
      id: FolderType.FACILITIES,
      icon: Wifi,
      title: 'Facilities, Wi-Fi & Timings',
      hindiTitle: 'लाइब्रेरी सुविधाएं एवं नियम',
      desc: `Timings: ${settings.openingHours} • Wi-Fi Password & Address`,
      actionType: 'modal',
    },
    {
      id: FolderType.BACKUP,
      icon: Share2,
      title: 'Data Backup & Export',
      hindiTitle: 'डेटा बैकअप एवं शेयरिंग',
      desc: 'Share live seat summary, student registers & configuration.',
      actionType: 'modal',
    },
  ];

  return (
    <div className="space-y-4 pb-12" data-testid="settings_folders_view">
      {/* Header Banner */}
      <div className="bg-white rounded-3xl p-5 border border-slate-200/80 shadow-xs space-y-1">
        <div className="flex items-center gap-2 text-[#3F51B5]">
          <Folder className="w-5 h-5" />
          <h2 className="text-base font-black uppercase tracking-wider">
            Directory & Archives
          </h2>
        </div>
        <p className="text-xs text-slate-500 font-medium">
          Access all administrative folders, attendance registers, accounts and library branch policies.
        </p>
      </div>

      {/* Folders List */}
      <div className="space-y-3">
        {folders.map((item) => {
          const Icon = item.icon;
          return (
            <button
              key={item.id}
              onClick={() => {
                if (item.actionType === 'navigate' && item.targetTab) {
                  onSelectTab(item.targetTab);
                } else {
                  onOpenFolderDetail(item.id);
                }
              }}
              data-testid={`folder_item_${item.id}`}
              className="w-full text-left bg-white rounded-3xl p-4 border border-slate-200/80 shadow-xs hover:border-[#3F51B5]/60 hover:shadow-sm transition-all flex items-center justify-between gap-3 cursor-pointer group"
            >
              <div className="flex items-center gap-3.5 min-w-0">
                <div className="w-12 h-12 rounded-2xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-[#3F51B5] group-hover:scale-105 transition-transform shrink-0">
                  <Icon className="w-6 h-6" />
                </div>
                <div className="min-w-0 flex-1">
                  <h3 className="text-sm md:text-base font-black text-slate-800 tracking-tight leading-snug truncate">
                    {item.title}
                  </h3>
                  <p className="text-xs font-bold text-[#3F51B5] leading-tight mt-0.5">
                    {item.hindiTitle}
                  </p>
                  <p className="text-[11px] text-slate-500 font-medium truncate mt-0.5">
                    {item.desc}
                  </p>
                </div>
              </div>

              <div className="w-8 h-8 rounded-full bg-slate-50 group-hover:bg-indigo-50 flex items-center justify-center text-slate-400 group-hover:text-[#3F51B5] transition-colors shrink-0">
                <ChevronRight className="w-4 h-4" />
              </div>
            </button>
          );
        })}
      </div>

      {/* App Info Footer */}
      <div className="pt-6 text-center space-y-1">
        <p className="text-xs font-black text-slate-600">
          {settings.libraryName} • {settings.branchName}
        </p>
        <p className="text-[10px] text-slate-400 font-semibold tracking-wider uppercase">
          Smart Seat & Attendance System v2.0
        </p>
      </div>
    </div>
  );
};
