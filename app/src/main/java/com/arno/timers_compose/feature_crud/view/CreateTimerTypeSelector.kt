package com.arno.timers_compose.feature_crud.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arno.timers_compose.feature_crud.TimerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimerTypeSelector(
        selectedType: TimerType,
        onTypeSelected: (TimerType) -> Unit,
        modifier: Modifier = Modifier
) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(horizontal = 16.dp)
        ) {
                Text(
                        text = "Тип таймера",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                )

                val timerTypes = listOf(
                        TimerType.DAILY to "Ежедневный",
                        TimerType.WEEKLY to "Еженедельный",
                        TimerType.UNLIMITED to "Без ограничений"
                )

                var expanded by remember { mutableStateOf(false) }
                val selectedLabel =
                        timerTypes.find { it.first == selectedType }?.second ?: "Ежедневный"

                ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                        OutlinedTextField(
                                value = selectedLabel,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Выберите тип", color = Color.Gray) },
                                trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedLabelColor = Color.Gray,
                                        unfocusedLabelColor = Color.Gray,
                                        focusedTrailingIconColor = Color.White,
                                        unfocusedTrailingIconColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                        ) {
                                timerTypes.forEach { (type, label) ->
                                        DropdownMenuItem(
                                                text = { Text(label) },
                                                onClick = {
                                                        onTypeSelected(type)
                                                        expanded = false
                                                }
                                        )
                                }
                        }
                }
        }
}
