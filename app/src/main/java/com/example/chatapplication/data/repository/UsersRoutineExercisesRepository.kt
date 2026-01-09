package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.UsersRoutineExercises
import io.github.jan.supabase.postgrest.result.PostgrestResult

interface UsersRoutineExercisesRepository {
    suspend fun getUsersExercisesByRoutine(routineId: String): NetworkResult<List<UsersRoutineExercises>>
    suspend fun updateExerciseWithNewOrder(
        routineId: String,
        exercises: List<UsersRoutineExercises>
    ): NetworkResult<PostgrestResult>
}