package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Student
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    verifiedStudent: Student?,
    allStudents: List<Student>,
    parentInputName: String,
    parentInputFather: String,
    parentInputClass: String,
    verificationError: String?,
    onNameChange: (String) -> Unit,
    onFatherChange: (String) -> Unit,
    onClassChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onSelectStudent: (Student) -> Unit,
    onOpenOptionPage: (optionKey: String) -> Unit,
    onBackClick: () -> Unit
) {
    var showVerificationDialog by remember { mutableStateOf(verifiedStudent == null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("3. Student Dashboard", fontWeight = FontWeight.Bold, color = SunriseNavy)
                        Text(
                            text = if (verifiedStudent != null) "${verifiedStudent.name} (${verifiedStudent.className})" else "Verification Required",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SunriseNavy)
                    }
                },
                actions = {
                    IconButton(onClick = { showVerificationDialog = true }) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = "Switch Student", tint = SunriseNavy)
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
                .background(BackgroundLight),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Verified Student Identity Header Card
            item {
                if (verifiedStudent != null) {
                    Surface(
                        color = SunriseNavy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.dp, SunriseGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Student Photo",
                                    tint = SunriseNavy,
                                    modifier = Modifier.size(48.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = verifiedStudent.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "S/o ${verifiedStudent.fatherName} • ${verifiedStudent.className} (${verifiedStudent.section})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SunriseGoldLight
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Roll No: ${verifiedStudent.rollNumber}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                } else {
                    Surface(
                        color = StatusRedContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "No Student Verified Yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusRed
                                )
                                Text(
                                    text = "Enter Student Name, Father's Name & Class to unlock dashboard.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                            }

                            Button(
                                onClick = { showVerificationDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = StatusRed)
                            ) {
                                Text("Verify Now", color = Color.White)
                            }
                        }
                    }
                }
            }

            // Notice about separate pages requirement
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Student Progress Modules",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SunriseNavy
                    )
                    Text(
                        text = "Tap any module below to view detailed report on a dedicated page",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 5 Separate Dashboard Pages/Options
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Option 1: Student Information
                    DashboardOptionTile(
                        title = "Student Information",
                        subtitle = "Photo, Roll Number, Class, Section & Family details",
                        icon = Icons.Default.Badge,
                        color = Color(0xFF0284C7),
                        onClick = { onOpenOptionPage("INFO") },
                        enabled = verifiedStudent != null
                    )

                    // Option 2: Academic Performance
                    DashboardOptionTile(
                        title = "Academic Performance",
                        subtitle = "Monthly Tests, Mid-Term & Final Exam subject marks",
                        icon = Icons.Default.Grade,
                        color = Color(0xFF7C3AED),
                        onClick = { onOpenOptionPage("ACADEMIC") },
                        enabled = verifiedStudent != null
                    )

                    // Option 3: Attendance Report
                    DashboardOptionTile(
                        title = "Attendance Report",
                        subtitle = "Present, Absent, Leave days & Monthly/Yearly Summary",
                        icon = Icons.Default.CalendarToday,
                        color = Color(0xFF059669),
                        onClick = { onOpenOptionPage("ATTENDANCE") },
                        enabled = verifiedStudent != null
                    )

                    // Option 4: Fee Status
                    DashboardOptionTile(
                        title = "Fee Status",
                        subtitle = "Check Fee Cleared / Pending status, due dates & amounts",
                        icon = Icons.Default.Payments,
                        color = Color(0xFFD97706),
                        onClick = { onOpenOptionPage("FEE") },
                        enabled = verifiedStudent != null
                    )

                    // Option 5: Teacher Remarks
                    DashboardOptionTile(
                        title = "Teacher Remarks",
                        subtitle = "Class teacher comments on academic & moral conduct",
                        icon = Icons.Default.Comment,
                        color = Color(0xFFDC2626),
                        onClick = { onOpenOptionPage("REMARKS") },
                        enabled = verifiedStudent != null
                    )
                }
            }
        }
    }

    // Verification Dialog
    if (showVerificationDialog) {
        AlertDialog(
            onDismissRequest = { if (verifiedStudent != null) showVerificationDialog = false },
            title = {
                Text(
                    text = "🔐 Parent Student Verification",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Enter student details to verify and view student records.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    // Quick Select Buttons for sample students
                    Text(
                        text = "Or tap a registered student to quick verify:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SunriseNavy
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(allStudents) { student ->
                            Surface(
                                onClick = {
                                    onSelectStudent(student)
                                    showVerificationDialog = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = SurfaceVariantLight,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "${student.name} (${student.className})",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = SunriseNavy
                                        )
                                        Text(
                                            text = "Father: ${student.fatherName}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.Gray
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = StatusGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    OutlinedTextField(
                        value = parentInputName,
                        onValueChange = onNameChange,
                        label = { Text("Student Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = parentInputFather,
                        onValueChange = onFatherChange,
                        label = { Text("Father's Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = parentInputClass,
                        onValueChange = onClassChange,
                        label = { Text("Class (e.g. Class 1)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (verificationError != null) {
                        Text(
                            text = verificationError,
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onVerifyClick()
                        if (verifiedStudent != null) showVerificationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                ) {
                    Text("Verify Student", color = Color.White)
                }
            },
            dismissButton = {
                if (verifiedStudent != null) {
                    TextButton(onClick = { showVerificationDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

@Composable
private fun DashboardOptionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        color = if (enabled) SurfaceLight else SurfaceVariantLight,
        shadowElevation = if (enabled) 2.dp else 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (enabled) color.copy(alpha = 0.15f) else Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) color else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) SunriseNavy else Color.Gray
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (enabled) color else Color.Gray
            )
        }
    }
}
