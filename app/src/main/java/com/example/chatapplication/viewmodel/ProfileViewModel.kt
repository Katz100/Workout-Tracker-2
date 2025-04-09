package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.dto.ProfileDto
import com.example.chatapplication.data.repository.ProfileRepository
import com.example.chatapplication.domain.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG2 = "ProfileModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    init {
       // Log.e(TAG2, "Init")
    }
    fun loadProfile(userId: String) {
        viewModelScope.launch {
            //Log.e(TAG2, "Loading profile")
            try {
                val dto = profileRepository.getProfile(userId)
                _profile.value = dto.asDomainModel()
               // Log.e(TAG2, _profile.value?.displayName ?: "Error")
            } catch (e: Exception) {
               // Log.e(TAG2, e.message.toString())
                _error.value = "Failed to load profile: ${e.message}"
            }
        }
    }

    private fun ProfileDto.asDomainModel() : Profile {
        return Profile(
            id = this.id,
            displayName = this.displayName
        )
    }

    fun updateDisplayName(userId: String, newName: String) {
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                profileRepository.updateDisplayName(userId, newName)
                // Update local state
                _profile.value = _profile.value?.copy(displayName = newName)
                // Log.e(TAG2, _profile.value?.displayName ?: "Error")
            } catch (e: Exception) {
                _error.value = "Failed to update display name: ${e.message}"
            } finally {
                _isUpdating.value = false
            }
        }
    }
}


