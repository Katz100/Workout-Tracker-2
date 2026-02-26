package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutLogDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("sessionid")
    val sessionId: String,

    @SerialName("exerciseid")
    val exerciseId: String,

    @SerialName("set")
    val set: Int,

    @SerialName("weightused")
    val weightUsed: Int? = null,

    @SerialName("createdat")
    val createdAt: String? = null
)
