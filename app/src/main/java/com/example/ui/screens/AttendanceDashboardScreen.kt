package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.Student
import com.example.data.model.Teacher
import com.example.data.model.UserRole
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceDashboardScreen(
    currentRole: UserRole,
    activeTeacher: Teacher?,
    allStudents: List<Student>,
    allAttendanceRecords: List<AttendanceRecord>,
    verifiedStudent: Student?,
    onSaveAttendance: (records: List<AttendanceRecord>, absentStudentNames: List<String>, className: String, date: String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(if (currentRole == UserRole.TEACHER) 0 else 1) } // 0 = Teacher Recording, 1 = Parent View

    // Teacher Attendance Recording State
    val availableClasses = listOf("Class 1", "Class 2", "Play", "Nursery", "Prep", "Class 3", "Class 4", "Class 5")
    var selectedClass by remember { mutableStateOf("Class 1") }
    
    val todayFormatted = remember {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.format(Date())
    }
    var selectedDate by remember { mutableStateOf(todayFormatted) }

    // Filter students belonging to the selected class
    val classStudents = remember(allStudents, selectedClass) {
        allStudents.filter { it.className.equals(selectedClass, ignoreCase = true) }
    }

    // Map studentId -> status ("PRESENT", "ABSENT", "LEAVE")
    val studentAttendanceState = remember(selectedClass, selectedDate, allAttendanceRecords, classStudents) {
        mutableStateMapOf<Int, String>().apply {
            classStudents.forEach { student ->
                val existing = allAttendanceRecords.firstOrNull { it.studentId == student.id && it.date == selectedDate }
                this[student.id] = existing?.status ?: "PRESENT"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "📋 Daily Attendance Dashboard",
                            fontWeight = FontWeight.Bold,
                            color = SunriseNavy
                        )
                        Text(
                            text = if (selectedTab == 0) "Teacher Mode: Mark Daily Presence" else "Parent Mode: Child Presence Tracker",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("attendance_back_button")
                    ) {
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
            // Mode Switcher Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceLight,
                contentColor = SunriseNavy
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.EditCalendar, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Teacher Marker", fontWeight = FontWeight.Bold)
                        }
                    },
                    modifier = Modifier.testTag("tab_teacher_attendance")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Parent Record View", fontWeight = FontWeight.Bold)
                        }
                    },
                    modifier = Modifier.testTag("tab_parent_attendance")
                )
            }

            if (selectedTab == 0) {
                // TEACHER RECORDING VIEW
                TeacherAttendanceRecordingContent(
                    selectedClass = selectedClass,
                    availableClasses = availableClasses,
                    selectedDate = selectedDate,
                    classStudents = classStudents,
                    attendanceMap = studentAttendanceState,
                    activeTeacher = activeTeacher,
                    onClassSelected = { selectedClass = it },
                    onDateSelected = { selectedDate = it },
                    onStatusChanged = { studentId, status ->
                        studentAttendanceState[studentId] = status
                    },
                    onMarkAll = { status ->
                        classStudents.forEach { studentAttendanceState[it.id] = status }
                    },
                    onSave = {
                        val monthStr = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
                        val recordsToSave = classStudents.map { student ->
                            val st = studentAttendanceState[student.id] ?: "PRESENT"
                            AttendanceRecord(
                                studentId = student.id,
                                date = selectedDate,
                                month = monthStr,
                                status = st
                            )
                        }
                        val absentNames = classStudents
                            .filter { (studentAttendanceState[it.id] ?: "PRESENT") == "ABSENT" }
                            .map { it.name }

                        onSaveAttendance(recordsToSave, absentNames, selectedClass, selectedDate)
                    }
                )
            } else {
                // PARENT VIEW
                ParentAttendanceViewContent(
                    verifiedStudent = verifiedStudent,
                    allStudents = allStudents,
                    allAttendanceRecords = allAttendanceRecords
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeacherAttendanceRecordingContent(
    selectedClass: String,
    availableClasses: List<String>,
    selectedDate: String,
    classStudents: List<Student>,
    attendanceMap: Map<Int, String>,
    activeTeacher: Teacher?,
    onClassSelected: (String) -> Unit,
    onDateSelected: (String) -> Unit,
    onStatusChanged: (studentId: Int, status: String) -> Unit,
    onMarkAll: (status: String) -> Unit,
    onSave: () -> Unit
) {
    val presentCount = classStudents.count { (attendanceMap[it.id] ?: "PRESENT") == "PRESENT" }
    val absentCount = classStudents.count { (attendanceMap[it.id] ?: "PRESENT") == "ABSENT" }
    val leaveCount = classStudents.count { (attendanceMap[it.id] ?: "PRESENT") == "LEAVE" }
    val totalCount = classStudents.size.coerceAtLeast(1)
    val percentage = (presentCount.toDouble() / totalCount * 100).toInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Teacher Banner Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SunriseNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SunriseGoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = SunriseGold)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Daily Attendance Marker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (activeTeacher != null) "Logged in as ${activeTeacher.name}" else "Class Teacher Mode",
                                style = MaterialTheme.typography.bodySmall,
                                color = SunriseGoldLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Class selector chips
                    Text(text = "Select Class:", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        availableClasses.take(5).forEach { cls ->
                            FilterChip(
                                selected = selectedClass == cls,
                                onClick = { onClassSelected(cls) },
                                label = { Text(cls) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SunriseGold,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White.copy(alpha = 0.2f),
                                    labelColor = Color.White
                                ),
                                modifier = Modifier.testTag("chip_class_$cls")
                            )
                        }
                    }
                }
            }
        }

        // Date & Stats Summary Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceLight,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Attendance for $selectedClass",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SunriseNavy
                            )
                            Text(
                                text = "Date: $selectedDate",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = StatusGreenContainer
                        ) {
                            Text(
                                text = "$percentage% Present",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = StatusGreen,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatBadge(label = "Enrolled", count = classStudents.size, color = SunriseNavy)
                        StatBadge(label = "Present", count = presentCount, color = StatusGreen)
                        StatBadge(label = "Absent", count = absentCount, color = StatusRed)
                        StatBadge(label = "Leave", count = leaveCount, color = SunriseGold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Actions Row
                    Text(
                        text = "Quick Batch Actions:",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onMarkAll("PRESENT") },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusGreenContainer, contentColor = StatusGreen),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_mark_all_present")
                        ) {
                            Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("All Present", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onMarkAll("ABSENT") },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusRedContainer, contentColor = StatusRed),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_mark_all_absent")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("All Absent", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Student Roster Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Student List (${classStudents.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )

                Text(
                    text = "Tap P / A / L to toggle",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }

        if (classStudents.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No students currently enrolled in $selectedClass.",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(classStudents) { student ->
                val currentStatus = attendanceMap[student.id] ?: "PRESENT"

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceLight,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_card_${student.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SunriseNavy.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = student.name.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = SunriseNavy
                                )
                            }

                            Column {
                                Text(
                                    text = student.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SunriseNavy
                                )
                                Text(
                                    text = "Roll #${student.rollNumber} • ${student.fatherName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        // P / A / L Toggle Group
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            StatusToggleChip(
                                label = "P",
                                isSelected = currentStatus == "PRESENT",
                                activeBg = StatusGreen,
                                onSelect = { onStatusChanged(student.id, "PRESENT") },
                                testTag = "btn_p_${student.id}"
                            )

                            StatusToggleChip(
                                label = "A",
                                isSelected = currentStatus == "ABSENT",
                                activeBg = StatusRed,
                                onSelect = { onStatusChanged(student.id, "ABSENT") },
                                testTag = "btn_a_${student.id}"
                            )

                            StatusToggleChip(
                                label = "L",
                                isSelected = currentStatus == "LEAVE",
                                activeBg = SunriseGold,
                                onSelect = { onStatusChanged(student.id, "LEAVE") },
                                testTag = "btn_l_${student.id}"
                            )
                        }
                    }
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_daily_attendance")
            ) {
                Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save & Publish Attendance ($selectedClass)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun StatusToggleChip(
    label: String,
    isSelected: Boolean,
    activeBg: Color,
    onSelect: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(if (isSelected) activeBg else Color.LightGray.copy(alpha = 0.3f))
            .clickable { onSelect() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (isSelected) Color.White else Color.DarkGray
        )
    }
}

