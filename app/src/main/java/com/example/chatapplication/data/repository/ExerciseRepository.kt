package com.example.chatapplication.data.repository

import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.domain.model.NetworkResult
import io.github.jan.supabase.postgrest.result.PostgrestResult

interface ExerciseRepository {
   suspend fun addExercise(exerciseDto: ExerciseDto) : NetworkResult<PostgrestResult>
}