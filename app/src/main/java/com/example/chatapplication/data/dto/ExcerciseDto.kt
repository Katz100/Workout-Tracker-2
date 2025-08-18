package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("userid")
    val userId: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String = "",

    @SerialName("sets")
    val sets: Int = 3,

    @SerialName("reps")
    val reps: List<Int> = listOf(8, 10, 12),
)

