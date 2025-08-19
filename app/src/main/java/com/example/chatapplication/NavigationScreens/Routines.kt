package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.Components.RoutineCard
import com.example.chatapplication.viewmodel.RoutinesViewModel

@Composable
fun Routines(
    modifier: Modifier = Modifier,
    routinesViewModel: RoutinesViewModel = hiltViewModel()
) {
    val routines = routinesViewModel.usersRoutines.collectAsState().value

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(routines) { routine ->
            RoutineCard (
                title = routine.name,
                subtitle = routine.description ?: "",
                onStartClick = {
                    // TODO: navigate to routine detail/start workout
                },
                onMenuClick = {
                    // TODO: open menu actions (edit/delete/etc.)
                }
            )
        }
    }
}