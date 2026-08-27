package com.example.ui

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary

enum class FolderType {
    ATTENDANCE,
    HOLDER,
    FEES,
    FACILITIES,
    BACKUP
}

@Composable
fun SettingsFoldersView(
    uiState: LibraryUiState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedFolderForDetail by remember { mutableStateOf<FolderType?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_folders_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Settings & Folders Directory Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = IndigoPrimary.copy(alpha = 0.12f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.FolderSpecial,
                                contentDescription = null,
                                tint = IndigoPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "LIBRARY DIRECTORY & FOLDERS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = IndigoPrimary
                        )
                        Text(
                            text = "Settings, Records & Controls",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "MANAGEMENT FOLDERS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        // Folder 1: Daily Attendance Folder
        item {
            FolderItemCard(
                folderName = "Daily Attendance Folder",
                hindiSubtitle = "दैनिक उपस्थिति रजिस्टर",
                itemCountText = "${uiState.attendanceLogs.size} Check-In Logs",
                description = "Daily punch-in, punch-out, student presence archives & date filters.",
                icon = Icons.Filled.HowToReg,
                badgeColor = Color(0xFF1E88E5),
                onOpen = { viewModel.selectTab(MainTab.ATTENDANCE) },
                onShowDetails = { selectedFolderForDetail = FolderType.ATTENDANCE }
            )
        }

        // Folder 2: Library Holder & Control Folder
        item {
            FolderItemCard(
                folderName = "Library Holder (Owner) Folder",
                hindiSubtitle = "लाइब्रेरी धारक एवं सम्पूर्ण नियंत्रण",
                itemCountText = "Admin Master Control",
                description = "Complete authority over library settings, capacity, owner profile & emergency actions.",
                icon = Icons.Filled.AdminPanelSettings,
                badgeColor = IndigoPrimary,
                onOpen = { viewModel.selectTab(MainTab.HOLDER) },
                onShowDetails = { selectedFolderForDetail = FolderType.HOLDER }
            )
        }

        // Folder 3: Monthly Fees & Accounts Folder
        item {
            FolderItemCard(
                folderName = "Monthly Fees & Accounts Folder",
                hindiSubtitle = "मासिक शुल्क (₹${uiState.monthlyFee}) व हिसाब",
                itemCountText = "${uiState.paidStudentsCount} Paid / ${uiState.pendingStudentsCount} Pending",
                description = "Track collected revenue (₹${uiState.totalRevenueCollected}) vs pending dues (₹${uiState.totalPendingRevenue}).",
                icon = Icons.Filled.LocalAtm,
                badgeColor = Color(0xFF2E7D32),
                onOpen = { viewModel.selectTab(MainTab.HOLDER) },
                onShowDetails = { selectedFolderForDetail = FolderType.FEES }
            )
        }

        // Folder 4: Facilities, Rules & Wi-Fi Folder
        item {
            FolderItemCard(
                folderName = "Facilities, Wi-Fi & Timings Folder",
                hindiSubtitle = "लाइब्रेरी सुविधाएं एवं नियम",
                itemCountText = "Timings: ${uiState.settings.openingHours}",
                description = "Wi-Fi credentials, air-conditioning silent zone rules, RO water & schedule.",
                icon = Icons.Filled.Wifi,
                badgeColor = Color(0xFFE65100),
                onOpen = { selectedFolderForDetail = FolderType.FACILITIES },
                onShowDetails = { selectedFolderForDetail = FolderType.FACILITIES }
            )
        }

        // Folder 5: Data Backup & Sharing Folder
        item {
            FolderItemCard(
                folderName = "Data Backup & Export Folder",
                hindiSubtitle = "डेटा बैकअप एवं शेयरिंग",
                itemCountText = "Cloud & Local Sync",
                description = "Export instant library attendance & seat status reports directly to WhatsApp.",
                icon = Icons.Filled.Share,
                badgeColor = Color(0xFF6A1B9A),
                onOpen = {
                    val report = buildString {
                        appendLine("📚 *${uiState.settings.libraryName}*")
                        appendLine("📍 ${uiState.settings.branchName} | Capacity: 100 Seats")
                        appendLine("👥 Occupied: ${uiState.occupiedCount} | Free: ${uiState.availableCount}")
                        appendLine("💰 Monthly Fee: ₹${uiState.monthlyFee}")
                        appendLine("📞 Contact: ${uiState.settings.ownerPhone}")
                    }
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, report)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Library Summary"))
                },
                onShowDetails = { selectedFolderForDetail = FolderType.BACKUP }
            )
        }

        // App Information Footer Card
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BHAGAT SINGH LIBRARY MALKHERA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = IndigoPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Version 2.0 • Full Holder Control & Attendance",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }

    // Detail Dialog for clicked folder
    selectedFolderForDetail?.let { folder ->
        FolderDetailDialog(
            folderType = folder,
            uiState = uiState,
            onDismiss = { selectedFolderForDetail = null },
            onNavigate = { tab ->
                selectedFolderForDetail = null
                viewModel.selectTab(tab)
            }
        )
    }
}

@Composable
fun FolderItemCard(
    folderName: String,
    hindiSubtitle: String,
    itemCountText: String,
    description: String,
    icon: ImageVector,
    badgeColor: Color,
    onOpen: () -> Unit,
    onShowDetails: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = badgeColor.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f)),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = folderName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = hindiSubtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Open",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = itemCountText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "Open Folder →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = badgeColor
                )
            }
        }
    }
}

