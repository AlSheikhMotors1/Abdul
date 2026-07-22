package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SchoolInfo
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun SchoolHeaderBanner(
    schoolInfo: SchoolInfo,
    currentRole: UserRole,
    onRoleSelect: (UserRole) -> Unit,
    onNotificationsClick: () -> Unit,
    unreadNotificationCount: Int = 2
) {
    var showRoleDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SunriseNavy, SunriseNavyLight)
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // School Crest & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.5.dp, SunriseGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "Sunrise School Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Column {
                    Text(
                        text = schoolInfo.schoolName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Lodhra • Punjab, Pakistan",
                        style = MaterialTheme.typography.bodySmall,
                        color = SunriseGoldLight,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Role Switcher Pill & Notifications Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Notifications Icon Button
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationCount > 0) {
                                Badge(
                                    containerColor = StatusRed,
                                    contentColor = Color.White
                                ) {
                                    Text(unreadNotificationCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }

                // Active Role Pill
                Surface(
                    onClick = { showRoleDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    color = when (currentRole) {
                        UserRole.ADMIN -> StatusRedContainer
                        UserRole.TEACHER -> StatusGreenContainer
                        UserRole.PARENT_STUDENT -> SunriseGoldContainer
                    },
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = when (currentRole) {
                                UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                UserRole.TEACHER -> Icons.Default.School
                                UserRole.PARENT_STUDENT -> Icons.Default.FamilyRestroom
                            },
                            contentDescription = null,
                            tint = when (currentRole) {
                                UserRole.ADMIN -> StatusRed
                                UserRole.TEACHER -> StatusGreen
                                UserRole.PARENT_STUDENT -> SunriseGold
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = when (currentRole) {
                                UserRole.ADMIN -> "Admin"
                                UserRole.TEACHER -> "Teacher"
                                UserRole.PARENT_STUDENT -> "Parent"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = when (currentRole) {
                                UserRole.ADMIN -> StatusRed
                                UserRole.TEACHER -> StatusGreen
                                UserRole.PARENT_STUDENT -> SunriseGold
                            }
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.DarkGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Customizable Admin Marquee / School Banner Box
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SunriseGold,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(
                        text = "ANNOUNCEMENT",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = schoolInfo.bannerText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // Role Switcher Dialog
    if (showRoleDialog) {
        AlertDialog(
            onDismissRequest = { showRoleDialog = false },
            title = {
                Text(
                    text = "Select User Role",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RoleOptionCard(
                        title = "Parent / Student Portal",
                        description = "Access student performance, homework, attendance & fee status",
                        icon = Icons.Default.FamilyRestroom,
                        isSelected = currentRole == UserRole.PARENT_STUDENT,
                        onClick = {
                            onRoleSelect(UserRole.PARENT_STUDENT)
                            showRoleDialog = false
                        }
                    )

                    RoleOptionCard(
                        title = "Teacher Portal",
                        description = "Upload homework, diary, and manage assigned classes",
                        icon = Icons.Default.School,
                        isSelected = currentRole == UserRole.TEACHER,
                        onClick = {
                            onRoleSelect(UserRole.TEACHER)
                            showRoleDialog = false
                        }
                    )

                    RoleOptionCard(
                        title = "Admin Panel",
                        description = "Full control over school records, staff & announcements",
                        icon = Icons.Default.AdminPanelSettings,
                        isSelected = currentRole == UserRole.ADMIN,
                        onClick = {
                            onRoleSelect(UserRole.ADMIN)
                            showRoleDialog = false
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun RoleOptionCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) SunriseGoldContainer else SurfaceVariantLight,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, SunriseGold) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SunriseNavy else Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) SunriseGold else SunriseNavy,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SunriseNavy
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SunriseGold
                )
            }
        }
    }
}
