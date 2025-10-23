package com.example.chatapplication.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

object Timer {
    val restTime = MutableStateFlow<Int>(0)

    val timerEventChannel = Channel<TimerEvent>()

    private val _isResting = MutableStateFlow<Boolean>(false)
    val isResting: MutableStateFlow<Boolean> = _isResting

    suspend fun onTimerFinished() {
        timerEventChannel.send(TimerEvent.OnTimerFinished)
        endRest()
    }

    suspend fun onStopTimer() {
        timerEventChannel.send(TimerEvent.OnStopTimer)
        endRest()
    }

    fun endRest() {
        _isResting.value = false
    }

    fun beginRest() {
        _isResting.value = true
    }
}

sealed interface TimerEvent {
    data object OnStopTimer : TimerEvent
    data object OnTimerFinished : TimerEvent
}