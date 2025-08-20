package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.domain.model.Exercise
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
               _usersExercises.value = it
            }
        }
    }

}