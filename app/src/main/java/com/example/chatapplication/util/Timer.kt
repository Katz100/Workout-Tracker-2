package com.example.chatapplication.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Timer {
    val restTime = MutableStateFlow<Int>(0)

    // TODO: Change timerEventChannel to be StateFlow or SharedFlow
    // Using Channel can cause methods to suspend indefinitely
    // Needs to be received as a hot flow anyway so multiple sessions work properly.
    val timerEventChannel = Channel<TimerEvent>(1)

    private val _isResting = MutableStateFlow<Boolean>(false)
    val isResting: StateFlow<Boolean> = _isResting

     fun onTimerFinished() {
        timerEventChannel.trySend(TimerEvent.OnTimerFinished)
        endRest()
    }

     fun onStopTimer() {
        timerEventChannel.trySend(TimerEvent.OnStopTimer)
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