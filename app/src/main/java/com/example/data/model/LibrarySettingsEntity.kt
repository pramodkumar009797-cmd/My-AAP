package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_settings")
data class LibrarySettingsEntity(
    @PrimaryKey val id: Int = 1,
    val libraryName: String = "Bhagat Singh Library",
    val branchName: String = "Malkhera",
    val ownerName: String = "Library Holder / Owner",
    val ownerPhone: String = "+91 98765 43210",
    val monthlyFee: Int = 400,
    val totalCapacity: Int = 100,
    val openingHours: String = "06:00 AM - 10:00 PM",
    val wifiPassword: String = "BSL@Malkhera2026",
    val pinProtectionEnabled: Boolean = false,
    val ownerPin: String = "1234",
    val address: String = "Near Main Chowk, Malkhera, Rajasthan"
)
