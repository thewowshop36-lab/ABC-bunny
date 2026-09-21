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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundManager
import com.example.data.model.AlphabetRepository
import com.example.data.model.LetterItem
import com.example.ui.components.BunnyAvatar
import com.example.ui.components.CelebrationOverlay
import com.example.ui.components.TopBarHeader
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyCream
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnySkyBlue
import com.example.ui.theme.BunnyStarGold
import com.example.ui.theme.BunnyYellow

@Composable
fun PuzzleScreen(
    totalStars: Int,
    carrots: Int,
    soundManager: SoundManager,
    onBack: () -> Unit,
    onAwardEarned: (stars: Int, carrots: Int) -> Unit,
    onParentLockClick: () -> Unit
) {
    val allLetters = remember { AlphabetRepository.letters }
    var currentItemIndex by remember { mutableIntStateOf(0) }
    val currentItem = allLetters[currentItemIndex]

    var letterPlaced by remember(currentItemIndex) { mutableStateOf(false) }
    var picturePlaced by remember(currentItemIndex) { mutableStateOf(false) }
    var wordPlaced by remember(currentItemIndex) { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }

    // Choices presented to child (1 correct, 2 distractors)
    val letterChoices = remember(currentItemIndex) {
        val distractors = allLetters.filter { it.charUpper != currentItem.charUpper }.shuffled().take(2)
        (listOf(currentItem) + distractors).shuffled()
    }

    LaunchedEffect(currentItemIndex) {
        soundManager.speak("Solve the puzzle for ${currentItem.word}!")
    }

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
                title = "Puzzle Challenge",
                totalStars = totalStars,
                carrots = carrots,
                onBack = onBack,
                onParentLockClick = onParentLockClick
            )

            // Mission prompt banner
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
                    Text(
                        text = "Build the Phonics Card!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = BunnyDarkText
                    )

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BunnyYellow)
                            .clickable {
                                currentItemIndex = (currentItemIndex + 1) % allLetters.size
                            }
                            .testTag("puzzle_next_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "New Puzzle",
                            tint = BunnyDarkText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Big Main Puzzle Frame with 3 slots (Letter slot, Picture slot, Word slot)
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .border(4.dp, BunnySkyBlue, RoundedCornerShape(26.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (letterPlaced && picturePlaced && wordPlaced) "Complete!" else "Tap the pieces below to fit them in!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (letterPlaced && picturePlaced && wordPlaced) BunnyMeadowGreen else BunnyOrange
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Slot 1: Letter
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (letterPlaced) BunnyYellow else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .size(90.dp)
                                .border(
                                    3.dp,
                                    if (letterPlaced) BunnyOrange else Color(0xFFCBD5E1),
                                    RoundedCornerShape(18.dp)
                                )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (letterPlaced) {
                                    Text(
                                        text = currentItem.charUpper.toString(),
                                        fontSize = 44.sp,
                                        fontWeight = FontWeight.Black,
                                        color = BunnyDarkText
                                    )
                                } else {
                                    Text(
                                        text = "?",
                                        fontSize = 32.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Slot 2: Picture / Emoji
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (picturePlaced) BunnyMeadowGreen.copy(alpha = 0.25f) else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .size(90.dp)
                                .border(
                                    3.dp,
                                    if (picturePlaced) BunnyMeadowGreen else Color(0xFFCBD5E1),
                                    RoundedCornerShape(18.dp)
                                )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (picturePlaced) {
                                    Text(
                                        text = currentItem.emoji,
                                        fontSize = 46.sp
                                    )
                                } else {
                                    Text(
                                        text = "🐾",
                                        fontSize = 32.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Slot 3: Word
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (wordPlaced) BunnySkyBlue.copy(alpha = 0.25f) else Color(0xFFF1F5F9),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(52.dp)
                            .border(
                                2.dp,
                                if (wordPlaced) BunnySkyBlue else Color(0xFFCBD5E1),
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (wordPlaced) {
                                Text(
                                    text = currentItem.word,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BunnyDarkText
                                )
                            } else {
                                Text(
                                    text = "--- Word Slot ---",
                                    fontSize = 15.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pieces Tray below
            Text(
                text = "Pick the matching piece:",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = BunnyDarkText
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                letterChoices.forEach { choice ->
                    val isCurrent = choice.charUpper == currentItem.charUpper

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(96.dp)
                            .border(3.dp, BunnyOrange, RoundedCornerShape(20.dp))
                            .clickable {
                                if (isCurrent) {
                                    soundManager.playSparkle()
                                    if (!letterPlaced) {
                                        letterPlaced = true
                                        soundManager.speak("Letter ${choice.charUpper}!")
                                    } else if (!picturePlaced) {
                                        picturePlaced = true
                                        soundManager.speak(choice.word)
                                    } else if (!wordPlaced) {
                                        wordPlaced = true
                                        soundManager.playStarChime()
                                        soundManager.speakLetterPhonics(choice.charUpper, choice.word, choice.phonicsSound)
                                        onAwardEarned(2, 2)
                                        showCelebration = true
                                    }
                                } else {
                                    soundManager.triggerHaptic(50)
                                    soundManager.speak("That's ${choice.word}! Find ${currentItem.word}!")
                                }
                            }
                            .testTag("puzzle_piece_${choice.charUpper}")
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = choice.emoji, fontSize = 28.sp)
                            Text(
                                text = choice.charUpper.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = BunnyDarkText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Bunny cheer
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BunnyAvatar(
                        speechText = if (letterPlaced && picturePlaced && wordPlaced) "Awesome puzzle!" else "Tap the right card!",
                        sizeDp = 58
                    )
                }
            }
        }

        CelebrationOverlay(
            visible = showCelebration,
            title = "Puzzle Solved!",
            subtitle = "${currentItem.charUpper} is for ${currentItem.word}!",
            starsCount = 3,
            onContinue = {
                showCelebration = false
                currentItemIndex = (currentItemIndex + 1) % allLetters.size
            }
        )
    }
}
