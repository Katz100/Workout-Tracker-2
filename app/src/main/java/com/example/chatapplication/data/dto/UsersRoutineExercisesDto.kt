package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsersRoutineExercisesDto(
    @SerialName("exercise_name")
    val exerciseName: String,

    @SerialName("routine_name")
    val routineName: String,

    @SerialName("sets")
    val sets: Int,

    @SerialName("reps")
    val reps: List<Int>,

    @SerialName("order_index")
    val orderIndex: Int
)