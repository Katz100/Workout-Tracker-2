package com.example.chatapplication.NavigationScreens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.viewmodel.EditRoutineViewModel
import sh.calvin.reorderable.ReorderableColumn

@Composable
fun EditRoutine(
    modifier: Modifier = Modifier,
    viewModel: EditRoutineViewModel = hiltViewModel()
) {
    val hapticFeedback = LocalHapticFeedback.current

    val isLoading = viewModel.isLoading.collectAsState().value
    val exerciseList = viewModel.exercises.collectAsState().value

    if (isLoading) {
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        ReorderableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            list = exerciseList,
            onSettle = { fromIndex, toIndex ->
                viewModel.reorderExercises(fromIndex, toIndex)
            },
            onMove = {
                hapticFeedback.performHapticFeedback(
                    HapticFeedbackType.TextHandleMove
                )
            },
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) { _, item, isDragging ->
            key(item) {
                ReorderableItem {
                    val elevation = animateDpAsState(
                        if (isDragging) 4.dp else 0.dp,
                        label = "dragElevation"
                    ).value

                    Surface(shadowElevation = elevation) {
                        Row {
                            Text(item.exerciseName, Modifier.padding(horizontal = 8.dp))
                            IconButton(
                                modifier = Modifier.draggableHandle(
                                    onDragStarted = {
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                    },
                                    onDragStopped = {
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.TextHandleMove
                                        )
                                    },
                                ),
                                onClick = {}
                            ) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    contentDescription = "Reorder"
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}