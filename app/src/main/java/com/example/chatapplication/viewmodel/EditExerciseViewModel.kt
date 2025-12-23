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

    private var exerciseToEdit =
        Exercise(
            id = "-1",
            userId = "-1",
            "null",
            description = "",
            sets = 0,
            rest = 0,
            reps = listOf()
        )

    val _exerciseName = MutableStateFlow<String>("")
    val exerciseName: StateFlow<String> = _exerciseName

    val _exerciseDescription = MutableStateFlow<String>("")
    val exerciseDescription: StateFlow<String> = _exerciseDescription

    val _exerciseSets = MutableStateFlow<String>("")
    val exerciseSets: StateFlow<String> = _exerciseSets

    val _exerciseRest = MutableStateFlow<String>("")
    val exerciseRest: StateFlow<String> = _exerciseRest

    val _exerciseReps = MutableStateFlow<String>("")
    val exerciseReps: StateFlow<String> = _exerciseReps

    init {
        viewModelScope.launch {
            val response = exerciseRepository.getExerciseById(exerciseArg.exerciseId)

            if (response !is NetworkResult.Error && response.data != null) {
                Log.i("EditExerciseVM", "Received response: ${response.data}")
                exerciseToEdit = response.data.first()
                onNameChange(exerciseToEdit.name)
                onDescChange(exerciseToEdit.description)
                onSetsChange(exerciseToEdit.sets.toString())
                onRestChange(exerciseToEdit.rest.toString())
                onRepsChange(exerciseToEdit.reps.joinToString(", ")
                )
            }
        }
    }

    fun onNameChange(value: String) {
        _exerciseName.value = value
    }

    fun onDescChange(value: String) {
        _exerciseDescription.value = value
    }

    fun onSetsChange(value: String) {
        _exerciseSets.value = value
    }

    fun onRestChange(value: String) {
        _exerciseRest.value = value
    }

    fun onRepsChange(value: String) {
        _exerciseReps.value = value
    }
}