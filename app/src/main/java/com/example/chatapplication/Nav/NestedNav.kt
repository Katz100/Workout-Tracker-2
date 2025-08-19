package com.example.chatapplication.Nav

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

@Composable
fun NestedNav(
    rootNavController: NavHostController
) {
    val currentDestination = rootNavController.currentBackStackEntryAsState().value?.destination

    val nestedNavController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                Text("Routine")
            }
            composable<Screen.Profile> { backStackEntry ->
                Text("Profile")
            }
            composable<Screen.Exercise> {
                Text("Exercise")
            }
            composable<Screen.History> {
                Text("History")
            }
        }
    }
}