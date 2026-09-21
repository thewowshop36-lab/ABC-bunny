package com.example.ui.components

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnyYellow

@Composable
fun BunnyAvatar(
    speechText: String? = null,
    modifier: Modifier = Modifier,
    sizeDp: Int = 80,
    onBunnyClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bunny_bob")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bunny Mascot avatar with bouncing animation
        Box(
            modifier = Modifier
                .offset(y = offsetY.dp)
                .size(sizeDp.dp)
                .clip(CircleShape)
                .background(BunnyYellow)
                .border(3.dp, BunnyOrange, CircleShape)
                .clickable(enabled = onBunnyClick != null) { onBunnyClick?.invoke() }
                .testTag("bunny_guide_mascot"),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_bunny_mascot),
                contentDescription = "ABC Bunny Guide",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size((sizeDp - 4).dp)
            )
        }

        // Speech Bubble if provided
        if (!speechText.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(10.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .border(2.dp, BunnyYellow, RoundedCornerShape(16.dp))
                    .padding(2.dp)
            ) {
                Text(
                    text = speechText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BunnyDarkText,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}
