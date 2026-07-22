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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ResultRecord
import com.example.data.model.Student
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicPerformanceScreen(
    student: Student,
    results: List<ResultRecord>,
    onBackClick: () -> Unit
) {
    var selectedTerm by remember { mutableStateOf("MONTHLY_TEST") }

    val terms = listOf(
        "MONTHLY_TEST" to "Monthly Test",
        "MID_TERM" to "Mid-Term",
        "FINAL_EXAM" to "Final Exam"
    )

    val termResults = remember(results, selectedTerm) {
        results.filter { it.term == selectedTerm }
    }

    val totalObtained = termResults.sumOf { it.obtainedMarks }
    val totalMax = termResults.sumOf { it.totalMarks }
    val percentage = if (totalMax > 0) (totalObtained.toDouble() / totalMax * 100).toInt() else 0

    val overallGrade = when {
        percentage >= 90 -> "A+ (Outstanding)"
        percentage >= 80 -> "A (Excellent)"
        percentage >= 70 -> "B (Very Good)"
        percentage >= 60 -> "C (Good)"
        else -> "Pass"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Academic Performance", fontWeight = FontWeight.Bold, color = SunriseNavy) },
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
            // Term Selector Tabs
            TabRow(
                selectedTabIndex = terms.indexOfFirst { it.first == selectedTerm },
                containerColor = SurfaceLight,
                contentColor = SunriseNavy
            ) {
                terms.forEach { (code, label) ->
                    Tab(
                        selected = selectedTerm == code,
                        onClick = { selectedTerm = code },
                        text = { Text(label, fontWeight = if (selectedTerm == code) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Summary Card
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SunriseNavy,
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
                                        text = student.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${selectedTerm.replace("_", " ")} Overall Summary",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SunriseGoldLight
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = SunriseGold
                                ) {
                                    Text(
                                        text = overallGrade,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                PerformanceMetric(label = "Total Marks", value = "$totalObtained / $totalMax")
                                PerformanceMetric(label = "Percentage", value = "$percentage%")
                                PerformanceMetric(label = "Subjects", value = termResults.size.toString())
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Subject-Wise Marks Breakdown",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SunriseNavy,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (termResults.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().padding(24.dp)
                        ) {
                            Text(
                                text = "No result records uploaded for $selectedTerm yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(termResults) { res ->
                        SubjectResultCard(result = res)
                    }
                }
            }
        }
    }
}

@Composable
private fun PerformanceMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun SubjectResultCard(result: ResultRecord) {
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
                    text = result.subjectName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
                Text(
                    text = "Remarks: ${result.teacherRemarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = StatusGreenContainer
            ) {
                Text(
                    text = "${result.obtainedMarks} / ${result.totalMarks}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = StatusGreen,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}
