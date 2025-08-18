package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSessionDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("userId")
    val userId: String,

    @SerialName("routineId")
    val routineId: String,

    @SerialName("startedAt")
    val startedAt: String? = null,

    @SerialName("completedAt")
    val completedAt: String? = null
)
