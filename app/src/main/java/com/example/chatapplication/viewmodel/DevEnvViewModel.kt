package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevEnvViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val _response = MutableStateFlow<NetworkResult<PostgrestResult>?>(null)
    val response: StateFlow<NetworkResult<PostgrestResult>?> = _response

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            val result = exerciseRepository.addExercise(exercise.asDto())
            Log.d("DevEnv", "Inserted exercise: ${result.data.toString()}")
            _response.value = result
        }
    }

    fun Exercise.asDto(): ExerciseDto {
        return ExerciseDto(
            id = this.id,
            userId = this.userId ?: "",
            name = this.name,
            description = this.description,
            sets = this.sets,
            reps = this.reps
        )
    }
}