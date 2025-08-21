package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.Components.CustomButton
import com.example.chatapplication.R
import com.example.chatapplication.viewmodel.WorkoutSessionViewModel

@Composable
fun WorkoutSession(
    modifier: Modifier = Modifier,
    routineId: String,
    viewModel: WorkoutSessionViewModel = hiltViewModel()
) {
    LaunchedEffect(routineId) {
        viewModel.loadRoutine(routineId)
    }
    val currentExerciseName = viewModel.currentExercise.collectAsState().value.exerciseName
    val currentSet = viewModel.currentSet.collectAsState().value
    val exerciseSetTotal = viewModel.currentExercise.collectAsState().value.sets
    val exerciseReps = viewModel.currentExercise.collectAsState().value.reps
    val exerciseRest = viewModel.currentExercise.collectAsState().value.rest

    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Workout in progress",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = currentExerciseName,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "$currentSet of $exerciseSetTotal",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Reps")
            Text(text = exerciseReps.toString())
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Rest")
            Text(text = exerciseRest.toString())
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(
                onClick = {
                    viewModel.playSound(context, R.raw.beep)
                },
                modifier = Modifier
                    .width(150.dp)
                    .padding(5.dp),
                text = "Start Rest",
                textModifier = Modifier,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                buttonColor = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomButton(
                onClick = { viewModel.onNextSet() },
                modifier = Modifier
                    .width(150.dp)
                    .padding(5.dp),
                text = "Next Set",
                textModifier = Modifier,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }

    }
}