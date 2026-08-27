package com.example.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LibrarySettingsEntity
import com.example.data.model.SeatEntity
import com.example.ui.theme.IndigoPrimary

@Composable
fun HolderControlView(
    uiState: LibraryUiState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showBroadcastNoticeDialog by remember { mutableStateOf(false) }

    val activeStudents = uiState.seats.filter { it.studentName.trim().isNotEmpty() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("holder_control_view_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Library Holder & Owner Admin Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = IndigoPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("holder_admin_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.25f),
                                modifier = Modifier.size(52.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "LIBRARY HOLDER / OWNER",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = uiState.settings.ownerName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "${uiState.settings.libraryName} (${uiState.settings.branchName})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .size(36.dp)
                                .testTag("edit_holder_profile_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "OWNER HELPLINE / CONTACT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                            Text(
                                text = uiState.settings.ownerPhone,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${uiState.settings.ownerPhone}")
                                        }
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = IndigoPrimary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 2. Financial & Revenue Control Matrix
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalAtm,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "FEES & REVENUE MATRIX",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IndigoPrimary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "₹${uiState.monthlyFee}/month",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = IndigoPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Collected Card
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            border = BorderStroke(1.dp, Color(0xFFA5D6A7))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "COLLECTED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "₹${uiState.totalRevenueCollected}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "${uiState.paidStudentsCount} Students Paid",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }

                        // Pending Card
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            border = BorderStroke(1.dp, Color(0xFFFFCDD2))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "PENDING DUES",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFB71C1C)
                                )
                                Text(
                                    text = "₹${uiState.totalPendingRevenue}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFB71C1C)
                                )
                                Text(
                                    text = "${uiState.pendingStudentsCount} Pending",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC62828)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Quick Action Buttons for Owner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "HOLDER COMMAND CONTROLS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                        color = IndigoPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showBroadcastNoticeDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Campaign,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Post Notice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val report = buildString {
                                    appendLine("📋 *${uiState.settings.libraryName} - Status Report*")
                                    appendLine("📍 Branch: ${uiState.settings.branchName}")
                                    appendLine("🪑 Total Capacity: ${uiState.totalSeats} Seats")
                                    appendLine("👥 Occupied: ${uiState.occupiedCount} | Free: ${uiState.availableCount}")
                                    appendLine("💰 Monthly Fee: ₹${uiState.monthlyFee}")
                                    appendLine("💵 Collected: ₹${uiState.totalRevenueCollected} | Pending: ₹${uiState.totalPendingRevenue}")
                                    appendLine("📞 Owner Contact: ${uiState.settings.ownerPhone}")
                                }
                                clipboardManager.setText(AnnotatedString(report))
                                try {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, report)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Library Report"))
                                } catch (_: Exception) {}
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 4. Student Fee Status & Direct Management List
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STUDENT FEE REGISTER (${activeStudents.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (activeStudents.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No students currently seated or registered",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(activeStudents, key = { it.seatNumber }) { seat ->
                StudentFeeItemCard(
                    seat = seat,
                    monthlyFee = uiState.monthlyFee,
                    onToggleFeeStatus = {
                        val newStatus = if (seat.feeStatus.equals("Paid", ignoreCase = true)) "Pending" else "Paid"
                        viewModel.saveSeat(
                            seatNumber = seat.seatNumber,
                            studentName = seat.studentName,
                            mobileNumber = seat.mobileNumber,
                            inTime = seat.inTime,
                            outTime = seat.outTime,
                            feeStatus = newStatus,
                            isReserved = seat.isReserved
                        )
                    }
                )
            }
        }

        // 5. Danger Zone / Reset Control
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE).copy(alpha = 0.7f)
                ),
                border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = null,
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "HOLDER MASTER RESET",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFB71C1C)
                        )
                    }

                    Text(
                        text = "Reset all 100 seats to empty when starting a new session or clearing all records.",
                        fontSize = 12.sp,
                        color = Color(0xFF5D1010)
                    )

                    Button(
                        onClick = { viewModel.openResetConfirmation() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC62828)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RestartAlt,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset All 100 Seats", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Dialog: Edit Holder Profile & Settings
    if (showEditProfileDialog) {
        EditHolderProfileDialog(
            currentSettings = uiState.settings,
            onDismiss = { showEditProfileDialog = false },
            onSave = { libName, branch, owner, phone, fee, hours, wifi, pin, pinEnabled, address ->
                viewModel.updateLibrarySettings(
                    libraryName = libName,
                    branchName = branch,
                    ownerName = owner,
                    ownerPhone = phone,
                    monthlyFee = fee,
                    openingHours = hours,
                    wifiPassword = wifi,
                    pinProtectionEnabled = pinEnabled,
                    ownerPin = pin,
                    address = address
                )
                showEditProfileDialog = false
            }
        )
    }

    // Dialog: Broadcast Notice
    if (showBroadcastNoticeDialog) {
        var noticeText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showBroadcastNoticeDialog = false },
            title = {
                Text(
                    text = "POST OWNER ANNOUNCEMENT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = IndigoPrimary
                )
            },
            text = {
                OutlinedTextField(
                    value = noticeText,
                    onValueChange = { noticeText = it },
                    label = { Text("Announcement message *") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noticeText.isNotBlank()) {
                            viewModel.addNotice(noticeText)
                            showBroadcastNoticeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Post Now", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBroadcastNoticeDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Reset Confirmation Dialog
    if (uiState.showResetConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.closeResetConfirmation() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "CONFIRM SEAT RESET",
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFB71C1C),
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to clear all occupied and reserved seats? All 100 seats will become Available. This cannot be undone.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetAllLibrarySeats() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Yes, Reset All", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeResetConfirmation() }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun StudentFeeItemCard(
    seat: SeatEntity,
    monthlyFee: Int,
    onToggleFeeStatus: () -> Unit
) {
    val context = LocalContext.current
    val isPaid = seat.feeStatus.equals("Paid", ignoreCase = true)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isPaid) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                border = BorderStroke(1.dp, if (isPaid) Color(0xFFA5D6A7) else Color(0xFFFFCDD2)),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (seat.seatNumber < 10) "0${seat.seatNumber}" else "${seat.seatNumber}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isPaid) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = seat.studentName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (seat.mobileNumber.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${seat.mobileNumber}")
                                    }
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = "Call",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = seat.mobileNumber,
                            fontSize = 12.sp,
                            color = IndigoPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Toggle Fee Button
            Button(
                onClick = onToggleFeeStatus,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPaid) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    contentColor = if (isPaid) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = if (isPaid) Icons.Filled.CheckCircle else Icons.Filled.Pending,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isPaid) "Paid (₹$monthlyFee)" else "Pending",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EditHolderProfileDialog(
    currentSettings: LibrarySettingsEntity,
    onDismiss: () -> Unit,
    onSave: (
        libraryName: String,
        branchName: String,
        ownerName: String,
        ownerPhone: String,
        monthlyFee: Int,
        openingHours: String,
        wifiPassword: String,
        ownerPin: String,
        pinProtectionEnabled: Boolean,
        address: String
    ) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var libraryName by remember { mutableStateOf(currentSettings.libraryName) }
    var branchName by remember { mutableStateOf(currentSettings.branchName) }
    var ownerName by remember { mutableStateOf(currentSettings.ownerName) }
    var ownerPhone by remember { mutableStateOf(currentSettings.ownerPhone) }
    var monthlyFeeStr by remember { mutableStateOf(currentSettings.monthlyFee.toString()) }
    var openingHours by remember { mutableStateOf(currentSettings.openingHours) }
    var wifiPassword by remember { mutableStateOf(currentSettings.wifiPassword) }
    var ownerPin by remember { mutableStateOf(currentSettings.ownerPin) }
    var pinProtectionEnabled by remember { mutableStateOf(currentSettings.pinProtectionEnabled) }
    var address by remember { mutableStateOf(currentSettings.address) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "HOLDER & LIBRARY SETTINGS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = IndigoPrimary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    label = { Text("Holder / Owner Name") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ownerPhone,
                    onValueChange = { ownerPhone = it },
                    label = { Text("Owner Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = libraryName,
                        onValueChange = { libraryName = it },
                        label = { Text("Library Name") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.weight(1.3f)
                    )

                    OutlinedTextField(
                        value = branchName,
                        onValueChange = { branchName = it },
                        label = { Text("Branch") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = monthlyFeeStr,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) monthlyFeeStr = it
                    },
                    label = { Text("Monthly Fee (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = openingHours,
                    onValueChange = { openingHours = it },
                    label = { Text("Operating Timings") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = wifiPassword,
                    onValueChange = { wifiPassword = it },
                    label = { Text("Wi-Fi Password") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fee = monthlyFeeStr.toIntOrNull() ?: 400
                    onSave(
                        libraryName,
                        branchName,
                        ownerName,
                        ownerPhone,
                        fee,
                        openingHours,
                        wifiPassword,
                        ownerPin,
                        pinProtectionEnabled,
                        address
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Settings", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
