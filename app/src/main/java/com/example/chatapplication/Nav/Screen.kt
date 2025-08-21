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
    @Serializable
    data object AddRoutine : Screen
    @Serializable
    data class AddExercisesToNewRoutine(val name: String, val desc: String) : Screen
    @Serializable
    data class WorkoutSession(val routineId: String) : Screen

    companion object {
        fun getScreenTitle(destination: String?): String {
            Log.d("ScreenDomain", "Destination: $destination")
            return when {
                destination == Routine::class.qualifiedName -> "Routines"
                destination == Exercise::class.qualifiedName -> "Exercises"
                destination == History::class.qualifiedName -> "History"
                destination == Profile::class.qualifiedName -> "Profile"
                destination == AddExercise::class.qualifiedName -> "Add Exercise"
                destination == AddRoutine::class.qualifiedName -> "Add Routine"
                destination?.startsWith(WorkoutSession::class.qualifiedName.toString()) == true -> "Workout Session"
                destination?.startsWith(AddExercisesToNewRoutine::class.qualifiedName.toString()) == true ->
                    "Add Exercises to Routine"
                else -> "Unknown"
            }
        }

        val availableScreensForAdding = listOf(
            Routine::class.qualifiedName,
            Exercise::class.qualifiedName,
        )
    }
}