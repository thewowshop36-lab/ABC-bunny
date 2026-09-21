package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val totalStars: Int = 15,
    val bunnyHappiness: Int = 70, // 0 to 100
    val carrotsAvailable: Int = 5,
    val unlockedStickers: String = "carrot,star_gold,magic_wand", // comma separated
    val placedStickersJson: String = "", // coordinates if any
    val tracingTolerance: String = "GENTLE", // EASY, GENTLE, EXACT
    val guidanceArrows: Boolean = true,
    val letterSetMode: String = "ALL", // ALL, VOWELS, BEGINNER
    val speechSpeed: Float = 0.95f,
    val soundEffectsVolume: Float = 1.0f
)
