package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.dto.ConversationDto
import com.example.chatapplication.data.dto.MessageDto
import com.example.chatapplication.data.repository.MessageRepository
import com.example.chatapplication.domain.model.Conversation
import com.example.chatapplication.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _message = MutableStateFlow<Message?>(null)
    val message: StateFlow<Message?> = _message

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private fun MessageDto.asDomainModel() : Message {
        return Message(
            id = this.id,
            conversationId = this.conversationId,
            senderId = this.senderId,
            read = this.read,
            body = this.body,
            createdAt = this.createdAt
        )
    }

    fun loadMessage(id: String) {
        viewModelScope.launch {
            try {
                val dto = messageRepository.getMessage(id)
                _message.value = dto.asDomainModel()
            } catch (e: Exception) {
                _error.value = "Failed to load message: ${e.message}"
            }
        }
    }

}
