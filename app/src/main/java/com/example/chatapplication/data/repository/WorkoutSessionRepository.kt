package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.WorkoutSession

interface WorkoutSessionRepository {
    suspend fun createNewWorkoutSession(workoutSession: WorkoutSession): NetworkResult<List<WorkoutSession>>
}