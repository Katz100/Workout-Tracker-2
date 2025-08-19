package com.example.chatapplication.Nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val icon: ImageVector,
    val label: String,
    val screen: Screen,
) {
    ROUTINE(
        icon = Icons.Rounded.Home,
        label = "Routine",
        screen = Screen.Routine,
    ),
    EXERCISE(
        icon = Icons.Rounded.Person,
        label = "Exercises",
        screen = Screen.Exercise
    ),
    HISTORY(
        icon = Icons.Rounded.Settings,
        label = "History",
        screen = Screen.History,
    ),
    PROFILE(
        icon = Icons.Rounded.Settings,
        label = "Profile",
        screen = Screen.Profile,
    ),
}