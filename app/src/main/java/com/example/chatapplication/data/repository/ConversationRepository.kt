package com.example.chatapplication.data.repository

import com.example.chatapplication.data.dto.ConversationDto

interface ConversationRepository {
    suspend fun getConversation(id: String) : ConversationDto
}