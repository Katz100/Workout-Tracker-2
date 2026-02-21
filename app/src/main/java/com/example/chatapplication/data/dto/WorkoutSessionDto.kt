package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class WorkoutSessionDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("userid")
    val userId: String,

    @SerialName("routineid")
    val routineId: String,

    @SerialName("totalworkouttime")
    val totalWorkoutTime: Int,
)
