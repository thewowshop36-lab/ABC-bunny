package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Refresh
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
import com.example.ui.theme.BunnyYellow
import kotlinx.coroutines.delay

enum class CardType { LETTER, PICTURE }

data class MatchCard(
    val id: Int,
    val letterItem: LetterItem,
    val type: CardType,
    val isMatched: Boolean = false,
    val isSelected: Boolean = false
)

@Composable
fun LetterMatchScreen(
    totalStars: Int,
    carrots: Int,
    soundManager: SoundManager,
    onBack: () -> Unit,
    onAwardEarned: (stars: Int, carrots: Int) -> Unit,
    onParentLockClick: () -> Unit
) {
    val allLetters = remember { AlphabetRepository.letters }
    val cards = remember { mutableStateListOf<MatchCard>() }
    var firstSelectedId by remember { mutableStateOf<Int?>(null) }
    var matchedPairsCount by remember { mutableIntStateOf(0) }
    var showCelebration by remember { mutableStateOf(false) }

    fun startNewGame() {
        val selectedLetterItems = allLetters.shuffled().take(3)
        var cardId = 0
        val newCards = mutableListOf<MatchCard>()
        selectedLetterItems.forEach { item ->
            newCards.add(MatchCard(id = cardId++, letterItem = item, type = CardType.LETTER))
            newCards.add(MatchCard(id = cardId++, letterItem = item, type = CardType.PICTURE))
        }
        cards.clear()
        cards.addAll(newCards.shuffled())
        firstSelectedId = null
        matchedPairsCount = 0
        showCelebration = false
    }

    LaunchedEffect(Unit) {
        startNewGame()
        soundManager.speak("Match the letters with their pictures!")
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
                title = "Letter Matching",
                totalStars = totalStars,
                carrots = carrots,
                onBack = onBack,
                onParentLockClick = onParentLockClick
            )

            // Mission prompt card
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
                            text = "Find Matching Pairs!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = BunnyDarkText
                        )
                        Text(
                            text = "Pairs: $matchedPairsCount / 3",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BunnyOrange
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BunnyYellow)
                            .clickable { startNewGame() }
                            .testTag("match_restart_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "New Game",
                            tint = BunnyDarkText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2x3 Grid of Matching Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cards) { card ->
                    val isFirstSelected = firstSelectedId == card.id
                    val isMatched = card.isMatched

                    val bgColor by animateColorAsState(
                        targetValue = when {
                            isMatched -> BunnyMeadowGreen.copy(alpha = 0.35f)
                            isFirstSelected -> BunnyYellow
                            else -> Color.White
                        },
                        animationSpec = tween(250),
                        label = "card_color"
                    )

                    val borderColor = when {
                        isMatched -> BunnyMeadowGreen
                        isFirstSelected -> BunnyOrange
                        else -> BunnySkyBlue
                    }

                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = bgColor,
                        shadowElevation = if (isMatched) 1.dp else 6.dp,
                        modifier = Modifier
                            .aspectRatio(1.1f)
                            .border(3.dp, borderColor, RoundedCornerShape(22.dp))
                            .clickable(enabled = !isMatched && !isFirstSelected) {
                                soundManager.playSparkle()

                                if (firstSelectedId == null) {
                                    // First card clicked
                                    firstSelectedId = card.id
                                    soundManager.speak(
                                        if (card.type == CardType.LETTER) "Letter ${card.letterItem.charUpper}"
                                        else card.letterItem.word
                                    )
                                } else {
                                    // Second card clicked
                                    val firstCard = cards.first { it.id == firstSelectedId }
                                    if (firstCard.letterItem.charUpper == card.letterItem.charUpper && firstCard.id != card.id) {
                                        // MATCH!
                                        soundManager.playStarChime()
                                        soundManager.speak("Match! ${card.letterItem.charUpper} is for ${card.letterItem.word}!")
                                        onAwardEarned(1, 1)

                                        // Update cards
                                        val idx1 = cards.indexOfFirst { it.id == firstCard.id }
                                        val idx2 = cards.indexOfFirst { it.id == card.id }
                                        cards[idx1] = firstCard.copy(isMatched = true)
                                        cards[idx2] = card.copy(isMatched = true)
                                        matchedPairsCount += 1
                                        firstSelectedId = null

                                        if (matchedPairsCount >= 3) {
                                            soundManager.playSuccessFanfare()
                                            soundManager.speakPraise()
                                            showCelebration = true
                                        }
                                    } else {
                                        // NO MATCH
                                        soundManager.triggerHaptic(50)
                                        firstSelectedId = null
                                    }
                                }
                            }
                            .testTag("card_${card.id}_${card.letterItem.charUpper}"),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (card.type == CardType.LETTER) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${card.letterItem.charUpper} ${card.letterItem.charLower}",
                                        fontSize = 38.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isMatched) BunnyMeadowGreen else BunnyDarkText
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = card.letterItem.emoji,
                                        fontSize = 42.sp
                                    )
                                    Text(
                                        text = card.letterItem.word,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BunnyDarkText
                                    )
                                }
                            }
                        }
                    }
                }
            }

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
                        speechText = if (matchedPairsCount == 3) "Hooray! All matched!" else "Tap 2 cards to match!",
                        sizeDp = 58
                    )
                }
            }
        }

        CelebrationOverlay(
            visible = showCelebration,
            title = "Match Master!",
            subtitle = "You matched all the alphabet cards!",
            starsCount = 3,
            onContinue = {
                showCelebration = false
                startNewGame()
            }
        )
    }
}