@Composable
fun FolderDetailDialog(
    folderType: FolderType,
    uiState: LibraryUiState,
    onDismiss: () -> Unit,
    onNavigate: (MainTab) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = when (folderType) {
                    FolderType.ATTENDANCE -> "📁 DAILY ATTENDANCE FOLDER"
                    FolderType.HOLDER -> "📁 LIBRARY HOLDER CONTROL"
                    FolderType.FEES -> "📁 MONTHLY FEES & REVENUE"
                    FolderType.FACILITIES -> "📁 FACILITIES & WI-FI"
                    FolderType.BACKUP -> "📁 BACKUP & DATA"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = IndigoPrimary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                when (folderType) {
                    FolderType.ATTENDANCE -> {
                        Text(
                            text = "Total Recorded Logs: ${uiState.attendanceLogs.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Contains student check-in timestamps, check-out status, and historical presence archives.",
                            fontSize = 12.sp
                        )
                    }
                    FolderType.HOLDER -> {
                        Text(
                            text = "Owner: ${uiState.settings.ownerName}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Helpline: ${uiState.settings.ownerPhone}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Full administration over seat configuration, announcements, and master reset.",
                            fontSize = 12.sp
                        )
                    }
                    FolderType.FEES -> {
                        Text(
                            text = "Monthly Rate: ₹${uiState.monthlyFee}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Collected: ₹${uiState.totalRevenueCollected} (${uiState.paidStudentsCount} Students)",
                            fontSize = 13.sp,
                            color = Color(0xFF1B5E20),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pending: ₹${uiState.totalPendingRevenue} (${uiState.pendingStudentsCount} Students)",
                            fontSize = 13.sp,
                            color = Color(0xFFB71C1C),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    FolderType.FACILITIES -> {
                        Text(
                            text = "🕒 Timings: ${uiState.settings.openingHours}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "📶 Wi-Fi: ${uiState.settings.wifiPassword}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "📍 Address: ${uiState.settings.address}",
                            fontSize = 12.sp
                        )
                    }
                    FolderType.BACKUP -> {
                        Text(
                            text = "Active Seats: ${uiState.occupiedCount} / 100",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Data is automatically persisted offline in the local Room database.",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when (folderType) {
                        FolderType.ATTENDANCE -> onNavigate(MainTab.ATTENDANCE)
                        FolderType.HOLDER, FolderType.FEES -> onNavigate(MainTab.HOLDER)
                        FolderType.FACILITIES, FolderType.BACKUP -> onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (folderType in listOf(FolderType.FACILITIES, FolderType.BACKUP)) "OK" else "Go to Section",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
