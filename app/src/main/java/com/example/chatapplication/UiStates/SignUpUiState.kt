package com.example.chatapplication.UiStates

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val displayName: String = ""
) {
}