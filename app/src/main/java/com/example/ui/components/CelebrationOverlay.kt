package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnySkyBlue
import com.example.ui.theme.BunnyStarGold
import com.example.ui.theme.BunnyYellow
import kotlinx.coroutines.delay
import kotlin.random.Random

data class ConfettiPiece(
    val startX: Float,
    val startY: Float,
    val targetX: Float,
    val targetY: Float,
    val color: Color,
    val size: Float,
    val rotation: Float
)

@Composable
fun CelebrationOverlay(
    visible: Boolean,
    starsCount: Int = 3,
    title: String = "Terrific Job!",
    subtitle: String = "You mastered the letter!",
    onContinue: () -> Unit
) {
    if (!visible) return

    val scaleAnim = remember { Animatable(0.2f) }
    val confettiProgress = remember { Animatable(0f) }

    val confettiList = remember {
        val colors = listOf(BunnyCoral, BunnyOrange, BunnyYellow, BunnyMeadowGreen, BunnySkyBlue, BunnyStarGold)
        List(45) {
            ConfettiPiece(
                startX = 0.5f,
                startY = 0.4f,
                targetX = Random.nextFloat() * 1.2f - 0.1f,
                targetY = Random.nextFloat() * 1.1f,
                color = colors.random(),
                size = Random.nextFloat() * 16f + 10f,
                rotation = Random.nextFloat() * 360f
            )
        }
    }

    LaunchedEffect(visible) {
        if (visible) {
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(450, easing = FastOutSlowInEasing)
            )
            confettiProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(1200, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        // Confetti Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val p = confettiProgress.value
            val width = size.width
            val height = size.height
            confettiList.forEach { c ->
                val currentX = (c.startX + (c.targetX - c.startX) * p) * width
                val currentY = (c.startY + (c.targetY - c.startY) * p + (p * p * 150f)) * height
                drawRect(
                    color = c.color.copy(alpha = (1f - p * 0.4f).coerceIn(0f, 1f)),
                    topLeft = Offset(currentX, currentY),
                    size = Size(c.size, c.size * 0.6f)
                )
            }
        }

        // Popup Card
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier
                .padding(28.dp)
                .scale(scaleAnim.value)
                .testTag("celebration_popup")
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🐰 🎉",
                    fontSize = 44.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = BunnyCoral
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF555555)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Stars display
                Row {
                    repeat(3) { index ->
                        val earned = index < starsCount
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = if (earned) BunnyStarGold else Color(0xFFDDDDDD),
                            modifier = Modifier
                                .size(44.dp)
                                .padding(horizontal = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(containerColor = BunnyMeadowGreen),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(width = 200.dp, height = 54.dp)
                        .testTag("celebration_continue_button")
                ) {
                    Text(
                        text = "Next Letter ➜",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
