package com.example.chatapplication.NavigationScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.viewmodel.ExercisesViewModel

@Composable
fun Exercises(
    modifier: Modifier = Modifier,
    exercisesViewModel: ExercisesViewModel = hiltViewModel()
) {
    val exercises = exercisesViewModel.usersRoutines.collectAsState().value
    //val isEmpty = routinesViewModel.isEmpty.collectAsState().value

}