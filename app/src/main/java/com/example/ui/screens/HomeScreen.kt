package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SoundManager
import com.example.ui.components.BunnyAvatar
import com.example.ui.components.TopBarHeader
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyCream
import com.example.ui.theme.BunnyDarkGreen
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyLavender
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnyPink
import com.example.ui.theme.BunnyPurple
import com.example.ui.theme.BunnySkyBlue
import com.example.ui.theme.BunnyStarGold
import com.example.ui.theme.BunnyYellow

@Composable
fun HomeScreen(
    totalStars: Int,
    carrots: Int,
    soundManager: SoundManager,
    onNavigateTracing: () -> Unit,
    onNavigateBalloonPop: () -> Unit,
    onNavigateLetterMatch: () -> Unit,
    onNavigatePuzzle: () -> Unit,
    onNavigatePetBunny: () -> Unit,
    onNavigateStickerBook: () -> Unit,
    onOpenParentLock: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "banner_bounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bannerBounce"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF7ED),
                        Color(0xFFE0F2FE),
                        BunnyCream
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                TopBarHeader(
                    title = "ABC Bunny",
                    totalStars = totalStars,
                    carrots = carrots,
                    onBack = null,
                    onParentLockClick = onOpenParentLock
                )
            }

            // Mascot & Title Banner
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .offset(y = bounceOffset.dp)
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(BunnyYellow)
                            .border(4.dp, BunnyOrange, CircleShape)
                            .clickable {
                                soundManager.playSparkle()
                                soundManager.speak("Hello little explorer! Let's trace ABCs together!")
                            }
                            .testTag("mascot_home_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_bunny_mascot),
                            contentDescription = "ABC Bunny",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(102.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ABC Bunny",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = BunnyOrange
                    )

                    Text(
                        text = "Tracing & Phonics World",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = BunnyDarkText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 3.dp,
                        modifier = Modifier.border(2.dp, BunnyYellow, RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            text = "✨ 100% Safe • Offline • Ad-Free ✨",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BunnyMeadowGreen,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 1. Primary Big Hero Button: Letter Tracing Adventure
            item {
                Spacer(modifier = Modifier.height(18.dp))
                HeroGameCard(
                    title = "Letter Tracing",
                    subtitle = "Learn A-Z with glowing arrows & cheerful phonics!",
                    badge = "Core Adventure",
                    bgColor = BunnyYellow,
                    accentColor = BunnyOrange,
                    emoji = "✍️",
                    onClick = {
                        soundManager.playSparkle()
                        onNavigateTracing()
                    },
                    testTag = "btn_play_tracing"
                )
            }

            // 2. Mini-Games Grid (Balloon Pop & Letter Match)
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    MiniGameSquareCard(
                        modifier = Modifier.weight(1f),
                        title = "Balloon Pop",
                        subtitle = "Find & pop letters!",
                        emoji = "🎈",
                        bgColor = BunnySkyBlue.copy(alpha = 0.2f),
                        borderColor = BunnySkyBlue,
                        onClick = {
                            soundManager.playPopSound()
                            onNavigateBalloonPop()
                        },
                        testTag = "btn_play_balloon_pop"
                    )

                    MiniGameSquareCard(
                        modifier = Modifier.weight(1f),
                        title = "Card Match",
                        subtitle = "Match words & ABCs!",
                        emoji = "🧩",
                        bgColor = BunnyMeadowGreen.copy(alpha = 0.2f),
                        borderColor = BunnyMeadowGreen,
                        onClick = {
                            soundManager.playSparkle()
                            onNavigateLetterMatch()
                        },
                        testTag = "btn_play_card_match"
                    )
                }
            }

            // 3. Mini-Games Row (Puzzle Challenge & Feed Virtual Bunny)
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    MiniGameSquareCard(
                        modifier = Modifier.weight(1f),
                        title = "Shape Puzzle",
                        subtitle = "Build phonics slots!",
                        emoji = "🔍",
                        bgColor = BunnyPurple.copy(alpha = 0.15f),
                        borderColor = BunnyPurple,
                        onClick = {
                            soundManager.playSparkle()
                            onNavigatePuzzle()
                        },
                        testTag = "btn_play_puzzle"
                    )

                    MiniGameSquareCard(
                        modifier = Modifier.weight(1f),
                        title = "Feed Bunny",
                        subtitle = "Carrots & happy pet!",
                        emoji = "🐰",
                        bgColor = BunnyCoral.copy(alpha = 0.18f),
                        borderColor = BunnyCoral,
                        onClick = {
                            soundManager.playChewSound()
                            onNavigatePetBunny()
                        },
                        testTag = "btn_play_feed_bunny"
                    )
                }
            }

            // 4. Digital Sticker Album
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .border(3.dp, BunnyStarGold, RoundedCornerShape(24.dp))
                        .clickable {
                            soundManager.playStarChime()
                            onNavigateStickerBook()
                        }
                        .testTag("btn_open_sticker_book")
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⭐", fontSize = 34.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Digital Sticker Book",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BunnyDarkText
                                )
                                Text(
                                    text = "Collect stickers from alphabet goals!",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Text(text = "➜", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BunnyStarGold)
                    }
                }
            }
        }
    }
}

@Composable
fun HeroGameCard(
    title: String,
    subtitle: String,
    badge: String,
    bgColor: Color,
    accentColor: Color,
    emoji: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(26.dp),
        color = bgColor,
        shadowElevation = 8.dp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .border(4.dp, accentColor, RoundedCornerShape(26.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor
                ) {
                    Text(
                        text = badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = BunnyDarkText
                )

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = BunnyDarkText.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, accentColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 34.sp)
            }
        }
    }
}

@Composable
fun MiniGameSquareCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    emoji: String,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = modifier
            .border(3.dp, borderColor, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .background(bgColor)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = BunnyDarkText
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
        }
    }
}
