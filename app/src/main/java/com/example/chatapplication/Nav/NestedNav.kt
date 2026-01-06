package com.example.chatapplication.Nav

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.chatapplication.Components.CustomDialog
import com.example.chatapplication.NavigationScreens.AddExercise
import com.example.chatapplication.NavigationScreens.AddExercisesToNewRoutine
import com.example.chatapplication.NavigationScreens.AddRoutine
import com.example.chatapplication.NavigationScreens.EditExercise
import com.example.chatapplication.NavigationScreens.EditRoutine
import com.example.chatapplication.NavigationScreens.Exercises
import com.example.chatapplication.NavigationScreens.Routines
import com.example.chatapplication.NavigationScreens.WorkoutSession
import com.example.chatapplication.NavigationScreens.WorkoutSummary
import com.example.chatapplication.util.NavEvent
import com.example.chatapplication.viewmodel.NestedNavViewModel

@RequiresApi(Build.VERSION_CODES.O)
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
    val context = LocalContext.current
    val navEvent = remember { NavEvent(context) }
    val openAlertDialog = remember { mutableStateOf(false) }

    DisposableEffect(nestedNavController, navEvent) {
        nestedNavController.addOnDestinationChangedListener(navEvent)

        onDispose {
            nestedNavController.removeOnDestinationChangedListener(navEvent)
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
                                nestedNavController.condPopBackStack(screenTitle) {
                                    openAlertDialog.value = true
                                }
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
                    onEditRoutine = {
                        Log.i("NestedNav", "Edit routine: $it")
                        nestedNavController.navigate(Screen.EditRoutine(it.id.toString()))
                    }
                )
            }
            composable<Screen.WorkoutSession> { backStackEntry ->
                BackHandler(enabled = true) {
                    openAlertDialog.value = true
                }

                if (openAlertDialog.value) {
                    CustomDialog(
                        onDismissRequest = {
                            openAlertDialog.value = false
                        },
                        onConfirmation = {
                            openAlertDialog.value = false
                            // should navigate to post workout screen
                            nestedNavController.popBackStack()
                        },
                        dialogTitle = "End Workout",
                        icon = Icons.Default.Clear,
                        dialogText = "Are you sure you want to end your workout?"
                    )
                }
                WorkoutSession(
                    modifier = Modifier.fillMaxSize(),
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
                    onCompleted = {
                        nestedNavController.popBackStack()
                        nestedNavController.popBackStack()
                    }
                )
            }
            composable<Screen.Profile> {
                Text("Profile")
            }
            composable<Screen.Exercise> {
                Exercises(
                    onEditExercise = {
                        nestedNavController.navigate(Screen.EditExercise(it.id.toString()))
                    }
                )
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
            composable<Screen.WorkoutSummary> {
                WorkoutSummary()
            }
            composable<Screen.EditExercise> {
                EditExercise()
            }
            composable<Screen.EditRoutine> {
                EditRoutine(
                    onDone = {
                        nestedNavController.popBackStack()
                    }
                )
            }
        }
    }
}

fun NavController.condPopBackStack(
    route: String,
    onBlocked: () -> Unit,
) {
    if (route == "Workout Session") {
        Log.i("NESTEDNAV","Attempted to pop workout session")
        onBlocked()
    } else {
        Log.i("NESTEDNAV", "pop")
        popBackStack()
    }
}