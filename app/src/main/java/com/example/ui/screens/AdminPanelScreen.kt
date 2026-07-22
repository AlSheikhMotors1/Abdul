package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    schoolInfo: SchoolInfo,
    students: List<Student>,
    teachers: List<Teacher>,
    assignments: List<TeacherAssignment>,
    notices: List<SchoolNotice>,
    onAddTeacher: (name: String, email: String) -> Unit,
    onAssignTeacher: (teacherId: Int, className: String, subjectName: String) -> Unit,
    onAddStudent: (id: Int, name: String, father: String, className: String, section: String, roll: String, feeCleared: Boolean, pending: Double, remarks: String) -> Unit,
    onDeleteStudent: (Student) -> Unit,
    onAddNotice: (category: String, title: String, description: String, isEmergency: Boolean) -> Unit,
    onSendNotification: (title: String, message: String) -> Unit,
    onUpdateBanner: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedAdminTab by remember { mutableStateOf("STUDENTS") }

    val adminTabs = listOf(
        "STUDENTS" to "Students",
        "TEACHERS" to "Teachers & Assign",
        "NOTICES" to "Notices & Banner",
        "BROADCAST" to "Push Notifications"
    )

    // Dialog states
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var showAddTeacherDialog by remember { mutableStateOf(false) }
    var showAssignSubjectDialog by remember { mutableStateOf(false) }
    var showAddNoticeDialog by remember { mutableStateOf(false) }
    var showBroadcastDialog by remember { mutableStateOf(false) }
    var showEditBannerDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("⚙️ Admin Control Panel", fontWeight = FontWeight.Bold, color = SunriseNavy)
                        Text("Full School Management Access", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
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
            // Admin Sub-Tab Bar
            ScrollableTabRow(
                selectedTabIndex = adminTabs.indexOfFirst { it.first == selectedAdminTab },
                containerColor = SurfaceLight,
                contentColor = SunriseNavy,
                edgePadding = 16.dp
            ) {
                adminTabs.forEach { (code, label) ->
                    Tab(
                        selected = selectedAdminTab == code,
                        onClick = { selectedAdminTab = code },
                        text = { Text(label, fontWeight = if (selectedAdminTab == code) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedAdminTab) {
                    "STUDENTS" -> {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Student Directory (${students.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SunriseNavy)
                                Button(
                                    onClick = { showAddStudentDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                                ) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Student")
                                }
                            }
                        }

                        items(students) { student ->
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${student.name} (${student.rollNumber})",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SunriseNavy
                                        )
                                        Text(
                                            text = "Father: ${student.fatherName} • ${student.className} (${student.section})",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = if (student.feeCleared) StatusGreenContainer else StatusRedContainer
                                            ) {
                                                Text(
                                                    text = if (student.feeCleared) "Fee Cleared" else "Fee Pending: PKR ${student.feePendingAmount.toInt()}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = if (student.feeCleared) StatusGreen else StatusRed,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    IconButton(onClick = { onDeleteStudent(student) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StatusRed)
                                    }
                                }
                            }
                        }
                    }

                    "TEACHERS" -> {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Teachers & Subject Assignments", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SunriseNavy)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    OutlinedButton(onClick = { showAddTeacherDialog = true }) {
                                        Text("New Teacher")
                                    }
                                    Button(
                                        onClick = { showAssignSubjectDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                                    ) {
                                        Text("Assign Subject")
                                    }
                                }
                            }
                        }

                        items(teachers) { teacher ->
                            val teacherAssigns = assignments.filter { it.teacherId == teacher.id }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SurfaceLight,
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "Teacher: ${teacher.name}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SunriseNavy
                                    )
                                    Text(
                                        text = teacher.email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Assigned Classes & Subjects:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SunriseNavy
                                    )

                                    if (teacherAssigns.isEmpty()) {
                                        Text("No classes assigned yet", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    } else {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.padding(top = 4.dp)
                                        ) {
                                            teacherAssigns.forEach { assign ->
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = SunriseGoldContainer
                                                ) {
                                                    Text(
                                                        text = "${assign.className} -> ${assign.subjectName}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = SunriseNavy,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "NOTICES" -> {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("School Announcements & Banner", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SunriseNavy)
                                    Button(
                                        onClick = { showAddNoticeDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                                    ) {
                                        Text("Post Notice")
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SurfaceVariantLight,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Home Banner Marquee Text:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                            Text(schoolInfo.bannerText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SunriseNavy)
                                        }
                                        TextButton(onClick = { showEditBannerDialog = true }) {
                                            Text("Edit Banner")
                                        }
                                    }
                                }
                            }
                        }

                        items(notices) { notice ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (notice.isEmergency) StatusRedContainer else SurfaceLight,
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "${if (notice.isEmergency) "🚨 " else ""}${notice.title}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (notice.isEmergency) StatusRed else SunriseNavy
                                    )
                                    Text(
                                        text = notice.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }

                    "BROADCAST" -> {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceLight,
                                shadowElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.CellTower, contentDescription = null, tint = SunriseNavy, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Push Notification Dispatcher", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SunriseNavy)
                                    Text("Broadcast instant push notifications to all parents for emergency alerts, homework updates, holiday reminders, or fee notices.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = { showBroadcastDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Send Push Notification")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Student Dialog
    if (showAddStudentDialog) {
        var name by remember { mutableStateOf("") }
        var father by remember { mutableStateOf("") }
        var cls by remember { mutableStateOf("Class 1") }
        var section by remember { mutableStateOf("A") }
        var roll by remember { mutableStateOf("SSL-") }
        var feeCleared by remember { mutableStateOf(true) }
        var pendingAmount by remember { mutableStateOf("0") }
        var remarks by remember { mutableStateOf("Attentive in class.") }

        AlertDialog(
            onDismissRequest = { showAddStudentDialog = false },
            title = { Text("Add New Student") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Student Name") })
                    OutlinedTextField(value = father, onValueChange = { father = it }, label = { Text("Father's Name") })
                    OutlinedTextField(value = cls, onValueChange = { cls = it }, label = { Text("Class (e.g. Class 1)") })
                    OutlinedTextField(value = section, onValueChange = { section = it }, label = { Text("Section") })
                    OutlinedTextField(value = roll, onValueChange = { roll = it }, label = { Text("Roll Number") })
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = feeCleared, onCheckedChange = { feeCleared = it })
                        Text("Fee Cleared?")
                    }
                    if (!feeCleared) {
                        OutlinedTextField(value = pendingAmount, onValueChange = { pendingAmount = it }, label = { Text("Pending Amount (PKR)") })
                    }
                    OutlinedTextField(value = remarks, onValueChange = { remarks = it }, label = { Text("Teacher Remarks") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    onAddStudent(0, name, father, cls, section, roll, feeCleared, pendingAmount.toDoubleOrNull() ?: 0.0, remarks)
                    showAddStudentDialog = false
                }) {
                    Text("Save Student")
                }
            },
            dismissButton = { TextButton(onClick = { showAddStudentDialog = false }) { Text("Cancel") } }
        )
    }

    // Add Teacher Dialog
    if (showAddTeacherDialog) {
        var tName by remember { mutableStateOf("") }
        var tEmail by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddTeacherDialog = false },
            title = { Text("Add New Teacher") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = tName, onValueChange = { tName = it }, label = { Text("Teacher Name") })
                    OutlinedTextField(value = tEmail, onValueChange = { tEmail = it }, label = { Text("Email Address") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    onAddTeacher(tName, tEmail)
                    showAddTeacherDialog = false
                }) {
                    Text("Add Teacher")
                }
            },
            dismissButton = { TextButton(onClick = { showAddTeacherDialog = false }) { Text("Cancel") } }
        )
    }

    // Assign Subject Dialog
    if (showAssignSubjectDialog) {
        var selectedTeacherId by remember { mutableStateOf(teachers.firstOrNull()?.id ?: 0) }
        var assignClass by remember { mutableStateOf("Class 1") }
        var assignSubject by remember { mutableStateOf("Science") }

        AlertDialog(
            onDismissRequest = { showAssignSubjectDialog = false },
            title = { Text("Assign Teacher to Class & Subject") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select Teacher:")
                    teachers.forEach { t ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedTeacherId == t.id, onClick = { selectedTeacherId = t.id })
                            Text(t.name)
                        }
                    }
                    OutlinedTextField(value = assignClass, onValueChange = { assignClass = it }, label = { Text("Class Name (e.g. Class 1)") })
                    OutlinedTextField(value = assignSubject, onValueChange = { assignSubject = it }, label = { Text("Subject Name (e.g. Science)") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    onAssignTeacher(selectedTeacherId, assignClass, assignSubject)
                    showAssignSubjectDialog = false
                }) {
                    Text("Save Assignment")
                }
            },
            dismissButton = { TextButton(onClick = { showAssignSubjectDialog = false }) { Text("Cancel") } }
        )
    }

    // Post Notice Dialog
    if (showAddNoticeDialog) {
        var nCategory by remember { mutableStateOf("NOTICES") }
        var nTitle by remember { mutableStateOf("") }
        var nDesc by remember { mutableStateOf("") }
        var isEmergency by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddNoticeDialog = false },
            title = { Text("Post School Notice") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = nTitle, onValueChange = { nTitle = it }, label = { Text("Notice Title") })
                    OutlinedTextField(value = nDesc, onValueChange = { nDesc = it }, label = { Text("Notice Description") }, minLines = 3)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isEmergency, onCheckedChange = { isEmergency = it })
                        Text("Is Emergency Notice?", color = StatusRed, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    onAddNotice(nCategory, nTitle, nDesc, isEmergency)
                    showAddNoticeDialog = false
                }) {
                    Text("Publish Notice")
                }
            },
            dismissButton = { TextButton(onClick = { showAddNoticeDialog = false }) { Text("Cancel") } }
        )
    }

    // Broadcast Push Notification Dialog
    if (showBroadcastDialog) {
        var notifTitle by remember { mutableStateOf("") }
        var notifMsg by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showBroadcastDialog = false },
            title = { Text("Send Push Notification") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = notifTitle, onValueChange = { notifTitle = it }, label = { Text("Notification Title") })
                    OutlinedTextField(value = notifMsg, onValueChange = { notifMsg = it }, label = { Text("Notification Message") }, minLines = 2)
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSendNotification(notifTitle, notifMsg)
                    showBroadcastDialog = false
                }) {
                    Text("Broadcast Now")
                }
            },
            dismissButton = { TextButton(onClick = { showBroadcastDialog = false }) { Text("Cancel") } }
        )
    }

    // Edit Banner Dialog
    if (showEditBannerDialog) {
        var bannerText by remember { mutableStateOf(schoolInfo.bannerText) }

        AlertDialog(
            onDismissRequest = { showEditBannerDialog = false },
            title = { Text("Edit Home Banner Marquee") },
            text = {
                OutlinedTextField(
                    value = bannerText,
                    onValueChange = { bannerText = it },
                    label = { Text("Banner Text") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    onUpdateBanner(bannerText)
                    showEditBannerDialog = false
                }) {
                    Text("Update Banner")
                }
            },
            dismissButton = { TextButton(onClick = { showEditBannerDialog = false }) { Text("Cancel") } }
        )
    }
}
