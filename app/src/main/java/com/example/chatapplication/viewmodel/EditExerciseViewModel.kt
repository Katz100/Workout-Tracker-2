package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.chatapplication.Nav.Screen
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val exerciseArg = savedStateHandle.toRoute<Screen.EditExercise>()

    private val _exerciseToEdit = MutableStateFlow<Exercise>(
        Exercise(
            id = "-1",
            userId = "-1",
            "null",
            description = "",
            sets = 0,
            rest = 0,
            reps = listOf()
        )
    )
    val exerciseToEdit: StateFlow<Exercise> = _exerciseToEdit

    init {
        viewModelScope.launch {
            val response = exerciseRepository.getExerciseById(exerciseArg.exerciseId)

            if (response !is NetworkResult.Error && response.data != null) {
                Log.i("EditExerciseVM", "Received response: ${response.data}")
                _exerciseToEdit.value = response.data.first()
            }
        }
    }
}