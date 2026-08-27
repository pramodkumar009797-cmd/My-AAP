import React from 'react';
import { MainTab, LibrarySettingsEntity } from '../types';
import { Bell } from 'lucide-react';

interface HeaderProps {
  currentTab: MainTab;
  settings: LibrarySettingsEntity;
  occupiedCount: number;
  onOpenAddNotice: () => void;
}

export const Header: React.FC<HeaderProps> = ({
  currentTab,
  settings,
  occupiedCount,
  onOpenAddNotice,
}) => {
  const getTitles = () => {
    switch (currentTab) {
      case MainTab.DASHBOARD:
        return {
          title: settings.libraryName.toUpperCase(),
          subtitle: `${settings.branchName.toUpperCase()} • ${occupiedCount} SEATED`,
        };
      case MainTab.ATTENDANCE:
        return {
          title: 'DAILY ATTENDANCE',
          subtitle: 'STUDENT PRESENCE REGISTER',
        };
      case MainTab.HOLDER:
        return {
          title: 'HOLDER CONTROL',
          subtitle: 'OWNER ADMINISTRATION',
        };
      case MainTab.SETTINGS:
        return {
          title: 'SETTINGS & FOLDERS',
          subtitle: 'FOLDERS & SYSTEM ARCHIVES',
        };
    }
  };

  const { title, subtitle } = getTitles();

  return (
    <header className="sticky top-0 z-30 bg-[#FBFBFF]/95 backdrop-blur-md border-b border-slate-200/80 transition-all">
      <div className="max-w-3xl mx-auto px-4 py-3 flex items-center justify-between">
        <div className="flex-1 text-center pl-8">
          <h1 className="text-lg md:text-xl font-black tracking-tight text-[#3F51B5] truncate">
            {title}
          </h1>
          <p className="text-[10px] font-bold tracking-[1.5px] text-slate-500 uppercase">
            {subtitle}
          </p>
        </div>

        <button
          onClick={onOpenAddNotice}
          data-testid="top_bar_add_notice_button"
          aria-label="Announcements"
          className="w-10 h-10 rounded-full bg-white border border-slate-200 shadow-sm flex items-center justify-center text-[#3F51B5] hover:bg-indigo-50 hover:border-indigo-200 transition-colors focus:outline-none cursor-pointer"
        >
          <Bell className="w-5 h-5" />
        </button>
      </div>
    </header>
  );
};
