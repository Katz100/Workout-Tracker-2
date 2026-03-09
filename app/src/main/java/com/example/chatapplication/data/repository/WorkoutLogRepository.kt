package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.ExerciseSessionMaxWeight
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.WorkoutLog
import io.github.jan.supabase.postgrest.result.PostgrestResult

interface WorkoutLogRepository {
    suspend fun logWeightForSession(log: WorkoutLog): NetworkResult<PostgrestResult>
    suspend fun getMaxWeightsUsedForExercise(exerciseId: String): NetworkResult<List<ExerciseSessionMaxWeight>>
}