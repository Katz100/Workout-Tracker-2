package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutineDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("userid")
    val userId: String? = null,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null
)