@Composable
private fun StatBadge(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = color.copy(alpha = 0.15f)
        ) {
            Text(
                text = count.toString(),
                fontWeight = FontWeight.Bold,
                color = color,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParentAttendanceViewContent(
    verifiedStudent: Student?,
    allStudents: List<Student>,
    allAttendanceRecords: List<AttendanceRecord>
) {
    var selectedStudent by remember { mutableStateOf(verifiedStudent ?: allStudents.firstOrNull()) }
    var filterStatus by remember { mutableStateOf("ALL") } // ALL, PRESENT, ABSENT, LEAVE

    val studentRecords = remember(allAttendanceRecords, selectedStudent) {
        if (selectedStudent != null) {
            allAttendanceRecords.filter { it.studentId == selectedStudent!!.id }
        } else emptyList()
    }

    val filteredRecords = remember(studentRecords, filterStatus) {
        if (filterStatus == "ALL") studentRecords
        else studentRecords.filter { it.status == filterStatus }
    }

    val presentCount = studentRecords.count { it.status == "PRESENT" }
    val absentCount = studentRecords.count { it.status == "ABSENT" }
    val leaveCount = studentRecords.count { it.status == "LEAVE" }
    val totalDays = studentRecords.size.coerceAtLeast(1)
    val percentage = (presentCount.toDouble() / totalDays * 100).toInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Child Selector Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SunriseNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "👨‍👩‍👧 Select Child Record:",
                        style = MaterialTheme.typography.labelSmall,
                        color = SunriseGoldLight
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allStudents.take(4).forEach { student ->
                            FilterChip(
                                selected = selectedStudent?.id == student.id,
                                onClick = { selectedStudent = student },
                                label = { Text("${student.name} (${student.className})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SunriseGold,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White.copy(alpha = 0.2f),
                                    labelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        if (selectedStudent == null) {
            item {
                Text("No student verified or selected.", color = Color.Gray)
            }
        } else {
            // Overall Presence Percentage Banner
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceLight,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${selectedStudent!!.name} - ${selectedStudent!!.className}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SunriseNavy
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (percentage >= 80) StatusGreen else StatusRed,
                            fontSize = 42.sp
                        )

                        Text(
                            text = "Overall Attendance Percentage",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatBadge(label = "Present", count = presentCount, color = StatusGreen)
                            StatBadge(label = "Absent", count = absentCount, color = StatusRed)
                            StatBadge(label = "Leave", count = leaveCount, color = SunriseGold)
                            StatBadge(label = "Total Days", count = studentRecords.size, color = SunriseNavy)
                        }
                    }
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "ALL" to "All (${studentRecords.size})",
                        "PRESENT" to "Present ($presentCount)",
                        "ABSENT" to "Absent ($absentCount)",
                        "LEAVE" to "Leave ($leaveCount)"
                    ).forEach { (code, label) ->
                        FilterChip(
                            selected = filterStatus == code,
                            onClick = { filterStatus = code },
                            label = { Text(label) },
                            modifier = Modifier.testTag("filter_$code")
                        )
                    }
                }
            }

            // Attendance Records List Title
            item {
                Text(
                    text = "Daily Attendance History Log",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
            }

            if (filteredRecords.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No attendance records match the selected filter.",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(filteredRecords) { rec ->
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
