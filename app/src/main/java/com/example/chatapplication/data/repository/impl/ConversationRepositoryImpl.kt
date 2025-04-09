package com.example.chatapplication.data.repository.impl

import com.example.chatapplication.data.dto.ConversationDto
import com.example.chatapplication.data.dto.ProfileDto
import com.example.chatapplication.data.repository.ConversationRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : ConversationRepository {
    override suspend fun getConversation(id: String): ConversationDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("conversation").select(columns = Columns.list("id", "name", "created_at")) {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<ConversationDto>()
        }
    }
}