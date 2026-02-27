package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.Components.RefreshCard
import com.example.chatapplication.Components.RoutineCard
import com.example.chatapplication.domain.model.Routine
import com.example.chatapplication.viewmodel.RoutinesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Routines(
    onRoutineStartClick: (Routine) -> Unit,
    onEditRoutine: (Routine) -> Unit,
    onDoneReloading: () -> Unit,
    modifier: Modifier = Modifier,
    shouldReload: Boolean = false,
    routinesViewModel: RoutinesViewModel = hiltViewModel(),
) {
    val routines = routinesViewModel.usersRoutines.collectAsState().value
    val isRefreshing = routinesViewModel.isRefreshing.collectAsState().value

    Column(modifier = modifier) {
        if (shouldReload) {
            RefreshCard()
        }
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { routinesViewModel.refreshRoutines(onDoneReloading) },
        ) {
            LazyColumn(
                modifier = modifier
            ) {
                items(routines) { routine ->
                    RoutineCard(
                        title = routine.name,
                        subtitle = routine.description ?: "",
                        onStartClick = { onRoutineStartClick(routine) },
                        onEditClick = { onEditRoutine(routine) },
                        onDeleteClick = { routinesViewModel.deleteRoutine(routine) },
                    )
                }
            }
        }
    }
}
