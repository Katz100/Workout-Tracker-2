package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.data.repository.RoutineRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExercisesToNewRoutineViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val routineRepository: RoutineRepository,
) : ViewModel() {
    private val _isEmpty = MutableStateFlow<Boolean>(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    private val _usersExercises = MutableStateFlow<List<Exercise>>(listOf())
    val usersExercises: StateFlow<List<Exercise>> = _usersExercises

    private val _checkedExercises = MutableStateFlow<List<Exercise>>(listOf())
    val checkExercises: StateFlow<List<Exercise>> = _checkedExercises

    init {
        viewModelScope.launch {
            _usersExercises.collect {
                _isEmpty.value = it.isEmpty()
            }
        }
        viewModelScope.launch {
            val result = exerciseRepository.getAllExercises()
            _usersExercises.value = result.data ?: emptyList()
        }
    }

    fun addExerciseToList(exercise: Exercise) {
        _checkedExercises.value += exercise
        _checkedExercises.value.onEach { Log.d("ADDVIEWMODEL", "Checked exercise: ${it.name}") }
    }

    fun removeExerciseFromList(exercise: Exercise) {
        _checkedExercises.value = _checkedExercises.value.filterNot { it == exercise }
        _checkedExercises.value.onEach { Log.d("ADDVIEWMODEL", "Unchecked exercise: ${it.name}") }
    }

    fun addNewRoutine(routine: Routine) {
        viewModelScope.launch {
            when (val result = routineRepository.createNewRoutine(routine)) {
                is NetworkResult.Success -> {
                    val createdRoutine = result.data?.firstOrNull()
                    val id = createdRoutine?.id ?: return@launch

                    _checkedExercises.value.forEachIndexed { index, item ->
                        val result = exerciseRepository.addExerciseToRoutine(id, item.id!!, index)
                        Log.d("ADDVIEWMODEL", "Adding exercise to routine result: $result")
                    }
                }

                is NetworkResult.Error -> {
                    Log.e("ADDVIEWMODEL", "Failed to create routine: ${result.message}")
                }

                is NetworkResult.Loading<*> -> TODO()
            }
        }
    }

}