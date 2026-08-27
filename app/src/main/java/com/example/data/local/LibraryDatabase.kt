package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AttendanceLog
import com.example.data.model.LibrarySettingsEntity
import com.example.data.model.NoticeEntity
import com.example.data.model.SeatEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        SeatEntity::class,
        NoticeEntity::class,
        AttendanceLog::class,
        LibrarySettingsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LibraryDatabase : RoomDatabase() {

    abstract fun seatDao(): SeatDao
    abstract fun noticeDao(): NoticeDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    val defaultSeats = (1..100).map { seatNum ->
                                        SeatEntity(seatNumber = seatNum)
                                    }
                                    database.seatDao().insertAllSeats(defaultSeats)

                                    val defaultNotices = listOf(
                                        NoticeEntity(
                                            text = "Bhagat Singh Library Malkhera में आपका स्वागत है।"
                                        ),
                                        NoticeEntity(
                                            text = "Monthly fee ₹400 है।"
                                        )
                                    )
                                    database.noticeDao().insertAllNotices(defaultNotices)

                                    // Default Settings
                                    database.settingsDao().insertOrUpdateSettings(
                                        LibrarySettingsEntity()
                                    )
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
