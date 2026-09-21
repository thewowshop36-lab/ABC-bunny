package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.db.AppDatabase
import com.example.data.db.LetterProgressEntity
import com.example.data.db.UserProfileEntity
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CurrentScreen {
    HOME,
    TRACING,
    BALLOON_POP,
    LETTER_MATCH,
    PUZZLE,
    PET_BUNNY,
    STICKER_BOOK,
    PARENT_MODE
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository
    val soundManager: SoundManager = SoundManager(application)

    private val _currentScreen = MutableStateFlow(CurrentScreen.HOME)
    val currentScreen: StateFlow<CurrentScreen> = _currentScreen.asStateFlow()

    private val _showMathGate = MutableStateFlow(false)
    val showMathGate: StateFlow<Boolean> = _showMathGate.asStateFlow()

    init {
        val database = AppDatabase.getInstance(application)
        repository = GameRepository(database.gameDao())
    }

    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileEntity()
        )

    val allLetterProgress: StateFlow<List<LetterProgressEntity>> = repository.allLetterProgress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun navigateTo(screen: CurrentScreen) {
        _currentScreen.value = screen
    }

    fun openParentGate() {
        _showMathGate.value = true
    }

    fun dismissParentGate() {
        _showMathGate.value = false
    }

    fun onMathGatePassed() {
        _showMathGate.value = false
        _currentScreen.value = CurrentScreen.PARENT_MODE
    }

    fun onLetterCompleted(char: Char, isLowercase: Boolean, stars: Int) {
        viewModelScope.launch {
            repository.saveLetterCompletion(char, isLowercase, stars)
            // Check for sticker unlocks
            if (char == 'A') repository.unlockSticker("carrot")
            if (char == 'B') repository.unlockSticker("rainbow_bunny")
            if (char == 'Z') repository.unlockSticker("star_gold")
            if (stars >= 3) repository.unlockSticker("crown")
        }
    }

    fun onAwardEarned(stars: Int, carrots: Int) {
        viewModelScope.launch {
            val profile = repository.getProfileOnce()
            val updated = profile.copy(
                totalStars = profile.totalStars + stars,
                carrotsAvailable = profile.carrotsAvailable + carrots,
                bunnyHappiness = minOf(100, profile.bunnyHappiness + 2)
            )
            AppDatabase.getInstance(getApplication()).gameDao().saveUserProfile(updated)
            if (updated.totalStars >= 25) repository.unlockSticker("trophy")
        }
    }

    fun feedBunny() {
        viewModelScope.launch {
            repository.feedBunny("carrot")
            repository.unlockSticker("rainbow_bunny")
        }
    }

    fun updateSettings(
        tolerance: String,
        arrows: Boolean,
        letterSet: String,
        speechSpeed: Float
    ) {
        viewModelScope.launch {
            repository.updateSettings(tolerance, arrows, letterSet, speechSpeed)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetProgress()
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.shutdown()
    }
}
