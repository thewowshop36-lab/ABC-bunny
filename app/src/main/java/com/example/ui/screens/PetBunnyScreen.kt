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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.scale
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
import com.example.ui.components.TopBarHeader
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyCream
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnyOrange
import com.example.ui.theme.BunnyPink
import com.example.ui.theme.BunnySkyBlue
import com.example.ui.theme.BunnyYellow

@Composable
fun PetBunnyScreen(
    totalStars: Int,
    carrots: Int,
    bunnyHappiness: Int,
    soundManager: SoundManager,
    onBack: () -> Unit,
    onFeedBunny: () -> Unit,
    onParentLockClick: () -> Unit
) {
    var speechBubble by remember { mutableStateOf("Hi friend! I love carrots! 🥕") }
    var eatingAnimTrigger by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "bunny_pet_idle")
    val bounceY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF9C4),
                        Color(0xFFE8F5E9),
                        BunnyCream
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarHeader(
                title = "Bunny's Meadow",
                totalStars = totalStars,
                carrots = carrots,
                onBack = onBack,
                onParentLockClick = onParentLockClick
            )

            // Happiness Bar Card
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 6.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Happiness",
                                tint = BunnyPink,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Bunny Happiness",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = BunnyDarkText
                            )
                        }

                        Text(
                            text = "$bunnyHappiness%",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = BunnyOrange
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { bunnyHappiness / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = BunnyMeadowGreen,
                        trackColor = Color(0xFFE0E0E0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Speech Bubble from Bunny
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .border(3.dp, BunnyYellow, RoundedCornerShape(20.dp))
                    .padding(2.dp)
            ) {
                Text(
                    text = speechBubble,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BunnyDarkText,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Virtual Bunny Hero Visual
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .offset(y = bounceY.dp)
                    .shadow(12.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(6.dp, BunnyOrange, CircleShape)
                    .clickable {
                        soundManager.playSparkle()
                        soundManager.speak("Tee-hee! That tickles!")
                        speechBubble = "Tee-hee! I love you! ❤️"
                    }
                    .testTag("pet_bunny_interactive"),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bunny_pet_happy),
                    contentDescription = "Happy Pet Bunny",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Treats Tray
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Feed Bunny a Treat! (Carrots: $carrots)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BunnyDarkText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // 1. Crunchy Carrot
                        TreatButton(
                            emoji = "🥕",
                            label = "Carrot",
                            onClick = {
                                if (carrots > 0) {
                                    soundManager.playChewSound()
                                    onFeedBunny()
                                    speechBubble = "Crunch crunch! So yummy! 🥕"
                                    soundManager.speak("Crunch crunch! Delicious carrot!")
                                } else {
                                    speechBubble = "Trace more letters to earn carrots! 🥕"
                                    soundManager.speak("Trace more letters to earn crunchy carrots!")
                                }
                            }
                        )

                        // 2. Sweet Apple
                        TreatButton(
                            emoji = "🍎",
                            label = "Apple",
                            onClick = {
                                if (carrots > 0) {
                                    soundManager.playChewSound()
                                    onFeedBunny()
                                    speechBubble = "Juicy apple! My favorite! 🍎"
                                    soundManager.speak("Mmm! Juicy apple!")
                                } else {
                                    speechBubble = "Trace letters to get more food! 🌟"
                                    soundManager.speak("Trace letters to get more food!")
                                }
                            }
                        )

                        // 3. Strawberry
                        TreatButton(
                            emoji = "🍓",
                            label = "Berry",
                            onClick = {
                                if (carrots > 0) {
                                    soundManager.playChewSound()
                                    onFeedBunny()
                                    speechBubble = "Sweet strawberry! Yay! 🍓"
                                    soundManager.speak("Sweet strawberry! Yay!")
                                } else {
                                    speechBubble = "Complete games to earn treats! 🏆"
                                    soundManager.speak("Complete games to earn treats!")
                                }
                            }
                        )

                        // 4. Cupcake
                        TreatButton(
                            emoji = "🧁",
                            label = "Cupcake",
                            onClick = {
                                if (carrots > 0) {
                                    soundManager.playChewSound()
                                    onFeedBunny()
                                    speechBubble = "Party cupcake! Woohoo! 🧁"
                                    soundManager.speak("Party cupcake! Woohoo!")
                                } else {
                                    speechBubble = "Trace letters to unlock cupcakes! 🧁"
                                    soundManager.speak("Trace letters to unlock cupcakes!")
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TreatButton(
    emoji: String,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = BunnyCream,
        shadowElevation = 3.dp,
        modifier = Modifier
            .size(72.dp)
            .border(2.dp, BunnyOrange, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("treat_$label")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BunnyDarkText
            )
        }
    }
}
