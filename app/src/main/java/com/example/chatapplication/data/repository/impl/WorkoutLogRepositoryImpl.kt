package com.example.chatapplication.data.repository.impl

import com.example.chatapplication.data.repository.WorkoutLogRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.WorkoutLog
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import javax.inject.Inject

class WorkoutLogRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): WorkoutLogRepository {

    companion object {
        const val WORKOUT_LOG_TABLE = "workout_log"
    }

    override suspend fun logWeightForSession(log: WorkoutLog): NetworkResult<PostgrestResult> {
        TODO("Not yet implemented")
    }
}