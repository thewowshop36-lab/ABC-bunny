package com.example.data.repository

import com.example.data.db.GameDao
import com.example.data.db.LetterProgressEntity
import com.example.data.db.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository(private val gameDao: GameDao) {

    val allLetterProgress: Flow<List<LetterProgressEntity>> = gameDao.getAllLetterProgress()

    val userProfile: Flow<UserProfileEntity> = gameDao.getUserProfile().map { profile ->
        profile ?: UserProfileEntity()
    }

    suspend fun getProfileOnce(): UserProfileEntity {
        return gameDao.getUserProfileOnce() ?: UserProfileEntity().also {
            gameDao.saveUserProfile(it)
        }
    }

    suspend fun saveLetterCompletion(
        charUpper: Char,
        isLowercase: Boolean,
        starsEarned: Int
    ) {
        val key = "${charUpper}_${if (isLowercase) "LOWER" else "UPPER"}"
        val existing = gameDao.getProgressForLetter(key)
        val newPracticeCount = (existing?.practiceCount ?: 0) + 1
        val newStars = maxOf(existing?.stars ?: 0, starsEarned)
        val entity = LetterProgressEntity(
            letter = key,
            charValue = charUpper,
            isLowercase = isLowercase,
            stars = newStars,
            practiceCount = newPracticeCount,
            isMastered = newStars >= 2,
            lastPracticedTimestamp = System.currentTimeMillis()
        )
        gameDao.saveLetterProgress(entity)

        // Award stars and food to profile
        val profile = getProfileOnce()
        val updatedProfile = profile.copy(
            totalStars = profile.totalStars + starsEarned,
            carrotsAvailable = profile.carrotsAvailable + 1,
            bunnyHappiness = minOf(100, profile.bunnyHappiness + 5)
        )
        gameDao.saveUserProfile(updatedProfile)
    }

    suspend fun feedBunny(foodType: String): Boolean {
        val profile = getProfileOnce()
        if (profile.carrotsAvailable <= 0) return false
        val newHappiness = minOf(100, profile.bunnyHappiness + 15)
        val updated = profile.copy(
            carrotsAvailable = profile.carrotsAvailable - 1,
            bunnyHappiness = newHappiness
        )
        gameDao.saveUserProfile(updated)
        return true
    }

    suspend fun unlockSticker(stickerId: String) {
        val profile = getProfileOnce()
        val currentList = profile.unlockedStickers.split(",").map { it.trim() }.toMutableSet()
        if (!currentList.contains(stickerId)) {
            currentList.add(stickerId)
            gameDao.saveUserProfile(profile.copy(unlockedStickers = currentList.joinToString(",")))
        }
    }

    suspend fun updateSettings(
        tolerance: String,
        arrows: Boolean,
        letterSet: String,
        speechSpeed: Float
    ) {
        val profile = getProfileOnce()
        val updated = profile.copy(
            tracingTolerance = tolerance,
            guidanceArrows = arrows,
            letterSetMode = letterSet,
            speechSpeed = speechSpeed
        )
        gameDao.saveUserProfile(updated)
    }

    suspend fun resetProgress() {
        gameDao.resetAllLetterProgress()
        val defaultProfile = UserProfileEntity()
        gameDao.saveUserProfile(defaultProfile)
    }
}
