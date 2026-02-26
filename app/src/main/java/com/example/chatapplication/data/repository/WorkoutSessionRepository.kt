package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.WorkoutSession
import io.github.jan.supabase.postgrest.result.PostgrestResult

interface WorkoutSessionRepository {
    suspend fun createNewWorkoutSession(workoutSession: WorkoutSession): NetworkResult<WorkoutSession>
    suspend fun updateTotalWorkoutTimeForSession(workoutSessionId: String, time: Int) : NetworkResult<PostgrestResult>
}