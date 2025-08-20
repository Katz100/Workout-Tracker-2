package com.example.chatapplication.Nav

import android.util.Log
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Routine : Screen
    @Serializable
    data object Exercise : Screen
    @Serializable
    data object History : Screen
    @Serializable
    data object Profile : Screen
    @Serializable
    data object AddExercise : Screen

    companion object {
        fun getScreenTitle(destination: String?): String {
            Log.d("ScreenDomain", "Destination: $destination")
            return when (destination) {
                Routine::class.qualifiedName -> "Routines"
                Exercise::class.qualifiedName -> "Exercises"
                History::class.qualifiedName -> "History"
                Profile::class.qualifiedName -> "Profile"
                AddExercise::class.qualifiedName -> "Add Exercise"
                else -> "Unknown"
            }
        }

        val unavailableScreensForAdding = listOf(
            Screen.History::class.qualifiedName,
            Screen.Profile::class.qualifiedName,
            Screen.AddExercise::class.qualifiedName,
        )
    }
}