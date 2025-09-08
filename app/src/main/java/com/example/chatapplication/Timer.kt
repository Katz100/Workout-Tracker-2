package com.example.chatapplication

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

object Timer {
    val restTime = MutableStateFlow<Int>(0)

    private val _onTimerFinished = MutableSharedFlow<Unit>()
    val onTimerFinished: SharedFlow<Unit> = _onTimerFinished

    private val _onStopTimer = MutableSharedFlow<Unit>()
    val onStopTimer: SharedFlow<Unit> = _onStopTimer

    suspend fun onTimerFinished() {
        _onTimerFinished.emit(Unit)
    }

    suspend fun onStopTimer() {
        _onStopTimer.emit(Unit)
    }
}