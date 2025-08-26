package com.example.chatapplication.viewmodel

import com.example.chatapplication.R
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.repository.UsersRoutineExercisesRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.UsersRoutineExercises
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val usersRoutineExercisesRepository: UsersRoutineExercisesRepository
) : ViewModel() {

    private var exerciseIndex = 0

    private var timerState = TimerState.TIMER_STOP

    private val _restTime = MutableStateFlow<Int>(0)
    val restTime: StateFlow<Int> = _restTime

    // TODO: convert to regular property
    private val _usersExercises = MutableStateFlow<List<UsersRoutineExercises>>(listOf())
    val usersExercises: StateFlow<List<UsersRoutineExercises>> = _usersExercises

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

    fun playSound() {
        // Release any existing player before creating a new one
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.beep2)
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    fun startTimer() {
        _restTime.value = _currentExercise.value.rest
        timerState = TimerState.TIMER_START
        viewModelScope.launch {
            while (_restTime.value != 0 && timerState != TimerState.TIMER_STOP) {
                delay(1.seconds)
                if (timerState == TimerState.TIMER_STOP) break
                _restTime.value--
            }
            if (timerState == TimerState.TIMER_START) {
                playSound()
                onNextSet()
            }
        }
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
                    exerciseIndex = 0
                    _currentExercise.value = _usersExercises.value[exerciseIndex]
                }
            }
        }
    }

    fun onPreviousSet() {
        timerState = TimerState.TIMER_STOP
        if (usersExercises.value.isEmpty()) return

        _currentSet.value--
        if (_currentSet.value == 0) {
            if (exerciseIndex != 0) {
                exerciseIndex--
                _currentExercise.value = usersExercises.value[exerciseIndex]
                _currentSet.value = _currentExercise.value.sets
            } else if (exerciseIndex == 0 && _currentSet.value == 0) {
                _currentSet.value = 1
            } else {
                _currentSet.value = _currentExercise.value.sets
            }
        }
    }

    fun onNextSet() {
        timerState = TimerState.TIMER_STOP
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
            exerciseIndex++

            if (exerciseIndex < _usersExercises.value.size) {
                _currentExercise.value = _usersExercises.value[exerciseIndex]
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
    TIMER_START,
    TIMER_STOP,
}