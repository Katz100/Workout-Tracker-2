package com.example.chatapplication.data.repository.impl

import com.example.chatapplication.data.dto.ConversationDto
import com.example.chatapplication.data.dto.MessageDto
import com.example.chatapplication.data.repository.MessageRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : MessageRepository {
    override suspend fun getMessage(id: String): MessageDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("message").select(columns = Columns.ALL) {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<MessageDto>()
        }
    }
}