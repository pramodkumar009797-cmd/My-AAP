import React, { useState } from 'react';
import { X, Megaphone } from 'lucide-react';

interface AddNoticeDialogProps {
  isOpen: boolean;
  onClose: () => void;
  onAddNotice: (text: string) => void;
}

export const AddNoticeDialog: React.FC<AddNoticeDialogProps> = ({
  isOpen,
  onClose,
  onAddNotice,
}) => {
  if (!isOpen) return null;

  const [text, setText] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (text.trim()) {
      onAddNotice(text.trim());
      setText('');
      onClose();
    }
  };

  return (
    <div
      data-testid="add_notice_dialog"
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-xs"
    >
      <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-200 space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-[#3F51B5]">
              <Megaphone className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-black text-slate-800 tracking-tight">
                Post Announcement
              </h2>
              <p className="text-xs text-slate-500 font-medium">
                Broadcast notice to all library students
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

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-1.5">
            <textarea
              value={text}
              onChange={(e) => setText(e.target.value)}
              placeholder="e.g. लाइब्रेरी का समय कल सुबह 6:00 बजे से रहेगा..."
              rows={4}
              data-testid="notice_input"
              className="w-full p-3.5 bg-slate-50 border border-slate-200 rounded-2xl text-sm font-medium focus:bg-white focus:outline-none focus:border-[#3F51B5]"
              autoFocus
            />
          </div>

          <div className="flex items-center gap-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-3 rounded-2xl bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={!text.trim()}
              data-testid="confirm_add_notice_button"
              className="flex-1 py-3 rounded-2xl bg-[#3F51B5] hover:bg-indigo-700 disabled:opacity-50 text-white font-black text-xs shadow-xs transition-colors cursor-pointer"
            >
              Post Notice
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
