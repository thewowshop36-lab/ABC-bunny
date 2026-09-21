package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.audio.SoundManager
import com.example.ui.components.TopBarHeader
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyCream
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnyPink
import com.example.ui.theme.BunnySkyBlue
import com.example.ui.theme.BunnyStarGold
import com.example.ui.theme.BunnyYellow

data class StickerDefinition(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String
)

val ALL_STICKERS = listOf(
    StickerDefinition("carrot", "Super Carrot", "🥕", "Traced 3 letters"),
    StickerDefinition("star_gold", "Golden Star", "⭐", "Mastered A-to-Z strokes"),
    StickerDefinition("magic_wand", "Magic Wand", "🪄", "Popped 5 balloons"),
    StickerDefinition("rainbow_bunny", "Rainbow Bunny", "🐰", "Fed Bunny treats"),
    StickerDefinition("cupcake", "Sweet Cupcake", "🧁", "Solved 3 puzzles"),
    StickerDefinition("rocket", "Space Rocket", "🚀", "Completed matching game"),
    StickerDefinition("trophy", "Alphabet Trophy", "🏆", "Learned 10 letters"),
    StickerDefinition("butterfly", "Rainbow Butterfly", "🦋", "Daily learning streak"),
    StickerDefinition("crown", "Queen Crown", "👑", "Perfect letter trace"),
    StickerDefinition("balloon", "Party Balloon", "🎈", "Balloon pop master"),
    StickerDefinition("music", "Dancing Notes", "🎵", "Listened to all phonics"),
    StickerDefinition("dino", "Baby Dino", "🦖", "Preschool Explorer")
)

@Composable
fun StickerBookScreen(
    totalStars: Int,
    carrots: Int,
    unlockedStickersCsv: String,
    soundManager: SoundManager,
    onBack: () -> Unit,
    onParentLockClick: () -> Unit
) {
    val unlockedSet = remember(unlockedStickersCsv) {
        unlockedStickersCsv.split(",").map { it.trim() }.toSet()
    }
    var selectedSticker by remember { mutableStateOf<StickerDefinition?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BunnyCream)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarHeader(
                title = "Sticker Book",
                totalStars = totalStars,
                carrots = carrots,
                onBack = onBack,
                onParentLockClick = onParentLockClick
            )

            // Header info card
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "My Digital Stickers",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = BunnyDarkText
                        )
                        Text(
                            text = "Collected: ${ALL_STICKERS.count { unlockedSet.contains(it.id) }} of ${ALL_STICKERS.size}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BunnyOrange
                        )
                    }

                    Text(text = "✨ 🌟 ✨", fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sticker Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(ALL_STICKERS) { sticker ->
                    val isUnlocked = unlockedSet.contains(sticker.id)

                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = if (isUnlocked) Color.White else Color(0xFFF0F0F0),
                        shadowElevation = if (isUnlocked) 6.dp else 1.dp,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .border(
                                3.dp,
                                if (isUnlocked) BunnyYellow else Color(0xFFDCDCDC),
                                RoundedCornerShape(22.dp)
                            )
                            .clickable {
                                if (isUnlocked) {
                                    soundManager.playSparkle()
                                    soundManager.speak("${sticker.name}! ${sticker.description}")
                                    selectedSticker = sticker
                                } else {
                                    soundManager.triggerHaptic(40)
                                    soundManager.speak("Locked sticker! Complete letters to unlock!")
                                }
                            }
                            .testTag("sticker_${sticker.id}")
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isUnlocked) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = sticker.emoji, fontSize = 42.sp)
                                    Text(
                                        text = sticker.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BunnyDarkText
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Locked",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom detail if selected
            if (selectedSticker != null) {
                Surface(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = selectedSticker!!.emoji, fontSize = 46.sp)
                        Spacer(modifier = Modifier.size(14.dp))
                        Column {
                            Text(
                                text = selectedSticker!!.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = BunnyDarkText
                            )
                            Text(
                                text = selectedSticker!!.description,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
