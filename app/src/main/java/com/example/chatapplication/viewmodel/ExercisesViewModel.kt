package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
): ViewModel() {
    private val _usersExercises = MutableStateFlow<List<Exercise>>(listOf())
    val usersRoutines: StateFlow<List<Exercise>> = _usersExercises

    private val _isEmpty = MutableStateFlow<Boolean>(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    init {
        viewModelScope.launch {
            _usersExercises.collect {
                _isEmpty.value = it.isEmpty()
            }
        }

        viewModelScope.launch {
            exerciseRepository.exerciseFlow.collect {
                Log.i("ExerciseVM", "Collected exercise: $it")
               _usersExercises.value = it
            }
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            val response = exerciseRepository.deleteExercise(exercise)

            if (response is NetworkResult.Success) {
                _usersExercises.value = exerciseRepository.getAllExercises().data!!
            }
        }
    }

}