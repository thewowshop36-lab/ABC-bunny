package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "letter_progress")
data class LetterProgressEntity(
    @PrimaryKey val letter: String, // e.g. "A_UPPER" or "A_LOWER"
    val charValue: Char,
    val isLowercase: Boolean,
    val stars: Int = 0,
    val practiceCount: Int = 0,
    val isMastered: Boolean = false,
    val lastPracticedTimestamp: Long = System.currentTimeMillis()
)
