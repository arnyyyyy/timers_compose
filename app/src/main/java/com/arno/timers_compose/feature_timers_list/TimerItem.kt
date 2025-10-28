package com.arno.timers_compose.feature_timers_list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arno.timers_compose.R
import com.arno.timers_compose.feature_store_timers.TimerEntity
import com.arno.timers_compose.utils.TimeFormatter.formatMillis
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimerItem(
        timer: TimerEntity,
        onClick: () -> Unit,
        onPlayPauseClick: () -> Unit
) {
        val progress =
                1f - (timer.remainingTimeMillis.toFloat() / timer.initialDurationMillis.toFloat())
        val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(300),
                label = "progress"
        )

        Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                ),
                modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onClick() }
        ) {
                Column(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                ) {
                        Text(
                                text = timer.name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = formatMillis(timer.remainingTimeMillis),
                                        style = MaterialTheme.typography.displayMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 42.sp,
                                                color = if (timer.isRunning)
                                                        MaterialTheme.colorScheme.primary
                                                else
                                                        MaterialTheme.colorScheme.tertiary,
                                                letterSpacing = (-0.5).sp
                                        )
                                )
                                Box(
                                        modifier = Modifier
                                                .size(56.dp)
                                                .clip(CircleShape)
                                                .clickable { onPlayPauseClick() }
                                                .background(
                                                        color = if (timer.isRunning) {
                                                                MaterialTheme.colorScheme.tertiary.copy(
                                                                        alpha = 0.85f
                                                                )
                                                        } else {
                                                                MaterialTheme.colorScheme.primary.copy(
                                                                        alpha = 0.85f
                                                                )
                                                        },
                                                        shape = CircleShape
                                                ),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Icon(
                                                imageVector = if (timer.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = if (timer.isRunning) stringResource(
                                                        R.string.pause
                                                ) else stringResource(R.string.start),
                                                tint = Color.White,
                                                modifier = Modifier.size(28.dp)
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                                Box(
                                        modifier = Modifier
                                                .fillMaxWidth(animatedProgress)
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(
                                                        if (timer.isRunning)
                                                                MaterialTheme.colorScheme.primary
                                                        else
                                                                MaterialTheme.colorScheme.primary.copy(
                                                                        alpha = 0.5f
                                                                )
                                                )
                                )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = "${(progress * 100).toInt()}% завершено",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                        )
                                )

                                Text(
                                        text = if (timer.isRunning && timer.lastStartedTime > 0) {
                                                val startTime = SimpleDateFormat(
                                                        "HH:mm",
                                                        Locale.getDefault()
                                                ).format(Date(timer.lastStartedTime))
                                                "Начат в $startTime"
                                        } else {
                                                "Всего ${formatMillis(timer.initialDurationMillis)}"
                                        },
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                        alpha = 0.7f
                                                ),
                                                fontSize = 12.sp
                                        )
                                )
                        }
                }
        }
}
