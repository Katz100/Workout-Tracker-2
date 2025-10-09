package com.example.chatapplication.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

object Timer {
    val restTime = MutableStateFlow<Int>(0)

    private val _onTimerFinished = MutableSharedFlow<Unit>()
    val onTimerFinished: SharedFlow<Unit> = _onTimerFinished

    private val _onStopTimer = MutableSharedFlow<Unit>()
    val onStopTimer: SharedFlow<Unit> = _onStopTimer

    private val _isResting = MutableStateFlow<Boolean>(false)
    val isResting: MutableStateFlow<Boolean> = _isResting

    suspend fun onTimerFinished() {
        _onTimerFinished.emit(Unit)
        endRest()
    }

    suspend fun onStopTimer() {
        _onStopTimer.emit(Unit)
        endRest()
    }

    fun endRest() {
        _isResting.value = false
    }

    fun beginRest() {
        _isResting.value = true
    }
}