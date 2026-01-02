package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.chatapplication.Nav.Screen
import com.example.chatapplication.data.repository.UsersRoutineExercisesRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.UsersRoutineExercises
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRoutineViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRoutinesExercisesRepository: UsersRoutineExercisesRepository,
) : ViewModel() {

    private val TAG = "EditRoutineVM"

    private val routineArg = savedStateHandle.toRoute<Screen.EditRoutine>()

    private val _exercises = MutableStateFlow<List<UsersRoutineExercises>>(emptyList())
    val exercises: StateFlow<List<UsersRoutineExercises>> = _exercises

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            val routineId = routineArg.routineId
            Log.i(TAG, "Getting routine with id: $routineId")
            val response = userRoutinesExercisesRepository.getUsersExercisesByRoutine(routineId)

            if (response !is NetworkResult.Error) {
                Log.i(TAG, "Successfully retrieved exercises from routine")
                val exerciseList = response.data
                exerciseList?.forEach {
                    Log.i(TAG, "Received exercise: ${it.exerciseName}")
                }
                _exercises.value = exerciseList!!
                _isLoading.value = false
            } else {
                Log.e(TAG, "Failed to get exercises for routine")
                _isLoading.value = false
            }
        }
    }

    fun reorderExercises(from: Int, to: Int) {
        _exercises.value.toMutableList().apply {
            add(to, removeAt(from))
        }
    }
}