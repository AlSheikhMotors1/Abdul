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
import androidx.compose.ui.unit.sp
import com.example.data.model.Student
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeeStatusScreen(
    student: Student,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fee Status", fontWeight = FontWeight.Bold, color = SunriseNavy) },
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
                    Text(
                        text = "Official Fee Status Card",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = student.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SunriseNavy
                    )

                    Text(
                        text = "${student.className} • Roll No: ${student.rollNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SunriseGold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Primary Status Badge (Fee Cleared vs Fee Pending)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(if (student.feeCleared) StatusGreenContainer else StatusRedContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (student.feeCleared) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (student.feeCleared) StatusGreen else StatusRed,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (student.feeCleared) "✅ Fee Cleared" else "❌ Fee Pending",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (student.feeCleared) StatusGreen else StatusRed
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = SurfaceVariantLight)
                    Spacer(modifier = Modifier.height(20.dp))

                    // Strictly required fields
                    FeeDataRow(
                        label = "Outstanding Amount",
                        value = if (student.feeCleared) "PKR 0.00" else "PKR ${student.feePendingAmount.toInt()}",
                        isHighlight = !student.feeCleared
                    )

                    FeeDataRow(
                        label = "Due Date",
                        value = student.feeDueDate,
                        isHighlight = false
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Privacy Shield Notice
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceVariantLight,
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
                                tint = SunriseGold,
                                modifier = Modifier.size(20.dp)
                            )

                            Text(
                                text = "Privacy Protected: Historical payment receipts and detailed ledger records are managed strictly by the School Admin Office.",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeeDataRow(
    label: String,
    value: String,
    isHighlight: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isHighlight) StatusRed else SunriseNavy
        )
    }
}
