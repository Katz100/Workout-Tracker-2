package com.example.chatapplication.Nav

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
}