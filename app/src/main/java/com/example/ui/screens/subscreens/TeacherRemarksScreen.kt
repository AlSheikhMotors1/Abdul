package com.example.ui.screens.subscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.data.model.Student
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherRemarksScreen(
    student: Student,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teacher Remarks", fontWeight = FontWeight.Bold, color = SunriseNavy) },
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
                .padding(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceLight,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
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
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = SunriseGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Class Teacher Evaluation",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SunriseNavy
                            )
                            Text(
                                text = "${student.name} • ${student.className}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = SurfaceVariantLight)
                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceVariantLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "💬 Class Teacher Remarks:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SunriseNavy
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = student.teacherRemarks,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Conduct Metrics
                    ConductMetricRow(label = "Discipline & Behavior", status = "Excellent")
                    ConductMetricRow(label = "Class Participation", status = "Active & Enthusiastic")
                    ConductMetricRow(label = "Homework Regularity", status = "Punctual")
                    ConductMetricRow(label = "Cleanliness & Uniform", status = "Neat & Tidy")
                }
            }
        }
    }
}

@Composable
private fun ConductMetricRow(label: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = StatusGreenContainer
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = StatusGreen,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
