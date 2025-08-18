package com.example.chatapplication.domain.model

data class RoutineExercise(
    val id: String? = null,
    val routineId: String,
    val exerciseId: String,
    val orderIndex: Int
)