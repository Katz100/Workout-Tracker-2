package com.example.chatapplication.NavigationScreens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.Components.RoutineCard
import com.example.chatapplication.viewmodel.ExercisesViewModel

@Composable
fun Exercises(
    modifier: Modifier = Modifier,
    exercisesViewModel: ExercisesViewModel = hiltViewModel()
) {
    val exercises = exercisesViewModel.usersRoutines.collectAsState().value
    val isEmpty = exercisesViewModel.isEmpty.collectAsState().value

    if (isEmpty) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text("No exercises found")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(exercises) { exercise ->
                RoutineCard(
                    title = exercise.name,
                    subtitle = exercise.description,
                    onStartClick = { /* no-op */ },
                    onDeleteClick = {
                        Log.i("Exercises", "Deleting exercise: $exercise")
                        exercisesViewModel.deleteExercise(exercise) },
                    onEditClick = {},
                    showButton = false,
                )
            }
        }
    }

}