package com.example.chatapplication.domain.model

data class WorkoutLog(
    val id: String? = null,
    val sessionId: String,
    val exerciseId: String,
    val set: Int,
    val weightUsed: Int? = 0,
    val createdAt: String? = null,
)
