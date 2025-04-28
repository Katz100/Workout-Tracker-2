package com.example.chatapplication.data.repository

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun signInWithGoogle(token: String, rawNonce: String): Boolean
    fun getCurrentUser() : String
}