package com.example.chatapplication

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

object Timer {
    val restTime = MutableStateFlow<Int>(0)

    private val _onTimerFinished = MutableSharedFlow<Unit>()
    val onTimerFinished: SharedFlow<Unit> = _onTimerFinished

    suspend fun onTimerFinished() {
        _onTimerFinished.emit(Unit)
    }
}