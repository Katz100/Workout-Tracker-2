package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSessionMaxWeightDto (
    @SerialName("exercise_name")
    val exerciseName: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("weight_used")
    val weightUsed: Int,
)
