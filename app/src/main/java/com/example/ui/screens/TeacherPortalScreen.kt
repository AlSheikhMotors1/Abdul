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
import com.example.data.model.Teacher
import com.example.data.model.TeacherAssignment
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPortalScreen(
    activeTeacher: Teacher?,
    allTeachers: List<Teacher>,
    assignments: List<TeacherAssignment>,
    onSelectTeacher: (Teacher) -> Unit,
    onUploadContent: (className: String, subjectName: String, contentType: String, chapter: String, content: String) -> Unit,
    onNavigateToAttendance: () -> Unit = {},
    onBackClick: () -> Unit
) {
    var showUploadDialog by remember { mutableStateOf(false) }
    var selectedAssignmentForUpload by remember { mutableStateOf<TeacherAssignment?>(null) }

    var inputContentType by remember { mutableStateOf("HOMEWORK") }
    var inputChapter by remember { mutableStateOf("") }
    var inputContent by remember { mutableStateOf("") }

    val teacherAssignments = remember(assignments, activeTeacher) {
        if (activeTeacher != null) {
            assignments.filter { it.teacherId == activeTeacher.id }
        } else emptyList()
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("👩‍🏫 Teacher Workspace", fontWeight = FontWeight.Bold, color = SunriseNavy)
                        Text(
                            text = if (activeTeacher != null) "Logged in as ${activeTeacher.name}" else "Select Teacher Account",
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
            // Teacher Selector / Profile Card
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
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(SunriseGoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = SunriseGold,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeTeacher?.name ?: "No Teacher Selected",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = activeTeacher?.email ?: "Tap below to switch teacher profile",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SunriseGoldLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Switch Teacher Chips
                        Text(
                            text = "Switch Teacher Profile:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            allTeachers.forEach { teacher ->
                                FilterChip(
                                    selected = activeTeacher?.id == teacher.id,
                                    onClick = { onSelectTeacher(teacher) },
                                    label = { Text(teacher.name) },
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

            // Security Notice
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = StatusGreenContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = StatusGreen,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "Assigned Scope Rule: Teachers can strictly view & manage only their assigned classes and subjects. Other teachers' work cannot be modified.",
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Attendance Action Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SunriseNavy,
                    shadowElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SunriseGoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EditCalendar, contentDescription = null, tint = SunriseNavy)
                            }

                            Column {
                                Text(
                                    text = "Daily Student Attendance",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Record daily presence, absences & leaves",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SunriseGoldLight
                                )
                            }
                        }

                        Button(
                            onClick = onNavigateToAttendance,
                            colors = ButtonDefaults.buttonColors(containerColor = SunriseGold, contentColor = Color.White)
                        ) {
                            Text("Open Marker", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Assigned Classes & Subjects Title
            item {
                Text(
                    text = "Your Assigned Classes & Subjects",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
            }

            if (teacherAssignments.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No subjects currently assigned by Admin to ${activeTeacher?.name}.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(teacherAssignments) { assign ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceLight,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SunriseGoldContainer
                                ) {
                                    Text(
                                        text = assign.className,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SunriseNavy,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = assign.subjectName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SunriseNavy
                                )
                            }

                            Button(
                                onClick = {
                                    selectedAssignmentForUpload = assign
                                    showUploadDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Upload Content")
                            }
                        }
                    }
                }
            }
        }
    }

    // Upload Content Dialog for Teacher
    if (showUploadDialog && selectedAssignmentForUpload != null) {
        val assign = selectedAssignmentForUpload!!
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = {
                Text(
                    text = "Upload for ${assign.className} - ${assign.subjectName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Content Type:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)

                    val types = listOf(
                        "HOMEWORK" to "Homework",
                        "DAILY_DIARY" to "Daily Diary",
                        "CLASSWORK" to "Classwork",
                        "ASSIGNMENT" to "Assignment",
                        "TEACHER_NOTES" to "Teacher Notes"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        types.take(3).forEach { (code, label) ->
                            FilterChip(
                                selected = inputContentType == code,
                                onClick = { inputContentType = code },
                                label = { Text(label) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = inputChapter,
                        onValueChange = { inputChapter = it },
                        label = { Text("Chapter / Topic Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = inputContent,
                        onValueChange = { inputContent = it },
                        label = { Text("Detailed Instructions / Homework Content") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUploadContent(
                            assign.className,
                            assign.subjectName,
                            inputContentType,
                            inputChapter,
                            inputContent
                        )
                        showUploadDialog = false
                        inputChapter = ""
                        inputContent = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                ) {
                    Text("Save & Distribute to Class", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
