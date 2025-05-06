package com.example.chatapplication.viewmodel

import android.R
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.UiStates.SignInUiState
import com.example.chatapplication.data.repository.AuthenticationRepository
import com.example.chatapplication.domain.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState

    private val _response = MutableStateFlow<NetworkResult<Boolean>?>(null)
    val response: StateFlow<NetworkResult<Boolean>?> = _response

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun onSignUp() {
        viewModelScope.launch {
            delay(1000)
            val response = authenticationRepository.signUp(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            _response.value = response
        }
    }

    fun onSignIn() {
        viewModelScope.launch {
            delay(1000)
            val response = authenticationRepository.signIn(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            _response.value = response
        }
    }

    fun onGoogleSignIn(token: String, rawNonce: String) {
        _response.value = NetworkResult.Loading()
        viewModelScope.launch {
            delay(1000)
            val response = authenticationRepository.signInWithGoogle(token, rawNonce)
            _response.value = response
        }
    }

    fun resetResponse() {
        _response.value = null
    }

}