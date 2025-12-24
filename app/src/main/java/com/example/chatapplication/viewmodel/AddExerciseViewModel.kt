package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.data.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val _exerciseName = MutableStateFlow<String>("")
    val exerciseName: StateFlow<String> = _exerciseName

    private val _exerciseDescription = MutableStateFlow<String>("")
    val exerciseDescription: StateFlow<String> = _exerciseDescription

    private val _sets = MutableStateFlow<String>("")
    val sets: StateFlow<String> = _sets

    private val _rest = MutableStateFlow<String>("")
    val rest: StateFlow<String> = _rest

    private val _repsPerSet = MutableStateFlow<String>("")
    val repsPerSet: StateFlow<String> = _repsPerSet

    fun onExerciseNameChange(exerciseName: String) {
        _exerciseName.value = exerciseName
    }

    fun onExerciseDescriptionChange(desc: String) {
        _exerciseDescription.value = desc
    }

    fun onSetsChange(sets: String) {
        _sets.value = sets
    }

    fun onRestChange(rest: String) {
        _rest.value = rest
    }

    fun onRepsPerSetChange(repsPerSet: String) {
        _repsPerSet.value = repsPerSet
    }

    fun checkFieldsAreEmpty(): Boolean {
        return _exerciseName.value.isEmpty() ||
                _exerciseDescription.value.isEmpty() ||
                _rest.value.isEmpty() ||
                _repsPerSet.value.isEmpty() ||
                _sets.value.isEmpty()
    }

    // TODO: move to util
    fun convertStringToList(str: String): List<Int> {
        return str.split(",")
            .map { it.trim().toInt() }
    }

    fun onAddExerciseButtonClicked() {
        if (checkFieldsAreEmpty()) return
        val repsList = convertStringToList(_repsPerSet.value)
        val exercise = ExerciseDto(
            name = _exerciseName.value,
            description = _exerciseDescription.value,
            sets = _sets.value.toInt(),
            rest = _rest.value.toInt(),
            reps = repsList,
            userId = ""
        )
        Log.i("ADDEXERCISEVIEWMODEL", "Adding exercise: $exercise")
        viewModelScope.launch {
           val result = exerciseRepository.addExercise(exercise)
            Log.i("ADDEXERCISEVIEWMODEL", "Result: ${result.message}")
        }
    }
}