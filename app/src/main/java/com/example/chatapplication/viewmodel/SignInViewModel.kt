package com.example.chatapplication.viewmodel

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password = _password

    init {
        _email.value = authenticationRepository.getCurrentUser()
    }
    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun getEmailValue(): String {
        return _email.value
    }

    fun onSignIn() {
        viewModelScope.launch {
            authenticationRepository.signIn(
                email = _email.value,
                password = _password.value
            )
        }
    }

    fun onGoogleSignIn(token: String, rawNonce: String) {
        viewModelScope.launch {
            authenticationRepository.signInWithGoogle(token, rawNonce)
            getCurrentUser()
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val currentUserEmail = authenticationRepository.getCurrentUser()
            _email.value = currentUserEmail
        }
    }

}