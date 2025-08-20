package com.example.chatapplication.Nav

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.NavigationScreens.Exercises
import com.example.chatapplication.NavigationScreens.Routines
import com.example.chatapplication.viewmodel.NestedNavViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedNav(
    rootNavController: NavHostController,
    nestedNavViewModel: NestedNavViewModel = hiltViewModel()
) {
    val topOfStack = nestedNavViewModel.topOfStack.collectAsState().value
    val currentScreen = nestedNavViewModel.currentScreen.collectAsState().value
    val nestedScreenName = nestedNavViewModel.nestedScreenName.collectAsState().value
    val currentDestination = rootNavController.currentBackStackEntryAsState().value?.destination
    val showAddIcon = nestedNavViewModel.showAddIcon.collectAsState().value

    val nestedNavController = rememberNavController()

    LaunchedEffect(nestedNavController) {
        snapshotFlow { nestedNavController.currentDestination }
            .collect { nestedNavViewModel.setTopOfStack(nestedNavController) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (topOfStack) {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(currentScreen.toString())
                            Spacer(modifier = Modifier.weight(1f))
                            if (showAddIcon) {
                                IconButton(
                                    onClick = { Log.i("NestedNav", "+ Clicked") }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add something")
                                }
                            }
                        }
                    },
                )
            } else {
                TopAppBar(
                    title = { Text(nestedScreenName) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                nestedNavController.popBackStack()
                            }
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(destination.screen::class)
                    } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) },
                        selected = selected,
                        onClick = {
                            nestedNavController.navigate(destination.screen) {
                                nestedNavViewModel.setScreen(destination.screen)
                                popUpTo(nestedNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = nestedNavController,
            startDestination = Screen.Routine
        ) {
            composable<Screen.Routine> {
                Routines(
                    onRoutineStartClick = {
                        Log.d("NestedNav", "Start routine: $it")
                    },
                    onMenuClick = {
                        Log.d("NestedNav", "Edit routine: $it")
                    },
                )
            }
            composable<Screen.Profile> {
                Text("Profile")
            }
            composable<Screen.Exercise> {
                Exercises()
            }
            composable<Screen.History> {
                Text("History")
            }
        }
    }
}