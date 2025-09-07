package com.example.chatapplication.viewmodel

import com.example.chatapplication.R
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Timer
import com.example.chatapplication.TimerService
import com.example.chatapplication.data.repository.UsersRoutineExercisesRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.UsersRoutineExercises
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val usersRoutineExercisesRepository: UsersRoutineExercisesRepository
) : ViewModel() {

    private val _exerciseIndex = MutableStateFlow(0)
    val exerciseIndex: StateFlow<Int> = _exerciseIndex

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var timerState = TimerState.TIMER_STOP

    val restTime: StateFlow<Int> = Timer.restTime

    private val _usersExercises = MutableStateFlow<List<UsersRoutineExercises>>(listOf())
    val usersExercises: StateFlow<List<UsersRoutineExercises>> = _usersExercises

    private val _isResting = MutableStateFlow<Boolean>(false)
    val isResting: StateFlow<Boolean> = _isResting

    private val _currentSet = MutableStateFlow<Int>(1)
    val currentSet: StateFlow<Int> = _currentSet

    private val _currentExercise = MutableStateFlow<UsersRoutineExercises>(
        UsersRoutineExercises(
            exerciseName = "Empty",
            routineName = "",
            exerciseId = "",
            sets = 1,
            reps = listOf(12, 8, 6),
            rest = 120,
            orderIndex = 0
        )
    )
    val currentExercise: StateFlow<UsersRoutineExercises> = _currentExercise

    private var mediaPlayer: MediaPlayer? = null

    init {
        viewModelScope.launch {
            Timer.onTimerFinished.collect {
                playSound()
                onNextSet()
                stopTimer()
            }
        }
    }

    fun nextExercise(): String {
        return if (_exerciseIndex.value == _usersExercises.value.size - 1) "N/A" else _usersExercises.value[_exerciseIndex.value + 1].exerciseName
    }

    fun previousExercise(): String {
        return if (_exerciseIndex.value == 0) "N/A" else _usersExercises.value[_exerciseIndex.value - 1].exerciseName
    }

    fun playSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.beep2)
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    fun stopTimer() {
        timerState = TimerState.TIMER_STOP
        val intent = Intent(context, TimerService::class.java)
        context.stopService(intent)
        _isResting.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startTimer() {
        // Moves timer logic into foreground service so operations
        // survive while app is in the background
        _isResting.value = true
        timerState = TimerState.TIMER_ACTIVE
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("duration", _currentExercise.value.rest)
        }
        context.startForegroundService(intent)
    }

    fun formatDuration(seconds: Int): String {
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return "%02d:%02d".format(minutes, secs)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun loadRoutine(routineId: String) {
        viewModelScope.launch {
            val result = usersRoutineExercisesRepository.getUsersExercisesByRoutine(routineId)
            if (result !is NetworkResult.Error) {
                _usersExercises.value = result.data ?: emptyList()
                if (_usersExercises.value.isNotEmpty()) {
                    _currentExercise.value = _usersExercises.value[_exerciseIndex.value]
                }
                _isLoading.value = false
            }
        }
    }

    fun onPreviousSet() {
        stopTimer()
        if (usersExercises.value.isEmpty()) return

        _currentSet.value--
        if (_currentSet.value == 0) {
            if (_exerciseIndex.value != 0) {
                _exerciseIndex.value--
                _currentExercise.value = usersExercises.value[_exerciseIndex.value]
                _currentSet.value = _currentExercise.value.sets
            } else if (_exerciseIndex.value == 0 && _currentSet.value == 0) {
                _currentSet.value = 1
            } else {
                _currentSet.value = _currentExercise.value.sets
            }
        }
    }

    fun onNextSet() {
        stopTimer()
        _currentSet.value++
        Log.d(
            "WorkoutSessionVM",
            "Next set clicked → Set: ${_currentSet.value} / ${_currentExercise.value.sets}, " +
                    "ExerciseIndex: $exerciseIndex, Exercise: ${_currentExercise.value.exerciseName}"
        )

        if (_currentSet.value > _currentExercise.value.sets) {
            Log.d(
                "WorkoutSessionVM",
                "Completed exercise at index $exerciseIndex → ${_currentExercise.value.exerciseName}"
            )

            _currentSet.value = 1
            _exerciseIndex.value++

            if (_exerciseIndex.value < _usersExercises.value.size) {
                _currentExercise.value = _usersExercises.value[_exerciseIndex.value]
                Log.d(
                    "WorkoutSessionVM",
                    "Moving to next exercise at index $exerciseIndex → ${_currentExercise.value.exerciseName}"
                )
            } else {
                Log.d(
                    "WorkoutSessionVM",
                    "Workout finished! No more exercises. Last index: $exerciseIndex"
                )
            }
        }
    }
}

private enum class TimerState {
    TIMER_ACTIVE,
    TIMER_STOP,
}