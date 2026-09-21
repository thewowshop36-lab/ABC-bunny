package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundManager
import com.example.data.model.AlphabetRepository
import com.example.ui.components.BunnyAvatar
import com.example.ui.components.CelebrationOverlay
import com.example.ui.components.TopBarHeader
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyCream
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnyPink
import com.example.ui.theme.BunnyPurple
import com.example.ui.theme.BunnySkyBlue
import com.example.ui.theme.BunnyYellow
import kotlinx.coroutines.delay
import kotlin.random.Random

data class BalloonItem(
    val id: Int,
    val char: Char,
    val color: Color,
    val xFraction: Float, // 0.1 to 0.85
    val yFraction: Float, // 0.1 to 0.75
    val popped: Boolean = false
)

@Composable
fun BalloonPopScreen(
    totalStars: Int,
    carrots: Int,
    soundManager: SoundManager,
    onBack: () -> Unit,
    onAwardEarned: (stars: Int, carrots: Int) -> Unit,
    onParentLockClick: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    val lettersPool = remember { AlphabetRepository.letters }
    var targetLetter by remember { mutableStateOf(lettersPool.random()) }
    val activeBalloons = remember { mutableStateListOf<BalloonItem>() }
    var nextBalloonId by remember { mutableIntStateOf(1) }
    var showRoundVictory by remember { mutableStateOf(false) }

    val balloonColors = listOf(
        BunnyCoral, BunnyOrange, BunnyYellow, BunnyMeadowGreen,
        BunnySkyBlue, BunnyPurple, BunnyPink
    )

    // Function to populate balloons
    fun spawnBalloons(targetChar: Char) {
        activeBalloons.clear()
        // 1 balloon is the target
        val targetBalloon = BalloonItem(
            id = nextBalloonId++,
            char = targetChar,
            color = balloonColors.random(),
            xFraction = Random.nextFloat() * 0.7f + 0.1f,
            yFraction = Random.nextFloat() * 0.6f + 0.15f
        )
        // 4 other random letters
        val otherLetters = lettersPool.filter { it.charUpper != targetChar }.shuffled().take(4)
        val others = otherLetters.map { other ->
            BalloonItem(
                id = nextBalloonId++,
                char = other.charUpper,
                color = balloonColors.random(),
                xFraction = Random.nextFloat() * 0.7f + 0.1f,
                yFraction = Random.nextFloat() * 0.6f + 0.15f
            )
        }
        val all = (listOf(targetBalloon) + others).shuffled()
        activeBalloons.addAll(all)
    }

    LaunchedEffect(targetLetter) {
        spawnBalloons(targetLetter.charUpper)
        soundManager.speak("Pop the letter ${targetLetter.charUpper}! ${targetLetter.word}!")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFBAE6FD),
                        Color(0xFFE0F2FE),
                        BunnyCream
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBarHeader(
                title = "Balloon Pop",
                totalStars = totalStars,
                carrots = carrots,
                onBack = onBack,
                onParentLockClick = onParentLockClick
            )

            // Mission prompt banner
            Surface(
                shape = RoundedCornerShape(24.dp),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎯", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Pop Letter ${targetLetter.charUpper}!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = BunnyDarkText
                            )
                            Text(
                                text = "${targetLetter.emoji} ${targetLetter.word}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BunnyOrange
                            )
                        }
                    }

                    // Audio replay prompt
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BunnyYellow)
                            .clickable {
                                soundManager.speak("Pop the letter ${targetLetter.charUpper}! ${targetLetter.word}!")
                            }
                            .testTag("hear_target_balloon"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VolumeUp,
                            contentDescription = "Speak Target",
                            tint = BunnyDarkText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Sky Playfield for Floating Balloons
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val areaWidth = maxWidth
                val areaHeight = maxHeight

                activeBalloons.forEach { balloon ->
                    val isTarget = balloon.char == targetLetter.charUpper

                    Box(
                        modifier = Modifier
                            .offset(
                                x = (areaWidth.value * balloon.xFraction).dp,
                                y = (areaHeight.value * balloon.yFraction).dp
                            )
                            .size(76.dp, 96.dp)
                            .clickable {
                                if (isTarget) {
                                    soundManager.playPopSound()
                                    score += 1
                                    streak += 1
                                    soundManager.playStarChime()
                                    onAwardEarned(1, 1)

                                    if (score % 4 == 0) {
                                        soundManager.playSuccessFanfare()
                                        showRoundVictory = true
                                    } else {
                                        targetLetter = lettersPool.random()
                                    }
                                } else {
                                    soundManager.triggerHaptic(60)
                                    soundManager.speak("That's letter ${balloon.char}! Find ${targetLetter.charUpper}!")
                                }
                            }
                            .testTag("balloon_${balloon.char}"),
                        contentAlignment = Alignment.Center
                    ) {
                        // Cartoon Balloon Body
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            // Oval balloon head
                            drawOval(
                                color = balloon.color,
                                topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                                size = androidx.compose.ui.geometry.Size(w, h * 0.82f)
                            )
                            // Shine highlight
                            drawOval(
                                color = Color.White.copy(alpha = 0.45f),
                                topLeft = androidx.compose.ui.geometry.Offset(w * 0.2f, h * 0.12f),
                                size = androidx.compose.ui.geometry.Size(w * 0.25f, h * 0.28f)
                            )
                            // Balloon knot
                            drawCircle(
                                color = balloon.color,
                                radius = 7f,
                                center = androidx.compose.ui.geometry.Offset(w / 2, h * 0.84f)
                            )
                            // String line
                            drawLine(
                                color = Color.Gray,
                                start = androidx.compose.ui.geometry.Offset(w / 2, h * 0.84f),
                                end = androidx.compose.ui.geometry.Offset(w / 2, h),
                                strokeWidth = 3f
                            )
                        }

                        // Big Letter on the balloon
                        Text(
                            text = balloon.char.toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 14.dp)
                        )
                    }
                }
            }

            // Bottom Bunny cheer bar
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BunnyAvatar(
                        speechText = if (streak > 2) "You're on fire! 🔥" else "Pop the balloons!",
                        sizeDp = 58
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = BunnyYellow,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Popped: $score 🎈",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BunnyDarkText,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        CelebrationOverlay(
            visible = showRoundVictory,
            title = "Balloon Master!",
            subtitle = "Great popping! You earned bonus stars & carrots!",
            starsCount = 3,
            onContinue = {
                showRoundVictory = false
                targetLetter = lettersPool.random()
            }
        )
    }
}
