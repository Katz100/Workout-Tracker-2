package com.example.chatapplication.NavigationScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.Components.CustomButton
import com.example.chatapplication.Components.ExerciseCard
import com.example.chatapplication.Components.RoutineCard
import com.example.chatapplication.domain.model.Routine
import com.example.chatapplication.viewmodel.AddExercisesToNewRoutineViewModel

@Composable
fun AddExercisesToNewRoutine(
    modifier: Modifier = Modifier,
    routineName: String,
    routineDesc: String,
    onCompleted: () -> Unit,
    viewModel: AddExercisesToNewRoutineViewModel = hiltViewModel(),
) {
    val isEmpty = viewModel.isEmpty.collectAsState().value
    val exercises = viewModel.usersExercises.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val context = LocalContext.current

    if (isEmpty) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text("No exercises found")
        }
    } else if (isLoading) {
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                items(exercises) { exercise ->
                    var checked = rememberSaveable { mutableStateOf(false) }
                    ExerciseCard(
                        title = exercise.name,
                        subtitle = exercise.description + "\n${exercise.sets}",
                        onCheckedChange = {
                            Log.i("ADDROUTINEEXERCISE", "Checked exercise: $exercise")
                            checked.value = it
                            if (it) {
                                viewModel.addExerciseToList(exercise)
                            } else {
                                viewModel.removeExerciseFromList(exercise)
                            }
                        },
                        checked = checked.value
                    )
                }
            }

            CustomButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    val routine = Routine (name = routineName, description = routineDesc)
                    viewModel.addNewRoutine(routine) {
                        onCompleted()
                        Toast.makeText(context, "Added exercises to new routine", Toast.LENGTH_SHORT).show()
                    }
                },
                text = "Save Routine",
                textModifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
    }
}
