package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutLogDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("exerciseId")
    val exerciseId: String,

    @SerialName("set")
    val set: Int,

    @SerialName("weightUsed")
    val weightUsed: Int? = null,

    @SerialName("createdAt")
    val createdAt: String? = null
)
