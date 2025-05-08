package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.NetworkResult

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): NetworkResult<Boolean>
    suspend fun signUp(email: String, password: String, displayName: String): NetworkResult<Boolean>
    suspend fun signInWithGoogle(token: String, rawNonce: String): NetworkResult<Boolean>
    suspend fun signOut() : NetworkResult<Boolean>
}