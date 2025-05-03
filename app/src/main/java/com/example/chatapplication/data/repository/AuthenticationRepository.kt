package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.NetworkResult

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun signInWithGoogle(token: String, rawNonce: String): NetworkResult<Boolean>
    fun getCurrentUser() : String
}