package com.example.chatapplication.UiStates

import io.github.jan.supabase.auth.status.SessionStatus

data class AuthUiState(
    val session: SessionStatus = SessionStatus.Initializing
) {}
