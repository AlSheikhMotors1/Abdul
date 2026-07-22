package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchoolNotice
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolUpdatesScreen(
    notices: List<SchoolNotice>,
    onBackClick: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("ALL") }

    val categories = listOf(
        "ALL" to "All Updates",
        "EMERGENCY_NOTICES" to "Emergency Notices",
        "SCHOOL_TIMING" to "School Timing",
        "HOLIDAYS" to "Holidays",
        "NOTICES" to "Notices",
        "WEEKLY_UPDATES" to "Weekly Updates",
        "MONTHLY_EVENTS" to "Monthly Events",
        "ANNUAL_FUNCTIONS" to "Annual Functions",
        "EXAM_SCHEDULE" to "Exam Schedule",
        "PARENT_MEETINGS" to "Parent Meetings"
    )

    val filteredNotices = remember(notices, selectedCategory) {
        if (selectedCategory == "ALL") notices
        else notices.filter { it.category == selectedCategory }
    }

    val emergencyNotice = remember(notices) {
        notices.firstOrNull { it.isEmergency }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("1. School Updates", fontWeight = FontWeight.Bold, color = SunriseNavy)
                        Text("Official Announcements & Timelines", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SunriseNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
        ) {
            // Category Selector Chips Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { (code, label) ->
                    val isSelected = selectedCategory == code
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = code },
                        label = {
                            Text(
                                text = label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        leadingIcon = if (code == "EMERGENCY_NOTICES") {
                            { Icon(Icons.Default.Warning, contentDescription = null, tint = StatusRed, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SunriseNavy,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceLight,
                            labelColor = SunriseNavy
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Featured Emergency Notice Card
                if (emergencyNotice != null && (selectedCategory == "ALL" || selectedCategory == "EMERGENCY_NOTICES")) {
                    item {
                        EmergencyAlertBanner(notice = emergencyNotice)
                    }
                }

                if (filteredNotices.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusGreen,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No updates in this category",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    items(filteredNotices) { notice ->
                        NoticeCard(notice = notice)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmergencyAlertBanner(notice: SchoolNotice) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = StatusRedContainer,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, StatusRed),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(StatusRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PriorityHigh,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "🚨 EMERGENCY NOTICE",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = StatusRed
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = notice.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = StatusRed
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notice.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7F1D1D)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = notice.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF991B1B)
            )
        }
    }
}

@Composable
private fun NoticeCard(notice: SchoolNotice) {
    val categoryIcon = when (notice.category) {
        "SCHOOL_TIMING" -> Icons.Outlined.Schedule
        "HOLIDAYS" -> Icons.Outlined.BeachAccess
        "EXAM_SCHEDULE" -> Icons.Outlined.Assignment
        "PARENT_MEETINGS" -> Icons.Outlined.Groups
        "ANNUAL_FUNCTIONS" -> Icons.Outlined.Celebration
        "MONTHLY_EVENTS" -> Icons.Outlined.Event
        else -> Icons.Outlined.Campaign
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceLight,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SunriseGoldContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = null,
                    tint = SunriseNavy,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SurfaceVariantLight
                    ) {
                        Text(
                            text = notice.category.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunriseNavy,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = notice.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = notice.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notice.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF475569)
                )
            }
        }
    }
}
