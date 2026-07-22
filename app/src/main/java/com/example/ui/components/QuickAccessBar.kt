package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class QuickAccessItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun QuickAccessSection(
    onItemClick: (String) -> Unit
) {
    val quickAccessItems = listOf(
        QuickAccessItem("gallery", "School Gallery", Icons.Outlined.Collections, Color(0xFF0284C7)),
        QuickAccessItem("about", "About School", Icons.Outlined.Info, Color(0xFF0D9488)),
        QuickAccessItem("admission", "Admission Info", Icons.Outlined.AppRegistration, Color(0xFFD97706)),
        QuickAccessItem("calendar", "School Calendar", Icons.Outlined.CalendarMonth, Color(0xFF7C3AED)),
        QuickAccessItem("hw_downloads", "Homework Downloads", Icons.Outlined.Download, Color(0xFF2563EB)),
        QuickAccessItem("result_downloads", "Result Downloads", Icons.Outlined.Assessment, Color(0xFF059669)),
        QuickAccessItem("location", "School Location", Icons.Outlined.LocationOn, Color(0xFFDC2626)),
        QuickAccessItem("call", "Call School", Icons.Outlined.Phone, Color(0xFF16A34A)),
        QuickAccessItem("whatsapp", "Official WhatsApp", Icons.Outlined.Chat, Color(0xFF128C7E)),
        QuickAccessItem("faqs", "School FAQs", Icons.Outlined.Quiz, Color(0xFFEA580C))
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚡ Quick Access Services",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SunriseNavy
            )
            Text(
                text = "10 Direct Tools",
                style = MaterialTheme.typography.labelMedium,
                color = SunriseGold,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceLight,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Display as a 5-column grid x 2 rows
                val row1 = quickAccessItems.take(5)
                val row2 = quickAccessItems.drop(5)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row1.forEach { item ->
                        QuickAccessGridButton(
                            item = item,
                            onClick = { onItemClick(item.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row2.forEach { item ->
                        QuickAccessGridButton(
                            item = item,
                            onClick = { onItemClick(item.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickAccessGridButton(
    item: QuickAccessItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(item.color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.color,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155),
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 12.sp
        )
    }
}
