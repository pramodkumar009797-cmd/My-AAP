import React from 'react';
import { MainTab } from '../types';
import { LayoutGrid, UserCheck, ShieldCheck, FolderGit2 } from 'lucide-react';

interface NavigationProps {
  currentTab: MainTab;
  onSelectTab: (tab: MainTab) => void;
}

export const Navigation: React.FC<NavigationProps> = ({ currentTab, onSelectTab }) => {
  const tabs = [
    {
      id: MainTab.DASHBOARD,
      label: 'Seats',
      icon: LayoutGrid,
      testTag: 'nav_item_dashboard',
    },
    {
      id: MainTab.ATTENDANCE,
      label: 'Attendance',
      icon: UserCheck,
      testTag: 'nav_item_attendance',
    },
    {
      id: MainTab.HOLDER,
      label: 'Holder',
      icon: ShieldCheck,
      testTag: 'nav_item_holder',
    },
    {
      id: MainTab.SETTINGS,
      label: 'Folders',
      icon: FolderGit2,
      testTag: 'nav_item_settings',
    },
  ];

  return (
    <nav
      data-testid="library_bottom_navigation"
      className="fixed bottom-0 left-0 right-0 z-40 bg-white/95 backdrop-blur-md border-t border-slate-200/90 shadow-lg"
    >
      <div className="max-w-3xl mx-auto px-4 flex items-center justify-around h-16">
        {tabs.map((tab) => {
          const isSelected = currentTab === tab.id;
          const Icon = tab.icon;
          return (
            <button
              key={tab.id}
              onClick={() => onSelectTab(tab.id)}
              data-testid={tab.testTag}
              className={`flex-1 py-1 flex flex-col items-center justify-center gap-1 rounded-2xl transition-all cursor-pointer ${
                isSelected
                  ? 'text-[#3F51B5]'
                  : 'text-slate-500 hover:text-slate-800'
              }`}
            >
              <div
                className={`px-4 py-1 rounded-full flex items-center justify-center transition-colors ${
                  isSelected ? 'bg-indigo-50 text-[#3F51B5]' : 'bg-transparent'
                }`}
              >
                <Icon className="w-5 h-5" />
              </div>
              <span
                className={`text-[11px] leading-tight ${
                  isSelected ? 'font-black text-[#3F51B5]' : 'font-semibold text-slate-500'
                }`}
              >
                {tab.label}
              </span>
            </button>
          );
        })}
      </div>
    </nav>
  );
};
