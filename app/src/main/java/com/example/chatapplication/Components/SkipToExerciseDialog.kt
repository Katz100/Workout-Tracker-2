package com.example.chatapplication.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chatapplication.domain.model.UsersRoutineExercises

@Composable
fun SkipToExerciseDialog(
    onDismissRequest: () -> Unit,
    exercises: List<UsersRoutineExercises>,
    currentExercise: UsersRoutineExercises,
    onExerciseSelected: (UsersRoutineExercises) -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        // TODO: Add custom background color to colors.xml
        Card(
            modifier = Modifier.size(width = 260.dp, height = 400.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C2533)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Skip to exercise",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        onDismissRequest()
                    }
                )
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                exercises.forEach { exercise ->
                    if (exercise == currentExercise) {
                        ExerciseItem(
                            sets = exercise.sets,
                            exerciseName = exercise.exerciseName,
                            current = true,
                            onExerciseSelected = { onExerciseSelected(exercise) }
                        )
                    } else {
                        ExerciseItem(
                            sets = exercise.sets,
                            exerciseName = exercise.exerciseName,
                            onExerciseSelected = { onExerciseSelected(exercise) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SkipToExerciseDialogPreview() {
    SkipToExerciseDialog(
        onDismissRequest = {},
        exercises = listOf(
            UsersRoutineExercises(
                exerciseName = "Push Ups", sets = 3, reps = listOf(1, 2, 3), rest = 120,
                routineName = "Some Routine",
                exerciseId = "0",
                orderIndex = 2
            ),
            UsersRoutineExercises(
                exerciseName = "Push Ups", sets = 3, reps = listOf(1, 2, 3), rest = 120,
                routineName = "Some Routine",
                exerciseId = "0",
                orderIndex = 2
            ),
            UsersRoutineExercises(
                exerciseName = "Push Ups", sets = 3, reps = listOf(1, 2, 3), rest = 120,
                routineName = "Some Routine",
                exerciseId = "0",
                orderIndex = 2
            ),
            UsersRoutineExercises(
                exerciseName = "Push Ups", sets = 3, reps = listOf(1, 2, 3), rest = 120,
                routineName = "Some Routine",
                exerciseId = "0",
                orderIndex = 2
            ),
            UsersRoutineExercises(
                exerciseName = "Push Ups", sets = 3, reps = listOf(1, 2, 3), rest = 120,
                routineName = "Some Routine",
                exerciseId = "0",
                orderIndex = 2
            )
        ),
        currentExercise = UsersRoutineExercises(
            exerciseName = "Push Ups", sets = 3, reps = listOf(1, 2, 3), rest = 120,
            routineName = "Some Routine",
            exerciseId = "0",
            orderIndex = 2
        ),
        onExerciseSelected = {}
    )
}