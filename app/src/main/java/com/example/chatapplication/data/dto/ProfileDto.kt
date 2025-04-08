package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(

    @SerialName("id")
    val id: String,

    @SerialName("display_name")
    val displayName: String
)
