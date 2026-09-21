package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CurrentScreen
import com.example.ui.GameViewModel
import com.example.ui.components.MathGateDialog
import com.example.ui.screens.BalloonPopScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LetterMatchScreen
import com.example.ui.screens.ParentModeScreen
import com.example.ui.screens.PetBunnyScreen
import com.example.ui.screens.PuzzleScreen
import com.example.ui.screens.StickerBookScreen
import com.example.ui.screens.TracingScreen
import com.example.ui.theme.ABCBunnyTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ABCBunnyTheme {
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                val profile by viewModel.userProfile.collectAsStateWithLifecycle()
                val letterProgress by viewModel.allLetterProgress.collectAsStateWithLifecycle()
                val showMathGate by viewModel.showMathGate.collectAsStateWithLifecycle()

                // Intercept back presses to stay inside child-friendly game hub
                BackHandler(enabled = currentScreen != CurrentScreen.HOME) {
                    viewModel.navigateTo(CurrentScreen.HOME)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Crossfade(
                            targetState = currentScreen,
                            animationSpec = tween(350),
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                CurrentScreen.HOME -> {
                                    HomeScreen(
                                        totalStars = profile.totalStars,
                                        carrots = profile.carrotsAvailable,
                                        soundManager = viewModel.soundManager,
                                        onNavigateTracing = { viewModel.navigateTo(CurrentScreen.TRACING) },
                                        onNavigateBalloonPop = { viewModel.navigateTo(CurrentScreen.BALLOON_POP) },
                                        onNavigateLetterMatch = { viewModel.navigateTo(CurrentScreen.LETTER_MATCH) },
                                        onNavigatePuzzle = { viewModel.navigateTo(CurrentScreen.PUZZLE) },
                                        onNavigatePetBunny = { viewModel.navigateTo(CurrentScreen.PET_BUNNY) },
                                        onNavigateStickerBook = { viewModel.navigateTo(CurrentScreen.STICKER_BOOK) },
                                        onOpenParentLock = { viewModel.openParentGate() }
                                    )
                                }

                                CurrentScreen.TRACING -> {
                                    TracingScreen(
                                        initialLetterChar = 'A',
                                        totalStars = profile.totalStars,
                                        carrots = profile.carrotsAvailable,
                                        soundManager = viewModel.soundManager,
                                        onBack = { viewModel.navigateTo(CurrentScreen.HOME) },
                                        onLetterCompleted = { char, isLower, stars ->
                                            viewModel.onLetterCompleted(char, isLower, stars)
                                        },
                                        onParentLockClick = { viewModel.openParentGate() }
                                    )
                                }

                                CurrentScreen.BALLOON_POP -> {
                                    BalloonPopScreen(
                                        totalStars = profile.totalStars,
                                        carrots = profile.carrotsAvailable,
                                        soundManager = viewModel.soundManager,
                                        onBack = { viewModel.navigateTo(CurrentScreen.HOME) },
                                        onAwardEarned = { stars, carrots ->
                                            viewModel.onAwardEarned(stars, carrots)
                                        },
                                        onParentLockClick = { viewModel.openParentGate() }
                                    )
                                }

                                CurrentScreen.LETTER_MATCH -> {
                                    LetterMatchScreen(
                                        totalStars = profile.totalStars,
                                        carrots = profile.carrotsAvailable,
                                        soundManager = viewModel.soundManager,
                                        onBack = { viewModel.navigateTo(CurrentScreen.HOME) },
                                        onAwardEarned = { stars, carrots ->
                                            viewModel.onAwardEarned(stars, carrots)
                                        },
                                        onParentLockClick = { viewModel.openParentGate() }
                                    )
                                }

                                CurrentScreen.PUZZLE -> {
                                    PuzzleScreen(
                                        totalStars = profile.totalStars,
                                        carrots = profile.carrotsAvailable,
                                        soundManager = viewModel.soundManager,
                                        onBack = { viewModel.navigateTo(CurrentScreen.HOME) },
                                        onAwardEarned = { stars, carrots ->
                                            viewModel.onAwardEarned(stars, carrots)
                                        },
                                        onParentLockClick = { viewModel.openParentGate() }
                                    )
                                }

                                CurrentScreen.PET_BUNNY -> {
                                    PetBunnyScreen(
                                        totalStars = profile.totalStars,
                                        carrots = profile.carrotsAvailable,
                                        bunnyHappiness = profile.bunnyHappiness,
                                        soundManager = viewModel.soundManager,
                                        onBack = { viewModel.navigateTo(CurrentScreen.HOME) },
                                        onFeedBunny = { viewModel.feedBunny() },
                                        onParentLockClick = { viewModel.openParentGate() }
                                    )
                                }

                                CurrentScreen.STICKER_BOOK -> {
                                    StickerBookScreen(
                                        totalStars = profile.totalStars,
                                        carrots = profile.carrotsAvailable,
                                        unlockedStickersCsv = profile.unlockedStickers,
                                        soundManager = viewModel.soundManager,
                                        onBack = { viewModel.navigateTo(CurrentScreen.HOME) },
                                        onParentLockClick = { viewModel.openParentGate() }
                                    )
                                }

                                CurrentScreen.PARENT_MODE -> {
                                    ParentModeScreen(
                                        userProfile = profile,
                                        progressList = letterProgress,
                                        onBack = { viewModel.navigateTo(CurrentScreen.HOME) },
                                        onUpdateSettings = { tolerance, arrows, letterSet, speed ->
                                            viewModel.updateSettings(tolerance, arrows, letterSet, speed)
                                        },
                                        onResetProgress = { viewModel.resetProgress() }
                                    )
                                }
                            }
                        }

                        // Math Gate Dialog for Parent Mode
                        if (showMathGate) {
                            MathGateDialog(
                                onDismiss = { viewModel.dismissParentGate() },
                                onSuccess = { viewModel.onMathGatePassed() }
                            )
                        }
                    }
                }
            }
        }
    }
}
