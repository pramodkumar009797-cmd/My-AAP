import { SeatEntity, NoticeEntity, AttendanceLog, LibrarySettingsEntity, SeatStatus } from '../types';

const SEATS_KEY = 'bsl_seats_v2';
const NOTICES_KEY = 'bsl_notices_v2';
const ATTENDANCE_KEY = 'bsl_attendance_v2';
const SETTINGS_KEY = 'bsl_settings_v2';

export function isSeatOccupied(seat: SeatEntity): boolean {
  return seat.studentName.trim().length > 0 && seat.outTime.trim().length === 0;
}

export function getSeatStatus(seat: SeatEntity): SeatStatus {
  if (seat.isReserved) return SeatStatus.RESERVED;
  if (isSeatOccupied(seat)) return SeatStatus.OCCUPIED;
  return SeatStatus.AVAILABLE;
}

export function isLogInside(log: AttendanceLog): boolean {
  return (
    log.outTime.trim().length === 0 &&
    log.status.toLowerCase() !== 'checked out' &&
    log.status.toLowerCase() !== 'departed'
  );
}

export function getTodayDateString(): string {
  const d = new Date();
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

export function getCurrentTimeString(): string {
  return new Date().toLocaleTimeString('en-US', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: true,
  });
}

export function formatDateForDisplay(dateStr: string): string {
  try {
    const [y, m, d] = dateStr.split('-').map(Number);
    const date = new Date(y, m - 1, d);
    return date.toLocaleDateString('en-US', {
      day: 'numeric',
      month: 'long',
      year: 'numeric',
      weekday: 'long',
    });
  } catch {
    return dateStr;
  }
}

export function formatTimestamp(ts: number): string {
  const date = new Date(ts);
  return date.toLocaleDateString('en-US', {
    day: '2-digit',
    month: 'short',
    hour: '2-digit',
    minute: '2-digit',
    hour12: true,
  });
}

// Initial default settings
export const DEFAULT_SETTINGS: LibrarySettingsEntity = {
  id: 1,
  libraryName: 'Bhagat Singh Library',
  branchName: 'Malkhera',
  ownerName: 'Library Holder / Owner',
  ownerPhone: '+91 98765 43210',
  monthlyFee: 400,
  totalCapacity: 100,
  openingHours: '06:00 AM - 10:00 PM',
  wifiPassword: 'BSL@Malkhera2026',
  pinProtectionEnabled: false,
  ownerPin: '1234',
  address: 'Near Main Chowk, Malkhera, Rajasthan',
};

// Storage Loaders
export function loadSeats(): SeatEntity[] {
  try {
    const raw = localStorage.getItem(SEATS_KEY);
    if (raw) {
      const parsed = JSON.parse(raw);
      if (Array.isArray(parsed) && parsed.length === 100) {
        return parsed;
      }
    }
  } catch (e) {
    console.error('Failed to load seats from localStorage', e);
  }

  // Create 100 default empty seats
  const defaultSeats: SeatEntity[] = Array.from({ length: 100 }, (_, i) => ({
    seatNumber: i + 1,
    studentName: '',
    mobileNumber: '',
    inTime: '',
    outTime: '',
    feeStatus: 'Pending',
    isReserved: false,
    updatedTimestamp: Date.now(),
  }));

  saveSeats(defaultSeats);
  return defaultSeats;
}

export function saveSeats(seats: SeatEntity[]) {
  try {
    localStorage.setItem(SEATS_KEY, JSON.stringify(seats));
  } catch (e) {
    console.error('Failed to save seats to localStorage', e);
  }
}

export function loadNotices(): NoticeEntity[] {
  try {
    const raw = localStorage.getItem(NOTICES_KEY);
    if (raw) {
      const parsed = JSON.parse(raw);
      if (Array.isArray(parsed) && parsed.length > 0) {
        return parsed;
      }
    }
  } catch (e) {
    console.error('Failed to load notices', e);
  }

  const defaultNotices: NoticeEntity[] = [
    {
      id: 1,
      text: 'Bhagat Singh Library Malkhera में आपका स्वागत है।',
      timestamp: Date.now() - 3600000 * 2,
    },
    {
      id: 2,
      text: 'Monthly admission fee ₹400 है। कृपया समय पर जमा कराएं।',
      timestamp: Date.now() - 3600000,
    },
  ];

  saveNotices(defaultNotices);
  return defaultNotices;
}

export function saveNotices(notices: NoticeEntity[]) {
  try {
    localStorage.setItem(NOTICES_KEY, JSON.stringify(notices));
  } catch (e) {
    console.error('Failed to save notices to localStorage', e);
  }
}

export function loadAttendanceLogs(): AttendanceLog[] {
  try {
    const raw = localStorage.getItem(ATTENDANCE_KEY);
    if (raw) {
      const parsed = JSON.parse(raw);
      if (Array.isArray(parsed)) {
        return parsed;
      }
    }
  } catch (e) {
    console.error('Failed to load attendance logs', e);
  }

  return [];
}

export function saveAttendanceLogs(logs: AttendanceLog[]) {
  try {
    localStorage.setItem(ATTENDANCE_KEY, JSON.stringify(logs));
  } catch (e) {
    console.error('Failed to save attendance logs to localStorage', e);
  }
}

export function loadSettings(): LibrarySettingsEntity {
  try {
    const raw = localStorage.getItem(SETTINGS_KEY);
    if (raw) {
      const parsed = JSON.parse(raw);
      if (parsed && typeof parsed === 'object' && parsed.libraryName) {
        return { ...DEFAULT_SETTINGS, ...parsed };
      }
    }
  } catch (e) {
    console.error('Failed to load settings', e);
  }

  saveSettings(DEFAULT_SETTINGS);
  return DEFAULT_SETTINGS;
}

export function saveSettings(settings: LibrarySettingsEntity) {
  try {
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
  } catch (e) {
    console.error('Failed to save settings to localStorage', e);
  }
}
