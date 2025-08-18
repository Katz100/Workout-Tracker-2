package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutineExerciseDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("routineId")
    val routineId: String,

    @SerialName("exerciseId")
    val exerciseId: String,

    @SerialName("orderIndex")
    val orderIndex: Int
)
