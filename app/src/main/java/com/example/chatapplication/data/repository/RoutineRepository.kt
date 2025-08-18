package com.example.chatapplication.data.repository

import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine

interface RoutineRepository {
    suspend fun createNewRoutine(routine: Routine): NetworkResult<List<Routine>>
    suspend fun getAllRoutines(): NetworkResult<List<Routine>>
}