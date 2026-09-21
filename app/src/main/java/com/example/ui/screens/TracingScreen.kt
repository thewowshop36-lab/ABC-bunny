package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlin.math.hypot

@Composable
fun TracingScreen(
    initialLetterChar: Char = 'A',
    totalStars: Int,
    carrots: Int,
    soundManager: SoundManager,
    onBack: () -> Unit,
    onLetterCompleted: (Char, Boolean, Int) -> Unit,
    onParentLockClick: () -> Unit
) {
    val allLetters = AlphabetRepository.letters
    var currentLetterChar by remember { mutableStateOf(initialLetterChar) }
    var isLowercase by remember { mutableStateOf(false) }

    val currentLetter = remember(currentLetterChar) {
        AlphabetRepository.getLetter(currentLetterChar)
    }

    val strokes = if (isLowercase) currentLetter.lowerStrokes else currentLetter.upperStrokes

    // Tracing state
    var currentStrokeIndex by remember(currentLetterChar, isLowercase) { mutableIntStateOf(0) }
    val completedStrokes = remember(currentLetterChar, isLowercase) { mutableStateListOf<List<Offset>>() }
    val currentDrawnPoints = remember(currentLetterChar, isLowercase) { mutableStateListOf<Offset>() }
    var showCelebration by remember { mutableStateOf(false) }

    // Touch tolerance radius in normalized fraction
    val tolerance = 0.16f

    // Animated glow pulse for arrow
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    // Initial phonics prompt on load
    LaunchedEffect(currentLetterChar, isLowercase) {
        soundManager.speakLetterPhonics(
            char = if (isLowercase) currentLetter.charLower else currentLetter.charUpper,
            word = currentLetter.word,
            phonicsSound = currentLetter.phonicsSound
        )
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
            // Header
            TopBarHeader(
                title = "Tracing Adventure",
                totalStars = totalStars,
                carrots = carrots,
                onBack = onBack,
                onParentLockClick = onParentLockClick
            )

            // Letter selector carousel
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allLetters) { item ->
                    val isSelected = item.charUpper == currentLetterChar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) BunnyOrange else Color.White
                            )
                            .border(
                                width = if (isSelected) 3.dp else 1.5.dp,
                                color = if (isSelected) BunnyYellow else Color(0xFFE0E0E0),
                                shape = CircleShape
                            )
                            .clickable {
                                currentLetterChar = item.charUpper
                                currentStrokeIndex = 0
                                completedStrokes.clear()
                                currentDrawnPoints.clear()
                            }
                            .testTag("select_letter_${item.charUpper}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isLowercase) item.charLower.toString() else item.charUpper.toString(),
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = if (isSelected) Color.White else BunnyDarkText
                        )
                    }
                }
            }

            // Word & Phonics Card
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = currentLetter.emoji, fontSize = 38.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "${if (isLowercase) currentLetter.charLower else currentLetter.charUpper} is for ${currentLetter.word}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = BunnyDarkText
                            )
                            Text(
                                text = currentLetter.phonicsSound,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = currentLetter.themeColor
                            )
                        }
                    }

                    // Voice repeat button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BunnyYellow)
                            .clickable {
                                soundManager.speakLetterPhonics(
                                    char = if (isLowercase) currentLetter.charLower else currentLetter.charUpper,
                                    word = currentLetter.word,
                                    phonicsSound = currentLetter.phonicsSound
                                )
                            }
                            .testTag("hear_phonics_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VolumeUp,
                            contentDescription = "Hear Phonics",
                            tint = BunnyDarkText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Tracing Board
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .aspectRatio(0.92f)
                    .shadow(8.dp, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .border(4.dp, BunnySkyBlue, RoundedCornerShape(28.dp))
                    .testTag("tracing_canvas_container"),
                contentAlignment = Alignment.Center
            ) {
                // Interactive Tracing Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .pointerInput(currentLetterChar, isLowercase, currentStrokeIndex) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val canvasW = size.width
                                    val canvasH = size.height
                                    val normX = offset.x / canvasW
                                    val normY = offset.y / canvasH

                                    if (currentStrokeIndex < strokes.size) {
                                        val activeStroke = strokes[currentStrokeIndex]
                                        val startPt = activeStroke.points.first()
                                        val distToStart = hypot(normX - startPt.x, normY - startPt.y)
                                        if (distToStart <= tolerance + 0.08f) {
                                            currentDrawnPoints.clear()
                                            currentDrawnPoints.add(offset)
                                            soundManager.triggerHaptic(20)
                                        }
                                    }
                                },
                                onDrag = { change, _ ->
                                    val canvasW = size.width
                                    val canvasH = size.height
                                    val pos = change.position
                                    currentDrawnPoints.add(pos)

                                    if (currentStrokeIndex < strokes.size) {
                                        val activeStroke = strokes[currentStrokeIndex]
                                        val targetEnd = activeStroke.points.last()
                                        val curNormX = pos.x / canvasW
                                        val curNormY = pos.y / canvasH
                                        val distToEnd = hypot(curNormX - targetEnd.x, curNormY - targetEnd.y)

                                        if (distToEnd <= tolerance) {
                                            // Completed this stroke!
                                            completedStrokes.add(currentDrawnPoints.toList())
                                            currentDrawnPoints.clear()
                                            soundManager.playSparkle()

                                            if (currentStrokeIndex + 1 < strokes.size) {
                                                currentStrokeIndex++
                                            } else {
                                                // All strokes completed!
                                                soundManager.playSuccessFanfare()
                                                soundManager.speakPraise()
                                                onLetterCompleted(currentLetter.charUpper, isLowercase, 3)
                                                showCelebration = true
                                            }
                                        }
                                    }
                                }
                            )
                        }
                ) {
                    val w = size.width
                    val h = size.height

                    // 1. Draw dashed guide paths for all strokes in this letter
                    strokes.forEachIndexed { strokeIdx, stroke ->
                        if (stroke.points.size >= 2) {
                            val guidePath = Path()
                            val first = stroke.points.first()
                            guidePath.moveTo(first.x * w, first.y * h)
                            for (i in 1 until stroke.points.size) {
                                val pt = stroke.points[i]
                                guidePath.lineTo(pt.x * w, pt.y * h)
                            }

                            // Dashed track
                            drawPath(
                                path = guidePath,
                                color = Color(0xFFE2E8F0),
                                style = Stroke(
                                    width = 46f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
                                )
                            )
                        }
                    }

                    // 2. Draw completed strokes
                    completedStrokes.forEach { pts ->
                        if (pts.size >= 2) {
                            val path = Path()
                            path.moveTo(pts[0].x, pts[0].y)
                            for (i in 1 until pts.size) {
                                path.lineTo(pts[i].x, pts[i].y)
                            }
                            drawPath(
                                path = path,
                                brush = Brush.linearGradient(
                                    listOf(BunnyOrange, BunnyCoral, BunnyYellow)
                                ),
                                style = Stroke(
                                    width = 42f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }

                    // 3. Draw currently active user drawn path
                    if (currentDrawnPoints.size >= 2) {
                        val path = Path()
                        path.moveTo(currentDrawnPoints[0].x, currentDrawnPoints[0].y)
                        for (i in 1 until currentDrawnPoints.size) {
                            path.lineTo(currentDrawnPoints[i].x, currentDrawnPoints[i].y)
                        }
                        drawPath(
                            path = path,
                            brush = Brush.linearGradient(
                                listOf(BunnySkyBlue, BunnyMeadowGreen)
                            ),
                            style = Stroke(
                                width = 42f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }

                    // 4. Draw active stroke hints: glowing start circle and end checkpoint
                    if (currentStrokeIndex < strokes.size) {
                        val active = strokes[currentStrokeIndex]
                        val startPt = active.points.first()
                        val endPt = active.points.last()

                        val startPx = Offset(startPt.x * w, startPt.y * h)
                        val endPx = Offset(endPt.x * w, endPt.y * h)

                        // Glowing pulsing circle at start point
                        drawCircle(
                            color = BunnyYellow.copy(alpha = pulseAlpha),
                            radius = 34f,
                            center = startPx
                        )
                        drawCircle(
                            color = BunnyOrange,
                            radius = 22f,
                            center = startPx
                        )
                        // Star at start point
                        drawCircle(
                            color = Color.White,
                            radius = 10f,
                            center = startPx
                        )

                        // Target checkpoint at end point
                        drawCircle(
                            color = BunnyMeadowGreen,
                            radius = 22f,
                            center = endPx
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 9f,
                            center = endPx
                        )
                    }
                }

                // Friendly prompt banner at bottom of card
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .background(BunnyYellow.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (currentStrokeIndex < strokes.size) {
                            "Follow arrow: Stroke ${currentStrokeIndex + 1} of ${strokes.size}"
                        } else {
                            "Letter Mastered! ⭐"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BunnyDarkText
                    )
                }
            }

            // Bottom action bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Uppercase / Lowercase toggle
                Row(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .border(2.dp, BunnyOrange, RoundedCornerShape(20.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (!isLowercase) BunnyOrange else Color.Transparent)
                            .clickable {
                                isLowercase = false
                                currentStrokeIndex = 0
                                completedStrokes.clear()
                                currentDrawnPoints.clear()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("toggle_uppercase"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ABC",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = if (!isLowercase) Color.White else BunnyDarkText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isLowercase) BunnyOrange else Color.Transparent)
                            .clickable {
                                isLowercase = true
                                currentStrokeIndex = 0
                                completedStrokes.clear()
                                currentDrawnPoints.clear()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("toggle_lowercase"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "abc",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = if (isLowercase) Color.White else BunnyDarkText
                        )
                    }
                }

                // Reset letter stroke canvas
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                        .border(2.dp, BunnyCoral, CircleShape)
                        .clickable {
                            currentStrokeIndex = 0
                            completedStrokes.clear()
                            currentDrawnPoints.clear()
                            soundManager.triggerHaptic(25)
                        }
                        .testTag("reset_tracing_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Erase & Try Again",
                        tint = BunnyCoral,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Next Letter button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(BunnyMeadowGreen, CircleShape)
                        .clickable {
                            val curIdx = allLetters.indexOfFirst { it.charUpper == currentLetterChar }
                            val nextIdx = (curIdx + 1) % allLetters.size
                            currentLetterChar = allLetters[nextIdx].charUpper
                            currentStrokeIndex = 0
                            completedStrokes.clear()
                            currentDrawnPoints.clear()
                        }
                        .testTag("next_letter_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Letter",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Celebration dialog overlay
        CelebrationOverlay(
            visible = showCelebration,
            title = "Star Tracing!",
            subtitle = "${currentLetter.charUpper} is for ${currentLetter.word}!",
            starsCount = 3,
            onContinue = {
                showCelebration = false
                val curIdx = allLetters.indexOfFirst { it.charUpper == currentLetterChar }
                val nextIdx = (curIdx + 1) % allLetters.size
                currentLetterChar = allLetters[nextIdx].charUpper
                currentStrokeIndex = 0
                completedStrokes.clear()
                currentDrawnPoints.clear()
            }
        )
    }
}
