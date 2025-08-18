package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chatapplication.Components.LogOutButton

@Composable
fun DevTestEnv(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onGetAllExercisesClicked: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            LogOutButton(
                onLogOut = onLogOut
            )

            Button(
                onClick = onAddExerciseClick
            ) {
                Text("Add test exercise")
            }

            Button(
                onClick = onGetAllExercisesClicked,
            ) {
                Text("Get all exercises")
            }
        }
    }
}