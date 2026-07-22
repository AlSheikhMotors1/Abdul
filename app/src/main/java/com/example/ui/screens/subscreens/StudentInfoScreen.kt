package com.example.ui.screens.subscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun StudentInfoScreen(
    student: Student,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Information", fontWeight = FontWeight.Bold, color = SunriseNavy) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceLight,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Student Photo Display
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(SunriseGoldContainer)
                            .border(3.dp, SunriseGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Student Photo",
                            tint = SunriseNavy,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = student.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SunriseNavy
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SunriseNavy,
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text(
                            text = "Verified Student Profile",
                            style = MaterialTheme.typography.labelMedium,
                            color = SunriseGold,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = SurfaceVariantLight)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Detail Rows
                    InfoDetailRow(icon = Icons.Outlined.Badge, label = "Roll Number", value = student.rollNumber)
                    InfoDetailRow(icon = Icons.Outlined.Person, label = "Student Full Name", value = student.name)
                    InfoDetailRow(icon = Icons.Outlined.FamilyRestroom, label = "Father's Name", value = student.fatherName)
                    InfoDetailRow(icon = Icons.Outlined.School, label = "Class", value = student.className)
                    InfoDetailRow(icon = Icons.Outlined.Class, label = "Section", value = student.section)
                    InfoDetailRow(icon = Icons.Outlined.LocationCity, label = "Institution", value = "Sunrise School Lodhra, Pakistan")
                }
            }
        }
    }
}

@Composable
private fun InfoDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceVariantLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SunriseNavy,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = SunriseNavy
            )
        }
    }
}
