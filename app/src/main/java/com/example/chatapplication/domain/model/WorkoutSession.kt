package com.example.chatapplication.domain.model

import java.time.LocalDateTime

data class WorkoutSession (
    val id: String? = null,         // DB also has DEFAULT gen_random_uuid()
    val userId: String,
    val routineId: String,
    val totalWorkoutTime: Int,
)
