package com.example.chatapplication.data.repository

import com.example.chatapplication.data.dto.MessageDto

interface MessageRepository {
    suspend fun getMessage(id: String) : MessageDto
    suspend fun sendMessage(message: MessageDto)
}