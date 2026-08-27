package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.NoticeEntity
import com.example.data.model.SeatEntity
import com.example.data.model.SeatStatus
import com.example.ui.theme.AvailableCardBg
import com.example.ui.theme.AvailableCardBorder
import com.example.ui.theme.AvailableCardText
import com.example.ui.theme.AvailableGreenDark
import com.example.ui.theme.AvailableGreenLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.OccupiedCardBg
import com.example.ui.theme.OccupiedCardBorder
import com.example.ui.theme.OccupiedCardText
import com.example.ui.theme.OccupiedRedDark
import com.example.ui.theme.OccupiedRedLight
import com.example.ui.theme.ReservedAmberDark
import com.example.ui.theme.ReservedAmberLight
import com.example.ui.theme.ReservedCardBg
import com.example.ui.theme.ReservedCardBorder
import com.example.ui.theme.ReservedCardText
import com.example.ui.theme.TotalCardBg
import com.example.ui.theme.TotalCardBorder
import com.example.ui.theme.TotalCardText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = when (uiState.currentTab) {
                                MainTab.DASHBOARD -> uiState.settings.libraryName.uppercase()
                                MainTab.ATTENDANCE -> "DAILY ATTENDANCE"
                                MainTab.HOLDER -> "HOLDER CONTROL"
                                MainTab.SETTINGS -> "SETTINGS & FOLDERS"
                            },
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 19.sp,
                                letterSpacing = (-0.5).sp,
                                color = IndigoPrimary
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = when (uiState.currentTab) {
                                MainTab.DASHBOARD -> "${uiState.settings.branchName.uppercase()} • ${uiState.occupiedCount} SEATED"
                                MainTab.ATTENDANCE -> "STUDENT PRESENCE REGISTER"
                                MainTab.HOLDER -> "OWNER ADMINISTRATION"
                                MainTab.SETTINGS -> "FOLDERS & SYSTEM ARCHIVES"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 1.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(44.dp)
                            .shadow(2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), CircleShape)
                            .clickable { viewModel.openAddNoticeDialog() }
                            .testTag("top_bar_add_notice_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("library_bottom_navigation")
            ) {
                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.DASHBOARD,
                    onClick = { viewModel.selectTab(MainTab.DASHBOARD) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.GridView,
                            contentDescription = "Seats",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Seats",
                            fontWeight = if (uiState.currentTab == MainTab.DASHBOARD) FontWeight.Black else FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = IndigoPrimary,
                        selectedTextColor = IndigoPrimary,
                        indicatorColor = IndigoPrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_item_dashboard")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.ATTENDANCE,
                    onClick = { viewModel.selectTab(MainTab.ATTENDANCE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.HowToReg,
                            contentDescription = "Attendance",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Attendance",
                            fontWeight = if (uiState.currentTab == MainTab.ATTENDANCE) FontWeight.Black else FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = IndigoPrimary,
                        selectedTextColor = IndigoPrimary,
                        indicatorColor = IndigoPrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_item_attendance")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.HOLDER,
                    onClick = { viewModel.selectTab(MainTab.HOLDER) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AdminPanelSettings,
                            contentDescription = "Holder",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Holder",
                            fontWeight = if (uiState.currentTab == MainTab.HOLDER) FontWeight.Black else FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = IndigoPrimary,
                        selectedTextColor = IndigoPrimary,
                        indicatorColor = IndigoPrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_item_holder")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.SETTINGS,
                    onClick = { viewModel.selectTab(MainTab.SETTINGS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.FolderSpecial,
                            contentDescription = "Folders",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Folders",
                            fontWeight = if (uiState.currentTab == MainTab.SETTINGS) FontWeight.Black else FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = IndigoPrimary,
                        selectedTextColor = IndigoPrimary,
                        indicatorColor = IndigoPrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_item_settings")
                )
            }
        }
    ) { innerPadding ->
        when (uiState.currentTab) {
            MainTab.DASHBOARD -> {
                DashboardContentView(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            MainTab.ATTENDANCE -> {
                AttendanceView(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            MainTab.HOLDER -> {
                HolderControlView(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            MainTab.SETTINGS -> {
                SettingsFoldersView(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    // Seat Dialog
    if (uiState.showSeatDialog && uiState.selectedSeat != null) {
        SeatDetailsDialog(
            seat = uiState.selectedSeat!!,
            monthlyFee = uiState.monthlyFee,
            onDismiss = { viewModel.closeSeatDialog() },
            onSave = { seatNumber, name, mobile, inTime, outTime, feeStatus, isReserved ->
                viewModel.saveSeat(
                    seatNumber = seatNumber,
                    studentName = name,
                    mobileNumber = mobile,
                    inTime = inTime,
                    outTime = outTime,
                    feeStatus = feeStatus,
                    isReserved = isReserved
                )
            },
            onClear = { seatNumber ->
                viewModel.clearSeat(seatNumber)
            }
        )
    }

    // Add Notice Dialog
    if (uiState.showAddNoticeDialog) {
        AddNoticeDialog(
            onDismiss = { viewModel.closeAddNoticeDialog() },
            onAdd = { text -> viewModel.addNotice(text) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DashboardContentView(
    uiState: LibraryUiState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refresh() },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("dashboard_scroll_view"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
                // 1. Stats Row - 4 Column Bold Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BoldStatCard(
                            label = "TOTAL",
                            value = uiState.totalSeats,
                            backgroundColor = TotalCardBg,
                            borderColor = TotalCardBorder,
                            contentColor = TotalCardText,
                            isSelected = uiState.filter == SeatFilter.ALL,
                            onClick = { viewModel.onFilterChanged(SeatFilter.ALL) },
                            modifier = Modifier.weight(1f)
                        )
                        BoldStatCard(
                            label = "OCC",
                            value = uiState.occupiedCount,
                            backgroundColor = OccupiedCardBg,
                            borderColor = OccupiedCardBorder,
                            contentColor = OccupiedCardText,
                            isSelected = uiState.filter == SeatFilter.OCCUPIED,
                            onClick = {
                                viewModel.onFilterChanged(
                                    if (uiState.filter == SeatFilter.OCCUPIED) SeatFilter.ALL else SeatFilter.OCCUPIED
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        BoldStatCard(
                            label = "FREE",
                            value = uiState.availableCount,
                            backgroundColor = AvailableCardBg,
                            borderColor = AvailableCardBorder,
                            contentColor = AvailableCardText,
                            isSelected = uiState.filter == SeatFilter.AVAILABLE,
                            onClick = {
                                viewModel.onFilterChanged(
                                    if (uiState.filter == SeatFilter.AVAILABLE) SeatFilter.ALL else SeatFilter.AVAILABLE
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        BoldStatCard(
                            label = "RES",
                            value = uiState.reservedCount,
                            backgroundColor = ReservedCardBg,
                            borderColor = ReservedCardBorder,
                            contentColor = ReservedCardText,
                            isSelected = uiState.filter == SeatFilter.RESERVED,
                            onClick = {
                                viewModel.onFilterChanged(
                                    if (uiState.filter == SeatFilter.RESERVED) SeatFilter.ALL else SeatFilter.RESERVED
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 2. Monthly Fee Card - Bold Indigo Hero Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(24.dp), ambientColor = IndigoPrimary.copy(alpha = 0.2f), spotColor = IndigoPrimary.copy(alpha = 0.3f))
                            .testTag("monthly_fee_card"),
                        colors = CardDefaults.cardColors(
                            containerColor = IndigoPrimary
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CurrencyRupee,
                                        contentDescription = "Fee",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Monthly Library Fee",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            letterSpacing = (-0.2).sp
                                        )
                                    )
                                    Text(
                                        text = "Standard per seat monthly admission",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                            Text(
                                text = "₹${uiState.monthlyFee}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 28.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                // 3. Search & Filter Bar
                item {
                    val focusManager = LocalFocusManager.current
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("seat_search_input"),
                            placeholder = {
                                Text(
                                    "Search seat #, student name, or phone...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = IndigoPrimary
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear search"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = uiState.filter == SeatFilter.ALL,
                                onClick = { viewModel.onFilterChanged(SeatFilter.ALL) },
                                label = {
                                    Text(
                                        "All (${uiState.totalSeats})",
                                        fontWeight = if (uiState.filter == SeatFilter.ALL) FontWeight.Black else FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TotalCardBg,
                                    selectedLabelColor = TotalCardText
                                )
                            )
                            FilterChip(
                                selected = uiState.filter == SeatFilter.AVAILABLE,
                                onClick = { viewModel.onFilterChanged(SeatFilter.AVAILABLE) },
                                label = {
                                    Text(
                                        "Free (${uiState.availableCount})",
                                        fontWeight = if (uiState.filter == SeatFilter.AVAILABLE) FontWeight.Black else FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AvailableCardBg,
                                    selectedLabelColor = AvailableCardText
                                )
                            )
                            FilterChip(
                                selected = uiState.filter == SeatFilter.OCCUPIED,
                                onClick = { viewModel.onFilterChanged(SeatFilter.OCCUPIED) },
                                label = {
                                    Text(
                                        "Occ (${uiState.occupiedCount})",
                                        fontWeight = if (uiState.filter == SeatFilter.OCCUPIED) FontWeight.Black else FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OccupiedCardBg,
                                    selectedLabelColor = OccupiedCardText
                                )
                            )
                            FilterChip(
                                selected = uiState.filter == SeatFilter.RESERVED,
                                onClick = { viewModel.onFilterChanged(SeatFilter.RESERVED) },
                                label = {
                                    Text(
                                        "Res (${uiState.reservedCount})",
                                        fontWeight = if (uiState.filter == SeatFilter.RESERVED) FontWeight.Black else FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ReservedCardBg,
                                    selectedLabelColor = ReservedCardText
                                )
                            )
                        }
                    }
                }

                // 4. Live Seat Map Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Live Seat Map",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 19.sp,
                                letterSpacing = (-0.3).sp
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MiniLegendDot(color = AvailableGreenLight, label = "Free")
                            MiniLegendDot(color = OccupiedRedLight, label = "Busy")
                            MiniLegendDot(color = ReservedAmberLight, label = "Res")
                        }
                    }
                }

                // 5. Seat Grid
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            if (uiState.filteredSeats.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No seats match your search or filter",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            } else {
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("seat_map_wrap"),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    uiState.filteredSeats.forEach { seat ->
                                        BoldSeatTile(
                                            seat = seat,
                                            onClick = { viewModel.openSeat(seat) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 6. Notifications Section Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Announcements",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 19.sp,
                                letterSpacing = (-0.3).sp
                            )
                        )
                        TextButton(
                            onClick = { viewModel.openAddNoticeDialog() },
                            modifier = Modifier.testTag("add_notice_icon_button")
                        ) {
                            Text(
                                text = "+ Add Notice",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = IndigoPrimary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                // 7. Notice List
                if (uiState.notices.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "No announcements yet. Tap '+ Add Notice' to broadcast.",
                                modifier = Modifier.padding(18.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                } else {
                    items(uiState.notices, key = { it.id }) { notice ->
                        BoldNoticeCard(
                            notice = notice,
                            onDelete = { viewModel.deleteNotice(notice) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

@Composable
fun BoldStatCard(
    label: String,
    value: Int,
    backgroundColor: Color,
    borderColor: Color,
    contentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("stat_card_${label.lowercase()}")
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) contentColor else borderColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    color = contentColor.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            val displayValue = if (value < 10 && value >= 0) "0$value" else "$value"
            Text(
                text = displayValue,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    lineHeight = 24.sp,
                    color = contentColor
                )
            )
        }
    }
}

@Composable
fun BoldSeatTile(
    seat: SeatEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (seat.status) {
        SeatStatus.RESERVED -> ReservedAmberLight
        SeatStatus.OCCUPIED -> OccupiedRedLight
        SeatStatus.AVAILABLE -> AvailableGreenLight
    }

    val textColor = when (seat.status) {
        SeatStatus.RESERVED -> ReservedAmberDark
        SeatStatus.OCCUPIED -> OccupiedRedDark
        SeatStatus.AVAILABLE -> AvailableGreenDark
    }

    val formattedNumber = if (seat.seatNumber < 10) "0${seat.seatNumber}" else "${seat.seatNumber}"

    Box(
        modifier = modifier
            .size(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, textColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag("seat_tile_${seat.seatNumber}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formattedNumber,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = textColor,
                    fontSize = 14.sp,
                    letterSpacing = (-0.3).sp
                )
            )
            if (seat.isReserved) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Reserved",
                    tint = textColor,
                    modifier = Modifier.size(10.dp)
                )
            } else if (seat.isOccupied && seat.studentName.isNotBlank()) {
                Text(
                    text = seat.studentName.take(3),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun MiniLegendDot(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun BoldNoticeCard(
    notice: NoticeEntity,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(notice.timestamp) {
        dateFormat.format(Date(notice.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .testTag("notice_card_${notice.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notice.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete notification",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatDetailsDialog(
    seat: SeatEntity,
    monthlyFee: Int,
    onDismiss: () -> Unit,
    onSave: (seatNumber: Int, studentName: String, mobileNumber: String, inTime: String, outTime: String, feeStatus: String, isReserved: Boolean) -> Unit,
    onClear: (seatNumber: Int) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var studentName by remember(seat) { mutableStateOf(seat.studentName) }
    var mobileNumber by remember(seat) { mutableStateOf(seat.mobileNumber) }
    var inTime by remember(seat) { mutableStateOf(seat.inTime) }
    var outTime by remember(seat) { mutableStateOf(seat.outTime) }
    var feeStatus by remember(seat) { mutableStateOf(seat.feeStatus) }
    var isReserved by remember(seat) { mutableStateOf(seat.isReserved) }

    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Seat ${if (seat.seatNumber < 10) "0${seat.seatNumber}" else "${seat.seatNumber}"}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                )
                val statusText = when {
                    isReserved -> "Reserved"
                    studentName.isNotBlank() && outTime.isBlank() -> "Occupied"
                    else -> "Available"
                }
                val statusBg = when {
                    isReserved -> ReservedAmberLight
                    studentName.isNotBlank() && outTime.isBlank() -> OccupiedRedLight
                    else -> AvailableGreenLight
                }
                val statusTextCol = when {
                    isReserved -> ReservedAmberDark
                    studentName.isNotBlank() && outTime.isBlank() -> OccupiedRedDark
                    else -> AvailableGreenDark
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = statusTextCol,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Student Name
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Name", fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Student Name"
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seat_dialog_student_name_input")
                )

                // Mobile Number
                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    label = { Text("Mobile Number", fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Mobile"
                        )
                    },
                    trailingIcon = {
                        if (mobileNumber.isNotBlank()) {
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$mobileNumber")
                                }
                                context.startActivity(intent)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Call",
                                    tint = IndigoPrimary
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seat_dialog_mobile_input")
                )

                // In Time
                OutlinedTextField(
                    value = inTime,
                    onValueChange = { inTime = it },
                    label = { Text("In Time", fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = "In Time"
                        )
                    },
                    trailingIcon = {
                        TextButton(onClick = {
                            inTime = timeFormatter.format(Date())
                        }) {
                            Text("Now", fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seat_dialog_in_time_input")
                )

                // Out Time
                OutlinedTextField(
                    value = outTime,
                    onValueChange = { outTime = it },
                    label = { Text("Out Time", fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Out Time"
                        )
                    },
                    trailingIcon = {
                        Row {
                            if (outTime.isNotBlank()) {
                                IconButton(onClick = { outTime = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear out time",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            TextButton(onClick = {
                                outTime = timeFormatter.format(Date())
                            }) {
                                Text("Now", fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seat_dialog_out_time_input")
                )

                // Reserved Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isReserved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isReserved) ReservedAmberDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reserve Seat",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Switch(
                        checked = isReserved,
                        onCheckedChange = { isReserved = it },
                        modifier = Modifier.testTag("seat_dialog_reserved_switch")
                    )
                }

                // Fee Status Segmented Buttons
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Monthly Fee (₹$monthlyFee)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black)
                    )
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = feeStatus == "Paid",
                            onClick = { feeStatus = "Paid" },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            icon = { SegmentedButtonDefaults.Icon(active = feeStatus == "Paid") }
                        ) {
                            Text("Paid (₹$monthlyFee)", fontWeight = FontWeight.Bold)
                        }
                        SegmentedButton(
                            selected = feeStatus == "Pending",
                            onClick = { feeStatus = "Pending" },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            icon = { SegmentedButtonDefaults.Icon(active = feeStatus == "Pending") }
                        ) {
                            Text("Pending", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        seat.seatNumber,
                        studentName,
                        mobileNumber,
                        inTime,
                        outTime,
                        feeStatus,
                        isReserved
                    )
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("seat_dialog_save_button")
            ) {
                Text("Save", fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (seat.studentName.isNotBlank() || seat.isReserved || seat.inTime.isNotBlank()) {
                    OutlinedButton(
                        onClick = { onClear(seat.seatNumber) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.testTag("seat_dialog_clear_button")
                    ) {
                        Text("Vacate", fontWeight = FontWeight.Bold)
                    }
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("seat_dialog_cancel_button")
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun AddNoticeDialog(
    onDismiss: () -> Unit,
    onAdd: (text: String) -> Unit
) {
    var noticeText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "नई Notification",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = noticeText,
                    onValueChange = { noticeText = it },
                    placeholder = { Text("Notification लिखें...", fontWeight = FontWeight.Medium) },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_notice_text_field")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (noticeText.isNotBlank()) {
                        onAdd(noticeText)
                    }
                },
                enabled = noticeText.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_notice_confirm_button")
            ) {
                Text("Add", fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("add_notice_cancel_button")
            ) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }
        }
    )
}

