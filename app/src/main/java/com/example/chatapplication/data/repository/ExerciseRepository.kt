package com.example.chatapplication.data.repository

import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
   suspend fun addExercise(exerciseDto: ExerciseDto) : NetworkResult<PostgrestResult>
   suspend fun getAllExercises() : NetworkResult<List<Exercise>>
   suspend fun getExerciseById(exerciseId: String): NetworkResult<List<Exercise>>
   val exerciseFlow: Flow<List<Exercise>>
}