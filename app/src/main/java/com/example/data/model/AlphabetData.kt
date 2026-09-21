package com.example.data.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Normalized stroke segment for letter tracing.
 * Points are in [0f, 1f] coordinate space relative to the letter canvas.
 */
data class LetterStroke(
    val id: Int,
    val points: List<Offset>,
    val instruction: String = "Follow arrow"
)

data class LetterItem(
    val charUpper: Char,
    val charLower: Char,
    val word: String,
    val phonicsSound: String,
    val emoji: String,
    val themeColor: Color,
    val upperStrokes: List<LetterStroke>,
    val lowerStrokes: List<LetterStroke>
)

object AlphabetRepository {
    val letters: List<LetterItem> = listOf(
        LetterItem(
            charUpper = 'A',
            charLower = 'a',
            word = "Apple",
            phonicsSound = "/æ/ /æ/ Apple",
            emoji = "🍎",
            themeColor = Color(0xFFFF5252),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.5f, 0.12f), Offset(0.2f, 0.88f)), "Down left"),
                LetterStroke(2, listOf(Offset(0.5f, 0.12f), Offset(0.8f, 0.88f)), "Down right"),
                LetterStroke(3, listOf(Offset(0.32f, 0.58f), Offset(0.68f, 0.58f)), "Across")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.7f, 0.42f), Offset(0.38f, 0.45f), Offset(0.32f, 0.65f), Offset(0.5f, 0.85f), Offset(0.7f, 0.72f)), "Circle"),
                LetterStroke(2, listOf(Offset(0.7f, 0.35f), Offset(0.7f, 0.88f)), "Down line")
            )
        ),
        LetterItem(
            charUpper = 'B',
            charLower = 'b',
            word = "Bunny",
            phonicsSound = "/b/ /b/ Bunny",
            emoji = "🐰",
            themeColor = Color(0xFFFF9800),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.28f, 0.12f), Offset(0.28f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.28f, 0.14f), Offset(0.65f, 0.25f), Offset(0.62f, 0.48f), Offset(0.28f, 0.5f)), "Top curve"),
                LetterStroke(3, listOf(Offset(0.28f, 0.5f), Offset(0.72f, 0.65f), Offset(0.65f, 0.86f), Offset(0.28f, 0.88f)), "Bottom curve")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Down line"),
                LetterStroke(2, listOf(Offset(0.3f, 0.5f), Offset(0.68f, 0.5f), Offset(0.7f, 0.75f), Offset(0.3f, 0.88f)), "Round belly")
            )
        ),
        LetterItem(
            charUpper = 'C',
            charLower = 'c',
            word = "Cat",
            phonicsSound = "/k/ /k/ Cat",
            emoji = "🐱",
            themeColor = Color(0xFF4CAF50),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.78f, 0.22f), Offset(0.5f, 0.12f), Offset(0.24f, 0.5f), Offset(0.5f, 0.88f), Offset(0.78f, 0.78f)), "Big curve")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.72f, 0.42f), Offset(0.48f, 0.35f), Offset(0.28f, 0.62f), Offset(0.5f, 0.88f), Offset(0.72f, 0.8f)), "Little curve")
            )
        ),
        LetterItem(
            charUpper = 'D',
            charLower = 'd',
            word = "Duck",
            phonicsSound = "/d/ /d/ Duck",
            emoji = "🦆",
            themeColor = Color(0xFF00BCD4),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.28f, 0.12f), Offset(0.28f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.28f, 0.12f), Offset(0.75f, 0.35f), Offset(0.75f, 0.65f), Offset(0.28f, 0.88f)), "Big round back")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.68f, 0.55f), Offset(0.32f, 0.55f), Offset(0.32f, 0.82f), Offset(0.68f, 0.85f)), "Round tummy"),
                LetterStroke(2, listOf(Offset(0.68f, 0.12f), Offset(0.68f, 0.88f)), "Tall straight down")
            )
        ),
        LetterItem(
            charUpper = 'E',
            charLower = 'e',
            word = "Elephant",
            phonicsSound = "/ɛ/ /ɛ/ Elephant",
            emoji = "🐘",
            themeColor = Color(0xFF7C4DFF),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.3f, 0.14f), Offset(0.75f, 0.14f)), "Top across"),
                LetterStroke(3, listOf(Offset(0.3f, 0.5f), Offset(0.65f, 0.5f)), "Middle across"),
                LetterStroke(4, listOf(Offset(0.3f, 0.86f), Offset(0.75f, 0.86f)), "Bottom across")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.6f), Offset(0.72f, 0.6f)), "Line across"),
                LetterStroke(2, listOf(Offset(0.72f, 0.55f), Offset(0.48f, 0.35f), Offset(0.28f, 0.62f), Offset(0.5f, 0.88f), Offset(0.72f, 0.8f)), "Curve around")
            )
        ),
        LetterItem(
            charUpper = 'F',
            charLower = 'f',
            word = "Fox",
            phonicsSound = "/f/ /f/ Fox",
            emoji = "🦊",
            themeColor = Color(0xFFFF5722),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.3f, 0.14f), Offset(0.75f, 0.14f)), "Top across"),
                LetterStroke(3, listOf(Offset(0.3f, 0.5f), Offset(0.65f, 0.5f)), "Middle across")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.68f, 0.16f), Offset(0.48f, 0.12f), Offset(0.45f, 0.88f)), "Hook and down"),
                LetterStroke(2, listOf(Offset(0.3f, 0.45f), Offset(0.65f, 0.45f)), "Cross")
            )
        ),
        LetterItem(
            charUpper = 'G',
            charLower = 'g',
            word = "Giraffe",
            phonicsSound = "/dʒ/ /dʒ/ Giraffe",
            emoji = "🦒",
            themeColor = Color(0xFFFFB300),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.78f, 0.22f), Offset(0.5f, 0.12f), Offset(0.24f, 0.5f), Offset(0.5f, 0.88f), Offset(0.75f, 0.88f)), "Round curve"),
                LetterStroke(2, listOf(Offset(0.75f, 0.88f), Offset(0.75f, 0.55f)), "Up to middle"),
                LetterStroke(3, listOf(Offset(0.75f, 0.55f), Offset(0.52f, 0.55f)), "Inwards")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.68f, 0.42f), Offset(0.38f, 0.45f), Offset(0.32f, 0.65f), Offset(0.5f, 0.85f), Offset(0.68f, 0.72f)), "Circle head"),
                LetterStroke(2, listOf(Offset(0.68f, 0.35f), Offset(0.68f, 0.9f), Offset(0.45f, 0.98f)), "Tail hook")
            )
        ),
        LetterItem(
            charUpper = 'H',
            charLower = 'h',
            word = "Hippo",
            phonicsSound = "/h/ /h/ Hippo",
            emoji = "🦛",
            themeColor = Color(0xFF26A69A),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.28f, 0.12f), Offset(0.28f, 0.88f)), "Left down"),
                LetterStroke(2, listOf(Offset(0.72f, 0.12f), Offset(0.72f, 0.88f)), "Right down"),
                LetterStroke(3, listOf(Offset(0.28f, 0.5f), Offset(0.72f, 0.5f)), "Bridge across")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Tall down"),
                LetterStroke(2, listOf(Offset(0.3f, 0.5f), Offset(0.55f, 0.45f), Offset(0.72f, 0.58f), Offset(0.72f, 0.88f)), "Hop over")
            )
        ),
        LetterItem(
            charUpper = 'I',
            charLower = 'i',
            word = "Iguana",
            phonicsSound = "/ɪ/ /ɪ/ Iguana",
            emoji = "🦎",
            themeColor = Color(0xFF8BC34A),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.5f, 0.15f), Offset(0.5f, 0.85f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.32f, 0.15f), Offset(0.68f, 0.15f)), "Top hat"),
                LetterStroke(3, listOf(Offset(0.32f, 0.85f), Offset(0.68f, 0.85f)), "Bottom shoe")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.5f, 0.4f), Offset(0.5f, 0.88f)), "Little line down"),
                LetterStroke(2, listOf(Offset(0.5f, 0.22f), Offset(0.5f, 0.25f)), "Little dot")
            )
        ),
        LetterItem(
            charUpper = 'J',
            charLower = 'j',
            word = "Jellyfish",
            phonicsSound = "/dʒ/ /dʒ/ Jellyfish",
            emoji = "🪼",
            themeColor = Color(0xFFE91E63),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.6f, 0.15f), Offset(0.6f, 0.72f), Offset(0.48f, 0.88f), Offset(0.32f, 0.8f)), "Down and hook"),
                LetterStroke(2, listOf(Offset(0.4f, 0.15f), Offset(0.8f, 0.15f)), "Top across")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.55f, 0.38f), Offset(0.55f, 0.85f), Offset(0.42f, 0.95f), Offset(0.32f, 0.88f)), "Long hook down"),
                LetterStroke(2, listOf(Offset(0.55f, 0.22f), Offset(0.55f, 0.25f)), "Dot above")
            )
        ),
        LetterItem(
            charUpper = 'K',
            charLower = 'k',
            word = "Kangaroo",
            phonicsSound = "/k/ /k/ Kangaroo",
            emoji = "🦘",
            themeColor = Color(0xFF8D6E63),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.72f, 0.18f), Offset(0.3f, 0.52f)), "Kick in"),
                LetterStroke(3, listOf(Offset(0.3f, 0.52f), Offset(0.72f, 0.86f)), "Kick out")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Tall down"),
                LetterStroke(2, listOf(Offset(0.68f, 0.45f), Offset(0.3f, 0.65f)), "Slide in"),
                LetterStroke(3, listOf(Offset(0.3f, 0.65f), Offset(0.68f, 0.88f)), "Slide down")
            )
        ),
        LetterItem(
            charUpper = 'L',
            charLower = 'l',
            word = "Lion",
            phonicsSound = "/l/ /l/ Lion",
            emoji = "🦁",
            themeColor = Color(0xFFFF9800),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.32f, 0.12f), Offset(0.32f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.32f, 0.88f), Offset(0.75f, 0.88f)), "Across right")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.5f, 0.12f), Offset(0.5f, 0.88f)), "Tall straight line")
            )
        ),
        LetterItem(
            charUpper = 'M',
            charLower = 'm',
            word = "Monkey",
            phonicsSound = "/m/ /m/ Monkey",
            emoji = "🐵",
            themeColor = Color(0xFF6D4C41),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.2f, 0.88f), Offset(0.2f, 0.12f)), "Up left"),
                LetterStroke(2, listOf(Offset(0.2f, 0.12f), Offset(0.5f, 0.6f)), "Down to valley"),
                LetterStroke(3, listOf(Offset(0.5f, 0.6f), Offset(0.8f, 0.12f)), "Up to mountain"),
                LetterStroke(4, listOf(Offset(0.8f, 0.12f), Offset(0.8f, 0.88f)), "Down right")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.22f, 0.42f), Offset(0.22f, 0.88f)), "Down line"),
                LetterStroke(2, listOf(Offset(0.22f, 0.5f), Offset(0.48f, 0.42f), Offset(0.48f, 0.88f)), "First arch"),
                LetterStroke(3, listOf(Offset(0.48f, 0.5f), Offset(0.76f, 0.42f), Offset(0.76f, 0.88f)), "Second arch")
            )
        ),
        LetterItem(
            charUpper = 'N',
            charLower = 'n',
            word = "Nest",
            phonicsSound = "/n/ /n/ Nest",
            emoji = "🪺",
            themeColor = Color(0xFF009688),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.25f, 0.88f), Offset(0.25f, 0.12f)), "Straight up"),
                LetterStroke(2, listOf(Offset(0.25f, 0.12f), Offset(0.75f, 0.88f)), "Slide diagonal"),
                LetterStroke(3, listOf(Offset(0.75f, 0.88f), Offset(0.75f, 0.12f)), "Straight up")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.32f, 0.42f), Offset(0.32f, 0.88f)), "Down line"),
                LetterStroke(2, listOf(Offset(0.32f, 0.52f), Offset(0.55f, 0.42f), Offset(0.72f, 0.55f), Offset(0.72f, 0.88f)), "Hop over")
            )
        ),
        LetterItem(
            charUpper = 'O',
            charLower = 'o',
            word = "Owl",
            phonicsSound = "/ɒ/ /ɒ/ Owl",
            emoji = "🦉",
            themeColor = Color(0xFF5C6BC0),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.5f, 0.12f), Offset(0.22f, 0.5f), Offset(0.5f, 0.88f), Offset(0.78f, 0.5f), Offset(0.5f, 0.12f)), "Big round circle")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.5f, 0.4f), Offset(0.28f, 0.65f), Offset(0.5f, 0.88f), Offset(0.72f, 0.65f), Offset(0.5f, 0.4f)), "Small round circle")
            )
        ),
        LetterItem(
            charUpper = 'P',
            charLower = 'p',
            word = "Penguin",
            phonicsSound = "/p/ /p/ Penguin",
            emoji = "🐧",
            themeColor = Color(0xFF37474F),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.3f, 0.14f), Offset(0.72f, 0.28f), Offset(0.72f, 0.48f), Offset(0.3f, 0.52f)), "Round head")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.32f, 0.38f), Offset(0.32f, 0.98f)), "Long stem down"),
                LetterStroke(2, listOf(Offset(0.32f, 0.48f), Offset(0.7f, 0.48f), Offset(0.7f, 0.72f), Offset(0.32f, 0.78f)), "Circle head")
            )
        ),
        LetterItem(
            charUpper = 'Q',
            charLower = 'q',
            word = "Queen",
            phonicsSound = "/kw/ /kw/ Queen",
            emoji = "👑",
            themeColor = Color(0xFFAB47BC),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.5f, 0.12f), Offset(0.22f, 0.5f), Offset(0.5f, 0.88f), Offset(0.78f, 0.5f), Offset(0.5f, 0.12f)), "Big round circle"),
                LetterStroke(2, listOf(Offset(0.6f, 0.68f), Offset(0.85f, 0.92f)), "Little tail")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.65f, 0.45f), Offset(0.35f, 0.45f), Offset(0.35f, 0.75f), Offset(0.65f, 0.78f)), "Round tummy"),
                LetterStroke(2, listOf(Offset(0.65f, 0.38f), Offset(0.65f, 0.98f)), "Stem down")
            )
        ),
        LetterItem(
            charUpper = 'R',
            charLower = 'r',
            word = "Rabbit",
            phonicsSound = "/r/ /r/ Rabbit",
            emoji = "🐇",
            themeColor = Color(0xFFEC407A),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.12f), Offset(0.3f, 0.88f)), "Straight down"),
                LetterStroke(2, listOf(Offset(0.3f, 0.14f), Offset(0.72f, 0.28f), Offset(0.72f, 0.48f), Offset(0.3f, 0.52f)), "Round head"),
                LetterStroke(3, listOf(Offset(0.48f, 0.52f), Offset(0.75f, 0.88f)), "Kick leg")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.35f, 0.42f), Offset(0.35f, 0.88f)), "Stem down"),
                LetterStroke(2, listOf(Offset(0.35f, 0.55f), Offset(0.55f, 0.42f), Offset(0.72f, 0.48f)), "Little branch")
            )
        ),
        LetterItem(
            charUpper = 'S',
            charLower = 's',
            word = "Sun",
            phonicsSound = "/s/ /s/ Sun",
            emoji = "☀️",
            themeColor = Color(0xFFFFCA28),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.75f, 0.25f), Offset(0.5f, 0.12f), Offset(0.32f, 0.35f), Offset(0.68f, 0.65f), Offset(0.5f, 0.88f), Offset(0.28f, 0.78f)), "Snake slither")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.7f, 0.48f), Offset(0.5f, 0.38f), Offset(0.35f, 0.52f), Offset(0.65f, 0.72f), Offset(0.5f, 0.88f), Offset(0.3f, 0.82f)), "Small slither")
            )
        ),
        LetterItem(
            charUpper = 'T',
            charLower = 't',
            word = "Tiger",
            phonicsSound = "/t/ /t/ Tiger",
            emoji = "🐯",
            themeColor = Color(0xFFFF7043),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.2f, 0.15f), Offset(0.8f, 0.15f)), "Top roof"),
                LetterStroke(2, listOf(Offset(0.5f, 0.15f), Offset(0.5f, 0.88f)), "Straight down")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.48f, 0.18f), Offset(0.48f, 0.82f), Offset(0.62f, 0.88f)), "Down and turn"),
                LetterStroke(2, listOf(Offset(0.32f, 0.42f), Offset(0.68f, 0.42f)), "Cross arms")
            )
        ),
        LetterItem(
            charUpper = 'U',
            charLower = 'u',
            word = "Umbrella",
            phonicsSound = "/ʌ/ /ʌ/ Umbrella",
            emoji = "☂️",
            themeColor = Color(0xFF42A5F5),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.28f, 0.15f), Offset(0.28f, 0.65f), Offset(0.5f, 0.88f), Offset(0.72f, 0.65f), Offset(0.72f, 0.15f)), "Big smile curve")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.32f, 0.42f), Offset(0.32f, 0.72f), Offset(0.5f, 0.88f), Offset(0.68f, 0.72f), Offset(0.68f, 0.42f)), "Little cup"),
                LetterStroke(2, listOf(Offset(0.68f, 0.42f), Offset(0.68f, 0.88f)), "Tail down")
            )
        ),
        LetterItem(
            charUpper = 'V',
            charLower = 'v',
            word = "Volcano",
            phonicsSound = "/v/ /v/ Volcano",
            emoji = "🌋",
            themeColor = Color(0xFFEF5350),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.25f, 0.15f), Offset(0.5f, 0.88f)), "Slide down right"),
                LetterStroke(2, listOf(Offset(0.5f, 0.88f), Offset(0.75f, 0.15f)), "Slide up right")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.3f, 0.42f), Offset(0.5f, 0.88f)), "Little slide down"),
                LetterStroke(2, listOf(Offset(0.5f, 0.88f), Offset(0.7f, 0.42f)), "Little slide up")
            )
        ),
        LetterItem(
            charUpper = 'W',
            charLower = 'w',
            word = "Whale",
            phonicsSound = "/w/ /w/ Whale",
            emoji = "🐳",
            themeColor = Color(0xFF29B6F6),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.18f, 0.15f), Offset(0.32f, 0.88f)), "Down 1"),
                LetterStroke(2, listOf(Offset(0.32f, 0.88f), Offset(0.5f, 0.38f)), "Up 1"),
                LetterStroke(3, listOf(Offset(0.5f, 0.38f), Offset(0.68f, 0.88f)), "Down 2"),
                LetterStroke(4, listOf(Offset(0.68f, 0.88f), Offset(0.82f, 0.15f)), "Up 2")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.22f, 0.45f), Offset(0.35f, 0.88f)), "Down 1"),
                LetterStroke(2, listOf(Offset(0.35f, 0.88f), Offset(0.5f, 0.55f)), "Up 1"),
                LetterStroke(3, listOf(Offset(0.5f, 0.55f), Offset(0.65f, 0.88f)), "Down 2"),
                LetterStroke(4, listOf(Offset(0.65f, 0.88f), Offset(0.78f, 0.45f)), "Up 2")
            )
        ),
        LetterItem(
            charUpper = 'X',
            charLower = 'x',
            word = "Xylophone",
            phonicsSound = "/z/ /z/ Xylophone",
            emoji = "🎵",
            themeColor = Color(0xFFAB47BC),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.28f, 0.15f), Offset(0.72f, 0.85f)), "Diagonal down-right"),
                LetterStroke(2, listOf(Offset(0.72f, 0.15f), Offset(0.28f, 0.85f)), "Diagonal down-left")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.32f, 0.45f), Offset(0.68f, 0.85f)), "Slant down right"),
                LetterStroke(2, listOf(Offset(0.68f, 0.45f), Offset(0.32f, 0.85f)), "Slant down left")
            )
        ),
        LetterItem(
            charUpper = 'Y',
            charLower = 'y',
            word = "Yak",
            phonicsSound = "/j/ /j/ Yak",
            emoji = "🐂",
            themeColor = Color(0xFF8D6E63),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.25f, 0.15f), Offset(0.5f, 0.5f)), "Left branch in"),
                LetterStroke(2, listOf(Offset(0.75f, 0.15f), Offset(0.5f, 0.5f)), "Right branch in"),
                LetterStroke(3, listOf(Offset(0.5f, 0.5f), Offset(0.5f, 0.88f)), "Trunk down")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.32f, 0.42f), Offset(0.5f, 0.72f)), "Slant right"),
                LetterStroke(2, listOf(Offset(0.68f, 0.42f), Offset(0.35f, 0.98f)), "Long slant down left")
            )
        ),
        LetterItem(
            charUpper = 'Z',
            charLower = 'z',
            word = "Zebra",
            phonicsSound = "/z/ /z/ Zebra",
            emoji = "🦓",
            themeColor = Color(0xFF263238),
            upperStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.28f, 0.15f), Offset(0.72f, 0.15f)), "Top across"),
                LetterStroke(2, listOf(Offset(0.72f, 0.15f), Offset(0.28f, 0.85f)), "Zig down"),
                LetterStroke(3, listOf(Offset(0.28f, 0.85f), Offset(0.72f, 0.85f)), "Zag across")
            ),
            lowerStrokes = listOf(
                LetterStroke(1, listOf(Offset(0.32f, 0.45f), Offset(0.68f, 0.45f)), "Little top across"),
                LetterStroke(2, listOf(Offset(0.68f, 0.45f), Offset(0.32f, 0.85f)), "Little zig down"),
                LetterStroke(3, listOf(Offset(0.32f, 0.85f), Offset(0.68f, 0.85f)), "Little zag across")
            )
        )
    )

    fun getLetter(char: Char): LetterItem {
        return letters.firstOrNull { it.charUpper.equals(char, ignoreCase = true) } ?: letters.first()
    }
}
