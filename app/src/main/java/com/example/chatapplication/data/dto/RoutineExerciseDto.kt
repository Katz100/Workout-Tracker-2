package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutineExerciseDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("routineid")
    val routineId: String,

    @SerialName("exerciseid")
    val exerciseId: String,

    @SerialName("orderindex")
    val orderIndex: Int
)
