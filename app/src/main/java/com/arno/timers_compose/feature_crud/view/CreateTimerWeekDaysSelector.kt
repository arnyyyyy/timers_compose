package com.arno.timers_compose.feature_crud.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CreateTimerWeekDaysSelector(
        selectedDays: List<String>,
        onDaysChanged: (List<String>) -> Unit,
        modifier: Modifier = Modifier
) {
        Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
        ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                ) {
                        val days = listOf(
                                "Mon",
                                "Tue",
                                "Wed",
                                "Thu",
                                "Fri",
                                "Sat",
                                "Sun"
                        )

                        days.forEach { day ->
                                val isSelected = selectedDays.contains(day)
                                Button(
                                        onClick = {
                                                val newList = selectedDays.toMutableList()
                                                if (isSelected) {
                                                        newList.remove(day)
                                                } else {
                                                        newList.add(day)
                                                }
                                                onDaysChanged(newList)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.LightGray,
                                                contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(40.dp),
                                        contentPadding = PaddingValues(0.dp)
                                ) {
                                        Text(text = day)
                                }
                        }
                }
        }
}
