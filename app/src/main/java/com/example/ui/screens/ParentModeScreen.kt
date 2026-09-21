package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.LetterProgressEntity
import com.example.data.db.UserProfileEntity
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyCream
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnySkyBlue
import com.example.ui.theme.BunnyStarGold
import com.example.ui.theme.BunnyYellow

@Composable
fun ParentModeScreen(
    userProfile: UserProfileEntity,
    progressList: List<LetterProgressEntity>,
    onBack: () -> Unit,
    onUpdateSettings: (tolerance: String, arrows: Boolean, letterSet: String, speechSpeed: Float) -> Unit,
    onResetProgress: () -> Unit
) {
    var tolerance by remember { mutableStateOf(userProfile.tracingTolerance) }
    var arrowsEnabled by remember { mutableStateOf(userProfile.guidanceArrows) }
    var letterSet by remember { mutableStateOf(userProfile.letterSetMode) }
    var speechSpeed by remember { mutableStateOf(userProfile.speechSpeed) }
    var showResetDialog by remember { mutableStateOf(false) }

    val masteredCount = progressList.count { it.isMastered }
    val practicedCount = progressList.size

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BunnyCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White, CircleShape)
                            .border(2.dp, BunnyOrange, CircleShape)
                            .clickable { onBack() }
                            .testTag("parent_back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BunnyOrange
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Parent Dashboard",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = BunnyDarkText
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BunnyCoral.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = BunnyCoral,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Secured",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BunnyCoral
                        )
                    }
                }
            }

            // 1. Progress Report Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📊 Child Learning Report Card",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = BunnyDarkText
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ReportStatItem(
                            label = "Mastered",
                            value = "$masteredCount / 26",
                            color = BunnyMeadowGreen,
                            emoji = "🏆"
                        )
                        ReportStatItem(
                            label = "Practiced",
                            value = "$practicedCount sets",
                            color = BunnySkyBlue,
                            emoji = "✍️"
                        )
                        ReportStatItem(
                            label = "Stars Earned",
                            value = "${userProfile.totalStars} ⭐",
                            color = BunnyStarGold,
                            emoji = "🌟"
                        )
                    }
                }
            }

            // 2. Learning & Tracing Difficulty Settings
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "⚙️ Tracing & Difficulty Settings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = BunnyDarkText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tracing Tolerance
                    Text(
                        text = "Tracing Tolerance (Finger Precision)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("EASY" to "Easy (Toddler)", "GENTLE" to "Gentle", "EXACT" to "Exact").forEach { (key, label) ->
                            val isSelected = tolerance == key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) BunnyOrange else Color(0xFFF1F5F9))
                                    .clickable {
                                        tolerance = key
                                        onUpdateSettings(tolerance, arrowsEnabled, letterSet, speechSpeed)
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else BunnyDarkText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Guidance Arrows Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Glowing Guide Arrows",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = BunnyDarkText
                            )
                            Text(
                                text = "Shows stroke checkpoints and directional arrows",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = arrowsEnabled,
                            onCheckedChange = {
                                arrowsEnabled = it
                                onUpdateSettings(tolerance, arrowsEnabled, letterSet, speechSpeed)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BunnyMeadowGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Letter Sets selection
                    Text(
                        text = "Curriculum / Letter Set",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("ALL" to "All A-Z", "VOWELS" to "Vowels", "BEGINNER" to "A-F Start").forEach { (key, label) ->
                            val isSelected = letterSet == key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) BunnySkyBlue else Color(0xFFF1F5F9))
                                    .clickable {
                                        letterSet = key
                                        onUpdateSettings(tolerance, arrowsEnabled, letterSet, speechSpeed)
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else BunnyDarkText
                                )
                            }
                        }
                    }
                }
            }

            // 3. Child Safety & Zero Disruption Guarantee
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = "Child Safety",
                        tint = BunnyMeadowGreen,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "100% Child-Safe & Ad-Free",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "No ads, no external links, zero in-app purchases, completely playable offline.",
                            fontSize = 13.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            // 4. Reset Data Option
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { showResetDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(2.dp, BunnyCoral),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("reset_progress_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Reset Progress",
                        tint = BunnyCoral,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reset All Progress",
                        color = BunnyCoral,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset Child's Progress?") },
                text = { Text("This will clear all star scores and letter mastery records so your child can start fresh.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showResetDialog = false
                            onResetProgress()
                        }
                    ) {
                        Text("Yes, Reset", color = BunnyCoral, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun ReportStatItem(
    label: String,
    value: String,
    color: Color,
    emoji: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.12f),
        modifier = Modifier.width(96.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = BunnyDarkText
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.DarkGray
            )
        }
    }
}
