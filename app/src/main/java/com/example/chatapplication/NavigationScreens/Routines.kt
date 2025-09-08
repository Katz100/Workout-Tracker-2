package com.example.chatapplication.NavigationScreens

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
import com.example.chatapplication.domain.model.Routine
import com.example.chatapplication.viewmodel.RoutinesViewModel

@Composable
fun Routines(
    modifier: Modifier = Modifier,
    routinesViewModel: RoutinesViewModel = hiltViewModel(),
    onRoutineStartClick: (Routine) -> Unit,
) {
    val routines = routinesViewModel.usersRoutines.collectAsState().value
    val isEmpty = routinesViewModel.isEmpty.collectAsState().value

    if (isEmpty) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text("No routines found")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(routines) { routine ->
                RoutineCard(
                    title = routine.name,
                    subtitle = routine.description ?: "",
                    onStartClick = { onRoutineStartClick(routine) },
                    onEditClick = {},
                    onDeleteClick = { routinesViewModel.deleteRoutine(routine) },
                )
            }
        }
    }
}