package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.UiStates.SignUpUiState
import com.example.chatapplication.data.repository.AuthenticationRepository
import com.example.chatapplication.domain.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

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

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(confirmPassword = confirmPassword)
        }
    }

    fun onDisplayNameChange(displayName: String) {
        _uiState.update {
            it.copy(displayName = displayName)
        }
    }


    fun onSignUp() {
        if (!passwordsMatch()) {
            _response.value = NetworkResult.Error("Passwords do not match", false)
        } else {
            viewModelScope.launch {
                val response = authenticationRepository.signUp(
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                    displayName = _uiState.value.displayName
                )

                _response.value = response
            }
        }
    }

    fun passwordsMatch() : Boolean {
        return _uiState.value.password == _uiState.value.confirmPassword
    }
}