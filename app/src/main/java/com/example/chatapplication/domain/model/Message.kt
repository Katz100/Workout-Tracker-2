package com.example.chatapplication.domain.model

import kotlinx.serialization.SerialName

data class Message(
    val id: String,

    val conversationId: String,

    val senderId: String,

    val body: String,

    val read: Boolean,

    val createdAt: String
)
