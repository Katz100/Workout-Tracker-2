package com.example.chatapplication.Nav

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                    title = { Text(currentScreen.toString()) },
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
                nestedNavViewModel.setScreen(Screen.Routine)
                Routines(
                    onRoutineStartClick = {
                        Log.d("NestedNav", "Start routine: $it")
                    },
                    onMenuClick = {
                        Log.d("NestedNav", "Edit routine: $it")
                    },
                )
            }
            composable<Screen.Profile> { backStackEntry ->
                nestedNavViewModel.setScreen(Screen.Profile)
                Text("Profile")
            }
            composable<Screen.Exercise> {
                nestedNavViewModel.setScreen(Screen.Exercise)
                Text("Exercise")
            }
            composable<Screen.History> {
                nestedNavViewModel.setScreen(Screen.History)
                Text("History")
            }
        }
    }
}