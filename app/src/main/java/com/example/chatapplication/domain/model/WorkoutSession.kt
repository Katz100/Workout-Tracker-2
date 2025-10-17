package com.example.chatapplication.domain.model

data class WorkoutSession (
    val id: String? = null,
    val userId: String,
    val routineId: String,
    val totalWorkoutTime: Int,
)
