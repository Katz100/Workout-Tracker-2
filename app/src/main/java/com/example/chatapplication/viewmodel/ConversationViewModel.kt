package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.dto.ConversationDto
import com.example.chatapplication.data.dto.ProfileDto
import com.example.chatapplication.data.repository.ConversationRepository
import com.example.chatapplication.domain.model.Conversation
import com.example.chatapplication.domain.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {
    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: StateFlow<Conversation?> = _conversation

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private fun ConversationDto.asDomainModel() : Conversation {
        return Conversation(
            id = this.id,
            conversationName = this.conversationName,
            createdAt = this.createdAt
        )
    }

    fun loadConversation(conId: String) {
        viewModelScope.launch {
            //Log.e(TAG2, "Loading profile")
            try {
                val dto = conversationRepository.getConversation(conId)
                _conversation.value = dto.asDomainModel()
                // Log.e(TAG2, _profile.value?.displayName ?: "Error")
            } catch (e: Exception) {
                // Log.e(TAG2, e.message.toString())
                _error.value = "Failed to load conversation: ${e.message}"
            }
        }
    }
    private fun Conversation.asDto() : ConversationDto {
        return ConversationDto(
            id = this.id,
            conversationName = this.conversationName,
            createdAt = this.createdAt
        )
    }
    fun createConversation(conversation: Conversation) {
        viewModelScope.launch {
            try {
                conversationRepository.createConversation(conversation.asDto())
                _conversation.value = conversation
            } catch (e: Exception) {
                _error.value = "Failed to create conversation: ${e.message}"
            }
        }
    }
}