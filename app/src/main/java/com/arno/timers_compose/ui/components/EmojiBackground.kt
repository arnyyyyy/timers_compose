package com.arno.timers_compose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmojiBackground(modifier: Modifier = Modifier) {
        val emojis = listOf("🦊", "🦔", "🍂", "🌰")
        val spacing = 60.dp
        val emojiSize = 24.sp

        Box(modifier = modifier.fillMaxSize()) {
                for (row in 0..20) {
                        for (col in 0..8) {
                                val emojiIndex = ((row + col) % emojis.size)
                                val emoji = emojis[emojiIndex]

                                val offsetX = col * spacing.value
                                val offsetY = row * spacing.value

                                Text(
                                        text = emoji,
                                        fontSize = emojiSize,
                                        modifier = Modifier
                                            .offset(x = offsetX.dp, y = offsetY.dp)
                                            .alpha(0.08f)
                                )
                        }
                }
        }
}

