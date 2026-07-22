package com.example.ui.screens.subscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.Student
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceReportScreen(
    student: Student,
    records: List<AttendanceRecord>,
    onBackClick: () -> Unit
) {
    val presentCount = records.count { it.status == "PRESENT" }
    val absentCount = records.count { it.status == "ABSENT" }
    val leaveCount = records.count { it.status == "LEAVE" }
    val totalDays = records.size.coerceAtLeast(1)
    val percentage = (presentCount.toDouble() / totalDays * 100).toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance Report", fontWeight = FontWeight.Bold, color = SunriseNavy) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SunriseNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Percentage Header Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SunriseNavy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${student.name} - Attendance",
                            style = MaterialTheme.typography.titleMedium,
                            color = SunriseGoldLight,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 44.sp
                        )

                        Text(
                            text = "Overall Attendance Percentage",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AttendanceCountChip(label = "Present", count = presentCount, color = StatusGreen)
                            AttendanceCountChip(label = "Absent", count = absentCount, color = StatusRed)
                            AttendanceCountChip(label = "Leave", count = leaveCount, color = SunriseGold)
                        }
                    }
                }
            }

            // Monthly Summary Card
            item {
                SummarySectionCard(
                    title = "📅 Monthly Summary (July 2026)",
                    totalDays = 24,
                    presentDays = 22,
                    absentDays = 1,
                    leaveDays = 1,
                    percentage = 91
                )
            }

            // Yearly Summary Card
            item {
                SummarySectionCard(
                    title = "🏫 Yearly Summary (Academic Session 2026)",
                    totalDays = 180,
                    presentDays = 168,
                    absentDays = 7,
                    leaveDays = 5,
                    percentage = 93
                )
            }

            // Daily History List Title
            item {
                Text(
                    text = "Daily Attendance Log",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
            }

            if (records.isEmpty()) {
                item {
                    Text("No daily attendance logged yet.", color = Color.Gray)
                }
            } else {
                items(records) { rec ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceLight,
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = rec.date,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SunriseNavy
                                )
                                Text(
                                    text = rec.month,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            val (statusBg, statusFg) = when (rec.status) {
                                "PRESENT" -> StatusGreenContainer to StatusGreen
                                "ABSENT" -> StatusRedContainer to StatusRed
                                else -> SunriseGoldContainer to SunriseGold
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = statusBg
                            ) {
                                Text(
                                    text = rec.status,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = statusFg,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendanceCountChip(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = color
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
    }
}

@Composable
private fun SummarySectionCard(
    title: String,
    totalDays: Int,
    presentDays: Int,
    absentDays: Int,
    leaveDays: Int,
    percentage: Int
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceLight,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SunriseNavy
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryMetricBox(label = "Total Days", value = totalDays.toString())
                SummaryMetricBox(label = "Present", value = presentDays.toString(), color = StatusGreen)
                SummaryMetricBox(label = "Absent", value = absentDays.toString(), color = StatusRed)
                SummaryMetricBox(label = "Leave", value = leaveDays.toString(), color = SunriseGold)
                SummaryMetricBox(label = "Percentage", value = "$percentage%", color = SunriseNavy)
            }
        }
    }
}

@Composable
private fun SummaryMetricBox(label: String, value: String, color: Color = SunriseNavy) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = Color.Gray)
    }
}
