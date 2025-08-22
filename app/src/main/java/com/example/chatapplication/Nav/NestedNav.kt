package com.example.chatapplication.Nav

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.chatapplication.NavigationScreens.AddExercise
import com.example.chatapplication.NavigationScreens.AddExercisesToNewRoutine
import com.example.chatapplication.NavigationScreens.AddRoutine
import com.example.chatapplication.NavigationScreens.Exercises
import com.example.chatapplication.NavigationScreens.Routines
import com.example.chatapplication.NavigationScreens.WorkoutSession
import com.example.chatapplication.viewmodel.NestedNavViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedNav(
    rootNavController: NavHostController,
    nestedNavViewModel: NestedNavViewModel = hiltViewModel()
) {
    val currentDestination = rootNavController.currentBackStackEntryAsState().value?.destination
    val nestedNavController = rememberNavController()
    val currentRoute = nestedNavController.currentBackStackEntryAsState().value?.destination?.route
    val isTopLevel = currentRoute in Screen.topLevelScreens
    val screenTitle = Screen.getScreenTitle(currentRoute)

    LaunchedEffect(currentRoute) {
        if (currentRoute !in Screen.topLevelScreens) {
            nestedNavViewModel.setTopOfStack(false)
            nestedNavViewModel.setNestedScreenName(Screen.getScreenTitle(currentRoute))
        } else {
            nestedNavViewModel.setTopOfStack(true)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isTopLevel) {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(Screen.getScreenTitle(currentRoute))
                            Spacer(modifier = Modifier.weight(1f))

                            if (Screen.availableScreensForAdding.contains(currentRoute)) {
                                IconButton(
                                    onClick = {
                                        Log.i("NestedNav", "+ Clicked for screen: $currentRoute")
                                        if (currentRoute == Screen.Exercise::class.qualifiedName) {
                                            nestedNavController.navigate(Screen.AddExercise)
                                        } else if (currentRoute == Screen.Routine::class.qualifiedName) {
                                            nestedNavController.navigate(Screen.AddRoutine)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Exercise"
                                    )
                                }
                            }

                        }
                    },
                )
            } else {
                TopAppBar(
                    title = { Text(screenTitle) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                nestedNavController.popBackStack()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                        Log.i("NestedNav", "Start routine: $it")
                        nestedNavController.navigate(Screen.WorkoutSession(it.id.toString()))
                    },
                    onMenuClick = {
                        Log.i("NestedNav", "Edit routine: $it")
                    },
                )
            }
            composable<Screen.WorkoutSession> { backStackEntry ->
                val args = backStackEntry.toRoute<Screen.WorkoutSession>()
                WorkoutSession(
                    modifier = Modifier.fillMaxSize(),
                    routineId = args.routineId
                )
            }
            composable<Screen.AddRoutine> {
                AddRoutine(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    onNextButtonClicked = { name, desc ->
                        nestedNavController.navigate(Screen.AddExercisesToNewRoutine(name, desc))
                    }
                )
            }
            composable<Screen.AddExercisesToNewRoutine> { backStackEntry ->
                val args = backStackEntry.toRoute<Screen.AddExercisesToNewRoutine>()
                AddExercisesToNewRoutine(
                    routineName = args.name,
                    routineDesc = args.desc,
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
            composable<Screen.AddExercise> {
                AddExercise(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
            }
        }
    }
}