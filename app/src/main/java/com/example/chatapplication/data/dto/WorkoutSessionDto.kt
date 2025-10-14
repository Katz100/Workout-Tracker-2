package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class WorkoutSessionDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("userId")
    val userId: String,

    @SerialName("routineId")
    val routineId: String,

    @SerialName("totalWorkoutTime")
    val totalWorkoutTime: Int,
)
