package com.example.chatapplication.data.repository.impl

import android.util.Log
import androidx.browser.trusted.Token
import com.example.chatapplication.data.repository.AuthenticationRepository
import com.example.chatapplication.domain.model.NetworkResult
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthenticationRepository {


    override suspend fun signIn(email: String, password: String): NetworkResult<Boolean> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Error(e.message, false)
        }
    }
    override suspend fun signUp(email: String, password: String): NetworkResult<Boolean> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Error(e.message, false)
        }
    }
    override suspend fun signInWithGoogle(token: String, rawNonce: String): NetworkResult<Boolean> {
        return try {
            Log.e("AUTH", "Logging in")
            auth.signInWith(IDToken) {
                idToken = token
                provider = Google
                nonce = rawNonce
            }
            Log.e("AUTHREPO", "Signing in.." + auth.currentUserOrNull().toString())
            NetworkResult.Success(true)
        } catch (e: Exception) {
            Log.e("AUTH", e.toString())
            NetworkResult.Error(e.message, false)
        }
    }

    override suspend fun signOut(): NetworkResult<Boolean> {
        return try {
            auth.signOut()
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }
}