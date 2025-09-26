package com.arno.timers_compose.feature_timers_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arno.timers_compose.utils.TimeFormatter.formatMillis

@Composable
fun TimersListScreen(
        timers: List<Timer>,
        onTimerClick: (String) -> Unit,
        onAddClick: () -> Unit
) {
        val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                        Color(0xFF88CCFF).copy(alpha = 0.7f),
                        Color(0xFFCCDDFF).copy(alpha = 0.6f)
                )
        )

        Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                        FloatingActionButton(
                                onClick = onAddClick,
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                contentColor = Color.White,
                                shape = CircleShape
                        ) {
                                Icon(Icons.Default.Add, contentDescription = "Добавить таймер")
                        }
                }
        ) { innerPadding ->
                Box(
                        modifier = Modifier
                                .fillMaxSize()
                ) {
                        Box(
                                modifier = Modifier
                                        .fillMaxSize()
                                        .background(gradientBrush)
                        )

                        Text(
                                text = "Мои Таймеры",
                                modifier = Modifier
                                        .padding(WindowInsets.statusBars.asPaddingValues())
                                        .align(Alignment.TopCenter)
                                        .padding(top = 16.dp, bottom = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color(0xFF283458)
                        )

                        Box(
                                modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .padding(top = 48.dp)
                        ) {
                                if (timers.isEmpty()) {
                                        Card(
                                                modifier = Modifier
                                                        .padding(16.dp)
                                                        .align(Alignment.Center),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                        containerColor = Color.White.copy(alpha = 0.7f)
                                                ),
                                        ) {
                                                Column(
                                                        modifier = Modifier.padding(24.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                        Text(
                                                                text = "Нет таймеров",
                                                                style = MaterialTheme.typography.titleMedium,
                                                                textAlign = TextAlign.Center
                                                        )
                                                        Text(
                                                                text = "Нажмите + чтобы создать таймер",
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                textAlign = TextAlign.Center,
                                                                modifier = Modifier.padding(top = 8.dp)
                                                        )
                                                }
                                        }
                                } else {
                                        LazyColumn(
                                                contentPadding = PaddingValues(
                                                        horizontal = 16.dp,
                                                        vertical = 8.dp
                                                ),
                                                verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                                items(timers) { timer ->
                                                        TimerItem(
                                                                timer = timer,
                                                                onClick = { onTimerClick(timer.id) })
                                                }
                                        }
                                }
                        }
                }
        }
}

@Composable
fun TimerItem(timer: Timer, onClick: () -> Unit) {
        Card(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.7f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
        ) {
                Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Column(modifier = Modifier.weight(1f)) {
                                Text(
                                        text = timer.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF283458)
                                        )
                                )
                                Text(
                                        text = formatMillis(timer.remainingTimeMillis),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (timer.isRunning) MaterialTheme.colorScheme.primary else Color(
                                                        0xFF4F5B77
                                                )
                                        )
                                )
                        }

                        val buttonColor = if (timer.isRunning)
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f)
                        else
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)

                        Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(containerColor = buttonColor)
                        ) {
                                Box(
                                        modifier = Modifier
                                                .size(48.dp)
                                                .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Icon(
                                                imageVector = if (timer.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = if (timer.isRunning) "Пауза" else "Старт",
                                                tint = Color.White
                                        )
                                }
                        }
                }
        }
}
