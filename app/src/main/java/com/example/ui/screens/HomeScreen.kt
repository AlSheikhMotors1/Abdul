package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SchoolInfo
import com.example.data.model.UserRole
import com.example.ui.components.QuickAccessSection
import com.example.ui.components.SchoolContactInfoCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    schoolInfo: SchoolInfo,
    onNavigateToUpdates: () -> Unit,
    onNavigateToDiary: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onQuickAccessClick: (String) -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onMapsClick: () -> Unit,
    onAdminClick: () -> Unit,
    onTeacherPortalClick: () -> Unit,
    currentRole: UserRole
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Image Banner Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_school_banner),
                    contentDescription = "Sunrise School Building",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for visual contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SunriseGold
                    ) {
                        Text(
                            text = "EST. LODHRA • PAKISTAN",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "Sunrise School Lodhra",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = schoolInfo.schoolTiming,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Section Title: Primary Navigation
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Main School Portals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
                Text(
                    text = "Tap any main button below to access school services",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 3 MAIN BUTTONS
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Button 1: School Updates
                MainFeatureButtonCard(
                    title = "1. School Updates",
                    subtitle = "Timings, Holidays, Notices, Exam Schedules & Events",
                    icon = Icons.Default.Campaign,
                    badgeText = "Latest Notices",
                    gradientColors = listOf(Color(0xFF0F172A), Color(0xFF1E293B)),
                    accentColor = SunriseGold,
                    onClick = onNavigateToUpdates
                )

                // Button 2: Classes & Daily Diary
                MainFeatureButtonCard(
                    title = "2. Classes & Daily Diary",
                    subtitle = "Play to Class 8 • Homework, Diary, Classwork & Notes",
                    icon = Icons.Default.MenuBook,
                    badgeText = "Play - Class 8",
                    gradientColors = listOf(Color(0xFF0B2545), Color(0xFF134074)),
                    accentColor = Color(0xFF38BDF8),
                    onClick = onNavigateToDiary
                )

                // Button 3: Student Dashboard
                MainFeatureButtonCard(
                    title = "3. Student Dashboard",
                    subtitle = "Academic Performance, Attendance, Fee Status & Remarks",
                    icon = Icons.Default.AccountBox,
                    badgeText = "Student Profile",
                    gradientColors = listOf(Color(0xFF047857), Color(0xFF065F46)),
                    accentColor = Color(0xFF34D399),
                    onClick = onNavigateToDashboard
                )
            }
        }

        // Role-based Shortcuts Bar (If Admin or Teacher)
        if (currentRole == UserRole.ADMIN || currentRole == UserRole.TEACHER) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = if (currentRole == UserRole.ADMIN) "⚙️ Admin Controls" else "👩‍🏫 Teacher Workspace",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SunriseNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (currentRole == UserRole.ADMIN) {
                        Surface(
                            onClick = onAdminClick,
                            shape = RoundedCornerShape(12.dp),
                            color = StatusRedContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = StatusRed
                                    )
                                    Column {
                                        Text(
                                            text = "Open Admin Control Panel",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusRed
                                        )
                                        Text(
                                            text = "Manage Students, Teachers, Fees, Homework & Notices",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = StatusRed
                                )
                            }
                        }
                    } else if (currentRole == UserRole.TEACHER) {
                        Surface(
                            onClick = onTeacherPortalClick,
                            shape = RoundedCornerShape(12.dp),
                            color = StatusGreenContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = StatusGreen
                                    )
                                    Column {
                                        Text(
                                            text = "Open Teacher Management Portal",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusGreen
                                        )
                                        Text(
                                            text = "Upload homework for assigned classes only",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = StatusGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Access Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            QuickAccessSection(onItemClick = onQuickAccessClick)
        }

        // Contact Info & Google Maps
        item {
            Spacer(modifier = Modifier.height(12.dp))
            SchoolContactInfoCard(
                schoolInfo = schoolInfo,
                onCallClick = onCallClick,
                onWhatsAppClick = onWhatsAppClick,
                onMapsClick = onMapsClick
            )
        }
    }
}

@Composable
private fun MainFeatureButtonCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    gradientColors: List<Color>,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors)
                )
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = accentColor.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
