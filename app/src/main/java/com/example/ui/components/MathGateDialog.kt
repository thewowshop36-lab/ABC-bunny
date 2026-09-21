package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.BunnyCoral
import com.example.ui.theme.BunnyDarkText
import com.example.ui.theme.BunnyMeadowGreen
import com.example.ui.theme.BunnySkyBlue
import kotlin.random.Random

@Composable
fun MathGateDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val num1 = remember { Random.nextInt(3, 9) }
    val num2 = remember { Random.nextInt(2, 7) }
    val correctAnswer = num1 + num2

    val choices = remember {
        val wrong1 = correctAnswer + if (Random.nextBoolean()) 1 else -1
        val wrong2 = correctAnswer + if (Random.nextBoolean()) 2 else -2
        listOf(correctAnswer, wrong1, wrong2).shuffled()
    }

    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Parent Gate",
                            tint = BunnyCoral,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Grown-Ups Only",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BunnyDarkText
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("parent_gate_close")
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Please solve this quick problem to access Parent Settings & Progress Reports:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Math Equation Box
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF0F4F8), RoundedCornerShape(16.dp))
                        .padding(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "$num1 + $num2 = ?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = BunnySkyBlue
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Options
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    choices.forEach { answer ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.White, CircleShape)
                                .border(3.dp, BunnySkyBlue, CircleShape)
                                .clickable {
                                    if (answer == correctAnswer) {
                                        onSuccess()
                                    } else {
                                        showError = true
                                    }
                                }
                                .testTag("math_choice_$answer"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = answer.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = BunnyDarkText
                            )
                        }
                    }
                }

                if (showError) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Oops! Try again or ask an adult.",
                        color = BunnyCoral,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
