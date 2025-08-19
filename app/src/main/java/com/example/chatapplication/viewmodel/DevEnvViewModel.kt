package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.data.repository.RoutineRepository
import com.example.chatapplication.data.repository.UsersRoutineExercisesRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine
import com.example.chatapplication.domain.model.UsersRoutineExercises
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevEnvViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val routineRepository: RoutineRepository,
    private val usersRoutineExercisesRepository: UsersRoutineExercisesRepository
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

    fun getExercises() {
        viewModelScope.launch {
            val result = exerciseRepository.getAllExercises()
            result.data?.onEach {
                Log.d("DevEnv", "Exercise: $it")
            }
        }
    }

    fun insertRoutine(routine: Routine) {
        viewModelScope.launch {
            val result = routineRepository.createNewRoutine(routine)
            result.data?.onEach {
                Log.d("DevEnv", "Routine added: $it")
            }
        }
    }

    fun getRoutines() {
        viewModelScope.launch {
            val result = routineRepository.getAllRoutines()
            result.data?.onEach {
                Log.d("DevEnv", "Routine: $it")
            }
        }
    }

    fun getUsersRoutineExercises(routineId: String) {
        viewModelScope.launch {
            val result = usersRoutineExercisesRepository.getUsersExercisesByRoutine(routineId)
            result.data?.onEach {
                Log.d("DevEnv", "Exercise for routine: $it")
                val exercise = exerciseRepository.getExerciseById(it.exerciseId)
                Log.d("DevEnv", "Get exercise by id: ${exercise.data}")
            }
        }
    }

    fun Exercise.asDto(): ExerciseDto {
        return ExerciseDto(
            id = this.id,
            userId = this.userId ?: "",
            name = this.name,
            description = this.description,
            sets = this.sets,
            reps = this.reps,
            rest = this.rest
        )
    }
}