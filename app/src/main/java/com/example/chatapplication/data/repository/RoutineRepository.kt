package com.example.chatapplication.data.repository

import com.example.chatapplication.data.dto.RoutineDto
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    suspend fun createNewRoutine(routine: Routine): NetworkResult<List<Routine>>
    suspend fun getAllRoutines(): NetworkResult<List<Routine>>
    suspend fun deleteRoutine(routine: Routine): NetworkResult<PostgrestResult>
    val routineFlow: Flow<List<Routine>>
}