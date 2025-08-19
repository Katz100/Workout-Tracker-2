package com.example.chatapplication.data.repository

import com.example.chatapplication.data.dto.RoutineDto
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    suspend fun createNewRoutine(routine: Routine): NetworkResult<List<Routine>>
    suspend fun getAllRoutines(): NetworkResult<List<Routine>>
    val routineFlow: Flow<List<Routine>>
}