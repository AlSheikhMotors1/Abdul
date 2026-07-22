package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.SchoolGalleryItem
import com.example.data.model.SchoolInfo
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAccessDetailScreen(
    itemId: String,
    schoolInfo: SchoolInfo,
    galleryItems: List<SchoolGalleryItem>,
    onBackClick: () -> Unit
) {
    val title = when (itemId) {
        "gallery" -> "School Gallery"
        "about" -> "About School & Principal Message"
        "admission" -> "Admission Information 2026-27"
        "calendar" -> "School Academic Calendar"
        "hw_downloads" -> "Homework Downloads & Resources"
        "result_downloads" -> "Result Cards Export"
        "location" -> "School Location & Map"
        "faqs" -> "School FAQs & Guidance"
        else -> "School Service"
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold, color = SunriseNavy) },
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
            when (itemId) {
                "gallery" -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(galleryItems) { item ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceLight,
                                shadowElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(SunriseNavyLight)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.img_school_banner),
                                            contentDescription = item.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SunriseNavy
                                    )
                                    Text(
                                        text = "Category: ${item.category} • Date: ${item.date}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                "about" -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceLight,
                                shadowElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "🏫 About Sunrise School Lodhra",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = SunriseNavy
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Sunrise School Lodhra is a premier educational institution in Punjab, Pakistan dedicated to fostering academic mastery, Islamic ethics, physical health, and modern analytical skills. We provide complete facilities from Play Group to Class 8.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF334155)
                                    )
                                }
                            }
                        }

                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SunriseNavy,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.img_principal_aftab_1784712687735),
                                            contentDescription = "Principal Muhammad Aftab Ali",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(CircleShape)
                                        )

                                        Column {
                                            Text(
                                                text = "Muhammad Aftab Ali",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "Principal & Managing Director",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = SunriseGoldLight
                                            )
                                            Text(
                                                text = "📞 03013662014",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.LightGray
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Principal's Message:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SunriseGold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "\"${schoolInfo.principalMessage}\"",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                "admission" -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceLight,
                                shadowElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "📝 Admission Criteria Session 2026-27",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = SunriseNavy
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = "Admissions are open for Play Group, Nursery, Prep, and Class 1 through Class 8.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF334155)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("Required Documents:", fontWeight = FontWeight.Bold, color = SunriseNavy)
                                    Text("1. Student B-Form / Birth Certificate Copy\n2. Father / Guardian CNIC Copy\n3. 4 passport size photos (White Background)\n4. Previous School Leaving Certificate (SLC)")

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            android.widget.Toast.makeText(context, "Contact Admission Office: ${schoolInfo.phone}", android.widget.Toast.LENGTH_LONG).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SunriseNavy)
                                    ) {
                                        Text("Apply For Admission")
                                    }
                                }
                            }
                        }
                    }
                }

                "calendar" -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceLight,
                                shadowElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("📅 Academic Calendar Highlights", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SunriseNavy)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("• 14 August 2026: Independence Day Function")
                                    Text("• 15 August 2026: Mid-Term Examinations Start")
                                    Text("• 06 September 2026: Defence Day Speeches")
                                    Text("• 25 December 2026: Winter Vacations Commence")
                                    Text("• 10 March 2027: Annual Final Examinations")
                                }
                            }
                        }
                    }
                }

                "faqs" -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val faqs = listOf(
                            "What are the school office hours?" to "School office remains open Monday to Saturday from 07:30 AM to 02:00 PM.",
                            "How is homework delivered to students?" to "Homework uploaded once by the teacher automatically appears for all students in that class.",
                            "How can parents check fee status?" to "Parents can check fee cleared/pending status under Student Dashboard.",
                            "How to contact the school administration?" to "Call official phone 03013662014 or message on Official WhatsApp 03013662014."
                        )

                        items(faqs) { (q, a) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SurfaceLight,
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Q: $q", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = SunriseNavy)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "A: $a", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF334155))
                                }
                            }
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Service available. Contact school office for details.", color = Color.Gray)
                    }
                }
            }
        }
    }
}
