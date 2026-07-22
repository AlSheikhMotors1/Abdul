package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.data.model.HomeworkEntry
import com.example.data.model.Teacher
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesAndDiaryScreen(
    homeworkEntries: List<HomeworkEntry>,
    currentRole: UserRole,
    activeTeacher: Teacher?,
    onUploadHomeworkClick: (className: String, subjectName: String, contentType: String) -> Unit,
    onBackClick: () -> Unit
) {
    val classesList = listOf(
        "Play", "Nursery", "Prep",
        "Class 1", "Class 2", "Class 3", "Class 4",
        "Class 5", "Class 6", "Class 7", "Class 8"
    )

    val subjectsList = listOf(
        "English", "Urdu", "Mathematics", "Science",
        "Islamiat", "Computer", "Sindhi", "General Knowledge", "Drawing"
    )

    val contentTypes = listOf(
        "HOMEWORK" to "Homework",
        "DAILY_DIARY" to "Daily Diary",
        "CLASSWORK" to "Classwork",
        "ASSIGNMENT" to "Assignments",
        "TEACHER_NOTES" to "Teacher Notes"
    )

    var selectedClass by remember { mutableStateOf("Class 1") }
    var selectedSubject by remember { mutableStateOf("Science") }
    var selectedType by remember { mutableStateOf("HOMEWORK") }

    // Filter homework matching selected class, subject, and type
    val filteredEntries = remember(homeworkEntries, selectedClass, selectedSubject, selectedType) {
        homeworkEntries.filter {
            it.className == selectedClass &&
                    it.subjectName == selectedSubject &&
                    it.contentType == selectedType
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("2. Classes & Daily Diary", fontWeight = FontWeight.Bold, color = SunriseNavy)
                        Text("$selectedClass • $selectedSubject", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SunriseNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight)
            )
        },
        floatingActionButton = {
            if (currentRole == UserRole.ADMIN || currentRole == UserRole.TEACHER) {
                ExtendedFloatingActionButton(
                    onClick = {
                        onUploadHomeworkClick(selectedClass, selectedSubject, selectedType)
                    },
                    containerColor = SunriseNavy,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload $selectedSubject Entry")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
        ) {
            // 1. Class Selection Horizontal Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceLight)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "SELECT CLASS",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(classesList) { cls ->
                        val isSelected = selectedClass == cls
                        Surface(
                            onClick = { selectedClass = cls },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) SunriseNavy else SurfaceVariantLight,
                            contentColor = if (isSelected) Color.White else SunriseNavy,
                            modifier = Modifier.height(36.dp)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cls,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = SurfaceVariantLight)

                // 2. Subject Selection Horizontal Bar
                Text(
                    text = "SELECT SUBJECT FOR $selectedClass",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(subjectsList) { subj ->
                        val isSelected = selectedSubject == subj
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSubject = subj },
                            label = { Text(subj) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SunriseGold,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceLight,
                                labelColor = SunriseNavy
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Content Type Tabs (Homework, Daily Diary, Classwork, Assignments, Teacher Notes)
            ScrollableTabRow(
                selectedTabIndex = contentTypes.indexOfFirst { it.first == selectedType },
                containerColor = SurfaceLight,
                contentColor = SunriseNavy,
                edgePadding = 16.dp
            ) {
                contentTypes.forEach { (code, label) ->
                    Tab(
                        selected = selectedType == code,
                        onClick = { selectedType = code },
                        text = {
                            Text(
                                text = label,
                                fontWeight = if (selectedType == code) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Notice Banner explaining the Automatic Distribution System
            Surface(
                color = SunriseGoldContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SunriseGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Every student in $selectedClass automatically receives these entries.",
                        style = MaterialTheme.typography.labelSmall,
                        color = SunriseNavy,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 4. Homework Entries List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                if (filteredEntries.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Task,
                                    contentDescription = null,
                                    tint = SunriseGold,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No $selectedType yet for $selectedClass ($selectedSubject)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SunriseNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Teacher uploads entries once, and all students in $selectedClass see them instantly.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(filteredEntries) { entry ->
                        HomeworkEntryCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeworkEntryCard(entry: HomeworkEntry) {
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
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SunriseGoldContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = SunriseGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Teacher: ${entry.authorTeacherName}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SunriseNavy
                        )
                    }
                }

                Text(
                    text = entry.dateFormatted,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = entry.chapter,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SunriseNavy
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceVariantLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = entry.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Distributed to all ${entry.className} students",
                    style = MaterialTheme.typography.labelSmall,
                    color = StatusGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
