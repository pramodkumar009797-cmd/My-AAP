export enum MainTab {
  DASHBOARD = 'DASHBOARD',
  ATTENDANCE = 'ATTENDANCE',
  HOLDER = 'HOLDER',
  SETTINGS = 'SETTINGS',
}

export enum SeatFilter {
  ALL = 'ALL',
  AVAILABLE = 'AVAILABLE',
  OCCUPIED = 'OCCUPIED',
  RESERVED = 'RESERVED',
}

export enum AttendanceFilter {
  ALL = 'ALL',
  INSIDE = 'INSIDE',
  CHECKED_OUT = 'CHECKED_OUT',
}

export enum SeatStatus {
  AVAILABLE = 'AVAILABLE',
  OCCUPIED = 'OCCUPIED',
  RESERVED = 'RESERVED',
}

export interface SeatEntity {
  seatNumber: number;
  studentName: string;
  mobileNumber: string;
  inTime: string;
  outTime: string;
  feeStatus: 'Paid' | 'Pending' | string;
  isReserved: boolean;
  updatedTimestamp: number;
}

export interface NoticeEntity {
  id: number;
  text: string;
  timestamp: number;
}

export interface AttendanceLog {
  id: number;
  date: string; // YYYY-MM-DD
  studentName: string;
  seatNumber: number;
  mobileNumber: string;
  inTime: string;
  outTime: string;
  status: string; // "Checked In", "Checked Out", "Present"
  notes: string;
  timestamp: number;
}

export interface LibrarySettingsEntity {
  id: number;
  libraryName: string;
  branchName: string;
  ownerName: string;
  ownerPhone: string;
  monthlyFee: number;
  totalCapacity: number;
  openingHours: string;
  wifiPassword: string;
  pinProtectionEnabled: boolean;
  ownerPin: string;
  address: string;
}

export enum FolderType {
  ATTENDANCE = 'ATTENDANCE',
  HOLDER = 'HOLDER',
  FEES = 'FEES',
  FACILITIES = 'FACILITIES',
  BACKUP = 'BACKUP',
}
