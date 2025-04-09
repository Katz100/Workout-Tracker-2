package com.example.chatapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ConversationDto(

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val conversationName: String,

    @SerialName("created_at")
    val createdAt: String
) {
}