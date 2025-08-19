package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.UsersRoutineExercises

interface UsersRoutineExercisesRepository {
    suspend fun getUsersExercisesByRoutine(routineId: String): NetworkResult<List<UsersRoutineExercises>>
}