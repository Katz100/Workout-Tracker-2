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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val usersRoutineExercisesRepository: UsersRoutineExercisesRepository
): ViewModel() {

    private var exerciseIndex = 0

    // TODO: convert to regular property
    private val _usersExercises = MutableStateFlow<List<UsersRoutineExercises>>(listOf())
    val usersExercises: StateFlow<List<UsersRoutineExercises>> = _usersExercises

    private val _currentSet = MutableStateFlow<Int>(1)
    val currentSet: StateFlow<Int> = _currentSet

    private val _currentExercise = MutableStateFlow<UsersRoutineExercises>(UsersRoutineExercises(
        exerciseName = "Empty",
        routineName = "",
        exerciseId = "",
        sets = 1,
        reps = listOf(12, 8, 6),
        rest = 120,
        orderIndex = 0
    ))
    val currentExercise: StateFlow<UsersRoutineExercises> = _currentExercise

    private var mediaPlayer: MediaPlayer? = null

    fun playSound(context: Context, soundResId: Int) {
        // Release any existing player before creating a new one
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
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

    fun onNextSet() {
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
                Log.d("WorkoutSessionVM", "Workout finished! No more exercises. Last index: $exerciseIndex")
            }
        }
    }
}