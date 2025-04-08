package com.example.chatapplication.data.repository

import com.example.chatapplication.data.dto.ProfileDto

interface ProfileRepository {
    suspend fun getProfile(id: String): ProfileDto
    suspend fun updateDisplayName(id: String, newName: String)
}